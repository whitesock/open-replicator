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

import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.common.util.IOUtils;
import com.google.code.or.io.SocketFactory;
import com.google.code.or.io.util.ActiveBufferedInputStream;
import com.google.code.or.net.Packet;
import com.google.code.or.net.TransportException;
import com.google.code.or.net.TransportInputStream;
import com.google.code.or.net.TransportOutputStream;
import com.google.code.or.net.impl.packet.ErrorPacket;
import com.google.code.or.net.impl.packet.GreetingPacket;

/**
 * 
 * @author Jingqi Xu
 */
public class TransportImpl extends AbstractTransport {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(TransportImpl.class);
	
	//
	protected Socket socket;
	protected TransportInputStream is;
	protected TransportOutputStream os;
	protected SocketFactory socketFactory;
	protected int level1BufferSize = 1024 * 1024;
	protected int level2BufferSize = 8 * 1024 * 1024;
	protected final AtomicBoolean connected = new AtomicBoolean(false);

	/**
	 * 
	 */
	public boolean isConnected() {
		return this.connected.get();
	}
	
	public void connect(String host, int port) throws Exception {
		//
		if(!this.connected.compareAndSet(false, true)) {
			return;
		}
		
		//
		if(isVerbose() && LOGGER.isInfoEnabled()) {
			LOGGER.info("connecting to host: {}, port: {}", host, port);
		}
		
		//
		this.socket = this.socketFactory.create(host, port);
		this.os = new TransportOutputStreamImpl(this.socket.getOutputStream());
		if(this.level2BufferSize <= 0) {
			this.is = new TransportInputStreamImpl(this.socket.getInputStream(), this.level1BufferSize);
		} else {
			this.is = new TransportInputStreamImpl(new ActiveBufferedInputStream(this.socket.getInputStream(), this.level2BufferSize), this.level1BufferSize);
		}
		
		//
		final Packet packet = this.is.readPacket();
		if(packet.getPacketBody()[0] == ErrorPacket.PACKET_MARKER) {
			final ErrorPacket error = ErrorPacket.valueOf(packet);
			LOGGER.info("failed to connect to host: {}, port: {}, error", new Object[]{host, port, error});
			throw new TransportException(error);
		} else {
			//
			final GreetingPacket greeting = GreetingPacket.valueOf(packet);
			this.context.setServerHost(host);
			this.context.setServerPort(port);
			this.context.setServerStatus(greeting.getServerStatus());
			this.context.setServerVersion(greeting.getServerVersion().toString());
			this.context.setServerCollation(greeting.getServerCollation());
			this.context.setServerCapabilities(greeting.getServerCapabilities());
			this.context.setThreadId(greeting.getThreadId());
			this.context.setProtocolVersion(greeting.getProtocolVersion());
			this.context.setScramble(greeting.getScramble1().toString() + greeting.getScramble2().toString());
			
			//
			if(isVerbose() && LOGGER.isInfoEnabled()) {
				LOGGER.info("connected to host: {}, port: {}, context: {}", new Object[]{host, port, this.context});
			}
		}
		
		//
		this.authenticator.login(this);
	}

	public void disconnect() throws Exception {
		//
		if(!this.connected.compareAndSet(true, false)) {
			return;
		}
		
		//
		IOUtils.closeQuietly(this.is);
		IOUtils.closeQuietly(this.os);
		IOUtils.closeQuietly(this.socket);
		
		//
		if(isVerbose() && LOGGER.isInfoEnabled()) {
			LOGGER.info("disconnected from {}:{}", this.context.getServerHost(), this.context.getServerPort());
		}
	}
	
	/**
	 * 
	 */
	public int getLevel1BufferSize() {
		return level1BufferSize;
	}

	public void setLevel1BufferSize(int size) {
		this.level1BufferSize = size;
	}

	public int getLevel2BufferSize() {
		return level2BufferSize;
	}

	public void setLevel2BufferSize(int size) {
		this.level2BufferSize = size;
	}
	
	public TransportInputStream getInputStream() {
		return this.is;
	}

	public TransportOutputStream getOutputStream() {
		return this.os;
	}
	
	public SocketFactory getSocketFactory() {
		return socketFactory;
	}

	public void setSocketFactory(SocketFactory factory) {
		this.socketFactory = factory;
	}
}
