package operations;


import org.json.JSONObject;

import database.QueryBuilder;
import utility.BankException;
import utility.InputDefectException;
//import utility.BankException;
import utility.UtilityHelper;

public class Employee extends Customer{
	//operations methods
	public void addUsers(JSONObject customer) throws BankException,InputDefectException{
		UtilityHelper.nullCheck(customer);
		checkIdUserAbsence(customer);
		String tableName="users";
		generalAdd(tableName, customer);	
	}
	public void addCustomers(JSONObject customer) throws BankException,InputDefectException{
		UtilityHelper.nullCheck(customer);
		checkIdUserPresence(customer);
		checkIdCustomerAbsence(customer);
		String tableName="customers";
		generalAdd(tableName, customer);	
	}
	public void createAccount(JSONObject account) throws BankException,InputDefectException {
		UtilityHelper.nullCheck(account);
		checkAccNoForAbsence(account);
		checkIdCustomerPresence(account);
		String tableName="accounts";
		generalAdd(tableName, account);
		
	}
	public void deleteAccount(JSONObject account) throws BankException,InputDefectException {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		String tableName="accounts";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.setAccountStatus(tableName, account);
		dtabase.update( query,account);
	}
	public void deactivateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		String tableName="accounts";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.setAccountStatus(tableName, account);
		dtabase.update( query,account);
	}
	public void activateAccount(JSONObject account) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		String tableName="accounts";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.setAccountStatus(tableName, account);
		dtabase.update( query,account);
	}
	//support methods
	protected void generalAdd(String tableName,JSONObject employee) throws BankException,InputDefectException {
		UtilityHelper.nullCheck(employee);
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.addJsonPrepStatement(tableName, employee);
		dtabase.add(query, employee);
	}
}
