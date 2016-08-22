package cn.com.wind.demo.sqoop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class SmbApi {
      public static File readFromSmb(String paramString1, String paramString2)
      {
        File localFile = null;
        BufferedInputStream localBufferedInputStream = null;
        BufferedOutputStream localBufferedOutputStream = null;
        try
        {
          SmbFile localSmbFile = new SmbFile(paramString1);
          String str = localSmbFile.getName();
          localBufferedInputStream = new BufferedInputStream(new SmbFileInputStream(localSmbFile));
          localFile = new File(paramString2 + File.separator + str);
          System.out.println("localfile==" + localFile);
          localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile));
          int i = localSmbFile.getContentLength();
          System.out.println("length==" + i);
          byte[] arrayOfByte = new byte[i];
          Date localDate1 = new Date();
          localBufferedInputStream.read(arrayOfByte);
          localBufferedOutputStream.write(arrayOfByte);
          Date localDate2 = new Date();
          int j = (int)((localDate2.getTime() - localDate1.getTime()) / 1000L);
          if (j > 0)
            System.out.println("用时:" + j + "秒 " + "速度:" + i / j / 1024 + "kb/秒");
        }
        catch (Exception localException)
        {
          System.out.println(localException.getMessage());
        }
        finally
        {
          try
          {
            localBufferedOutputStream.close();
            localBufferedInputStream.close();
          }
          catch (IOException localIOException3)
          {
            localIOException3.printStackTrace();
          }
        }
        return localFile;
      }

      public static boolean removeFile(File paramFile)
      {
        return paramFile.delete();
      }
}
