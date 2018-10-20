package com.excel.comparison;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/*Compares two Excel workbooks, and saves the difference (cell by cell) in a result file for manual analysis. 
 * The seventh parameter is the sheet name which is optional. 
 * If seventh parameter is not given then active sheet will be compared else the sheet name*/
public class CompareReports   
{
	
//	TestcaseExecutionResultVO testCaseExecutionResult = new TestcaseExecutionResultVO();
	
	/**
	 * This is logger object used to log keyword actions into a log file
	 */
	private static Logger logger = Logger.getLogger( CompareReports.class.getName());

	
	Sheet baseSheetX, currentSheetX, resultSheetX;
	Workbook baseWorkBookX, currentWorkBookX, resultWorkBookX;
	Row baseRowX, currentRowX, resultRowX;
	Cell baseCellX, currentCellX, resultCellX;

	String baseValue, currentValue;
	int diffCount = 1;
	FileOutputStream resultFileOut;

	/*Path to folder containing the Base Excel file*/
	 private String baseFilePath = null;
	 
	 /*Name of Base Excel workbook (.xls)*/
	 private String baseFileName = null;
	 
	/* Path to folder containing the Current Excel file */
	 private String currentFilePath = null;
			 
	/* Name of Current Excel workbook (.xls)*/
	 private String currentFileName = null;
	 
	/* Path to folder containing output diff file */
	 private String resultFilePath = null;
	 
	/*Name of Result Excel workbook (.xls)*/	 
	 private String resultFileName = null;
	 
	 private String baseReport = null; 
	 private String currentReport = null; 
	 private String compareResult = null;
	 private String resultReport = null;
	 
	//boolean isXLSXFile = false;

	/**
	 * This method runs after all the validations are successful
	 * 
	 * @param listOfParameters
	 *            : This method requires six parameters, the file path and file
	 *            name for each of current, base and compare report Compare
	 *            result will be append the compare file name
	 *            Seventh parameter is the optional for sheet name
	 * @return <code>True</code> if the comparison is done and file is updated
	 */
	public void executeScript(String... listOfParameters) 
	{
		// Comparison is done cell by cell for each row.
		
		logger.info("Inside CompareReports executeScript ");

		
		int caseId = 0;

		Map<String, Map<String, String>> baseSheetMap = null;
		Map<String, Map<String, String>> currentSheeMap = null;
		List<String> baseColTobeSkipped = new ArrayList<String>();
		List<String> currentColTobeSkipped = new ArrayList<String>();

		String baseSheetName = null;
		String currentSheetName = null;
		boolean isCompareByColumnNameInsteadOfCellByCell = false;
		
		
		
			isCompareByColumnNameInsteadOfCellByCell = parseParamToFetchComparisonType(listOfParameters[6]);

			if(!isCompareByColumnNameInsteadOfCellByCell)
			{
				isCompareByColumnNameInsteadOfCellByCell = parseParamToFetchComparisonType(listOfParameters[7]);
			}

			listOfParameters[6] = listOfParameters[6].replaceAll("<COMPARE_BY_COLUMN_NAMES>", "");
			listOfParameters[7] = listOfParameters[7].replaceAll("<COMPARE_BY_COLUMN_NAMES>", "");

			listOfParameters[6] = parseParamToFetchSkippedColumnNames(listOfParameters[6], baseColTobeSkipped);
			listOfParameters[7] = parseParamToFetchSkippedColumnNames(listOfParameters[7], currentColTobeSkipped);

			baseSheetName = listOfParameters[6];
			currentSheetName = listOfParameters[7];
		
		/*If only sheet names passed in first 2 arguments (and not file paths and names), 
		 * this call is definitely for webservises. Retrieve the sheet data from global 'workbookmap' and compare.*/
		if((baseFilePath.toUpperCase().startsWith("<TEST DATA>") || baseFilePath.toUpperCase().startsWith("<TEST_DATA>")) 
				&& (baseFileName.toUpperCase().startsWith("<TEST DATA>") || baseFileName.toUpperCase().startsWith("<TEST_DATA>")))
		{
			caseId = 2;
			baseSheetName = listOfParameters[0];
			currentSheetName = listOfParameters[1];

			baseSheetName = removeTESTDATATagFromSheetName(baseSheetName);
			currentSheetName = removeTESTDATATagFromSheetName(currentSheetName);

			if(baseSheetName.equalsIgnoreCase("") && currentSheetName.equalsIgnoreCase(""))
			{
				logger.error("Sheet names " + baseSheetName + " and " + currentSheetName + " are blank");
//				testCaseExecutionResult.setMessage("Sheet names " + baseSheetName + " and " + currentSheetName + " are blank");
//				return testCaseExecutionResult;
			}
			else if(baseSheetName.equalsIgnoreCase(""))
			{
				logger.error("Sheet name " + baseSheetName + " is blank");
//				testCaseExecutionResult.setMessage("Sheet name " + baseSheetName + " is blank");
//				return testCaseExecutionResult;
			}
			else if(currentSheetName.equalsIgnoreCase(""))
			{
				logger.error("Sheet name " + currentSheetName + " is blank");
//				testCaseExecutionResult.setMessage("Sheet name " + currentSheetName + " is blank");
//				return testCaseExecutionResult;
			}
		}
		else if(!baseSheetName.equalsIgnoreCase("") && !baseSheetName.equalsIgnoreCase(null))
		{
			caseId = 1;

			if(currentSheetName.equalsIgnoreCase(""))
			{
				currentSheetName = baseSheetName;
			}
		}

		try 
		{
			compareResult = resultFilePath.concat(resultFileName);

			if(caseId != 2)
			{
				baseReport = baseFilePath.concat(baseFileName);
				currentReport = currentFilePath.concat(currentFileName);

				if(baseReport.toUpperCase().endsWith(".XLSX"))
				{
					baseWorkBookX = new XSSFWorkbook(new FileInputStream(baseReport));
				}
				else
				{
					POIFSFileSystem baseFileSystem=null;
					try {
						baseFileSystem = new POIFSFileSystem(new FileInputStream(baseReport));
						baseWorkBookX = new HSSFWorkbook(baseFileSystem);
					} catch (Exception e) {
						baseWorkBookX = new XSSFWorkbook(new FileInputStream(baseReport));
					}
				}

				if(currentReport.toUpperCase().endsWith(".XLSX"))
				{
					currentWorkBookX = new XSSFWorkbook(new FileInputStream(currentReport));
				}
				else
				{
					try {
						POIFSFileSystem currentFileSystem = new POIFSFileSystem(new FileInputStream(currentReport));
						currentWorkBookX = new HSSFWorkbook(currentFileSystem);
					} catch (Exception e) {
						currentWorkBookX = new XSSFWorkbook(new FileInputStream(currentReport));
					}
				}
			}

			if(caseId==0)
			{
				baseSheetX = baseWorkBookX.getSheetAt(baseWorkBookX.getActiveSheetIndex());			
				currentSheetX = currentWorkBookX.getSheetAt(currentWorkBookX.getActiveSheetIndex());

			}else if(caseId==1)
			{
				baseSheetX = baseWorkBookX.getSheet(baseSheetName);
				currentSheetX = currentWorkBookX.getSheet(currentSheetName);

				if(baseSheetX == null && currentSheetX == null)
				{
					logger.error("Sheets '" + baseSheetName + "' and '" + currentSheetName + "' not found");
//					testCaseExecutionResult.setMessage("Sheets '" + baseSheetName + "' and '" + currentSheetName + "' not found");
//					return testCaseExecutionResult;
				}
				else if(baseSheetX == null)
				{
					logger.error("Sheet '" + baseSheetName + "' not found");
//					testCaseExecutionResult.setMessage("Sheet '" + baseSheetName + "' not found");
//					return testCaseExecutionResult;
				}
				else if(currentSheetX == null)
				{
					logger.error("Sheet '" + currentSheetName + "' not found");
//					testCaseExecutionResult.setMessage("Sheet '" + currentSheetName + "' not found");
//					return testCaseExecutionResult;
				}

			}else if(caseId == 2)
			{
				baseSheetMap = workBookMap.get(baseSheetName);
				currentSheeMap = workBookMap.get(currentSheetName);

				if(baseSheetMap == null && currentSheeMap == null)
				{
					logger.error("Sheets '" + baseSheetName + "' and '" + currentSheetName + "' not found");
					testCaseExecutionResult.setMessage("Sheets '" + baseSheetName + "' and '" + currentSheetName + "' not found");
					return testCaseExecutionResult;
				}
				else if(baseSheetMap == null)
				{
					logger.error("Sheet '" + baseSheetName + "' not found");
					testCaseExecutionResult.setMessage("Sheet '" + baseSheetName + "' not found");
					return testCaseExecutionResult;
				}
				else if(currentSheeMap == null)
				{
					logger.error("Sheet '" + currentSheetName + "' not found");
					testCaseExecutionResult.setMessage("Sheet '" + currentSheetName + "' not found");
					return testCaseExecutionResult;
				}
				else if(baseSheetMap.isEmpty() && currentSheeMap.isEmpty())
				{
					logger.error("Sheets '" + baseSheetName + "' and '" + currentSheetName + "' are empty");
					testCaseExecutionResult.setMessage("Sheets '" + baseSheetName + "' and '" + currentSheetName + "' are empty");
					return testCaseExecutionResult;
				}
				else if(baseSheetMap.isEmpty())
				{
					logger.error("Sheet '" + baseSheetName + "' is empty");
					testCaseExecutionResult.setMessage("Sheet '" + baseSheetName + "' is empty");
					return testCaseExecutionResult;
				}
				else if(currentSheeMap.isEmpty())
				{
					logger.error("Sheet '" + currentSheetName + "' is empty");
					testCaseExecutionResult.setMessage("Sheet '" + currentSheetName + "' is empty");
					return testCaseExecutionResult;
				}
			}
		} catch (FileNotFoundException e) 
		{

		} catch (IOException e)
		{

		}

		try 
		{
			if(compareResult.toUpperCase().endsWith(".XLSX"))
			{
				resultWorkBookX = new XSSFWorkbook(new FileInputStream(compareResult));
			}
			else
			{
				POIFSFileSystem resultFileSystem = new POIFSFileSystem(new FileInputStream(compareResult));
				resultWorkBookX = new HSSFWorkbook(resultFileSystem);
			}
		} catch (FileNotFoundException e2) 
		{
			e2.printStackTrace();
		} catch (IOException e2) 
		{
			e2.printStackTrace();
		}

		//if(isXLSXFile)
		//{
		if (resultWorkBookX.getSheet("result") == null)
		{
			//resultWorkBookX = new XSSFWorkbook();
			resultSheetX = resultWorkBookX.createSheet("result");
			resultRowX = resultSheetX.createRow((short) 0);

			resultRowX.createCell(0).setCellValue("Row");
			resultRowX.createCell(1).setCellValue("Column");
			resultRowX.createCell(2).setCellValue("Base Value <> Current Value");

			resultSheetX.autoSizeColumn(1);
			resultSheetX.autoSizeColumn(2);
			resultSheetX.autoSizeColumn(3);

			diffCount++;

		} else 
		{
			resultSheetX = resultWorkBookX.getSheet("result");
			diffCount = resultSheetX.getPhysicalNumberOfRows() + 1;
		}
		/*}
		else
		{
			if (resultWorkBookX.getSheet("result") == null)
			{
				resultWorkBookX = new HSSFWorkbook();
				resultSheetX = resultWorkBookX.createSheet("result");
				resultRowX = resultSheetX.createRow((short) 0);

				resultRowX.createCell(0).setCellValue(new HSSFRichTextString("Row"));
				resultRowX.createCell(1).setCellValue(new HSSFRichTextString("Column"));
				resultRowX.createCell(2).setCellValue(new HSSFRichTextString("Base Value <> Current Value"));

				resultSheetX.autoSizeColumn(1);
				resultSheetX.autoSizeColumn(2);
				resultSheetX.autoSizeColumn(3);

				diffCount++;

			} else 
			{
				resultSheetX = resultWorkBookX.getSheet("result");
				diffCount = resultSheetX.getPhysicalNumberOfRows() + 1;
			}
		}
		 */		
		try
		{
			if(caseId == 0 || caseId == 1)
			{
				//if(isXLSXFile)
				//{
				testCaseExecutionResult = compareXLSXFilesIrrespectiveOfColSequence(testCaseExecutionResult, caseId, resultFilePath, resultFileName, 
						baseSheetName, currentSheetName, baseReport, currentReport, compareResult, baseColTobeSkipped, currentColTobeSkipped, isCompareByColumnNameInsteadOfCellByCell);
				/*}
				else
				{
					testcaseExecutionResult = compareXLSXFilesIrrespectiveOfColSequence(testcaseExecutionResult, caseId, resultFilePath, resultFileName, 
							baseSheetName, currentSheetName, baseReport, currentReport, compareResult);
				}*/
			}
			else if(caseId == 2)
			{
				testCaseExecutionResult = compareExcelSheetsFromUITestData(testCaseExecutionResult, resultFilePath, resultFileName, 
						baseSheetName, currentSheetName, baseSheetMap, currentSheeMap, compareResult);

				if(testCaseExecutionResult.getStatus() == FAIL)
				{
					testCaseExecutionResult.setMessage("See compare result file : "	+ resultFilePath.concat("Fail\\").concat(resultFileName));
				}
			}

		} catch (FileNotFoundException e2) 
		{
			e2.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} 

		return testCaseExecutionResult;
	}

