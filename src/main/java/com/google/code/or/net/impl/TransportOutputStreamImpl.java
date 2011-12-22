/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.or.net.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.impl.XOutputStreamImpl;
import com.google.code.or.net.Packet;
import com.google.code.or.net.TransportOutputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class TransportOutputStreamImpl extends XOutputStreamImpl implements TransportOutputStream {

	/**
	 * 
	 */
	public TransportOutputStreamImpl(OutputStream out) {
		super(out);
	}
	
	/**
	 * 
	 */
	public void writePacket(Packet packet) throws IOException {
		//
		final byte body[] = packet.getPacketBody();
		if(body.length < MySQLConstants.MAX_PACKET_LENGTH) { // Single packet
			writeInt(body.length, 3);
			writeInt(packet.getSequence(), 1);
			writeBytes(body);
			return;
		}
		
		// If the length of the packet is greater than the value of MAX_PACKET_LENGTH,
		// which is defined to be power(2, 24) �C 1 in sql/net_serv.cc, the packet gets 
		// split into smaller packets with bodies of MAX_PACKET_LENGTH plus the last 
		// packet with a body that is shorter than MAX_PACKET_LENGTH. 
		int offset = 0;
		int sequence = packet.getSequence();
		for(; offset + MySQLConstants.MAX_PACKET_LENGTH <= body.length; offset += MySQLConstants.MAX_PACKET_LENGTH) {
			writeInt(MySQLConstants.MAX_PACKET_LENGTH, 3);
			writeInt(sequence++, 1);
			writeBytes(body, offset, MySQLConstants.MAX_PACKET_LENGTH);
		}
		
		// The last short packet will always be present even if it must have a zero-length body.
		// It serves as an indicator that there are no more packet parts left in the stream for this large packet.
		writeInt(body.length - offset, 3);
		writeInt(sequence++, 1);
		writeBytes(body, offset, body.length - offset);
	}
}
