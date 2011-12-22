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
public class QCharsetDatabaseCode extends AbstractStatusVariable {
	//
	public static final int TYPE = MySQLConstants.Q_CHARSET_DATABASE_CODE;
	
	//
	private final int collationDatabase;

	/**
	 * 
	 */
	public QCharsetDatabaseCode(int collationDatabase) {
		super(TYPE);
		this.collationDatabase = collationDatabase;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("collationDatabase", collationDatabase).toString();
	}
	
	/**
	 * 
	 */
	public int getCollationDatabase() {
		return collationDatabase;
	}
	
	/**
	 * 
	 */
	public static QCharsetDatabaseCode valueOf(XInputStream tis) throws IOException {
		final int collationDatabase = tis.readInt(2);
		return new QCharsetDatabaseCode(collationDatabase);
	}
}
