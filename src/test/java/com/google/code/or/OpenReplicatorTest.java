package com.google.code.or;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;

public class OpenReplicatorTest {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenReplicatorTest.class);

	/**
	 * 
	 */
	public static void main(String args[]) throws Exception {
		//
		final OpenReplicator or = new OpenReplicator();
		or.setUser("nextop");
		or.setPassword("nextop");
		or.setHost("192.168.1.216");
		or.setPort(3306);
		or.setServerId(6789);
		or.setBinlogPosition(599421378);
		or.setBinlogFileName("mysql-bin.000002");
		or.setBinlogEventListener(new BinlogEventListener() {
		    public void onEvents(BinlogEventV4 event) {
		    	LOGGER.info("{}", event);
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
