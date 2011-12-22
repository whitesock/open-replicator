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
package com.google.code.or.io.impl;

import java.net.Socket;

import com.google.code.or.io.SocketFactory;

/**
 * 
 * @author Jingqi Xu
 */
public class SocketFactoryImpl implements SocketFactory {
	//
	private boolean keepAlive = false;
	private boolean tcpNoDelay = false;
	private int receiveBufferSize = -1;

	/**
	 * 
	 */
	public Socket create(String host, int port) throws Exception {
		final Socket r = new Socket(host, port);
		r.setKeepAlive(this.keepAlive);
		r.setTcpNoDelay(this.tcpNoDelay);
		if(this.receiveBufferSize > 0) r.setReceiveBufferSize(this.receiveBufferSize);
		return r;
	}
	
	/**
	 * 
	 */
	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}
}
