package com.google.code.or;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.impl.event.XidEvent;
import com.google.code.or.logging.Log4jInitializer;

public class OpenReplicatorTest {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenReplicatorTest.class);

	/**
	 * 
	 */
	public static void main(String args[]) throws Exception {
		//
		Log4jInitializer.initialize();
		
		//
		final OpenReplicator or = new OpenReplicator();
		or.setUser("xjq");
		or.setPassword("123456");
		or.setHost("localhost");
		or.setPort(3306);
		or.setServerId(6789);
		or.setBinlogPosition(4);
		or.setBinlogFileName("mysql_bin.000050");
		or.setBinlogEventListener(new BinlogEventListener() {
		    public void onEvents(BinlogEventV4 event) {
		    	if(event instanceof XidEvent) {
		    		LOGGER.info("{}", event);
		    	}
		    }
		});
		or.start();

		//
		LOGGER.info("press 'q' to stop");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(String line = br.readLine(); line != null; line = br.readLine()) {
		    if(line.equals("q")) {
		        or.stop(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		        break;
		    }
		}
	}
}
