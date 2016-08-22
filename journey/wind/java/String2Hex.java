package cn.com.wind.email.utils;

import java.io.UnsupportedEncodingException;

public class BytesUtil {

    
    public static String encode(String str) throws UnsupportedEncodingException {
        if(str==null||str.length()==0){
            return "";
        }
        byte[] bytes = str.getBytes("utf-8");
        
        return bytesToHexString(bytes);
    }
    
    
    public static String decode(String str) throws UnsupportedEncodingException{
        if(str==null||str.length()==0){
            return "";
        }
        byte[] bytes = hexStringToBytes(str);
        return new String(bytes,"utf-8");
    }
    
    private static String bytesToHexString(byte[] src){   
        StringBuilder stringBuilder = new StringBuilder("");   
        if (src == null || src.length <= 0) {   
            return null;   
        }   
        for (int i = 0; i < src.length; i++) {   
            int v = src[i] & 0xFF;   
            String hv = Integer.toHexString(v);   
            if (hv.length() < 2) {   
                stringBuilder.append(0);   
            }   
            stringBuilder.append(hv);   
        }   
        return stringBuilder.toString();   
    }
    
    private static byte[] hexStringToBytes(String hexString) {   
        if (hexString == null || hexString.equals("")) {   
            return null;   
        }   
        hexString = hexString.toUpperCase();   
        int length = hexString.length() / 2;   
        char[] hexChars = hexString.toCharArray();   
        byte[] d = new byte[length];   
        for (int i = 0; i < length; i++) {   
            int pos = i * 2;   
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
        }   
        return d;   
    }
    
     private static byte charToByte(char c) {   
            return (byte) "0123456789ABCDEF".indexOf(c);   
        }  
}
