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
package com.google.code.or.common.util;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 
 * @author Jingqi Xu
 * @see tungsten replicator
 */
public final class MySQLUtils {
	//
	private static final int DIGITS_PER_4BYTES = 9;
	private static final BigDecimal POSITIVE_ONE = BigDecimal.ONE;
	private static final BigDecimal NEGATIVE_ONE = new BigDecimal("-1");
	private static final int DECIMAL_BINARY_SIZE[] = {0, 1, 1, 2, 2, 3, 3, 4, 4, 4};
	
	/**
	 * 
	 */
	public static byte[] password41OrLater(byte password[], byte scramble[]) {
		final byte[] stage1 = CodecUtils.sha(password);
		final byte[] stage2 = CodecUtils.sha(CodecUtils.concat(scramble, CodecUtils.sha(stage1)));
		return CodecUtils.xor(stage1, stage2);
	}

	/**
	 * 
	 */
	public static int toYear(int value) {
		return 1900 + value;
	}

	public static java.sql.Date toDate(int value) {
		final int d = value % 32; value >>>= 5; 
		final int m = value % 16;
		final int y = value >> 4;
		final Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(y, m - 1, d);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	public static java.sql.Time toTime(int value) {
		final int s = (int)(value % 100); value /= 100;
		final int m = (int)(value % 100);
		final int h = (int)(value / 100);
		final Calendar c = Calendar.getInstance();
        c.set(70, 0, 1, h, m, s);
        return new java.sql.Time(c.getTimeInMillis());
	}
	
	public static java.util.Date toDatetime(long value) {
		final int sec  = (int)(value % 100); value /= 100;
		final int min  = (int)(value % 100); value /= 100;
		final int hour = (int)(value % 100); value /= 100;
		final int day  = (int)(value % 100); value /= 100;
		final int mon  = (int)(value % 100);
		final int year = (int)(value / 100);
		final Calendar c = Calendar.getInstance();
        c.set(year, mon - 1, day, hour, min, sec);
        return c.getTime();
	}
	
	public static java.sql.Timestamp toTimestamp(long value) {
		return new java.sql.Timestamp(value * 1000L);
	}
	
	public static BigDecimal toDecimal(int precision, int scale, byte[] value) {
		//
        final boolean positive = (value[0] & 0x80) == 0x80;
        value[0] ^= 0x80;
        if (!positive) {
        	 for (int i = 0; i < value.length; i++) {
        		 value[i] ^= 0xFF;
             }
        }

        //
        final int x = precision - scale;
        final int ipDigits = x / DIGITS_PER_4BYTES;
        final int ipDigitsX = x - ipDigits * DIGITS_PER_4BYTES;
        final int ipSize = (ipDigits << 2) + DECIMAL_BINARY_SIZE[ipDigitsX];
        int offset = DECIMAL_BINARY_SIZE[ipDigitsX];
        BigDecimal ip = offset > 0 ? BigDecimal.valueOf(CodecUtils.toInt(value, 0, offset)) : BigDecimal.ZERO;
        for(; offset < ipSize; offset += 4) {
        	final int i = CodecUtils.toInt(value, offset, 4);
        	ip = ip.movePointRight(DIGITS_PER_4BYTES).add(BigDecimal.valueOf(i));
        }

        // 
        int shift = 0;
        BigDecimal fp = BigDecimal.ZERO;
        for (; shift + DIGITS_PER_4BYTES <= scale; shift += DIGITS_PER_4BYTES, offset += 4) {
            final int i = CodecUtils.toInt(value, offset, 4);
            fp = fp.add(BigDecimal.valueOf(i).movePointLeft(shift + DIGITS_PER_4BYTES));
        }
        if(shift < scale) {
        	final int i = CodecUtils.toInt(value, offset, DECIMAL_BINARY_SIZE[scale - shift]);
            fp = fp.add(BigDecimal.valueOf(i).movePointLeft(scale));
        }
        
        //
        return positive ? POSITIVE_ONE.multiply(ip.add(fp)) : NEGATIVE_ONE.multiply(ip.add(fp));
	}
	
	/**
	 * 
	 */
	public static int getDecimalBinarySize(int precision, int scale) {
		final int x = precision - scale;
        final int ipDigits = x / DIGITS_PER_4BYTES;
        final int fpDigits = scale / DIGITS_PER_4BYTES;
        final int ipDigitsX = x - ipDigits * DIGITS_PER_4BYTES;
        final int fpDigitsX = scale - fpDigits * DIGITS_PER_4BYTES;
        return (ipDigits << 2) + DECIMAL_BINARY_SIZE[ipDigitsX] + (fpDigits << 2) + DECIMAL_BINARY_SIZE[fpDigitsX];
    }
}
