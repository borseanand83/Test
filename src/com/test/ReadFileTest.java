package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ReadFileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader reader = null;
		String searchString = "ERROR";
		long startTime = System.currentTimeMillis();
		try {
			
			System.out.println("start Time : "+new Date().toString());
			File file = new File("C:\\Workarea\\jboss-4.0.5.GA\\jboss-4.0.5.GA\\server\\default\\log\\server.log");
			double bytes = file.length();
			double kilobytes = (bytes / 1024);
			reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))),2);
 		    LineNumberReader lnr = new LineNumberReader(reader);
		    int linenumber = 0;
//            while (lnr.readLine() != null){
//	        	linenumber++;
//            }
            
			String line = reader.readLine();
			int fileCnt = 0;
	
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			while(line!=null){
				File outFile = new File("C:\\Users\\IBM_ADMIN\\Desktop\\bak files\\out"+fileCnt+".txt");
//				FileWriter fileWriter = new FileWriter(outFile);
				int count =0;
				String lineStr="";
				while(count<1000){
//					fileWriter.write(line+"\n");
					lineStr+=line+"\n";
					line = reader.readLine();
					count++;
				}
				Thread t1 = new Thread(new WriteFile(outFile,lineStr),"Thread"+fileCnt);
				t1.start();
				t1.join();
				Thread t2 = new Thread(new ReadFile(outFile,searchString),"Thread"+fileCnt);
				t2.start();

//				
//				fileWriter.close();
			
//				for (int i = 0; i < 100; i++) {
//					executorService.execute(new ReadFile(outFile,searchString));		
//				}
				

				fileCnt++;
			}
//			executorService.shutdown();
//	        while (!executorService.isTerminated()) {
//	        }
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("execution Time : "+(System.currentTimeMillis() - startTime) );
		
	}

}
