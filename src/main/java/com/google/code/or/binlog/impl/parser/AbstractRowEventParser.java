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
package com.google.code.or.binlog.impl.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.google.code.or.binlog.BinlogRowEventFilter;
import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.google.code.or.binlog.impl.filter.DefaultBinlogRowEventFilter;
import com.google.code.or.common.glossary.Column;
import com.google.code.or.common.glossary.Metadata;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.common.glossary.column.BitColumn;
import com.google.code.or.common.glossary.column.BlobColumn;
import com.google.code.or.common.glossary.column.DateColumn;
import com.google.code.or.common.glossary.column.DatetimeColumn;
import com.google.code.or.common.glossary.column.DecimalColumn;
import com.google.code.or.common.glossary.column.DoubleColumn;
import com.google.code.or.common.glossary.column.EnumColumn;
import com.google.code.or.common.glossary.column.FloatColumn;
import com.google.code.or.common.glossary.column.Int24Column;
import com.google.code.or.common.glossary.column.LongColumn;
import com.google.code.or.common.glossary.column.LongLongColumn;
import com.google.code.or.common.glossary.column.NullColumn;
import com.google.code.or.common.glossary.column.SetColumn;
import com.google.code.or.common.glossary.column.ShortColumn;
import com.google.code.or.common.glossary.column.TimeColumn;
import com.google.code.or.common.glossary.column.TimestampColumn;
import com.google.code.or.common.glossary.column.TinyColumn;
import com.google.code.or.common.glossary.column.YearColumn;
import com.google.code.or.common.util.CodecUtils;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.common.util.MySQLUtils;
import com.google.code.or.io.XInputStream;

/**
 * 
 * @author Jingqi Xu
 */
public abstract class AbstractRowEventParser extends AbstractBinlogEventParser {
	//
	protected BinlogRowEventFilter rowEventFilter;
	
	/**
	 * 
	 */
	public AbstractRowEventParser(int eventType) {
		super(eventType);
		this.rowEventFilter = new DefaultBinlogRowEventFilter();
	}
	
	/**
	 * 
	 */
	public BinlogRowEventFilter getRowEventFilter() {
		return rowEventFilter;
	}

	public void setRowEventFilter(BinlogRowEventFilter filter) {
		this.rowEventFilter = filter;
	}
	
	/**
	 * 
	 */
	protected Row parseRow(XInputStream tis, TableMapEvent tme, BitColumn usedColumns) 
	throws IOException {
		//
		final byte[] types = tme.getColumnTypes();
		final Metadata metadata = tme.getColumnMetadata();
		final BitColumn nullColumns = tis.readBit(types.length, true);
		final List<Column> columns = new ArrayList<Column>(types.length);
		for(int i = 0; i < types.length; ++i) {
			//
			int length = 0;
			final int meta = metadata.getMetadata(i);
			int type = CodecUtils.toUnsigned(types[i]);
			if(type == MySQLConstants.TYPE_STRING && meta > 256) {
				final int meta0 = meta >> 8;
				final int meta1 = meta & 0xFF;
				if ((meta0 & 0x30) != 0x30) { // a long CHAR() field: see #37426
					type = meta0 | 0x30;
					length = meta1 | (((meta0 & 0x30) ^ 0x30) << 4); 
				} else {
					switch (meta0) {
					case MySQLConstants.TYPE_SET:
					case MySQLConstants.TYPE_ENUM:
					case MySQLConstants.TYPE_STRING:
						type = meta0;
						length = meta1;
						break;
					default:
						throw new NestableRuntimeException("assertion failed, unknown column type: " + type);
					}
				}
			}
			
			//
			if(!usedColumns.get(i)) {
				continue;
			} else if(nullColumns.get(i)) {
				columns.add(NullColumn.valueOf(type));
				continue;
			}
			
			//
			switch(type) {
			case MySQLConstants.TYPE_TINY: columns.add(TinyColumn.valueOf(tis.readInt(1))); break;
			case MySQLConstants.TYPE_SHORT: columns.add(ShortColumn.valueOf(tis.readInt(2))); break;
			case MySQLConstants.TYPE_INT24: columns.add(Int24Column.valueOf(tis.readInt(3))); break;
			case MySQLConstants.TYPE_LONG: columns.add(LongColumn.valueOf(tis.readInt(4))); break;
			case MySQLConstants.TYPE_LONGLONG: columns.add(LongLongColumn.valueOf(tis.readLong(8))); break;
			case MySQLConstants.TYPE_FLOAT: columns.add(FloatColumn.valueOf(Float.intBitsToFloat(tis.readInt(4)))); break;
			case MySQLConstants.TYPE_DOUBLE: columns.add(DoubleColumn.valueOf(Double.longBitsToDouble(tis.readLong(8)))); break;
			case MySQLConstants.TYPE_YEAR: columns.add(YearColumn.valueOf(MySQLUtils.toYear(tis.readInt(1)))); break;
			case MySQLConstants.TYPE_DATE: columns.add(DateColumn.valueOf(MySQLUtils.toDate(tis.readInt(3)))); break;
			case MySQLConstants.TYPE_TIME: columns.add(TimeColumn.valueOf(MySQLUtils.toTime(tis.readInt(3)))); break;
			case MySQLConstants.TYPE_TIMESTAMP: columns.add(TimestampColumn.valueOf(MySQLUtils.toTimestamp(tis.readLong(4)))); break;
			case MySQLConstants.TYPE_DATETIME: columns.add(DatetimeColumn.valueOf(MySQLUtils.toDatetime(tis.readLong(8)))); break;
			case MySQLConstants.TYPE_ENUM: columns.add(EnumColumn.valueOf(tis.readInt(length))); break;
			case MySQLConstants.TYPE_SET: columns.add(SetColumn.valueOf(tis.readLong(length))); break;
			case MySQLConstants.TYPE_STRING:
				final int stringLength = length < 256 ? tis.readInt(1) : tis.readInt(2);
				columns.add(tis.readFixedLengthString(stringLength));
				break;
			case MySQLConstants.TYPE_BIT: 
				final int bitLength = (meta >> 8) * 8 + (meta & 0xFF);
				columns.add(tis.readBit(bitLength, false));
				break;
			case MySQLConstants.TYPE_NEWDECIMAL:
				final int precision = meta & 0xFF;
		        final int scale = meta >> 8;
		        final int decimalLength = MySQLUtils.getDecimalBinarySize(precision, scale);
		        columns.add(DecimalColumn.valueOf(MySQLUtils.toDecimal(precision, scale, tis.readBytes(decimalLength)), precision, scale));
				break;
			case MySQLConstants.TYPE_BLOB:
				final int blobLength = tis.readInt(meta);
				columns.add(BlobColumn.valueOf(tis.readBytes(blobLength)));
				break;
			case MySQLConstants.TYPE_VARCHAR:
			case MySQLConstants.TYPE_VAR_STRING:
				final int varcharLength = meta < 256 ? tis.readInt(1) : tis.readInt(2);
				columns.add(tis.readFixedLengthString(varcharLength));
				break;
			default:
				throw new NestableRuntimeException("assertion failed, unknown column type: " + type);
			}
		}
		return new Row(columns);
	}
}
