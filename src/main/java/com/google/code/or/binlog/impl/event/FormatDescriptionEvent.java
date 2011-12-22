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
 * A descriptor event that is written to the beginning of the each binary log file. 
 * This event is used as of MySQL 5.0; it supersedes START_EVENT_V3. 
 * 
 * @author Jingqi Xu
 */
public final class FormatDescriptionEvent extends AbstractBinlogEventV4 {
	//
	public static final int EVENT_TYPE = MySQLConstants.FORMAT_DESCRIPTION_EVENT;
	
	//
	private int binlogVersion;
	private StringColumn serverVersion;
	private long createTimestamp;
	private int headerLength;
	private byte[] eventTypes;
	
	/**
	 * 
	 */
	public FormatDescriptionEvent() {
	}
	
	public FormatDescriptionEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("binlogVersion", binlogVersion)
		.append("serverVersion", serverVersion)
		.append("createTimestamp", createTimestamp)
		.append("headerLength", headerLength)
		.append("eventTypes", eventTypes).toString();
	}
	
	/**
	 * 
	 */
	public int getBinlogVersion() {
		return binlogVersion;
	}

	public void setBinlogVersion(int binlogVersion) {
		this.binlogVersion = binlogVersion;
	}

	public StringColumn getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(StringColumn serverVersion) {
		this.serverVersion = serverVersion;
	}

	public long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
	public int getHeaderLength() {
		return headerLength;
	}

	public void setHeaderLength(int headerLength) {
		this.headerLength = headerLength;
	}
	
	public byte[] getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(byte[] eventTypes) {
		this.eventTypes = eventTypes;
	}
}
