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
package com.google.code.or.binlog.impl.event;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.binlog.UserVariable;
import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.common.util.MySQLConstants;

/**
 * Written every time a statement uses a user variable; precedes other events for the statement. 
 * Indicates the value to use for the user variable in the next statement. 
 * This is written only before a QUERY_EVENT and is not used with row-based logging. 
 * 
 * @author Jingqi Xu
 */
public final class UserVarEvent extends AbstractBinlogEventV4 {
	//
	public static final int EVENT_TYPE = MySQLConstants.USER_VAR_EVENT;
	
	//
	private int varNameLength;
	private StringColumn varName;
	private int isNull;
	private int varType;
	private int varCollation;
	private int varValueLength;
	private UserVariable varValue;
	
	/**
	 * 
	 */
	public UserVarEvent() {
	}
	
	public UserVarEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("varNameLength", varNameLength)
		.append("varName", varName)
		.append("isNull", isNull)
		.append("varType", varType)
		.append("varCollation", varCollation)
		.append("varValueLength", varValueLength)
		.append("varValue", varValue).toString();
	}
	
	/**
	 * 
	 */
	public int getVarNameLength() {
		return varNameLength;
	}

	public void setVarNameLength(int varNameLength) {
		this.varNameLength = varNameLength;
	}

	public StringColumn getVarName() {
		return varName;
	}

	public void setVarName(StringColumn varName) {
		this.varName = varName;
	}

	public int getIsNull() {
		return isNull;
	}

	public void setIsNull(int isNull) {
		this.isNull = isNull;
	}

	public int getVarType() {
		return varType;
	}

	public void setVarType(int variableType) {
		this.varType = variableType;
	}

	public int getVarCollation() {
		return varCollation;
	}

	public void setVarCollation(int varCollation) {
		this.varCollation = varCollation;
	}

	public int getVarValueLength() {
		return varValueLength;
	}

	public void setVarValueLength(int varValueLength) {
		this.varValueLength = varValueLength;
	}

	public UserVariable getVarValue() {
		return varValue;
	}

	public void setVarValue(UserVariable varValue) {
		this.varValue = varValue;
	}
}
