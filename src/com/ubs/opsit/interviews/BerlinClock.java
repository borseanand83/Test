package com.ubs.opsit.interviews;

import com.ubs.opsit.interviews.exception.ApplicationException;
import com.ubs.opsit.interviews.model.BerlinClockVO;
import com.ubs.opsit.interviews.model.InputVO;

public interface BerlinClock {

	public InputVO parseInput(String input) throws ApplicationException;
	
	public BerlinClockVO computeTime(InputVO input) throws ApplicationException;
	
	public String showTime(BerlinClockVO output) throws ApplicationException;
}
