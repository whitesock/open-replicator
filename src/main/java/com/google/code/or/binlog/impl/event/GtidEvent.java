package com.google.code.or.binlog.impl.event;

public class GtidEvent extends AbstractBinlogEventV4
{
  private final byte[] sourceId;
  private final long transactionId;

  public GtidEvent(byte[] sourceId, long transactionId) {
    this.sourceId = sourceId;
    this.transactionId = transactionId;
  }

  public byte[] getSourceId() {
    return sourceId;
  }

  public long getTransactionId() {
    return transactionId;
  }
}
