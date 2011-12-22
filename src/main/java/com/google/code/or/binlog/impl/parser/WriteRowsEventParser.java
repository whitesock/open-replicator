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
import com.google.code.or.binlog.BinlogParsingContext;
import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.google.code.or.binlog.impl.event.WriteRowsEvent;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class WriteRowsEventParser extends AbstractRowEventParser {

	/**
	 * 
	 */
	public WriteRowsEventParser() {
		super(WriteRowsEvent.EVENT_TYPE);
	}
	
	/**
	 * 
	 */
	public void parse(XInputStream is, BinlogEventV4Header header, BinlogParsingContext context)
	throws IOException {
		//
		final long tableId = is.readLong(6);
		final TableMapEvent tme = context.getTableMapEvent(tableId);
		if(this.filter != null && !this.filter.accepts(tableId, tme)) {
			is.skip(is.available());
			return;
		}
		
		//
		final WriteRowsEvent event = new WriteRowsEvent(header);
		event.setTableId(tableId);
		event.setReserved(is.readInt(2));
		event.setColumnCount(is.readUnsignedLong()); 
		event.setUsedColumns(is.readBit(event.getColumnCount().intValue(), true));
		event.setRows(parseRows(is, tme, event));
		context.getListener().onEvents(event);
	}
	
	/**
	 * 
	 */
	protected List<Row> parseRows(XInputStream is, TableMapEvent tme, WriteRowsEvent wre)
	throws IOException {
		final List<Row> r = new LinkedList<Row>();
		while(is.available() > 0) {
			r.add(parseRow(is, tme, wre.getUsedColumns()));
		}
		return r;
	}
}
