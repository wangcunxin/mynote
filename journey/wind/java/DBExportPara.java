package cn.com.wind.demo.sqoop;

import org.w3c.dom.NodeList;

public class DBExportPara {

      private FunctionCalculate funcCal = new FunctionCalculate();
      String KEY_SERVER = "Wind.Galaxy.Export.Server";
      String KEY_Type = "Wind.Galaxy.Export.Type";
      String KEY_Port = "Wind.Galaxy.Export.Port";
      String KEY_USERNAME = "Wind.Galaxy.Export.UserName";
      String KEY_PASSWORD = "Wind.Galaxy.Export.Password";
      String KEY_DATABASE = "Wind.Galaxy.Export.Database";
      String KEY_TABLE_NAME = "Wind.Galaxy.Export.TableName";
      String KEY_SELECT_COLS = "Wind.Galaxy.Export.SelectCols";
      String KEY_HDFSPATH = "Wind.Galaxy.Export.HDFSPath";
      String KET_FIELDS_TERMINATED_BY = "Wind.Galaxy.Export.FieldsTerminatedBy";
      String KEY_IS_REPLACE = "Wind.Galaxy.Export.IsReplace";
      public String server;
      public String type;
      public String port;
      public String userName;
      public String password;
      public String database;
      public String tableName;
      public String selectCols;
      public String HDFSPath;
      public String fieldsTerminatedBy;
      public boolean isReplace = true;

      public DBExportPara(NodeList paramNodeList)
      {
        for (int i = 0; i < paramNodeList.getLength(); i++)
        {
          String str1 = paramNodeList.item(i).getNodeName();
          String str2 = this.funcCal.calAll(paramNodeList.item(i).getTextContent().trim());
          if (str1.compareTo(this.KEY_SERVER) == 0)
            this.server = str2;
          else if (str1.compareTo(this.KEY_Type) == 0)
            this.type = str2;
          else if (str1.compareTo(this.KEY_Port) == 0)
            this.port = str2;
          else if (str1.compareTo(this.KEY_USERNAME) == 0)
            this.userName = str2;
          else if (str1.compareTo(this.KEY_PASSWORD) == 0)
            this.password = str2;
          else if (str1.compareTo(this.KEY_DATABASE) == 0)
            this.database = str2;
          else if (str1.compareTo(this.KEY_TABLE_NAME) == 0)
            this.tableName = str2;
          else if (str1.compareTo(this.KEY_SELECT_COLS) == 0)
            this.selectCols = str2;
          else if (str1.compareTo(this.KEY_HDFSPATH) == 0)
            this.HDFSPath = str2;
          else if (str1.compareTo(this.KET_FIELDS_TERMINATED_BY) == 0)
            this.fieldsTerminatedBy = str2;
          else if (str1.compareTo(this.KEY_IS_REPLACE) == 0)
            this.isReplace = Boolean.parseBoolean(str2);
          System.out.println(str1 + ":" + str2);
        }
      }

      public boolean checkPara()
      {
        return (this.server != null) && (this.server.length() > 0) && (this.type != null) && (this.type.length() > 0) && (this.port != null) && (this.port.length() > 0) && (this.userName != null) && (this.userName.length() > 0) && (this.password != null) && (this.password.length() > 0) && (this.database != null) && (this.database.length() > 0) && (this.tableName != null) && (this.tableName.length() > 0) && (this.selectCols != null) && (this.selectCols.length() > 0) && (this.HDFSPath != null) && (this.HDFSPath.length() > 0) && (this.fieldsTerminatedBy != null) && (this.fieldsTerminatedBy.length() > 0);
      }

      public void printPara()
      {
        String str = this.KEY_SERVER + this.server + this.KEY_Type + this.type + this.KEY_Port + this.port + this.KEY_USERNAME + this.userName + this.KEY_PASSWORD + this.password + this.KEY_DATABASE + this.database + this.KEY_TABLE_NAME + this.tableName + this.KEY_SELECT_COLS + this.selectCols + this.KEY_HDFSPATH + this.HDFSPath + this.KET_FIELDS_TERMINATED_BY + this.fieldsTerminatedBy + this.KEY_IS_REPLACE + this.isReplace;
        System.out.println(str);
      }

}