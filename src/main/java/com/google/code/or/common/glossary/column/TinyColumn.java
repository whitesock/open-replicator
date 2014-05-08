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
public final class TinyColumn implements Column {
	//
	private static final long serialVersionUID = 3629858638897033423L;
	
	//
	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 256;
	
	//
	private static final TinyColumn[] CACHE = new TinyColumn[256];
	static {
		for(int i = 0; i < 128; i++) {
			CACHE[i] = new TinyColumn(i);
		}
		for(int i = 128; i < 256; i++) {
			CACHE[i] = new TinyColumn(i - 256);
		}
	}
	
	//
	private final int value;
	
	/**
	 * 
	 */
	private TinyColumn(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return String.valueOf(this.value);
	}

	/**
	 * 
	 */
	public Integer getValue() {
		return this.value;
	}
	
	/**
	 * 
	 */
	public static final TinyColumn valueOf(int value) {
		if(value < MIN_VALUE || value > MAX_VALUE) throw new IllegalArgumentException("invalid value: " + value);
		return CACHE[value];
	}
}
