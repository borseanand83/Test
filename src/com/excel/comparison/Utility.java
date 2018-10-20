package com.ktt.model.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ktt.model.config.InitSAFALProperties;
import com.ktt.view.config.KTTGuiConstants;


public class Utility {

	private static Logger logger = Logger.getLogger(Utility.class.getName());
	
	public static Integer isNumber(String str)
	{
		try 
		{
			return Integer.parseInt(str.trim());
			
		} catch (NumberFormatException e) 
		{
			return null;
		}
	}
	
	public static long parseLong(String str){

		if(str == null || str.trim().equals("")){

			return 0;
		}

		return Long.parseLong(str);

	}

	public static String toString(Object str){

		if(str == null){

			return "";
		}

		return str.toString();

	}
	
	public static Calendar increamentDateValidation(Calendar cal) {

		cal.setTimeInMillis(cal.getTimeInMillis() + (24 * 60 * 60 * 1000));				

		return cal;
	}

	public static int parseInt(String str){

		if(str == null || str.trim().equals("")){

			return 0;
		}

		return Integer.parseInt(str);

	}

	public static boolean parseBoolean(String str){

		if(str == null || str.trim().isEmpty() || str.trim().equalsIgnoreCase("false")){

			return false;
		}

		return true;

	}

	/**
	 * This method will convert bytes to Hex format.
	 * @param bytes
	 * @param max
	 * @return
	 */
	public static String bytesToHex(byte[] bytes, int max) {

		StringBuffer buffer = new StringBuffer();

		for (int i=0; i<bytes.length && i<max; i++) {

			buffer.append(Integer.toHexString(bytes[i] & 0xFF)); 
		}

		return buffer.toString().toUpperCase();
	}
	
	/**
	 * This function generates a -ne random number and returns it.
	 * 
	 * @return long randonId
	 */
	public static long getRandomId(){
		
		//generate random id for new tables to identify them while saving
		long randonId = new Random().nextLong();

		if(!(randonId < 0)){

			randonId *= -1L;
		}		
		
		return randonId;
	}		

