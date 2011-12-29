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

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Jingqi Xu
 */
public final class CodecUtils {
	
	/**
	 * 
	 */
	public static int toBigEndian(final int v) {
		int r = v;
		for(int i = 0; i < 4; i++) {
			r = ((r & 0x000000FF) << 24) | (r >>> 8);
		}
		return r;
	}
	
	public static long toBigEndian(final long v) {
		long r = v;
		for(int i = 0; i < 8; i++) {
			r = ((r & 0x00000000000000FFL) << 56) | (r >>> 8);
		}
		return r;
	}
	
	public static byte[] toBigEndian(byte[] value) {
		for(int i = 0, length = value.length >> 2; i <= length; i++) {
			final int j = value.length - 1 - i;
			final byte t = value[i];
			value[i] = value[j];
			value[j] = t;
		}
		return value;
	}
	
	/**
	 * 
	 */
	public static int toUnsigned(byte b) {
		return b & 0xFF;
	}
	
	public static int toUnsigned(short s) {
		return s & 0xFFFF;
	}
	
	public static long toUnsigned(int i) {
		return i & 0xFFFFFFFFL;
	}
	
	/**
	 * 
	 */
	public static byte[] toByteArray(byte num) {
		return new byte[]{num};
	}
	
	public static byte[] toByteArray(short num) {
		final byte[] r = new byte[2];
		for(int i = 0; i < 2; i++) {
			r[i] = (byte)(num >>> (8 - i * 8));
		}
		return r;
	}
	
	public static byte[] toByteArray(int num) {
		final byte[] r = new byte[4];
		for(int i = 0; i < 4; i++) {
			r[i] = (byte)(num >>> (24 - i * 8));
		}
		return r;
	}
	
	public static byte[] toByteArray(long num) {
		final byte[] r = new byte[8];
		for(int i = 0; i < 8; i++) {
			r[i] = (byte)(num >>> (56 - i * 8));
		}
		return r;
	}
	
	/**
	 * 
	 */
	public static int toInt(byte[] data, int offset, int length) {
		int r = 0;
        for (int i = offset; i < (offset + length); i++) {
        	final byte b = data[i];
        	r = (r << 8) | (b >= 0 ? (int)b : (b + 256));
        }
        return r;
	}
	
	public static long toLong(byte[] data, int offset, int length) {
		long r = 0;
        for (int i = offset; i < (offset + length); i++) {
        	final byte b = data[i];
        	r = (r << 8) | (b >= 0 ? (int)b : (b + 256));
        }
        return r;
	}
	
	/**
	 * 
	 */
	public static byte[] md5(byte data[]) {
		return getMd5Digest().digest(data);
	}
	
	public static byte[] md5(InputStream is) throws IOException {
		return digest(getMd5Digest(), is);
	}
	
	public static byte[] sha(byte data[]) {
		return getShaDigest().digest(data);
	}
	
	public static byte[] sha(InputStream is) throws IOException {
		return digest(getShaDigest(), is);
	}
	
	/**
	 * 
	 */
	public static byte[] or(byte[] data1, byte[] data2) {
		//
		if(data1.length != data2.length) {
			throw new IllegalArgumentException("array lenth does NOT match, " + data1.length + " vs " + data2.length);
		}
		
		//
		final byte r[] = new byte[data1.length];
		for(int i = 0; i < r.length; i++) {
			r[i] = (byte)(data1[i] | data2[i]);
		}
		return r;
	}
	
	public static byte[] and(byte[] data1, byte[] data2) {
		//
		if(data1.length != data2.length) {
			throw new IllegalArgumentException("array lenth does NOT match, " + data1.length + " vs " + data2.length);
		}
		
		//
		final byte r[] = new byte[data1.length];
		for(int i = 0; i < r.length; i++) {
			r[i] = (byte)(data1[i] & data2[i]);
		}
		return r;
	}
	
	public static byte[] xor(byte[] data1, byte[] data2) {
		//
		if(data1.length != data2.length) {
			throw new IllegalArgumentException("array lenth does NOT match, " + data1.length + " vs " + data2.length);
		}
		
		//
		final byte r[] = new byte[data1.length];
		for(int i = 0; i < r.length; i++) {
			r[i] = (byte)(data1[i] ^ data2[i]);
		}
		return r;
	}
	
	public static boolean equals(byte[] data1, byte[] data2) {
		//
		if(data1 == data2) {
			return true;
		} else if(data1 == null || data2 == null) {
			return false;
		} else if(data1.length != data2.length) {
			return false;
		}
		
		//
		for(int i = 0; i < data1.length; i++) {
			if(data1[i] != data2[i]) {
				return false;
			}
		}
		return true;
	}
	
	public static byte[] concat(byte[] data1, byte[] data2) {
		final byte r[] = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, r, 0, data1.length);
		System.arraycopy(data2, 0, r, data1.length, data2.length);
		return r;
	}
	
	/**
	 * 
	 */
	private static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }

    private static MessageDigest getShaDigest() {
        return getDigest("SHA");
    }
    
	private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	private static byte[] digest(MessageDigest md, InputStream is) throws IOException {
		final byte buffer[] = new byte[4096];
		for(int count = is.read(buffer); count > 0; count = is.read(buffer)) {
			md.update(buffer, 0, count);
		}
		return md.digest();
	}
}
