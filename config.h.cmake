/** HEADER CHECKS **/

/* Define to 1 if you have the <architecture/byte_order.h> header file. */
#define HAVE_ARCHITECTURE_BYTE_ORDER_H ${HAVE_ARCHITECTURE_BYTE_ORDER_H}

/* Define to 1 if you have the <AvailabilityMacros.h> header file. */
#define HAVE_AVAILABILITYMACROS_H ${HAVE_AVAILABILITYMACROS_H}

/* Define to 1 if you have the <byteswap.h> header file. */
#define HAVE_BYTESWAP_H ${HAVE_BYTESWAP_H}

/* Define to 1 if you have the <dlfcn.h> header file. */
#define HAVE_DLFCN_H ${HAVE_DLFCN_H}

/* Define to 1 if you have the <errno.h> header file. */
#define HAVE_ERRNO_H ${HAVE_ERRNO_H}

/* Define to 1 if you have the <inttypes.h> header file. */
#define HAVE_INTTYPES_H ${HAVE_INTTYPES_H}

/* Define to 1 if your system has a GNU libc compatible `malloc' function, and
   to 0 otherwise. */
#define HAVE_MALLOC_H ${HAVE_MALLOC_H}

/* Define to 1 if you have the <memory.h> header file. */
#define HAVE_MEMORY_H ${HAVE_MEMORY_H}

/* Define to 1 if you have the <netdb.h> header file. */
#define HAVE_NETDB_H ${HAVE_NETDB_H}

/* Define to 1 if you have the <netinet/in.h> header file. */
#define HAVE_NETINET_IN_H ${HAVE_NETINET_IN_H}

/* Define to 1 if you have the <netinet/in_systm.h> header file. */
#define HAVE_NETINET_IN_SYSTM_H ${HAVE_NETINET_IN_SYSTM_H}

/* Define to 1 if you have the <netinet/ip.h> header file. */
#define HAVE_NETINET_IP_H ${HAVE_NETINET_IP_H}

/* Define to 1 if you have the <netinet/ip_icmp.h> header file. */
#define HAVE_NETINET_IP_ICMP_H ${HAVE_NETINET_IP_ICMP_H}

/* Define to 1 if you have the <stdint.h> header file. */
#define HAVE_STDINT_H ${HAVE_STDINT_H}

/* Define to 1 if you have the <stdlib.h> header file. */
#define HAVE_STDLIB_H ${HAVE_STDLIB_H}

/* Define to 1 if you have the <strings.h> header file. */
#define HAVE_STRINGS_H ${HAVE_STRINGS_H}

/* Define to 1 if you have the <string.h> header file. */
#define HAVE_STRING_H ${HAVE_STRING_H}

/* Define to 1 if you have the <sys/byteorder.h> header file. */
#define HAVE_SYS_BYTEORDER_H ${HAVE_SYS_BYTEORDER_H}

/* Define to 1 if you have the <sys/socket.h> header file. */
#define HAVE_SYS_SOCKET_H ${HAVE_SYS_SOCKET_H}

/* Define to 1 if you have the <sys/stat.h> header file. */
#define HAVE_SYS_STAT_H ${HAVE_SYS_STAT_H}

/* Define to 1 if you have the <sys/time.h> header file. */
#define HAVE_SYS_TIME_H ${HAVE_SYS_TIME_H}

/* Define to 1 if you have the <sys/types.h> header file. */
#define HAVE_SYS_TYPES_H ${HAVE_SYS_TYPES_H}

/* Define to 1 if you have the <unistd.h> header file. */
#define HAVE_UNISTD_H ${HAVE_UNISTD_H}

/* Define to 1 if you have the <win32/icmp.h> header file. */
#define HAVE_WIN32_ICMP_H ${HAVE_WIN32_ICMP_H}

/* Define to 1 if you have the <winsock2.h> header file. */
#define HAVE_WINSOCK2_H ${HAVE_WINSOCK2_H}

/* Define to 1 if you have the <ws2tcpip.h> header file. */
#define HAVE_WS2TCPIP_H ${HAVE_WS2TCPIP_H}

/** BEHAVIOR CHECKS **/

/* struct icmp needed for IP headers */
#define HAVE_STRUCT_ICMP ${HAVE_STRUCT_ICMP_BOOL}

/* struct icmphdr needed for IP headers */
#define HAVE_STRUCT_ICMPHDR ${HAVE_STRUCT_ICMPHDR_BOOL}

/* struct ip needed for IP headers */
#define HAVE_STRUCT_IP ${HAVE_STRUCT_IP_BOOL}

/* struct iphdr needed for IP headers */
#define HAVE_STRUCT_IPHDR ${HAVE_STRUCT_IPHDR_BOOL}

/* the checksum entry in the ICMP struct */
#define ICMP_CHECKSUM ${CHECKSUM}

/* the type entry in the ICMP struct */
#define ICMP_TYPE ${TYPE}

/* the ihl entry in the IP struct */
#define ONMS_IP_HL ${HL}

/* Define to 1 if your processor stores words with the most significant byte
   first (like Motorola and SPARC, unlike Intel and VAX). */
#define WORDS_BIGENDIAN ${WORDS_BIGENDIAN}

/* type to use in place of socklen_t if not defined -- is it necessary to test for this? */
#define onms_socklen_t socklen_t
