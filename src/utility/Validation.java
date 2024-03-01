package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	
	public static boolean validatePassword(CharSequence passWord) {
		return comparator("((?=.*[A-Z]{1,})(?=.*[a-z]{1,})(?=.*[0-9]{1,})(?=.*\\p{Punct}{1,})).{8,}", passWord);
	}
	public static boolean validateEmail(CharSequence emailId) {
		return comparator("^[^@\\.]{1}[^@]+[@]{1}[^@\\.]+[\\.]{1}[^@\\.]+$",emailId);
	}
	public static boolean validatePhoenNo(CharSequence phoneNumber) {
		return comparator("^[7-9]{1}[0-9]{9}$",phoneNumber);
	}
	public static boolean validateAlphaNume(CharSequence input) {
		return comparator("\\P{Alnum}", input);
	}
	public static boolean validateUserId(CharSequence userId) {
		return comparator("[0-9]{1,20}", userId);
	}
	public static boolean validateAccountNumber(CharSequence accountNumber) {
		return validateUserId(accountNumber);
	}
	public  static boolean validateAdharNumber(CharSequence aadharNumber) {
		return comparator("[0-9]{12}", aadharNumber);
	}
	public  static boolean validatePanNumber(CharSequence panNumber) {
		return comparator("^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$", panNumber);
	}
		
	
	private static boolean comparator(String regex,CharSequence subject) {
		Pattern pattern= Pattern.compile(regex);
		Matcher matcher=pattern.matcher(subject);
		return matcher.find();
	}
}
