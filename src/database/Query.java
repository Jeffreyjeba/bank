package database;

import org.json.JSONObject;

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
	// Update query builder
	public StringBuilder singleSetWhere(String tableName, String field, String conditionField);
	public StringBuilder singleSetWhere(String tableName, String field, String conditionField, String coditionValue);
	public StringBuilder setStatus(String tableName, JSONObject json) throws BankException;
	// Delete query builder
	public StringBuilder deleteFromJson(String tableName, JSONObject json);
	// Create query builder
	public StringBuilder createTable(String tableName, String[] parametre);

}
