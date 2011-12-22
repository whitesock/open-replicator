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
package com.google.code.or.net.impl.packet;

import java.io.IOException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.io.util.XDeserializer;
import com.google.code.or.io.util.XSerializer;
import com.google.code.or.net.Packet;

/**
 * 
 * @author Jingqi Xu
 */
public class EOFPacket extends AbstractPacket {
	//
	private static final long serialVersionUID = 7001637720833705527L;
	
	//
	public static final byte PACKET_MARKER = (byte)0xFE;

	//
	private int packetMarker;
	private int warningCount;
	private int serverStatus;

	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("packetMarker", packetMarker)
		.append("warningCount", warningCount)
		.append("serverStatus", serverStatus).toString();
	}
	
	/**
	 * 
	 */
	public byte[] getPacketBody() {
		final XSerializer s = new XSerializer(32);
		s.writeInt(this.packetMarker, 1);
		s.writeInt(this.warningCount, 2);
		s.writeInt(this.serverStatus, 2);
		return s.toByteArray();
	}
	
	/**
	 * 
	 */
	public int getPacketMarker() {
		return packetMarker;
	}

	public void setPacketMarker(int packetMarker) {
		this.packetMarker = packetMarker;
	}

	public int getWarningCount() {
		return warningCount;
	}

	public void setWarningCount(int warningCount) {
		this.warningCount = warningCount;
	}

	public int getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(int serverStatus) {
		this.serverStatus = serverStatus;
	}
	
	/**
	 * 
	 */
	public static EOFPacket valueOf(Packet packet) throws IOException {
		final XDeserializer d = new XDeserializer(packet.getPacketBody());
		final EOFPacket r = new EOFPacket();
		r.length = packet.getLength();
		r.sequence = packet.getSequence();
		r.packetMarker = d.readInt(1);
		r.warningCount = d.readInt(2);
		r.serverStatus = d.readInt(2);
		return r;
	}
}
