#                                               -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

AC_PREREQ(2.59)
AC_INIT([jicmp], [1.0.2], [http://www.opennms.org])

RELEASE=1
AC_SUBST([RELEASE])

AC_CONFIG_MACRO_DIR([m4])
AC_CONFIG_HEADER([config.h])
AM_INIT_AUTOMAKE([1.9 foreign])
AC_ENABLE_STATIC([no])

# don't test c++ and f77 in libtool
m4_ifdef([AC_LIBTOOL_TAGS], [AC_LIBTOOL_TAGS([])])

# Checks for programs.
AC_PROG_CC
AC_PROG_LN_S
AC_PROG_LIBTOOL
AC_PROG_INSTALL
AC_C_BIGENDIAN
ONMS_CHECK_JDK([1.5])

CFLAGS="-Wall -Werror -m$JAVA_ARCH"

AM_RPM_INIT
AM_CONDITIONAL(MAKE_RPMS, test x$make_rpms = xtrue)

PRINT_VAR(JAVA_HOME)
PRINT_VAR(JAVA)
PRINT_VAR(JAVAC)
PRINT_VAR(JAVAH)
PRINT_VAR(JAR)
PRINT_VAR(JNI_INCLUDES)

AC_HEADER_STDC

# check for jni.h
CPPFLAGS="$CPPFLAGS $JNI_INCLUDES"
AC_CHECK_HEADER([jni.h], [], [AC_MSG_ERROR([cannot find jni.h header with CPPFLAGS="$CPPFLAGS"])])

AC_CHECK_HEADERS([AvailabilityMacros.h architecture/byte_order.h byteswap.h errno.h netdb.h sys/socket.h netinet/in.h netinet/ip.h netinet/ip_icmp.h netinet/in_systm.h sys/byteorder.h sys/time.h], [], [], [
#ifdef HAVE_SYS_SOCKET_H
# include <sys/socket.h>
#endif

#ifdef HAVE_NETINET_IN_H
# include <netinet/in.h>
#endif

#ifdef HAVE_NETINET_IP_H
# include <netinet/ip.h>
#endif
])

# check for socklen_t
ONMS_CHECK_SOCKLEN_T

# Checks for typedefs, structures, and compiler characteristics.
AC_C_CONST

# Checks for library functions.
AC_FUNC_MALLOC

ONMS_CHECK_IP_STRUCT([ip])
ONMS_CHECK_IP_STRUCT([iphdr])
ONMS_CHECK_IP_STRUCT([icmp],    [#include <netinet/ip_icmp.h>])
ONMS_CHECK_IP_STRUCT([icmphdr], [#include <netinet/ip_icmp.h] )

ONMS_CHECK_IP_STRUCT_ENTRY([HL], [ip_hl])
ONMS_CHECK_IP_STRUCT_ENTRY([HL], [ihl])
ONMS_CHECK_ICMP_STRUCT_ENTRY([TYPE], [icmp_type])
ONMS_CHECK_ICMP_STRUCT_ENTRY([TYPE], [type])
ONMS_CHECK_ICMP_STRUCT_ENTRY([CHECKSUM], [icmp_cksum])
ONMS_CHECK_ICMP_STRUCT_ENTRY([CHECKSUM], [checksum])

AC_CHECK_TYPE([struct timeval],
	[],
	[ AC_MSG_ERROR([cannot find struct timeval]) ],
	[ #include <sys/time.h> ]
)

gl_BYTESWAP

ARCH_PKG="$host_cpu"
AC_SUBST([ARCH_PKG])

UNAME_R="`uname -r`"
AC_SUBST([UNAME_R])
UNAME_S="`uname -s`"
AC_SUBST([UNAME_S])

AC_CONFIG_FILES([Makefile jicmp.spec pom.xml solaris/pkginfo])
AC_OUTPUT
