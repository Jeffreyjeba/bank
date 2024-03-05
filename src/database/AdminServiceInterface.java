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

	public JSONArray getTransactionHistory(JSONObject json) throws BankException;

	public void checkUserPresence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkUserAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkCustomerAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkCustomerPresence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkAccountAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkAccountPrecence(JSONObject json, String field) throws BankException, InputDefectException;
	
	// Employee operations
	
	public void addUsers(JSONObject customer) throws BankException, InputDefectException;

	public void addCustomers(JSONObject customer) throws BankException, InputDefectException;

	public void createAccount(JSONObject account) throws BankException, InputDefectException;

	public void deleteAccount(JSONObject account) throws BankException, InputDefectException;

	public void deactivateAccount(JSONObject account) throws BankException, InputDefectException;

	public void activateAccount(JSONObject account) throws BankException, InputDefectException;
	
	// ADMIN operations
	
	public void createBranch(JSONObject branch) throws BankException, InputDefectException;

	public void addAdmin(JSONObject admin) throws BankException, InputDefectException;

	public void addEmployee(JSONObject employee) throws BankException, InputDefectException;

	public void removeEmployee(JSONObject employee) throws BankException, InputDefectException;

	public JSONArray getAllBranchId() throws BankException;
	
	public void checkBranchAbsence(JSONObject json, String field) throws BankException, InputDefectException ;

	public void checkBranchPrecence(JSONObject json, String field) throws BankException, InputDefectException ;
	
	public void checkEmployeeAbsence(JSONObject json, String field) throws BankException, InputDefectException ;

	public void checkEmployeePrecence(JSONObject json, String field) throws BankException, InputDefectException ;

}
