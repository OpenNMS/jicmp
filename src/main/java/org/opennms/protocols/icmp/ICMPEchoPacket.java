//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2003 Mar 05: Cleaned up some ICMP related code.
// 2003 Jan 31: Cleaned up some unused imports. 
// 2002 Nov 13: Added response time stats for ICMP requests.
// 
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.                                                            
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact: 
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
// Tab Size = 8
//

package org.opennms.protocols.icmp;

import org.opennms.protocols.ip.OC16ChecksumProducer;

/**
 * The ping packet for discovery
 * 
 * @author Brian Weaver
 * @author Sowmya
 * @author <a href="http://www.opennms.org/">OpenNMS</a>
 */
public final class ICMPEchoPacket extends ICMPHeader {
    /**
     * Unique named padding that is placed in front of the incremental padding.
     */
    private static final byte NAMED_PAD[] = { (byte) 'O', (byte) 'p', (byte) 'e', (byte) 'n', (byte) 'N', (byte) 'M', (byte) 'S', (byte) '!' };

    /**
     * Timestamp when packet was sent
     */
    private long m_sent;

    /**
     * Timestamp of when packet was received.
     */
    private long m_recv;

    /**
     * The thread id of the sender. Effective key for the packet.
     */
    private long m_tid; // thread id

    /**
     * Padding used to make the packet conform to the defacto unix ping program
     * (56 bytes) or whatever packetsize is sent in
     */
    private byte[] m_pad;

    /**
     * The ping rtt (microseconds)
     */
    private long m_rtt;

    /**
     * Converts a byte to a long and wraps the value to avoid sign extension.
     * The method essentially treats the value of 'b' as an 8-bit unsigned value
     * for conversion purposes.
     * 
     * @param b
     *            The byte to convert.
     * 
     * @return The converted long value.
     */
    static private long byteToLong(byte b) {
        long r = (long) b;
        if (r < 0)
            r += 256;
        return r;
    }

    /**
     * Private constructor to disallow default construction of an object.
     * 
     * @exception java.lang.UnsupportedOperationException
     *                Always thrown.
     */
    @SuppressWarnings("unused")
	private ICMPEchoPacket() {
        throw new java.lang.UnsupportedOperationException("illegal constructor call");
    }

    /**
     * Creates a new discovery ping packet that can be sent to a remote protocol
     * stack. The ICMP type is set to an Echo Request. The next sequence in the
     * ICMPHeader base class is set and the sent time is set to the current
     * time.
     * 
     * @param tid
     *            The thread id for the packet.
     * 
     * @see java.lang.System#currentTimeMillis
     */
    public ICMPEchoPacket(long tid) {
    	this(tid, 64);
    }

    /**
     * Creates a new discovery ping packet that can be sent to a remote protocol
     * stack. The ICMP type is set to an Echo Request. The next sequence in the
     * ICMPHeader base class is set and the sent time is set to the current
     * time.
     * 
     * @param tid
     *            The thread id for the packet.
     * @param packetsize
     *            The pad size in bytes
     * @see java.lang.System#currentTimeMillis
     */
    public ICMPEchoPacket(long tid, int packetsize) {
        super(ICMPHeader.TYPE_ECHO_REQUEST, (byte) 0);
        setNextSequenceId();

        m_rtt = 0;
        m_sent = 0;
        m_recv = 0;
        m_tid = tid;
        
        if (packetsize < getMinimumNetworkSize()) {
        	throw new IllegalArgumentException("Minimum size for a ICMPEchoPacket is " + getMinimumNetworkSize() + " bytes.");
        }
        
        m_pad = new byte[packetsize - getMinimumNetworkSize()];
        for (int x = 0; x < NAMED_PAD.length && x < m_pad.length; x++)
            m_pad[x] = NAMED_PAD[x];
        for (int x = NAMED_PAD.length; x < m_pad.length; x++)
            m_pad[x] = (byte) 0;

    }


    /**
     * Creates a new discovery ping packet from the passed buffer.
     * 
     * @param buf
     *            The buffer containing a refected ping packet.
     */
    public ICMPEchoPacket(byte[] buf) {
        loadFromBuffer(buf, 0);
    }

    /**
     * Returns the time the packet was sent.
     */
    public final long getSentTime() {
        return m_sent;
    }

    /**
     * Sets the sent time to the current time.
     * 
     * @see java.lang.System#currentTimeMillis
     */
    public final long setSentTime() {
        m_sent = System.currentTimeMillis();
        return m_sent;
    }

    /**
     * Sets the sent time to the passed value.
     * 
     * @param time
     *            The new sent time.
     */
    public final void setSentTime(long time) {
        m_sent = time;
    }

    /**
     * Gets the currently set received time.
     */
    public final long getReceivedTime() {
        return m_recv;
    }

    /**
     * Sets the recieved time for the packet.
     * 
     * @see java.lang.System#currentTimeMillis
     */
    public final long setReceivedTime() {
        m_recv = System.currentTimeMillis();
        return m_recv;
    }

    /**
     * Sets the received time to the passed value.
     * 
     * @param time
     *            The new received time.
     * 
     */
    public final void setReceivedTime(long time) {
        m_recv = time;
    }

    /**
     * Sets the ping Round Trip Time
     */
    public final void setPingRTT(long time) {
        m_rtt = time;
    }

    /**
     * Gets the ping Round Trip Time
     */
    public final long getPingRTT() {
        return m_rtt;
    }