	public static String getDateAsString(Calendar modifiedDateCal)	
	{
		String systemDateAsString = "";
		//"yyyy-MM-dd hh:mm:ss"
		
		try 
		{
			Calendar cal = null;
			
			if(modifiedDateCal != null)
			{
				cal = modifiedDateCal;
				
			}else
			{
				cal = Calendar.getInstance();
			}
						
			int y =cal.get(Calendar.YEAR);
			int m=cal.get(Calendar.MONTH) + 1;
			int d=cal.get(Calendar.DATE);
			int hr=cal.get(Calendar.HOUR_OF_DAY);
			int min=cal.get(Calendar.MINUTE);
			int sec=cal.get(Calendar.SECOND);

			systemDateAsString = y+"-"+m+"-"+d+" "+hr+":"+min+":"+sec;
						
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return systemDateAsString;//"yyyy-MM-dd hh:mm:ss"
	}
	
	public static String getDateAsStringInDDMMYYYYFormat(Calendar modifiedDateCal)	
	{
		String systemDateAsString = "";
		
		try 
		{
			Calendar cal = null;
			
			if(modifiedDateCal != null)
			{
				cal = modifiedDateCal;
				
			}else
			{
				cal = Calendar.getInstance();
			}
						
			int y =cal.get(Calendar.YEAR);
			int m=cal.get(Calendar.MONTH) + 1;
			int d=cal.get(Calendar.DATE);
			int hr=cal.get(Calendar.HOUR);
			int min=cal.get(Calendar.MINUTE);
			
			String ampm = "";
			
			if(cal.get(Calendar.AM_PM) == 1)
			{
				if(hr == 0)
				{
					hr = 12;
				}

				ampm = "PM";
			}
			else
			{
				ampm = "AM";
			}
			
			if(hr == 0 && min == 0)
			{
				systemDateAsString = d+"-"+m+"-"+y;
			}
			else
			{
				systemDateAsString = d+"-"+m+"-"+y+" "+hr+":"+min+" "+ampm;
			}
						
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return systemDateAsString;
	}
	
	public static String getDateAsStringAsParsing(String modifiedDateStr)	
	{
		String systemDateAsString = "";
		//"yyyy-MM-dd hh:mm:ss"
		
		try 
		{
			Calendar cal = null;
			
			if(modifiedDateStr != null && !modifiedDateStr.trim().equals(""))
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

				cal = Calendar.getInstance();
				Date date = simpleDateFormat.parse(modifiedDateStr);

				cal.setTime(date);

				int y =cal.get(Calendar.YEAR);
				int m=cal.get(Calendar.MONTH) + 1;
				int d=cal.get(Calendar.DATE);
				int hr=cal.get(Calendar.HOUR_OF_DAY);
				int min=cal.get(Calendar.MINUTE);
				int sec=cal.get(Calendar.SECOND);

				systemDateAsString = y+"-"+m+"-"+d+" "+hr+":"+min+":"+sec;
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return systemDateAsString;//"yyyy-MM-dd hh:mm:ss"
	}
	
	public static Calendar getDateAsCalendar(String modifiedDateStr)	
	{
		Calendar cal = null;
		
		try 
		{
			if(modifiedDateStr != null && !modifiedDateStr.trim().equals(""))
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				cal = Calendar.getInstance();
				Date date = simpleDateFormat.parse(modifiedDateStr);

				cal.setTime(date);
			}
		} catch (Exception e) 
		{
			try
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

				cal = Calendar.getInstance();
				Date date = simpleDateFormat.parse(modifiedDateStr);

				cal.setTime(date);

			} catch (Exception e1) 
			{
				try
				{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");

					cal = Calendar.getInstance();
					Date date = simpleDateFormat.parse(modifiedDateStr);

					cal.setTime(date);

				} catch (Exception e2) 
				{
					e2.printStackTrace();
				}
			}
		}
		
		return cal;
	}
	
	public static Calendar getDateAsCalendar(String modifiedDateStr, String dateFormatRequired)	
	{
		Calendar cal = null;
		
		try 
		{
			if(modifiedDateStr != null && !modifiedDateStr.trim().equals(""))
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatRequired);

				cal = Calendar.getInstance();
				Date date = simpleDateFormat.parse(modifiedDateStr);

				cal.setTime(date);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return cal;
	}

	public static boolean isIPAddress(String str)
	{
		try 
		{
			if(str == null || str.trim().isEmpty())
			{
				return false;
			}
			
			String arr[] = str.split("\\.");
			
			if(arr.length == 4)//there should be exactly 4 sets 
			{
				for(int a = 0; a < arr.length; a++)
				{
					String aSet = arr[a];
					int aSetInt = Integer.parseInt(aSet);
					
					if(aSetInt < 0 || aSetInt > 255)
					{
						return false;
					}
				}
				
				return true;
			}
		} catch (NumberFormatException e) 
		{		
			return false;
		}
		
		return false;
	}
	
	public static String getCellValue(org.apache.poi.ss.usermodel.Cell cell) 
	{
		logger.trace("Entering method String getCellValue(Cell cell) " + cell);

		String value = "";
		
		if (cell != null) 
		{
			int cellType = cell.getCellType();

			switch (cellType) 
			{
			
			case Cell.CELL_TYPE_STRING:
				
				value = handleStringCase(cell.getRichStringCellValue().toString());
				
				if (value.contains("password")) 
				{
					// value = encode(value);
				}
				
				break;
				
			case Cell.CELL_TYPE_NUMERIC:

				if (HSSFDateUtil.isCellDateFormatted(cell))
				{
					Date date2 = cell.getDateCellValue();
					String dateFmt = cell.getCellStyle().getDataFormatString();
					value = new CellDateFormatter(dateFmt).format(date2);
				}else
				{
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					value = cell.getStringCellValue();
				}

				break;
				
			case Cell.CELL_TYPE_BOOLEAN:
				
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				value = cell.getStringCellValue();
				break;
				
			case Cell.CELL_TYPE_BLANK:
				
				value = "";
				break;

			default:
				
				return value;
			}
		} else 
		{
			return value;
		}

		logger.trace("Exiting method String getCellValue(Cell cell) " + cell);
		return value;
	}
	
	public static String handleStringCase(String cellString) {


		//commented for SAF-341
		/*if (cellString.contains("'")) {
			cellString = cellString.replaceAll("'", "");
		}*/

		return cellString;
	}

	public static String generateReportName(String workBookName, String sheetName, boolean isCalledFromSaveOnlyFailedResult, String extension)
	{
		InitSAFALProperties initSAFALProperties = InitSAFALProperties.getInstance();
		String resultStorePath = initSAFALProperties.resultStorePath;
		
		if(resultStorePath == null || resultStorePath.isEmpty())
		{
			resultStorePath = KTTGuiConstants.RESULT_DIRECTORY_PATH;
		}
		
		if(!resultStorePath.endsWith("/"))
		{
			resultStorePath += "/";
		}
		
		if(!new File(resultStorePath).exists())
		{
			new File(resultStorePath).mkdir();
		}
		
		String reportName = "";
		
		long timeInMiliSec = System.currentTimeMillis();
		
		String timeStamp = getDateStr(timeInMiliSec) + "_" + getTimeStr(timeInMiliSec);

		if(workBookName == null)
		{
			workBookName="(DB_MODE)." + extension;
		}

		if(workBookName.contains("\\") && workBookName.contains("."))
		{
			int start = workBookName.lastIndexOf("\\");
			int end = workBookName.lastIndexOf(".");
			workBookName = workBookName.substring(start + 1, end);
		}
		
		/*SAF-1342  Result file not generated under Results folder
		 * if sheetName/workBookName contains '/' or '//', it will create wrong path
		 * so need to remove '/'*/
		sheetName = sheetName.replaceAll("/", "");
		sheetName = sheetName.replaceAll("//", "");
		workBookName = workBookName.replaceAll("/", "");
		workBookName = workBookName.replaceAll("/", "");

		reportName = resultStorePath + "SAFAL_ResultSummary_" + workBookName + "_"+ sheetName + "_" + timeStamp + "." + extension;

		if(isCalledFromSaveOnlyFailedResult)
		{
			reportName = resultStorePath + "SAFAL_ResultSummary_Only_Failed_" + workBookName + "_"+ sheetName + "_" + timeStamp + "." + extension;				
		}
		
		return reportName;
	}
	
	public static String getDateStr(long timeInMiliSec)
	{
		String dateStr = "";
		
		Date date = null;
		
		if(timeInMiliSec == 0)
		{
			date = new Date(System.currentTimeMillis());
		}
		else
		{
			date = new Date(timeInMiliSec);
		}
		
		String[] datArr = date.toString().split(" ");
		
		dateStr = datArr[0] + "_" + datArr[1] + "_" + datArr[2] + "_" + datArr[5];
		
		return dateStr;
	}
	
	public static String getDateStr1(long timeInMiliSec)
	{
		String dateStr = "";
		
		Date date = null;
		
		if(timeInMiliSec == 0)
		{
			date = new Date(System.currentTimeMillis());
		}
		else
		{
			date = new Date(timeInMiliSec);
		}
		
		String[] datArr = date.toString().split(" ");
		
		dateStr = datArr[0] + "," + datArr[1] + "/" + datArr[2] + "/" + datArr[5];
		
		return dateStr;
	}
	
	public static String getTimeStr(long timeInMiliSec)
	{
		String timeStr = "";
		
		Date date = null;
		
		if(timeInMiliSec == 0)
		{
			date = new Date(System.currentTimeMillis());
		}
		else
		{
			date = new Date(timeInMiliSec);
		}
		
		String[] datArr = date.toString().split(" ");
		String[] time = datArr[3].split(":");
		
		timeStr = time[0] + "_" + time[1] + "_" + time[2];
		
		return timeStr;
	}
	
	/**
	 * SAF-1887
	 * This function will pass environment param IgnoreRow.
	 * 
	 * @param ignoreRowValue
	 * @return
	 */
	public static Map<Integer, List<Integer>> parseIgnoreRowValue(String ignoreRowValue)
	{
		//possible entries:
		//13=2,3,4;14=4,5,6;16=1,2,3;20=1
		//13
		//13;14;16;20

		//<Row number = <Cell numbers, Cell numbers, Cell numbers>, Row number = <Cell numbers, Cell numbers, Cell numbers> ...>
		Map<Integer, List<Integer>> ignoreRowMap = new HashMap<Integer, List<Integer>>();
		
		if(ignoreRowValue != null && !ignoreRowValue.isEmpty())
		{
			Integer rowNumberToBeIgnored = Utility.isNumber(ignoreRowValue);

			if(rowNumberToBeIgnored == null)
			{
				String[] ignoreRowValueArr = ignoreRowValue.split(";");

				for(int r = 0; r < ignoreRowValueArr.length; r++)
				{
					String tempIgnoreRowValue = ignoreRowValueArr[r];

					if(tempIgnoreRowValue != null && !tempIgnoreRowValue.isEmpty())
					{
						Integer rowNumberToBeIgnored2 = Utility.isNumber(tempIgnoreRowValue);

						if(rowNumberToBeIgnored2 == null)
						{
							String[] ignoreRowCellArr = tempIgnoreRowValue.split("=");

							if(ignoreRowCellArr.length == 2)
							{
								String ignoreRowNumberStr = ignoreRowCellArr[0];
								String ignoreRowCellStr = ignoreRowCellArr[1];

								Integer rowNumberToBeIgnored3 = Utility.isNumber(ignoreRowNumberStr);

								if(rowNumberToBeIgnored3 != null)
								{
									List<Integer> cellNumberList = new ArrayList<Integer>();
									
									String[] ignoreRowCellValueArr = ignoreRowCellStr.split(",");

									for(int c = 0; c < ignoreRowCellValueArr.length; c++)
									{
										String ignoreRowCellValue = ignoreRowCellValueArr[c];
										
										Integer cellNumberToBeIgnored = Utility.isNumber(ignoreRowCellValue);
										
										if(cellNumberToBeIgnored == null)
										{
											//error parsing cell number
											ignoreRowMap.put(-1, null);
										}
										else
										{
											cellNumberList.add(cellNumberToBeIgnored);
										}
									}
									
									putIntoIgnoreRowMap(rowNumberToBeIgnored3, ignoreRowMap, cellNumberList);
								}
								else
								{
									//error parsing row number
									ignoreRowMap.put(-2, null);
								}
							}
							else
							{
								//error parsing row number
								ignoreRowMap.put(-3, null);
							}
						}
						else
						{
							putIntoIgnoreRowMap(rowNumberToBeIgnored2, ignoreRowMap, null);
						}
					}
				}
			}
			else
			{
				putIntoIgnoreRowMap(rowNumberToBeIgnored, ignoreRowMap, null);
			}
		}
		
		return ignoreRowMap;
	}
	
	public static void putIntoIgnoreRowMap(Integer rowNumberToBeIgnored, Map<Integer, List<Integer>> ignoreRowMap, List<Integer> cellNumberList)
	{
		if(rowNumberToBeIgnored >= 0)
		{
			ignoreRowMap.put(rowNumberToBeIgnored, cellNumberList);
		}
		else
		{
			//negative row number is not permitted
			ignoreRowMap.put(-4, null);
		}
	}

	public static void createXMLFromMap(Map<String, String> configMap, String parentPath, String scriptName)
	{
		try 
		{
			//create an xml file from environment data
			if(configMap != null && !configMap.isEmpty())
			{
				DocumentBuilder builder = null;  
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
				Document document = builder.newDocument();  

				Element root = document.createElement("Environment");  
				
				for(String key : configMap.keySet())
				{  
					String value = configMap.get(key);  

					Element newVariableNode = document.createElement("Variable");  

					Element newKey = document.createElement("Name");  
					Element newValue = document.createElement("Value");  

					newKey.appendChild(document.createTextNode(key));
					newValue.appendChild(document.createTextNode(value));

					//newKey.setTextContent(key);  
					//newValue.setTextContent(value.toString());  

					newVariableNode.appendChild(newKey);  
					newVariableNode.appendChild(newValue);  

					root.appendChild(newVariableNode);  
				}
				
				document.appendChild(root);  

				Transformer transformer = TransformerFactory.newInstance().newTransformer();  
				Source source = new DOMSource(document);  
				
				String path = parentPath + "/" + scriptName + "_Environment_Data.xml";
				int instance = 1; 
				
				while(new File(path).exists())
				{
					path = parentPath + "/" + scriptName + "_Environment_Data_"+instance+".xml";
					
					instance++;
				}
				
				File file = new File(path);  
				Result result = new StreamResult(file.getPath());  
				transformer.transform(source,result);  
			}
			
		} catch (DOMException e)
		{
			e.printStackTrace();
		} catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
		} catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
		} catch (TransformerException e) 
		{
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void createORXMLMap(Map<Long, Map<String, String>> objectMap, Map<Long, String> pageMap, String filePath)
	{
		try 
		{
			//create an xml file from OR data
			if(objectMap != null && !objectMap.isEmpty())
			{
				DocumentBuilder builder = null;  
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
				Document document = builder.newDocument();  

				Element root = document.createElement("ObjectMap");  
				
				Set<Long> keyset1 = objectMap.keySet();
				Iterator<Long> itr1 = keyset1.iterator();
				
				while(itr1.hasNext())
				{
					Long pageId = itr1.next();
					String pageName = pageMap.get(pageId);
					
					if(pageId != null)
					{
						Element pageElement = document.createElement("Page");  
						pageElement.setTextContent(pageName);  
						
						Map<String, String> objectDetailsMap = objectMap.get(pageId);
						
						Set<String> keyset = objectDetailsMap.keySet();
						Iterator<String> itr = keyset.iterator();
						
						while(itr.hasNext())
						{
							String logicalName = itr.next();
							String locator = objectDetailsMap.get(logicalName);
							
							Element objectElement = document.createElement("Object");  
							
							Element logicalNameElement = document.createElement("Logical");  
							Element locatorElement = document.createElement("Locator");  
							Element tagElement = document.createElement("Tag");  

							logicalNameElement.setTextContent(logicalName);  
							locatorElement.setTextContent(locator);  
							tagElement.setTextContent("");  

							objectElement.appendChild(logicalNameElement);  
							objectElement.appendChild(locatorElement);  
							objectElement.appendChild(tagElement);  

							pageElement.appendChild(objectElement);  
						}
						
						root.appendChild(pageElement);  
					}
				}
								
				document.appendChild(root);  

				Transformer transformer = TransformerFactory.newInstance().newTransformer();  
				Source source = new DOMSource(document);  
				File file = new File(filePath);  
				Result result = new StreamResult(file.getPath());  
				transformer.transform(source,result);  
			}
			
		} catch (DOMException e)
		{
			e.printStackTrace();
		} catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
		} catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
		} catch (TransformerException e) 
		{
			e.printStackTrace();
		}
	}

	public static Calendar parseDate(String dateStr)
	{
		Date date = null;
		
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM/dd/yyyy hh:mm:ss");		//Mon,Jan/30/2012 15:39:11	
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEE_MMM_dd_yyyy hh_mm_ss");   //Fri_Dec_13_2013 11_57_53 
		SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("hh_mm_ss");   //11_57_53 
		SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("hh:mm:ss");		//15:39:11	
		SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("dd-mm-yyyy hh:mm a");	//17-12-2013 6:22 PM
		SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");	//17-12-2013 6:22:55
		SimpleDateFormat simpleDateFormat7 = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss a");	//17-12-2013 6:22:55 PM
		try
		{
			date = simpleDateFormat1.parse(dateStr);
		}
		catch(Exception e)
		{
			try 
			{
				date = simpleDateFormat2.parse(dateStr);
			} catch (ParseException e1) 
			{
				try 
				{
					date = simpleDateFormat3.parse(dateStr);
				} catch (ParseException e2) 
				{
					try 
					{
						date = simpleDateFormat4.parse(dateStr);
					} catch (ParseException e3) 
					{
						try 
						{
							date = simpleDateFormat5.parse(dateStr);
						} catch (ParseException e4) 
						{
							try 
							{
								date = simpleDateFormat6.parse(dateStr);
							} catch (ParseException e5) 
							{
								try 
								{
									date = simpleDateFormat7.parse(dateStr);
								} catch (ParseException e6) 
								{
								}
							}
						}
					}
				}
			}
		}
		
		Calendar cal = Calendar.getInstance();
		
		if(date != null)
		{
			cal.setTime(date);
		}
		
		return cal;
	}
	
	public static Calendar merge2TimeStrForTimeOnly(String fullDateStr, String onlyTimeStr)
	{
		if(onlyTimeStr != null && !onlyTimeStr.isEmpty())
		{
			Calendar finalCal = parseDate(fullDateStr);
			
			Calendar onlyTimeCal = parseDate(onlyTimeStr);

			int hour = onlyTimeCal.get(Calendar.HOUR);
			int min = onlyTimeCal.get(Calendar.MINUTE);
			int sec = onlyTimeCal.get(Calendar.SECOND);

			finalCal.set(Calendar.HOUR, hour);
			finalCal.set(Calendar.MINUTE, min);
			finalCal.set(Calendar.SECOND, sec);
			
			return finalCal;
		}
		
		return null;
	}

}