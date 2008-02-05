/** HEADER CHECKS **/

/* Define if you have the <architecture/byte_order.h> header file. */
#cmakedefine HAVE_ARCHITECTURE_BYTE_ORDER_H

/* Define if you have the <AvailabilityMacros.h> header file. */
#cmakedefine HAVE_AVAILABILITYMACROS_H

/* Define if you have the <byteswap.h> header file. */
#cmakedefine HAVE_BYTESWAP_H

/* Define if you have the <dlfcn.h> header file. */
#cmakedefine HAVE_DLFCN_H

/* Define if you have the <errno.h> header file. */
#cmakedefine HAVE_ERRNO_H

/* Define if you have the <intsafe.h> header file. */
#cmakedefine HAVE_INTSAFE_H

/* Define if you have the <inttypes.h> header file. */
#cmakedefine HAVE_INTTYPES_H

/* Define if your system has a GNU libc compatible `malloc' function, and
   to 0 otherwise. */
#cmakedefine HAVE_MALLOC_H

/* Define if you have the <memory.h> header file. */
#cmakedefine HAVE_MEMORY_H

/* Define if you have the <netdb.h> header file. */
#cmakedefine HAVE_NETDB_H

/* Define if you have the <netinet/in.h> header file. */
#cmakedefine HAVE_NETINET_IN_H

/* Define if you have the <netinet/in_systm.h> header file. */
#cmakedefine HAVE_NETINET_IN_SYSTM_H

/* Define if you have the <netinet/ip.h> header file. */
#cmakedefine HAVE_NETINET_IP_H

/* Define if you have the <netinet/ip_icmp.h> header file. */
#cmakedefine HAVE_NETINET_IP_ICMP_H

/* Define if you have the <stdint.h> header file. */
#cmakedefine HAVE_STDINT_H

/* Define if you have the <stdlib.h> header file. */
#cmakedefine HAVE_STDLIB_H

/* Define if you have the <strings.h> header file. */
#cmakedefine HAVE_STRINGS_H

/* Define if you have the <string.h> header file. */
#cmakedefine HAVE_STRING_H

/* Define if you have the <sys/byteorder.h> header file. */
#cmakedefine HAVE_SYS_BYTEORDER_H

/* Define if you have the <sys/socket.h> header file. */
#cmakedefine HAVE_SYS_SOCKET_H

/* Define if you have the <sys/stat.h> header file. */
#cmakedefine HAVE_SYS_STAT_H

/* Define if you have the <sys/time.h> header file. */
#cmakedefine HAVE_SYS_TIME_H

/* Define if you have the <sys/types.h> header file. */
#cmakedefine HAVE_SYS_TYPES_H

/* Define if you have the <unistd.h> header file. */
#cmakedefine HAVE_UNISTD_H

/* Define if you have the <win32/icmp.h> header file. */
#cmakedefine HAVE_WIN32_ICMP_H

/* Define if you have the <winsock2.h> header file. */
#cmakedefine HAVE_WINSOCK2_H

/* Define if you have the <ws2def.h> header file. */
#cmakedefine HAVE_WS2DEF_H

/* Define if you have the <ws2tcpip.h> header file. */
#cmakedefine HAVE_WS2TCPIP_H

/** BEHAVIOR/TYPE CHECKS **/

/* in_addr_t needed for IP headers */
#cmakedefine HAVE_IN_ADDR_T

#ifndef HAVE_IN_ADDR_T
# if defined(HAVE_WINSOCK2_H)
#  include <winsock2.h>
   typedef u_int in_addr_t;
# elif defined(HAVE_SYS_TYPES_H)
# include <sys/types.h>
  typedef u_int32_t in_addr_t;
# endif
#endif

/* struct icmp needed for IP headers */
#cmakedefine HAVE_STRUCT_ICMP

/* struct icmphdr needed for IP headers */
#cmakedefine HAVE_STRUCT_ICMPHDR

/* struct ip needed for IP headers */
#cmakedefine HAVE_STRUCT_IP

/* struct iphdr needed for IP headers */
#cmakedefine HAVE_STRUCT_IPHDR

/* the checksum entry in the ICMP struct */
#define ICMP_CHECKSUM ${CHECKSUM}

/* the type entry in the ICMP struct */
#define ICMP_TYPE ${TYPE}

/* the ihl entry in the IP struct */
#define ONMS_IP_HL ${HL}

/* Define if your processor stores words with the most significant byte
   first (like Motorola and SPARC, unlike Intel and VAX). */
#cmakedefine WORDS_BIGENDIAN

/* type to use in place of socklen_t if not defined -- is it necessary to test for this? */
#define onms_socklen_t socklen_t
