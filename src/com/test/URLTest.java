package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			String tr = "T1 Range rr yyy zzz    ee";
			System.out.println(tr.replaceAll(" ", ""));
		URL url = new URL("http://abbpatil.in.ibm.com/cognos/realtime/dashboard/doreader.htm?refreshinterval=300&hideborder=javascript:alert(1)&do=\"CallCenter Demo\".\"Call Center Performance\".\"Dashboard Objects\".\"Employee Churn\"&sessionID=MTsxMDE6NzRjMGU2ZTItMWQ3NS05Mjc4LTk0MzctZjI4NjE1ZDVlMTUzOjI0ODYyNTEzNjg7MDszOzA7&ObiSessionId=MTsxMDE6NzRjMGU2ZTItMWQ3NS05Mjc4LTk0MzctZjI4NjE1ZDVlMTUzOjI0ODYyNTEzNjg7MDszOzA7");
//		String params = "javascript:alert(1)";
		HttpURLConnection connection;
		OutputStream output = null;

			connection = (HttpURLConnection)url.openConnection();
			
		
//		connection.setDoOutput(true); // Triggers POST.
//		connection.setRequestMethod("POST");
//		connection.setRequestProperty("test", params);
//		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		

//		     output = connection.getOutputStream();
//		     output.write(query.getBytes(charset));
		     InputStream response = connection.getInputStream();
		     BufferedReader in = new BufferedReader(new InputStreamReader(response));
		     String inputLine;
		     File f = new File("sample.html");
		     f.setWritable(true, false);
		     f.createNewFile();
		     BufferedWriter bw = null;
		     FileWriter fw = new FileWriter(f.getAbsoluteFile());
		 	bw = new BufferedWriter(fw);
		        while ((inputLine = in.readLine()) != null){
		        	System.out.println(inputLine);
					bw.write(inputLine);
		        } 
				bw.close(); 
		        in.close();
//		     System.out.println(response.read());
//		     System.out.println(response);
		     
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
//		     if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
		}
		
	}

}
