package com.excel.comparison;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.PDFReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;



public class XLSToJSONConverter {

	public static void convertToJson(File inputFile){

		BufferedWriter bufferWriter = null;
		BufferedWriter bufferWriterJson =null;
		try {
			FileInputStream excellFile1 = new FileInputStream(inputFile);
			File newCSVFile = new File("/Users/aborse/Documents/Output.csv");
			File newJsonFile = new File("/Users/aborse/Documents/OutputTelstra.json");
			newCSVFile.createNewFile();
			newJsonFile.createNewFile();
			bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newCSVFile))));
			bufferWriterJson = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newJsonFile))));

			bufferWriter.write("COMPLETE ERROR MESSAGE,Error Type,CAUSE OF ERRORS,SUPPORTING INSTRUCTIONS,HOW TO AVOIDFIX");
			Workbook workbook1 = WorkbookFactory.create(excellFile1);
			Sheet sheet1 = workbook1.getSheetAt(0);
			System.out.println(sheet1.getFirstRowNum());
			System.out.println(sheet1.getLastRowNum());
			int firstRow1 = sheet1.getFirstRowNum();
			int lastRow1 = sheet1.getLastRowNum();
			boolean newsubcat = false;
			JsonArray errors = null;
			JsonObject subCatObject = null;
			JsonArray completeObjectArray = new JsonArray();
			for(int i=firstRow1+7; i <= lastRow1; i++) {

				bufferWriter.newLine();
				Row row     = sheet1.getRow(i);

				if(newsubcat){
					errors = new JsonArray();
				}
				if(row!=null && row.getCell(0)!=null){
					//check if the row is a subtitle row	
					if(row.getCell(0).getStringCellValue().equalsIgnoreCase("Generic Siebel") || 
							row.getCell(0).getStringCellValue().equalsIgnoreCase("Work Force Management Errors")|| 
							row.getCell(0).getStringCellValue().equalsIgnoreCase("BigPond Strategic Errors")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB RCRM Errors")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB Alarms Errors")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB One Click Resume/Unbar Error Messages")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB Business/Residential Customer Type")){
						if(subCatObject!=null)
							completeObjectArray.add(subCatObject);
						subCatObject = new JsonObject();
						subCatObject.addProperty("error_cat",row.getCell(0).getStringCellValue());
						newsubcat = true;
					}
					else{
						JsonObject rowObject = new JsonObject();
						String completeErrorMessage = (row.getCell(0) !=null && (row.getCell(0).getStringCellValue()!=null ||  row.getCell(0).getStringCellValue().equals("")))?row.getCell(0).getStringCellValue():"";
						//				        	System.out.println(completeErrorMessage);
						bufferWriter.write("\""+completeErrorMessage+"\",");
						rowObject.addProperty("completeErrorMessage",completeErrorMessage);
						String errorType = (row.getCell(1) !=null && (row.getCell(1).getStringCellValue()!=null ||  row.getCell(1).getStringCellValue().equals("")))?row.getCell(1).getStringCellValue():"";
						//				        	System.out.println(errorType);
						bufferWriter.write(errorType+",");
						rowObject.addProperty("errorType",errorType);
						String causeOfErrors = (row.getCell(2) !=null && (row.getCell(2).getStringCellValue()!=null ||  row.getCell(2).getStringCellValue().equals("")))?row.getCell(2).getStringCellValue():"";
						//				        	System.out.println(causeOfErrors);
						bufferWriter.write("\""+causeOfErrors+"\",");
						rowObject.addProperty("causeOfErrors",causeOfErrors);
						String supportingInstructions = (row.getCell(3) !=null && (row.getCell(3).getStringCellValue()!=null ||  row.getCell(3).getStringCellValue().equals("")))?row.getCell(3).getStringCellValue():"";
						//				        	System.out.println(supportingInstructions);
						bufferWriter.write("\""+supportingInstructions+"\",");
						rowObject.addProperty("supportingInstructions",supportingInstructions);
						String howToAvoidFix = (row.getCell(4) !=null && (row.getCell(4).getStringCellValue()!=null ||  row.getCell(4).getStringCellValue().equals("")))?row.getCell(4).getStringCellValue():"";
						//				        	System.out.println(howToAvoidFix);
						bufferWriter.write("\""+howToAvoidFix+"\"");
						rowObject.addProperty("howToAvoidFix",howToAvoidFix);
						errors.add(rowObject);
						subCatObject.add("Errors", errors);
						newsubcat = false;
					}

				}


			}
			System.out.println(completeObjectArray.toString());
			bufferWriterJson.write(completeObjectArray.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bufferWriter.close();
				bufferWriterJson.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void convertToMultipleJson(File inputFile){

		BufferedWriter bufferWriter = null;
		BufferedWriter bufferWriterJson =null;
		try {
			FileInputStream excellFile1 = new FileInputStream(inputFile);
			File newCSVFile = new File("/Users/aborse/Documents/Output.csv");

			newCSVFile.createNewFile();

			bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newCSVFile))));


			bufferWriter.write("COMPLETE ERROR MESSAGE,Error Type,CAUSE OF ERRORS,SUPPORTING INSTRUCTIONS,HOW TO AVOIDFIX");
			Workbook workbook1 = WorkbookFactory.create(excellFile1);
			Sheet sheet1 = workbook1.getSheetAt(0);
			System.out.println(sheet1.getFirstRowNum());
			System.out.println(sheet1.getLastRowNum());
			int firstRow1 = sheet1.getFirstRowNum();
			int lastRow1 = sheet1.getLastRowNum();


			String ErrorSubCat = null;
			for(int i=firstRow1+7; i <= lastRow1; i++) {

				bufferWriter.newLine();
				Row row     = sheet1.getRow(i);
				if(row!=null && row.getCell(0)!=null){
					//check if the row is a subtitle row	
					if(row.getCell(0).getStringCellValue().equalsIgnoreCase("Generic Siebel") || 
							row.getCell(0).getStringCellValue().equalsIgnoreCase("Work Force Management Errors")|| 
							row.getCell(0).getStringCellValue().equalsIgnoreCase("BigPond Strategic Errors")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB RCRM Errors")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB Alarms Errors")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB One Click Resume/Unbar Error Messages")||
							row.getCell(0).getStringCellValue().equalsIgnoreCase("CCB Business/Residential Customer Type")){

						ErrorSubCat = row.getCell(0).getStringCellValue();

					}
					else{
						JsonObject parentRowObject = new JsonObject();
						JsonObject rowObject = new JsonObject();
						parentRowObject.addProperty("Error_Cat", ErrorSubCat);
						String completeErrorMessage = (row.getCell(0) !=null && (row.getCell(0).getStringCellValue()!=null ||  row.getCell(0).getStringCellValue().equals("")))?row.getCell(0).getStringCellValue():"";
						//				        	System.out.println(completeErrorMessage);
						bufferWriter.write("\""+completeErrorMessage+"\",");
						rowObject.addProperty("completeErrorMessage",completeErrorMessage);
						String errorType = (row.getCell(1) !=null && (row.getCell(1).getStringCellValue()!=null ||  row.getCell(1).getStringCellValue().equals("")))?row.getCell(1).getStringCellValue():"";
						//				        	System.out.println(errorType);
						bufferWriter.write(errorType+",");
						rowObject.addProperty("errorType",errorType);
						String causeOfErrors = (row.getCell(2) !=null && (row.getCell(2).getStringCellValue()!=null ||  row.getCell(2).getStringCellValue().equals("")))?row.getCell(2).getStringCellValue():"";
						//				        	System.out.println(causeOfErrors);
						bufferWriter.write("\""+causeOfErrors+"\",");
						rowObject.addProperty("causeOfErrors",causeOfErrors);
						String supportingInstructions = (row.getCell(3) !=null && (row.getCell(3).getStringCellValue()!=null ||  row.getCell(3).getStringCellValue().equals("")))?row.getCell(3).getStringCellValue():"";
						//				        	System.out.println(supportingInstructions);
						bufferWriter.write("\""+supportingInstructions+"\",");
						rowObject.addProperty("supportingInstructions",supportingInstructions);
						String howToAvoidFix = (row.getCell(4) !=null && (row.getCell(4).getStringCellValue()!=null ||  row.getCell(4).getStringCellValue().equals("")))?row.getCell(4).getStringCellValue():"";
						//				        	System.out.println(howToAvoidFix);
						bufferWriter.write("\""+howToAvoidFix+"\"");
						rowObject.addProperty("howToAvoidFix",howToAvoidFix);
						parentRowObject.add("Errors", rowObject);
						System.out.println(parentRowObject.toString());
						File newJsonFile = new File("/Users/aborse/Documents/Workarea/Telstra/jsons/TelstraOutputErrorsRow"+i+".json");
						newJsonFile.createNewFile();
						bufferWriterJson = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newJsonFile))));
						bufferWriterJson.write(parentRowObject.toString());
						bufferWriterJson.close();
					}

				}


			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bufferWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	public static void convertExcelToCSVAndMergeSameRows(File inputFile){

		BufferedWriter bufferWriter = null;
		try {
			FileInputStream excellFile1 = new FileInputStream(inputFile);
			File newCSVFile = new File("/Users/aborse/Documents/Workarea/AGL/Output_MergedData_full_2.csv");

			newCSVFile.createNewFile();

			bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newCSVFile))));


			//bufferWriter.write("COMPLETE ERROR MESSAGE,Error Type,CAUSE OF ERRORS,SUPPORTING INSTRUCTIONS,HOW TO AVOIDFIX");
			Workbook workbook1 = WorkbookFactory.create(excellFile1);
			int numberOfSheets = workbook1.getNumberOfSheets();
			for(int j=0 ; j<numberOfSheets;j++){
				Sheet sheet1 = workbook1.getSheetAt(j);
				System.out.println(sheet1.getFirstRowNum());
				System.out.println(sheet1.getLastRowNum());
				int firstRow1 = sheet1.getFirstRowNum();
				int lastRow1 = sheet1.getLastRowNum();
				String conversationString = "";
				conversationString+="\"";
				for(int i=firstRow1+1; i <= lastRow1; i++) {
					Row row     = sheet1.getRow(i);

					conversationString+=row.getCell(0).getStringCellValue();
					conversationString+=" ";
				}
				conversationString+="\"";
				Row row     =  sheet1.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				if(j == 0)	
				{
					while(cellIterator.hasNext()){
						String cellValue = cellIterator.next().getStringCellValue();
						System.out.println("CELL HEADER VALUE : "+cellValue);
						bufferWriter.write(cellValue);
						bufferWriter.write(",");
					}
				}
				bufferWriter.newLine();
				bufferWriter.write(conversationString);
				//				bufferWriter.write(",");
				row    =  sheet1.getRow(firstRow1+1);
				cellIterator = row.cellIterator();
				cellIterator.next();
				while(cellIterator.hasNext()){
					bufferWriter.write(",");
					Cell cell =cellIterator.next();
					if(cell.getCellType()==0){
						System.out.println("CELL HEADER VALUE : "+cell.getNumericCellValue());
						bufferWriter.write(Double.valueOf(cell.getNumericCellValue()).toString());
					}
					else{
						//				   System.out.println("CELL HEADER VALUE : "+cell.getNumericCellValue());
						System.out.println("CELL HEADER VALUE : "+cell.getStringCellValue());
						bufferWriter.write("\"");
						bufferWriter.write(cell.getStringCellValue());
						bufferWriter.write("\"");
					}

					//				System.out.println("CELL HEADER VALUE : "+cellValue);
					//				bufferWriter.write(cellValue);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bufferWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	public static void convertExcelToCSVAndMergeSameRows2(File inputFile){

		BufferedWriter bufferWriter = null;
		try {
			FileInputStream excellFile1 = new FileInputStream(inputFile);
			File newCSVFile = new File("/Users/aborse/Documents/Workarea/AGL/Output_MergedData_New_noplus_wk9.csv");

			newCSVFile.createNewFile();

			bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newCSVFile))));


			//bufferWriter.write("COMPLETE ERROR MESSAGE,Error Type,CAUSE OF ERRORS,SUPPORTING INSTRUCTIONS,HOW TO AVOIDFIX");
			Workbook workbook1 = WorkbookFactory.create(excellFile1);
			int numberOfSheets = workbook1.getNumberOfSheets();
			for(int j=0 ; j<numberOfSheets;j++){
				Sheet sheet1 = workbook1.getSheetAt(j);
				System.out.println(sheet1.getFirstRowNum());
				System.out.println(sheet1.getLastRowNum());
				int firstRow1 = sheet1.getFirstRowNum();
				int lastRow1 = sheet1.getLastRowNum();

				Row headerRow     =  sheet1.getRow(0);
				Iterator<Cell> cellIterator = headerRow.cellIterator();
				if(j == 0)	
				{
					while(cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						if(cell.getColumnIndex() == 1 ||cell.getColumnIndex() == 47 || cell.getColumnIndex() == 39 ||cell.getColumnIndex() == 49){
							String cellValue = cell.getStringCellValue();
							System.out.println("CELL HEADER VALUE : "+cellValue);
							bufferWriter.write(cellValue);
							bufferWriter.write(",");
						}
					}
				}
				int k =0;
				for(int i=k+1; i <= lastRow1; i++) {
					String conversationString = "";
					Row row     = sheet1.getRow(i);
					double callId = row.getCell(1).getNumericCellValue();
					k =i;
					conversationString+="\"";
					while(callId == sheet1.getRow(k).getCell(1).getNumericCellValue()){
						Row row2     = sheet1.getRow(k);
						conversationString+=row2.getCell(47).getStringCellValue();
						conversationString+=" ";
						k++;
					}
					conversationString+="\"";
					System.out.println("Converation String : "+conversationString);
					bufferWriter.newLine();
					//row     = sheet1.getRow(j);
					cellIterator = row.cellIterator();
					//cellIterator.next();
					while(cellIterator.hasNext()){
						//bufferWriter.write(",");
						Cell cell =cellIterator.next();
						System.out.println("cell col index : "+cell.getColumnIndex());
						System.out.println("cell type : "+cell.getCellType());
						if(cell.getColumnIndex() == 1 ||cell.getColumnIndex() == 47 || cell.getColumnIndex() == 39 ||cell.getColumnIndex() == 49){
							if(cell.getColumnIndex()==49){
								System.out.println("CELL  VALUE date : "+cell.getDateCellValue());
								bufferWriter.write(cell.getDateCellValue().toString());
								bufferWriter.write(",");
							}
							else if(cell.getColumnIndex() == 47){
								bufferWriter.write(conversationString);
								bufferWriter.write(",");
							}
							else if(cell.getCellType()==0){
								System.out.println("CELL HEADER VALUE : "+cell.getNumericCellValue());
								bufferWriter.write(Double.valueOf(cell.getNumericCellValue()).toString());
								bufferWriter.write(",");
							}
							else{
								System.out.println("CELL  VALUE : "+cell.getStringCellValue());
								bufferWriter.write("\"");
								bufferWriter.write(cell.getStringCellValue());
								bufferWriter.write("\"");
								bufferWriter.write(",");
							}
						}
					}
					i=k;
				}
			}	

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bufferWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	public static void splitIntoMultipleFiles(File inputFile){
		BufferedWriter bufferWriter = null;
		int i =1;
		try {
			FileInputStream inputIsentiaFile = new FileInputStream(inputFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputIsentiaFile)));
			String line = reader.readLine();
			boolean newfileCreated = false;
			while(line!=null){

				if((i == 1 && line!=null) || (line !=null && line.contains("--------------------"))){
					if(bufferWriter!=null)
					{
						bufferWriter.write("</html></body>");
						bufferWriter.flush();
					}
					File newCSVFile = new File("/Users/aborse/Documents/Workarea/AGL/iSentia 2/multiplearticles/iSentiaArticle_"+inputFile.getName()+"_"+i+".txt");
					newCSVFile.createNewFile();
					newfileCreated = true;
					bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newCSVFile))));
					i++;
				}
				else{
					if(line.contains("===============") || line.matches("\\d{1,2}/\\d{1,2}/\\d{4}:\\s[0-9]\\s(item)[s]{0,1}") || line.isEmpty())
						reader.readLine();
					else{
						System.out.println("line written: "+ line);
						if(newfileCreated)
						{
							bufferWriter.write("<html><title>");
							bufferWriter.write(line);
							bufferWriter.write("<html></title>");
							bufferWriter.write("<html><body>");
							newfileCreated = false;
						}
						else{
							bufferWriter.write(line);	
						}
						bufferWriter.newLine();
					}
				}
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				bufferWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void splitHTMLFileIntoMultipleFiles(File inputFile,String inputTag){
		Document doc = null;
		try {
			doc = Jsoup.parse(inputFile, "UTF-8", "http://example.com/");
			Elements chapters = doc.select(inputTag); 
			//			System.out.println(chapters.html());
			Element head = doc.select("head").first();
			Element firstH2  = doc.select("h2").first();
			Elements siblings = firstH2.siblingElements();
			String h2Text = firstH2.html();
			List<Element> elementsBetween = new ArrayList<Element>();
			for(int i=1;i<siblings.size(); i++){
				Element sibling = siblings.get(i);
				if(!"h2".equals(sibling.tagName())){
					elementsBetween.add(sibling);
				}else{
					processElementsBetween(h2Text, head, elementsBetween,inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")),inputTag);
					elementsBetween.clear();
					h2Text = sibling.html();
				}
			}

			if (! elementsBetween.isEmpty())
				processElementsBetween(h2Text, head, elementsBetween,inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")),inputTag);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public static void splitHTMLFileIntoMultipleFilesWithInputTag(File inputFile,String inputTag){
		Document doc = null;
		try {
			doc = Jsoup.parse(inputFile, "UTF-8", "http://example.com/");
			Elements chapters = doc.select(inputTag); 
			//			System.out.println(chapters.html());
			Element head = doc.select("head").first();
			Element firstH2  = doc.select(inputTag).first();
			Elements siblings = firstH2.siblingElements();
			String h2Text = firstH2.html();
			List<Element> elementsBetween = new ArrayList<Element>();
			for(int i=1;i<siblings.size(); i++){
				System.out.println("Tag name ::------>>"+siblings.get(i));
				Element sibling = siblings.get(i);
				if(!inputTag.equals(sibling.tagName())){
					elementsBetween.add(sibling);
				}else{
					processElementsBetween(h2Text, head, elementsBetween,inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")),inputTag);
					elementsBetween.clear();
					h2Text = sibling.html();
				}
			}

			if (! elementsBetween.isEmpty())
				processElementsBetween(h2Text, head, elementsBetween,inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")),inputTag);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	private static void processElementsBetween(String h2Text,Element head,
			List<Element> elementsBetween,String inputFileName,String inputTag) throws IOException {
		BufferedWriter bufferWriter = null;
		String filename =null;
		if(h2Text.contains("<"))
			filename = h2Text.substring(h2Text.indexOf(">")+1, h2Text.lastIndexOf("<")).replace("<p>", "").replace("</p>", "").replace("<i>", "").replace("</i>", "");
		File newDir = new File("/Users/aborse/Documents/Workarea/tallife/MVP/sectioned html files/sectionedFilesH3/"+inputFileName);
		if(!newDir.exists())
		{
			newDir.mkdir();
		}
		File newHtmlFile = new File("/Users/aborse/Documents/Workarea/tallife/MVP/sectioned html files/sectionedFilesH3/"+inputFileName+"/"+filename+".html");
		if(!newHtmlFile.exists())
		{
			newHtmlFile.createNewFile();
		}
		StringBuffer htmlString = new StringBuffer("");
		htmlString.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
		head.getElementsByTag("title").get(0).text(inputFileName+" ---- "+filename);
		htmlString.append(head);
		htmlString.append("<body>"
				+"<div class=\"c2\">"
				+"<br/>"
				+"<br/>"+"<"+inputTag+">"+h2Text+"</"+inputTag+">"
				+"<br/>"
				+"<br/>"
				+"</div>");
		System.out.println("---");
		for (Element element : elementsBetween) {
			htmlString.append(element.toString());
		}
		htmlString.append("</body></html>");
		bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newHtmlFile))));
		bufferWriter.write(htmlString.toString());
		bufferWriter.close();

	}   

	/*	public void detectPDFFontSize(){
		PDFReader reader = new PDFReader();
				 int nbmax = reader.getNumberOfPages();
				 System.out.println("nb pages " + nbmax);

				 for (int i = 1; i <= nbmax; i++) {
				    System.out.println("----------------------------------------");
				    System.out.println("Page " + i);
				    PdfDictionary dico = reader.getPageN(i);
				    PdfDictionary ressource = dico.getAsDict(PdfName.RESOURCES);
				    PdfDictionary font = ressource.getAsDict(PdfName.FONT);
				    // we got the page fonts
				    Set keys = font.getKeys();
				    Iterator it = keys.iterator();
				    while (it.hasNext()) {
				       PdfName name = (PdfName) it.next();
				       PdfDictionary fontdict = font.getAsDict(name);
				       PdfObject typeFont = fontdict.getDirectObject(PdfName.SUBTYPE);
				       PdfObject baseFont = fontdict.getDirectObject(PdfName.BASEFONT);               
				       System.out.println(baseFont.toString());              
				    }
				 }

	}*/

	public static void convertJsontoHTML(String jsonFilePath) throws JsonIOException, JsonSyntaxException, IOException{
		File jsonFile = new File(jsonFilePath);
		String filename = jsonFilePath.substring(jsonFilePath.lastIndexOf("/")+1, jsonFilePath.indexOf("."));
		JsonParser parser = new JsonParser();
		JsonObject completeJsonObject = parser.parse(new FileReader(jsonFile)).getAsJsonObject();
		JsonArray metadataArray = completeJsonObject.get("metadata").getAsJsonArray();

		//metaInfo+="</head>";
		JsonArray answerUnitsArray = completeJsonObject.get("answer_units").getAsJsonArray();
		int filenameCounter = 0;
		for (JsonElement answerUnit : answerUnitsArray) {
			if(!answerUnit.getAsJsonObject().get("content").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString().equals("")){
				String title = answerUnit.getAsJsonObject().get("title").getAsString();
				System.out.println(title);
				if((title+"/Users/aborse/Documents/Workarea/tallife/MVP/PDF documents/jsonsfromhtmls/titlesInTitlesTag/"+filename).length() > 255){
					//if(title.contains(".")){
					/*title = title.substring(0, title.indexOf("."));
				}
				else if(title.contains(","))
					title = title.substring(0, title.indexOf(","));
				else{*/
					title = title.substring(0, 20);
					//}
				}
				title = checkForSpecialChars(title);
				File newHtmlFile = new File("/Users/aborse/Documents/Workarea/tallife/MVP/PDF documents/jsonsfromhtmls/titlesInTitlesTag/"+filename+"/"+filename+"_"+title+".html");
				if(!newHtmlFile.exists())
				{
					newHtmlFile.createNewFile();
				}
				else{
					filenameCounter++;
					newHtmlFile = new File("/Users/aborse/Documents/Workarea/tallife/MVP/PDF documents/jsonsfromhtmls/titlesInTitlesTag/"+filename+"/"+filename+"_"+title+"_"+filenameCounter+".html");
					System.out.println("This file was overwritten..."+newHtmlFile.getName());
				}
				StringBuffer metaInfo =new StringBuffer();
				StringBuffer titlesString = new StringBuffer();
				metaInfo.append("<head>");
				for (JsonElement metadata : metadataArray) {
					metaInfo.append("<meta name=\""+metadata.getAsJsonObject().get("name").getAsString()+"\" content=\""+metadata.getAsJsonObject().get("content").getAsString()+"\">");
				}
				metaInfo.append("<meta name=\"sourceDocumentName\" content=\""+filename+".pdf\">");
				//metaInfo.append("<title>"+answerUnit.getAsJsonObject().get("title").getAsString()+"</title></head>");
				String completeTitle = getTitle(answerUnit.getAsJsonObject().get("parent_id").getAsString(),answerUnit.getAsJsonObject().get("title").getAsString(),answerUnitsArray);
				if(completeTitle.contains("::")){
					String[] titles = completeTitle.split("::");
					titlesString =new StringBuffer();
					titlesString.append(filename+"&gt;&gt;");
//					titlesString.append("<ul style=\"list-style: none\">");
					for (int i = titles.length-1,j=1; i > 0; i--,j++) {
						if(i > 1){
//							titlesString.append("<li style=\"display: inline\">");
							titlesString.append(titles[i-1]+"&gt;&gt;");
//							titlesString.append(" ");
//							titlesString.append(">>");
//							titlesString.append("</li>");
//							titlesString.append(" ");
							
						}
						else{
//							titlesString.append("</ul>");
//							titlesString.append("<H"+(j)+">");
							titlesString.append(titles[i-1]);
//							titlesString.append("</H"+(j)+">");
						}
//						titlesString.append("<H"+(j)+">");
//						titlesString.append(titles[i-1]);
//						titlesString.append("</H"+(j)+">");
//						if(i == 2)
//							titlesString.append("<br/>");
					}
				}
				else{
//					titlesString.append("<H1>");
					titlesString.append(completeTitle);
//					titlesString.append("</H1>");
				}
//				metaInfo.append("<title>"+answerUnit.getAsJsonObject().get("title").getAsString()+"</title></head>");
				metaInfo.append("<title>"+titlesString+"</title></head>");
				StringBuffer htmlString = new StringBuffer("");
				htmlString.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
				htmlString.append(metaInfo.toString());
				htmlString.append("<body>");
				//htmlString.append("<h1>");
				// htmlString.append(answerUnit.getAsJsonObject().get("title").getAsString());
//				htmlString.append(titlesString.toString());
				//htmlString.append("</h1>");
				JsonArray contentArray = answerUnit.getAsJsonObject().get("content").getAsJsonArray();
				for (JsonElement content : contentArray) {
					if(content.getAsJsonObject().get("media_type").getAsString().equals("text/html")){
						htmlString.append(content.getAsJsonObject().get("text").getAsString());
					}
				}
				htmlString.append("</body></html>");
				BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newHtmlFile))));
				bufferWriter.write(htmlString.toString());
				bufferWriter.close();
			}
		}
	}

	public static String checkForSpecialChars(String title){
		String regex = "[/\"';+()*&^%$#@!~?,�]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(title);
		title = m.replaceAll("");
		return title;
	}
	
	public static String getTitle(String parentId,String title, JsonArray answerUnitsArray){
		StringBuffer originalTitle = new StringBuffer(title);
		for (JsonElement answerUnit : answerUnitsArray) {
			if(parentId.equals(answerUnit.getAsJsonObject().get("id").getAsString())){
				return originalTitle.append("::").append(getTitle(answerUnit.getAsJsonObject().get("parent_id").getAsString(), answerUnit.getAsJsonObject().get("title").getAsString(), answerUnitsArray)).toString();
			}
		}
		return title;
	}

	public static void main(String[] args) {
		//		convertToJson(new File("/Users/aborse/Downloads/Pre Submission Error Matrix General-672334.xls"));
		//convertToMultipleJson(new File("/Users/aborse/Downloads/Pre Submission Error Matrix General-672334.xls"));
		//convertExcelToCSVAndMergeSameRows(new File("/Users/aborse/Documents/Workarea/AGL/10 Text Transcripts (random) converted.xls"));
		//convertExcelToCSVAndMergeSameRows2(new File("/Users/aborse/Documents/Workarea/AGL/Seven_Weeks_Call_Transcripts_Data/xls/Call_Transcripts_Master_Wk9_redacted.xls"));
		//		File folder = new File("/Users/aborse/Documents/Workarea/AGL/iSentia 2/");
		//		File[] listOfFiles = folder.listFiles();
		//
		//		    for (int i = 0; i < listOfFiles.length; i++) {
		//		      if (listOfFiles[i].isFile()) {
		//		        System.out.println("File " + listOfFiles[i].getName());
		//		        splitIntoMultipleFiles(listOfFiles[i]);
		//		      } else if (listOfFiles[i].isDirectory()) {
		//		        System.out.println("Directory " + listOfFiles[i].getName());
		//		      }
		//		    }
		//splitIntoMultipleFiles(new File("/Users/aborse/Documents/Workarea/AGL/iSentia 2/Mediaportal-report-1500847037164 Customer Loyalty.txt"));
		//		splitHTMLFileIntoMultipleFilesWithInputTag(new File("/Users/aborse/Documents/Workarea/tallife/MVP/sectioned html files/unsectioned files/Accelerated Protection Adviser Guide.html"), "h3");
		try {
			convertJsontoHTML("/Users/aborse/Documents/Workarea/tallife/MVP/PDF documents/jsonsfromhtmls/Accelerated Protection PDS.json");
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
