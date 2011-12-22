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
package com.google.code.or.net.impl.packet.command;

import java.io.IOException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.util.XSerializer;

/**
 * 
 * @author Jingqi Xu
 */
public class ComBinlogDumpPacket extends AbstractCommandPacket {
	//
	private static final long serialVersionUID = 449639496684376511L;
	
	//
	private long binlogPosition;
	private int binlogFlag;
	private long serverId;
	private StringColumn binlogFileName;
	
	/**
	 * 
	 */
	public ComBinlogDumpPacket() {
		super(MySQLConstants.COM_BINLOG_DUMP);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("binlogPosition", binlogPosition)
		.append("binlogFlag", binlogFlag)
		.append("serverId", serverId)
		.append("binlogFileName", binlogFileName).toString();
	}
	
	/**
	 * 
	 */
	public byte[] getPacketBody() throws IOException {
		final XSerializer ps = new XSerializer();
		ps.writeInt(this.command, 1);
		ps.writeLong(this.binlogPosition, 4);
		ps.writeInt(this.binlogFlag, 2);
		ps.writeLong(this.serverId, 4);
		ps.writeFixedLengthString(this.binlogFileName);
		return ps.toByteArray();
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

	public int getBinlogFlag() {
		return binlogFlag;
	}

	public void setBinlogFlag(int binlogFlag) {
		this.binlogFlag = binlogFlag;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public StringColumn getBinlogFileName() {
		return binlogFileName;
	}

	public void setBinlogFileName(StringColumn binlogFileName) {
		this.binlogFileName = binlogFileName;
	}
}
