/*
 This file is part of the OpenNMS(R) Application.

 OpenNMS(R) is Copyright (C) 2002-2007 The OpenNMS Group, Inc.  All rights reserved.
 OpenNMS(R) is a derivative work, containing both original code, included code and modified
 code that was published under the GNU General Public License. Copyrights for modified
 and included code are below.

 OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.

 Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

 For more information contact:
      OpenNMS Licensing       <license@opennms.org>
      http://www.opennms.org/
      http://www.opennms.com/


 Tab Size = 8
*/

#ifndef _ICMPSOCKET_H
#define _ICMPSOCKET_H

#ifdef HAVE_SYS_TYPES_H
#include <sys/types.h>
#endif

#ifdef HAVE_ERRNO_H
#include <errno.h>
#endif

#ifdef HAVE_STDIO_H
#include <stdio.h>
#endif

#ifdef HAVE_STDINT_H
#include <stdint.h>
#endif

#ifdef HAVE_STDLIB_H
#include <stdlib.h>
#endif

#ifdef HAVE_STRING_H
#include <string.h>
#endif

#ifdef HAVE_UNISTD_H
#include <unistd.h>
#endif

#ifdef HAVE_MALLOC_H
#include <malloc.h>
#endif

#ifdef HAVE_SYS_SOCKET_H
#include <sys/socket.h>
#endif

#ifdef HAVE_NETINET_IN_SYSTM_H
# include <netinet/in_systm.h>
#endif

#ifdef HAVE_AVAILABILITYMACROS_H
#include <AvailabilityMacros.h>
#endif

#ifdef HAVE_NETINET_IN_H
#include <netinet/in.h>
#endif

#ifdef HAVE_NETINET_IP_H
#include <netinet/ip.h>
#endif

#ifdef HAVE_NETINET_IP_ICMP_H
#include <netinet/ip_icmp.h>
#endif

#ifdef HAVE_NETDB_H
#include <netdb.h>
#endif

#ifdef HAVE_SYS_TIME_H
#include <sys/time.h>
#endif

#ifdef HAVE_SYS_BYTEORDER_H
#include <sys/byteorder.h>
#endif

#ifdef HAVE_BYTESWAP_H
#include <byteswap.h>
#else
#include "byteswap.h"
#endif

#ifdef HAVE_WINSOCK2_H

# ifdef HAVE_WS2DEF_H
#  include <ws2def.h>
# endif

# include <winsock2.h>

# ifdef HAVE_WS2TCPIP_H
#  include <ws2tcpip.h>
# endif

# include "win32/icmp.h"
# ifndef HAVE_STDINT_H
   typedef u_int in_addr_t;
   typedef u_int64 uint64_t;
# endif

# ifdef __MINGW32__
#  ifdef HAVE_STDINT_H
    typedef u_int in_addr_t;
#  endif
# else
   /* Visual Studio */
#  define close closesocket
#  define snprintf _snprintf
#  pragma warning(disable: 4996)
# endif
#endif

/**
 * Macros for doing byte swapping
 */


#ifdef HAVE_LIBKERN_OSBYTEORDER_H
# include <libkern/OSByteOrder.h>
# define ntohll(_x_) OSSwapBigToHostInt64(_x_)
# define htonll(_x_) OSSwapHostToBigInt64(_x_)

#elif HAVE_ARCHITECTURE_BYTE_ORDER_H
# include <architecture/byte_order.h>
# define ntohll(_x_) NXSwapBigLongLongToHost(_x_)
# define htonll(_x_) NXSwapHostLongLongToBig(_x_)

#elif defined(WORDS_BIGENDIAN)
#  define ntohll(_x_) (_x_)
#  define htonll(_x_) (_x_)

#elif defined(BSWAP_64)
#define ntohll(_x_) BSWAP_64(_x_)
#define htonll(_x_) BSWAP_64(_x_)

