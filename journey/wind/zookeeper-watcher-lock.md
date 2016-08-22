public class ZookeeperManagerImpl implements IZookeeperManager{

    private final static Logger logger = Logger.getLogger(ZookeeperManagerImpl.class);
    
    private String zkConn;
    private IZookeeperHandler handler;
    private Watcher watcher = new WorkWatcher();
    
    private ZooKeeper zk;
    private CountDownLatch countDownLatch;
    private boolean isActive = false;
    
    public ZookeeperManagerImpl(String zkConn, IZookeeperHandler handler) {
        super();
        this.zkConn = zkConn;
        this.handler = handler;
    }

    @Override
    public void connectZk() throws Exception {
        countDownLatch = new CountDownLatch(1);
        zk = new ZooKeeper(zkConn,CommonVar.SESSION_TIME_OUT,this.watcher);
        
        countDownLatch.await();
        
        if(!zk.getState().isConnected()){
            logger.error("connect time out");
            System.exit(0);
        }
        logger.info("succeed to connect-->sessionid:"+zk.getSessionId());
    }

    @Override
    public void closeZk() throws InterruptedException {
        if(zk!=null){
            zk.close();
            isActive = false;
        }
    }

    @Override
    public void createNode() {
        
    }

    @Override
    public void createRootNode() {
        try {
            Stat stat=this.zk.exists("/"+CommonVar.ROOT_NODE, false);
            if(stat==null){
                zk.create("/"+CommonVar.ROOT_NODE, CommonVar.ROOT_NODE.getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createLockNode() {
        try {
            String lockPath = "/"+CommonVar.ROOT_NODE+"/"+CommonVar.LOCK;
            Stat stat = this.zk.exists(lockPath, false);
            if(stat==null){
                //����lock
                this.zk.create("/"+CommonVar.ROOT_NODE+"/"+CommonVar.LOCK,CommonVar.LOCK.getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                handler.createdLockHandler();
                //�������м
                this.createBreadCrumbNode();
                handler.createdBreadCrumbHandler();
                isActive = true;
            }else{
                logger.error(lockPath+" exists");
            }
            
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createBreadCrumbNode() {
        try {
            String data= CommonVar.getIP()+":"+CommonVar.SERVER_PORT;
            zk.create("/"+CommonVar.ROOT_NODE+"/"+CommonVar.CRUMB, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

     class WorkWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {

            if (event.getType() == EventType.NodeChildrenChanged) {
                //�ڵ�ı䣬���
                Stat lockStat = null;
                Stat crumbStat = null;
                try {
                    lockStat=zk.exists("/"+CommonVar.ROOT_NODE+"/"+CommonVar.LOCK, false);
                    crumbStat=zk.exists("/"+CommonVar.ROOT_NODE+"/"+CommonVar.CRUMB, false);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(lockStat==null && crumbStat==null){
                    createLockNode();
                }
                
            } else if (event.getState() == KeeperState.SyncConnected) {
                logger.info("connect to zk");
                //���ӳɹ������������������м
                countDownLatch.countDown();
                createRootNode();
                createLockNode();
                
            } else if (event.getState() == KeeperState.Disconnected) {
                logger.info("disconnect to zk");
                //�Ͽ�����
                try {
                    closeZk();
                    connectZk();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                createLockNode();
            }
        }

    }
}



public class ZkServer {
   
    private final static Logger logger = Logger.getLogger(ZkServer.class);
   
    public static void main(String[] args) {

        try {
            //��ʼ�������ļ�
            logger.debug(CommonVar.ZOOKEEPER_LIST);
            //���ö�ʱ��
            CountDownLatch countDownLatch=new CountDownLatch(1);
            //��zookeeper�¼���handler
            IZookeeperHandler zookeeperHandler = new ZookeeperHandlerImpl();
           
            IZookeeperManager zookeeperManager = new ZookeeperManagerImpl(CommonVar.ZOOKEEPER_LIST,zookeeperHandler);
            zookeeperManager.connectZk();
           
            countDownLatch.await();
           
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

}


public class ZookeeperHandlerImpl implements IZookeeperHandler {

    @Override
    public void createdLockHandler() {
        System.out.println("���ػ�������");
    }

    @Override
    public void nodeDeletedHandler() {
       
    }

    @Override
    public void createdBreadCrumbHandler() {
        System.out.println("����һ������");
    }

}