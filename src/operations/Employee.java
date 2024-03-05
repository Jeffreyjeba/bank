package operations;


import org.json.JSONObject;

import database.EmployeeService;
import database.EmployeeServiceInterface;
import utility.BankException;
import utility.InputDefectException;
//import utility.BankException;
import utility.UtilityHelper;

public class Employee extends Customer{
	//operations methods
	EmployeeServiceInterface employee=new EmployeeService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	
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
		checkAccNoForPrecence(account);
		employee.deleteAccount(account);
	}
	public void deactivateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		employee.deactivateAccount(account);
	}
	public void activateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		employee.activateAccount(account);
	}
	
	public JSONObject getBranchId(long id) throws BankException {
		return employee.getBranch(id);
	}
}
