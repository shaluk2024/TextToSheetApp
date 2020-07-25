package nt.service;

import java.io.*;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Stack;


public class FileUtility {

    static int fileCount = 0;
    static int totalFiles = 0;
    static int excp = 0;

    public void iterateDirectoryContents(File dir) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    iterateDirectoryContents(file);
                } else {
                    totalFiles++;
                    readFile(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(File file) {
        String f_name = null;
        String line_just_read = new String();
        BufferedReader br = null;
        InputStreamReader isr = null;
        FileInputStream fis = null;

        DatabaseUtility db_util = new DatabaseUtility();

        try {
            if (file.isFile()) {
                f_name = file.getName();
                System.out.println("File is :" + f_name);

                if (file.getName().endsWith(".txt")) {

                    String fileString = Cleaner.readLineByLine(file);
                    File f = new File("AfterCommentRemover");
                    if (!f.exists() && !f.mkdirs()) throw new IOException("Could not create directory " + f);
                    String abc = f.getCanonicalPath() + "\\" + "myFile1.txt";
                    FileWriter fw = new FileWriter(abc);
                    fw.write(fileString);
                    fw.close();

                    File fileWithoutComment = new File(abc);
                    fis = new FileInputStream(fileWithoutComment.getAbsolutePath());
                    isr = new InputStreamReader(fis);
                    br = new BufferedReader(isr);
                    fileCount++;
                    String db = "";
                    String table = "";
                    String columns = "";
                    boolean table_start_flag = false;
                    Stack<String> bracketStack = new Stack<String>();

                    while ((line_just_read = br.readLine()) != null) {

                        line_just_read = line_just_read.trim();

                        if (!"".equals(line_just_read.trim())) {
                            //   System.out.println("line just read: " + line_just_read);
                            if (line_just_read.startsWith("USE")) {
                                db = line_just_read.substring(line_just_read.indexOf("["), (line_just_read.lastIndexOf("]") + 1));
                                System.out.println("db " + db);
                            }
                            if (line_just_read.startsWith("CREATE TABLE")) {
                                table_start_flag = true;
                                table = line_just_read.substring(line_just_read.indexOf("["), (line_just_read.lastIndexOf("]") + 1));
                                System.out.println("table " + table);
                            }
                            if (table_start_flag && line_just_read.contains("(")) {
                                bracketStack.push("(");
                            }
                            if (table_start_flag && line_just_read.contains("CONSTRAINT")) {
                                table_start_flag = false;
                                Connection con = db_util.getConnection();
                                db_util.recordExecutionStats(con, db, table, columns);
                                columns = "";
                                bracketStack.clear();
                            }
                            if (table_start_flag && line_just_read.contains(")")) {
                                bracketStack.pop();
                                if (bracketStack.empty()) {
                                    table_start_flag = false;
                                }
                            }
                            if (table_start_flag && !line_just_read.contains("CONSTRAINT") &&
                                    !line_just_read.startsWith("CREATE TABLE") && !bracketStack.empty()) {
                                String col = line_just_read.substring(line_just_read.indexOf("["),
                                        (line_just_read.indexOf(" ")));
                                columns = columns + col;
                                System.out.println("columns " + columns);
                            }
                        }
                    }
                    br.close();
                    isr.close();
                    fis.close();
                } else {
                    System.out.println("file not found");
                }
            }
        } catch (IOException e) {
            excp++;
            e.printStackTrace();
        }
    }
}
