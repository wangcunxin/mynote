package cn.com.wind.demo.sqoop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlFileLoader {
     private final String DB_IMPORT = "Wind.Galaxy.Import.DBImport";
      private final String DB_EXPORT = "Wind.Galaxy.Export.DBExport";
      private final String FILE_IMPORT = "Wind.Galaxy.Import.FileImport";
      private final String FILE_EXPORT = "Wind.Galaxy.Export.FileExport";

      public List loadImportList(String paramString)
      {
        try
        {
          ArrayList localArrayList = new ArrayList();
          DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
          Document localDocument = localDocumentBuilder.parse(paramString);
          NodeList localNodeList1 = localDocument.getChildNodes();
          for (int i = 0; i < localNodeList1.getLength(); i++)
          {
            Node localNode1 = localNodeList1.item(i);
            NodeList localNodeList2 = localNode1.getChildNodes();
            for (int j = 0; j < localNodeList2.getLength(); j++)
            {
              Node localNode2 = localNodeList2.item(j);
              NodeList localNodeList3;
              if (localNode2.getNodeName().compareTo("Wind.Galaxy.Import.DBImport") == 0)
              {
                localNodeList3 = localNode2.getChildNodes();
                try
                {
                  localArrayList.add(new DBImportPara(localNodeList3));
                }
                catch (Exception localException1)
                {
                  System.out.println("Import para Error!");
                  return null;
                }
              }
              else if (localNode2.getNodeName().compareTo("Wind.Galaxy.Import.FileImport") == 0)
              {
                localNodeList3 = localNode2.getChildNodes();
                try
                {
                  localArrayList.add(new FileImportPara(localNodeList3));
                }
                catch (Exception localException2)
                {
                  System.out.println("Import para Error!");
                  return null;
                }
              }
            }
          }
          System.out.println("解析完毕");
          return localArrayList;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          System.out.println(localFileNotFoundException.getMessage());
        }
        catch (ParserConfigurationException localParserConfigurationException)
        {
          System.out.println(localParserConfigurationException.getMessage());
        }
        catch (SAXException localSAXException)
        {
          System.out.println(localSAXException.getMessage());
        }
        catch (IOException localIOException)
        {
          System.out.println(localIOException.getMessage());
        }
        return null;
      }

      public List loadExportList(String paramString)
      {
        try
        {
          ArrayList localArrayList = new ArrayList();
          DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
          Document localDocument = localDocumentBuilder.parse(paramString);
          NodeList localNodeList1 = localDocument.getChildNodes();
          for (int i = 0; i < localNodeList1.getLength(); i++)
          {
            Node localNode1 = localNodeList1.item(i);
            NodeList localNodeList2 = localNode1.getChildNodes();
            for (int j = 0; j < localNodeList2.getLength(); j++)
            {
              Node localNode2 = localNodeList2.item(j);
              NodeList localNodeList3;
              if (localNode2.getNodeName().compareTo("Wind.Galaxy.Export.DBExport") == 0)
              {
                localNodeList3 = localNode2.getChildNodes();
                localArrayList.add(new DBExportPara(localNodeList3));
              }
              else if (localNode2.getNodeName().compareTo("Wind.Galaxy.Export.FileExport") == 0)
              {
                localNodeList3 = localNode2.getChildNodes();
                localArrayList.add(new FileExportPara(localNodeList3));
              }
            }
          }
          System.out.println("解析完毕");
          return localArrayList;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          System.out.println(localFileNotFoundException.getMessage());
        }
        catch (ParserConfigurationException localParserConfigurationException)
        {
          System.out.println(localParserConfigurationException.getMessage());
        }
        catch (SAXException localSAXException)
        {
          System.out.println(localSAXException.getMessage());
        }
        catch (IOException localIOException)
        {
          System.out.println(localIOException.getMessage());
        }
        return null;
      }
}