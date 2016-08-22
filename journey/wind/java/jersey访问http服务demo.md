//替换字符串
String.format("http://%s:50070%s",activeHost,filePath);

//jersey 访问http服务
public class HdfsHttpClient {

    private final static Log log=LogFactory.getLog(HdfsHttpClient.class);
    
    static Client c = Client.create();
    
    
    public static String doInvokeHdfsWeb(String action,String url) {
        
        WebResource r=c.resource(url);
        try {
            log.info("正在调用"+url);
            ClientResponse response = null;
            if("GET".equals(action)){
                response=r.get(ClientResponse.class);;
            }else if("DELETE".equals(action)){
                response=r.delete(ClientResponse.class);
            }
            if(response.getStatus()!=200){
                String msg=response.getEntity(String.class);
                log.error("调用hdfs的webhdfs错误="+"code="+response.getStatus()+",msg="+msg);
                throw new RuntimeException(msg);
            }
            return response.getEntity(String.class);
        } catch (Exception e) {
            String msg="调用hdfsweb异常"+e.getMessage();
            log.error(msg,e.fillInStackTrace());
            HdfsServlet.reSetActiveHost();
            throw new RuntimeException(e.getMessage());
        }
    }
    

}