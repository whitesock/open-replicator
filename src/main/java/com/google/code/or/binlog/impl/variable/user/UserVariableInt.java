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
package com.google.code.or.binlog.impl.variable.user;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.common.util.MySQLConstants;

/**
 * 
 * @author Jingqi Xu
 */
public class UserVariableInt extends AbstractUserVariable {
	//
	public static final int TYPE = MySQLConstants.INT_RESULT;
	
	//
	private final long value;
	@SuppressWarnings("unused") private final int todo; // TODO

	/**
	 * 
	 */
	public UserVariableInt(long value, int todo) {
		super(TYPE);
		this.value = value;
		this.todo = todo;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("value", value).toString();
	}
	
	/**
	 * 
	 */
	public Long getValue() {
		return this.value;
	}
}
