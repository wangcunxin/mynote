public class FirstTopology {
 public static void main(String[] args){
  try {
   // ʵ����TopologyBuilder�ࡣ
   TopologyBuilder topologyBuilder = new TopologyBuilder();
   // �����緢�ڵ㲢���䲢�������ò�����������Ƹö����ڼ�Ⱥ�е��߳�����
   topologyBuilder.setSpout("firstspout", new FirstSpout(), 1);
   topologyBuilder.setSpout("secondspout", new SecondSpout(), 1);
   topologyBuilder.setSpout("thirdspout", new ThirdSpout(), 1);
   // �������ݴ���ڵ㲢���䲢������ָ���ýڵ�����緢�ڵ�Ĳ���Ϊ�����ʽ��
   topologyBuilder.setBolt("firstbolt1", new FirstBolt(), 1)
     .shuffleGrouping("firstspout");
   topologyBuilder.setBolt("firstbolt2", new FirstBolt(), 1)
     .shuffleGrouping("secondspout");
   topologyBuilder.setBolt("firstbolt3", new FirstBolt(), 2)
     .shuffleGrouping("thirdspout");
   Config config = new Config();
   config.setDebug(false);
   // ��Ⱥģʽ
   if (args != null && args.length > 0) {
    config.setNumWorkers(3);
    StormSubmitter.submitTopology(args[0], config,
      topologyBuilder.createTopology());
   } else {
    // ����ģʽ
    config.setMaxTaskParallelism(1);
    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology("simple", config,
      topologyBuilder.createTopology());
   }
   
  } catch (AlreadyAliveException e) {
   e.printStackTrace();
  } catch (InvalidTopologyException e) {
   e.printStackTrace();
  }
 }
}


public class FirstSpout extends BaseRichSpout {
 // �����������ݵĹ�����
 private SpoutOutputCollector collector;
 private static String[] info = new String[] { "wind01", "wind02", "wind03",
   "wind04", "wind05", "wind06" };
 private Random random = new Random();
 
 public void open(Map conf, TopologyContext context,
   SpoutOutputCollector collector) {
  this.collector = collector;
 }
 
 @Override
 public void nextTuple() {
  try {
   String msg = info[random.nextInt(6)];
   System.out.println("spout send :" + msg);
   // ���÷��䷽��
   collector.emit(new Values(msg));
   Thread.sleep(5000);
  } catch (InterruptedException e) {
   e.printStackTrace();
  }
 }
 
 @Override
 public void declareOutputFields(OutputFieldsDeclarer declarer) {
  // collector.emit(new Values(msg));����Ҫ��Ӧ
  declarer.declare(new Fields("source"));
 }
 @Override
 public void ack(Object msgId) {
  System.out.println("***************" + msgId);
  super.ack(msgId);
 }
 @Override
 public void fail(Object msgId) {
  System.out.println("&&&&&&&&&&&&&&" + msgId);
  super.fail(msgId);
 }
}

public class SecondSpout extends BaseRichSpout {
 private SpoutOutputCollector collector;
 private static List list = new ArrayList(1);
 @Override
 public void open(Map conf, TopologyContext context,
   SpoutOutputCollector collector) {
  this.collector = collector;
  list.add("2");
 }
 @Override
 public void nextTuple() {
  try {
   if (list.size() > 0) {
    String value = list.remove(0);
    System.out.println("spout2 send :" + value);
    // ���÷��䷽��
    collector.emit(new Values(value));
   } else {
    collector.emit(new Values("1"));
   }
   Thread.sleep(4000);
  } catch (InterruptedException e) {
   e.printStackTrace();
  }
 }
 @Override
 public void declareOutputFields(OutputFieldsDeclarer declarer) {
  declarer.declare(new Fields("source"));
 }
 @Override
 public void ack(Object msgId) {
  System.out.println("***************" + msgId);
  super.ack(msgId);
 }
 @Override
 public void fail(Object msgId) {
  System.out.println("&&&&&&&&&&&&&&" + msgId);
  super.fail(msgId);
 }
}

public class ThirdSpout extends BaseRichSpout {
 public final static String topicName = "mytopic";
 private SpoutOutputCollector collector;
 private ConsumerIterator it = null;
 @Override
 public void open(Map conf, TopologyContext context,
   SpoutOutputCollector collector) {
  this.collector = collector;
  Properties props = new Properties();
  props.put("zookeeper.connect", "10.100.5.9:2181");
  props.put("group.id", "0");
  props.put("zookeeper.session.timeout.ms", "400000");
  props.put("zookeeper.sync.time.ms", "200");
  props.put("auto.commit.interval.ms", "1000");
  ConsumerConfig consumerConfig = new ConsumerConfig(props);
  ConsumerConnector consumer = kafka.consumer.Consumer
    .createJavaConsumerConnector(consumerConfig);
  Map topicCountMap = new HashMap();
  topicCountMap.put(topicName, new Integer(1));
  Map>> consumerMap = consumer
    .createMessageStreams(topicCountMap);
  KafkaStream stream = consumerMap.get(topicName).get(0);
  this.it = stream.iterator();
 }
 @Override
 public void nextTuple() {
  try {
   if (it.hasNext()) {
    String msg = new String(it.next().message());
    System.out.println("spout send :" + msg);
    // ���÷��䷽��
    collector.emit(new Values(msg));
   }
   Thread.sleep(1000);
  } catch (InterruptedException e) {
   e.printStackTrace();
  }
 }
 @Override
 public void declareOutputFields(OutputFieldsDeclarer declarer) {
  declarer.declare(new Fields("source"));
 }
 @Override
 public void ack(Object msgId) {
  System.out.println("***************" + msgId);
  super.ack(msgId);
 }
 @Override
 public void fail(Object msgId) {
  System.out.println("&&&&&&&&&&&&&&" + msgId);
  super.fail(msgId);
 }
}
public class FirstBolt extends BaseBasicBolt {
 @Override
 public void execute(Tuple input, BasicOutputCollector collector) {
  // Returns the String at position i in the tuple.
  String msg = input.getString(0);
  if (msg != null) {
   try {
    if ("2".equals(msg)) {//�����ط�
     System.out.println("bolt fail to done:" + msg);
     throw new RuntimeException("xxx");
    } else {
     collector.emit(new Values(msg + "msg is processed!"));
     System.out.println("bolt send:" + msg);
    }
   } catch (Exception e) {
    e.printStackTrace();
   }
  }
 }
 @Override
 public void declareOutputFields(OutputFieldsDeclarer declarer) {
  declarer.declare(new Fields("msg"));
 }
}