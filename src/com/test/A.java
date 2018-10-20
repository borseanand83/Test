package com.test;

public class A {

	public String a="xyz";
	
	static void print(){
		System.out.println("xyz");
		
		A a;
//		a.method();
	}
	
	private static void method(){
		System.out.println("fdsfsd");
	}
	
public static void main(String[] args) {
//	A a = new A();
//	a.print();
	double num = 0.0008086423141035456;
	String str = "agsdgfds";

//	File f = new File("/Users/aborse/Desktop/test.txt");
//	Path ifile = FileSystems.getDefault().getPath(f.getParent(), f.getName());
//	try {
//		List<String> lines  = Files.readAllLines(ifile, Charset.forName("UTF-8"));
//		System.out.println(lines.get(5));
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	String filename = "Mirroring $ iOS Devices !@#$%^&*()_+to a Solstice Display with Apple AirPlay®";
	String filename2 = "WHERE IS 1I2.02";
	filename = filename.replaceAll("[^A-Za-z0-9+-@|!#%*()_\\$&\\^\\s]","");
	filename = filename2.replaceAll("!^[A-Za-z]{1,2}B?\\d(\\.\\d{2,3}){1,2}","LECTURE THEATRE");
	
//	Regex.Replace(filename, "([ a-zA-Z0-9&, _]|^\\s)", "");
	System.out.println(filename2);
	String test = "Mirroring iOS Devices to a Solstice Display with Apple AirPlayÂ®";
	try {
		System.out.println(truncate("1234567890123456789012345678901", 25));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
//	
	
//	String line = "\"ukjgfdsg,jkjhgk\njdhfg\",\"abcdtrue\",\"positive\"";
//	String newline = line.replaceAll("\n","123");
//	System.out.println(newline);
//	String[] arr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//	System.out.println(arr[0]+arr[1]+arr[2]);	
//	System.out.println(Double.valueOf("0.0008086423141035456"));
//	System.out.printf("num : %f\n",num);
//	String scientificNotation = "8.175565104208233E-4";
//	String scientificNotation2 = "0.008086423141035456";
//	Double scientificDouble = Double.parseDouble(scientificNotation2);
//	NumberFormat nf = new DecimalFormat("################################################.###########################################");
//	System.out.println(nf.format(scientificDouble));
}

 public void func1() throws Exception{
	System.out.println("A func1");
}
 
public static String truncate(String str,int numbeOfCharsAllowed) throws Exception{
	String placeholder = "(truncated)";
	if(numbeOfCharsAllowed > str.length())
		throw new Exception("The String to be replaced should be greater than the number of characters allowed");
	int numberOfDotsNeeded = numbeOfCharsAllowed - placeholder.length();
	if(numberOfDotsNeeded > 0){
		int prefixDots , postfixDots = 0;
		if(numberOfDotsNeeded%2 ==0){
			prefixDots = postfixDots = numberOfDotsNeeded/2 ;
		}
		else{
			prefixDots = numberOfDotsNeeded/2;
			postfixDots = numberOfDotsNeeded - prefixDots-1;
		}
		String dotString = "";
		for(int i = 0;i <=prefixDots-1; i++){
			dotString+= ".";
		}
		placeholder=dotString+placeholder;
		dotString = "";
		for(int i = 0;i <=postfixDots-1; i++){
			dotString+= ".";
		}
		placeholder=placeholder+dotString;
	}
	int difference = str.length() - placeholder.length();
	String finalString = "";
	String prefixFinalString = str.substring(0, difference/2);
	finalString = prefixFinalString+placeholder;
	String postFixString="";
	if(difference%2==0)
		postFixString = str.substring(finalString.length(), str.length());
	else
		postFixString = str.substring(finalString.length()+1, str.length());
	finalString = finalString+postFixString;
	return finalString;
}
}

