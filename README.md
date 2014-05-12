open-replicator
===============

Open Replicator is a high performance MySQL binlog parser written in Java. It unfolds the possibilities that you can parse, filter and broadcast the binlog events in a real time manner.

### note

For MySQL 5.6.6 users, binlog_checksum system variable is NOT supported by open-replicator at the moment, please set it to NONE.

### releases
1.0.7

    release date: 2014-05-12
    support signed tinyint, smallint, mediumint, int, bigint
    
1.0.6

    release date: 2014-05-08
    remove dependency commons-lang, log4j
    support MYSQL_TYPE_TIMESTAMP2, MYSQL_TYPE_DATETIME2, MYSQL_TYPE_TIME2 

1.0.0

    release date: 2011-12-29
    
### maven
```
<dependency>
        <groupId>open-replicator</groupId>
        <artifactId>open-replicator</artifactId>
        <version>1.0.7</version>
</dependency>
```
### parsers

BinlogEventParser is plugable. All available implementations are registered by default, but you can register only the parsers you are interested in. 
![Alt text](http://dl.iteye.com/upload/attachment/0070/3054/4274ab64-b6d2-380b-86b2-56afa0de523d.png)

### usage
```
final OpenReplicator or = new OpenReplicator();
or.setUser("root");
or.setPassword("123456");
or.setHost("localhost");
or.setPort(3306);
or.setServerId(6789);
or.setBinlogPosition(4);
or.setBinlogFileName("mysql_bin.000001");
or.setBinlogEventListener(new BinlogEventListener() {
    public void onEvents(BinlogEventV4 event) {
        // your code goes here
    }
});
or.start();

System.out.println("press 'q' to stop");
final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
for(String line = br.readLine(); line != null; line = br.readLine()) {
    if(line.equals("q")) {
        or.stop();
        break;
    }
}
```
