package database;

import org.json.JSONObject;
import pojo.Accounts;
import pojo.Customers;
import pojo.Users;
import utility.BankException;

public interface EmployeeServiceInterface extends CustomerServiceInterface {
	
	
	public void addUsers(Users user) throws BankException;

	public void addCustomers(Customers customer) throws BankException;

	public void createAccount(Accounts account) throws BankException;

	public void deleteAccount(long accountNumber) throws BankException;

	public void deactivateAccount(long accountNumber) throws BankException;

	public void activateAccount(long accountNumber) throws BankException;

	public JSONObject getBranch(long empId) throws BankException ;
	
	public void activateCustomer(long id) throws BankException ;
	
	public void deactivateCustomer(long id) throws BankException ;
	
}
