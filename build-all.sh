#!/bin/bash

PLATFORM="$1"; shift

set -e
set -o pipefail

VERSION="$(./configure --version | grep 'jicmp configure' | cut -d' ' -f3)"
REV=0

if [ -n "${CIRCLE_TAG}" ]; then
	REV=1
elif [ -n "${CIRCLE_BUILD_NUM}" ]; then
	REV="0.${CIRCLE_BUILD_NUM}"
fi

apt-get update
apt-get -y --no-install-recommends install \
	autotools-dev \
	build-essential \
	cdbs \
	debhelper \
	devscripts \
	dpkg-dev \
	gcc \
	gnupg2 \
	lsb-base \
	lsb-release \
	make \
	patchutils \
	rpm \
	ruby \
	selinux-policy-dev \
	software-properties-common \
	wget

apt-add-repository --yes 'deb http://security.debian.org/debian-security stretch/updates main'
apt-get update
apt-get -y install openjdk-8-jdk-headless

# build tarball and binaries from source
make clean >/dev/null 2>&1 || :
./configure
make libjicmp.la jicmp.jar dist

make dist
make -f /usr/share/selinux/devel/Makefile JICMP.pp
rm -f JICMP.pp.bz2
bzip2 -9 JICMP.pp

echo ':ssl_verify_mode: 0' > ~/.gemrc
gem install --no-document fpm

generatePackage() {
	_type="$1"; shift

	fpm -f -s dir \
		--name jicmp \
		--description "Java interface to ICMP (ping)" \
		--vendor "The OpenNMS Group, Inc." \
		--license "LGPLv3" \
		--maintainer "opennms@opennms.org" \
		--url "https://github.com/OpenNMS/jicmp" \
		--version "${VERSION}" \
		-t "${_type}" \
		-C "./tmp/dist/${_type}" \
		"$@"
}

# Debian Package

mkdir -p "tmp/dist/deb/usr/lib/jni" tmp/dist/deb/usr/share/java
install -c -m 644 .libs/libjicmp.so tmp/dist/deb/usr/lib/jni/
install -c -m 644 jicmp.jar         tmp/dist/deb/usr/share/java/

generatePackage deb \
	--iteration    "${REV}" \
	--deb-priority extra \
	--conflicts    libicmp-jni \
	--replaces     libicmp-jni

# RPM Package

LIBDIR=lib
if [ "${PLATFORM}" = "amd64" ]; then
	LIBDIR=lib64
fi

mkdir -p "tmp/dist/rpm/usr/${LIBDIR}" "tmp/dist/rpm/usr/share/java" "tmp/dist/rpm/usr/share/selinux/packages"
install -c -m 644 .libs/libjicmp.so "tmp/dist/rpm/usr/${LIBDIR}"
install -c -m 644 jicmp.jar         "tmp/dist/rpm/usr/share/java/"
install -c -m 644 JICMP.pp.bz2      "tmp/dist/rpm/usr/share/selinux/packages"

declare -a EXTRA_ARGS
case "${PLATFORM}" in
	i386)
		EXTRA_ARGS+=("-a" "i686")
		;;
	arm32v7)
		EXTRA_ARGS+=("-a" "armv7hl")
		;;
esac

generatePackage rpm \
	--iteration     "${REV}" \
	--after-install src/rpm/post.sh \
	--after-upgrade src/rpm/post.sh \
	--after-remove  src/rpm/postun.sh \
	--rpm-digest    sha256 \
	"${EXTRA_ARGS[@]}"

mkdir -p target
mv *.rpm *.deb target/
