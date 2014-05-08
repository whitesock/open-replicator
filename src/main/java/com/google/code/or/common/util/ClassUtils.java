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

/**
 * 
 * @author Jingqi Xu
 */
public final class ClassUtils {

	/**
	 * 
	 */
	public static String getShortClassName(String className) {
		//
        if(className == null) return "";
        if(className.length() == 0) return "";
        StringBuilder arrayPrefix = new StringBuilder();

        // Handle array encoding
        if (className.startsWith("[")) {
            while (className.charAt(0) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            // Strip Object type encoding
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
                className = className.substring(1, className.length() - 1);
            }
        }

        //
        int lastDotIdx = className.lastIndexOf('.');
        int innerIdx = className.indexOf('$', lastDotIdx == -1 ? 0 : lastDotIdx + 1);
        String out = className.substring(lastDotIdx + 1);
        if (innerIdx != -1) {
            out = out.replace('$', '.');
        }
        return out + arrayPrefix;
    }
}
