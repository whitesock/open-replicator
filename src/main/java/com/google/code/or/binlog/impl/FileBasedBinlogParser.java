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
package com.google.code.or.binlog.impl;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventParser;
import com.google.code.or.binlog.impl.event.BinlogEventV4HeaderImpl;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class FileBasedBinlogParser extends AbstractBinlogParser {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedBinlogParser.class);
	
	//
	protected long startPosition = 4;
	protected long stopPosition = -1;
	
	/**
	 * 
	 */
	public long getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(long startPosition) {
		this.startPosition = startPosition;
	}

	public long getStopPosition() {
		return stopPosition;
	}

	public void setStopPosition(long stopPosition) {
		this.stopPosition = stopPosition;
	}
	
	/**
	 * 
	 */
	@Override
	protected void parse(XInputStream is) throws Exception {
		//
		final int length = checkBinlogMagic(is);
		
		//
		is.skip(this.startPosition - length); // TODO
		
		//
		while(isRunning() && is.available() > 0) {
			try {
				//
				final BinlogEventV4HeaderImpl header = new BinlogEventV4HeaderImpl();
				header.setTimestamp(is.readLong(4) * 1000L);
				header.setEventType(is.readInt(1));
				header.setServerId(is.readLong(4));
				header.setEventLength(is.readInt(4));
				header.setNextPosition(is.readLong(4));
				header.setFlags(is.readInt(2));
				is.setReadLimit((int)(header.getEventLength() - header.getHeaderLength())); // Ensure the event boundary
				if(isVerbose() && LOGGER.isInfoEnabled()) {
					LOGGER.info("received an event, header: {}", header);
				}
				
				//
				if(this.stopPosition > 0 && header.getPosition() > this.stopPosition) {
					break;
				}
				
				// Parse the event body
				if(this.eventFilter != null && !this.eventFilter.accepts(header, this.context)) {
					this.defaultParser.parse(is, header, this.context);
				} else {
					BinlogEventParser parser = getEventParser(header.getEventType());
					if(parser == null) parser = this.defaultParser;
					parser.parse(is, header, this.context);
				}
				
				// Ensure the packet boundary
				if(is.available() != 0) {
					throw new NestableRuntimeException("assertion failed, available: " + is.available() + ", event type: " + header.getEventType());
				}
			} finally {
				is.setReadLimit(0);
			}
		}
	}

	/**
	 * 
	 */
	protected int checkBinlogMagic(XInputStream is) throws Exception {
		//
		final int length = MySQLConstants.BINLOG_MAGIC.length;
		final byte[] magic = is.readBytes(length);
		for(int i = 0; i < length; i++) {
			if(magic[i] != MySQLConstants.BINLOG_MAGIC[i]) {
				throw new NestableRuntimeException("assertion failed, invalid magic: " + magic);
			}
		}
		return length;
	}
}
