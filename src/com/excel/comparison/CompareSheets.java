package com.excel.comparison;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.plaf.FileChooserUI;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CompareSheets {

	
	public static void main(String[] args) {
		
	
		BufferedWriter bufferWriter = null;
		try {
			FileInputStream excellFile1 = new FileInputStream(new File(
			        "C:\\Workarea_Watson\\DBS\\Outlookassignment\\186Test\\PredefinedQuestions\\PredefinedQA_07082015.xls"));
		
        FileInputStream excellFile2 = new FileInputStream(new File(
                "C:\\Workarea_Watson\\DBS\\Outlookassignment\\186Test\\PredefinedQuestions\\pdfn_1442_rating_export.xls"));
        File newCSVFile = new File("C:\\Workarea_Watson\\DBS\\Outlookassignment\\186Test\\PredefinedQuestions\\Comparison.csv");
		if(!newCSVFile.exists())
		{
			newCSVFile.createNewFile();
		}
		
		bufferWriter = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(newCSVFile))));
		bufferWriter.write("Question,Batch_Polarity,ART_Polarity1,ART_Polarity2,ART_Polarity3,ART_Polarity4,ART_Polarity5,IS_POLARITY_EQUAL");
        Workbook workbook1 = WorkbookFactory.create(excellFile1);
        Workbook workbook2 = WorkbookFactory.create(excellFile2);
//        XSSFWorkbook wb1 = new XSSFWorkbook(excellFile1);
//        XSSFSheet sheet1 = wb1.getSheetAt(0);
        Sheet sheet1 = workbook1.getSheetAt(0);
        System.out.println(sheet1.getFirstRowNum());
        System.out.println(sheet1.getLastRowNum());
        int firstRow1 = sheet1.getFirstRowNum();
        int lastRow1 = sheet1.getLastRowNum();
        
        Sheet sheet2 = workbook2.getSheetAt(0);
        System.out.println(sheet2.getFirstRowNum());
        System.out.println(sheet2.getLastRowNum());
        int firstRow2 = sheet2.getFirstRowNum();
        int lastRow2 = sheet2.getLastRowNum();
        int k =firstRow2;
        for(int i=firstRow1+1; i <= lastRow1; i++) {
        	bufferWriter.newLine();
        	Row row     = sheet1.getRow(i);
        	String question1 = row.getCell(1).getStringCellValue();
        	bufferWriter.write(question1+",");
        	String polarity1 = row.getCell(3).getStringCellValue();
        	bufferWriter.write(polarity1);
        	String conviction = row.getCell(4).getStringCellValue();
        	System.out.println("Question for Batch ::"+question1);
        	System.out.println("Polarity for Batch ::"+polarity1);
        	for(int j=k+1; j <= lastRow2; j++) {
        		Row row2     = sheet2.getRow(j);
            	String question2 = row2.getCell(2).getStringCellValue();
            	String polarity2 = row2.getCell(4).getStringCellValue();
            	String conviction2 = row2.getCell(3).getStringCellValue();
            	System.out.println("Question for ART ::"+question2);
            	System.out.println("Polarity for ART ::"+polarity2);
            	
        		if(!(question1.trim()).equalsIgnoreCase(question2.trim())){
        			if((polarity1.trim()).equalsIgnoreCase(polarity2.trim()))
            			bufferWriter.write(",TRUE");
            		else
            			bufferWriter.write(",FALSE");
        			break;
        		}
        		else{
        			bufferWriter.write(",");
        		}
        		bufferWriter.write(polarity2);
        		
        		k=j;
        	}
//        	if(j <= lastRow2){
//            	Row row2     = sheet2.getRow(j);
//            	String question2 = row2.getCell(2).getStringCellValue();
//            	String polarity2 = row2.getCell(4).getStringCellValue();
//            	System.out.println("Question for ART ::"+question2);
//                while((question1.trim()).equalsIgnoreCase(question2.trim())){
//                	System.out.println("Polarity for ART ::"+polarity2);
//                	j++;
//                }
//        		
//        	}
        	
        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
//			bufferRead.close();
			try {
				bufferWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void compareExcels(XSSFWorkbook wb1,XSSFWorkbook wb2 ){
		
//		Workbook wb = Workbook.getWorkbook(new FileInputStream("D:\\ssb.xls"));
		XSSFSheet sheet1 = wb1.getSheetAt(0);
		XSSFSheet sheet2 = wb1.getSheetAt(0);
		compareTwoSheets(sheet1,sheet2);
	}
	
	  // Compare Two Sheets
    public static boolean compareTwoSheets(XSSFSheet sheet1, XSSFSheet sheet2) {
        int firstRow1 = sheet1.getFirstRowNum();
        int lastRow1 = sheet1.getLastRowNum();
        boolean equalSheets = true;
        for(int i=firstRow1; i <= lastRow1; i++) {
            
            System.out.println("\n\nComparing Row "+i);
            
            XSSFRow row1 = sheet1.getRow(i);
            XSSFRow row2 = sheet2.getRow(i);
            if(!compareTwoRows(row1, row2)) {
                equalSheets = false;
                System.out.println("Row "+i+" - Not Equal");
                break;
            } else {
                System.out.println("Row "+i+" - Equal");
            }
        }
        return equalSheets;
    }

    // Compare Two Rows
    public static boolean compareTwoRows(XSSFRow row1, XSSFRow row2) {
        if((row1 == null) && (row2 == null)) {
            return true;
        } else if((row1 == null) || (row2 == null)) {
            return false;
        }
        
        int firstCell1 = row1.getFirstCellNum();
        int lastCell1 = row1.getLastCellNum();
        boolean equalRows = true;
        
        // Compare all cells in a row
        for(int i=firstCell1; i <= lastCell1; i++) {
            XSSFCell cell1 = row1.getCell(i);
            XSSFCell cell2 = row2.getCell(i);
            if(!compareTwoCells(cell1, cell2)) {
                equalRows = false;
                System.err.println("       Cell "+i+" - NOt Equal");
                break;
            } else {
                System.out.println("       Cell "+i+" - Equal");
            }
        }
        return equalRows;
    }

    // Compare Two Cells
    public static boolean compareTwoCells(XSSFCell cell1, XSSFCell cell2) {
        if((cell1 == null) && (cell2 == null)) {
            return true;
        } else if((cell1 == null) || (cell2 == null)) {
            return false;
        }
        
        boolean equalCells = false;
        int type1 = cell1.getCellType();
        int type2 = cell2.getCellType();
        if (type1 == type2) {
            if (cell1.getCellStyle().equals(cell2.getCellStyle())) {
                // Compare cells based on its type
                switch (cell1.getCellType()) {
                case HSSFCell.CELL_TYPE_FORMULA:
                    if (cell1.getCellFormula().equals(cell2.getCellFormula())) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if (cell1.getNumericCellValue() == cell2
                            .getNumericCellValue()) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    if (cell1.getStringCellValue().equals(cell2
                            .getStringCellValue())) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    if (cell2.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    if (cell1.getBooleanCellValue() == cell2
                            .getBooleanCellValue()) {
                        equalCells = true;
                    }
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    if (cell1.getErrorCellValue() == cell2.getErrorCellValue()) {
                        equalCells = true;
                    }
                    break;
                default:
                    if (cell1.getStringCellValue().equals(
                            cell2.getStringCellValue())) {
                        equalCells = true;
                    }
                    break;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return equalCells;
    }
}

