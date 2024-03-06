package database;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.BankException;
import utility.InputDefectException;

public interface EmployeeServiceInterface {
	
	// customer operations
	
	public JSONObject getBalance(JSONObject json) throws BankException;

	public JSONArray getAccounts(JSONObject json) throws BankException;

	public void resetPassword(JSONObject json) throws BankException;

	public JSONObject accountStatus(JSONObject json) throws BankException;

	public void modifyMoney(JSONObject json) throws BankException;

	public void putHistory(JSONObject json) throws BankException;

	public JSONArray getTransactionHistory(JSONObject json,int quantity ,int page,long searchMilli) throws BankException;

	public void checkUserPresence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkUserAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkCustomerAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkCustomerPresence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkAccountAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkAccountPrecence(JSONObject json, String field) throws BankException, InputDefectException;

	// employee operations
	
	public void addUsers(JSONObject customer) throws BankException, InputDefectException;

	public void addCustomers(JSONObject customer) throws BankException, InputDefectException;

	public void createAccount(JSONObject account) throws BankException, InputDefectException;

	public void deleteAccount(JSONObject account) throws BankException, InputDefectException;

	public void deactivateAccount(JSONObject account) throws BankException, InputDefectException;

	public void activateAccount(JSONObject account) throws BankException, InputDefectException;

	public JSONObject getBranch(long empId) throws BankException ;
	
	// public JSONObject viewProfile(JSONObject json) throws BankException;
}
