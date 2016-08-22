public class DrpcServer extends BaseBasicBolt {
 public static void main(String[] args) throws AlreadyAliveException,
   InvalidTopologyException {
  //function:客户端调用的func name
  LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("hellodrpcserver");
  builder.addBolt(new DrpcServer(), 3);
  
  Config config = new Config();
  config.setDebug(true);
  if (args == null || args.length == 0) {
   LocalDRPC drpc = new LocalDRPC();
   LocalCluster cluster = new LocalCluster();
   
   cluster.submitTopology("localdrpcserver", config,
     builder.createLocalTopology(drpc));
   for (String word : new String[] { "hello", "goodbye" }) {
    System.err.println("Result for "" + word + "": "
      + drpc.execute("exclamation", word));
   }
   cluster.shutdown();
   drpc.shutdown();
  } else {
   // conf.setNumWorkers(3);
   //ui显示的drpc topology名称
   StormSubmitter.submitTopology("drpcserver", config,
     builder.createRemoteTopology());
  }
 }
 public void execute(Tuple tuple, BasicOutputCollector collector) {
  String input = tuple.getString(1);
  collector.emit(new Values(tuple.getValue(0), input + "$$$$$$$$$$$$$"));
 }
 public void declareOutputFields(OutputFieldsDeclarer declarer) {
  declarer.declare(new Fields("id", "result"));
 }
}

public class DrpcClient {
 public static void main(String[] args) throws TException, DRPCExecutionException {
  DRPCClient client = new DRPCClient("10.100.7.19", 3772);
  long start = System.currentTimeMillis();
  //topologyName,args
    String result = client.execute("hellodrpcserver","msg000001"); 
     long end = System.currentTimeMillis();
     System.out.println("result:"+result);
     System.out.println("run time:"+(end-start)+"ms");
 }
}