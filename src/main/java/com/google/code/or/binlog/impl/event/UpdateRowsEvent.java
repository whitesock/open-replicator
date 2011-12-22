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
import com.google.code.or.common.glossary.Pair;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.common.glossary.UnsignedLong;
import com.google.code.or.common.glossary.column.BitColumn;
import com.google.code.or.common.util.MySQLConstants;

/**
 * Used for row-based binary logging. This event logs updates of rows in a single table. 
 * 
 * @author Jingqi Xu
 */
public final class UpdateRowsEvent extends AbstractRowEvent {
	//
	public static final int EVENT_TYPE = MySQLConstants.UPDATE_ROWS_EVENT;
	
	//
	private UnsignedLong columnCount;
	private BitColumn usedColumnsBefore;
	private BitColumn usedColumnsAfter;
	private List<Pair<Row>> rows;
	
	/**
	 * 
	 */
	public UpdateRowsEvent() {
	}
	
	public UpdateRowsEvent(BinlogEventV4Header header) {
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
		.append("usedColumnsBefore", usedColumnsBefore)
		.append("usedColumnsAfter", usedColumnsAfter)
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

	public BitColumn getUsedColumnsBefore() {
		return usedColumnsBefore;
	}

	public void setUsedColumnsBefore(BitColumn usedColumnsBefore) {
		this.usedColumnsBefore = usedColumnsBefore;
	}
	
	public BitColumn getUsedColumnsAfter() {
		return usedColumnsAfter;
	}

	public void setUsedColumnsAfter(BitColumn usedColumnsAfter) {
		this.usedColumnsAfter = usedColumnsAfter;
	}
	
	public List<Pair<Row>> getRows() {
		return rows;
	}

	public void setRows(List<Pair<Row>> rows) {
		this.rows = rows;
	}
}
