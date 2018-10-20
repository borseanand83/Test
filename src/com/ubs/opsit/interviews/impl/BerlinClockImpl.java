package com.ubs.opsit.interviews.impl;

import java.util.StringTokenizer;

import com.ubs.opsit.interviews.BerlinClock;
import com.ubs.opsit.interviews.exception.ApplicationException;
import com.ubs.opsit.interviews.model.BerlinClockVO;
import com.ubs.opsit.interviews.model.InputVO;

public class BerlinClockImpl implements BerlinClock {

	
	@Override
	public InputVO parseInput(String input) throws ApplicationException {
		StringTokenizer tokennizer = new StringTokenizer(input,":");
		if(tokennizer.countTokens()!=3)
			throw new ApplicationException("input not valid");
		
		int hours = Integer.parseInt(tokennizer.nextToken());
		int mins = Integer.parseInt(tokennizer.nextToken());
		int seconds = Integer.parseInt(tokennizer.nextToken());
		InputVO inputModel = new InputVO();
		inputModel.setHours(hours);
		inputModel.setMinutes(mins);
		inputModel.setSeconds(seconds);
		return inputModel;
	}

	@Override
	public BerlinClockVO computeTime(InputVO input) throws ApplicationException {
		BerlinClockVO outputVo = new BerlinClockVO();
		outputVo.setHour1(getLightStatusForRow1(input.getHours()));
		outputVo.setHour2(getLightStatusForRow2(input.getHours()));
		outputVo.setMinutes1(getLightStatusForRow1(input.getMinutes()));
		outputVo.setMinutes2(getLightStatusForRow2(input.getMinutes()));
		outputVo.setSecs(getTopLightStatus(input.getSeconds()));
		return outputVo;
	}

	@Override
	public String showTime(BerlinClockVO output) throws ApplicationException {
		String outputString = "";
		outputString+=getSecondsLightOutput(output.getSecs());
		outputString+=getHoursRowOutput(output.getHour1());
		outputString+="\r\n";
		outputString+=getHoursRowOutput(output.getHour2());
		outputString+="\r\n";
		outputString+=getMinutesRowOutput(output.getMinutes1(),11);
		outputString+="\r\n";
		outputString+=getMinutesRowOutput(output.getMinutes2(),4);
		System.out.println(outputString);
		return outputString;
	}
	
	
	public String showTime2(BerlinClockVO output) throws ApplicationException {
		String outputString = "";
		
		return outputString;
	}
	private String getSecondsLightOutput(int seconds){
		String outputString="";
		if(seconds>0)
			outputString+="O\r\n";
		else
			outputString+="Y\r\n";
		return outputString;
	}
	
	private String getHoursRowOutput(int hours){
		int counter =0;
		String outputString="";
		for(counter=0;counter<hours;counter++){
			outputString+="R";
		}
		if(counter == hours && counter < 4)
		{
			while(counter < 4)
			{
				outputString+="O";
				counter++;
			}
		}
		return outputString;
	}
	
	private String getMinutesRowOutput(int mins,int maxMins){
		int counter =0;
		String outputString="";
		for(counter=0;counter<mins;counter++){
			
			if(maxMins > 4 && counter> 0 && (counter+1)%3 ==0)
				outputString+="R";
			else
				outputString+="Y";
		}
		if(counter == mins && counter < maxMins)
			while(counter < maxMins)
			{
				outputString+="O";
				counter++;
			}
		return outputString;
	}
	
	private int getLightStatusForRow1(int number){
		return number/5;
	}

	private int getLightStatusForRow2(int number){
		return number%5;
	}

	private int getTopLightStatus(int seconds){
		return seconds%2;
	}

	public static void main(String[] args) {
		BerlinClockImpl impl = new BerlinClockImpl();
		try {
			InputVO input = impl.parseInput("23:59:59");
			BerlinClockVO output = impl.computeTime(input);
			impl.showTime(output);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
