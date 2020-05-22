package com.ucd.oursql.sql.driver;

import com.ucd.oursql.sql.table.BTree.CglibBean;
import com.ucd.oursql.sql.table.type.SqlType;
import com.ucd.oursql.sql.table.type.number.SqlBigInt;
import com.ucd.oursql.sql.table.type.text.SqlVarChar;
import org.apache.tomcat.util.bcel.classfile.ClassFormatException;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.Year;
import java.util.*;

import com.ucd.oursql.sql.storage.Storage.UnCorrectDataStructureException;

import static com.ucd.oursql.sql.execution.DMLTool.removeQutationMark;

public class OurSqlResultset implements ResultSet {
    List<CglibBean> datas;
    HashMap propertyMap;

    public OurSqlResultset(List<CglibBean> datas, HashMap propertyMap) {
        this.datas = datas;
        this.propertyMap = propertyMap;
    }

//    public OurSqlResultset() {
//    }

    private int next = -1;

    @Override
    public boolean next() throws SQLException {
        if(next < datas.size()-1){
            next++;
            return true;
        }else {
            return false;
        }
    }

    public String[][] getFormDatas(){
        String[][] result = new String[datas.size()+1][propertyMap.size()];
        int rowPosition = 0;
        int columnPosition = 0;
        List<String> ColumnNameList = new ArrayList<String>();
        for(Object key : propertyMap.keySet()){
            String keyValue = (String)key;
            ColumnNameList.add(keyValue);
            result[rowPosition][columnPosition] = keyValue;
            columnPosition++;
        }
        columnPosition = 0;
        rowPosition++;
        while (rowPosition < datas.size()+1){
            CglibBean cglibBean = (CglibBean) datas.get(rowPosition - 1);
            for (String name : ColumnNameList) {
                Object value = cglibBean.getValue(name);
                if(value!=null){
                    result[rowPosition][columnPosition] = value.toString();
                }
                columnPosition++;
//                    String value = cglibBean.getValue(name).toString();
//                    System.out.println("the value is: "+value+" name: "+name);
//                    resultMap.put(name, value);
            }
            columnPosition = 0;
            rowPosition++;
        }
        return result;
    }

    public String convertStructureName(String name){
        return name.substring(38);
    }

//    public void MethodInvoke(String SubName){
//        String MethodName = "get"+SubName;
//        try {
//            this.getClass().getMethod(MethodName,new Class[]{String.class,String.class}).invoke(new OurSqlResultset(),new Object[]{"td","com.ucd.oursql.sql.table.type.date.SqlDate"});
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    public boolean getDataStructure(String columnName,String dataStructureRequired){
        String dataStructureName = (String)propertyMap.get(columnName);
        return dataStructureName.equals(dataStructureRequired);
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public boolean wasNull() throws SQLException {
        return false;
    }

    @Override
    public String getString(int i) throws SQLException {
        return null;
    }

    @Override
    public boolean getBoolean(int i) throws SQLException {
        return false;
    }

    @Override
    public byte getByte(int i) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(int i) throws SQLException {
        return 0;
    }

    @Override
    public int getInt(int i) throws SQLException {
        return 0;
    }

    @Override
    public long getLong(int i) throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(int i) throws SQLException {
        return 0;
    }

    @Override
    public double getDouble(int i) throws SQLException {
        return 0;
    }

    @Override
    public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
        return null;
    }

