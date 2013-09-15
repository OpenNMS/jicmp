CC        = cc
CFLAGS32  =
CFLAGS64  =
LDFLAGS32 =
LDFLAGS64 =
ARGS32    =
ARGS64    =

TOP:sh    = pwd
TARGET    = $(TOP)/target
LIB       = $(TARGET)/lib

ARCH:sh   = uname -p
PROTOTYPE = prototype.$(ARCH)

GENERATED = pkginfo

package: jicmp.pkg

jicmp.pkg: check_prototype clean clean_source $(TARGET)
	( cd .. && CC=$(CC) CFLAGS="$(CFLAGS32)" LDFLAGS="$(LDFLAGS32)" ./configure --with-jvm-arch=32 --prefix="$(TARGET)" --libdir="$(LIB)/32" "$(ARGS32)" )
	( cd .. && make install )

	( cd .. && make distclean )

	( cd .. && CC=$(CC) CFLAGS="$(CFLAGS64)" LDFLAGS="$(LDFLAGS64)" ./configure --with-jvm-arch=64 --prefix="$(TARGET)" --libdir="$(LIB)/64" "$(ARGS64)" )
	( cd .. && make install )

	( cd .. && make solaris/pkginfo )

	find $(TARGET) -name '*.la' -print | xargs rm

	pkgmk -o -r $(TOP) -f $(PROTOTYPE)

	pkgtrans /var/spool/pkg $(TOP)/jicmp.pkg NMSjicmp

$(TARGET):
	mkdir $(TARGET)

check_prototype:
	@if [ ! -f $(PROTOTYPE) ]; then \
		echo "$(PROTOTYPE) does not exist.  this architecture ($(ARCH)) does not seem to be supported."; \
		exit 1 ; \
	fi

clean:
	-rm -r $(TARGET)
	-rm jicmp.pkg
	-rm $(GENERATED)

clean_source:
	@if [ -f ../Makefile ]; then \
		( cd .. && make distclean ) || exit 1; \
	fi

