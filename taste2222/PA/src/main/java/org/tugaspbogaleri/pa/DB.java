package org.tugaspbogaleri.pa;

import java.sql.*;

public class DB {
    static Connection con;
    public static Connection connectDB() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbgaleri","root","");
            return con;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
}
