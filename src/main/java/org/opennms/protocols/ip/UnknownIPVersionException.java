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

package org.opennms.protocols.ip;

/**
 * This exception is thrown when the IP version is not supported by the IPHeader
 * class.
 * 
 * @author Brian Weaver
 */
public class UnknownIPVersionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnknownIPVersionException() {
        super();
    }

    public UnknownIPVersionException(String why) {
        super(why);
    }
}
