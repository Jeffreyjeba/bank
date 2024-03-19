package database;

import org.json.JSONObject;

import pojo.Accounts;
import pojo.Customers;
import pojo.Users;
import utility.BankException;


public class EmployeeService extends CustomerService implements EmployeeServiceInterface{
	
	protected EmployeeService(String url, String userName, String password) {
		super(url, userName, password);
	}
	
	private static class BillpoughEmployee{
		private static final EmployeeService employeeService=new EmployeeService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
	
	public static EmployeeService getEmployeeService() {
		return BillpoughEmployee.employeeService;
	}

	public void addUsers(Users user) throws BankException {
		String tableName = "users";
		generalAdd(tableName, user);
	}

	public void addCustomers(Customers customer) throws BankException {
		String tableName = "customers";
		generalAdd(tableName, customer);
	}

	public void createAccount(Accounts account) throws BankException {
		String tableName = "accounts";
		generalAdd(tableName, account);
	}

	public void deleteAccount(long accountNumber) throws BankException {
		StringBuilder query = builder.setStatus("accounts","deleted","AccountNumber");
		update(query, accountNumber);
	}

	public void deactivateAccount(long accountNumber) throws BankException {
		StringBuilder query = builder.setStatus("accounts", "inactive","AccountNumber");
		update(query, accountNumber);
	}

	public void activateAccount(long accountNumber) throws BankException {
		StringBuilder query= builder.setStatus("accounts","active","AccountNumber");
		update( query,accountNumber);
	}
	
	public JSONObject getBranch(long empId) throws BankException {
		return selectWhere("employees","Id="+empId,"BranchId");
	}
	
	public void activateCustomer(long id) throws BankException {
		StringBuilder query=builder.setStatus("users", "active","Id");
		update(query, id);
	}
	
	public void deactivateCustomer(long id) throws BankException {
		StringBuilder query=builder.setStatus("users", "inactive","Id");
		update(query, id);
	}


}
