package cn.com.wind.demo.sqoop;

import com.cloudera.sqoop.Sqoop;
import com.cloudera.sqoop.tool.ExportTool;
import com.cloudera.sqoop.tool.ImportTool;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;

public class SqoopApi {

    public static void export2DB(DBExportPara paramDBExportPara) {
        Configuration localConfiguration = new Configuration();
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("--table");
        localArrayList.add(paramDBExportPara.tableName);
        localArrayList.add("--export-dir");
        localArrayList.add(paramDBExportPara.HDFSPath);
        localArrayList.add("--connect");
        String str = new StringBuilder()
                .append("jdbc:sqlserver://")
                .append(paramDBExportPara.server)
                .append(":")
                .append(paramDBExportPara.port.compareTo("") == 0 ? "1433"
                        : paramDBExportPara.port).append(";userName=")
                .append(paramDBExportPara.userName).append(";password=")
                .append(paramDBExportPara.password).append(";database=")
                .append(paramDBExportPara.database).toString();
        localArrayList.add(str);
        if ((paramDBExportPara.selectCols != null)
                && (paramDBExportPara.selectCols.length() > 0)) {
            localArrayList.add("--columns");
            localArrayList.add(new StringBuilder().append(""")
                    .append(paramDBExportPara.selectCols.replace(""", ""))
                    .append(""").toString());
        }
        if ((paramDBExportPara.fieldsTerminatedBy != null)
                && (paramDBExportPara.fieldsTerminatedBy.length() > 0)) {
            localArrayList.add("--fields-terminated-by");
            localArrayList.add(paramDBExportPara.fieldsTerminatedBy);
        }
        localArrayList.add("-m");
        localArrayList.add("1");
        String[] arrayOfString = new String[1];
        ExportTool localExportTool = new ExportTool();
        Sqoop localSqoop = new Sqoop(localExportTool);
        localSqoop.setConf(localConfiguration);
        arrayOfString = (String[]) localArrayList.toArray(new String[0]);
        int i = Sqoop.runSqoop(localSqoop, arrayOfString);
        System.out.println(new StringBuilder().append("res:").append(i)
                .toString());
    }
    
      public static void import2HDFS(DBImportPara paramDBImportPara)
                throws Exception
              {
                Configuration localConfiguration = new Configuration();
                ArrayList localArrayList = new ArrayList();
                localArrayList.add("--query");
                localArrayList.add(paramDBImportPara.query);
                localArrayList.add("--connect");
                String str = new StringBuilder().append(getConnectHeader(paramDBImportPara.type)).append(paramDBImportPara.server).append(":").append(paramDBImportPara.port.compareTo("") == 0 ? "1433" : paramDBImportPara.port).append(";userName=").append(paramDBImportPara.userName).append(";password=").append(paramDBImportPara.password).append(";database=").append(paramDBImportPara.database).toString();
                localArrayList.add(str);
                localArrayList.add("--target-dir");
                localArrayList.add(paramDBImportPara.HDFSPath);
                localArrayList.add("-m");
                localArrayList.add("1");
                String[] arrayOfString = new String[1];
                ImportTool localImportTool = new ImportTool();
                Sqoop localSqoop = new Sqoop(localImportTool);
                localSqoop.setConf(localConfiguration);
                arrayOfString = (String[])localArrayList.toArray(new String[0]);
                int i = Sqoop.runSqoop(localSqoop, arrayOfString);
                System.out.println(new StringBuilder().append("res:").append(i).toString());
              }

              private static String getConnectHeader(DBType paramDBType)
                throws Exception
              {
                switch (1.$SwitchMap$Wind$Galaxy$Tools$DBType[paramDBType.ordinal()])
                {
                case 1:
                  return "jdbc:sqlserver://";
                case 2:
                  return "jdbc:mysqlserver://";
                }
                throw new Exception("DBType Error!");
              }
}
