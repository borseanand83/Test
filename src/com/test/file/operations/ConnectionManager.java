package com.test.file.operations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	
	private static Connection conn = null;
	private ConnectionManager(){
		
	}

	public static Connection newInstance() throws ClassNotFoundException, SQLException{
		if(conn==null){
			synchronized (Connection.class) {
				if(conn==null){
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");			
				}
			}
		}
		
		return conn;
	}
}
