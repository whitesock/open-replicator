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

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.common.util.MySQLUtils;
import com.google.code.or.io.util.XSerializer;
import com.google.code.or.net.Packet;
import com.google.code.or.net.Transport;
import com.google.code.or.net.TransportContext;
import com.google.code.or.net.TransportException;
import com.google.code.or.net.impl.packet.ErrorPacket;
import com.google.code.or.net.impl.packet.OKPacket;
import com.google.code.or.net.impl.packet.RawPacket;

/**
 * 
 * @author Jingqi Xu
 */
public class AuthenticatorImpl implements Transport.Authenticator {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatorImpl.class);
	
	//
	public static final int DEFAULT_CAPABILITIES = (MySQLConstants.CLIENT_LONG_FLAG | MySQLConstants.CLIENT_PROTOCOL_41 | MySQLConstants.CLIENT_SECURE_CONNECTION);

	//
	protected String user;
	protected String password;
	protected String initialSchema;
	protected int clientCollation;
	protected int clientCapabilities;
	protected int maximumPacketLength;
	protected String encoding = "utf-8";
	
	/**
	 * 
	 */
	public void login(Transport transport) throws IOException {
		//
		final TransportContext ctx = transport.getContext();
		LOGGER.info("start to login, user: {}, host: {}, port: {}", new Object[]{this.user, ctx.getServerHost(), ctx.getServerPort()});
		
		//
		final XSerializer s = new XSerializer(64);
		s.writeInt(buildClientCapabilities(), 4);
		s.writeInt(this.maximumPacketLength, 4);
		s.writeInt(this.clientCollation > 0 ? this.clientCollation : ctx.getServerCollation(), 1);
		s.writeBytes((byte)0, 23); // Fixed, all 0
		s.writeNullTerminatedString(StringColumn.valueOf(this.user.getBytes(this.encoding)));
		s.writeInt(20, 1); // the length of the SHA1 encrypted password
		s.writeBytes(MySQLUtils.password41OrLater(this.password.getBytes(this.encoding), ctx.getScramble().getBytes(this.encoding)));
		if(this.initialSchema != null) s.writeNullTerminatedString(StringColumn.valueOf(this.initialSchema.getBytes(this.encoding)));
		
		//
		final RawPacket request = new RawPacket();
		request.setSequence(1);
		request.setPacketBody(s.toByteArray());
		request.setLength(request.getPacketBody().length);
		transport.getOutputStream().writePacket(request);
		transport.getOutputStream().flush();
		
		//
		final Packet response = transport.getInputStream().readPacket();
		if(response.getPacketBody()[0] == ErrorPacket.PACKET_MARKER) {
			final ErrorPacket error = ErrorPacket.valueOf(response);
			LOGGER.info("login failed, user: {}, error: {}", this.user, error);
			throw new TransportException(error);
		} else if(response.getPacketBody()[0] == OKPacket.PACKET_MARKER) {
			final OKPacket ok = OKPacket.valueOf(response);
			LOGGER.info("login successfully, user: {}, detail: {}", this.user, ok);
		} else {
			LOGGER.warn("login failed, unknown packet: ", response);
			throw new NestableRuntimeException("assertion failed, invalid packet: " + response);
		}
	}
	
	/**
	 * 
	 */
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getInitialSchema() {
		return initialSchema;
	}

	public void setInitialSchema(String schema) {
		this.initialSchema = schema;
	}
	
	public int getClientCollation() {
		return clientCollation;
	}

	public void setClientCollation(int collation) {
		this.clientCollation = collation;
	}

	public int getClientCapabilities() {
		return clientCapabilities;
	}

	public void setClientCapabilities(int capabilities) {
		this.clientCapabilities = capabilities;
	}
	
	public int getMaximumPacketLength() {
		return maximumPacketLength;
	}

	public void setMaximumPacketLength(int packetLength) {
		this.maximumPacketLength = packetLength;
	}
	
	/**
	 * 
	 */
	protected int buildClientCapabilities() {
		int r = this.clientCapabilities > 0 ? this.clientCapabilities : DEFAULT_CAPABILITIES;
		if(this.initialSchema != null) r |= MySQLConstants.CLIENT_CONNECT_WITH_DB;
		return r;
	}
}
