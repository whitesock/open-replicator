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

import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.io.util.XDeserializer;
import com.google.code.or.io.util.XSerializer;
import com.google.code.or.net.Packet;

/**
 * 
 * @author Jingqi Xu
 */
public class GreetingPacket extends AbstractPacket {
	//
	private static final long serialVersionUID = 5506239117316020734L;
	
	//
	private int protocolVersion;
	private StringColumn serverVersion;
	private long threadId;
	private StringColumn scramble1;
	private int serverCapabilities;
	private int serverCollation;
	private int serverStatus;
	private StringColumn scramble2;
	private StringColumn pluginProvidedData;
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("protocolVersion", protocolVersion)
		.append("serverVersion", serverVersion)
		.append("threadId", threadId)
		.append("scramble1", scramble1)
		.append("serverCapabilities", serverCapabilities)
		.append("serverCollation", serverCollation)
		.append("serverStatus", serverStatus)
		.append("scramble2", scramble2)
		.append("pluginProvidedData", pluginProvidedData).toString();
	}
	
	/**
	 * 
	 */
	public byte[] getPacketBody() {
		final XSerializer s = new XSerializer(128);
		s.writeInt(this.protocolVersion, 1);
		s.writeNullTerminatedString(this.serverVersion);
		s.writeLong(this.threadId, 4);
		s.writeNullTerminatedString(this.scramble1);
		s.writeInt(this.serverCapabilities, 2);
		s.writeInt(this.serverCollation, 1);
		s.writeInt(this.serverStatus, 2);
		s.writeInt(0, 13);
		s.writeNullTerminatedString(this.scramble2);
		s.writeNullTerminatedString(this.pluginProvidedData);
		return s.toByteArray();
	}
	
	/**
	 * 
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}

	public StringColumn getServerVersion() {
		return serverVersion;
	}

	public long getThreadId() {
		return threadId;
	}

	public StringColumn getScramble1() {
		return scramble1;
	}
	
	public int getServerCapabilities() {
		return serverCapabilities;
	}

	public int getServerCollation() {
		return serverCollation;
	}

	public int getServerStatus() {
		return serverStatus;
	}

	public StringColumn getScramble2() {
		return scramble2;
	}
	
	/**
	 * 
	 */
	public static GreetingPacket valueOf(Packet packet) throws IOException {
		final XDeserializer d = new XDeserializer(packet.getPacketBody());
		final GreetingPacket r = new GreetingPacket();
		r.length = packet.getLength();
		r.sequence = packet.getSequence();
		r.protocolVersion = d.readInt(1);
		r.serverVersion = d.readNullTerminatedString();
		r.threadId = d.readLong(4);
		r.scramble1 = d.readNullTerminatedString();
		r.serverCapabilities = d.readInt(2);
		r.serverCollation = d.readInt(1);
		r.serverStatus = d.readInt(2);
		d.skip(13); // reserved, all 0
		r.scramble2 = d.readNullTerminatedString();
		r.pluginProvidedData = d.readNullTerminatedString();
		return r;
	}
}
