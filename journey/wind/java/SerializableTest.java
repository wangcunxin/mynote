package cn.com.wind.demo.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableTest {

    
    public static byte[] serialize(Object obj) throws SerializeIOException {
        byte[] bytes = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bytes = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            //异常转换
            throw new SerializeIOException("fail to serialize");
        }finally{
            try {
                if(baos!=null){
                    baos.close();
                }
                if(oos!=null){
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    
    public static Object deserialize(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        
         try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(bais!=null){
                    bais.close();
                }
                if(ois!=null){
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return obj;
    }

    public static void main(String[] args) {
        Person p = new Person(0, "kevin", 20);

        try {
            byte[] serialize = serialize(p);
            System.out.println(serialize.length);

            Person deserialize = (Person) deserialize(serialize);
            System.out.println(deserialize);
            
        } catch (SerializeIOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
