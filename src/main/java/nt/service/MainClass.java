package nt.service;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public class MainClass {
    public static void main(String args[]){
        DatabaseUtility db = new DatabaseUtility();
        GenerateReport gen = new GenerateReport();
        Connection con = db.getConnection();
        FileUtility fileUtils = new FileUtility();
        db.createTable(con);
        String sourcesLocation = "/home/nt/code/Files"; //args[0];
        File root = new File(sourcesLocation);
        fileUtils.iterateDirectoryContents(root);
        try {
            gen.generateReport(con);
        }catch(IOException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }













    }
}
