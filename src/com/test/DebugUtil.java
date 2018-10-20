package com.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class DebugUtil {

	public static void findEntityInSentimentResult(String entityNameInQuestion,String csvResultFilePath) throws IOException{
		File resultFile = new File(csvResultFilePath);
		BufferedReader bufferRead = null;
		bufferRead = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(resultFile))));
		String line = bufferRead.readLine();
		line = bufferRead.readLine();
		
		while (line != null)   {
			String[] dataArray = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			line = line.replaceAll("\n", " ");
//			StringTokenizer tokenizer = new StringTokenizer(line, ",");
//			line.replaceAll("\".,.\""," ");
			/*if (tokenizer.countTokens() != 30) {
				line = bufferRead.readLine();
				System.out.println("LINE not matched------>"+line);
				continue;
			}*/
			System.out.println("LINE not matched------>"+line);
			String document =dataArray[0];
			String label = dataArray[1];
			String id = dataArray[2];
			String pattern = dataArray[3];
			String patternCode =dataArray[4];
			String target = dataArray[5];
			String targetHead = dataArray[6];	
			String targetHeadNF =dataArray[7];
			String strippedTargetNF =dataArray[8];
			String sentimentClue  =dataArray[9];
			String sentimentSupportingClue = dataArray[10];
			String targetProvenance = dataArray[11];
			String temporalTense= dataArray[12];
			String temporalMood	= dataArray[13];
			
			String sentence	= dataArray[14];
			
			String paragraph =dataArray[15];
			String sentimentPolarity = dataArray[16];
			String entityName = dataArray[17];
			String entityMatch = dataArray[18];
			String entityType = dataArray[19];
			String aspectMatch	= dataArray[20];
			String aspectName = dataArray[21];
			String aspectType = dataArray[22];
			String sentimentCategory = dataArray[23];
			String documentType	= dataArray[24];
			String documentSource = dataArray[25];
			String sourceFilename = dataArray[26];
			String pubDate = dataArray[27];
			String documentTitle = dataArray[28];
			String originalEntityName = dataArray[29];

			if(entityNameInQuestion.equals(entityName) || entityNameInQuestion.equals(entityMatch)){
				System.out.println(line);
			}
			line = bufferRead.readLine();
		}
	}

	public static void normalizeToken(String token){
		if(token.equals(",")){
			
		}
	}
	
public static void main(String[] args) {
	try {
		findEntityInSentimentResult("China", "C:\\Workarea_Watson\\DBS\\Outlookassignment\\186Test\\SentimentForEntity_Dedup results\\SentimentDeployment.SentimentForEntity_Dedup_Aug07.csv");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
