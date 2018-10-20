package com.test;

public enum PayrollDay {
	MONDAY(PayType.WEEKDAY),
	TUESDAY(PayType.WEEKDAY),
	WEDNESDAY(PayType.WEEKDAY),
	THURSDAY(PayType.WEEKDAY),
	FRIDAY(PayType.WEEKDAY),
	SATURDAY(PayType.WEEKEND),
	SUNDAY(PayType.WEEKEND);

	private final PayType paytype;
	private PayrollDay(PayType paytype) {
		this.paytype = paytype;
	}
	
	double pay(double hoursWorked, double payrate){
		return paytype.pay(hoursWorked, payrate);
	}
	private enum PayType{
		
		WEEKDAY{
			double overtimePay(double hours, double payrate){
				return hours < SHIFT_HOURS_PER_DAY?0:(hours-SHIFT_HOURS_PER_DAY) * payrate;
			};
		},
		WEEKEND{
			double overtimePay(double hours, double payrate){
				return hours < SHIFT_HOURS_PER_DAY?0:(hours-SHIFT_HOURS_PER_DAY) * payrate/2;
			};
			
		};
		private static final int SHIFT_HOURS_PER_DAY = 8;
		
		abstract double overtimePay(double hours, double payrate);
		
		double pay(double hoursWorked,double payrate){
			double basepay = hoursWorked * payrate;
			return basepay + overtimePay(hoursWorked, payrate);
		}
	}
	

	public static void main(String[] args) {
		System.out.println("payday monday = "+PayrollDay.MONDAY.pay(10, 40));
	}
}


