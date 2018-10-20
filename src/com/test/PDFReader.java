package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFReader {

	public static void main(String[] args){
		 PDDocument pd;
		 BufferedWriter wr;
		 try {
		         File input = new File("C:\\Workarea_Watson\\DBS\\corpus\\1_HK_EQUITY\\2014\\1_HK_EQUITY__0701.pdf");  // The PDF file from where you would like to extract
		         File output = new File("C:\\Workarea_Watson\\DBS\\SampleText.txt"); // The text file where you are going to store the extracted data
		         pd = PDDocument.load(input);
		         PDPage page = (PDPage) pd.getDocumentCatalog().getAllPages().get(0);
		         PDResources resources = page.getResources();
		         System.out.println("*************"+resources.getXObjects());
//		         System.out.println(pd.getNumberOfPages()+" "+pd.getDocument().getTrailer());
		         
		         System.out.println(pd.isEncrypted());
		         pd.save("CopyOfInvoice.pdf"); // Creates a copy called "CopyOfInvoice.pdf"
		         PDFTextStripper stripper = new PDFTextStripper();
		         String txt = stripper.getText(pd);
		         System.out.println(page.getContents().getInputStreamAsString());

		         wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
		         wr.write(page.getContents().getInputStreamAsString());
		         if (pd != null) {
		             pd.close();
		         }
		         
		         BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(output)));
		         String line = reader.readLine();
		 		while (line != null) {
		 			if(line.contains("October")){
		 				System.out.println(line);
		 			}
		 			line = reader.readLine();
		 		}
		 		//System.out.println(grammar);
		 		reader.close();
		        // I use close() to flush the stream.
		        wr.close();
		 } catch (Exception e){
		         e.printStackTrace();
        } 
     }
	
}
