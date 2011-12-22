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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventParser;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.impl.event.BinlogEventV4HeaderImpl;
import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class ReplicationBasedBinlogParser extends AbstractBinlogParser implements BinlogEventListener {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationBasedBinlogParser.class);
	
	//
	protected final Map<Long, TableMapEvent> tableMaps;
	
	/**
	 * 
	 */
	public ReplicationBasedBinlogParser() {
		this.tableMaps = new HashMap<Long, TableMapEvent>();
	}
	
	@Override
	protected void doStart() {
		LOGGER.info(getClass().getName() + " was successfully started"); 
	}

	@Override
	protected void doStop(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		LOGGER.info(getClass().getName() + " was successfully stopped"); 
	}
	
	/**
	 * 
	 */
	public void parse(XInputStream is) throws IOException {
		//
		while(isRunning()) {
			try {
				// Parse packet
				final int packetLength = is.readInt(3);
				final int packetSequence = is.readInt(1);
				is.setReadLimit(packetLength); // Enforce the packet boundary
				is.skip(1);
				if(isVerbose() && LOGGER.isInfoEnabled()) {
					LOGGER.info("received a packet, length: {}, sequence: {}", packetLength, packetSequence);
				}
				
				// Parse the event header
				final BinlogEventV4HeaderImpl header = new BinlogEventV4HeaderImpl();
				header.setTimestamp(is.readLong(4) * 1000L);
				header.setEventType(is.readInt(1));
				header.setServerId(is.readLong(4));
				header.setEventLength(is.readInt(4));
				header.setNextPosition(is.readLong(4));
				header.setFlags(is.readInt(2));
				
				// Parse the event body
				final Context context = new Context();
				context.setHeader(header);
				context.setListener(this);
				context.setTableMaps(this.tableMaps);
				final BinlogEventParser parser = getEventParser(header.getEventType());
				if(parser != null) {
					parser.parse(is, context);
				} else {
					this.defaultEventParser.parse(is, context);
				}
				
				// Enforce the packet boundary
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
	public void onEvents(BinlogEventV4 event) {
		//
		if(event == null) return;
		if(event instanceof TableMapEvent) {
			final TableMapEvent tme = (TableMapEvent)event;
			this.tableMaps.put(tme.getTableId(), tme);
		}
		
		//
		this.listener.onEvents(event);
	}
}
