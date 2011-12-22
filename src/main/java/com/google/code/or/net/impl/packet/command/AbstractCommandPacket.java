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
package com.google.code.or.net.impl.packet.command;

import com.google.code.or.net.impl.packet.AbstractPacket;

/**
 * 
 * @author Jingqi Xu
 */
public abstract class AbstractCommandPacket extends AbstractPacket {
	//
	private static final long serialVersionUID = -8046179372409111502L;
	
	//
	protected final int command;
	
	/**
	 * 
	 */
	public AbstractCommandPacket(int command) {
		this.command = command;
	}

	/**
	 * 
	 */
	public int getCommand() {
		return command;
	}
}
