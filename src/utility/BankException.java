package utility;

@SuppressWarnings("serial")
public class BankException extends Exception {
	public BankException(String errorMessage) {
		super(errorMessage);
	}
	
	public BankException(String errorMessage,Throwable cause) {
		super(errorMessage);
		initCause(cause);
	}
}
