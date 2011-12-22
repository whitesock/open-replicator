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
package com.google.code.or.common.glossary;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.common.util.CodecUtils;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.util.XDeserializer;

/**
 * 
 * @author Jingqi Xu
 */
public final class Metadata implements Serializable {
	//
	private static final long serialVersionUID = 4634414541769527837L;
	
	//
	private final byte[] type;
	private final int[] metadata;
	
	/**
	 * 
	 */
	public Metadata(byte[] type, int[] metadata) {
		this.type = type;
		this.metadata = metadata;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("metadata", metadata).toString();
	}
	
	/**
	 * 
	 */
	public byte getType(int column) {
		return this.type[column];
	}
	
	public int getMetadata(int column) {
		return this.metadata[column];
	}
	
	/**
	 * 
	 */
	public static final Metadata valueOf(byte[] type, byte[] data)
	throws IOException {
		final int[] metadata = new int[type.length];
		final XDeserializer d = new XDeserializer(data);
		for(int i = 0; i < type.length; i++) {
			final int t = CodecUtils.toUnsigned(type[i]);
			switch(t) {
			case MySQLConstants.TYPE_FLOAT:
			case MySQLConstants.TYPE_DOUBLE:
			case MySQLConstants.TYPE_TINY_BLOB:
            case MySQLConstants.TYPE_BLOB:
            case MySQLConstants.TYPE_MEDIUM_BLOB:
            case MySQLConstants.TYPE_LONG_BLOB:
            	metadata[i] = d.readInt(1);
            	break;
            case MySQLConstants.TYPE_BIT:
            case MySQLConstants.TYPE_VARCHAR:
			case MySQLConstants.TYPE_NEWDECIMAL:	
				metadata[i] = d.readInt(2); // Little-endian
            	break;
			case MySQLConstants.TYPE_SET:
            case MySQLConstants.TYPE_ENUM:
            case MySQLConstants.TYPE_STRING:
            	metadata[i] = CodecUtils.toInt(d.readBytes(2), 0, 2); // Big-endian
            	break;
            default:
            	metadata[i] = 0;
			}
		}
		return new Metadata(type, metadata);
	}
}
