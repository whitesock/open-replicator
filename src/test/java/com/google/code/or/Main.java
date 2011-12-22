package com.google.code.or;

import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.impl.FileBasedBinlogParser;
import com.google.code.or.binlog.impl.ReplicationBasedBinlogParser;
import com.google.code.or.binlog.impl.event.XidEvent;
import com.google.code.or.binlog.impl.parser.DeleteRowsEventParser;
import com.google.code.or.binlog.impl.parser.FormatDescriptionEventParser;
import com.google.code.or.binlog.impl.parser.IncidentEventParser;
import com.google.code.or.binlog.impl.parser.IntvarEventParser;
import com.google.code.or.binlog.impl.parser.QueryEventParser;
import com.google.code.or.binlog.impl.parser.RandEventParser;
import com.google.code.or.binlog.impl.parser.RotateEventParser;
import com.google.code.or.binlog.impl.parser.StopEventParser;
import com.google.code.or.binlog.impl.parser.TableMapEventParser;
import com.google.code.or.binlog.impl.parser.UpdateRowsEventParser;
import com.google.code.or.binlog.impl.parser.UserVarEventParser;
import com.google.code.or.binlog.impl.parser.WriteRowsEventParser;
import com.google.code.or.binlog.impl.parser.XidEventParser;
import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.common.logging.Log4jInitializer;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.XInputStream;
import com.google.code.or.io.impl.XInputStreamImpl;
import com.google.code.or.net.Packet;
import com.google.code.or.net.impl.AuthenticatorImpl;
import com.google.code.or.net.impl.TransportImpl;
import com.google.code.or.net.impl.packet.EOFPacket;
import com.google.code.or.net.impl.packet.ErrorPacket;
import com.google.code.or.net.impl.packet.ResultSetFieldPacket;
import com.google.code.or.net.impl.packet.ResultSetHeaderPacket;
import com.google.code.or.net.impl.packet.ResultSetRowPacket;
import com.google.code.or.net.impl.packet.command.ComBinlogDumpPacket;
import com.google.code.or.net.impl.packet.command.ComQuery;

/**
 * 
 * @author Jingqi Xu
 */
public class Main {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * 
	 */
	public static void main(String args[]) throws Exception {
		//
		Log4jInitializer.initialize();
		
		//execQuery();
		
		//
		//dumpBinlog();
		
		//
		parseBinlog();
	}
	
	/**
	 * 
	 */
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

	public static void dumpBinlog() throws Exception {
		//
		final AuthenticatorImpl authenticator = new AuthenticatorImpl();
		authenticator.setUser("xjq");
		authenticator.setPassword("123456");
		
		//
		final TransportImpl transport = new TransportImpl();
		transport.setAuthenticator(authenticator);
		transport.connect("localhost", 3306);
		
		//
		final ComBinlogDumpPacket command = new ComBinlogDumpPacket();
		command.setBinlogFlag(0);
		command.setServerId(999);
		command.setBinlogPosition(4);
		command.setBinlogFileName(StringColumn.valueOf("mysql_bin.000026".getBytes()));
		transport.getOutputStream().writePacket(command);
		transport.getOutputStream().flush();
		
		//
		Packet packet = transport.getInputStream().readPacket();
		if(packet.getPacketBody()[0] == ErrorPacket.PACKET_MARKER) {
			final ErrorPacket error = ErrorPacket.valueOf(packet);
			System.out.println(error);
		} else {
			final ReplicationBasedBinlogParser parser = new ReplicationBasedBinlogParser();
			parser.registgerEventParser(new StopEventParser());
			parser.registgerEventParser(new RotateEventParser());
			parser.registgerEventParser(new IntvarEventParser());
			parser.registgerEventParser(new XidEventParser());
			parser.registgerEventParser(new RandEventParser());
			parser.registgerEventParser(new QueryEventParser());
			parser.registgerEventParser(new UserVarEventParser());
			parser.registgerEventParser(new IncidentEventParser());
			parser.registgerEventParser(new TableMapEventParser());
			parser.registgerEventParser(new WriteRowsEventParser());
			parser.registgerEventParser(new DeleteRowsEventParser());
			parser.registgerEventParser(new UpdateRowsEventParser());
			parser.registgerEventParser(new FormatDescriptionEventParser());
			parser.setBinlogEventListener(new BinlogEventListener() {
				public void onEvents(BinlogEventV4 event) {
					if(event instanceof XidEvent) {
						LOGGER.info("{}",  event);
					} 
				}
			});
			parser.start();
			parser.parse(transport.getInputStream());
		}
	}

	public static void parseBinlog() throws Exception {
		//
		final String fileName = "C:/Documents and Settings/All Users/Application Data/MySQL/MySQL Server 5.5/data/mysql_bin.000031";
		final int position = 4;
		
		//
		final XInputStream is = new XInputStreamImpl(new FileInputStream(fileName));
		final byte[] magic = is.readBytes(4);
		for(int i = 0; i < magic.length; i++) {
			if(magic[i] != MySQLConstants.BINLOG_MAGIC[i]) {
				LOGGER.error("invalid binlog file");
				return;
			}
		}
		is.skip(position - 4);
		LOGGER.info("start to parse binlog file: {}", fileName);
		
		//
		final FileBasedBinlogParser parser = new FileBasedBinlogParser();
		parser.registgerEventParser(new StopEventParser());
		parser.registgerEventParser(new RotateEventParser());
		parser.registgerEventParser(new IntvarEventParser());
		parser.registgerEventParser(new XidEventParser());
		parser.registgerEventParser(new RandEventParser());
		parser.registgerEventParser(new QueryEventParser());
		parser.registgerEventParser(new UserVarEventParser());
		parser.registgerEventParser(new IncidentEventParser());
		parser.registgerEventParser(new TableMapEventParser());
		parser.registgerEventParser(new WriteRowsEventParser());
		parser.registgerEventParser(new DeleteRowsEventParser());
		parser.registgerEventParser(new UpdateRowsEventParser());
		parser.registgerEventParser(new FormatDescriptionEventParser());
		parser.setBinlogEventListener(new BinlogEventListener() {
			public void onEvents(BinlogEventV4 event) {
				if(event instanceof XidEvent) {
					LOGGER.info("{}",  event);
				}
			}
		});
		parser.start();
		parser.parse(is);
	}
}
