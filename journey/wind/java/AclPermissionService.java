package cn.com.wind.demo.fsacl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryScope;
import org.apache.hadoop.fs.permission.AclStatus;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.shell.PathData;

public class AclPermissionService {

    private static Configuration conf = new Configuration();
    public static void main(String[] args) {
        //System.out.println(listFsAction("r--"));
        System.out.println(getUer("hdfs://10.100.1.201:8020/home/x2hadoop/cloud"));
    }

    
    public static FsAction listFsAction(String permission) {
        FsAction action=null;
        for (FsAction fsAction : FsAction.values()) {
            System.out.println(fsAction.SYMBOL);
            if (fsAction.SYMBOL.equals(permission)) {
                action= fsAction;
            }
        }
        return action;
    }
    
    
    public static String getUer(String filePath) {
        String userL = null;
        PathData pathData;
        try {
            pathData = new PathData(filePath, conf);
            AclStatus status = pathData.fs.getAclStatus(pathData.path);
            int index = 0;
            for (AclEntry entry : status.getEntries()) {
                String user = entry.getName();
                if (user != null) {
                    if (0 == index) {
                        userL = user;
                    } else {
                        userL += ";";
                        userL += user;
                    }
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userL;
    }
    
    public static String getPermission(String filePath) {
        String acl = null;
        try {
            PathData pathData;
            pathData = new PathData(filePath, conf);
            AclStatus status = pathData.fs.getAclStatus(pathData.path);
            int index = 0;
            for (AclEntry entry : status.getEntries()) {
                String user = entry.getName();
                if (user != null) {
                    String permissionCode = entry.getPermission().SYMBOL;
                    String tmp = user + ":" + permissionCode;
                    AclEntryScope scope = entry.getScope();
                    tmp = tmp + ":" + scope;
                    if (0 == index) {
                        acl = tmp;
                    } else {
                        acl += ";" ;
                        acl += tmp ;
                    }
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return acl;
    }
}
