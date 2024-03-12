package database2;

import org.json.JSONArray;
import org.json.JSONObject;

import bank.Priority;
import database2.DataStorageService;
import query.Query;
import query.QueryBuilderMySql;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class CustomerService extends DataStorageService {
	
	public CustomerService(String url, String userName, String password) {
		super(url, userName, password);
	}

	Query builder=new QueryBuilderMySql();
	
	public JSONObject getBalance(long accountNumber) throws BankException {
		StringBuilder query = builder.selectFromWhere("accounts","AccountNumber=" + accountNumber, "Balance");
		return select(query);
	}

	public JSONArray getAccounts(long id) throws BankException {
		StringBuilder query = builder.selectFromWhere("accounts", "Id=" + id, "AccountNumber");
		return bulkSelect(query);
	}

	public void resetPassword(long id,String password) throws BankException {
		StringBuilder query = builder.singleSetWhere("users", "Password", "Id", Long.toString(id));
		update(query, password);
	}

	public JSONObject accountStatus(JSONObject customerJson) throws BankException {
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		return selectWhere("accounts", "AccountNumber=" + accountNumber, "Status");
	}

	public void modifyMoney(JSONObject customerJson) throws BankException {
		StringBuilder query = builder.singleSetWhere("accounts", "Balance", "AccountNumber");
		update(query, customerJson);
	}

	public void putHistory(JSONObject customerJson) throws BankException {
		generalAdd("transactionHistory", customerJson);
	}

	public JSONArray getTransactionHistory(JSONObject customerJson,int quantity ,int page,long searchMilli) throws BankException {
		long accountNumber= UtilityHelper.getLong(customerJson, "AccountNumber");
		StringBuilder query = builder.selectAllFromWherePrep("transactionHistory",
								"AccountNumber=" + accountNumber+ " and TransactionId > "
								+searchMilli+" order by TransactionId asc limit "
								+quantity+" offset "+(page-1)*quantity);
		return bulkSelect(query);
	}
	
	public int pageCount(JSONObject customerJson,int quantity,long searchMilli) throws BankException {
		long accountNumber= UtilityHelper.getLong(customerJson, "AccountNumber");
		StringBuilder countQuery = builder.selectAllCountFromWherePrep("transactionHistory",
									"AccountNumber=" + accountNumber
									+ " and TransactionId > "+searchMilli);
		float count= UtilityHelper.getInt(select(countQuery),"count(*)");
		return (int) Math.ceil(count/quantity); 
	}

	public JSONObject viewProfile(JSONObject customerJson) throws BankException {
		long id= UtilityHelper.getLong(customerJson,"Id");
		StringBuilder query=builder.viewCustomerProfile(id);
		JSONObject jsonResult= select(query);
		JSONArray jsonArray=getAccounts(customerJson);
		return UtilityHelper.put(jsonResult,"AccountNumber", jsonArray);
	}
	
	public JSONObject getPrimaryAccount(JSONObject customerJson) throws BankException {
		long id=UtilityHelper.getLong(customerJson,"Id");
		StringBuilder query =builder.selectFromWhere("accounts","Id = "+id+" and Priority='primary'","AccountNumber");
		return select(query);
	}
	
	public void setPrimaryAccount(JSONObject customerJson) throws BankException {
		updatePriority(customerJson, Priority.primary);
	}
	
	public void removePrimaryAccount(JSONObject customerJson) throws BankException {
		updatePriority(customerJson, null);
	}
	

	public void checkUserPresence(JSONObject customerJson, String field) throws BankException, InputDefectException {
		checkLongPresence(customerJson, "users", field, field);
	}

	public void checkUserAbsence(JSONObject customerJson, String field) throws BankException, InputDefectException {
		checkLongAbsence(customerJson, "users", field, field);
	}

	public void checkCustomerAbsence(JSONObject customerJson, String field) throws BankException, InputDefectException {
		checkLongAbsence(customerJson, "customers", field, field);
	}

	public void checkCustomerPresence(JSONObject customerJson, String field) throws BankException, InputDefectException {
		checkLongPresence(customerJson, "customers", field, field);
	}

	public void checkAccountAbsence(JSONObject customerJson, String field) throws BankException, InputDefectException {
		checkLongAbsence(customerJson, "accounts", field, field);
	}

	public void checkAccountPresence(JSONObject customerJson, String field) throws BankException, InputDefectException {
		checkLongPresence(customerJson, "accounts", field, field);
	}
	
	public void checkAccountPrimary(JSONObject customerJson) {
		// TODO
	}
	
	
	
	// support methods
	protected void checkLongAbsence(JSONObject customerJson, String tableName, String fieldName, String selectionField)
			throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long id = UtilityHelper.getLong(customerJson, fieldName);
		JSONObject resultJson = selectWhere(tableName, fieldName + "=" + id, selectionField);
		if (resultJson != null) {
			throw new BankException(selectionField + "  : " + id + " is alreday present");
		}
	}

	protected void checkLongPresence(JSONObject customerJson, String tableName, String fieldName, String selectionField)
			throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long id = UtilityHelper.getLong(customerJson, fieldName);
		JSONObject resultJson = selectWhere(tableName, fieldName + "=" + id, selectionField);
		if (resultJson == null) {
			throw new BankException(selectionField + " : " + id + " is not available");
		}
	}
	
	
	protected JSONArray selectOne(String tableName, String fieldName) throws BankException {
		StringBuilder query = builder.selectFrom(tableName, fieldName);
		return bulkSelect(query);
	}

	protected void generalAdd(String tableName, JSONObject employee) throws BankException {
		StringBuilder query = builder.addJsonPrepStatement(tableName, employee);
		add(query, employee);
	}
	
	protected JSONObject selectWhere(String tableName, String condition, String target) throws BankException {
		StringBuilder query =builder.selectFromWhere(tableName, condition, target);
		return select(query);
	}
	
	protected void updatePriority(JSONObject customerJson,Priority priority) throws BankException {
		long accountNumber=UtilityHelper.getLong(customerJson,"AccountNumber");
		StringBuilder query=builder.singleSetWhere("accounts","Priority","AccountNumber",Long.toString(accountNumber));
		if(priority!=null) {
			update(query, UtilityHelper.put(new JSONObject(),"Priority",priority.toString())); 
		}
		else {
			JSONObject resultJson=UtilityHelper.put(new JSONObject(),"Priority",JSONObject.NULL);
			update(query,resultJson);
		}
	}

}
