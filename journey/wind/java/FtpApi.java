package cn.com.wind.demo.sqoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpApi
{
  public static boolean FTP2HDFS(FileImportPara paramFileImportPara)
  {
    String str1 = paramFileImportPara.HDFSPath + "/" + new File(paramFileImportPara.serverPath).getName();
    try
    {
      if (HdfsAPI.existFolder(str1))
      {
        System.out.println("The folder exists on HDFS!");
        return false;
      }
    }
    catch (IOException localIOException1)
    {
      return false;
    }
    FTPClient localFTPClient = new FTPClient();
    String str2 = CommonFunction.GetTMPFolderPath();
    String str3 = str2 + "/" + new File(paramFileImportPara.serverPath).getName();
    System.out.println(new File(str3).mkdir());
    try
    {
      localFTPClient.connect(paramFileImportPara.server, paramFileImportPara.port);
      localFTPClient.login(paramFileImportPara.userName, paramFileImportPara.password);
      localFTPClient.enterLocalPassiveMode();
      int i = localFTPClient.getReplyCode();
      boolean bool;
      if (!FTPReply.isPositiveCompletion(i))
      {
        localFTPClient.disconnect();
        bool = false;
        return bool;
      }
      while ((paramFileImportPara.serverPath.startsWith("/")) || (paramFileImportPara.serverPath.startsWith("\")))
        paramFileImportPara.serverPath = paramFileImportPara.serverPath.substring(1);
      download2LoacalFolder(localFTPClient, paramFileImportPara.serverPath, str3, paramFileImportPara.HDFSPath);
      HdfsAPI.uploadFile(str3, paramFileImportPara.HDFSPath, true);
      if (!ClearTempFolder(str2))
      {
        System.out.println("Clear local temp folder error!");
        bool = false;
        return bool;
      }
      localFTPClient.logout();
    }
    catch (IOException localIOException3)
    {
      localIOException3.printStackTrace();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    finally
    {
      if (localFTPClient.isConnected())
        try
        {
          localFTPClient.disconnect();
        }
        catch (IOException localIOException8)
        {
        }
    }
    return true;
  }

  private static boolean ClearTempFolder(String paramString)
  {
    return new File(paramString).delete();
  }

  public static void download2LoacalFolder(FTPClient paramFTPClient, String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    if ((new File(paramString2).exists()) || (new File(paramString2).mkdir()))
    {
      paramFTPClient.changeWorkingDirectory("/");
      FTPFile[] arrayOfFTPFile = paramFTPClient.listFiles(paramString1);
      for (int i = 0; i < arrayOfFTPFile.length; i++)
        if (arrayOfFTPFile[i].getType() == 0)
          download2LocalFile(paramFTPClient, paramString1, paramString2, paramString3, arrayOfFTPFile[i].getName());
      for (i = 0; i < arrayOfFTPFile.length; i++)
        if (arrayOfFTPFile[i].getType() == 1)
          download2LoacalFolder(paramFTPClient, paramString1 + "/" + arrayOfFTPFile[i].getName(), paramString2 + "/" + arrayOfFTPFile[i].getName(), paramString3 + "/" + arrayOfFTPFile[i].getName());
    }
  }

  public static void download2LocalFile(FTPClient paramFTPClient, String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception
  {
    if (paramFTPClient.changeWorkingDirectory(paramString1))
    {
      File localFile = new File(paramString2 + "/" + paramString4);
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
      System.out.println(paramFTPClient.retrieveFile(paramString4, localFileOutputStream));
      localFileOutputStream.close();
    }
    else
    {
      System.out.println("changeWorkingDirectory error:" + paramString1);
    }
  }

  public static boolean HDFS2FTP(FileExportPara paramFileExportPara)
  {
    String str1 = CommonFunction.GetTMPFolderPath();
    String str2 = str1 + "/" + new File(paramFileExportPara.HDFSPath).getName();
    try
    {
      HdfsAPI.downloadFile(paramFileExportPara.HDFSPath, str2, paramFileExportPara.isCut);
    }
    catch (IOException localIOException1)
    {
      System.out.println("Download from HDFS to local error!");
      return false;
    }
    FTPClient localFTPClient = new FTPClient();
    try
    {
      localFTPClient.connect(paramFileExportPara.server, paramFileExportPara.port);
      localFTPClient.login(paramFileExportPara.userName, paramFileExportPara.password);
      localFTPClient.enterLocalPassiveMode();
      int i = localFTPClient.getReplyCode();
      boolean bool;
      if (!FTPReply.isPositiveCompletion(i))
      {
        localFTPClient.disconnect();
        bool = false;
        return bool;
      }
      while ((paramFileExportPara.serverPath.startsWith("/")) || (paramFileExportPara.serverPath.startsWith("\")))
        paramFileExportPara.serverPath = paramFileExportPara.serverPath.substring(1);
      uploadFolder2FTP(localFTPClient, paramFileExportPara.serverPath, str2, paramFileExportPara.HDFSPath);
      if (!ClearTempFolder(str1))
      {
        CommonFunction.printLog("HDFS2FTP", "Clear local temp folder error!");
        bool = false;
        return bool;
      }
      localFTPClient.logout();
    }
    catch (IOException localIOException3)
    {
      localIOException3.printStackTrace();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    finally
    {
      if (localFTPClient.isConnected())
        try
        {
          localFTPClient.disconnect();
        }
        catch (IOException localIOException8)
        {
        }
    }
    return true;
  }

  public static void uploadFolder2FTP(FTPClient paramFTPClient, String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    System.out.println(paramString1 + "|" + paramString2);
    String str1 = new File(paramString2).getName();
    String str2 = paramString1 + "/" + str1;
    paramFTPClient.changeWorkingDirectory("/");
    int i = paramFTPClient.mkd(str2);
    if (!FTPReply.isPositiveCompletion(i))
      throw new Exception("Create FTP Folder failed:" + str2);
    System.out.println(i);
    File[] arrayOfFile = new File(paramString2).listFiles();
    String str3;
    for (int j = 0; j < arrayOfFile.length; j++)
    {
      str3 = arrayOfFile[j].getName();
      if ((!str3.startsWith(".")) && (arrayOfFile[j].isFile()))
        uploadFile2FTP(paramFTPClient, str2, paramString2, paramString3, str3);
    }
    for (j = 0; j < arrayOfFile.length; j++)
    {
      str3 = arrayOfFile[j].getName();
      if ((!str3.startsWith(".")) && (arrayOfFile[j].isDirectory()))
        uploadFolder2FTP(paramFTPClient, str2, paramString2 + "/" + str3, paramString3 + "/" + str3);
    }
  }

  public static void uploadFile2FTP(FTPClient paramFTPClient, String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception
  {
    System.out.println(paramString1 + "|" + paramString2 + "|" + paramString4);
    File localFile = new File(paramString2 + "/" + paramString4);
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    if (paramFTPClient.changeWorkingDirectory(paramString1))
      System.out.println(paramFTPClient.storeFile(paramString4, localFileInputStream));
    else
      System.out.println("changeWorkingDirectory error:" + paramString1);
    localFileInputStream.close();
  }

  public static void listFiles(String paramString)
  {
    FTPClient localFTPClient = new FTPClient();
    try
    {
      localFTPClient.connect("10.100.4.208", 21);
      localFTPClient.login("nzhang", "lance");
      localFTPClient.enterLocalPassiveMode();
      int i = localFTPClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(i))
      {
        localFTPClient.disconnect();
        return;
      }
      localFTPClient.changeWorkingDirectory("test");
      FTPFile[] arrayOfFTPFile = localFTPClient.listFiles(paramString);
      System.out.println(arrayOfFTPFile.length);
      localFTPClient.logout();
    }
    catch (IOException localIOException2)
    {
      localIOException2.printStackTrace();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    finally
    {
      if (localFTPClient.isConnected())
        try
        {
          localFTPClient.disconnect();
        }
        catch (IOException localIOException6)
        {
        }
    }
  }

  public static void mkFTPdir(String paramString)
  {
    FTPClient localFTPClient = new FTPClient();
    try
    {
      localFTPClient.connect("10.100.4.208", 21);
      localFTPClient.login("nzhang", "lance");
      localFTPClient.enterLocalPassiveMode();
      int i = localFTPClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(i))
      {
        localFTPClient.disconnect();
        return;
      }
      int j = localFTPClient.mkd(paramString);
      System.out.println(j);
      System.out.println(FTPReply.isPositiveCompletion(j));
      localFTPClient.logout();
    }
    catch (IOException localIOException2)
    {
      localIOException2.printStackTrace();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    finally
    {
      if (localFTPClient.isConnected())
        try
        {
          localFTPClient.disconnect();
        }
        catch (IOException localIOException6)
        {
        }
    }
  }

  public boolean deleteFile(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    boolean bool1 = false;
    FTPClient localFTPClient = new FTPClient();
    try
    {
      localFTPClient.connect(paramString1, paramInt);
      localFTPClient.setControlEncoding("GBK");
      FTPClientConfig localFTPClientConfig = new FTPClientConfig("WINDOWS");
      localFTPClientConfig.setServerLanguageCode("zh");
      localFTPClient.login(paramString2, paramString3);
      int i = localFTPClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(i))
      {
        localFTPClient.disconnect();
        System.out.println("连接服务器失败");
        boolean bool2 = bool1;
        return bool2;
      }
      System.out.println("登陆服务器成功");
      String str1 = new String(paramString5.getBytes("GBK"), "ISO-8859-1");
      String str2 = new String(paramString4.getBytes("GBK"), "ISO-8859-1");
      localFTPClient.changeWorkingDirectory(str2);
      localFTPClient.deleteFile(str1);
      localFTPClient.logout();
      bool1 = true;
    }
    catch (IOException localIOException2)
    {
      System.out.println(localIOException2);
    }
    finally
    {
      if (localFTPClient.isConnected())
        try
        {
          localFTPClient.disconnect();
        }
        catch (IOException localIOException5)
        {
        }
    }
    return bool1;
  }
}
