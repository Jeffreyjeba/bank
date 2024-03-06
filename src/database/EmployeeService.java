package database;

import org.json.JSONObject;

import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class EmployeeService extends CustomerService implements EmployeeServiceInterface{
	
	public EmployeeService(String url, String userName, String password) {
		super(url, userName, password);
	}

	public void addUsers(JSONObject customer) throws BankException, InputDefectException {
		String tableName = "users";
		generalAdd(tableName, customer);
	}

	public void addCustomers(JSONObject customer) throws BankException, InputDefectException {
		String tableName = "customers";
		generalAdd(tableName, customer);
	}

	public void createAccount(JSONObject account) throws BankException, InputDefectException {
		String tableName = "accounts";
		generalAdd(tableName, account);
	}

	public void deleteAccount(JSONObject account) throws BankException, InputDefectException {
		StringBuilder query = queryBuilder.setAccountStatus("accounts", account);
		update(query, account);
	}

	public void deactivateAccount(JSONObject account) throws BankException, InputDefectException {
		StringBuilder query = queryBuilder.setAccountStatus("accounts", account);
		update(query, account);
	}

	public void activateAccount(JSONObject account) throws BankException,InputDefectException  {
		StringBuilder query= queryBuilder.setAccountStatus("accounts", account);
		update( query,account);
	}
	
	public JSONObject getBranch(long empId) throws BankException {
		return selectWhere("employees","Id="+empId,"BranchId");
	}

}
