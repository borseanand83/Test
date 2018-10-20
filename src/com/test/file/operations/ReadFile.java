package com.test.file.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReadFile {

	public void readFile(File file) throws IOException, ClassNotFoundException, SQLException, InterruptedException{
		Connection conn = ConnectionManager.newInstance();
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO TEST.BRANCH (BRANCHCD, TERM_ID,FINAL_BALANCE,Weekday,Location,Make,LOAD_TYPE,CIRCLE,Recommended_Refill_Total,BRNAME) VALUES (?,?,?,?,?,?,?,?,?,?)");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			ExecutorService executor = Executors.newFixedThreadPool(10);
			StringTokenizer tokenizer = null;
			String line = reader.readLine();
			line = reader.readLine();
			while (line!=null) {
				executor.execute(new WorkerThread(line, stmt));
				line = reader.readLine();
			}
		executor.shutdown();
		while(true){
			executor.awaitTermination(300, TimeUnit.MILLISECONDS);
		}
	}

	class WorkerThread implements Runnable{

		String line = null;
		PreparedStatement stmt = null;
		public WorkerThread(String line,PreparedStatement stmt) {
			this.line = line;
			this.stmt = stmt;
		}
		@Override
		public void run() {
			try {
				if(line!=null){
					StringTokenizer tokenizer = new StringTokenizer(line, ",");
					Integer branchCd = new Integer(tokenizer.nextToken());  
					String termId = tokenizer.nextToken();
					Integer finalBalance = new Integer(tokenizer.nextToken());
					String weekday = tokenizer.nextToken();
					String location = tokenizer.nextToken();
					String make = tokenizer.nextToken();
					String loadType = tokenizer.nextToken();
					String circle = tokenizer.nextToken();
					Double recommended_Refill_Total = new Double(tokenizer.nextToken());
					String brName = tokenizer.nextToken();
					System.out.println("query :: "+stmt.toString());
					stmt.setInt(1, branchCd);
					stmt.setString(2, termId);
					stmt.setInt(3, finalBalance);
					stmt.setString(4, weekday);
					stmt.setString(5, location);
					stmt.setString(6, make);
					stmt.setString(7, loadType);
					stmt.setString(8, circle);
					stmt.setDouble(9, recommended_Refill_Total);
					stmt.setString(10, brName);
					stmt.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}
	
	public static void main(String[] args) {
		ReadFile rf = new ReadFile();
		File file = new File("C:\\Anand\\testData.csv");
		try {
			rf.readFile(file);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
