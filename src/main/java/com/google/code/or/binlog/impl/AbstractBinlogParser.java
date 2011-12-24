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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventFilter;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogParser;
import com.google.code.or.common.util.XThreadFactory;
import com.google.code.or.io.XInputStream;

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
	protected final AtomicBoolean running = new AtomicBoolean(false);
	protected final AtomicBoolean verbose = new AtomicBoolean(false);
	
	//
	protected abstract void doParse(XInputStream is) throws Exception;
	

	/**
	 * 
	 */
	public AbstractBinlogParser() {
		this.threadFactory = new XThreadFactory("binlog-parser", false);
	}
	
	/**
	 * 
	 */
	public final boolean isRunning() {
		return this.running.get();
	}
	
	public void start(XInputStream is) throws Exception {
		//
		if(!this.running.compareAndSet(false, true)) {
			return;
		}
		
		//
		this.worker = this.threadFactory.newThread(new ParserTask(is));
		this.worker.start();
	}

	public void stop() throws Exception {
		//
		if(!this.running.compareAndSet(true, false)) {
			return;
		}
		
		//
		this.worker.join();
		this.worker = null;
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
	
	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
	}
	
	public BinlogEventFilter getEventFilter() {
		return eventFilter;
	}

	public void setEventFilter(BinlogEventFilter eventFilter) {
		this.eventFilter = eventFilter;
	}
	
	public BinlogEventListener getEventListener() {
		return eventListener;
	}
	
	public void setEventListener(BinlogEventListener eventListener) {
		this.eventListener = eventListener;
	}
	
	/**
	 * 
	 */
	protected final class ParserTask implements Runnable {
		//
		private final XInputStream is;
		
		/**
		 * 
		 */
		public ParserTask(XInputStream is) {
			this.is = is;
		}

		/**
		 * 
		 */
		public void run() {
			try {
				doParse(this.is);
			} catch (Exception e) {
				LOGGER.error("failed to parse binlog", e);
			}
		}
	}
}
