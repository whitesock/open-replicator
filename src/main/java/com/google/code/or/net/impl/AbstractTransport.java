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

import com.google.code.or.io.XInputStream;
import com.google.code.or.io.XOutputStream;
import com.google.code.or.net.Transport;

/**
 * 
 * @author Jingqi Xu
 */
public abstract class AbstractTransport implements Transport {
	//
	protected Transport.Authenticator authenticator;
	protected final AtomicBoolean verbose = new AtomicBoolean(true);
	protected final TransportContextImpl context = new TransportContextImpl();

	/**
	 * 
	 */
	public boolean isVerbose() {
		return this.verbose.get();
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose.set(verbose);
	}
	
	public TransportContextImpl getContext() {
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
	protected final void closeQuietly(Socket socket) {
		try {
			socket.close();
		} catch(Exception e) {
			// NOP
		}
	}
	
	protected final void closeQuietly(XInputStream is) {
		try {
			is.close();
		} catch(Exception e) {
			// NOP
		}
	}
	
	protected final void closeQuietly(XOutputStream os) {
		try {
			os.close();
		} catch(Exception e) {
			// NOP
		}
	}
}
