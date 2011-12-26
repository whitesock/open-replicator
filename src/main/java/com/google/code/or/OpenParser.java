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
package com.google.code.or;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogParser;
import com.google.code.or.binlog.impl.FileBasedBinlogParser;
import com.google.code.or.binlog.impl.parser.DeleteRowsEventParser;
import com.google.code.or.binlog.impl.parser.FormatDescriptionEventParser;
import com.google.code.or.binlog.impl.parser.IncidentEventParser;
import com.google.code.or.binlog.impl.parser.IntvarEventParser;
import com.google.code.or.binlog.impl.parser.QueryEventParser;
import com.google.code.or.binlog.impl.parser.RandEventParser;
import com.google.code.or.binlog.impl.parser.RotateEventParser;
import com.google.code.or.binlog.impl.parser.StopEventParser;
import com.google.code.or.binlog.impl.parser.TableMapEventParser;
import com.google.code.or.binlog.impl.parser.UpdateRowsEventParser;
import com.google.code.or.binlog.impl.parser.UserVarEventParser;
import com.google.code.or.binlog.impl.parser.WriteRowsEventParser;
import com.google.code.or.binlog.impl.parser.XidEventParser;

/**
 * 
 * @author Jingqi Xu
 */
public class OpenParser {
	//
	protected long stopPosition;
	protected long startPosition;
	protected String binlogFileName;
	protected String binlogFilePath;
	
	//
	protected BinlogParser binlogParser;
	protected BinlogEventListener binlogEventListener;
	protected final AtomicBoolean running = new AtomicBoolean(false);
	
	/**
	 * 
	 */
	public boolean isRunning() {
		return this.running.get();
	}
	
	public void start() throws Exception {
		//
		if(!this.running.compareAndSet(false, true)) {
			return;
		}
		
		//
		if(this.binlogParser == null) this.binlogParser = getDefaultBinlogParser();
		this.binlogParser.setEventListener(this.binlogEventListener);
		this.binlogParser.start();
	}

	public void stop(long timeout, TimeUnit unit) throws Exception {
		//
		if(!this.running.compareAndSet(true, false)) {
			return;
		}
		
		//
		this.binlogParser.stop(timeout, unit);
	}
	
	/**
	 * 
	 */
	public long getStopPosition() {
		return stopPosition;
	}
	
	public void setStopPosition(long position) {
		this.stopPosition = position;
	}
	
	public long getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(long position) {
		this.startPosition = position;
	}

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

	/**
	 * 
	 */
	public BinlogParser getBinlogParser() {
		return binlogParser;
	}

	public void setBinlogParser(BinlogParser parser) {
		this.binlogParser = parser;
	}
	
	public BinlogEventListener getBinlogEventListener() {
		return binlogEventListener;
	}

	public void setBinlogEventListener(BinlogEventListener listener) {
		this.binlogEventListener = listener;
	}

	/**
	 * 
	 */
	protected FileBasedBinlogParser getDefaultBinlogParser() throws Exception {
		//
		final FileBasedBinlogParser r = new FileBasedBinlogParser();
		r.registgerEventParser(new StopEventParser());
		r.registgerEventParser(new RotateEventParser());
		r.registgerEventParser(new IntvarEventParser());
		r.registgerEventParser(new XidEventParser());
		r.registgerEventParser(new RandEventParser());
		r.registgerEventParser(new QueryEventParser());
		r.registgerEventParser(new UserVarEventParser());
		r.registgerEventParser(new IncidentEventParser());
		r.registgerEventParser(new TableMapEventParser());
		r.registgerEventParser(new WriteRowsEventParser());
		r.registgerEventParser(new DeleteRowsEventParser());
		r.registgerEventParser(new UpdateRowsEventParser());
		r.registgerEventParser(new FormatDescriptionEventParser());
		
		//
		r.setStopPosition(this.stopPosition);
		r.setStartPosition(this.startPosition);
		r.setBinlogFileName(this.binlogFileName);
		r.setBinlogFilePath(this.binlogFilePath);
		return r;
	}
}
