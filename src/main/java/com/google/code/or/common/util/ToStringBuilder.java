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
package com.google.code.or.common.util;

/**
 * 
 * @author Jingqi Xu
 */
public final class ToStringBuilder {
	//
	private int count;
	private final StringBuilder builder;

	/**
	 * 
	 */
	public ToStringBuilder(Object object) {
		String name = ClassUtils.getShortClassName(object.getClass().getName());
		this.builder = new StringBuilder(32); this.builder.append(name).append("[");
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.builder.append("]").toString();
	}

	/**
	 * 
	 */
	public ToStringBuilder append(String key, int value) {
		if(count++ > 0) this.builder.append(',');
		this.builder.append(key).append('=').append(value);
		return this;
	}
	
	public ToStringBuilder append(String key, long value) {
		if(count++ > 0) this.builder.append(',');
		this.builder.append(key).append('=').append(value);
		return this;
	}
	
	public ToStringBuilder append(String key, byte value) {
		if(count++ > 0) this.builder.append(',');
		this.builder.append(key).append('=').append(value);
		return this;
	}
	
	public ToStringBuilder append(String key, String value) {
		if(count++ > 0) this.builder.append(',');
		this.builder.append(key).append('=').append(value == null ? "<null>" : value);
		return this;
	}
	
	public ToStringBuilder append(String key, Object value) {
		if(count++ > 0) this.builder.append(',');
		this.builder.append(key).append('=').append(value == null ? "<null>" : value);
		return this;
	}
}
