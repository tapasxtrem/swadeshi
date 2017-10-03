/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swadeshi;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author XPC17
 */
public class DBPool {

  //  public static String DATABASE_IP = "127.0.0.1";  //SF
      //public static String DATABASE_IP = "172.168.10.88\\sqlexpress"; //LOCAL
      // public static String DATABASE_IP = "
       //public static String DATABASE_IP = "MARGINEYE";//Azure
      public static String DATABASE_IP = "127.11.147.130";  //Openshift/swadeshi
    /**
     *PORT NO
     */
    //public static String DATABASE_PORT = "1433";
    public static String DATABASE_PORT = "3306";//openshift
   

    /**
     *DATABASE NAME
     */
        public static String DATABASE_NAME = "swadeshi"; //VENTURA data from swadeshiData.sql
        

    //public static String DATABASE_NAME = "swadeshi"; //VENTURA
   // public static String DATABASE_NAME = "notification";  //SF

    /**
     *USER NAME
     */
    //public static String DATABASE_USERNAME = "xtrem";  
   // public static String DATABASE_USERNAME = "xtrem"; //Azure,local
    // public static String DATABASE_USERNAME = "xtremsoft";  //Ventura
    public static String DATABASE_USERNAME = "adminIgWihrN";// opensiftBonBon;

    /**
     *PASSWORD FOR DATABASE
     */
    //public static String DATABASE_PWD = "xtrem";//local
   // public static String DATABASE_PWD = "susxtrem"; //SF
   //public static String DATABASE_PWD = "test1234";  //Azure
    public static String DATABASE_PWD = "fsEIgDedYmzS";// opensiftBonBon;
    /**
     *DATABASE CLASS NAME
     */
    //public static final String DATABASE_CLASSNAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String DATABASE_CLASSNAME = "com.mysql.jdbc.Driver";

    /**
     *DATABASE URL 
     */
    //public static final String DATABASE_URL = "jdbc:sqlserver://" + DATABASE_IP + ":1433;"  + "database=" + DATABASE_NAME;
    
    //public static final String DATABASE_URL = "jdbc:sqlserver://" + DATABASE_IP + ":1433;"  + "database=" + DATABASE_NAME;
    public static final String DATABASE_URL = "jdbc:mysql://" + DATABASE_IP + ":" + DATABASE_PORT + "/" + DATABASE_NAME;
    
    /**
     * Log
     */
    private static ArrayList<String> log = new ArrayList(1024);

//no of shared Connections
    static {
        try {
            Class.forName(DATABASE_CLASSNAME);
            log = new ArrayList(1024);
            
        } catch (Exception ex) {
         System.out.println(ex.toString());

        }
    }

    /**
     *
     * @return
     */
   public static Connection get() {
        try {
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PWD);
        } catch (SQLException ex) {
            System.out.println("Cannot get connection");
            System.out.println(ex.toString());
            log(ex);
        }
        return get();
    }

   
    /**
     *
     * @param s
     */
    public static void log(String s){
        log.add(s);
    }
    
    /**
     *
     * @param s
     */
    public static void log(Exception s){
        log.add((s.toString() + "," + Arrays.toString(s.getStackTrace())).replaceAll(",","<br />"));
    
    }
    
    /**
     *
     * @param out
     * @throws IOException
     */
    public static void printLog(Writer out) throws IOException{
        for(String s:log) {
            out.write(s+"<br />");
        }
        log.clear();
    }

    public static void close(Connection con, Statement st) {
    try { if (st != null) {
        st.close();
      }
      close(con);
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void close(Connection con, PreparedStatement st)
  {
    try {
      if (st != null) {
        st.close();
      }
      close(con);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void close(Connection con, PreparedStatement st, ResultSet rs) {
    try {
      if (rs != null) {
        rs.close();
      }
      close(con, st);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void close(Connection con, Statement st, ResultSet rs) {
    try {
      if (rs != null) {
        rs.close();
      }
      close(con, st);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void close(Connection con, CallableStatement st)
  {
    try {
      if (st != null) {
        st.close();
      }
      close(con);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void close(Connection con, CallableStatement st, ResultSet rs) {
    try {
      if (rs != null) {
        rs.close();
      }
      close(con, st);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void close(Connection con) {
    try {
      if (con != null) {
        con.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
}
    
