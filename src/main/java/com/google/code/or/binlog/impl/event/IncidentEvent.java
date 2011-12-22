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
import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.common.util.MySQLConstants;

/**
 * Used to log an out of the ordinary event that occurred on the master. 
 * It notifies the slave that something happened on the master that might 
 * cause data to be in an inconsistent state. 
 * 
 * @author Jingqi Xu
 * @see sql/rpl_constants.h
 */
public final class IncidentEvent extends AbstractBinlogEventV4 {
	//
	public static final int EVENT_TYPE = MySQLConstants.INCIDENT_EVENT;
	
	//
	private int incidentNumber;
	private int messageLength;
	private StringColumn message;
	
	/**
	 * 
	 */
	public IncidentEvent() {
	}
	
	public IncidentEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("incidentNumber", incidentNumber)
		.append("messageLength", messageLength)
		.append("message", message).toString();
	}
	
	/**
	 * 
	 */
	public int getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(int incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public StringColumn getMessage() {
		return message;
	}

	public void setMessage(StringColumn message) {
		this.message = message;
	}
}
