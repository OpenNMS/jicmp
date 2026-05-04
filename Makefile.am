ACLOCAL_AMFLAGS = -I m4
AM_CPPFLAGS = $(JNI_INCLUDES) -I$(top_srcdir) -I$(top_builddir)
export TAR_OPTIONS = --owner=0 --group=0 --numeric-owner

JAVA_FILES = src/main/java/org/opennms/protocols/icmp/AddressMaskReply.java \
	src/main/java/org/opennms/protocols/icmp/AddressMaskRequest.java \
	src/main/java/org/opennms/protocols/icmp/ErrorMessage.java \
	src/main/java/org/opennms/protocols/icmp/ICMPEchoPacket.java \
	src/main/java/org/opennms/protocols/icmp/ICMPHeader.java \
	src/main/java/org/opennms/protocols/icmp/IcmpSocket.java \
	src/main/java/org/opennms/protocols/icmp/TimestampReply.java \
	src/main/java/org/opennms/protocols/icmp/TimestampRequest.java \
	src/main/java/org/opennms/protocols/ip/IPHeader.java \
	src/main/java/org/opennms/protocols/ip/IPv4Address.java \
	src/main/java/org/opennms/protocols/ip/OC16ChecksumProducer.java \
	src/main/java/org/opennms/protocols/ip/UnknownIPVersionException.java

classdir=classes
VPATH += $(classdir)

JICMP_SOURCES = byteswap.h IcmpSocket.h IcmpSocket.c
JICMP_LDFLAGS = -module -avoid-version -no-undefined $(JAVA_SHREXT_COMMAND) $(NOEXECSTACK_LDFLAGS)

if WIN32
lib_LTLIBRARIES = jicmp.la
jicmp_la_SOURCES = $(JICMP_SOURCES)
jicmp_la_LDFLAGS = $(JICMP_LDFLAGS)
jicmp_la_LIBADD = -lws2_32
else
lib_LTLIBRARIES = libjicmp.la
libjicmp_la_SOURCES = $(JICMP_SOURCES)
libjicmp_la_LDFLAGS = $(JICMP_LDFLAGS)
endif

TESTS_ENVIRONMENT = $(LIBTOOL) --mode=execute -dlopen $(top_builddir)/libjicmp.la
TESTS = tests.sh
MAINTAINERCLEANFILES = aclocal.m4 config.guess config.sub configure depcomp install-sh ltmain.sh Makefile.in missing config.h.in *~ *.tar.gz
CLEANFILES = org_*.h jicmp.jar docs-src/antora.yml public-lock.json

.PHONY: clean-local

clean-local:
	-rm -rf $(classdir) target java.built *.tar.gz public build package-lock.json node_modules

java-classes: java.built

java.built: $(JAVA_FILES) pom.xml
	-mkdir -p $(classdir)
	$(JAVAC) $(JAVACFLAGS) --release $(JAVA_TARGET_VERSION) -h . -d $(classdir) $(JAVA_FILES) && touch java.built

IcmpSocket.c: org_opennms_protocols_icmp_IcmpSocket.h

org_opennms_protocols_icmp_IcmpSocket.h: java.built

jicmp.jar: java.built
	cd $(classdir) && \
	$(JAR) cvf ../$@ *

jardir=$(datadir)/java
jar_DATA = jicmp.jar

node_modules/.bin/antora:
	npm i @antora/site-generator-default @antora/cli

DOCS_FILES = docs-src/antora.yml.in \
	docs-src/modules/*/*.adoc \
	docs-src/modules/*/pages/*.adoc

build/site.zip: node_modules/.bin/antora docs-src/antora.yml local-site.yml $(DOCS_FILES)
	node_modules/.bin/antora --stacktrace generate local-site.yml

target/jicmp-api-@VERSION@-javadoc.jar: java.built
	mvn javadoc:jar

docs: build/site.zip target/jicmp-api-@VERSION@-javadoc.jar

deploy:
	mvn deploy:deploy-file

.PHONY: deploy rpm srpm

EXTRA_DIST = ChangeLog $(JAVA_FILES) \
	tests.sh \
	tests/*.java \
	m4/* \
	debian/changelog \
	debian/compat \
	debian/control \
	debian/copyright \
	debian/dirs \
	debian/getjava \
	debian/rules \
	$(DOCS_FILES) \
	solaris/README \
	solaris/pkginfo* \
	solaris/Makefile \
	solaris/prototype.* \
	win32/*.txt \
	win32/*.h \
	win32/*.cpp \
	win32/jicmp.rc \
	win32/jicmp.sln \
	win32/jicmp.vcxproj* \
	win32/*.vdproj \
	win32/ReadMe.txt

if MAKE_RPMS
rpm: @RPM_TARGET@

srpm: @SRPM_TARGET@

print_rpm: @RPM_TARGET@
	@echo $(RPM_TARGET)

EXTRA_DIST += jicmp.spec

$(RPM_TARGET): $(DISTFILES)
	${MAKE} dist
	-mkdir -p $(SRPM_DIR)
	-mkdir -p `dirname $(RPM_TARGET)`
	$(RPMBUILD_PROG) --define 'version $(VERSION)' --define 'rel $(RELEASE)' $(RPM_ARGS) $(RPM_TARBALL)
	@echo "$(RPM_TARGET) created"

$(SRPM_TARGET): $(DISTFILES)
	${MAKE} dist
	-mkdir -p $(SRPM_DIR)
	$(RPMBUILD_PROG) --define 'version $(VERSION)' --define 'rel $(RELEASE)' $(SRPM_ARGS) $(RPM_TARBALL)
	@echo "$(SRPM_TARGET) created"
endif
