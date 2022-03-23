/*
 * This file is part of JICMP.
 *
 * JICMP is Copyright (C) 2002-2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 * 
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 * 
 * JICMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License, as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * JICMP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with JICMP. If not, see:
 *      http://www.gnu.org/licenses/
 * 
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.com>
 *     http://www.opennms.com/
 */

package org.opennms.protocols.icmp;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * This class provides a bridge between the host operating system so that ICMP
 * messages may be sent and received.
 * 
 * @author Brian Weaver
 * @author <a href="http://www.opennms.org/">OpenNMS</a>
 */
public final class IcmpSocket {
    private static final String LIBRARY_NAME = "jicmp";
    private static final String PROPERTY_NAME = "opennms.library.jicmp";
    private static final String LOGGER_PROPERTY_NAME = "opennms.logger.jicmp";

    public interface Logger {
        public void debug(String msg);
        public void info(String msg);
    }

    /**
     * This instance is used by the native code to save and store file
     * descriptor information about the icmp socket. This needs to be
     * constructed prior to calling the init method, preferable in the
     * constructor.
     * 
     * It looks unused, but it is used solely by native code.
     */
    private final FileDescriptor m_rawFd;

    /**
     * This method is used to open the initial operating system icmp socket. The
     * descriptor for the socket is stored in the member m_rawFd.
     * @param id 
     * 
     * @throws java.io.IOException
     *             This is thrown if an error occurs opening the ICMP socket.
     */
    private native void initSocket() throws IOException;

    /**
     * This method is used to bind the socket.  It should be done after {@link #initSocket(short)}
     * and any {@link #setTrafficClass(int)} or {@link #dontFragment()} calls, but
     * before {@link #send(DatagramPacket)} or {@link #receive()} is first called.
     * 
     * @throws java.io.IOException
     *             This is thrown if an error occurs binding the ICMP socket.
     */
    private native void bindSocket(final short id) throws IOException;

    /**
     * Constructs a new socket that is able to send and receive ICMP messages.
     * The newly constructed socket will receive all ICMP messages directed at
     * the local machine. The application must be prepared to handle any and
     * discard any non-interesting ICMP messages.
     * @exception java.io.IOException
     *                This exception is thrown if the socket fails to be opened
     *                correctly.
     */
    public IcmpSocket(final short id) throws IOException {
        String property = System.getProperty(PROPERTY_NAME);

        boolean loaded = false;
        if (property != null) {
            log().debug("System property '" + PROPERTY_NAME + "' set to '" + property + ".  Attempting to load " + LIBRARY_NAME + " library from this location.");
            try {
                System.load(property);
                loaded = true;
            } catch (final UnsatisfiedLinkError e) {
                log().info("Failed to load library " + property + ".");
            }
        }

        if (!loaded) {
            log().debug("Attempting to load library using System.loadLibrary(\"" + LIBRARY_NAME + "\").");
            System.loadLibrary(LIBRARY_NAME);
        }

        log().info("Successfully loaded " + LIBRARY_NAME + " library.");

        m_rawFd = new FileDescriptor();
        initSocket();
        bindSocket(id);
        String osName = System.getProperty("os.name");
        if (osName != null && osName.toLowerCase().startsWith("windows")) {
            // Windows complains if you receive before sending a packet
            ICMPEchoPacket p = new ICMPEchoPacket(0);
            p.setIdentity((short) 0);
            p.computeChecksum();
            byte[] buf = p.toBytes();
            DatagramPacket dgp = new DatagramPacket(buf, buf.length, InetAddress.getByName("127.0.0.1"), 0);
            send(dgp);
        }
    }

    private Logger log() {
        try {
            if (System.getProperty(LOGGER_PROPERTY_NAME) != null) {
                return (Logger)Class.forName(System.getProperty(LOGGER_PROPERTY_NAME)).newInstance();
            }
        } catch (Exception e) {
            System.err.println("[WARN] Unable to create jicmp logger from property "+LOGGER_PROPERTY_NAME+" with value "+System.getProperty(LOGGER_PROPERTY_NAME)+". "+e);
        }
        return new Logger() {
            public void debug(String msg) {
                System.err.println("[DEBUG] "+msg);
            }
            public void info(String msg) {
                System.err.println("[INFO] "+msg);
            }
        };
    }

    /**
     * This method is used to set the traffic class for the socket. (Essentially,
     * the QoS flags for the packets sent through this socket.)  This is equivalent
     * to Java's {@link java.net.Socket#setTrafficClass(int)}.
     * 
     * @param tc the traffic class
     * @throws IOException
     */
    public final native void setTrafficClass(final int tc) throws IOException;

    /**
     * This method is used to set the "Don't Fragment" bit for the socket.
     * 
     * @throws IOException
     */
    public final native void dontFragment() throws IOException;

    /**
     * This method is used to receive the next ICMP datagram from the operating
     * system. The returned datagram packet's address is set to the sending
     * host's address. The port number is always set to Zero, and the buffer is
     * set to the contents of the raw ICMP message.
     * 
     * @exception java.io.IOException
     *                Thrown if an error occurs reading the next ICMP message.
     * 
     */
    public final native DatagramPacket receive() throws IOException;

    /**
     * This method is used to send the passed datagram using the ICMP transport.
     * The destination of the datagram packet is used as the send to destination
     * for the underlying ICMP socket. The port number of the datagram packet is
     * ignored completely.
     * 
     * @exception java.io.IOException
     *                Thrown if an error occurs sending the datagram to the
     *                remote host.
     * @exception java.net.NoRouteToHostException
     *                Thrown if the destination address is a broadcast address.
     */
    public final native void send(DatagramPacket packet) throws IOException;

    /**
     * This method is used to close and release the resources associated with the
     * instance. The file descriptor is closed at the operating system level and
     * any subsequent calls to this instance should result in exceptions being
     * generated.
     */
    public final native void close();
}
