#define HAVE_WIN32_ICMP_H 1
#define HAVE_WINSOCK2_H 1
#define HAVE_WS2TCPIP_H 1
#define HAVE_WINDOWS_H 1

#define HAVE_ERRNO_H 1
#define HAVE_SYS_TYPES_H 1
#define HAVE_STDIO_H 1
#define HAVE_STDINT_H 1
#define HAVE_STDLIB_H 1
#define HAVE_STRING_H 1

#define HAVE_STRUCT_IP 1
#define HAVE_STRUCT_ICMP 1
#define ICMP_CHECKSUM icmp_cksum
/* #define ICMP_CHECKSUM checksum */
#define ICMP_TYPE icmp_type
#define ONMS_IP_HL ip_hl
#define HAVE_STRUCT_IP_BOOL 1

#ifndef HAVE_IN_ADDR_T
# if defined(HAVE_WINSOCK2_H)
#  include <winsock2.h>
   typedef u_int in_addr_t;
# elif defined(HAVE_SYS_TYPES_H)
# include <sys/types.h>
  typedef u_int32_t in_addr_t;
# endif
#endif

#define onms_socklen_t socklen_t