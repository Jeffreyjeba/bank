package database;

import org.json.JSONArray;
import org.json.JSONObject;

import pojo.Accounts;
import pojo.Branch;
import pojo.Customers;
import pojo.Employees;
import pojo.TransactionHistory;
import pojo.Users;
import utility.BankException;
import utility.InputDefectException;

public interface AdminServiceInterface {
	
	public JSONObject getBalance(long accountNumber) throws BankException;

	public JSONArray getAccounts(long id) throws BankException;

	public void resetPassword(long id,String password) throws BankException;

	public JSONObject accountStatus(long accountNumber) throws BankException;

	public void modifyMoney(long accounyNumber,long balance) throws BankException;

	public void putHistory(TransactionHistory history) throws BankException;

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
	// employee operations
	
	public void addUsers(Users user) throws BankException;

	public void addCustomers(Customers customer) throws BankException;

	public void createAccount(Accounts account) throws BankException;

	public void deleteAccount(long accountNumber) throws BankException;

	public void deactivateAccount(long accountNumber) throws BankException;

	public void activateAccount(long accountNumber) throws BankException;

	public JSONObject getBranch(long empId) throws BankException ;
	
	public void activateCustomer(long id) throws BankException ;
	
	public void deactivateCustomer(long id) throws BankException ;
	// ADMIN operations
	
	public void createBranch(Branch branch) throws BankException;

	public void addAdmin(Employees admin) throws BankException;

	public void addEmployee(Employees employee) throws BankException;

	public void removeEmployee(long id) throws BankException;

	public JSONArray getAllBranchId() throws BankException;
	
	public void checkBranchAbsence(long value , String field) throws BankException, InputDefectException ;

	public void checkBranchPrecence(long value , String field) throws BankException, InputDefectException ;
	
	public void checkEmployeeAbsence(long value , String field) throws BankException, InputDefectException ;

	public void checkEmployeePrecence(long value , String field) throws BankException, InputDefectException ;

}
