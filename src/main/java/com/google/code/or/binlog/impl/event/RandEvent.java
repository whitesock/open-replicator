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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.common.util.MySQLConstants;

/**
 * Written every time a statement uses the RAND() function; precedes other events for the statement. 
 * Indicates the seed values to use for generating a random number with RAND() in the next statement. 
 * This is written only before a QUERY_EVENT and is not used with row-based logging. 
 * 
 * @author Jingqi Xu
 */
public final class RandEvent extends AbstractBinlogEventV4 {
	//
	public static final int EVENT_TYPE = MySQLConstants.RAND_EVENT;
	
	//
	private long randSeed1;
	private long randSeed2;
	
	/**
	 * 
	 */
	public RandEvent() {
	}
	
	public RandEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("randSeed1", randSeed1)
		.append("randSeed2", randSeed2).toString();
	}
	
	/**
	 * 
	 */
	public long getRandSeed1() {
		return randSeed1;
	}

	public void setRandSeed1(long randSeed1) {
		this.randSeed1 = randSeed1;
	}
	
	public long getRandSeed2() {
		return randSeed2;
	}

	public void setRandSeed2(long randSeed2) {
		this.randSeed2 = randSeed2;
	}
}