#elif defined(__bswap_64)
#define ntohll(_x_) __bswap_64(_x_)
#define htonll(_x_) __bswap_64(_x_)

#else
# define ntohll(_x_) bswap_64(_x_)
# define htonll(_x_) bswap_64(_x_)

#endif

#if defined(HAVE_STRUCT_IP)
typedef struct ip iphdr_t;
#elif defined(HAVE_STRUCT_IPHDR)
typedef struct iphdr iphdr_t;
#else
# error "not sure how to get an IP header struct on this platform!"
#endif

#if defined(HAVE_STRUCT_ICMP)
typedef struct icmp icmphdr_t;
#elif defined(HAVE_STRUCT_ICMPHDR)
typedef struct icmphdr icmphdr_t;
#else
# error "not sure how to get an ICMP header struct on this platform!"
#endif

/**
 * This macro is used to recover the current time
 * in milliseconds.
 */
#ifndef CURRENTTIMEMILLIS
#define CURRENTTIMEMILLIS(_dst_) \
{				\
	struct timeval tv;	\
	gettimeofday(&tv,NULL); \
	_dst_ = (uint64_t)tv.tv_sec * 1000UL + (uint64_t)tv.tv_usec / 1000UL; \
}
#endif

/**
 * This macro is used to recover the current time
 * in microseconds
 */
#ifndef CURRENTTIMEMICROS
#define CURRENTTIMEMICROS(_dst_) \
{				\
	struct timeval tv;	\
	gettimeofday(&tv,NULL); \
	_dst_ = (uint64_t)tv.tv_sec * 1000000UL + (uint64_t)tv.tv_usec; \
}
#endif

/**
 * converts microseconds to milliseconds
 */
#ifndef MICROS_TO_MILLIS
# define MICROS_TO_MILLIS(_val_) ((_val_) / 1000UL)
#endif

/**
 * convert milliseconds to microseconds.
 */
#ifndef MILLIS_TO_MICROS
# define MILLIS_TO_MICROS(_val_) ((_val_) * 1000UL)
#endif

/**
 * This constant specifies the length of a
 * time field in the buffer
 */
#ifndef TIME_LENGTH
# define TIME_LENGTH sizeof(uint64_t)
#endif

/**
 * Specifies the header offset and length
 */
#ifndef ICMP_HEADER_OFFSET
# define ICMP_HEADER_OFFSET 0
# define ICMP_HEADER_LENGTH 8
#endif

/**
 * specifies the offset of the sent time.
 */
#ifndef SENTTIME_OFFSET
# define SENTTIME_OFFSET (ICMP_HEADER_OFFSET + ICMP_HEADER_LENGTH)
#endif

/**
 * Specifies the offset of the received time.
 */
#ifndef RECVTIME_OFFSET
# define RECVTIME_OFFSET (SENTTIME_OFFSET + TIME_LENGTH)
#endif

/**
 * Specifies the offset of the thread identifer
 */
#ifndef THREADID_OFFSET
# define THREADID_OFFSET (RECVTIME_OFFSET + TIME_LENGTH)
#endif

/**
 * Specifies the offset of the round trip time
 */
#ifndef RTT_OFFSET
# define RTT_OFFSET (THREADID_OFFSET + TIME_LENGTH)
#endif

/**
 * specifies the magic tag and the offset/length of
 * the tag in the header.
 */
#ifndef OPENNMS_TAG
# define OPENNMS_TAG "OpenNMS!"
# define OPENNMS_TAG_LEN 8
# define OPENNMS_TAG_OFFSET (RTT_OFFSET + TIME_LENGTH)
#endif

/**
 * Winsock uses SOCKET, which is a special kind of Windows
 * HANDLE object, not just an int
 **/

#ifdef __WIN32__
#define onms_socket SOCKET
#else
#define onms_socket int
#define INVALID_SOCKET -1
#define SOCKET_ERROR -1
#endif

#endif // _ICMPSOCKET_H
