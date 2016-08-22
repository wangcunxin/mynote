生产者：
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
public class ProducerSample {
 public final static String topicName = "mytopic";
 public static void main(String[] args) {
  Properties props = new Properties();
  props.put("zk.connect", "10.100.5.9:2181");
  props.put("serializer.class", "kafka.serializer.StringEncoder");
  props.put("metadata.broker.list", "10.100.5.9:9092,10.100.5.9:9093,10.100.5.9:9094");
  ProducerConfig config = new ProducerConfig(props);
  Producer producer = new Producer(config);
  int i =0;
  while(true){
   producer.send(new KeyedMessage(topicName, "test" + (++i)));
   try {
    Thread.sleep(2000);
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
  }
 }
}
 
消费者：
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
public class ConsumerSample {
 public final static String topicName = "mytopic";
 public static void main(String[] args) {
  Properties props = new Properties();
  props.put("zookeeper.connect", "10.100.5.9:2181");
  props.put("group.id", "0");
  props.put("zookeeper.session.timeout.ms", "400000");
  props.put("zookeeper.sync.time.ms", "200");
  props.put("auto.commit.interval.ms", "1000");
  ConsumerConfig consumerConfig = new ConsumerConfig(props);
  ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
  Map topicCountMap = new HashMap();
  topicCountMap.put(topicName, new Integer(1));
  
  Map>> consumerMap = consumer.createMessageStreams(topicCountMap);
  KafkaStream stream = consumerMap.get(topicName).get(0);
  
  ConsumerIterator it = stream.iterator();
  while (it.hasNext())
   System.out.println(new String(it.next().message()));
 }
}