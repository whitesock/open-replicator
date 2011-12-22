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
public final class YearColumn implements Column {
	//
	private static final long serialVersionUID = 6428744630692270846L;
	
	//
	private static final YearColumn[] CACHE = new YearColumn[255];
	static {
		for(int i = 0; i < CACHE.length; i++) {
			CACHE[i] = new YearColumn(i + 1900);
		}
	}
	
	//
	private final int value;
	
	/**
	 * 
	 */
	private YearColumn(int value) {
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
	public static final YearColumn valueOf(int value) {
		final int index = value - 1900;
		return (index >= 0 && index < CACHE.length) ? CACHE[index] : new YearColumn(value);
	}
}
