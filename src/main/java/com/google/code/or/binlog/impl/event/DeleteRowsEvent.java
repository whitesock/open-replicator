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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.common.glossary.UnsignedLong;
import com.google.code.or.common.glossary.column.BitColumn;
import com.google.code.or.common.util.MySQLConstants;

/**
 * Used for row-based binary logging. This event logs deletions of rows in a single table. 
 * 
 * @author Jingqi Xu
 */
public final class DeleteRowsEvent extends AbstractRowEvent {
	//
	public static final int EVENT_TYPE = MySQLConstants.DELETE_ROWS_EVENT;
	
	//
	private UnsignedLong columnCount;
	private BitColumn usedColumns;
	private List<Row> rows;
	
	/**
	 * 
	 */
	public DeleteRowsEvent() {
	}
	
	public DeleteRowsEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("tableId", tableId)
		.append("reserved", reserved)
		.append("columnCount", columnCount)
		.append("usedColumns", usedColumns)
		.append("rows", rows).toString();
	}
	
	/**
	 * 
	 */
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
