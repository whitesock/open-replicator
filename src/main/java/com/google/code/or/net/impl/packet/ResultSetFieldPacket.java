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
package com.google.code.or.net.impl.packet;

import java.io.IOException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.io.util.XDeserializer;
import com.google.code.or.io.util.XSerializer;
import com.google.code.or.net.Packet;

/**
 * 
 * @author Jingqi Xu
 */
public class ResultSetFieldPacket extends AbstractPacket {
	//
	private static final long serialVersionUID = -6484191963940716299L;
	
	//
	private StringColumn catalog;
	private StringColumn db;
	private StringColumn table;
	private StringColumn orginalTable;
	private StringColumn column;
	private StringColumn originalColumn;
	private int fixed12;
	private int charset;
	private long fieldLength;
	private int fieldType;
	private int fieldOptions;
	private int decimalPrecision;
	private int reserved;
	private StringColumn defaultValue; // Optional
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		.append("catalog", catalog)
		.append("db", db)
		.append("table", table)
		.append("orginalTable", orginalTable)
		.append("column", column)
		.append("originalColumn", originalColumn)
		.append("fixed12", fixed12)
		.append("charset", charset)
		.append("fieldLength", fieldLength)
		.append("fieldType", fieldType)
		.append("fieldOptions", fieldOptions)
		.append("decimalPrecision", decimalPrecision)
		.append("reserved", reserved)
		.append("defaultValue", defaultValue).toString();
	}
	
	/**
	 * 
	 */
	public byte[] getPacketBody() {
		final XSerializer s = new XSerializer(256);
		s.writeLengthCodedString(this.catalog);
		s.writeLengthCodedString(this.db);
		s.writeLengthCodedString(this.table);
		s.writeLengthCodedString(this.orginalTable);
		s.writeLengthCodedString(this.column);
		s.writeLengthCodedString(this.originalColumn);
		s.writeInt(this.fixed12, 1);
		s.writeInt(this.charset, 2);
		s.writeLong(this.fieldLength, 4);
		s.writeInt(this.fieldType, 1);
		s.writeInt(this.fieldOptions, 2);
		s.writeInt(this.decimalPrecision, 1);
		s.writeInt(this.reserved, 2);
		if(this.defaultValue != null) s.writeLengthCodedString(this.defaultValue);
		return s.toByteArray();
	}
	
	/**
	 * 
	 */
	public StringColumn getCatalog() {
		return catalog;
	}

	public void setCatalog(StringColumn catalog) {
		this.catalog = catalog;
	}

	public StringColumn getDb() {
		return db;
	}

	public void setDb(StringColumn db) {
		this.db = db;
	}

	public StringColumn getTable() {
		return table;
	}

	public void setTable(StringColumn table) {
		this.table = table;
	}

	public StringColumn getOrginalTable() {
		return orginalTable;
	}

	public void setOrginalTable(StringColumn orginalTable) {
		this.orginalTable = orginalTable;
	}

	public StringColumn getColumn() {
		return column;
	}

	public void setColumn(StringColumn column) {
		this.column = column;
	}

	public StringColumn getOriginalColumn() {
		return originalColumn;
	}

	public void setOriginalColumn(StringColumn originalColumn) {
		this.originalColumn = originalColumn;
	}

	public int getFixed12() {
		return fixed12;
	}

	public void setFixed12(int fixed12) {
		this.fixed12 = fixed12;
	}

	public int getCharset() {
		return charset;
	}

	public void setCharset(int charset) {
		this.charset = charset;
	}

	public long getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(long fieldLength) {
		this.fieldLength = fieldLength;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public int getFieldOptions() {
		return fieldOptions;
	}

	public void setFieldOptions(int fieldOptions) {
		this.fieldOptions = fieldOptions;
	}

	public int getDecimalPrecision() {
		return decimalPrecision;
	}

	public void setDecimalPrecision(int decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public int getReserved() {
		return reserved;
	}

	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public StringColumn getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(StringColumn defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * 
	 */
	public static ResultSetFieldPacket valueOf(Packet packet) throws IOException {
		final XDeserializer d = new XDeserializer(packet.getPacketBody());
		final ResultSetFieldPacket r = new ResultSetFieldPacket();
		r.fieldLength = packet.getLength();
		r.sequence = packet.getSequence();
		r.catalog = d.readLengthCodedString();
		r.db = d.readLengthCodedString();
		r.table = d.readLengthCodedString();
		r.orginalTable = d.readLengthCodedString();
		r.column = d.readLengthCodedString();
		r.originalColumn = d.readLengthCodedString();
		r.fixed12 = d.readInt(1);
		r.charset = d.readInt(2);
		r.fieldLength = d.readLong(4);
		r.fieldType = d.readInt(1);
		r.fieldOptions = d.readInt(2);
		r.decimalPrecision = d.readInt(1);
		r.reserved = d.readInt(2);
		if(d.available() > 0) r.defaultValue = d.readLengthCodedString();
		return r;
	}
}
