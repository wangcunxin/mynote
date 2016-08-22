1.QueryServer
public class QueryServer {
 private final static Logger log = Logger.getLogger(QueryServer.class);
 public static Map> cacheMap = new HashMap>();
 private static ExecutorService threadPool = Executors
   .newFixedThreadPool(1000);
 public static void main(String[] args) {
  log.info("query server start to running...");
  try {
   ServerSocket serverSocket = new ServerSocket(8888);
   Socket server = null;
   ServiceThread thread = null;
   while (true) {
    server = serverSocket.accept();
    thread = new ServiceThread(server);
    threadPool.execute(thread);
   }
  } catch (IOException e) {
   e.printStackTrace();
   log.error("socket server fail to do ");
  } finally {
   if (threadPool != null) {
    threadPool.shutdown();
   }
  }
 }
 public static ExecutorService getThreadPool() {
  return threadPool;
 }
}
2.ServiceThread
public class ServiceThread implements Runnable, Serializable {
 private final static Logger log = Logger.getLogger(ServiceThread.class);
 private Socket server;
 private static Map> cacheMap = QueryServer.cacheMap;
 private  Map map = null ;
 private String uuid = null;
 
 public ServiceThread(Socket server) {
  this.server = server;
  uuid = UUID.randomUUID().toString();
  cacheMap.put(uuid, new TreeMap());
 }
 @Override
 public void run() {
  ExecutorService threadPool = QueryServer.getThreadPool();
  InputStream inputStream = null;
  OutputStream outputStream = null;
  DataInputStream dataInputStream = null;
  DataOutputStream dataOutputStream = null;
  ServiceTask task = null;
  int cellQty = 0;
  try {
   inputStream = server.getInputStream();
   outputStream = server.getOutputStream();
   // 接收请求参数
   dataInputStream = new DataInputStream(inputStream);
   String paramJsonStr = dataInputStream.readUTF();
   log.info(paramJsonStr);
   // 解析json数据
   // 查询条件字符串分拆
   String[] params = paramJsonStr.split("#");
   int conditionQty = params.length - 2;
   String indexId = params[0];// 报表索引编号
   String[] split = params[1].split("-");// 开始结束日期
   String beginDate = split[0];
   String endDate = split[1];
   int periodDate = DateUtil.getDayNum(beginDate, endDate);
   cellQty = conditionQty * periodDate;
   String searchDate = null;
   for (int i = 0; i < periodDate; i++) {// 行
    searchDate = DateUtil.getDate(beginDate, i);
    for (int j = 2; j < params.length; j++) {// 列
     String sql = params[j];
     int distance = j - 2;
     String param = indexId + "#" + searchDate + "#" + distance
       + "#" + sql;
     task = new ServiceTask(param,uuid);
     threadPool.execute(task);
    }
   }
   // 定时3s
   int size = 0;
   long begin = System.currentTimeMillis();
   long end = 0;
   while (true) {
    map = ServiceThread.getCacheMap(uuid);
    size = map.size();
    if (size >= cellQty) {
     // 将cacheMap结果拼接成json返回结果
     String result = map.toString();
     // 响应返回
     dataOutputStream = new DataOutputStream(outputStream);
     dataOutputStream.writeBytes(result);
     dataOutputStream.flush();
     cacheMap.remove(uuid);
     break;
    } else {
     end = System.currentTimeMillis();
     if ((end - begin) > 3000) {
      dataOutputStream = new DataOutputStream(outputStream);
      dataOutputStream.writeBytes("查询超时，请重新查询");
      dataOutputStream.flush();
      break;
     }
    }
    Thread.sleep(100);
   }
  } catch (IOException e) {
   e.printStackTrace();
  } catch (ParseException e) {
   e.printStackTrace();
  } catch (InterruptedException e) {
   e.printStackTrace();
  } catch (Exception e) {
   e.printStackTrace();
  } finally {
   try {
    if (dataOutputStream != null) {
     dataOutputStream.close();
    }
    if (dataInputStream != null) {
     dataInputStream.close();
    }
    if (inputStream != null) {
     inputStream.close();
    }
    if (outputStream != null) {
     outputStream.close();
    }
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }
 public static Map getCacheMap(String uuid) {
  return cacheMap.get(uuid);
 }
}
3.ServiceTask
public class ServiceTask implements Runnable {
 private final static Logger log = Logger.getLogger(ServiceTask.class);
 private String param;// indexId+"#"+searchDate+"#"+distance+"#"+sql;
 private String uuid;
 public ServiceTask(String param, String uuid) {
  super();
  this.param = param;
  this.uuid = uuid;
 }
 @Override
 public void run() {
  DataOutputStream dataOutputStream = null;
  DataInputStream dataInputStream = null;
  Socket socket = null;
  try {
   socket = new Socket("127.0.0.1", 9999);
   // 发送消息
   dataOutputStream = new DataOutputStream(socket.getOutputStream());
   dataOutputStream.writeUTF(param);
   dataOutputStream.flush();
   // 接收消息
   dataInputStream = new DataInputStream(socket.getInputStream());
   byte[] buf = new byte[1024];
   int len = 0;
   StringBuffer sb = new StringBuffer();
   while ((len = dataInputStream.read(buf)) != -1) {
    sb.append(new String(buf, 0, len, "UTF-8"));
   }
   // indexId+"#"+searchDate+"#"+distance+"#"+amount
   String result = sb.toString();
   log.info(result);
   String[] split = result.split("#");
   // 正常返回结果
   if (split.length == 3) {
    String searchDate = split[0];
    String distance = split[1];
    String amount = split[2];
    Map cacheMap = ServiceThread.getCacheMap(uuid);
    cacheMap.put(searchDate + "#" + distance, amount);
   }
  } catch (UnknownHostException e) {
   e.printStackTrace();
  } catch (UnsupportedEncodingException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  } finally {
   try {
    if (dataOutputStream != null) {
     dataOutputStream.close();
    }
    if (dataInputStream != null) {
     dataInputStream.close();
    }
    if (socket != null) {
     socket.close();
    }
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }
}
4.IndexServer
public class IndexServer {
 private final static Logger log = Logger.getLogger(IndexServer.class);
 private static Map indexMap = new HashMap();
 private static ExecutorService threadPool = Executors
   .newFixedThreadPool(1000);
 public static void main(String[] args) {
  log.info("index server start to running...");
  try {
   ServerSocket serverSocket = new ServerSocket(9999);
   Socket server = null;
   ServiceThread thread = null;
   while (true) {
    server = serverSocket.accept(); 
    thread = new ServiceThread(server);
    threadPool.execute(thread);
   }
  } catch (IOException e) {
   e.printStackTrace();
   log.error("socket server fail to do ");
  } finally {
   if (threadPool != null) {
    threadPool.shutdown();
   }
  }
 }
 public static Map getIndexMap(){
  return Collections.synchronizedMap(indexMap);
 }
 
 public static synchronized ExecutorService getThreadPool(){
  return threadPool;
 }
}
5.ServiceThread
public class ServiceThread implements Runnable, Serializable{
 private final static Logger log = Logger.getLogger(ServiceThread.class);
 private Socket server;
 
 public ServiceThread(Socket server) {
  this.server = server;
 }
 @Override
 public void run() {
  ExecutorService threadPool = IndexServer.getThreadPool();
  InputStream inputStream = null;
  OutputStream outputStream = null;
  DataInputStream dataInputStream = null;
  DataOutputStream dataOutputStream = null;
  ServiceTask task = null;
  
  try {
   inputStream = server.getInputStream();
   outputStream = server.getOutputStream();
   // 接收请求参数
   dataInputStream = new DataInputStream(inputStream);
   String paramStr = dataInputStream.readUTF();
   log.info(paramStr);
   task = new ServiceTask(paramStr);
   Future future = threadPool.submit(task);
   String result = future.get();
   log.info(result);
   
   // 响应返回
   dataOutputStream = new DataOutputStream(outputStream);
   dataOutputStream.writeBytes(result);
   dataOutputStream.flush();
   
  } catch (IOException e) {
   e.printStackTrace();
  } catch (InterruptedException e) {
   e.printStackTrace();
  } catch (ExecutionException e) {
   e.printStackTrace();
  }finally{
   try {
    if (dataOutputStream != null) {
     dataOutputStream.close();
    }
    if (dataInputStream != null) {
     dataInputStream.close();
    }
    if (inputStream != null) {
     inputStream.close();
    }
    if (outputStream != null) {
     outputStream.close();
    }
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }
 
6.ServiceTask
public class ServiceTask implements Callable {
 private String param;
 public ServiceTask(String param) {
  super();
  this.param = param;
 }
 @Override
 public String call() throws Exception {
  String[] split = this.param.split("#");
  if (split.length < 4) {
   return null;
  }
  String indexId = split[0];
  String dateStr = split[1];
  String distanceStr = split[2];
  //String id = split[3];
  String queryText = split[3];
  int indexIdInt = Integer.parseInt(indexId);
  int classType = Integer.parseInt(dateStr);
  Map indexMap = IndexServer.getIndexMap();
  String key = indexId + "_" + dateStr;
  if (indexMap.get(key) == null) {
   CppIndex cppIndex = new CppIndex((short) indexIdInt);
   cppIndex.loadDump(classType);
   indexMap.put(key, cppIndex);
  }
  CppIndex index = indexMap.get(key);
  
  int[] result = index.query(queryText, classType, classType, 1);
  String resultStr = dateStr + "#" + distanceStr + "#" + result[0];
  return resultStr;
 }
}
7.client
public class Client {
 public static void main(String[] args) throws UnknownHostException, IOException {
 try {
   Socket socket = new Socket("10.100.3.61", 8888);
   //Socket socket = new Socket("127.0.0.1", 8888);
   //发送消息
   DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
   dataOutputStream.writeUTF("100011#20131108-20131115#osid:4");
   dataOutputStream.flush();
   
   //接收消息
   DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
   byte[] buf = new byte[1024];
   int len=0;
   StringBuffer sb = new StringBuffer();
   while ((len = dataInputStream.read(buf)) != -1) {
       sb.append(new String(buf,0,len,"UTF-8"));
   }
   System.out.println(sb.toString());
   
   dataOutputStream.close();
   dataInputStream.close();
   socket.close();
  } catch (UnknownHostException e) {
   e.printStackTrace();
  } catch (UnsupportedEncodingException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
}