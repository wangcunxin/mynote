客户端：
public class MinaTcpClient {
 
 private static final int PORT = 8080; 
 
 public static void main(String[] args) {
  
  NioSocketConnector connector = new NioSocketConnector();
  connector.getFilterChain().addLast("codec",
    new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
  connector.getFilterChain().addLast("logging", new LoggingFilter());
  
  connector.setHandler(ClientHandler.getInstance());
  connector.getSessionConfig().setUseReadOperation(true); 
  
  ConnectFuture connectFuture = connector.connect(new InetSocketAddress("127.0.0.1", 
   PORT)); 
  connectFuture.awaitUninterruptibly();
  
  IoSession session = connectFuture.getSession();
  
  Message msg = new Message("uuid","01","20140101","20140201","01","02","sqltext");
  session.write(msg);
  connector.dispose();
 }
}
public class ClientHandler extends IoHandlerAdapter {
 private static ClientHandler clientHandler = new ClientHandler();
 
 public static IoHandler getInstance() {
  
  return clientHandler;
 }
 private ClientHandler() {
  super();
 }
 @Override
 public void messageReceived(IoSession session, Object message)
   throws Exception {
  Message msg = (Message)message;
  System.out.println(msg.toString());
 }
}
消息实体：
public class Message implements Serializable{
 
 
 private static final long serialVersionUID = 1L;
 
 private String uuid;//标识一次回话
 private String modelId;//模型号
 private String beginDate,endDate;//查询时间段
 private String row,column;//行和列，方便组织结果
 private String sqlText;//查询条件
 private String result;//结果值
}
服务端：
public class MinaTcpServer {
 private static final int PORT = 8080;
 public static void main(String[] args) {
  // 建立监听器
  NioSocketAcceptor acceptor = new NioSocketAcceptor();
  // 配置过滤器链：codec传递对象，logging记录日志
  acceptor.getFilterChain().addLast("codec",
    new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
  acceptor.getFilterChain().addLast("logging", new LoggingFilter());
  // 设置服务端的handler
  acceptor.setHandler(ServerHandler.getInstance());
  
  try {
   acceptor.bind(new InetSocketAddress(PORT));
   System.out.println("server start");
   
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
}
public class ServerHandler extends IoHandlerAdapter  {
 private static ServerHandler serverHandler = new ServerHandler();
 
 public static IoHandler getInstance() {
  
  return serverHandler;
 }
 private ServerHandler() {
  super();
 }
 @Override
 public void messageReceived(IoSession session, Object message)
   throws Exception {
  Message msg = (Message)message;
  msg.setModelId("110");
  Thread th = new Thread(new ServerThread(session,msg));
  th.start();
 }
}
public class ServerThread implements Runnable {
 private Message msg;
 private IoSession session;
 
 public ServerThread(IoSession session, Message msg) {
  this.msg=msg;
  this.session=session;
 }
 @Override
 public void run() {
  try {
   Thread.sleep(5000);
  } catch (InterruptedException e) {
   e.printStackTrace();
  }
  session.write(msg);
 }
}