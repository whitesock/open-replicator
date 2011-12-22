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

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.binlog.BinlogParsingContext;
import com.google.code.or.binlog.impl.event.XidEvent;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class XidEventParser extends AbstractBinlogEventParser {

	/**
	 * 
	 */
	public XidEventParser() {
		super(XidEvent.EVENT_TYPE);
	}

	/**
	 * Note: Contrary to all other numeric fields, the XID transaction number is not always 
	 * written in little-endian format. The bytes are copied unmodified from memory to disk, 
	 * so the format is machine-dependent. Hence, when replicating from a little-endian to a 
	 * big-endian machine (or vice versa), the numeric value of transaction numbers will differ. 
	 * In particular, the output of mysqlbinlog differs. This should does not cause inconsistencies 
	 * in replication because the only important property of transaction numbers is that different 
	 * transactions have different numbers (relative order does not matter). 
	 */
	public void parse(XInputStream is, BinlogEventV4Header header, BinlogParsingContext context)
	throws IOException {
		final XidEvent event = new XidEvent(header);
		event.setXid(is.readLong(8)); 
		context.getListener().onEvents(event);
	}
}