	private boolean parseParamToFetchComparisonType(String paramValue) 
	{
		if(paramValue.toUpperCase().contains("<COMPARE_BY_COLUMN_NAMES>"))
		{
			return true;
		}

		return false;
	}

	private String parseParamToFetchSkippedColumnNames(String paramValue, List<String> colTobeSkipped) 
	{
		if(paramValue.toUpperCase().contains("<SKIP>"))
		{    
			String sheetName = null;

			if(paramValue.toUpperCase().startsWith("<SKIP>"))
			{
				paramValue = paramValue.replaceFirst("<SKIP>", "");//remove <SKIP> from paramValue
				sheetName = "";//so no sheet name added to param
			}
			else
			{
				String[] paramparts = paramValue.split("<SKIP>");//split paramValue over <SKIP>
				paramValue = paramparts[1];//second entry will be for skips col names
				sheetName = paramparts[0];//first entry will be for sheet name
			}

			String[] skipCols = paramValue.split(";");//split paramValue over ;

			for(int c = 0; c < skipCols.length; c++)//iterate col names
			{
				String colName = skipCols[c];

				if(colName != null && !colName.trim().isEmpty())
				{
					colTobeSkipped.add(colName);//add col names to list 
				}
			}

			return sheetName;
		}
		else
		{
			return paramValue;
		}
	}

	private TestcaseExecutionResultVO compareExcelSheetsFromUITestData(TestcaseExecutionResultVO testcaseExecutionResult, String resultFilePath, 
			String resultFileName, String baseSheetName, String currentSheetName, Map<String, Map<String, String>> baseSheetMap,
			Map<String, Map<String, String>> currentSheeMap, String compareResult) throws IOException 
			{
		boolean isPassed = true;

		int rowPointer = 1;
		int colPointer = 0;

		int iBaseSheetRows = baseSheetMap.size();
		int iCurrentSheetRows = currentSheeMap.size();

		if (iBaseSheetRows != iCurrentSheetRows) 
		{
			boolean isIgnoreRowCount = false;

			try 
			{
				String isMatchRowCountStr = configurationMap.get("IgnoreRowCount");

				if(isMatchRowCountStr != null && (isMatchRowCountStr.equalsIgnoreCase("Y") || isMatchRowCountStr.equalsIgnoreCase("Yes")))
				{
					isIgnoreRowCount = true;
				}
			} 
			catch (Exception e) 
			{
				isIgnoreRowCount = false;
			}

			//SAF-1568
			if(!isIgnoreRowCount)
			{
				testcaseExecutionResult.setMessage(ERROR_COMPARE_SHEET_ROW_MISMATCH);
				new File(compareResult).delete();
				return testcaseExecutionResult;
			}
			else
			{
				resultRowX = resultSheetX.createRow((short) diffCount++);
				resultRowX.createCell(0).setCellValue(iBaseSheetRows + " <> " );
				resultRowX.createCell(1).setCellValue(iCurrentSheetRows);
				resultRowX.createCell(2).setCellValue(ERROR_COMPARE_SHEET_ROW_MISMATCH);
			}
		}

		Set<String> baseSheetMapKeySet = baseSheetMap.keySet();
		Iterator<String> baseSheetMapKeySetItr = baseSheetMapKeySet.iterator();

		while(baseSheetMapKeySetItr.hasNext())
		{
			String baseRowId = baseSheetMapKeySetItr.next();

			Map<String, String> baseColumnMap = baseSheetMap.get(baseRowId);
			Map<String, String> currentColumnMap = currentSheeMap.get(baseRowId);

			Set<String> baseColumnMapKeySet = baseColumnMap.keySet();
			Iterator<String> baseColumnMapKeySetItr = baseColumnMapKeySet.iterator();

			//Set<String> currentColumnMapKeySet = currentColumnMap.keySet();
			//Iterator<String> currentColumnMapKeySetItr = currentColumnMapKeySet.iterator();

			while(baseColumnMapKeySetItr.hasNext())
			{
				colPointer++;

				String baseColumnName = baseColumnMapKeySetItr.next();
				//				String currentColumnName = null;
				//				
				//				if(currentColumnMapKeySetItr.hasNext())
				//				{
				//					currentColumnName = currentColumnMapKeySetItr.next();
				//				}

				String baseColumnValue = baseColumnMap.get(baseColumnName);
				String currentColumnValue = currentColumnMap.get(baseColumnName);

				if (currentColumnValue == null || baseColumnValue == null)
				{
					isPassed = false;

					resultRowX = resultSheetX.createRow((short) rowPointer++);

					resultRowX.createCell(0).setCellValue(baseRowId);
					resultRowX.createCell(1).setCellValue(colPointer);
					resultRowX.createCell(2).setCellValue(new HSSFRichTextString(baseColumnName) + " <> " + new HSSFRichTextString(baseColumnName));

					resultSheetX.autoSizeColumn(1);
					resultSheetX.autoSizeColumn(2);
					resultSheetX.autoSizeColumn(3);

					resultFileOut = new FileOutputStream(compareResult);
					resultWorkBookX.write(resultFileOut);
					resultFileOut.close();
				}
				else if (!baseColumnValue.trim().equalsIgnoreCase(currentColumnValue.trim())									
						&& !baseColumnValue.trim().contains("Created:")
						&& !currentColumnValue.trim().contains("Created:")
						&& !baseColumnValue.trim().contains("Run Date")
						&& !currentColumnValue.trim().contains("Run Date")
						&& !baseColumnValue.trim().contains("Run Time")
						&& !currentColumnValue.trim().contains("Run Time"))
				{
					isPassed = false;

					resultRowX = resultSheetX.createRow((short) rowPointer++);

					resultRowX.createCell(0).setCellValue(baseRowId);
					resultRowX.createCell(1).setCellValue(colPointer);
					resultRowX.createCell(2).setCellValue(new HSSFRichTextString(baseColumnValue) + " <> " + new HSSFRichTextString(currentColumnValue));

					resultSheetX.autoSizeColumn(1);
					resultSheetX.autoSizeColumn(2);
					resultSheetX.autoSizeColumn(3);

					resultFileOut = new FileOutputStream(compareResult);
					resultWorkBookX.write(resultFileOut);
					resultFileOut.close();
				}
			}

			colPointer = 0;
		}

		if(isPassed)
		{
			testcaseExecutionResult.setStatus(PASS);
			copyfile(compareResult, resultFilePath.concat("/PASS/").concat(resultFileName));
		}
		else
		{
			testcaseExecutionResult.setStatus(FAIL);
			copyfile(compareResult, resultFilePath.concat("/FAIL/").concat(resultFileName));
		}

		new File(compareResult).delete();//to delete file

		return testcaseExecutionResult;
			}

