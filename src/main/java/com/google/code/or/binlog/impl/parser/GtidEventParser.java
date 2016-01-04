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
import com.google.code.or.binlog.BinlogParserContext;
import com.google.code.or.binlog.impl.event.GtidEvent;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.XInputStream;

/**
 * GTID Event
 *
 * <p>
 *     Event format:
 *     <pre>
 *         +-------------------+
 *         | 1B commit flag    |
 *         +-------------------+
 *         | 16B Source ID     |
 *         +-------------------+
 *         | 8B Txn ID         |
 *         +-------------------+
 *         | ...               |
 *         +-------------------+
 *     </pre>
 * </p>
 * @author brandtg
 */
public class GtidEventParser extends AbstractBinlogEventParser {

	/**
	 * 
	 */
	public GtidEventParser() {
		super(MySQLConstants.GTID_LOG_EVENT);
	}

	/**
	 * 
	 */
	public void parse(XInputStream is, BinlogEventV4Header header, BinlogParserContext context) throws IOException {
		GtidEvent event = new GtidEvent(header);
		is.readBytes(1); // commit flag, always true
		event.setSourceId(is.readBytes(16));
		event.setTransactionId(is.readLong(8, true));
		//event.setTransactionId(ByteBuffer.wrap(is.readBytes(8)).order(ByteOrder.LITTLE_ENDIAN).getLong());
		is.skip(is.available()); // position at next event
		context.getEventListener().onEvents(event);
	}
}
