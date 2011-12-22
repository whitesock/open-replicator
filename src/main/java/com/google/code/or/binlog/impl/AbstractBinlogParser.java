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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventParser;
import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.binlog.BinlogParser;
import com.google.code.or.binlog.ParserContext;
import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.google.code.or.binlog.impl.parser.NopEventParser;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public abstract class AbstractBinlogParser implements BinlogParser {
	//
	protected Thread worker;
	protected BinlogEventListener listener;
	protected BinlogEventParser defaultEventParser;
	protected final AtomicBoolean running = new AtomicBoolean(false);
	protected final AtomicBoolean verbose = new AtomicBoolean(false);
	protected final BinlogEventParser[] parsers = new BinlogEventParser[128];
	
	//
	protected abstract void doParse(XInputStream is) throws Exception;
	

	/**
	 * 
	 */
	public AbstractBinlogParser() {
		this.defaultEventParser = new NopEventParser();
	}
	
	/**
	 * 
	 */
	public final boolean isRunning() {
		return this.running.get();
	}
	
	public final void start(XInputStream is) throws Exception {
		//
		if(this.running.compareAndSet(false, true)) {
			return;
		}
		
		//
		this.worker = new Worker(is);
		this.worker.start();
	}

	public final void stop() throws Exception {
		//
		if(this.running.compareAndSet(true, false)) {
			return;
		}
		
		//
		this.worker.join();
	}

	/**
	 * 
	 */
	public final boolean isVerbose() {
		return this.verbose.get();
	}
	
	public final void setVerbose(boolean verbose) {
		this.verbose.set(verbose);
	}
	
	public BinlogEventListener getBinlogEventListener() {
		return listener;
	}
	
	public void setBinlogEventListener(BinlogEventListener listener) {
		this.listener = listener;
	}
	
	public BinlogEventParser getDefaultEventParser() {
		return defaultEventParser;
	}

	public void setDefaultEventParser(BinlogEventParser parser) {
		this.defaultEventParser = parser;
	}
	
	public BinlogEventParser getEventParser(int eventType) {
		return this.parsers[eventType];
	}
	
	public void registgerEventParser(BinlogEventParser parser) {
		this.parsers[parser.getEventType()] = parser;
	}
	
	public BinlogEventParser unregistgerEventParser(int eventType) {
		return this.parsers[eventType] = null;
	}
	
	public List<BinlogEventParser> getEventParsers() {
		final List<BinlogEventParser> r = new ArrayList<BinlogEventParser>();
		for(int i = 0; i < this.parsers.length; i++) {
			if(this.parsers[i] != null) r.add(this.parsers[i]);
		}
		return r;
	}
	
	public void setEventParsers(List<BinlogEventParser> parsers) {
		//
		for(int i = 0; i < this.parsers.length; i++) {
			this.parsers[i] = null;
		}
		
		// 
		if(parsers == null) return;
		for(BinlogEventParser parser : parsers) {
			this.parsers[parser.getEventType()] = parser;
		}
	}
	
	/**
	 * 
	 */
	protected class Worker extends Thread {
		//
		private XInputStream is;
		
		/**
		 * 
		 */
		public Worker(XInputStream is) {
			this.is = is;
		}

		/**
		 * 
		 */
		public void run() {
			try {
				doParse(this.is);
			} catch (Exception e) {
				// TODO
			}
		}
	}
	
	protected final class Context implements ParserContext {
		//
		private BinlogEventV4Header header;
		private BinlogEventListener listener;
		private Map<Long, TableMapEvent> tableMaps;
		
		/**
		 * 
		 */
		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("header", header)
			.append("listener", listener)
			.append("tableMaps", tableMaps).toString();
		}
		
		/**
		 * 
		 */
		public BinlogEventV4Header getHeader() {
			return header;
		}

		public void setHeader(BinlogEventV4Header header) {
			this.header = header;
		}
		
		public BinlogEventListener getListener() {
			return listener;
		}

		public void setListener(BinlogEventListener listener) {
			this.listener = listener;
		}
		
		public TableMapEvent getTableMapEvent(long tableId) {
			return this.tableMaps.get(tableId);
		}
		
		public Map<Long, TableMapEvent> getTableMaps() {
			return tableMaps;
		}

		public void setTableMaps(Map<Long, TableMapEvent> tableMaps) {
			this.tableMaps = tableMaps;
		}
	}
}
