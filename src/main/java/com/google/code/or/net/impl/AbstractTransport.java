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

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.net.Transport;
import com.google.code.or.net.TransportContext;

/**
 * 
 * @author Jingqi Xu
 */
public abstract class AbstractTransport implements Transport {
	//
	protected Transport.Authenticator authenticator;
	protected final DefaultContext context = new DefaultContext();
	protected final AtomicBoolean verbose = new AtomicBoolean(true);

	/**
	 * 
	 */
	public boolean isVerbose() {
		return this.verbose.get();
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose.set(verbose);
	}
	
	public TransportContext getContext() {
		return context;
	}
	
	public Transport.Authenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(Transport.Authenticator authenticator) {
		this.authenticator = authenticator;
	}
	
	/**
	 * 
	 */
	public static class DefaultContext implements TransportContext {
		//
		private long threadId;
		private String scramble;
		private int protocolVersion;
		private String serverHost;
		private int serverPort;
		private int serverStatus;
		private int serverCollation;
		private String serverVersion;
		private int serverCapabilities;
		
		/**
		 * 
		 */
		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("threadId", threadId)
			.append("scramble", scramble)
			.append("protocolVersion", protocolVersion)
			.append("serverHost", serverHost)
			.append("serverPort", serverPort)
			.append("serverStatus", serverStatus)
			.append("serverCollation", serverCollation)
			.append("serverVersion", serverVersion)
			.append("serverCapabilities", serverCapabilities).toString();
		}
		
		/**
		 * 
		 */
		public long getThreadId() {
			return threadId;
		}
		
		public void setThreadId(long threadId) {
			this.threadId = threadId;
		}
		
		public String getScramble() {
			return scramble;
		}
		
		public void setScramble(String scramble) {
			this.scramble = scramble;
		}
		
		public int getProtocolVersion() {
			return protocolVersion;
		}
		
		public void setProtocolVersion(int protocolVersion) {
			this.protocolVersion = protocolVersion;
		}
		
		public String getServerHost() {
			return serverHost;
		}
		
		public void setServerHost(String host) {
			this.serverHost = host;
		}
		
		public int getServerPort() {
			return serverPort;
		}
		
		public void setServerPort(int port) {
			this.serverPort = port;
		}
		
		public int getServerStatus() {
			return serverStatus;
		}
		
		public void setServerStatus(int serverStatus) {
			this.serverStatus = serverStatus;
		}
		
		public int getServerCollation() {
			return serverCollation;
		}
		
		public void setServerCollation(int serverCollation) {
			this.serverCollation = serverCollation;
		}
		
		public String getServerVersion() {
			return serverVersion;
		}
		
		public void setServerVersion(String serverVersion) {
			this.serverVersion = serverVersion;
		}
		
		public int getServerCapabilities() {
			return serverCapabilities;
		}
		
		public void setServerCapabilities(int serverCapabilities) {
			this.serverCapabilities = serverCapabilities;
		}
	}
}
