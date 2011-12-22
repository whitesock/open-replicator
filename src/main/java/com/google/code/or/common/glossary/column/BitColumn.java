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
package com.google.code.or.common.glossary.column;

import com.google.code.or.common.glossary.Column;

/**
 * 
 * @author Jingqi Xu
 */
public final class BitColumn implements Column {
	//
	private static final long serialVersionUID = 4193150509864408687L;
	
	//
	private static final int BIT_MASKS[] = {1 << 0, 1 << 1, 1 << 2, 1 << 3, 1 << 4, 1 << 5, 1 << 6, 1 << 7};
	
	//
	private final int length;
	private final byte[] value;

	/**
	 * 
	 */
	private BitColumn(int length, byte[] value) {
		this.length = length;
		this.value = value;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		final StringBuilder r = new StringBuilder(this.length);
		for(int i = 0; i < this.length; i++) {
			r.append(get(i) ? "1" : "0");
		}
		return r.toString();
	}
	
	/**
	 * 
	 */
	public int getLength() {
		return this.length;
	}
	
	public byte[] getValue() {
		return this.value;
	}

	/**
	 * 
	 */
	public boolean get(int index) {
		final int byteIndex = (index >> 3);
		final int bitIndex = (index - (byteIndex << 3));
		return (this.value[byteIndex] & BIT_MASKS[bitIndex]) != 0;
	}
	
	public void set(int index) {
		final int byteIndex = (index >> 3);
		final int bitIndex = (index - (byteIndex << 3));
		this.value[byteIndex] |= BIT_MASKS[bitIndex];
	}

	/**
	 * 
	 */
	public static final BitColumn valueOf(int length, byte[] value) {
		if(length < 0 || length > (value.length << 3)) throw new IllegalArgumentException("invalid length: " + length);
		return new BitColumn(length, value);
	}
}
