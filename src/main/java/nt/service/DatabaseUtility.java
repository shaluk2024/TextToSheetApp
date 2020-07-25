package nt.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtility {

    Properties prop = null;
    FileInputStream inStream = null;
    DatabaseUtility() {

    }

    public Connection getConnection() {
        Connection connection = null;

        String url= "jdbc:h2:mem:testdb";
        String user ="sa";
        String pass = "password";
        String driverclass ="org.h2.Driver";
        try {
            Class.forName(driverclass);
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void createTable(Connection con){
        String query = "";
        Statement st = null;
        query = "CREATE TABLE EXCEL_UTIL_EXEC_STATS(\n" +
                "\tTXT_DB_NAME VARCHAR2(100 CHAR),\n" +
                "\tTXT_TABLE_NAME VARCHAR2(100 CHAR),\n" +
                "\tTXT_COLUMNS_NAME VARCHAR2(500 CHAR),\n" +
                "\tPRIMARY KEY (TXT_DB_NAME, TXT_TABLE_NAME)\n" +
                ");";
        try {
            st = con.createStatement();
            st.execute(query);
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public void recordExecutionStats(Connection connection, String db, String table, String cols) throws IOException {
        String query = "";
        PreparedStatement ps = null;

        try {
            System.out.println("Inside recordExecutionStats.............. ");
            query="Insert into EXCEL_UTIL_EXEC_STATS(TXT_DB_NAME,TXT_TABLE_NAME,TXT_COLUMNS_NAME ) values ( ? , ?, ?)";
            ps = connection.prepareStatement(query);

            ps.setString(1, db);
            ps.setString(2,table);
            ps.setString(3,cols);

            int i = ps.executeUpdate();
            System.out.println(i + " records inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
