package com.ucd.oursql.sql.storage.Storage;


import com.ucd.oursql.sql.execution.DMLTool;
import com.ucd.oursql.sql.table.ColumnDescriptorList;
import com.ucd.oursql.sql.table.type.SqlType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.ucd.oursql.sql.table.BTree.BPlusTree;
import com.ucd.oursql.sql.table.BTree.CglibBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TreeLoader {

    public TreeLoader() {

    }

    public ArrayList<BPlusTree> loadAllFile() throws JDOMException, IOException, ClassNotFoundException {
        ArrayList<BPlusTree> resultList = new ArrayList<>();
        File file = new File("data/");
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                //System.out.println("文件夹:" + file2.getPath().substring(4));
                File datafile = new File(file2.getPath()+ storage.Storage.XMLUtils.getFileName(file2.getPath())+".txt");
                String filepath = file2.getPath()+ storage.Storage.XMLUtils.getFileName(file2.getPath())+".xml";
                System.out.println("filepath is : "+filepath);
//                resultList.add(loadFromFile(filepath));
            } else {

            }
        }
        return resultList;
    }

    public BPlusTree loadFromFile(String tableName, HashMap<String,String> pm, ColumnDescriptorList columnDescriptorList, String userName) throws JDOMException, IOException, ClassNotFoundException {
        BPlusTree resultTree = new BPlusTree<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        //你也可以将demo.xml放在resources目录下，然后通过下面方式获取
//        InputStream resourceAsStream = JDOMParseXml.class.getClassLoader().getResourceAsStream("demo.xml");
//        Document document1 = saxBuilder.build(new File("data/"+tableName+"/"+tableName+".xml"));
        String filepath = "data/"+userName+"/"+tableName+"/"+tableName+".xml";
        Document document1 = saxBuilder.build(new File(filepath));
        Element rootElement = document1.getRootElement();


        List<Element> elementList = rootElement.getChildren();
        for (Element element : elementList) {
            //获取所有的子节点
            List<Element> innerElementList = element.getChildren();
            //建立一个哈希map
//            HashMap propertyMap = new HashMap();
            HashMap valueMap = new HashMap();
            for (Element innerElement : innerElementList) {
                String varName = innerElement.getName();
                String varVal = innerElement.getValue();
                System.out.println("the value of varVal is: "+varVal);
                valueMap.put(varName,varVal);
//                propertyMap.put(varName, Class.forName(varVal));

            }
            //property map已经处理完毕

//            Iterator it=propertyMap.keySet().iterator();
//            while(it.hasNext()){
//                String k= (String) it.next();
//                System.out.println(k+":"+propertyMap.get(k));
//            }
            HashMap property=DMLTool.convertPropertyMap(pm);

            CglibBean b = new CglibBean(property);

            Iterator iter = valueMap.keySet().iterator();
            while (iter.hasNext()) {
                String valueName = (String) iter.next();
                //==================================================
                //??????????????????????????????????????????????????
                //??????????????????????????????????????????????????
                try {
                    SqlType v = DMLTool.forXMLConvertStringToValue(valueName,valueMap.get(valueName).toString(),pm,columnDescriptorList);
//                    System.out.println(v+"====");
                    b.setValue(valueName, v);
                }catch (Exception e){
                    e.printStackTrace();
                }

                }
            resultTree.insert(b, (Comparable) b.getValue("primary_key"));



            }
        return resultTree;
        }


    }
