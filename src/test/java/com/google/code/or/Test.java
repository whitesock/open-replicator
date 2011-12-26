package com.google.code.or;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.logging.Log4jInitializer;
import com.google.code.or.net.Packet;
import com.google.code.or.net.impl.AuthenticatorImpl;
import com.google.code.or.net.impl.TransportImpl;
import com.google.code.or.net.impl.packet.EOFPacket;
import com.google.code.or.net.impl.packet.ErrorPacket;
import com.google.code.or.net.impl.packet.ResultSetFieldPacket;
import com.google.code.or.net.impl.packet.ResultSetHeaderPacket;
import com.google.code.or.net.impl.packet.ResultSetRowPacket;
import com.google.code.or.net.impl.packet.command.ComQuery;

/**
 * 
 * @author Jingqi Xu
 */
public class Test {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

	/**
	 * 
	 */
	public static void main(String args[]) throws Exception {
		//
		Log4jInitializer.initialize();
		
		//
		parseBinlog();
		
		dumpBinlog();
		
		//execQuery();
	}
	
	/**
	 * 
	 */
	public static void parseBinlog() throws Exception {
		//
		final OpenParser op = new OpenParser();
		op.setStartPosition(307);
		op.setBinlogFileName("mysql_bin.000050");
		op.setBinlogFilePath("C:/Documents and Settings/All Users/Application Data/MySQL/MySQL Server 5.5/data");
		op.setBinlogEventListener(new BinlogEventListener() {
		    public void onEvents(BinlogEventV4 event) {
		    	LOGGER.info("{}", event);
		    }
		});
		op.start();
		
		//
		System.out.println("press 'q' to stop");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(String line = br.readLine(); line != null; line = br.readLine()) {
		    if(line.equals("q")) {
		        op.stop(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		        break;
		    }
		}
	}
	
	public static void dumpBinlog() throws Exception {
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
		    	LOGGER.info("{}", event);
		    }
		});
		or.start();

		//
		System.out.println("press 'q' to stop");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(String line = br.readLine(); line != null; line = br.readLine()) {
		    if(line.equals("q")) {
		        or.stop(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		        break;
		    }
		}
	}
	
	public static void execQuery() throws Exception {
		//
		final AuthenticatorImpl authenticator = new AuthenticatorImpl();
		authenticator.setUser("xjq");
		authenticator.setPassword("123456");
		authenticator.setInitialSchema("test");
		
		//
		final TransportImpl transport = new TransportImpl();
		transport.setAuthenticator(authenticator);
		transport.connect("localhost", 3306);
		
		//
		final ComQuery command = new ComQuery();
		command.setSql(StringColumn.valueOf("select * from test.abc where id < 6".getBytes()));
		transport.getOutputStream().writePacket(command);
		transport.getOutputStream().flush();
		
		//
		Packet packet = transport.getInputStream().readPacket();
		if(packet.getPacketBody()[0] == ErrorPacket.PACKET_MARKER) {
			final ErrorPacket error = ErrorPacket.valueOf(packet);
			System.out.println(error);
			return;
		}
		
		//
		final ResultSetHeaderPacket header = ResultSetHeaderPacket.valueOf(packet);
		System.out.println(header);
		
		//
		while(true) {
			packet = transport.getInputStream().readPacket();
			if(packet.getPacketBody()[0] == EOFPacket.PACKET_MARKER) {
				EOFPacket eof = EOFPacket.valueOf(packet);
				System.out.println(eof);
				break;
			} else {
				ResultSetFieldPacket field = ResultSetFieldPacket.valueOf(packet);
				System.out.println(field);
			}
		}
		
		//
		while(true) {
			packet = transport.getInputStream().readPacket();
			if(packet.getPacketBody()[0] == EOFPacket.PACKET_MARKER) {
				EOFPacket eof = EOFPacket.valueOf(packet);
				System.out.println(eof);
				break;
			} else {
				ResultSetRowPacket row = ResultSetRowPacket.valueOf(packet);
				System.out.println(row);
			}
		}
	}
}