	/*
	private TestcaseExecutionResult compareXLSFiles(TestcaseExecutionResult testcaseExecutionResult, int caseId, String resultFilePath,
			String resultFileName, String baseSheetName, String currentSheetName, String baseReport, String currentReport, String compareResult)
	{
		List<Integer> failRowList = new ArrayList<Integer>();
		List<Integer> failColumnList = new ArrayList<Integer>();

		int iBaseSheetRows = baseSheet.getLastRowNum();
		int iCurrentSheetRows = currentSheet.getLastRowNum();

		if (iBaseSheetRows != iCurrentSheetRows) 
		{
			testcaseExecutionResult.setMessage(ERROR_COMPARE_SHEET_ROW_MISMATCH);
			new File(compareResult).delete();
			return testcaseExecutionResult;
		}

		boolean bPassFlag = true;

		try
		{
			for (int i1 = 0, i2 = 0; i1 <= iBaseSheetRows
			&& i2 <= iCurrentSheetRows; i1++, i2++) 
			{
				try 
				{
					int baseRowColCount = 0;

					try 
					{
						baseRow = baseSheet.getRow(i1);
						baseRowColCount = baseRow.getLastCellNum();

					} catch (Exception e1) 
					{
						baseRow=null;
					}

					int  currRowColCount = 0;

					try 
					{
						currentRow = currentSheet.getRow(i2);
						currRowColCount  = currentRow.getLastCellNum();

					} catch (Exception e1) 
					{
						currentRow=null;
					}

					for (int j1 = 0, j2 = 0; j1 <= baseRowColCount 
					|| j2 <= currRowColCount; j1++, j2++) 
					{
						try
						{
							try 
							{
								baseCell = baseRow.getCell(j1);

							} catch (Exception e) 
							{
								baseCell = null;
							}	

							try 
							{
								currentCell = currentRow.getCell(j2);

							} catch (Exception e) 
							{
								currentCell = null;
							}

							baseValue = null;
							currentValue = null;

							boolean isBaseColHidden = false; 

							try 
							{
								int colBase=baseCell.getColumnIndex();
								isBaseColHidden = baseSheet.isColumnHidden(colBase);
							} catch (Exception e) 
							{
								isBaseColHidden = false;
							}

							boolean isRCellHidden=false;

							try 
							{
								isRCellHidden=baseCell.getCellStyle().getHidden();

							} catch (Exception e) 
							{
								isRCellHidden=false;
							}

							boolean isRzeroHeight=false;

							try 
							{
								isRzeroHeight=baseRow.getZeroHeight();

							} catch (Exception e) 
							{
								isRzeroHeight=false;
							}

							if (baseCell != null && isRCellHidden==false && isRzeroHeight==false && isBaseColHidden==false)
							{
								try 
								{
									baseValue = Utility.getCellValue(baseCell);

								} catch (Exception e) 
								{
									baseValue="";
								}
							}
							else
							{	
								baseValue="";
							}	

							boolean isCurrentColhidden=false;

							try
							{
								int currCol=currentCell.getColumnIndex();
								isCurrentColhidden=currentSheet.isColumnHidden(currCol) ;

							} catch (Exception e) 
							{
								isCurrentColhidden = false;
							}

							boolean isCellHidden=false;

							try 
							{
								isCellHidden=currentCell.getCellStyle().getHidden();

							} catch (Exception e) 
							{
								isCellHidden=false;
							}

							boolean iszeroHeight=false;

							try 
							{
								iszeroHeight=currentRow.getZeroHeight();

							} catch (Exception e) 
							{
								iszeroHeight=false;
							}

							if (currentCell != null &&  isCellHidden==false && iszeroHeight==false && isCurrentColhidden==false)
							{
								try 
								{
									currentValue = Utility.getCellValue(currentCell);

								} catch (Exception e) 
								{
									currentValue = "";
								}
							}	
							else
							{	
								currentValue = "";
							}	

							if (!baseValue.trim().equalsIgnoreCase(currentValue.trim())									
									&& !baseValue.trim().contains("Created:")
									&& !currentValue.trim().contains("Created:")
									&& !baseValue.trim().contains("Run Date")
									&& !currentValue.trim().contains("Run Date")
									&& !baseValue.trim().contains("Run Time")
									&& !currentValue.trim().contains("Run Time")) 
							{
								failRowList.add(i1);
								failColumnList.add(j1);

								resultRow = resultSheet.createRow((short) diffCount++);
								resultRow.createCell(0).setCellValue(i1+1);
								resultRow.createCell(1).setCellValue(j1+1);
								resultRow.createCell(2).setCellValue(new HSSFRichTextString(baseValue) + " <> " + new HSSFRichTextString(currentValue));

								resultSheet.autoSizeColumn(1);
								resultSheet.autoSizeColumn(2);
								resultSheet.autoSizeColumn(3);

								resultFileOut = new FileOutputStream(compareResult);
								resultWorkBook.write(resultFileOut);
								resultFileOut.close();

								bPassFlag = false;
							}
						} catch (Exception e) 
						{
						}
					}
				} catch (Exception e) 
				{
				}
			}
		} catch (Exception e) 
		{
		}

		try 
		{
			if (bPassFlag) 
			{
				testcaseExecutionResult.setStatus(PASS);
				copyfile(compareResult, resultFilePath.concat("/Pass/").concat(resultFileName));

			} else 
			{
				copyfile(compareResult, resultFilePath.concat("/Fail/").concat(resultFileName));

				testcaseExecutionResult.setMessage("See compare result file : "	+ resultFilePath.concat("Fail\\").concat(resultFileName));

				String currentFile = currentReport;
				String compareFile = resultFilePath.concat("Fail/").concat(resultFileName);

				HSSFWorkbook resultWorkBook;

				try 
				{
					if(caseId==0)
					{
						baseSheet = baseWorkBook1.getSheetAt(baseWorkBook1.getActiveSheetIndex());			

					}else if(caseId==1)
					{
						baseSheet = baseWorkBook1.getSheet(baseSheetName);
					}

					resultFileSystem = new POIFSFileSystem(new FileInputStream(compareFile));
					resultWorkBook = new HSSFWorkbook(resultFileSystem);			

					resultSheet = resultWorkBook.createSheet("base");

					iBaseSheetRows = baseSheet.getLastRowNum();

					boolean colHideflag=false;
					boolean rowHideflag=false;

					HSSFCellStyle cellStyle = resultWorkBook.createCellStyle();
					HSSFFont font = resultWorkBook.createFont();

					for (int i1 = 0; i1 <= iBaseSheetRows; i1++) 
					{
						try 
						{
							baseRow = baseSheet.getRow(i1);
							resultRow = resultSheet.createRow((short) i1);

							for (int j1 = 0; j1 < baseRow.getLastCellNum(); j1++) 
							{
								try 
								{
									rowHideflag=false;
									colHideflag=false;
									baseCell = baseRow.getCell(j1);
									baseValue = null;
									int colBase=baseCell.getColumnIndex();
									int baseWidth =baseSheet.getColumnWidth(colBase);

									if (baseCell != null && baseCell.getCellStyle().getHidden()==false && !baseRow.getZeroHeight()==true && baseWidth>0 && baseSheet.isColumnHidden(colBase)==false)
									{
										baseValue = Utility.getCellValue(baseCell);
									}	
									else if (baseRow.getZeroHeight()==true)
									{
										rowHideflag=true;
										baseValue = Utility.getCellValue(baseCell);
									}
									else if (baseSheet.isColumnHidden(colBase)==true || baseWidth==0)
									{
										colHideflag=true;
										baseValue = Utility.getCellValue(baseCell);
									}

									HSSFCell cell1 = resultRow.createCell(j1);
									int reqcol=cell1.getColumnIndex();
									int reqrow=cell1.getRowIndex();

									cell1.setCellValue(new HSSFRichTextString(baseValue));

									for (int iCnt=0;iCnt<failRowList.size();iCnt++)
									{
										if(failColumnList.get(iCnt).intValue()==reqcol && failRowList.get(iCnt).intValue()==reqrow && rowHideflag==false && colHideflag==false) 
										{
											font.setColor(IndexedColors.RED.getIndex());
											cellStyle.setFont(font);
											cell1.setCellStyle(cellStyle);
										}
									}

									resultSheet.autoSizeColumn(colBase);

									if (rowHideflag)
									{
										resultRow.setZeroHeight(true);
									}

									if (colHideflag)
									{
										resultSheet.setColumnWidth(colBase,0);
										resultSheet.setColumnHidden(colBase, true);
									}

									resultFileOut = new FileOutputStream(compareFile);
									resultWorkBook.write(resultFileOut);
									resultFileOut.close();

								} catch (Exception e) 
								{
								}
							}
						} catch (Exception e) 
						{

						}
					}

					currentFileSystem = new POIFSFileSystem(new FileInputStream(currentFile));
					currentWorkBook = new HSSFWorkbook(currentFileSystem);

					if(caseId==0)
					{
						currentSheet = currentWorkBook.getSheetAt(currentWorkBook.getActiveSheetIndex());

					}else if(caseId==1)
					{
						currentSheet = currentWorkBook.getSheet(currentSheetName);
					}

					resultSheet = resultWorkBook.createSheet("current");

					iBaseSheetRows = currentSheet.getLastRowNum();

					for (int i1 = 0; i1 <= iBaseSheetRows; i1++) 
					{
						try 
						{
							baseRow = currentSheet.getRow(i1);
							resultRow = resultSheet.createRow((short) i1);

							for (int j1 = 0; j1 < baseRow.getLastCellNum(); j1++) 
							{
								try 
								{
									rowHideflag=false;
									colHideflag=false;
									baseCell = baseRow.getCell(j1);
									baseValue = null;
									int colBase=baseCell.getColumnIndex();
									int baseWidth =currentSheet.getColumnWidth(colBase);

									if (baseCell != null && baseCell.getCellStyle().getHidden()==false && !baseRow.getZeroHeight()==true && baseWidth>0 && baseSheet.isColumnHidden(colBase)==false)
									{
										baseValue = Utility.getCellValue(baseCell);
									}	
									else if (baseRow.getZeroHeight()==true)
									{
										rowHideflag=true;
										baseValue = Utility.getCellValue(baseCell);
									}
									else if (baseSheet.isColumnHidden(colBase)==true || baseWidth==0)
									{
										colHideflag=true;
										baseValue = Utility.getCellValue(baseCell);
									}

									HSSFCell cell1 = resultRow.createCell(j1);
									int reqcol=cell1.getColumnIndex();
									int reqrow=cell1.getRowIndex();

									cell1.setCellValue(new HSSFRichTextString(baseValue));

									for (int iCnt=0;iCnt<failRowList.size();iCnt++)
									{
										if(failColumnList.get(iCnt).intValue()==reqcol && failRowList.get(iCnt).intValue()==reqrow && rowHideflag==false && colHideflag==false) 
										{
											font.setColor(IndexedColors.RED.getIndex());
											cellStyle.setFont(font);
											cell1.setCellStyle(cellStyle);
										}
									}

									resultSheet.autoSizeColumn(colBase);

									if (rowHideflag)
									{
										resultRow.setZeroHeight(true);
									}

									if (colHideflag)
									{
										resultSheet.setColumnWidth(colBase,0);
										resultSheet.setColumnHidden(colBase, true);
									}

									resultFileOut = new FileOutputStream(compareFile);
									resultWorkBook.write(resultFileOut);
									resultFileOut.close();

								} catch (Exception e) 
								{

								}
							}
						} catch (Exception e) 
						{

						}
					}
				} catch (FileNotFoundException e) 
				{

				} catch (IOException e) 
				{

				}
			}
		} catch (Exception e) 
		{

		} finally 
		{
			new File(compareResult).delete();
		}

		return testcaseExecutionResult;
	}
	 */	

