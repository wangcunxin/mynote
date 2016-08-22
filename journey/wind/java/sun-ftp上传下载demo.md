package cn.com.wind.transferdatabyftp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;



public class Ftp {
   
    private String localfilename;
   
    private String remotefilename;
   
    private FtpClient ftpClient;
    
   
    public void connectServer(String ip, int port, String user,
            String password, String path) {
        try {
            ftpClient = new FtpClient(ip);
            ftpClient.login(user, password);
            // 设置成2进制传输
            ftpClient.binary();
            BiLogger.businessDebug("succed to login ftp server!");
            if (path.length() != 0) {
                // 把远程系统上的目录切换到参数path所指定的目录
                ftpClient.cd(path);
            }
            ftpClient.binary();
        } catch (IOException e) {
            e.printStackTrace();
            BiLogger.businessError("fail to connect server");
            throw new RuntimeException(e);
        }
    }
   
    public void closeConnect() {
        try {
            ftpClient.closeServer();
            BiLogger.businessInfo("disconnect success");
        } catch (IOException ex) {
            ex.printStackTrace();
            BiLogger.businessError("fail to close");
        }
    }
    
   
    public void download(String remoteFile, String localFile) {
        TelnetInputStream is = null;
        FileOutputStream os = null;
        try {
            //获取远程机器上的文件filename，借助TelnetInputStream把该文件传送到本地。
            is = ftpClient.get(remoteFile);
            File file_in = new File(localFile);
            os = new FileOutputStream(file_in);
            byte[] bytes = new byte[1024*1024];
            int len=0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            BiLogger.businessError("fail to download:"+remoteFile);
        } finally{
            try {
                if(is != null){
                    is.close();
                }
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
   
    public void upload(String localFile, String remoteFile) {
        this.localfilename = localFile;
        this.remotefilename = remoteFile;
        TelnetOutputStream os = null;
        FileInputStream is = null;
        try {
            //将远程文件加入输出流中
            os = ftpClient.put(this.remotefilename);
            //获取本地文件的输入流
            File file_in = new File(this.localfilename);
            is = new FileInputStream(file_in);
            //创建一个缓冲区
            byte[] bytes = new byte[1024*1024];
            int len=0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            BiLogger.businessError("fail to fail:"+localFile);
        } finally{
            try {
                if(is != null){
                    is.close();
                }
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   
    
    public List getFileNameList(String ftpDirectory) {
        List list = new ArrayList();
        try {
            DataInputStream dis = new DataInputStream(
                    ftpClient.nameList(ftpDirectory));
            String filename = "";
            while ((filename = dis.readLine()) != null) {
                list.add(filename);
                BiLogger.businessDebug("fileName:"+filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BiLogger.businessError("fail to getFileName");
        }
        return list;
    }
    
   
    public boolean cd(String dir) {
        try {
            ftpClient.cd(dir);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
