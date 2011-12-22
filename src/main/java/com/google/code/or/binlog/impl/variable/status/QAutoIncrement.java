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

import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public class QAutoIncrement extends AbstractStatusVariable {
	//
	public static final int TYPE = MySQLConstants.Q_AUTO_INCREMENT;
	
	//
	private final int autoIncrementIncrement;
	private final int autoIncrementOffset;

	/**
	 * 
	 */
	public QAutoIncrement(int autoIncrementIncrement, int autoIncrementOffset) {
		super(TYPE);
		this.autoIncrementIncrement = autoIncrementIncrement;
		this.autoIncrementOffset = autoIncrementOffset;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("autoIncrementIncrement", autoIncrementIncrement)
		.append("autoIncrementOffset", autoIncrementOffset).toString();
	}
	
	/**
	 * 
	 */
	public int getAutoIncrementIncrement() {
		return autoIncrementIncrement;
	}
	
	public int getAutoIncrementOffset() {
		return autoIncrementOffset;
	}
	
	/**
	 * 
	 */
	public static QAutoIncrement valueOf(XInputStream tis) throws IOException {
		final int autoIncrementIncrement = tis.readInt(2);
		final int autoIncrementOffset = tis.readInt(2);
		return new QAutoIncrement(autoIncrementIncrement, autoIncrementOffset);
	}
}
