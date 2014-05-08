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

import java.util.List;

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.common.glossary.UnsignedLong;
import com.google.code.or.common.glossary.column.BitColumn;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.common.util.ToStringBuilder;

/**
 * Used for row-based binary logging. This event logs inserts of rows in a single table. 
 * 
 * @author Jingqi Xu
 */
public final class WriteRowsEventV2 extends AbstractRowEvent {
	//
	public static final int EVENT_TYPE = MySQLConstants.WRITE_ROWS_EVENT_V2;
	
	//
	private int extraInfoLength;
	private byte extraInfo[];
	private UnsignedLong columnCount;
	private BitColumn usedColumns;
	private List<Row> rows;
	
	/**
	 * 
	 */
	public WriteRowsEventV2() {
	}
	
	public WriteRowsEventV2(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("header", header)
		.append("tableId", tableId)
		.append("reserved", reserved)
		.append("extraInfoLength", extraInfoLength)
		.append("extraInfo", extraInfo)
		.append("columnCount", columnCount)
		.append("usedColumns", usedColumns)
		.append("rows", rows).toString();
	}
	
	/**
	 * 
	 */
	public int getExtraInfoLength() {
		return extraInfoLength;
	}

	public void setExtraInfoLength(int extraInfoLength) {
		this.extraInfoLength = extraInfoLength;
	}
	
	public byte[] getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(byte[] extraInfo) {
		this.extraInfo = extraInfo;
	}
	
	public UnsignedLong getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(UnsignedLong columnCount) {
		this.columnCount = columnCount;
	}

	public BitColumn getUsedColumns() {
		return usedColumns;
	}

	public void setUsedColumns(BitColumn usedColumns) {
		this.usedColumns = usedColumns;
	}
	
	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
}
