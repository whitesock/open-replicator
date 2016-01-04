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
package com.google.code.or.binlog.impl.event;

import java.util.Arrays;

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.common.util.ToStringBuilder;

/**
 * 
 * @author brandtg
 */
public class GtidEvent extends AbstractBinlogEventV4 {
	//
	private byte[] sourceId;
	private long transactionId;

	/**
	 * 
	 */
	public GtidEvent() {
	}
	
	public GtidEvent(BinlogEventV4Header header) {
		this.header = header;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("header", header)
		.append("transactionId", transactionId)
		.append("sourceId", Arrays.toString(sourceId)).toString();
	}
	
	/**
	 * 
	 */
	public byte[] getSourceId() {
		return sourceId;
	}

	public void setSourceId(byte[] sourceId) {
		this.sourceId = sourceId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
}