    /**
	 * Returns the size of the integer headers in packet 
     */
    public int getDataSize() {
        return (getHeaderSize() + 32);
    }

    /**
     * Returns the size of the integer headers in the packet plus the required 'OpenNMS!' string.
     */
    public int getMinimumNetworkSize() {
        return (getDataSize() + NAMED_PAD.length);
    }
    
    /**
     * Useless function with variable packet sizes but preserved for backwards compatability
     * @return
     */
    @Deprecated
    public static final int getNetworkSize() {
    	return ICMPHeader.getNetworkSize() + 32 + 16;
    }
    
    
    public int getPacketSize() {
    	return getDataSize() + m_pad.length;
    }

    /**
     * Computes and stores the current checksum based upon the data currently
     * contained in the object.
     */
    public final void computeChecksum() {
        OC16ChecksumProducer summer = new OC16ChecksumProducer();

        super.computeChecksum(summer);
        summer.add(m_rtt);
        summer.add(m_sent);
        summer.add(m_recv);
        summer.add(m_tid);

        //
        // do all the elements combining two bytes
        // into a single short.
        //
        int stop = m_pad.length - (m_pad.length % 2);
        for (int i = 0; i < stop; i += 2)
            summer.add(m_pad[i], m_pad[i + 1]);

        //
        // take care of any stray bytes
        //
        if ((m_pad.length % 2) == 1)
            summer.add(m_pad[m_pad.length - 1]);

        //
        // set the checksum in the header
        //
        super.setChecksum(summer.getChecksum());
    }

    /**
     * Returns the currently set Thread ID
     */
    public final long getTID() {
        return m_tid;
    }

    /**
     * Sets the current Thread Id
     */
    public final void setTID(long tid) {
        m_tid = tid;
    }

    /**
     * Loads the data from the passed buffer into the current object. Once
     * loaded the object's values should reflect the contents of the buffer.
     * 
     * @param buf
     *            The buffer to load from
     * @param offset
     *            The offset to begin loading from
     * 
     * @return The offset of the next byte of data that was not used to
     *         initialize this object.
     * 
     * @exception java.lang.IndexOutOfBoundsException
     *                Thrown if there is not enough data contained in the buffer
     *                to sufficent set the state of the object
     * 
     */
    public final int loadFromBuffer(byte[] buf, int offset) {
        if ((buf.length - offset) < getMinimumNetworkSize())
            throw new IndexOutOfBoundsException("Insufficient Data: packet must be at least " + getMinimumNetworkSize() + " bytes long.");

        offset = super.loadFromBuffer(buf, offset);
        if (!isEchoReply() && !isEchoRequest())
            throw new IllegalArgumentException("Invalid type, must be echo request/reply packet");

        m_sent = 0;
        for (int x = 0; x < 8; x++) {
            m_sent <<= 8;
            m_sent |= byteToLong(buf[offset++]);
        }

        m_recv = 0;
        for (int x = 0; x < 8; x++) {
            m_recv <<= 8;
            m_recv |= byteToLong(buf[offset++]);
        }

        m_tid = 0;
        for (int x = 0; x < 8; x++) {
            m_tid <<= 8;
            m_tid |= byteToLong(buf[offset++]);
        }

        m_rtt = 0;
        for (int x = 0; x < 8; x++) {
            m_rtt <<= 8;
            m_rtt |= byteToLong(buf[offset++]);
        }

        // skip over the header and timestamp data
        int remainingBytes = buf.length - getDataSize();
        if (m_pad == null || m_pad.length != remainingBytes) {
        	m_pad = new byte[remainingBytes];
        }

        for (int x = 0; x < m_pad.length; x++) {
            m_pad[x] = buf[offset++];
        }

        return offset;
    }

    /**
     * Writes the objects data out to the specified buffer at the starting
     * offset. If the buffer does not have sufficent data to store the
     * information then an IndexOutOfBoundsException is thrown.
     * 
     * @param buf
     *            The storage buffer.
     * @param offset
     *            The location to start in buf.
     * 
     * @return The new offset after storing to the buffer.
     * 
     * @exception IndexOutOfBoundsException
     *                Thrown if the buffer does not have enough storage space.
     * 
     */
    public final int storeToBuffer(byte[] buf, int offset) {
        if ((buf.length - offset) < getPacketSize()) {
            throw new IndexOutOfBoundsException("Insufficient Buffer Size.  Need at least " + getPacketSize() + " bytes to store packet.");
        }

        offset = super.storeToBuffer(buf, offset);

        long t = m_sent;
        for (int x = 0; x < 8; x++) {
            buf[offset++] = (byte) (t >>> 56);
            t <<= 8;
        }

        t = m_recv;
        for (int x = 0; x < 8; x++) {
            buf[offset++] = (byte) (t >>> 56);
            t <<= 8;
        }

        t = m_tid;
        for (int x = 0; x < 8; x++) {
            buf[offset++] = (byte) (t >>> 56);
            t <<= 8;
        }

        t = m_rtt;
        for (int x = 0; x < 8; x++) {
            buf[offset++] = (byte) (t >>> 56);
            t <<= 8;
        }

        for (int x = 0; x < m_pad.length; x++) {
            buf[offset++] = m_pad[x];
        }

        return offset;
    }

    /**
     * Converts the object into an array of bytes which is suitable for
     * transmission to remote hosts.
     * 
     * @return The object as an array of bytes.
     */
    public final byte[] toBytes() {
        byte[] buf = new byte[getPacketSize()];
        storeToBuffer(buf, 0);
        return buf;
    }

} // end Packet.
