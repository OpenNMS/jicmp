project(jicmp)

cmake_minimum_required(VERSION 2.4.3)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -I${CMAKE_CURRENT_SOURCE_DIR} -I${CMAKE_CURRENT_BINARY_DIR}")

set(CPACK_PACKAGE_DESCRIPTION_SUMMARY "Java ICMP Library")
set(CPACK_PACKAGE_VENDOR "The OpenNMS Group, Inc.")
set(CPACK_PACKAGE_DESCRIPTION_FILE "${CMAKE_CURRENT_SOURCE_DIR}/README.txt")
set(CPACK_RESOURCE_FILE_LICENSE "${CMAKE_CURRENT_SOURCE_DIR}/LICENSE.txt")
set(CPACK_PACKAGE_VERSION_MAJOR "1")
set(CPACK_PACKAGE_VERSION_MINOR "2")
set(CPACK_PACKAGE_VERSION_PATCH "1")
set(CPACK_PACKAGE_INSTALL_DIRECTORY "JICMP ${CPACK_PACKAGE_VERSION_MAJOR}.${CPACK_PACKAGE_VERSION_MINOR}")
set(CPACK_SOURCE_PACKAGE_FILE_NAME "jicmp-${CPACK_PACKAGE_VERSION_MAJOR}.${CPACK_PACKAGE_VERSION_MINOR}.${CPACK_PACKAGE_VERSION_PATCH}")

include(CheckIncludeFiles)
include(CheckTypeSize)
include(CMakeJavaInformation)
include(CMakeDetermineJavaCompiler)
include(TestBigEndian)
find_package(Java REQUIRED)
find_package(JNI REQUIRED)
get_filename_component(JAVABIN_PATH ${JAVA_COMPILE} PATH)
include(CPack)

include_directories("${JAVA_INCLUDE_PATH}" "${JAVA_INCLUDE_PATH}/win32" "${JAVA_INCLUDE_PATH}/linux")

check_include_files("architecture/byte_order.h"                                                  HAVE_ARCHITECTURE_BYTE_ORDER_H)
check_include_files("libkern/OSByteOrder.h"                                                      HAVE_LIBKERN_OSBYTEORDER_H)
check_include_files("AvailabilityMacros.h"                                                       HAVE_AVAILABILITYMACROS_H)
check_include_files("byteswap.h"                                                                 HAVE_BYTESWAP_H)
check_include_files("dlfcn.h"                                                                    HAVE_DLFCN_H)
check_include_files("errno.h"                                                                    HAVE_ERRNO_H)
check_include_files("intsafe.h"                                                                  HAVE_INTSAFE_H)
check_include_files("inttypes.h"                                                                 HAVE_INTTYPES_H)
check_include_files("malloc.h"                                                                   HAVE_MALLOC_H)
check_include_files("memory.h"                                                                   HAVE_MEMORY_H)
check_include_files("netdb.h"                                                                    HAVE_NETDB_H)
check_include_files("sys/types.h;netinet/in.h"                                                   HAVE_NETINET_IN_H)
check_include_files("sys/types.h;netinet/in_systm.h"                                             HAVE_NETINET_IN_SYSTM_H)
check_include_files("sys/types.h;netinet/in.h;netinet/ip.h"                                      HAVE_NETINET_IP_H)
check_include_files("sys/types.h;netinet/in.h;netinet/in_systm.h;netinet/ip.h;netinet/ip_icmp.h" HAVE_NETINET_IP_ICMP_H)
check_include_files("stdint.h"                                                                   HAVE_STDINT_H)
check_include_files("stdlib.h"                                                                   HAVE_STDLIB_H)
check_include_files("strings.h"                                                                  HAVE_STRINGS_H)
check_include_files("string.h"                                                                   HAVE_STRING_H)
check_include_files("sys/byteorder.h"                                                            HAVE_SYS_BYTEORDER_H)
check_include_files("sys/socket.h"                                                               HAVE_SYS_SOCKET_H)
check_include_files("sys/stat.h"                                                                 HAVE_SYS_STAT_H)
check_include_files("sys/time.h"                                                                 HAVE_SYS_TIME_H)
check_include_files("sys/types.h"                                                                HAVE_SYS_TYPES_H)
check_include_files("unistd.h"                                                                   HAVE_UNISTD_H)
check_include_files("winsock2.h"                                                                 HAVE_WINSOCK2_H)
check_include_files("ws2def.h"                                                                   HAVE_WS2DEF_H)
check_include_files("ws2tcpip.h"                                                                 HAVE_WS2TCPIP_H)

add_definitions(-D_JNI_IMPLEMENTATION_ -DDLL_EXPORT -DPIC)
if(WIN32)
	set(CMAKE_EXTRA_INCLUDE_FILES sys/types.h winsock2.h ws2tcpip.h win32/icmp.h)
	add_definitions(-D__WIN32__)
else(WIN32)
	set(CMAKE_EXTRA_INCLUDE_FILES sys/types.h netinet/in.h netinet/in_systm.h netinet/ip.h netinet/ip_icmp.h)
