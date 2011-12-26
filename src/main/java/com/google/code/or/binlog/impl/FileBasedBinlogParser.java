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

import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventParser;
import com.google.code.or.binlog.impl.event.BinlogEventV4HeaderImpl;
import com.google.code.or.common.util.CodecUtils;
import com.google.code.or.common.util.IOUtils;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.XInputStream;
import com.google.code.or.io.impl.XInputStreamImpl;
import com.google.code.or.io.util.RamdomAccessFileInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class FileBasedBinlogParser extends AbstractBinlogParser {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedBinlogParser.class);
	
	//
	protected XInputStream is;
	protected String binlogFileName;
	protected String binlogFilePath;
	protected long stopPosition = 0;
	protected long startPosition = 4;

	
	/**
	 * 
	 */
	public FileBasedBinlogParser() {
	}
	
	@Override
	protected void doStart() throws Exception {
		this.is = open(this.binlogFilePath + "/" +  this.binlogFileName);
	}

	@Override
	protected void doStop(long timeout, TimeUnit unit) throws Exception {
		IOUtils.closeQuietly(this.is);
	}
	
	/**
	 * 
	 */
	public String getBinlogFileName() {
		return binlogFileName;
	}
	
	public void setBinlogFileName(String name) {
		this.binlogFileName = name;
	}

	public String getBinlogFilePath() {
		return binlogFilePath;
	}

	public void setBinlogFilePath(String path) {
		this.binlogFilePath = path;
	}
	
	public long getStopPosition() {
		return stopPosition;
	}
	
	public void setStopPosition(long stopPosition) {
		this.stopPosition = stopPosition;
	}
	
	public long getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(long startPosition) {
		this.startPosition = startPosition;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doParse() throws Exception {
		//
		final Context context = new Context(this.binlogFileName);
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
					LOGGER.info("read an event, header: {}", header);
				}
				
				//
				if(this.stopPosition > 0 && header.getPosition() > this.stopPosition) {
					break;
				}
				
				// Parse the event body
				if(this.eventFilter != null && !this.eventFilter.accepts(header, context)) {
					this.defaultParser.parse(is, header, context);
				} else {
					BinlogEventParser parser = getEventParser(header.getEventType());
					if(parser == null) parser = this.defaultParser;
					parser.parse(is, header, context);
				}
				
				// Ensure the packet boundary
				if(is.available() != 0) {
					throw new NestableRuntimeException("assertion failed, available: " + is.available() + ", event type: " + header.getEventType());
				}
			} catch(Exception e) {
				IOUtils.closeQuietly(is);
				throw e;
			} finally {
				is.setReadLimit(0);
			}
		}
	}

	/**
	 * 
	 */
	protected XInputStream open(String path) throws Exception {
		//
		final RandomAccessFile file = new RandomAccessFile(path, "r");
		final XInputStream is = new XInputStreamImpl(new RamdomAccessFileInputStream(file));
		try {
			// Check binlog magic
			final byte[] magic = is.readBytes(MySQLConstants.BINLOG_MAGIC.length);
			if(!CodecUtils.equals(magic, MySQLConstants.BINLOG_MAGIC)) {
				throw new NestableRuntimeException("invalid binlog magic, file: " + path);
			}
			
			//
			if(this.startPosition > MySQLConstants.BINLOG_MAGIC.length) {
				is.skip(this.startPosition - MySQLConstants.BINLOG_MAGIC.length);
			}
			return is;
		} catch(Exception e) {
			IOUtils.closeQuietly(is);
			throw e;
		}
	}
}
