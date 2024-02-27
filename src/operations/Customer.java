package operations;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bank.Authenticator;
import database.JDBC;
import database.QueryBuilder;
import database.DataStorage;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class Customer {
	protected DataStorage dtabase = new JDBC("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");

	public JSONObject getBalance(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere("accounts","AccountNumber="+json.getLong("AccountNumber"),"Balance");
		System.out.println(query);
		return dtabase.select(query);
	}
	
	
	public void switchAccount(JSONObject json) throws JSONException, InputDefectException {
		UtilityHelper.nullCheck(json);
		long accountNumber=json.getLong("AccountNumber");
		new Authenticator().accountTag(accountNumber);
	}
	
	
	public long[] getAccounts(long id) throws Exception {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere("accounts","Id="+id,"AccountNumber");
		JSONArray jArray= dtabase.bulkSelect(query);
		if(jArray.length()==0){
			throw new BankException("Id or accounts for this Id dosent exist");
		}
		int size =jArray.length();
		long [] array =new long[size];
		for(int iterator=0;iterator<size;iterator++) {
			JSONObject json=jArray.getJSONObject(iterator);
			array[iterator]=json.getLong("AccountNumber");
		}
		return array;
	}
	
	
	public void resetPassword(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		String pasword=json.getString("Password");
		String newPasswordHash= UtilityHelper.passHasher(pasword);
		json.put("Password",newPasswordHash);
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		Long id=json.getLong("Id");
		json.remove("Id");
		builder.singleSetWhere("users","Password","Id",id.toString());
		dtabase.update(query, json);
	}
	
	
	public String accountStatus(long accountNumber) throws Exception {
		 JSONObject json=selectwhere("accounts","AccountNumber="+accountNumber,"Status");
		 if(json==null) {
			 return null;
		 }
		 return json.getString("Status");
	}
	
	
	public void debit(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long accountNumber=json.getLong("AccountNumber");
		long balanceAmount=getBalance(accountNumber);
		long amount=json.getLong("Amount");
		String description=json.getString("Description");
		json.remove("Description");
		json.remove("Amount");
		if(balanceAmount<amount) {
			throw new BankException("Insuffecient balance");
		}
		modifyMoney(json, balanceAmount-amount);
		long tId=System.currentTimeMillis();
		JSONObject hisJson= historyJson("debit", amount, tId,accountNumber, description, balanceAmount,null);
		putHistory(hisJson);
	}
	
	
	public void credit(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long accountNumber=json.getLong("AccountNumber");
		long balanceAmount=getBalance(accountNumber);
		long amount=json.getLong("Amount");
		String description=json.getString("Description");
		json.remove("Description");
		json.remove("Amount");
		modifyMoney(json, balanceAmount+amount);
		long tId=System.currentTimeMillis();
		JSONObject hisJson= historyJson("credit", amount, tId,accountNumber, description, balanceAmount,null);
		putHistory(hisJson);
	}
	
	
	public void moneyTransfer(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long accountNumber=json.getLong("AccountNumber");
		long balanceAmount=getBalance(accountNumber);
		long amount=json.getLong("Amount");
		long tId=System.currentTimeMillis();
		String description=json.getString("Description");
		if(balanceAmount<amount) {
			throw new BankException("Insuffecient balance");
		}
		JSONObject jsonDeb= new JSONObject();
		jsonDeb.put("AccountNumber",json.getLong("AccountNumber"));
		long trasactionAccountNumber=json.getLong("TransactionAccountNumber");
		String status=accountStatus(trasactionAccountNumber);
		if(status==null) {
			modifyMoney(jsonDeb, balanceAmount-amount);
			JSONObject hisJson= historyJson("OBMoneyTransfer", amount, tId,accountNumber, description, balanceAmount,null);
			putHistory(hisJson);
		}
		else{
			switch (status) {
			case "active":
				modifyMoney(jsonDeb, balanceAmount-amount);
				long tBalanceAmount=getBalance(json.getLong("TransactionAccountNumber"));
				JSONObject jsonCred=new JSONObject();
				jsonCred.put("AccountNumber",trasactionAccountNumber);
				modifyMoney(jsonCred,tBalanceAmount+amount);
				JSONObject hisJson= historyJson("moneyTransfer", -amount, tId,accountNumber, description, balanceAmount,trasactionAccountNumber);
				putHistory(hisJson);
				hisJson= historyJson("moneyTransfer", amount, tId,trasactionAccountNumber, description, tBalanceAmount,accountNumber);
				putHistory(hisJson);
				break;
			case "inactive":
				throw new BankException("your reciptant account is blocked");
			case "deleted":
				throw new BankException("your reciptant account is deleted");
			}
		}	
	}
	
	public JSONArray transactionHistory(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		StringBuilder query=new StringBuilder();
		QueryBuilder builder=new QueryBuilder(query);
		builder.selectAllFromWherePrep("transactionHistory","AccountNumber="+json.getLong("AccountNumber"));
		 JSONArray jArray= dtabase.bulkSelect(query);
		 if(jArray.length()==0) {
			 throw new BankException("NO transacations made");
		 }
		return jArray;
	}
	
	public JSONObject selectwhere(String tableName, String condition, String target) throws Exception {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere(tableName, condition, target);
		return dtabase.select(query);
	}

	
	protected void putHistory(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		StringBuilder query=new StringBuilder();
		QueryBuilder builder=new QueryBuilder(query);
		builder.addJsonPrepStatement("transactionHistory", json);
		dtabase.add(query, json);
	}
	protected JSONObject historyJson(String type,long amount,long transactionId,
			long accountNumber,String description,long balance,Long TransactionAccountNumber) throws JSONException {
		String dateTime=null;
		JSONObject json=new JSONObject();
		json.put("AccountNumber",accountNumber);
		json.put("TransactionAccountNumber",TransactionAccountNumber);
		json.put("Description",description);
		json.put("TransactionAmount", amount);
		json.put("TransactionType",type);
		json.put("TransactionId",transactionId);
		json.put("Balance",balance);
		json.put("DateTime",dateTime);
		return json;
	}
	
	protected void modifyMoney(JSONObject json,long closingBalance) throws Exception {
		json.put("Balance",closingBalance);
		StringBuilder query=new StringBuilder();
		QueryBuilder builder=new QueryBuilder(query);
		builder.singleSetWhere("accounts","Balance","AccountNumber");
		dtabase.update(query,json);
	}
	
	
	protected JSONArray selectOne(String tableName,String fieldName) throws Exception {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFrom(tableName,fieldName);
		return dtabase.bulkSelect(query);
	}

	protected long getBalance(long accountNumber) throws JSONException, Exception {
		JSONObject json=new JSONObject();
		json.put("AccountNumber",accountNumber);
		JSONObject json2= getBalance(json);
		System.out.println(json2);
			return	json2.getLong("Balance");
	}
}
