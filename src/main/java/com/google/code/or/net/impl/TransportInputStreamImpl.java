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
import java.io.InputStream;

import com.google.code.or.io.impl.XInputStreamImpl;
import com.google.code.or.net.Packet;
import com.google.code.or.net.TransportInputStream;
import com.google.code.or.net.impl.packet.RawPacket;

/**
 * 
 * @author Jingqi Xu
 */
public class TransportInputStreamImpl extends XInputStreamImpl implements TransportInputStream {

	/**
	 * 
	 */
	public TransportInputStreamImpl(InputStream is) {
		super(is);
	}
	
	public TransportInputStreamImpl(InputStream is, int size) {
		super(is, size);
	}

	/**
	 * 
	 */
	public Packet readPacket() throws IOException {
		//
		final RawPacket r = new RawPacket();
		r.setLength(readInt(3));
		r.setSequence(readInt(1));
		
		//
		int total = 0;
		final byte[] body = new byte[r.getLength()];
		while(total < body.length) {
			total += this.read(body, total, body.length - total);
		}
		r.setPacketBody(body);
		return r;
	}
}
