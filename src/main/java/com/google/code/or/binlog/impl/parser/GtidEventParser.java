package com.google.code.or.binlog.impl.parser;

import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.binlog.BinlogParserContext;
import com.google.code.or.binlog.impl.event.GtidEvent;
import com.google.code.or.common.util.MySQLConstants;
import com.google.code.or.io.XInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * GTID Event
 *
 * <p>
 *     Event format:
 *     <pre>
 *         +-------------------+
 *         | 1B commit flag    |
 *         +-------------------+
 *         | 16B Source ID     |
 *         +-------------------+
 *         | 8B Txn ID         |
 *         +-------------------+
 *         | ...               |
 *         +-------------------+
 *     </pre>
 * </p>
 */
public class GtidEventParser extends AbstractBinlogEventParser {

  public GtidEventParser() {
    super(MySQLConstants.GTID_LOG_EVENT);
  }

  @Override
  public void parse(XInputStream is, BinlogEventV4Header header, BinlogParserContext context) throws IOException {
    is.readBytes(1); // commit flag, always true
    byte[] sourceId = is.readBytes(16);
    long transactionId = ByteBuffer.wrap(is.readBytes(8)).order(ByteOrder.LITTLE_ENDIAN).getLong();
    is.skip(is.available()); // position at next event

    GtidEvent event = new GtidEvent(sourceId, transactionId);
    event.setHeader(header);

    context.getEventListener().onEvents(event);
  }
}
