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
package com.google.code.or.binlog.impl.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.binlog.BinlogParserContext;
import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.google.code.or.binlog.impl.event.WriteRowsEventV2;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class WriteRowsEventV2Parser extends AbstractRowEventParser {

	/**
	 * 
	 */
	public WriteRowsEventV2Parser() {
		super(WriteRowsEventV2.EVENT_TYPE);
	}
	
	/**
	 * 
	 */
	public void parse(XInputStream is, BinlogEventV4Header header, BinlogParserContext context)
	throws IOException {
		//
		final long tableId = is.readLong(6);
		final TableMapEvent tme = context.getTableMapEvent(tableId);
		if(this.rowEventFilter != null && !this.rowEventFilter.accepts(header, context, tme)) {
			is.skip(is.available());
			return;
		}
		
		//
		final WriteRowsEventV2 event = new WriteRowsEventV2(header);
		event.setTableId(tableId);
		event.setReserved(is.readInt(2));
		event.setExtraInfoLength(is.readInt(2));
		if(event.getExtraInfoLength() > 2) event.setExtraInfo(is.readBytes(event.getExtraInfoLength() - 2));
		event.setColumnCount(is.readUnsignedLong()); 
		event.setUsedColumns(is.readBit(event.getColumnCount().intValue()));
		event.setRows(parseRows(is, tme, event));
		context.getEventListener().onEvents(event);
	}
	
	/**
	 * 
	 */
	protected List<Row> parseRows(XInputStream is, TableMapEvent tme, WriteRowsEventV2 wre)
	throws IOException {
		final List<Row> r = new LinkedList<Row>();
		while(is.available() > 0) {
			r.add(parseRow(is, tme, wre.getUsedColumns()));
		}
		return r;
	}
}
