package nt.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class GenerateReport {

    Properties prop = null;
    FileInputStream inStream = null;
    GenerateReport(){}

    public void generateReport(Connection con) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet summarySpreadsheet = workbook.createSheet("Summary");

        PreparedStatement pStatement = null;
        ResultSet resultSet = null;

        String query ="SELECT TXT_DB_NAME, TXT_TABLE_NAME, TXT_COLUMNS_NAME FROM EXCEL_UTIL_EXEC_STATS";

        pStatement = con.prepareStatement(query);
        resultSet = pStatement.executeQuery();

        XSSFRow summaryRow = summarySpreadsheet.createRow(1);
        XSSFCell summaryCell;
        summaryCell = summaryRow.createCell(1);
        summaryCell.setCellValue("Database Name");
        summaryCell = summaryRow.createCell(2);
        summaryCell.setCellValue("Table Name ");
        summaryCell = summaryRow.createCell(3);
        summaryCell.setCellValue("Columns Name");
        int j=2;


        while (resultSet.next()) {
            summaryRow = summarySpreadsheet.createRow(j);
            summaryCell = summaryRow.createCell(1);
            summaryCell.setCellValue(resultSet.getString("TXT_DB_NAME"));
            summaryCell = summaryRow.createCell(2);
            summaryCell.setCellValue(resultSet.getString("TXT_TABLE_NAME"));
            summaryCell = summaryRow.createCell(3);
            summaryCell.setCellValue(resultSet.getString("TXT_COLUMNS_NAME"));
            j++;
        }

        FileOutputStream out = new FileOutputStream(new File("_Report.xlsx"));
        workbook.write(out);
        out.close();
        workbook.close();
        pStatement.close();
        resultSet.close();
        System.out.println("Report File : _Report.xlsx\""+" written successfully");


    }
}
