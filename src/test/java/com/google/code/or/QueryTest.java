package com.google.code.or;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.or.common.glossary.column.StringColumn;
import com.google.code.or.io.impl.SocketFactoryImpl;
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
public class QueryTest {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryTest.class);

	/**
	 * 
	 */
	public static void main(String args[]) throws Exception {
		//
		Log4jInitializer.initialize();
		
		//
		final AuthenticatorImpl authenticator = new AuthenticatorImpl();
		authenticator.setUser("xjq");
		authenticator.setPassword("123456");
		authenticator.setInitialSchema("test");
		
		//
		final TransportImpl transport = new TransportImpl();
		transport.setAuthenticator(authenticator);
		transport.setSocketFactory(new SocketFactoryImpl());
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
			LOGGER.info("{}", error);
			return;
		}
		
		//
		final ResultSetHeaderPacket header = ResultSetHeaderPacket.valueOf(packet);
		LOGGER.info("{}", header);
		
		//
		while(true) {
			packet = transport.getInputStream().readPacket();
			if(packet.getPacketBody()[0] == EOFPacket.PACKET_MARKER) {
				EOFPacket eof = EOFPacket.valueOf(packet);
				LOGGER.info("{}", eof);
				break;
			} else {
				ResultSetFieldPacket field = ResultSetFieldPacket.valueOf(packet);
				LOGGER.info("{}", field);
			}
		}
		
		//
		while(true) {
			packet = transport.getInputStream().readPacket();
			if(packet.getPacketBody()[0] == EOFPacket.PACKET_MARKER) {
				EOFPacket eof = EOFPacket.valueOf(packet);
				LOGGER.info("{}", eof);
				break;
			} else {
				ResultSetRowPacket row = ResultSetRowPacket.valueOf(packet);
				LOGGER.info("{}", row);
			}
		}
	}
}
