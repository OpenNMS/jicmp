ACLOCAL_AMFLAGS = -I m4

classdir=classes
VPATH += $(classdir)

lib_LTLIBRARIES = libjrrd.la
libjrrd_la_SOURCES = rrd_jinterface.c
libjrrd_la_LDFLAGS = -module -avoid-version $(JAVA_SHREXT_COMMAND)

MAINTAINERCLEANFILES = aclocal.m4 config.guess config.sub configure depcomp install-sh ltmain.sh Makefile.in missing config.h.in *~ *.tar.gz
CLEANFILES = org_opennms_netmgt_rrd_rrdtool_Interface.h jrrd.jar

clean-local:
	-rm -rf $(classdir)

.java.class:
	-mkdir -p $(classdir)
	$(JAVAC) -d $(classdir) $<

rrd_jinterface.c: org_opennms_netmgt_rrd_rrdtool_Interface.h

org_opennms_netmgt_rrd_rrdtool_Interface.h: org/opennms/netmgt/rrd/rrdtool/Interface.class
	$(JAVAH) -classpath $(classdir) org.opennms.netmgt.rrd.rrdtool.Interface

jrrd.jar: org/opennms/netmgt/rrd/rrdtool/Interface.class
	cd $(classdir) && \
	$(JAR) cvf ../$@ *

jardir=$(datadir)/java
jar_DATA = jrrd.jar

deploy:
	mvn deploy:deploy-file

.PHONY: deploy rpm

if MAKE_RPMS
rpm: @RPM_TARGET@

EXTRA_DIST = jrrd.spec org/opennms/netmgt/rrd/rrdtool/Interface.java

$(RPM_TARGET): $(DISTFILES)
	${MAKE} dist
	-mkdir -p $(RPM_DIR)/SRPMS
	-mkdir -p `dirname $(RPM_TARGET)`
	$(RPMBUILD_PROG) $(RPM_ARGS) $(RPM_TARBALL)
	@echo "$(RPM_TARGET) created"
endif
