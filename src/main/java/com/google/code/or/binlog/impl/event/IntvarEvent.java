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
import com.google.code.or.common.glossary.UnsignedLong;
import com.google.code.or.common.util.MySQLConstants;

/**
 * Written every time a statement uses an AUTO_INCREMENT column or the LAST_INSERT_ID() function; 
 * precedes other events for the statement. This is written only before a QUERY_EVENT and is not 
 * used with row-based logging. An INTVAR_EVENT is written with a "subtype" in the event data part:
 *   INSERT_ID_EVENT indicates the value to use for an AUTO_INCREMENT column in the next statement.
 *   LAST_INSERT_ID_EVENT indicates the value to use for the LAST_INSERT_ID() function in the next statement. 
 *   
 * @author Jingqi Xu
 */
public final class IntvarEvent extends AbstractBinlogEventV4 {
	//
	public static final int EVENT_TYPE = MySQLConstants.INTVAR_EVENT;
	
	//
	private int type;
	private UnsignedLong value;
	
	/**
	 * 
	 */
	public IntvarEvent() {
	}
	
	public IntvarEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("type", type)
		.append("value", value).toString();
	}
	
	/**
	 * 
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public UnsignedLong getValue() {
		return value;
	}

	public void setValue(UnsignedLong value) {
		this.value = value;
	}
}
