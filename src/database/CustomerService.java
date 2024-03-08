package database;

import org.json.JSONArray;
import org.json.JSONObject;

import bank.Priority;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class CustomerService extends DataStorageService implements CustomerServiceInterface {

	public CustomerService(String url, String userName, String password) {
		super(url, userName, password);
	}

	Query queryBuilder = new QueryBuilderMySql();

	public JSONObject getBalance(JSONObject json) throws BankException {
		StringBuilder query = queryBuilder.selectFromWhere("accounts","AccountNumber=" + UtilityHelper.getLong(json, "AccountNumber"), "Balance");
		return select(query);
	}

	public JSONArray getAccounts(JSONObject json) throws BankException {
		long id = UtilityHelper.getLong(json, "Id");
		StringBuilder query = queryBuilder.selectFromWhere("accounts", "Id=" + id, "AccountNumber");
		return bulkSelect(query);
	}

	public void resetPassword(JSONObject json) throws BankException {
		long id = UtilityHelper.getLong(json, "Id");
		json.remove("Id");
		StringBuilder query = queryBuilder.singleSetWhere("users", "Password", "Id", Long.toString(id));
		update(query, json);
	}

	public JSONObject accountStatus(JSONObject json) throws BankException {
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		return selectWhere("accounts", "AccountNumber=" + accountNumber, "Status");
	}

	public void modifyMoney(JSONObject json) throws BankException {
		StringBuilder query = queryBuilder.singleSetWhere("accounts", "Balance", "AccountNumber");
		update(query, json);
	}

	public void putHistory(JSONObject json) throws BankException {
		StringBuilder query = queryBuilder.addJsonPrepStatement("transactionHistory", json);
		add(query, json);
	}

	public JSONArray getTransactionHistory(JSONObject json,int quantity ,int page,long searchMilli) throws BankException {
		long accountNumber= UtilityHelper.getLong(json, "AccountNumber");
		StringBuilder query = queryBuilder.selectAllFromWherePrep("transactionHistory",
								"AccountNumber=" + accountNumber+ " and TransactionId > "
								+searchMilli+" order by TransactionId asc limit "
								+quantity+" offset "+(page-1)*quantity);
		return bulkSelect(query);
	}
	
	public int pageCount(JSONObject json,int quantity,long searchMilli) throws BankException {
		long accountNumber= UtilityHelper.getLong(json, "AccountNumber");
		StringBuilder countQuery = queryBuilder.selectAllCountFromWherePrep("transactionHistory",
									"AccountNumber=" + accountNumber
									+ " and TransactionId > "+searchMilli);
		float count= UtilityHelper.getInt(select(countQuery),"count(*)");
		return (int) Math.ceil(count/quantity); 
	}

	public JSONObject viewProfile(JSONObject json) throws BankException {
		long id= UtilityHelper.getLong(json,"Id");
		StringBuilder query=builder.viewCustomerProfile(id);
		JSONObject jsonResult= select(query);
		JSONArray jsonArray=getAccounts(json);
		return UtilityHelper.put(jsonResult,"AccountNumber", jsonArray);
	}
	
	public JSONObject getPrimaryAccount(JSONObject json) throws BankException {
		System.out.println(json);// remove
		long id=UtilityHelper.getLong(json,"Id");
		StringBuilder query =queryBuilder.selectFromWhere("accounts","Id = "+id+" and Priority='primary'","AccountNumber");
		return select(query);
	}
	
	public void setPrimaryAccount(JSONObject json) throws BankException {
		updatePriority(json, Priority.primary);
	}
	
	public void removePrimaryAccount(JSONObject json) throws BankException {
		updatePriority(json, null);
	}
	

	public void checkUserPresence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongPresence(json, "users", field, field);
	}

	public void checkUserAbsence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongAbsence(json, "users", field, field);
	}

	public void checkCustomerAbsence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongAbsence(json, "customers", field, field);
	}

	public void checkCustomerPresence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongPresence(json, "customers", field, field);
	}

	public void checkAccountAbsence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongAbsence(json, "accounts", field, field);
	}

	public void checkAccountPrecence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongPresence(json, "accounts", field, field);
	}
	
	public void checkAccountPrimary(JSONObject json) {
		// TODO
	}
	
	
	
	// support methods
	protected void checkLongAbsence(JSONObject json, String tableName, String fieldName, String selectionField)
			throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		long id = UtilityHelper.getLong(json, fieldName);
		JSONObject json2 = selectWhere(tableName, fieldName + "=" + id, selectionField);
		if (json2 != null) {
			throw new BankException(selectionField + "  : " + id + " is alreday present");
		}
	}

	protected void checkLongPresence(JSONObject json, String tableName, String fieldName, String selectionField)
			throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		long id = UtilityHelper.getLong(json, fieldName);
		JSONObject json2 = selectWhere(tableName, fieldName + "=" + id, selectionField);
		if (json2 == null) {
			throw new BankException(selectionField + " : " + id + " is not available");
		}
	}
	
	
	protected JSONArray selectOne(String tableName, String fieldName) throws BankException {
		StringBuilder query = builder.selectFrom(tableName, fieldName);
		return bulkSelect(query);
	}

	protected void generalAdd(String tableName, JSONObject employee) throws BankException, InputDefectException {
		StringBuilder query = builder.addJsonPrepStatement(tableName, employee);
		add(query, employee);
	}
	
	protected JSONObject selectWhere(String tableName, String condition, String target) throws BankException {
		StringBuilder query =builder.selectFromWhere(tableName, condition, target);
		return select(query);
	}
	
	protected void updatePriority(JSONObject json,Priority priority) throws BankException {
		long accountNumber=UtilityHelper.getLong(json,"AccountNumber");
		StringBuilder query=builder.singleSetWhere("accounts","Priority","AccountNumber",Long.toString(accountNumber));
		if(priority!=null) {
			update(query, UtilityHelper.put(new JSONObject(),"Priority",priority.toString())); 
		}
		else {
			JSONObject json2=UtilityHelper.put(new JSONObject(),"Priority",JSONObject.NULL);
			update(query,json2); //error
		}
	}

}
