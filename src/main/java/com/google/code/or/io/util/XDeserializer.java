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
package com.google.code.or.io.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.google.code.or.common.glossary.UnsignedLong;
import com.google.code.or.common.glossary.column.BitColumn;
import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.io.XInputStream;
import com.google.code.or.io.impl.XInputStreamImpl;

/**
 * 
 * @author Jingqi Xu
 */
public class XDeserializer implements XInputStream {
	//
	private final XInputStream tis;

	/**
	 * 
	 */
	public XDeserializer(byte[] data) {
		this.tis = new XInputStreamImpl(new ByteArrayInputStream(data));
	}
	
	/**
	 * 
	 */
	public void close() throws IOException {
		this.tis.close();
	}
	
	public int available() throws IOException {
		return this.tis.available();
	}
	
	public boolean hasMore() throws IOException {
		return this.tis.hasMore();
	}
	
	public void setReadLimit(int limit) throws IOException {
		this.tis.setReadLimit(limit);
	}
	
	/**
	 * 
	 */
	public long skip(long n) throws IOException {
		return this.tis.skip(n);
	}
	
	public int readInt(int length) throws IOException {
		return this.tis.readInt(length);
	}

	public long readLong(int length) throws IOException {
		return this.tis.readLong(length);
	}
	
	public byte[] readBytes(int length) throws IOException {
		return this.tis.readBytes(length);
	}
	
	public UnsignedLong readUnsignedLong() throws IOException {
		return tis.readUnsignedLong();
	}
	
	public StringColumn readLengthCodedString() throws IOException {
		return this.tis.readLengthCodedString();
	}
	
	public StringColumn readNullTerminatedString() throws IOException {
		return this.tis.readNullTerminatedString();
	}
	
	public StringColumn readFixedLengthString(int length) throws IOException {
		return this.tis.readFixedLengthString(length);
	}
	
	public BitColumn readBit(int length, boolean isBigEndian) throws IOException {
		return this.tis.readBit(length, isBigEndian);
	}
}