	/**
	 * This functions compares XLSX Files sheets irrespective of Column sequence.
	 * 
	 * @param testcaseExecutionResult
	 * @param caseId
	 * @param resultFilePath
	 * @param resultFileName
	 * @param baseSheetName
	 * @param currentSheetName
	 * @param baseReport
	 * @param currentReport
	 * @param compareResult
	 * @param currentColTobeSkipped 
	 * @param baseColTobeSkipped 
	 * @param isCompareByColumnNameInsteadOfCellByCell 
	 * @return
	 */
	private TestcaseExecutionResultVO compareXLSXFilesIrrespectiveOfColSequence(TestcaseExecutionResultVO testcaseExecutionResult, int caseId, 
			String resultFilePath, String resultFileName, String baseSheetName, String currentSheetName, String baseReport, String currentReport, 
			String compareResult, List<String> baseColTobeSkipped, List<String> currentColTobeSkipped, boolean isCompareByColumnNameInsteadOfCellByCell)
	{
		List<Integer> failRowList = new ArrayList<Integer>();

		List<Integer> baseFailColumnList = new ArrayList<Integer>();
		List<Integer> currentFailColumnList = new ArrayList<Integer>();

		int iBaseSheetRows = baseSheetX.getLastRowNum();
		int iCurrentSheetRows = currentSheetX.getLastRowNum();
		int baseFirstRowCount = 0;
		int currentFirstRowCount = 0;

		try 
		{
			baseFirstRowCount = baseSheetX.getFirstRowNum();
			currentFirstRowCount = currentSheetX.getFirstRowNum();

		} catch (Exception e2) 
		{
		}

		if (iBaseSheetRows != iCurrentSheetRows) 
		{
			boolean isIgnoreRowCount = false;

			try 
			{
				String isMatchRowCountStr = configurationMap.get("IgnoreRowCount");

				if(isMatchRowCountStr != null && (isMatchRowCountStr.equalsIgnoreCase("Y") || isMatchRowCountStr.equalsIgnoreCase("Yes")))
				{
					isIgnoreRowCount = true;
				}
			} 
			catch (Exception e) 
			{
				isIgnoreRowCount = false;
			}

			//SAF-1568
			if(!isIgnoreRowCount)
			{
				testcaseExecutionResult.setMessage(ERROR_COMPARE_SHEET_ROW_MISMATCH);
				new File(compareResult).delete();
				return testcaseExecutionResult;
			}
			else
			{
				resultRowX = resultSheetX.createRow((short) diffCount++);
				resultRowX.createCell(0).setCellValue(iBaseSheetRows + " <> " );
				resultRowX.createCell(1).setCellValue(iCurrentSheetRows);
				resultRowX.createCell(2).setCellValue(ERROR_COMPARE_SHEET_ROW_MISMATCH);
			}
		}

		Map<String, Integer> baseColNameColIndexMap = null;
		Map<String, Integer> currentColNameColIndexMap = null;

		if(isCompareByColumnNameInsteadOfCellByCell)
		{
			/*Maps to remember column names and column position (index) mapping*/
			baseColNameColIndexMap = new HashMap<String, Integer>();
			currentColNameColIndexMap = new HashMap<String, Integer>();

			//Get header rows
			Row headerRow = baseSheetX.getRow(baseFirstRowCount);

			if(headerRow == null)
			{
				testcaseExecutionResult.setMessage("Error while reading '" + baseSheetName + "'");
				return testcaseExecutionResult;
			}

			int headerRowColCount = headerRow.getLastCellNum();

			for (int col = 0; col < headerRowColCount; col++) 
			{
				Cell headerCell = headerRow.getCell(col);

				if(headerCell != null && !baseSheetX.isColumnHidden(col) && !headerCell.getCellStyle().getHidden())
				{
					try 
					{
						String columnName = Utility.getCellValue(headerCell).trim();//SAF-1663

						if(columnName != null && !columnName.trim().isEmpty() && !baseColTobeSkipped.contains(columnName))
						{
							baseColNameColIndexMap.put(columnName, col);
						}
					} catch (Exception e) 
					{
					}
				}
			}

			headerRow = currentSheetX.getRow(currentFirstRowCount);

			if(headerRow == null)
			{
				testcaseExecutionResult.setMessage("Error while reading '" + currentSheetName + "'");
				return testcaseExecutionResult;
			}

			headerRowColCount = headerRow.getLastCellNum();

			for (int col = 0; col < headerRowColCount; col++) 
			{
				Cell headerCell = headerRow.getCell(col);

				if(headerCell != null && !currentSheetX.isColumnHidden(col) && !headerCell.getCellStyle().getHidden())
				{
					try 
					{
						String columnName = Utility.getCellValue(headerCell).trim();//SAF-1663

						if(columnName != null && !columnName.trim().isEmpty() && !currentColTobeSkipped.contains(columnName))
						{
							currentColNameColIndexMap.put(columnName, col);
						}
					} catch (Exception e) 
					{
					}
				}
			}

			if(currentColNameColIndexMap.size() != baseColNameColIndexMap.size())
			{
				testcaseExecutionResult.setMessage(ERROR_COMPARE_SHEET_COL_MISMATCH);
				new File(compareResult).delete();
				return testcaseExecutionResult;
			}
		}

		boolean bPassFlag = true;

		try
		{
			//SAF-1887
			//possible entries:
			//13=2,3,4;14=4,5,6;16=1,2,3;20=1
			//13
			//13;14;16;20
			String ignoreRowValue = configurationMap.get("IgnoreRow");
			Map<Integer, List<Integer>> ignoreRowMap = Utility.parseIgnoreRowValue(ignoreRowValue);

			for (int i1 = baseFirstRowCount + 1, i2 = currentFirstRowCount + 1; i1 <= iBaseSheetRows || i2 <= iCurrentSheetRows; i1++, i2++) 
			{
				try 
				{
					//SAF-1887
					if(ignoreRowMap.containsKey(i1 + 1) && (ignoreRowMap.get(i1 + 1) == null || ignoreRowMap.get(i1 + 1).isEmpty()))
					{
						resultRowX = resultSheetX.createRow((short) diffCount++);
						resultRowX.createCell(0).setCellValue(i1 + 1);
						resultRowX.createCell(2).setCellValue("Row Ignored");
						
						continue;
					}
					
					try 
					{
						baseRowX = baseSheetX.getRow(i1);
					} catch (Exception e1) 
					{
						baseRowX=null;
					}

					try 
					{
						currentRowX = currentSheetX.getRow(i2);
					} catch (Exception e1) 
					{
						currentRowX=null;
					}

					if(baseRowX == null && currentRowX == null)
					{
						continue;
					}
					else 
					{
						//This else loop added for SAF-1914
						//If one of the row is null and other is not, null row is initialized 
						//with same number of columns as that of other. 
						//So that null pointer exception does not occur
						
						if(currentRowX == null)
						{
							currentRowX = currentSheetX.createRow((short) i2);
							
							int baseColumnIndexFirst = baseRowX.getFirstCellNum();
							int baseColumnIndexLast = baseRowX.getLastCellNum();
							
							for(int colBase = baseColumnIndexFirst; colBase < baseColumnIndexLast; colBase++)
							{
								currentRowX.createCell(colBase);
							}
						}

						if(baseRowX == null)
						{
							baseRowX = baseSheetX.createRow((short) i1);
							
							int currentColumnIndexFirst = currentRowX.getFirstCellNum();
							int currentColumnIndexLast = currentRowX.getLastCellNum();
							
							for(int colCurrent = currentColumnIndexFirst; colCurrent < currentColumnIndexLast; colCurrent++)
							{
								baseRowX.createCell(colCurrent);
							}
						}
					}

					if(isCompareByColumnNameInsteadOfCellByCell && baseColNameColIndexMap != null  && !baseColNameColIndexMap.isEmpty())
					{
						Set<String> baseColNameColIndexMapKeySet = baseColNameColIndexMap.keySet();
						Iterator<String> baseColNameColIndexMapKeySetIterator = baseColNameColIndexMapKeySet.iterator();

						while(baseColNameColIndexMapKeySetIterator.hasNext())
						{

							String baseColumnName = baseColNameColIndexMapKeySetIterator.next();
							//SAF-1742
							int baseColumnIndex = baseColNameColIndexMap.get(baseColumnName);
							Integer currentColumnIndex = currentColNameColIndexMap.get(baseColumnName);

							if(currentColumnIndex == null)
							{
								baseFailColumnList.add(baseColumnIndex);

								resultRowX = resultSheetX.createRow((short) diffCount++);
								resultRowX.createCell(0).setCellValue(i1+1);
								resultRowX.createCell(1).setCellValue(baseColumnName);
								resultRowX.createCell(2).setCellValue("Column not found");

								bPassFlag = false;

								continue;
							}//SAF-1742 ends

							//SAF-1887
							if(ignoreRowMap.containsKey(i1 + 1) && ignoreRowMap.get(i1 + 1).contains(baseColumnIndex + 1))
							{
								resultRowX = resultSheetX.createRow((short) diffCount++);
								resultRowX.createCell(0).setCellValue(i1 + 1);
								resultRowX.createCell(1).setCellValue(baseColumnIndex + 1);
								resultRowX.createCell(2).setCellValue("Column Ignored");
								
								continue;
							}
							
							bPassFlag = compareCell(baseRowX, currentRowX, baseColumnIndex, currentColumnIndex, failRowList, baseFailColumnList, currentFailColumnList, i1, bPassFlag, baseColumnName);
						}
					}
					else
					{
						int baseColumnIndexFirst = baseRowX.getFirstCellNum();
						int baseColumnIndexLast = baseRowX.getLastCellNum();

						String isMatchRowCountStr = configurationMap.get("IgnoreColCount");

						if(isMatchRowCountStr==null||"".equals(isMatchRowCountStr))
						{
							isMatchRowCountStr="false";
						}

						int currentColumnIndexFirst = currentRowX.getFirstCellNum();
						int currentColumnIndexLast = currentRowX.getLastCellNum();

						if((baseColumnIndexLast - baseColumnIndexFirst) != (currentColumnIndexLast - currentColumnIndexFirst))
						{
							if(isMatchRowCountStr.equalsIgnoreCase("false"))
							{
								resultRowX = resultSheetX.createRow((short) diffCount++);
								resultRowX.createCell(0).setCellValue(i1+1);
								resultRowX.createCell(1).setCellValue("");
								resultRowX.createCell(2).setCellValue("Column count does not match!");
								
								bPassFlag = false;
							}
							
							//continue;
						}

						if(i1 == 18)
						{
							System.out.println("");
						}
						
						for(int colBase = baseColumnIndexFirst, colCurrent = currentColumnIndexFirst; colBase <= currentColumnIndexLast; colBase++, colCurrent++)
						{
							//SAF-1887
							if(ignoreRowMap.containsKey(i1 + 1) && ignoreRowMap.get(i1 + 1).contains(colBase + 1))
							{
								resultRowX = resultSheetX.createRow((short) diffCount++);
								resultRowX.createCell(0).setCellValue(i1 + 1);
								resultRowX.createCell(1).setCellValue(colBase + 1);
								resultRowX.createCell(2).setCellValue("Column Ignored");
								
								continue;
							}
							
							bPassFlag = compareCell(baseRowX, currentRowX, colBase, colCurrent, failRowList, baseFailColumnList, currentFailColumnList, i1, bPassFlag, (colBase+1)+"");
							
							if(!bPassFlag)
							{
								System.out.println("hi");
							}
						}

					}
				} catch (Exception e) 
				{
					e.printStackTrace();
					testcaseExecutionResult.setMessage(e.getMessage());
					return testcaseExecutionResult;
				}
			}

			//resultSheetX.autoSizeColumn(1);
			//resultSheetX.autoSizeColumn(2);
			//resultSheetX.autoSizeColumn(3);

			resultFileOut = new FileOutputStream(compareResult);
			resultWorkBookX.write(resultFileOut);
			//resultFileOut.close();

		} catch (Exception e) 
		{
			testcaseExecutionResult.setMessage(e.getMessage());
			return testcaseExecutionResult;
		}

		try 
		{
			if (bPassFlag) 
			{
				testcaseExecutionResult.setStatus(PASS);
				copyfile(compareResult, resultFilePath.concat("/Pass/").concat(resultFileName));

			} else 
			{
				copyfile(compareResult, resultFilePath.concat("/Fail/").concat(resultFileName));

				testcaseExecutionResult.setMessage("See compare result file : "	+ resultFilePath.concat("Fail\\").concat(resultFileName));

				//String currentFile = currentReport;
				String compareFile = resultFilePath.concat("Fail/").concat(resultFileName);

				Workbook resultWorkBookX;

				try 
				{
					if(caseId==0)
					{
						baseSheetX = baseWorkBookX.getSheetAt(baseWorkBookX.getActiveSheetIndex());			

					}else if(caseId==1)
					{
						baseSheetX = baseWorkBookX.getSheet(baseSheetName);
					}

					if(compareFile.toUpperCase().endsWith("XLSX"))
					{
						resultWorkBookX = new XSSFWorkbook(new FileInputStream(compareFile));			
					}
					else
					{
						POIFSFileSystem compareFileSystem = new POIFSFileSystem(new FileInputStream(compareFile));
						resultWorkBookX = new HSSFWorkbook(compareFileSystem);
					}

					resultSheetX = resultWorkBookX.createSheet("base");

					iBaseSheetRows = baseSheetX.getLastRowNum();

					boolean colHideflag=false;
					boolean rowHideflag=false;

					CellStyle cellStyle = resultWorkBookX.createCellStyle();
					Font font = resultWorkBookX.createFont();

					for (int i1 = baseFirstRowCount; i1 <= iBaseSheetRows; i1++) 
					{
						try 
						{
							baseRowX = baseSheetX.getRow(i1);
							resultRowX = resultSheetX.createRow((short) i1);

							for (int j1 = 0; baseRowX!=null && j1 < baseRowX.getLastCellNum(); j1++) 
							{
								try 
								{
									rowHideflag=false;
									colHideflag=false;
									baseCellX = baseRowX.getCell(j1);
									baseValue = null;

									int colBase=0;
									int baseWidth =0;

									if (baseCellX != null && baseSheetX != null)
									{
										colBase=baseCellX.getColumnIndex();
										baseWidth =baseSheetX.getColumnWidth(colBase);
									}

									if (baseCellX == null)//SAF-1663
									{
										baseValue = "";
									}
									else if (baseCellX != null && !baseCellX.getCellStyle().getHidden() && !baseRowX.getZeroHeight() && baseWidth > 0 && !baseSheetX.isColumnHidden(colBase))
									{
										baseValue = Utility.getCellValue(baseCellX);
									}	
									else if (baseRowX.getZeroHeight()==true)
									{
										rowHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}
									else if (baseSheetX.isColumnHidden(colBase)==true || baseWidth==0)
									{
										colHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}

									Cell cell1 = resultRowX.createCell(j1);
									//int reqcol=cell1.getColumnIndex();
									//int reqrow=cell1.getRowIndex();

									cell1.setCellValue(baseValue);

									if(failRowList.contains(i1) && baseFailColumnList.contains(j1))
									{
										font.setColor(IndexedColors.RED.getIndex());
										cellStyle.setFont(font);
										cell1.setCellStyle(cellStyle);
									}

									/*for (int iCnt=0;iCnt<failRowList.size();iCnt++)
									{
										if(baseFailColumnList.get(iCnt).intValue()==reqcol && baseFailColumnList.get(iCnt).intValue()==reqrow && rowHideflag==false && colHideflag==false) 
										{
											font.setColor(IndexedColors.RED.getIndex());
											cellStyle.setFont(font);
											cell1.setCellStyle(cellStyle);
										}
									}
									 */
									//resultSheetX.autoSizeColumn(colBase);

									if (rowHideflag)
									{
										resultRowX.setZeroHeight(true);
									}

									if (colHideflag)
									{
										resultSheetX.setColumnWidth(colBase,0);
										resultSheetX.setColumnHidden(colBase, true);
									}
								} catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
					}

					//resultFileOut = new FileOutputStream(compareFile);
					//resultWorkBookX.write(resultFileOut);
					//resultFileOut.close();

					/*if(currentFile.toUpperCase().endsWith("XLSX"))
					{
						currentWorkBookX = new XSSFWorkbook(new FileInputStream(currentFile));
					}
					else
					{
						POIFSFileSystem currentSystem = new POIFSFileSystem(new FileInputStream(currentFile));
						currentWorkBookX = new HSSFWorkbook(currentSystem);
					}
					 */

					if(caseId==0)
					{
						currentSheetX = currentWorkBookX.getSheetAt(currentWorkBookX.getActiveSheetIndex());

					}else if(caseId==1)
					{
						currentSheetX = currentWorkBookX.getSheet(currentSheetName);
					}

					resultSheetX = resultWorkBookX.createSheet("current");

					iCurrentSheetRows = currentSheetX.getLastRowNum();

					for (int i1 = currentFirstRowCount; i1 <= iCurrentSheetRows; i1++) //SAF-1663
					{
						try 
						{
							baseRowX = currentSheetX.getRow(i1);
							resultRowX = resultSheetX.createRow((short) i1);

							for (int j1 = 0; baseRowX!=null && j1 < baseRowX.getLastCellNum(); j1++) 
							{
								try 
								{
									rowHideflag=false;
									colHideflag=false;
									baseCellX = baseRowX.getCell(j1);
									baseValue = null;

									int colBase=0;
									int baseWidth =0;

									if (baseCellX != null && baseSheetX != null)
									{
										colBase=baseCellX.getColumnIndex();
										baseWidth =currentSheetX.getColumnWidth(colBase);
									}

									if (baseCellX == null)
									{
										baseValue = "";
									}
									if (baseCellX != null && !baseCellX.getCellStyle().getHidden() && !baseRowX.getZeroHeight() && baseWidth > 0 && !baseSheetX.isColumnHidden(colBase))
									{
										baseValue = Utility.getCellValue(baseCellX);
									}	
									else if (baseRowX.getZeroHeight()==true)
									{
										rowHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}
									else if (baseSheetX.isColumnHidden(colBase)==true || baseWidth==0)
									{
										colHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}

									Cell cell1 = resultRowX.createCell(j1);
									//int reqcol=cell1.getColumnIndex();
									//int reqrow=cell1.getRowIndex();
									cell1.setCellValue(baseValue);

									if(failRowList.contains(i1) && currentFailColumnList.contains(j1))
									{
										font.setColor(IndexedColors.RED.getIndex());
										cellStyle.setFont(font);
										cell1.setCellStyle(cellStyle);
									}

									/*for (int iCnt=0;iCnt<failRowList.size();iCnt++)
									{
										if(currentFailColumnList.get(iCnt).intValue()==reqcol && currentFailColumnList.get(iCnt).intValue()==reqrow && rowHideflag==false && colHideflag==false) 
										{
											font.setColor(IndexedColors.RED.getIndex());
											cellStyle.setFont(font);
											cell1.setCellStyle(cellStyle);
										}
									}*/

									//resultSheetX.autoSizeColumn(colBase);

									if (rowHideflag)
									{
										resultRowX.setZeroHeight(true);
									}

									if (colHideflag)
									{
										resultSheetX.setColumnWidth(colBase,0);
										resultSheetX.setColumnHidden(colBase, true);
									}
								} catch (Exception e) 
								{
									e.printStackTrace();
								}
							}
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
					}

					resultFileOut = new FileOutputStream(compareFile);
					resultWorkBookX.write(resultFileOut);
					resultFileOut.close();

				} catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}

		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			File f = new File(compareResult);

			if(f.exists())
			{
				f.delete();
			}

			if(f.exists())
			{
				f.delete();
			}

			if(f.exists())
			{
				f.deleteOnExit();
			}
		}

		return testcaseExecutionResult;
	}

	private boolean compareCell(Row baseRowX2, Row currentRowX2, int colBase, int colCurrent, List<Integer> failRowList, List<Integer> baseFailColumnList, List<Integer> currentFailColumnList, int i1, boolean bPassFlag, String baseColumnName) 
	{
		try
		{
			try 
			{
				baseCellX = baseRowX.getCell(colBase);

			} catch (Exception e) 
			{
				baseCellX = null;
			}	

			baseValue = null;

			boolean isRCellHidden=false;

			try 
			{
				if(baseCellX != null && baseCellX.getCellStyle() != null)
				{
					isRCellHidden=baseCellX.getCellStyle().getHidden();
				}
				
				if(!isRCellHidden)//if cell not hidden check for column, if column is hidden
				{
					isRCellHidden = baseSheetX.isColumnHidden(colBase);
				}

			} catch (Exception e) 
			{
				isRCellHidden=false;
			}

			boolean isRzeroHeight=false;

			try 
			{
				isRzeroHeight=baseRowX.getZeroHeight();

			} catch (Exception e) 
			{
				isRzeroHeight=false;
			}

			if (baseCellX != null && isRCellHidden==false && isRzeroHeight==false)
			{
				try 
				{
					baseValue = Utility.getCellValue(baseCellX);

				} catch (Exception e) 
				{
					baseValue="";
				}
			}
			else
			{	
				baseValue="";
			}	

			try 
			{
				currentCellX = currentRowX.getCell(colCurrent);

			} catch (Exception e) 
			{
				currentCellX = null;
			}

			currentValue = null;

			boolean isCellHidden=false;

			try
			{
				if(currentCellX != null && currentCellX.getCellStyle() != null)
				{
					isCellHidden=currentCellX.getCellStyle().getHidden();
				}
				
				if(!isCellHidden)//if cell not hidden check for column, if column is hidden
				{
					isCellHidden = currentSheetX.isColumnHidden(colCurrent);
				}
				
			} catch (Exception e) 
			{
				isCellHidden=false;
			}

			boolean iszeroHeight=false;

			try 
			{
				iszeroHeight=currentRowX.getZeroHeight();

			} catch (Exception e) 
			{
				iszeroHeight=false;
			}


			if (currentCellX != null &&  isCellHidden==false && iszeroHeight==false)
			{
				try 
				{
					currentValue = Utility.getCellValue(currentCellX);

				} catch (Exception e) 
				{
					currentValue = "";
				}
			}	
			else
			{	
				currentValue = "";
			}	

			if (baseValue.trim().equalsIgnoreCase("amit") || currentValue.trim().equalsIgnoreCase("amit"))
			{
				System.out.println(currentValue);
			}
			
			if (!baseValue.trim().equalsIgnoreCase(currentValue.trim())									
					&& !baseValue.trim().contains("Created:")
					&& !currentValue.trim().contains("Created:")
					&& !baseValue.trim().contains("Run Date")
					&& !currentValue.trim().contains("Run Date")
					&& !baseValue.trim().contains("Run Time")
					&& !currentValue.trim().contains("Run Time")) 
			{
				failRowList.add(i1);
				baseFailColumnList.add(colBase);
				currentFailColumnList.add(colCurrent);

				resultRowX = resultSheetX.createRow((short) diffCount++);
				resultRowX.createCell(0).setCellValue(i1+1);
				resultRowX.createCell(1).setCellValue(baseColumnName);
				resultRowX.createCell(2).setCellValue(new HSSFRichTextString(baseValue) + " <> " + new HSSFRichTextString(currentValue));

				bPassFlag = false;
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}		

		return bPassFlag;
	}

	/*private TestcaseExecutionResult compareXLSXFiles(TestcaseExecutionResult testcaseExecutionResult, int caseId, String resultFilePath,
			String resultFileName, String baseSheetName, String currentSheetName, String baseReport, String currentReport, String compareResult)
	{
		List<Integer> failRowList = new ArrayList<Integer>();
		List<Integer> failColumnList = new ArrayList<Integer>();

		int iBaseSheetRows = baseSheetX.getLastRowNum();
		int iCurrentSheetRows = currentSheetX.getLastRowNum();

		if (iBaseSheetRows != iCurrentSheetRows) 
		{
			testcaseExecutionResult.setMessage(ERROR_COMPARE_SHEET_ROW_MISMATCH);
			new File(compareResult).delete();
			return testcaseExecutionResult;
		}

		boolean bPassFlag = true;

		try
		{
			for (int i1 = 0, i2 = 0; i1 <= iBaseSheetRows && i2 <= iCurrentSheetRows; i1++, i2++) 
			{
				try 
				{
					int baseRowColCount = 0;

					try 
					{
						baseRowX = baseSheetX.getRow(i1);
						baseRowColCount = baseRowX.getLastCellNum();

					} catch (Exception e1) 
					{
						baseRowX=null;
					}

					int  currRowColCount = 0;

					try 
					{
						currentRowX = currentSheetX.getRow(i2);
						currRowColCount  = currentRowX.getLastCellNum();

					} catch (Exception e1) 
					{
						currentRowX=null;
					}

					for (int j1 = 0, j2 = 0; j1 <= baseRowColCount || j2 <= currRowColCount; j1++, j2++) 
					{
						try
						{
							try 
							{
								baseCellX = baseRowX.getCell(j1);

							} catch (Exception e) 
							{
								baseCellX = null;
							}	

							try 
							{
								currentCellX = currentRowX.getCell(j2);

							} catch (Exception e) 
							{
								currentCellX = null;
							}

							baseValue = null;
							currentValue = null;

							boolean isBaseColHidden = false; 

							try 
							{
								int colBase=baseCellX.getColumnIndex();
								isBaseColHidden = baseSheetX.isColumnHidden(colBase);

							} catch (Exception e) 
							{
								isBaseColHidden = false;
							}

							boolean isRCellHidden=false;

							try 
							{
								isRCellHidden=baseCellX.getCellStyle().getHidden();

							} catch (Exception e) 
							{
								isRCellHidden=false;
							}

							boolean isRzeroHeight=false;

							try 
							{
								isRzeroHeight=baseRowX.getZeroHeight();

							} catch (Exception e) 
							{
								isRzeroHeight=false;
							}

							if (baseCellX != null && isRCellHidden==false && isRzeroHeight==false && isBaseColHidden==false)
							{
								try 
								{
									baseValue = Utility.getCellValue(baseCellX);

								} catch (Exception e) 
								{
									baseValue="";
								}
							}
							else
							{	
								baseValue="";
							}	

							boolean isCurrentColhidden=false;

							try 
							{
								int currCol=currentCellX.getColumnIndex();
								isCurrentColhidden=currentSheetX.isColumnHidden(currCol) ;

							} catch (Exception e) 
							{
								isCurrentColhidden = false;
							}

							boolean isCellHidden=false;

							try
							{
								isCellHidden=currentCellX.getCellStyle().getHidden();
							} catch (Exception e) 
							{
								isCellHidden=false;
							}

							boolean iszeroHeight=false;

							try 
							{
								iszeroHeight=currentRowX.getZeroHeight();

							} catch (Exception e) 
							{
								iszeroHeight=false;
							}


							if (currentCellX != null &&  isCellHidden==false && iszeroHeight==false && isCurrentColhidden==false)
							{
								try 
								{
									currentValue = Utility.getCellValue(currentCellX);

								} catch (Exception e) 
								{
									currentValue = "";
								}
							}	
							else
							{	
								currentValue = "";
							}	

							if (!baseValue.trim().equalsIgnoreCase(currentValue.trim())									
									&& !baseValue.trim().contains("Created:")
									&& !currentValue.trim().contains("Created:")
									&& !baseValue.trim().contains("Run Date")
									&& !currentValue.trim().contains("Run Date")
									&& !baseValue.trim().contains("Run Time")
									&& !currentValue.trim().contains("Run Time")) 
							{
								failRowList.add(i1);
								failColumnList.add(j1);

								resultRowX = resultSheetX.createRow((short) diffCount++);
								resultRowX.createCell(0).setCellValue(i1+1);
								resultRowX.createCell(1).setCellValue(j1+1);
								resultRowX.createCell(2).setCellValue(new HSSFRichTextString(baseValue) + " <> " + new HSSFRichTextString(currentValue));

								bPassFlag = false;
							}
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}

			//resultSheetX.autoSizeColumn(1);
			//resultSheetX.autoSizeColumn(2);
			//resultSheetX.autoSizeColumn(3);

			resultFileOut = new FileOutputStream(compareResult);
			resultWorkBookX.write(resultFileOut);
			//resultFileOut.close();

		} catch (Exception e) 
		{
			e.printStackTrace();
		}

		try 
		{
			if (bPassFlag) 
			{
				testcaseExecutionResult.setStatus(PASS);
				copyfile(compareResult, resultFilePath.concat("/Pass/").concat(resultFileName));

			} else 
			{
				copyfile(compareResult, resultFilePath.concat("/Fail/").concat(resultFileName));

				testcaseExecutionResult.setMessage("See compare result file : "	+ resultFilePath.concat("Fail\\").concat(resultFileName));

				String currentFile = currentReport;
				String compareFile = resultFilePath.concat("Fail/").concat(resultFileName);

				XSSFWorkbook resultWorkBookX;

				try 
				{
					if(caseId==0)
					{
						baseSheetX = baseWorkBook1X.getSheetAt(baseWorkBook1X.getActiveSheetIndex());			

					}else if(caseId==1)
					{
						baseSheetX = baseWorkBook1X.getSheet(baseSheetName);
					}

					resultWorkBookX = new XSSFWorkbook(new FileInputStream(compareFile));			

					resultSheetX = resultWorkBookX.createSheet("base");

					iBaseSheetRows = baseSheetX.getLastRowNum();

					boolean colHideflag=false;
					boolean rowHideflag=false;

					XSSFCellStyle cellStyle = resultWorkBookX.createCellStyle();
					XSSFFont font = resultWorkBookX.createFont();

					for (int i1 = 0; i1 <= iBaseSheetRows; i1++) 
					{
						try 
						{
							baseRowX = baseSheetX.getRow(i1);
							resultRowX = resultSheetX.createRow((short) i1);

							for (int j1 = 0; j1 < baseRowX.getLastCellNum(); j1++) 
							{
								try 
								{
									rowHideflag=false;
									colHideflag=false;
									baseCellX = baseRowX.getCell(j1);
									baseValue = null;
									int colBase=baseCellX.getColumnIndex();
									int baseWidth =baseSheetX.getColumnWidth(colBase);

									if (baseCellX != null && baseCellX.getCellStyle().getHidden()==false && !baseRowX.getZeroHeight()==true && baseWidth>0 && baseSheetX.isColumnHidden(colBase)==false)
									{
										baseValue = Utility.getCellValue(baseCellX);
									}	
									else if (baseRowX.getZeroHeight()==true)
									{
										rowHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}
									else if (baseSheetX.isColumnHidden(colBase)==true || baseWidth==0)
									{
										colHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}

									Cell cell1 = resultRowX.createCell(j1);
									int reqcol=cell1.getColumnIndex();
									int reqrow=cell1.getRowIndex();

									cell1.setCellValue(new XSSFRichTextString(baseValue));

									for (int iCnt=0;iCnt<failRowList.size();iCnt++)
									{
										if(failColumnList.get(iCnt).intValue()==reqcol && failRowList.get(iCnt).intValue()==reqrow && rowHideflag==false && colHideflag==false) 
										{
											font.setColor(IndexedColors.RED.getIndex());
											cellStyle.setFont(font);
											cell1.setCellStyle(cellStyle);
										}
									}

									//resultSheetX.autoSizeColumn(colBase);

									if (rowHideflag)
									{
										resultRowX.setZeroHeight(true);
									}

									if (colHideflag)
									{
										resultSheetX.setColumnWidth(colBase,0);
										resultSheetX.setColumnHidden(colBase, true);
									}



								} catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
					}

					//resultFileOut = new FileOutputStream(compareFile);
					//resultWorkBookX.write(resultFileOut);
					//resultFileOut.close();

					currentWorkBookX = new XSSFWorkbook(new FileInputStream(currentFile));

					if(caseId==0)
					{
						currentSheetX = currentWorkBookX.getSheetAt(currentWorkBookX.getActiveSheetIndex());

					}else if(caseId==1)
					{
						currentSheetX = currentWorkBookX.getSheet(currentSheetName);
					}

					resultSheetX = resultWorkBookX.createSheet("current");

					iBaseSheetRows = currentSheetX.getLastRowNum();

					for (int i1 = 0; i1 <= iBaseSheetRows; i1++) 
					{
						try 
						{
							baseRowX = currentSheetX.getRow(i1);
							resultRowX = resultSheetX.createRow((short) i1);

							for (int j1 = 0; j1 < baseRowX.getLastCellNum(); j1++) 
							{
								try 
								{
									rowHideflag=false;
									colHideflag=false;
									baseCellX = baseRowX.getCell(j1);
									baseValue = null;
									int colBase=baseCellX.getColumnIndex();
									int baseWidth =currentSheetX.getColumnWidth(colBase);

									if (baseCellX != null && baseCellX.getCellStyle().getHidden()==false && !baseRowX.getZeroHeight()==true && baseWidth>0 && baseSheetX.isColumnHidden(colBase)==false)
									{
										baseValue = Utility.getCellValue(baseCellX);
									}	
									else if (baseRowX.getZeroHeight()==true)
									{
										rowHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}
									else if (baseSheetX.isColumnHidden(colBase)==true || baseWidth==0)
									{
										colHideflag=true;
										baseValue = Utility.getCellValue(baseCellX);
									}

									Cell cell1 = resultRowX.createCell(j1);
									int reqcol=cell1.getColumnIndex();
									int reqrow=cell1.getRowIndex();

									cell1.setCellValue(new XSSFRichTextString(baseValue));

									for (int iCnt=0;iCnt<failRowList.size();iCnt++)
									{
										if(failColumnList.get(iCnt).intValue()==reqcol && failRowList.get(iCnt).intValue()==reqrow && rowHideflag==false && colHideflag==false) 
										{
											font.setColor(IndexedColors.RED.getIndex());
											cellStyle.setFont(font);
											cell1.setCellStyle(cellStyle);
										}
									}

									//resultSheetX.autoSizeColumn(colBase);

									if (rowHideflag)
									{
										resultRowX.setZeroHeight(true);
									}

									if (colHideflag)
									{
										resultSheetX.setColumnWidth(colBase,0);
										resultSheetX.setColumnHidden(colBase, true);
									}



								} catch (Exception e) 
								{
									e.printStackTrace();
								}
							}
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
					}

					resultFileOut = new FileOutputStream(compareFile);
					resultWorkBookX.write(resultFileOut);
					resultFileOut.close();

				} catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}

		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			new File(compareResult).delete();
		}

		return testcaseExecutionResult;
	}*/

	private String removeTESTDATATagFromSheetName(String sheetName) 
	{
		sheetName = sheetName.replaceAll("<TEST DATA>", "");
		sheetName = sheetName.replaceAll("<TEST_DATA>", "");
		sheetName = sheetName.replaceAll("</TEST DATA>", "");
		sheetName = sheetName.replaceAll("</TEST_DATA>", "");

		sheetName = sheetName.replaceAll("<Test Data>", "");
		sheetName = sheetName.replaceAll("<Test_Data>", "");
		sheetName = sheetName.replaceAll("</Test Data>", "");
		sheetName = sheetName.replaceAll("</Test_Data>", "");

		return sheetName;
	}

	/**
	 * This method performs validation of the keyword
	 * 
	 * @param listOfParameters
	 *            : This method requires six parameters, the file path and file
	 *            name for each of current, base and compare report.
	 *            Seventh parameter is the optional for sheet name
	 * @return ExecutionResults containing step execution status(pass/fail),
	 *         exact error message according to failure
	 */
	public TestcaseExecutionResultVO validateKeyword(String... listOfParameters) {
		// the keyword requires six parameters
		
		logger.info("Inside CompareReports validateKeyword ");
		
		if (listOfParameters != null) {
			baseFilePath = listOfParameters[0];
			baseFileName = listOfParameters[1];
			currentFilePath = listOfParameters[2];
			currentFileName = listOfParameters[3];
			resultFilePath = listOfParameters[4];
			resultFileName = listOfParameters[5];
		} else {
			logger.error ("Insufficient Parameters!");
			testCaseExecutionResult.setMessage(ERROR_PARAMETERS_LIST);
			return testCaseExecutionResult;
		}

		testCaseExecutionResult.setExpectedResultFlag(true);

		testCaseExecutionResult.setTestData(baseFilePath + "," + baseFileName
				+ "," + currentFilePath + "," + currentFileName + ","
				+ resultFilePath + "," + resultFileName);

		/*If only sheet names passed in first 2 arguments (and not file paths and names), 
		 * this call is definitely for webservises. Retrieve the sheet data from global 'workbookmap' and compare.*/
		if((baseFilePath.toUpperCase().startsWith("<TEST DATA>") || baseFilePath.toUpperCase().startsWith("<TEST_DATA>")) 
				&& (baseFileName.toUpperCase().startsWith("<TEST DATA>") || baseFileName.toUpperCase().startsWith("<TEST_DATA>")))
		{
			String baseSheetName = listOfParameters[0];
			String currentSheetName = listOfParameters[1];

			baseSheetName = removeTESTDATATagFromSheetName(baseSheetName);
			currentSheetName = removeTESTDATATagFromSheetName(currentSheetName);

			if(baseSheetName.equalsIgnoreCase("") && currentSheetName.equalsIgnoreCase(""))
			{
				logger.error(("Sheet names " + baseSheetName + " and " + currentSheetName + " are blank"));
				testCaseExecutionResult.setMessage("Sheet names " + baseSheetName + " and " + currentSheetName + " are blank");
				return testCaseExecutionResult;
			}
			else if(baseSheetName.equalsIgnoreCase(""))
			{
				logger.error("Sheet name " + baseSheetName + " is blank");
				testCaseExecutionResult.setMessage("Sheet name " + baseSheetName + " is blank");
				return testCaseExecutionResult;
			}
			else if(currentSheetName.equalsIgnoreCase(""))
			{
				logger.error("Sheet name " + currentSheetName + " is blank");
				testCaseExecutionResult.setMessage("Sheet name " + currentSheetName + " is blank");
				return testCaseExecutionResult;
			}
		}
		else
		{
			boolean bErrorPath = true;
			String files = "";
			if (KeywordUtilities.isEmptyString(baseFilePath)) {
				files = "Base";
				bErrorPath = false;
			}

			else if (KeywordUtilities.isEmptyString(currentFilePath)) {
				bErrorPath = false;
				files = "Current";
			} else if (KeywordUtilities.isEmptyString(resultFilePath)) {
				files = "Result";
				bErrorPath = false;
			}

			if (!bErrorPath) {
				testCaseExecutionResult.setMessage(files + " "
						+ ERROR_FILE_PATH_NOT_PASSED);
				return testCaseExecutionResult;
			}

			bErrorPath = true;
			files = "";
			if (KeywordUtilities.isEmptyString(baseFileName)) {
				files = "Base";
				bErrorPath = false;
			}

			else if (KeywordUtilities.isEmptyString(currentFileName)) {
				bErrorPath = false;
				files = "Current";
			} else if (KeywordUtilities.isEmptyString(resultFileName)) {
				files = "Result";
				bErrorPath = false;
			}

			if (!bErrorPath) {
				testCaseExecutionResult.setMessage(files + " "
						+ ERROR_FILE_NAME_NOT_PASSED);
				return testCaseExecutionResult;
			}

			/*Following if loop to mark if its xlsx file or xls file
			if(baseFileName.toUpperCase().endsWith(".XLSX"))
			{
				isXLSXFile = true;
			}*/

			testCaseExecutionResult.setTestData(baseFilePath + "," + baseFileName
					+ "," + currentFilePath + "," + currentFileName + ","
					+ resultFilePath + "," + resultFileName);
			String sheetName = null;
			if(!listOfParameters[6].equalsIgnoreCase("") || !listOfParameters[6].equalsIgnoreCase(null)){
				sheetName = listOfParameters[6];
				testCaseExecutionResult.setTestData(baseFilePath + "," + baseFileName
						+ "," + currentFilePath + "," + currentFileName + ","
						+ resultFilePath + "," + resultFileName + "," +sheetName);
			}
		}

		testCaseExecutionResult.setValid(true);
		return testCaseExecutionResult;
	}

	/**
	 * This method validates the required object for the keyword
	 * 
	 * @param params
	 *            listOfParameters : This method requires six parameters, the
	 *            file path and file name for each of current, base and compare
	 *            report The file path and name existence for compare report is
	 *            optional (i.e. it may or may not exist) If doesn't exist then
	 *            new file directory or file or both for compare report is
	 *            created.
	 *            Seventh parameter is the optional for sheet name
	 * @return ExecutionResults containing step execution status(pass/fail),
	 *         exact error message according to failure
	 */
	public TestcaseExecutionResultVO validateObject(String... listOfParameters) {
		// validaton is done on basis of the file exists or not for the base and
		
		logger.info("Inside CompareReports validateObject ");
		
		
		testCaseExecutionResult.setExpectedResultFlag(true);

		File file = new File(resultFilePath);
		if (!file.exists()) {
			File f = new File(resultFilePath);
			try {
				if (!f.mkdirs()) {
					testCaseExecutionResult.setObjectError(true);
					logger.error("Directory is not created");
					testCaseExecutionResult.setMessage("Directory is not created");
					return testCaseExecutionResult;
				}
			} catch (Exception e) {
				return testCaseExecutionResult;
			}
		}
		file = new File(resultFilePath.concat("/Fail"));
		if (!file.exists()) {
			File f = new File(resultFilePath.concat("/Fail"));
			try {
				if (!f.mkdirs()) {
					//	System.out.println("Directory is not created");
					testCaseExecutionResult.setObjectError(true);
					logger.error("Directory is not created");
					testCaseExecutionResult.setMessage("Directory is not created");
					return testCaseExecutionResult;
				}
			} catch (Exception e) {
				return testCaseExecutionResult;
			}
		}
		file = new File(resultFilePath.concat("/Pass"));
		if (!file.exists()) {
			File f = new File(resultFilePath.concat("/Pass"));
			try {
				if (!f.mkdirs()) {
					// System.out.println("Directory is not created");
					testCaseExecutionResult.setObjectError(true);
					logger.error("Directory is not created");
					testCaseExecutionResult.setMessage("Directory is not created");
					return testCaseExecutionResult;
				}
			} catch (Exception e) {
				return testCaseExecutionResult;
			}
		}

		resultReport = resultFilePath.concat(resultFileName);
		File resultFile = new File(resultReport);

		/*If only sheet names passed in first 2 arguments (and not file paths and names), 
		 * this call is definitely for webservises. Retrieve the sheet data from global 'workbookmap' and compare.*/
		if(!baseFilePath.toUpperCase().startsWith("<TEST DATA>") && !baseFilePath.toUpperCase().startsWith("<TEST_DATA>"))
		{
			baseReport = baseFilePath.concat(baseFileName);
			currentReport = currentFilePath.concat(currentFileName);

			File baseFile = new File(baseReport);
			File currentFile = new File(currentReport);

			if (KeywordUtilities.isEmptyString(resultFilePath)) {
				testCaseExecutionResult.setObjectError(true);
				logger.error(ERROR_FILE_PATH_NOT_PASSED);
				testCaseExecutionResult.setMessage(ERROR_FILE_PATH_NOT_PASSED);
				return testCaseExecutionResult;
			}
			if (!baseFile.exists() || !currentFile.exists()) {
				testCaseExecutionResult.setObjectError(true);
				logger.error(ERROR_FILE_PATH_NOT_PASSED);
				testCaseExecutionResult.setMessage(ERROR_FILE_DOESNOT_EXIST);
				return testCaseExecutionResult;
			}

		}

		if (!resultFile.exists()) 
		{
			if(resultReport.toUpperCase().endsWith(".XLSX"))
			{
				resultWorkBookX = new XSSFWorkbook();
				resultSheetX = resultWorkBookX.createSheet("result");
				resultRowX = resultSheetX.createRow((short) 0);

				resultRowX.createCell(0).setCellValue(new XSSFRichTextString("Row"));
				resultRowX.createCell(1).setCellValue(new XSSFRichTextString("Column"));
				resultRowX.createCell(2).setCellValue(new XSSFRichTextString("Base Value <> Current Value"));


				diffCount++;
				try {
					resultFileOut = new FileOutputStream(resultReport);
					resultWorkBookX.write(resultFileOut);
					resultFileOut.close();
				} catch (Exception e) {

				}
			}
			else
			{
				resultWorkBookX = new HSSFWorkbook();
				resultSheetX = resultWorkBookX.createSheet("result");
				resultRowX = resultSheetX.createRow((short) 0);

				resultRowX.createCell(0).setCellValue(new HSSFRichTextString("Row"));
				resultRowX.createCell(1).setCellValue(new HSSFRichTextString("Column"));
				resultRowX.createCell(2).setCellValue(new HSSFRichTextString("Base Value <> Current Value"));


				diffCount++;
				try {
					resultFileOut = new FileOutputStream(resultReport);
					resultWorkBookX.write(resultFileOut);
					resultFileOut.close();
				} catch (Exception e) {

				}
			}
		}
		else
		{//SAF-1568
			if(resultReport.toUpperCase().endsWith(".XLSX"))
			{
				resultWorkBookX = new XSSFWorkbook();
			}
			else
			{
				resultWorkBookX = new HSSFWorkbook();
			}

			for(int i = resultWorkBookX.getNumberOfSheets() - 1; i >= 0 ; i--)
			{
				resultWorkBookX.removeSheetAt(i);
			}

			try 
			{
				resultFileOut = new FileOutputStream(resultReport);
				resultWorkBookX.write(resultFileOut);
				resultFileOut.close();

			} catch (Exception e) 
			{

			}
		}

		testCaseExecutionResult.setObject(baseFilePath);
		testCaseExecutionResult.setValid(true);
		return testCaseExecutionResult;
	}

	private void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);

			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

		} catch (Exception e) {

		}
	}
}
