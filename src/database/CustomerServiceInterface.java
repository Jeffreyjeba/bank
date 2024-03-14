package database;

import org.json.JSONArray;
import org.json.JSONObject;

import pojo.TransactionHistory;
import utility.BankException;
import utility.InputDefectException;


public interface CustomerServiceInterface {
	
	public JSONObject getBalance(long accountNumber) throws BankException;

	public JSONArray getAccounts(long id) throws BankException;

	public void resetPassword(long id,String password) throws BankException;

	public JSONObject accountStatus(long accountNumber) throws BankException;

	//public void modifyMoney(long accountNumber,long balance) throws BankException;

	//public void putHistory(TransactionHistory history) throws BankException;

	public JSONArray getTransactionHistory(long accountNumber,int quantity ,int page,long searchMilli) throws BankException;

	public void checkUserPresence(long value, String field) throws BankException, InputDefectException;

	public void checkUserAbsence(long value, String field) throws BankException, InputDefectException;

	public void checkCustomerAbsence(long value, String field) throws BankException, InputDefectException;

	public void checkCustomerPresence(long value, String field) throws BankException, InputDefectException;

	public void checkAccountAbsence(long value, String field) throws BankException, InputDefectException;

	public void checkAccountPresence(long value, String field) throws BankException, InputDefectException;
	
	public int pageCount(long accountNumber,int quantity,long searchMilli) throws BankException ;
	
	public JSONObject viewProfile(long id) throws BankException;
	
	public JSONObject getPrimaryAccount(long id) throws BankException;
	
	public void setPrimaryAccount(long accountNumber) throws BankException;	
	
	public void removePrimaryAccount(long accountNumber) throws BankException;
	
	public void inBank(TransactionHistory historySender,TransactionHistory historyReceiver) throws BankException;
	
	public void creditDebitOutBank(TransactionHistory history) throws BankException;
	
}