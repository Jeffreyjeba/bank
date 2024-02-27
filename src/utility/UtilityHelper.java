package utility;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UtilityHelper{


	public static int findLengthString(String word) throws InputDefectException {								    	//1 findLength
		nullCheck(word);            			   
		return word.length();
	}


	public static void lengthIndexCheck(int length1,int length2) throws InputDefectException{				// exception support 
		if(length1<length2){
			throw new InputDefectException("The required index " +length2+ " is greater than the available index " +(length1-1));
		} 
		if(length2<0){
			throw new InputDefectException("Index value cannot be negative");
		}
	}

	public static void startStopCheck(int length1,int length2,int len) throws InputDefectException{				// exception support 
		if(length1>length2){
			throw new InputDefectException("The start cannot be greater than the end value");
		} 
		if(length2<0||length1<0){
			throw new InputDefectException("The start and end values cannot be negative");
		}
		if(length1>len){
			throw new InputDefectException("The start " +length1+ " cannot be greater than available index"  +(len-1));
		}
		if(length2>len){
			throw new InputDefectException("The end " +length2+ " cannot be greater than available index " +(len-1));
		}
	}

	public static void checkNegative(long number)throws InputDefectException {
		if(number<0) {
			throw new InputDefectException("The number cannot be negative");
		}
	}
	public static void checkTwo(int min,int max)throws InputDefectException {
		if(min>max){
			throw new InputDefectException("The start cannot be greater than the end value");
		} 	
	}


	public static void nullCheck(Object word) throws InputDefectException{											//exception  support 

		nullCheck(word,"The input object value cannot be null");
	}


	public static void nullCheck(Object word,String message ) throws InputDefectException{							//exception  support 
		if(word==null){
			throw new InputDefectException(message);
		}
	}

	public static void logSetter(String fileName,Boolean append,Logger logger) throws SecurityException, IOException {
		FileHandler handler= new FileHandler(fileName,append);
		Formatter formatter=new SimpleFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);
		logger.setLevel(Level.ALL);
	}
	
	public static String passHasher(String password) throws NoSuchAlgorithmException {
		MessageDigest digest=MessageDigest.getInstance("MD5");
		byte[] pass=digest.digest(password.getBytes());
		return byteToString(pass);
	}
	private static String byteToString(byte[] pass) {
		StringBuilder stringBuilder=new StringBuilder();
		for(byte byt:pass) {
			stringBuilder.append(String.format("%02x",byt));
		}
		return stringBuilder.toString();
	}


}

