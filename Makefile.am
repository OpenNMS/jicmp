ACLOCAL_AMFLAGS = -I m4
AM_CPPFLAGS = $(JNI_INCLUDES) -I$(top_srcdir) -I$(top_builddir)

CLASS_FILES = org/opennms/protocols/icmp/AddressMaskReply.class \
	org/opennms/protocols/icmp/AddressMaskRequest.class \
	org/opennms/protocols/icmp/ErrorMessage.class \
	org/opennms/protocols/icmp/ICMPEchoPacket.class \
	org/opennms/protocols/icmp/ICMPHeader.class \
	org/opennms/protocols/icmp/IcmpSocket.class \
	org/opennms/protocols/icmp/TimestampReply.class \
	org/opennms/protocols/icmp/TimestampRequest.class \
	org/opennms/protocols/ip/IPHeader.class \
	org/opennms/protocols/ip/IPv4Address.class \
	org/opennms/protocols/ip/OC16ChecksumProducer.class \
	org/opennms/protocols/ip/UnknownIPVersionException.class

JAVA_FILES = org/opennms/protocols/icmp/AddressMaskReply.java \
	org/opennms/protocols/icmp/AddressMaskRequest.java \
	org/opennms/protocols/icmp/ErrorMessage.java \
	org/opennms/protocols/icmp/ICMPEchoPacket.java \
	org/opennms/protocols/icmp/ICMPHeader.java \
	org/opennms/protocols/icmp/IcmpSocket.java \
	org/opennms/protocols/icmp/TimestampReply.java \
	org/opennms/protocols/icmp/TimestampRequest.java \
	org/opennms/protocols/ip/IPHeader.java \
	org/opennms/protocols/ip/IPv4Address.java \
	org/opennms/protocols/ip/OC16ChecksumProducer.java \
	org/opennms/protocols/ip/UnknownIPVersionException.java

classdir=classes
VPATH += $(classdir)

lib_LTLIBRARIES = libjicmp.la
libjicmp_la_SOURCES = IcmpSocket.h IcmpSocket.c
libjicmp_la_LDFLAGS = -module -avoid-version $(JAVA_SHREXT_COMMAND)

MAINTAINERCLEANFILES = aclocal.m4 config.guess config.sub configure depcomp install-sh ltmain.sh Makefile.in missing config.h.in *~ *.tar.gz
CLEANFILES = org_*.h jicmp.jar

clean-local:
	-rm -rf $(classdir)

.java.class:
	-mkdir -p $(classdir)
	$(JAVAC) -d $(classdir) $<

IcmpSocket.c: org_opennms_protocols_icmp_IcmpSocket.h

org_opennms_protocols_icmp_IcmpSocket.h: org/opennms/protocols/icmp/IcmpSocket.class
	$(JAVAH) -classpath $(classdir) org.opennms.protocols.icmp.IcmpSocket

jicmp.jar: $(CLASS_FILES)
	cd $(classdir) && \
	$(JAR) cvf ../$@ *

jardir=$(datadir)/java
jar_DATA = jicmp.jar

deploy:
	mvn deploy:deploy-file

.PHONY: deploy rpm

if MAKE_RPMS
rpm: @RPM_TARGET@

EXTRA_DIST = jicmp.spec $(JAVA_FILES)

$(RPM_TARGET): $(DISTFILES)
	${MAKE} dist
	-mkdir -p $(RPM_DIR)/SRPMS
	-mkdir -p `dirname $(RPM_TARGET)`
	$(RPMBUILD_PROG) $(RPM_ARGS) $(RPM_TARBALL)
	@echo "$(RPM_TARGET) created"
endif
