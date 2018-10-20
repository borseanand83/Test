package com.test;

import java.util.Arrays;
import java.util.LinkedList;
public class CandidateCode 
{ 
	private static int startTime = 5;
	private static int endTime = 23;

	public static int jobMachine(String[] input1)
	{
		ProcessedVO[] parsedInputArr = parseAndConvertInput(input1);
		LinkedList<ProcessedVO> outputList = new LinkedList<CandidateCode.ProcessedVO>();
		//Form a list of entries at each hour that represent minimum occupied duration for that hour and store it in outputList linkedlist
		for(int i=5;i<24;i++){
			int interval = 18;
			for (ProcessedVO inputVO : parsedInputArr) {
				if(inputVO.getStartTime() >= startTime && inputVO.getEndTime() <= endTime){

					if(i == inputVO.getStartTime()){
						if(inputVO.getInterval() < interval){
							interval = inputVO.getInterval();
							outputList.add(inputVO);
						}

					}
					else if(i < inputVO.getStartTime())
						break;
				}	

			}

		}
		//iterate over the output list to remove the overlapping entries so that the end list will have only those entries that are not overlapped and yield maximum income
		for (int i = 0; i < outputList.size(); i++) {
			for (int k = i; k < outputList.size()-1; k++) {
				ProcessedVO entry1 = outputList.get(i);
				ProcessedVO entry2 = outputList.get(k+1);
				if(entry1.getEndTime() > entry2.getStartTime()){
					if(entry1.getInterval() < entry2.getInterval())	{
						outputList.remove(entry2);
					}
					else if(entry1.getInterval() >= entry2.getInterval()){
						outputList.remove(entry1);
					}
				}
				else
					break;
			}
		}

		for (ProcessedVO processedVO : outputList) {
			System.out.println(processedVO);
		}
		return outputList.size()*500;
	}


	/**
	 * This method parses the string input and produces a list of ProcessedVO's that can be further used for computation
	 * @param input1 is the original list that is passed as input
	 * @return ProcessedVO[]
	 */
	private static ProcessedVO[] parseAndConvertInput(String[] input1){
		ProcessedVO[] parsedInputArr = new ProcessedVO[input1.length];

		for (int i=0;i<input1.length;i++) {
			String input = input1[i];
			if(input!=null){
				ProcessedVO inputVO = new CandidateCode.ProcessedVO();
				inputVO.setStartTime(input.substring(0,input.indexOf("#")));
				inputVO.setEndTime(input.substring(input.indexOf("#")+1,input.length()));
				parsedInputArr[i] = inputVO;
			}
		}
		Arrays.sort(parsedInputArr);
		return parsedInputArr;
	}

	/**
	 * This is an interim class which represents a value object to hold the processed data.
	 *
	 */
	static class ProcessedVO implements Comparable<ProcessedVO>{
		private int startTime;
		private int endTime;

		public int getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			if(startTime.contains("AM"))
				this.startTime = Integer.valueOf(startTime.substring(0,startTime.indexOf("AM")));
			else if(startTime.contains("PM")){
				this.startTime = computeTime(startTime);
			}

		}

		public int getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			if(endTime.contains("AM"))
				this.endTime = Integer.valueOf(endTime.substring(0,endTime.indexOf("AM")));
			else if(endTime.contains("PM"))
			{
				this.endTime = computeTime(endTime);
			}
		}

		public int getInterval(){
			return (this.getEndTime() - this.getStartTime());
		}
		@Override
		public int compareTo(ProcessedVO o) {
			if(this.getStartTime() < o.getStartTime())
				return -1;
			else if(this.getStartTime() > o.getStartTime())
				return 1;
			return 0;
		}

		@Override
		public String toString() {
			return "startTime ::"+this.getStartTime() + " endTime::"+ this.getEndTime()+ " interval::"+this.getInterval();
		}

		private int computeTime(String time) {
			if(!time.contains("12"))
				return 12 + Integer.valueOf(time.substring(0,time.indexOf("PM")));
			else
				return Integer.valueOf(time.substring(0,time.indexOf("PM")));
		}
	}


	public static void main(String[] args) {
//		String[] input = {"6AM#8AM","11AM#1PM","7AM#3PM","7AM#10PM","10AM#12PM","2PM#4PM","1PM#4PM","8AM#9AM"/*,"3PM#5PM","3PM#4PM","2PM#3PM","12PM#6PM","5AM#11PM"*/};
		String[] input = {"5AM#6AM","6AM#7AM","7AM#8AM","8AM#9AM","9AM#10AM","11AM#12PM","12PM#1PM","1PM#2PM","2PM#3PM","3PM#4PM","4PM#5PM","5PM#6PM","6PM#7PM","7PM#8PM","8PM#9PM","9PM#10PM","10PM#11PM","2PM#4PM","2PM#6PM","7PM#11PM"};
		System.out.println(jobMachine(input));
	}
}