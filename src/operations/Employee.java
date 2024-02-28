package operations;


import org.json.JSONObject;

import database.QueryBuilder;
import utility.BankException;
import utility.UtilityHelper;

public class Employee extends Customer{
	public void addUsers(JSONObject customer) throws Exception{
		UtilityHelper.nullCheck(customer);
		checkuserabsence(customer);
		String tableName="users";
		generalAdd(tableName, customer);	
	}
	public void addCustomers(JSONObject customer) throws Exception{
		UtilityHelper.nullCheck(customer);
		checkIdForPresence(customer);
		String tableName="customers";
		generalAdd(tableName, customer);	
	}
	public void createAccount(JSONObject account) throws Exception {
		UtilityHelper.nullCheck(account);
		checkAccNoForAbsence(account);
		checkIdForPresence(account);
		String tableName="accounts";
		generalAdd(tableName, account);
		
	}
	public void deleteAccount(JSONObject account) throws Exception {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		String tableName="accounts";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.setAccountStatus(tableName, account);
		dtabase.update( query,account);
	}
	public void deactivateAccount(JSONObject account) throws Exception {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		String tableName="accounts";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.setAccountStatus(tableName, account);
		dtabase.update( query,account);
	}
	public void activateAccount(JSONObject account) throws Exception {
		UtilityHelper.nullCheck(account);
		checkAccNoForPrecence(account);
		String tableName="accounts";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.setAccountStatus(tableName, account);
		dtabase.update( query,account);
	}
	protected void generalAdd(String tableName,JSONObject employee) throws Exception {
		UtilityHelper.nullCheck(employee);
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.addJsonPrepStatement(tableName, employee);
		dtabase.add(query, employee);
	}
	protected void checkIdForPresence(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		int id=json.getInt("Id");
		JSONObject json2=selectwhere("users","Id="+id,"Id");
		if(json2==null) { 
			throw new BankException("No user id found as : "+id);
		}
	}
	protected void checkAccNoForAbsence(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long accountNumber=json.getLong("AccountNumber");
		JSONObject json2=selectwhere("accounts","AccountNumber="+accountNumber,"AccountNumber");
		if(json2!=null) { 
			throw new BankException("account already exist with accouny number : "+accountNumber);
		}
	}
	protected void checkAccNoForPrecence(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long accountNumber=json.getLong("AccountNumber");
		JSONObject json2=selectwhere("accounts","AccountNumber="+accountNumber,"AccountNumber");
		if(json2==null) { 
			throw new BankException("account already exist with accouny number : "+accountNumber);
		}
	}
	protected void checkuserabsence(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		int id=json.getInt("Id");
		JSONObject json2=selectwhere("users","Id="+id,"Id");
		if(json2!=null) { 
			throw new BankException("user id already exist : "+id);
		}
	}
}
