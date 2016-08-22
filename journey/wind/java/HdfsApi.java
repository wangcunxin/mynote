package cn.com.wind.demo.sqoop;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsApi
{
  public static void createFile(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath = new Path(paramString);
    FSDataOutputStream localFSDataOutputStream = localFileSystem.create(localPath);
    localFSDataOutputStream.write(paramArrayOfByte);
    localFSDataOutputStream.close();
    localFileSystem.close();
    System.out.println("文件创建成功！");
  }

  public static void uploadFile(String paramString1, String paramString2, boolean paramBoolean)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath1 = new Path(paramString1);
    Path localPath2 = new Path(paramString2);
    localFileSystem.copyFromLocalFile(paramBoolean, true, localPath1, localPath2);
    System.out.println("Upload to " + localConfiguration.get("fs.default.name"));
    System.out.println("------------list files------------\n");
    FileStatus[] arrayOfFileStatus1 = localFileSystem.listStatus(localPath2);
    for (FileStatus localFileStatus : arrayOfFileStatus1)
      System.out.println(localFileStatus.getPath());
    localFileSystem.close();
  }

  public static void downloadFile(String paramString1, String paramString2, boolean paramBoolean)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath1 = new Path(paramString1);
    Path localPath2 = new Path(paramString2);
    localFileSystem.copyToLocalFile(paramBoolean, localPath1, localPath2);
    localFileSystem.close();
  }

  public static void rename(String paramString1, String paramString2)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath1 = new Path(paramString1);
    Path localPath2 = new Path(paramString2);
    boolean bool = localFileSystem.rename(localPath1, localPath2);
    if (bool)
      System.out.println("rename ok!");
    else
      System.out.println("rename failure");
    localFileSystem.close();
  }

  public static void delete(String paramString)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath = new Path(paramString);
    boolean bool = localFileSystem.deleteOnExit(localPath);
    if (bool)
      System.out.println("delete ok!");
    else
      System.out.println("delete failure");
    localFileSystem.close();
  }

  public static boolean mkdir(String paramString)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath = new Path(paramString);
    boolean bool = localFileSystem.mkdirs(localPath);
    if (bool)
      System.out.println("create dir ok!");
    else
      System.out.println("create dir failure");
    localFileSystem.close();
    return bool;
  }

  public static void readFile(String paramString)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath = new Path(paramString);
    FSDataInputStream localFSDataInputStream = null;
    try
    {
      localFSDataInputStream = localFileSystem.open(localPath);
      IOUtils.copyBytes(localFSDataInputStream, System.out, 4096, false);
    }
    finally
    {
      IOUtils.closeStream(localFSDataInputStream);
    }
  }

  public static boolean existFolder(String paramString)
    throws IOException
  {
    Configuration localConfiguration = new Configuration();
    FileSystem localFileSystem = FileSystem.get(localConfiguration);
    Path localPath = new Path(paramString);
    return localFileSystem.exists(localPath);
  }
}