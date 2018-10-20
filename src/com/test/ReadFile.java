package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFile implements Runnable{

	String searchString;
	File file;

	public ReadFile(File file,String searchString) {
		this.file = file;
		this.searchString = searchString;
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		try {
			if(file == null || file.length() ==0 )
				file.wait();
			synchronized (file) {
				System.out.println("In Read thread  :: "+Thread.currentThread().getName());
				reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
				String line = reader.readLine();
				boolean found = false;
				while(line!=null){
					if(line.contains(searchString)){
						found = true;
						System.out.println("line with SearchString : "+line);
					}
					line = reader.readLine();
				}
				if(!found)
					System.out.println("String not found");
				reader.close();
				if(file!=null && file.length()!=0)
					file.notify();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
