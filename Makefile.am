ACLOCAL_AMFLAGS = -I m4
AM_CPPFLAGS = $(JNI_INCLUDES) -I$(top_srcdir) -I$(top_builddir)

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
JICMP_LDFLAGS = -module -avoid-version -no-undefined $(JAVA_SHREXT_COMMAND)

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
CLEANFILES = org_*.h jicmp.jar

.PHONY: clean-local

clean-local:
	-rm -rf $(classdir) target java.built *.tar.gz

java-classes: java.built

java.built: $(JAVA_FILES)
	-mkdir -p $(classdir)
	$(JAVAC) $(JAVACFLAGS) -source 1.5 -target 1.5 -d $(classdir) $(JAVA_FILES) && touch java.built

IcmpSocket.c: org_opennms_protocols_icmp_IcmpSocket.h

org_opennms_protocols_icmp_IcmpSocket.h: java.built
	$(JAVAH) -classpath $(classdir) org.opennms.protocols.icmp.IcmpSocket

jicmp.jar: java.built
	cd $(classdir) && \
	$(JAR) cvf ../$@ *

jardir=$(datadir)/java
jar_DATA = jicmp.jar

deploy:
	mvn deploy:deploy-file

.PHONY: deploy rpm

EXTRA_DIST = ChangeLog solaris/pkginfo $(JAVA_FILES) win32/icmp.h \
	tests.sh \
	tests/*.java \
	m4/* \
	debian/changelog \
	debian/compat \
	debian/control \
	debian/copyright \
	debian/dirs \
	debian/getjava \
	debian/rules

if MAKE_RPMS
rpm: @RPM_TARGET@

print_rpm: @RPM_TARGET@
	@echo $(RPM_TARGET)

EXTRA_DIST += jicmp.spec

$(RPM_TARGET): $(DISTFILES)
	${MAKE} dist
	-mkdir -p $(RPM_DIR)/SRPMS
	-mkdir -p `dirname $(RPM_TARGET)`
	$(RPMBUILD_PROG) --define 'version $(VERSION)' --define 'rel $(RELEASE)' $(RPM_ARGS) $(RPM_TARBALL)
	@echo "$(RPM_TARGET) created"
endif
