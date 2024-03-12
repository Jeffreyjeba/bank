package operations;


import org.json.JSONObject;

import bank.ServiceFactory;
import database.EmployeeServiceInterface;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class Employee extends Customer{
	//operations methods
	EmployeeServiceInterface employee=ServiceFactory.getEmployeeService();
	
	public void addUsers(JSONObject customer) throws BankException,InputDefectException{
		UtilityHelper.nullCheck(customer);
		checkIdUserAbsence(customer);
		employee.addUsers(customer);
	}
	public void addCustomers(JSONObject customer) throws BankException,InputDefectException{
		UtilityHelper.nullCheck(customer);
		checkIdUserPresence(customer);
		checkIdCustomerAbsence(customer);
		employee.addCustomers(customer);
	}
	public void createAccount(JSONObject account) throws BankException,InputDefectException {
		UtilityHelper.nullCheck(account);
		checkAccNoForAbsence(account);
		checkIdCustomerPresence(account);
		employee.createAccount(account);
	}
	public void deleteAccount(JSONObject account) throws BankException,InputDefectException {
		UtilityHelper.nullCheck(account);
		checkAccNoForPresence(account);
		employee.deleteAccount(account);
	}
	public void deactivateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		checkAccNoForPresence(account);
		employee.deactivateAccount(account);
	}
	public void activateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		checkAccNoForPresence(account);
		employee.activateAccount(account);
	}
	
	public JSONObject getBranchId(long id) throws BankException {
		return employee.getBranch(id);
	}
	
	public void activateCustomer(JSONObject customer) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customer);
		checkIdCustomerPresence(customer);
		employee.activateCustomer(customer);
	}
	
	public void deactivateCustomer(JSONObject customer) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customer);
		checkIdCustomerPresence(customer);
		employee.deactivateCustomer(customer);
	}

}
