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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventFilter;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventParser;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.BinlogParser;
import com.google.code.or.binlog.BinlogParserContext;
import com.google.code.or.binlog.impl.event.RotateEvent;
import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.google.code.or.binlog.impl.parser.NopEventParser;
import com.google.code.or.common.util.XThreadFactory;

/**
 * 
 * @author Jingqi Xu
 */
public abstract class AbstractBinlogParser implements BinlogParser {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBinlogParser.class);
	
	//
	protected Thread worker;
	protected ThreadFactory threadFactory;
	protected BinlogEventFilter eventFilter;
	protected BinlogEventListener eventListener;
	protected final AtomicBoolean verbose = new AtomicBoolean(false);
	protected final AtomicBoolean running = new AtomicBoolean(false);
	protected final BinlogEventParser defaultParser = new NopEventParser();
	protected final BinlogEventParser[] parsers = new BinlogEventParser[128];
	
	//
	protected abstract void doParse() throws Exception;
	protected abstract void doStart() throws Exception;
	protected abstract void doStop(long timeout, TimeUnit unit) throws Exception;
	
	/**
	 * 
	 */
	public AbstractBinlogParser() {
		this.threadFactory = new XThreadFactory("binlog-parser", false);
	}
	
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
		doStart();
		
		//
		this.worker = this.threadFactory.newThread(new Task());
		this.worker.start();	
	}

	public void stop(long timeout, TimeUnit unit) throws Exception {
		//
		if(!this.running.compareAndSet(true, false)) {
			return;
		}
		
		//
		final long now = System.nanoTime();
		doStop(timeout, unit);
		timeout -= unit.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS);
		
		//
		if(timeout > 0) {
			unit.timedJoin(this.worker, timeout);
			this.worker = null;
		}
	}
	
	/**
	 * 
	 */
	public boolean isVerbose() {
		return this.verbose.get();
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose.set(verbose);
	}
	
	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	public void setThreadFactory(ThreadFactory tf) {
		this.threadFactory = tf;
	}
	
	public BinlogEventFilter getEventFilter() {
		return eventFilter;
	}

	public void setEventFilter(BinlogEventFilter filter) {
		this.eventFilter = filter;
	}
	
	public BinlogEventListener getEventListener() {
		return eventListener;
	}
	
	public void setEventListener(BinlogEventListener listener) {
		this.eventListener = listener;
	}
	
	/**
	 * 
	 */
	public void clearEventParsers() {
		for(int i = 0; i < this.parsers.length; i++) {
			this.parsers[i] = null;
		}
	}
	
	public BinlogEventParser getEventParser(int type) {
		return this.parsers[type];
	}
	
	public BinlogEventParser unregistgerEventParser(int type) {
		return this.parsers[type] = null;
	}
	
	public void registgerEventParser(BinlogEventParser parser) {
		this.parsers[parser.getEventType()] = parser;
	}
	
	public void setEventParsers(List<BinlogEventParser> parsers) {
		clearEventParsers();
		if(parsers != null) {
			for(BinlogEventParser parser : parsers) {
				registgerEventParser(parser);
			}
		}
	}
	
	/**
	 * 
	 */
	protected class Task implements Runnable {
		
		public void run() {
			try {
				doParse();
			} catch (Exception e) {
				LOGGER.error("failed to parse binlog", e);
			} finally {
				try {
					stop(0, TimeUnit.MILLISECONDS);
				} catch(Exception e) {
					LOGGER.error("failed to stop parser", e);
				}
			}
		}
	}
	
	protected class Context implements BinlogParserContext, BinlogEventListener {
		//
		private String binlogFileName;
		private final Map<Long, TableMapEvent> tableMaps = new HashMap<Long, TableMapEvent>();

		/**
		 * 
		 */
		public Context() {
		}
		
		public Context(String binlogFileName) {
			this.binlogFileName = binlogFileName;
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
		
		public BinlogEventListener getEventListener() {
			return this;
		}

		public TableMapEvent getTableMapEvent(long tableId) {
			return this.tableMaps.get(tableId);
		}
		
		/**
		 * 
		 */
		public void onEvents(BinlogEventV4 event) {
			//
			if(event == null) {
				return;
			}
			
			//
			if(event instanceof TableMapEvent) {
				final TableMapEvent tme = (TableMapEvent)event;
				this.tableMaps.put(tme.getTableId(), tme);
			} else if(event instanceof RotateEvent) {
				final RotateEvent re = (RotateEvent)event;
				this.binlogFileName = re.getBinlogFileName().toString();
			}
			
			//
			AbstractBinlogParser.this.eventListener.onEvents(event);
		}
	}
}
