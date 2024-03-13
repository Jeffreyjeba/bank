package operations;


import org.json.JSONObject;

import bank.ServiceFactory;
import bank.UserHirarchy;
import database.EmployeeServiceInterface;
import pojo.Accounts;
import pojo.Customers;
import pojo.Users;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class Employee extends Customer{
	
	EmployeeServiceInterface employee=ServiceFactory.getEmployeeService();
	
	public void addUsers(JSONObject userJson) throws BankException,InputDefectException{
		UtilityHelper.nullCheck(userJson);
		Users user=josnToUsers(userJson);
		checkIdUserAbsence(user.getId());
		employee.addUsers(user);
	}
	public void addCustomers(JSONObject customerJson) throws BankException,InputDefectException{
		UtilityHelper.nullCheck(customerJson);
		Customers customer=josnToCustomers(customerJson);
		long id=customer.getId();
		checkIdUserPresence(id);
		checkIdCustomerAbsence(id);
		employee.addCustomers(customer);
	}
	public void createAccount(JSONObject accountJson) throws BankException,InputDefectException {
		UtilityHelper.nullCheck(accountJson);
		Accounts account=jsonToAccounts(accountJson);
		checkAccNoForAbsence(account.getAccountNumber());
		checkIdCustomerPresence(account.getId());
		employee.createAccount(account);
	}
	public void deleteAccount(JSONObject account) throws BankException,InputDefectException {
		UtilityHelper.nullCheck(account);
		long accountNumber=UtilityHelper.getLong(account,"AccountNumber");
		checkAccNoForPresence(accountNumber);
		employee.deleteAccount(accountNumber);
	}
	public void deactivateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		long accountNumber=UtilityHelper.getLong(account,"AccountNumber");
		checkAccNoForPresence(accountNumber);
		employee.deactivateAccount(accountNumber);
	}
	public void activateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		long accountNumber=UtilityHelper.getLong(account,"AccountNumber");
		checkAccNoForPresence(accountNumber);
		employee.activateAccount(accountNumber);
	}
	
	public JSONObject getBranchId(long id) throws BankException {
		return employee.getBranch(id);
	}
	
	public void activateCustomer(JSONObject customer) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customer);
		long id=UtilityHelper.getLong(customer,"Id");
		checkIdCustomerPresence(id);
		employee.activateCustomer(id);
	}
	
	public void deactivateCustomer(JSONObject customer) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customer);
		long id=UtilityHelper.getLong(customer,"Id");
		checkIdCustomerPresence(id);
		employee.deactivateCustomer(id);
	}
	
	protected Users josnToUsers(JSONObject userPojo) throws BankException {
		Users users=new Users();
		users.setEmailId(UtilityHelper.getString(userPojo,"EmailId"));
		users.setId(UtilityHelper.getLong(userPojo,"Id"));
		users.setName(UtilityHelper.getString(userPojo,"Name"));
		users.setPhoneNumber(UtilityHelper.getLong(userPojo,"PhoneNumber"));
		users.setUserType(UserHirarchy.valueOf(UtilityHelper.getString(userPojo,"UserType")));
		return users;
	}
	
	protected Customers josnToCustomers(JSONObject customerPojo) throws BankException {
		Customers customer=new Customers();
		customer.setAadharNumber(UtilityHelper.getLong(customerPojo,"AadharNumber"));
		customer.setAddress(UtilityHelper.getString(customerPojo,"Address"));
		customer.setId(UtilityHelper.getLong(customerPojo,"Id"));
		customer.setPanNumber(UtilityHelper.getString(customerPojo,"PanNumber"));
		return customer;
	}
	
	protected Accounts jsonToAccounts(JSONObject accountPojo) throws BankException {
		Accounts accounts=new Accounts();
		accounts.setAccountNumber(UtilityHelper.getLong(accountPojo,"AccountNumber"));
		accounts.setBalance(UtilityHelper.getLong(accountPojo,"Balance"));
		accounts.setBranchId(UtilityHelper.getInt(accountPojo,"BranchId"));
		accounts.setId(UtilityHelper.getLong(accountPojo,"Id"));
		return accounts;
	}

}
