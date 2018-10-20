package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CopyFile {

	public static void copyFilesToSingleFile(String folderPath) throws IOException{
		File dir = new File(folderPath);
		if(!dir.isDirectory())
			throw new IllegalArgumentException("the path is invalid");
		File newCSVFile = new File("C:/Workarea_Watson/DBS/Outlookassignment/datasets/completeFile/dataCollection_Aug07.json");
		if(!newCSVFile.exists())
		{
			newCSVFile.createNewFile();
		}
		BufferedReader bufferRead = null;
		BufferedWriter bufferWriter = null;
		try {
			bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newCSVFile))));
			for (File file : dir.listFiles()) {
				System.out.println("file name "+file.getName());

				bufferRead = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file))));
				String line= null;

				while ((line = bufferRead.readLine()) != null)   {
					//					System.out.println(line);
					bufferWriter.write(line);
					bufferWriter.newLine();
					//					FileWriter fw = new FileWriter(newCSVFile, true);
					//					fw.write(line);
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated cOtch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			bufferRead.close();
			bufferWriter.close();
		}
	}

	public static void findTextFile(String folderPath,String text) throws IOException{
		File dir = new File(folderPath);
		if(!dir.isDirectory())
			throw new IllegalArgumentException("the path is invalid");
		File newCSVFile = new File("C:/Workarea_Watson/DBS/Outlookassignment/datasets/completeFile/dataCollection_Aug07.json");
		if(!newCSVFile.exists())
		{
			newCSVFile.createNewFile();
		}
		BufferedReader bufferRead = null;
		try {
			for (File file : dir.listFiles()) {
				System.out.println("file name "+file.getName());

				bufferRead = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file))));
				String line= null;

				while ((line = bufferRead.readLine()) != null)   {
					//					System.out.println(line);
					if(line.contains(text)){
						System.out.println(line);
					}
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			bufferRead.close();
		}
	}
	
	
	public static void main(String[] args) {
		try {
			copyFilesToSingleFile("C:\\Workarea_Watson\\DBS\\Outlookassignment\\datasets\\dataCollection_Aug07\\");
//			findTextFile("C:\\Workarea_Watson\\DBS\\Outlookassignment\\datasets\\dataCollection_Aug07\\","Spain");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
