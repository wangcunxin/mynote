package cn.com.wind.transferdatabyftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class ApacheFtpManager {

    private static ApacheFtpManager manager = new ApacheFtpManager();
    private static Logger log = Logger.getLogger(ApacheFtpManager.class);
    
    private FTPClient ftpClient;
    private String ip,user,pwd;
    private int port;
    
    public static ApacheFtpManager getInstance(){
        return manager;
    }
    private ApacheFtpManager() {
        this.ftpClient = new FTPClient();
        this.ip = Constant.ip;
        this.user = Constant.user;
        this.pwd = Constant.password;
        this.port = Constant.port;
    }

    public ApacheFtpManager(FTPClient ftpClient, String ip, String user,
            String pwd, int port) {
        super();
        this.ftpClient = ftpClient;
        this.ip = ip;
        this.user = user;
        this.pwd = pwd;
        this.port = port;
    }
    
    public boolean login(){
        boolean flag = true;
          try {
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setDefaultPort(port);
            ftpClient.connect(ip);
            ftpClient.login(user, pwd);

            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                flag = false;
            }else{
                BiLogger.businessInfo("connect & login");
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
          return flag;        
    }
    
    public void logout(){
        try {
            if(ftpClient!=null){
                boolean logout = this.ftpClient.logout();
                if(logout){
                    log.info("logout");
                }else{
                    log.error("fail to logout");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                this.ftpClient.disconnect();
                BiLogger.businessInfo("disconnect");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean download(String remoteFile, String localFile){
        boolean flag = true;
        
        BufferedOutputStream output = null;
        
        try {
            output = new BufferedOutputStream(new FileOutputStream(localFile));
             flag = this.ftpClient.retrieveFile(remoteFile, output);
             output.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(output!=null){
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean upload(String localFile, String remoteFile) {
        boolean flag = true;
        BufferedInputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(localFile));
            
            flag = this.ftpClient.storeFile(remoteFile,input);
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(input!=null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public List getFilePathList(String ftpDirectory) {
        List list = new ArrayList();
        try {
            String[] names = ftpClient.listNames(ftpDirectory);
            
            for (String name : names) {
                list.add(name);
                BiLogger.businessDebug("-->fileName:" + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
            BiLogger.businessError("the path isn't exist:" + ftpDirectory);
        }
        return list;
    }
}