endif(WIN32)

check_type_size("in_addr_t"      IN_ADDR_T)
check_type_size("struct icmp"    STRUCT_ICMP)
check_type_size("struct icmphdr" STRUCT_ICMPHDR)
check_type_size("struct ip"      STRUCT_IP)
check_type_size("struct iphdr"   STRUCT_IPHDR)

set(CMAKE_EXTRA_INCLUDE_FILES)

if(HAVE_STRUCT_IP)
	set(HL       "ip_hl")
	set(TYPE     "icmp_type")
	set(CHECKSUM "icmp_cksum")
	set(HAVE_STRUCT_IP_BOOL 1)
else(HAVE_STRUCT_IP)
	if(HAVE_STRUCT_IPHDR)
		set(HL       "ihl")
		set(TYPE     "type")
		set(CHECKSUM "checksum")
		set(HAVE_STRUCT_IPHDR_BOOL 1)
	endif(HAVE_STRUCT_IPHDR)
endif(HAVE_STRUCT_IP)

test_big_endian(WORDS_BIGENDIAN)

set(JAVA_SOURCE_FILES
	org/opennms/protocols/icmp/AddressMaskReply.java
	org/opennms/protocols/icmp/AddressMaskRequest.java
	org/opennms/protocols/icmp/ErrorMessage.java
	org/opennms/protocols/icmp/ICMPEchoPacket.java
	org/opennms/protocols/icmp/ICMPHeader.java
	org/opennms/protocols/icmp/IcmpSocket.java
	org/opennms/protocols/icmp/TimestampReply.java
	org/opennms/protocols/icmp/TimestampRequest.java
	org/opennms/protocols/ip/IPHeader.java
	org/opennms/protocols/ip/IPv4Address.java
	org/opennms/protocols/ip/OC16ChecksumProducer.java
	org/opennms/protocols/ip/UnknownIPVersionException.java
)

foreach(FILE ${JAVA_SOURCE_FILES})
	string(REGEX REPLACE ".java$" ".class" CLASSFILE ${FILE})
	list(APPEND JAVA_CLASS_FILES "${CMAKE_CURRENT_BINARY_DIR}/${CLASSFILE}")
endforeach(FILE ${JAVA_SOURCE_FILES})

add_custom_command(OUTPUT ${JAVA_CLASS_FILES} COMMAND "${JAVABIN_PATH}/javac" -d ${CMAKE_CURRENT_BINARY_DIR} -classpath ${CMAKE_CURRENT_BINARY_DIR} ${JAVA_SOURCE_FILES} DEPENDS ${JAVA_SOURCE_FILES} WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR})

add_custom_command(OUTPUT "${CMAKE_CURRENT_BINARY_DIR}/org_opennms_protocols_icmp_IcmpSocket.h" COMMAND "${JAVABIN_PATH}/javah" -d ${CMAKE_CURRENT_BINARY_DIR} -classpath ${CMAKE_CURRENT_BINARY_DIR} org.opennms.protocols.icmp.IcmpSocket DEPENDS ${JAVA_CLASS_FILES} "${CMAKE_CURRENT_BINARY_DIR}/jicmp.jar" WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR})

configure_file(config.h.cmake "${CMAKE_CURRENT_BINARY_DIR}/config.h")
include_directories(${JAVA_INCLUDE_PATH})

add_library(jicmp MODULE IcmpSocket.c "${CMAKE_CURRENT_BINARY_DIR}/org_opennms_protocols_icmp_IcmpSocket.h")

if(APPLE)
	set_target_properties(jicmp PROPERTIES SUFFIX .jnilib)
endif(APPLE)

if(WIN32)
	set_target_properties(jicmp PROPERTIES PREFIX "")
	target_link_libraries(jicmp ws2_32)

	foreach(flag_var
		CMAKE_CXX_FLAGS
		CMAKE_CXX_FLAGS_DEBUG
		CMAKE_CXX_FLAGS_RELEASE
		CMAKE_CXX_FLAGS_MINSIZEREL
		CMAKE_CXX_FLAGS_RELWITHDEBINFO
	)
		if(${flag_var} MATCHES "/MD")
			string(REGEX REPLACE "/MD" "/MT" ${flag_var} "${${flag_var}}")
		endif(${flag_var} MATCHES "/MD")
	endforeach(flag_var)
endif(WIN32)

add_custom_command(OUTPUT "${CMAKE_CURRENT_BINARY_DIR}/jicmp.jar" COMMAND ${JAVA_ARCHIVE} -cvf jicmp.jar org DEPENDS ${JAVA_CLASS_FILES} WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR})

install(TARGETS jicmp DESTINATION lib)
install(FILES "${CMAKE_CURRENT_BINARY_DIR}/jicmp.jar" DESTINATION share/java RENAME "jicmp-${CPACK_PACKAGE_VERSION_MAJOR}.${CPACK_PACKAGE_VERSION_MINOR}.${CPACK_PACKAGE_VERSION_PATCH}.jar")
