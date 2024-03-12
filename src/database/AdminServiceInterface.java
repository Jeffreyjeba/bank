package database;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.BankException;
import utility.InputDefectException;

public interface AdminServiceInterface {
	
	// Customer operations
	
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

	public void checkAccountPresence(JSONObject json, String field) throws BankException, InputDefectException;
	
	public int pageCount(JSONObject json,int quantity,long searchMilli) throws BankException ;
	
	public JSONObject viewProfile(JSONObject json) throws BankException;
	
	public JSONObject getPrimaryAccount(JSONObject json) throws BankException;
	
	public void setPrimaryAccount(JSONObject json) throws BankException;	
	
	public void removePrimaryAccount(JSONObject json) throws BankException;
	
	// employee operations
	
	public void addUsers(JSONObject customer) throws BankException;

	public void addCustomers(JSONObject customer) throws BankException;

	public void createAccount(JSONObject account) throws BankException;

	public void deleteAccount(JSONObject account) throws BankException;

	public void deactivateAccount(JSONObject account) throws BankException;

	public void activateAccount(JSONObject account) throws BankException;

	public JSONObject getBranch(long empId) throws BankException ;
	
	public void activateCustomer(JSONObject customer) throws BankException ;
	
	public void deactivateCustomer(JSONObject customer) throws BankException ;
	
	// ADMIN operations
	
	public void createBranch(JSONObject branch) throws BankException;

	public void addAdmin(JSONObject admin) throws BankException;

	public void addEmployee(JSONObject employee) throws BankException;

	public void removeEmployee(JSONObject employee) throws BankException;

	public JSONArray getAllBranchId() throws BankException;
	
	public void checkBranchAbsence(JSONObject json, String field) throws BankException, InputDefectException ;

	public void checkBranchPrecence(JSONObject json, String field) throws BankException, InputDefectException ;
	
	public void checkEmployeeAbsence(JSONObject json, String field) throws BankException, InputDefectException ;

	public void checkEmployeePrecence(JSONObject json, String field) throws BankException, InputDefectException ;

}
