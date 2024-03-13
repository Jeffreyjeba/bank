package query;

import org.json.JSONObject;

import pojo.BankMarker;
import utility.BankException;

public interface Query {
	
	// Select query builder
	public StringBuilder selectAll(String tableName);
	public StringBuilder selectFrom(String tableName, String... fields) ;
	public StringBuilder selectFromWhere(String tableName, String conditionField, String... fields);
	public StringBuilder selectFromWherePrep(String tableName, String conditionField, String... fields);
	public StringBuilder selectItem(String tableName, String itemName);
	public StringBuilder selectAllFromWherePrep(String tableName, String conditionField);
	public StringBuilder selectAllCountFromWherePrep(String tableName, String conditionField);
	public StringBuilder viewCustomerProfile(long id);
	// Add query builder
	public StringBuilder addJsonPrepStatement(String tableName, JSONObject json);
	
	public StringBuilder pojoToAddQuery(String tableName,BankMarker data) throws BankException;
	// Update query builder
	public StringBuilder singleSetWhere(String tableName, String field, String conditionField);
	public StringBuilder singleSetWhere(String tableName, String field, String conditionField, String coditionValue);
	public StringBuilder setStatus(String tableName,String Status ,String fieldname);
	// Delete query builder
	public StringBuilder deleteFrom(String tableName,String fieldName);
	// Create query builder
	public StringBuilder createTable(String tableName, String[] parametre);

}
