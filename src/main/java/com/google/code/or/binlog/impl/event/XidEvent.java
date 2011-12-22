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
 * Generated for a commit of a transaction that modifies one or more tables of an XA-capable storage engine. 
 * Normal transactions are implemented by sending a QUERY_EVENT containing a BEGIN statement and a QUERY_EVENT 
 * containing a COMMIT statement (or a ROLLBACK statement if the transaction is rolled back).
 * Strictly speaking, Xid_log_event is used if thd->transaction.xid_state.xid.get_my_xid() returns non-zero. 
 * 
 * @author Jingqi Xu
 */
public final class XidEvent extends AbstractBinlogEventV4 {
	//
	public static final int EVENT_TYPE = MySQLConstants.XID_EVENT;
	
	//
	private long xid;
	
	/**
	 * 
	 */
	public XidEvent() {
	}
	
	public XidEvent(BinlogEventV4Header header) {
		this.header = header;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("header", header)
		.append("xid", xid).toString();
	}
	
	/**
	 * 
	 */
	public long getXid() {
		return xid;
	}

	public void setXid(long xid) {
		this.xid = xid;
	}
}
