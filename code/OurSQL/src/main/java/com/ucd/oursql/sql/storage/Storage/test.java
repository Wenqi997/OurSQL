package com.ucd.oursql.sql.storage.Storage;

import com.ucd.oursql.sql.execution.ExecuteStatement;
import com.ucd.oursql.sql.storage.Configuration.Configuration;
import com.ucd.oursql.sql.table.BTree.BPlusTree;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.ucd.oursql.sql.driver.OurSqlResultset;
import com.ucd.oursql.sql.storage.Storage.descriptorLoader;

public class test {
    public static void main(String[] args){
        descriptorLoader descriptorLoader = new descriptorLoader();
        HashMap propertyMap = descriptorLoader.loadPropertyFromFile("tdate", ExecuteStatement.user.getUserName());
        OurSqlResultset resultset = new OurSqlResultset(null,propertyMap);
        System.out.println(resultset.getDataStructure("td","com.ucd.oursql.sql.table.type.date.SqlDate"));
        System.out.println("the name after convert is: "+resultset.convertStructureName("com.ucd.oursql.sql.table.type.date.SqlDate"));
//        resultset.MethodInvoke("Date");
    }
}
