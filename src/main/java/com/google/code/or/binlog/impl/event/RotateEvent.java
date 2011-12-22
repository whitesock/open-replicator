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
 * Written when mysqld switches to a new binary log file. This occurs when someone 
 * issues a FLUSH LOGS statement or the current binary log file becomes too large. 
 * The maximum size is determined by max_binlog_size. 
 * 
 * @author Jingqi Xu
 */
public final class RotateEvent extends AbstractBinlogEventV4 {
	//
	public static final int EVENT_TYPE = MySQLConstants.ROTATE_EVENT;
	
	//
	private long binlogPosition;
	private StringColumn binlogFileName;
	
	/**
	 * 
	 */
	public RotateEvent() {
	}
	
	public RotateEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("binlogPosition", binlogPosition)
		.append("binlogFileName", binlogFileName).toString();
	}
	
	/**
	 * 
	 */
	public long getBinlogPosition() {
		return binlogPosition;
	}

	public void setBinlogPosition(long binlogPosition) {
		this.binlogPosition = binlogPosition;
	}
	
	public StringColumn getBinlogFileName() {
		return binlogFileName;
	}

	public void setBinlogFileName(StringColumn binlogFileName) {
		this.binlogFileName = binlogFileName;
	}
}
