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
package com.google.code.or.logging;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * 
 * @author Jingqi Xu
 */
public class Log4jInitializer {
	//
	private static final Object PRESENT = new Object();
	private static final ConcurrentHashMap<String, Object> INITIALIZED_URLS = new ConcurrentHashMap<String, Object>();
	
	/**
	 * 
	 */
	public static final void initialize() {
		initialize(Log4jInitializer.class.getResource("log4j.xml"));
	}
	
	public static final void initialize(URL url) {
		//
		Object present = INITIALIZED_URLS.putIfAbsent(url.toString(), PRESENT);
		if(present != null) {
			return;
		}
		DOMConfigurator.configure(url);
	}
	
	public static final void initialize(String path) {
		//
		if(path.startsWith("file:")) {
			initialize(toUrl(path));
			return;
		}
		
		//
		Object present = INITIALIZED_URLS.putIfAbsent(path, PRESENT);
		if(present != null) {
			return;
		}
		DOMConfigurator.configure(path);
	}

	/**
	 * 
	 */
	private static URL toUrl(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid parameter url: " + url);
		}
	}
}
