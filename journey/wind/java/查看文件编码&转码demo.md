package cn.com.wind.charset;

import info.monitorenter.cpdetector.CharsetPrinter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FileEncode {
    
    public static String getEncoding(String filename) {
        try {
            CharsetPrinter charsetPrinter = new CharsetPrinter();
            String encode = charsetPrinter.guessEncoding(new File(filename));
            return encode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public static void write(String input, String output, String decode,
            String encode) {
        BufferedReader buffr = null;
        PrintWriter writer = null;
        try {
            buffr = new BufferedReader(new InputStreamReader(
                    new FileInputStream(input), decode));

            writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(output), encode));
            String line = null;
            while ((line = buffr.readLine()) != null) {
                writer.println(line);
            }
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (buffr != null) {
                    buffr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void read(String inputFileName, String encoding) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(
                    inputFileName), encoding));

            String str = "";
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
#测试
public class FileConvertCharset {

    private static String input = "D:/temp/xml/20140915.ua.8";
    private static String output = "D:/temp/xml/20140915.ua.8_2";

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        String encode = FileEncode.getEncoding(input);
        long end = System.currentTimeMillis();
        System.out.println(encode);
        System.out.println("encode:" + (end - begin));
        // FileEncode.read(input, encode);

        System.out.println("----------");
        begin = System.currentTimeMillis();
        FileEncode.write(input, output, encode, "utf-8");
        end = System.currentTimeMillis();
        System.out.println("convert:" + (end - begin));

        encode = FileEncode.getEncoding(output);
        System.out.println(encode);
        // FileEncode.read(output, encode);
    }
}
#验证
public class FileConvertTest {

    private static String inputFile="D:\\temp\\xml\\20140915.ua.8";
    private static Charset charset=null;
           
    public static void main(String[] args) {
        charset = Charset.forName("gb2312");
        FileInputStream input = null;
        try {
             input = new FileInputStream(new File(inputFile));
             byte[] buf = new byte[2014];
             String line= null;
             int len = 0;
             while((len=input.read(buf))!=-1){
                 line = new String(buf,0,len,charset);
                 System.out.println(line);
             }
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
       
    }

}

第三方开源包：
http://sourceforge.jp/projects/sfnet_cpdetector/downloads/cpdetector/binaries/cpdetector_1.0.6.jar/