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

import org.opennms.protocols.ip.OC16ChecksumProducer;

/**
 * This is the implementation of an ICMP Address Mask Reqeust object. The object
 * can be stored in a buffer to send or loaded from a received buffer. The class
 * is marked final since it is not intended to be extended.
 * 
 * @author Brian Weaver
 * 
 */
public final class AddressMaskRequest extends ICMPHeader {
    // m_mask = 0

    /**
     * Creates a new ICMP Address Mask Request object.
     * 
     */
    public AddressMaskRequest() {
        super(ICMPHeader.TYPE_ADDRESS_MASK_REQUEST, (byte) 0);
    }

    /**
     * Creates a new ICMP Address mask request from the spcified data at the
     * specific offset.
     * 
     * @param buf
     *            The buffer containing the data.
     * @param offset
     *            The start of the icmp data.
     * 
     * @exception java.lang.IndexOutOfBoundsException
     *                Thrown if there is not sufficent data in the buffer.
     * @exception java.lang.IllegalArgumentException
     *                Thrown if the ICMP type is not an Address Mask Request.
     */
    public AddressMaskRequest(byte[] buf, int offset) {
        super();
        loadFromBuffer(buf, offset);
    }

    /**
     * Computes the ones compliment 16-bit checksum for the ICMP message.
     * 
     */
    public final void computeChecksum() {
        OC16ChecksumProducer summer = new OC16ChecksumProducer();
        super.computeChecksum(summer);

        summer.add((int) 0);
        setChecksum(summer.getChecksum());
    }

    /**
     * Writes the ICMP address mask request out to the specified buffer at the
     * starting offset. If the buffer does not have sufficent data to store the
     * information then an IndexOutOfBoundsException is thrown.
     * 
     * @param buf
     *            The storage buffer.
     * @param offset
     *            The location to start in buf.
     * 
     * @return The new offset after storing to the buffer.
     * 
     * @exception java.lang.IndexOutOfBoundsException
     *                Thrown if the buffer does not have enough storage space.
     * 
     */
    public final int storeToBuffer(byte[] buf, int offset) {
        if (buf.length < (offset + 12))
            throw new IndexOutOfBoundsException("Array index overflow in buffer build");

        computeChecksum();
        offset = super.storeToBuffer(buf, offset);

        //
        // add in the 32-bit zero mask
        //
        for (int x = 0; x < 4; x++)
            buf[offset++] = 0;

        return offset;
    }

    /**
     * Reads the ICMP Address Mask Reqeust from the specified buffer and sets
     * the internal fields equal to the data. If the buffer does not have
     * sufficent data to restore the header then an IndexOutOfBoundsException is
     * thrown by the method. If the buffer does not contain an address mask
     * reqeust then an IllegalArgumentException is thrown.
     * 
     * @param buf
     *            The buffer to read the data from.
     * @param offset
     *            The offset to start reading data.
     * 
     * @return The new offset after reading the data.
     * 
     * @exception java.lang.IndexOutOfBoundsException
     *                Thrown if there is not sufficent data in the buffer.
     * @exception java.lang.IllegalArgumentException
     *                Thrown if the ICMP type is not an Address Mask Request.
     */
    public final int loadFromBuffer(byte[] buf, int offset) {
        if (buf.length < (offset + 12))
            throw new IndexOutOfBoundsException("Insufficient data to load ICMP header");

        offset = super.loadFromBuffer(buf, offset);

        if (getType() != TYPE_ADDRESS_MASK_REQUEST)
            throw new IllegalArgumentException("The buffer did not contain an Address Mask Request");

        offset += 4;
        return offset;
    }

    /**
     * Converts the object to an array of bytes
     * 
     */
    public final byte[] toBytes() {
        byte[] b = new byte[12];
        storeToBuffer(b, 0);
        return b;
    }
}
