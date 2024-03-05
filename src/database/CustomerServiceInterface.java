package database;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.BankException;
import utility.InputDefectException;


public interface CustomerServiceInterface {
	
	public JSONObject getBalance(JSONObject json) throws BankException;

	public JSONArray getAccounts(JSONObject json) throws BankException;

	public void resetPassword(JSONObject json) throws BankException;

	public JSONObject accountStatus(JSONObject json) throws BankException;

	public void modifyMoney(JSONObject json) throws BankException;

	public void putHistory(JSONObject json) throws BankException;

	public JSONArray getTransactionHistory(JSONObject json) throws BankException;

	public void checkUserPresence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkUserAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkCustomerAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkCustomerPresence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkAccountAbsence(JSONObject json, String field) throws BankException, InputDefectException;

	public void checkAccountPrecence(JSONObject json, String field) throws BankException, InputDefectException;
	
	public JSONObject selectwhere(String tableName, String condition, String target) throws BankException;

}