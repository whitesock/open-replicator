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
package com.google.code.or.binlog.impl.variable.status;

import java.io.IOException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class QTimeZoneCode extends AbstractStatusVariable {
	//
	public static final int TYPE = MySQLConstants.Q_TIME_ZONE_CODE;
	
	//
	private final StringColumn timeZone;
	
	/**
	 * 
	 */
	public QTimeZoneCode(StringColumn timeZone) {
		super(TYPE);
		this.timeZone = timeZone;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("timeZone", timeZone).toString();
	}
	
	/**
	 * 
	 */
	public StringColumn getTimeZone() {
		return timeZone;
	}
	
	/**
	 * 
	 */
	public static QTimeZoneCode valueOf(XInputStream tis) throws IOException {
		final int length = tis.readInt(1); // Length
		return new QTimeZoneCode(tis.readFixedLengthString(length));
	}
}
