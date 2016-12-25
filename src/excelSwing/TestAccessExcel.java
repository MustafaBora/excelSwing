package excelSwing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestAccessExcel {
  public static Connection getConnection() throws Exception {
    String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
    String url = "jdbc:odbc:excelDB";
    String username = "";
    String password = "";
    Class.forName(driver);
    return DriverManager.getConnection(url, username, password);
  }

  public static void main(String args[]) {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.createStatement();
//      String excelQuery = "select * from [Sheet1$]";
      String excelQuery = "CREATE TABLE `sheetname` (`DartNumber` VarChar (16), `ClientId` VarChar (10), `Population` VarChar (30))";
      rs = stmt.executeQuery(excelQuery);

      while (rs.next()) {
    	  System.out.println(rs.getString("DartNumber") + " " + rs.getString("ClientId") + " " + rs.getString("Population"));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        rs.close();
        stmt.close();
        conn.close();

      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}