    @Override
    public byte[] getBytes(int i) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(int i) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int i) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int i) throws SQLException {
        return null;
    }

    @Override
    public InputStream getAsciiStream(int i) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(int i) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(int i) throws SQLException {
        return null;
    }

    @Override
    public String getString(String s) throws SQLException {
//        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.text.SqlString")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return value;
//        }
//        else {
//            System.out.println("Uncorrect Data Structure");
//            throw new UnCorrectDataStructureException();
//        }

    }

    @Override
    public boolean getBoolean(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.number.SqlBoolean")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return Boolean.parseBoolean(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    public char getChar(String s) throws SQLException{
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.text.SqlChar")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return value.toCharArray()[0];
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    public long getBigInt(String s) throws SQLException{
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.number.SqlBigInt")){
            CglibBean currentBean = datas.get(next);
            SqlBigInt columnData = (SqlBigInt) currentBean.getValue("s");
            String value = currentBean.getValue(s).toString();
            return columnData.getData();
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }



    @Override
    public byte getByte(String s) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(String s) throws SQLException {
        return 0;
    }

    @Override
    public int getInt(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.number.SqlInt")){
//            System.out.println("next:==============="+next);
//            System.out.println(datas==null);
            System.out.println("next is :"+next);
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return Integer.valueOf(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public long getLong(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.number.SqlBigInt")){
            CglibBean currentBean = datas.get(next);
            SqlBigInt columnData = (SqlBigInt) currentBean.getValue("s");
            String value = currentBean.getValue(s).toString();
            return Long.valueOf(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public float getFloat(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.number.SqlFloat")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return Float.valueOf(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    public Year getYear(String s) throws SQLException{
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.date.SqlYear")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            value=removeQutationMark(value);
            Year resultYear = Year.parse(value);
//            return Year.parse(value.toCharArray());
            return resultYear;
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    public String getVarChar(String s) throws SQLException{
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.text.SqlVarChar")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return String.valueOf(s);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public double getDouble(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.number.SqlDouble")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return Double.valueOf(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public BigDecimal getBigDecimal(String s, int i) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.number.SqlDecimal")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            return new BigDecimal(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public byte[] getBytes(String s) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.date.SqlDate")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            value=removeQutationMark(value);
            return Date.valueOf(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public Time getTime(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.date.SqlTime")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            value=removeQutationMark(value);
            return Time.valueOf(value);
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public Timestamp getTimestamp(String s) throws SQLException {
        if(getDataStructure(s,"com.ucd.oursql.sql.table.type.date.SqlTimeStamp")){
            CglibBean currentBean = datas.get(next);
            String value = currentBean.getValue(s).toString();
            value=removeQutationMark(value);
            Timestamp resultTimestamp = Timestamp.valueOf(value);
            return resultTimestamp;
        }
        else {
            System.out.println("Uncorrect Data Structure");
            throw new UnCorrectDataStructureException();
        }
    }

    @Override
    public InputStream getAsciiStream(String s) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(String s) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(String s) throws SQLException {
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public String getCursorName() throws SQLException {
        return null;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public Object getObject(int i) throws SQLException {
        String[][] result = new String[datas.size()+1][propertyMap.size()];
        int rowPosition = 0;
        int columnPosition = 0;
        List<String> ColumnNameList = new ArrayList<String>();
        for(Object key : propertyMap.keySet()){
            String keyValue = (String)key;
            ColumnNameList.add(keyValue);
            result[rowPosition][columnPosition] = keyValue;
            columnPosition++;
        }
        columnPosition = 0;
        rowPosition++;
        while (rowPosition < datas.size()+1){
            CglibBean cglibBean = (CglibBean) datas.get(rowPosition - 1);
            for (String name : ColumnNameList) {
                Object value = cglibBean.getValue(name);
                if(value!=null){
                    result[rowPosition][columnPosition] = value.toString();
                }
                columnPosition++;
            }
            columnPosition = 0;
            rowPosition++;
        }
        return result;
    }

    @Override
    public Object getObject(String s) throws SQLException {
        return null;
    }

    @Override
    public int findColumn(String s) throws SQLException {
        return 0;
    }

    @Override
    public Reader getCharacterStream(int i) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(String s) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int i) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String s) throws SQLException {
        return null;
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return (next < 0);
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return (next > datas.size());
    }

    @Override
    public boolean isFirst() throws SQLException {
        return (next == 0);
    }

    @Override
    public boolean isLast() throws SQLException {
        return (next == datas.size());
    }

    @Override
    public void beforeFirst() throws SQLException {

    }

    @Override
    public void afterLast() throws SQLException {

    }

    @Override
    public boolean first() throws SQLException {
        return false;
    }

    @Override
    public boolean last() throws SQLException {
        try{
            next = datas.size();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public int getRow() throws SQLException {
        return next;
    }

    @Override
    public boolean absolute(int i) throws SQLException {
        return false;
    }

    @Override
    public boolean relative(int i) throws SQLException {
        return false;
    }

    @Override
    public boolean previous() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int i) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int i) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getType() throws SQLException {
        return 0;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    @Override
    public void updateNull(int i) throws SQLException {

    }

    @Override
    public void updateBoolean(int i, boolean b) throws SQLException {

    }

    @Override
    public void updateByte(int i, byte b) throws SQLException {

    }

    @Override
    public void updateShort(int i, short i1) throws SQLException {

    }

    @Override
    public void updateInt(int i, int i1) throws SQLException {

    }

    @Override
    public void updateLong(int i, long l) throws SQLException {

    }

    @Override
    public void updateFloat(int i, float v) throws SQLException {

    }

    @Override
    public void updateDouble(int i, double v) throws SQLException {

    }

    @Override
    public void updateBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {

    }

    @Override
    public void updateString(int i, String s) throws SQLException {

    }

    @Override
    public void updateBytes(int i, byte[] bytes) throws SQLException {

    }

    @Override
    public void updateDate(int i, Date date) throws SQLException {

    }

    @Override
    public void updateTime(int i, Time time) throws SQLException {

    }

    @Override
    public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {

    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {

    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {

    }

    @Override
    public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException {

    }

    @Override
    public void updateObject(int i, Object o, int i1) throws SQLException {

    }

    @Override
    public void updateObject(int i, Object o) throws SQLException {

    }

    @Override
    public void updateNull(String s) throws SQLException {

    }

    @Override
    public void updateBoolean(String s, boolean b) throws SQLException {

    }

    @Override
    public void updateByte(String s, byte b) throws SQLException {

    }

    @Override
    public void updateShort(String s, short i) throws SQLException {

    }

    @Override
    public void updateInt(String s, int i) throws SQLException {

    }

    @Override
    public void updateLong(String s, long l) throws SQLException {

    }

    @Override
    public void updateFloat(String s, float v) throws SQLException {

    }

    @Override
    public void updateDouble(String s, double v) throws SQLException {

    }

    @Override
    public void updateBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {

    }

    @Override
    public void updateString(String s, String s1) throws SQLException {

    }

    @Override
    public void updateBytes(String s, byte[] bytes) throws SQLException {

    }

    @Override
    public void updateDate(String s, Date date) throws SQLException {

    }

    @Override
    public void updateTime(String s, Time time) throws SQLException {

    }

    @Override
    public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {

    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream, int i) throws SQLException {

    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream, int i) throws SQLException {

    }

    @Override
    public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {

    }

    @Override
    public void updateObject(String s, Object o, int i) throws SQLException {

    }

    @Override
    public void updateObject(String s, Object o) throws SQLException {

    }

    @Override
    public void insertRow() throws SQLException {

    }

    @Override
    public void updateRow() throws SQLException {

    }

    @Override
    public void deleteRow() throws SQLException {

    }

    @Override
    public void refreshRow() throws SQLException {

    }

    @Override
    public void cancelRowUpdates() throws SQLException {

    }

    @Override
    public void moveToInsertRow() throws SQLException {

    }

    @Override
    public void moveToCurrentRow() throws SQLException {

    }

    @Override
    public Statement getStatement() throws SQLException {
        return null;
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(int i) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(int i) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(String s) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(String s) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(String s) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(String s) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(int i, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(String s, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int i, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(String s, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(int i) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(String s) throws SQLException {
        return null;
    }

    @Override
    public void updateRef(int i, Ref ref) throws SQLException {

    }

    @Override
    public void updateRef(String s, Ref ref) throws SQLException {

    }

    @Override
    public void updateBlob(int i, Blob blob) throws SQLException {

    }

    @Override
    public void updateBlob(String s, Blob blob) throws SQLException {

    }

    @Override
    public void updateClob(int i, Clob clob) throws SQLException {

    }

    @Override
    public void updateClob(String s, Clob clob) throws SQLException {

    }

    @Override
    public void updateArray(int i, Array array) throws SQLException {

    }

    @Override
    public void updateArray(String s, Array array) throws SQLException {

    }

    @Override
    public RowId getRowId(int i) throws SQLException {
        return null;
    }

    @Override
    public RowId getRowId(String s) throws SQLException {
        return null;
    }

    @Override
    public void updateRowId(int i, RowId rowId) throws SQLException {

    }

    @Override
    public void updateRowId(String s, RowId rowId) throws SQLException {

    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void updateNString(int i, String s) throws SQLException {

    }

    @Override
    public void updateNString(String s, String s1) throws SQLException {

    }

    @Override
    public void updateNClob(int i, NClob nClob) throws SQLException {

    }

    @Override
    public void updateNClob(String s, NClob nClob) throws SQLException {

    }

    @Override
    public NClob getNClob(int i) throws SQLException {
        return null;
    }

    @Override
    public NClob getNClob(String s) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(int i) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(String s) throws SQLException {
        return null;
    }

    @Override
    public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {

    }

    @Override
    public void updateSQLXML(String s, SQLXML sqlxml) throws SQLException {

    }

    @Override
    public String getNString(int i) throws SQLException {
        return null;
    }

    @Override
    public String getNString(String s) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(int i) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(String s) throws SQLException {
        return null;
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNCharacterStream(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateCharacterStream(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateBlob(int i, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateBlob(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void updateClob(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateClob(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNClob(int i, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNClob(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void updateNCharacterStream(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateNCharacterStream(String s, Reader reader) throws SQLException {

    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateCharacterStream(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateCharacterStream(String s, Reader reader) throws SQLException {

    }

    @Override
    public void updateBlob(int i, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateBlob(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void updateClob(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateClob(String s, Reader reader) throws SQLException {

    }

    @Override
    public void updateNClob(int i, Reader reader) throws SQLException {

    }

    @Override
    public void updateNClob(String s, Reader reader) throws SQLException {

    }

    @Override
    public <T> T getObject(int i, Class<T> aClass) throws SQLException {
        return null;
    }

    @Override
    public <T> T getObject(String s, Class<T> aClass) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;
    }
}
