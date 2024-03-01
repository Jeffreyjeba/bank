package operations;

import org.json.JSONArray;
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

	// operation methods
	public JSONObject getBalance(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere("accounts", "AccountNumber=" + UtilityHelper.getLong(json, "AccountNumber"), "Balance");
		return dtabase.select(query);
	}

	public void switchAccount(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		new Authenticator().accountTag(accountNumber);
	}

	public long[] getAccounts(long id) throws BankException, InputDefectException {
		checkIdCustomerPresence(UtilityHelper.put(new JSONObject(), "Id", id));
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere("accounts", "Id=" + id, "AccountNumber");
		JSONArray jArray = dtabase.bulkSelect(query);
		if (jArray.length() == 0) {
			throw new BankException("Id or accounts for this Id dosent exist");
		}
		return jArrayToArray(jArray);
	}

	public void resetPassword(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkIdUserPresence(json);
		String password = UtilityHelper.getString(json, "Password");
		String newPasswordHash = UtilityHelper.passHasher(password);
		UtilityHelper.put(json, "Password", newPasswordHash);
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		long id = UtilityHelper.getLong(json, "Id");
		json.remove("Id");
		builder.singleSetWhere("users", "Password", "Id", Long.toString(id));
		dtabase.update(query, json);
	}

	public String accountStatus(long accountNumber) throws BankException, InputDefectException {
		checkAccNoForPrecence(UtilityHelper.put(new JSONObject(), "AccountNumber", accountNumber));
		JSONObject json = selectwhere("accounts", "AccountNumber=" + accountNumber, "Status");
		if (json == null) {
			return null;
		}
		return UtilityHelper.getString(json, "Status");
	}

	public void debit(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(accountNumber);
		long amount = UtilityHelper.getLong(json, "Amount");
		String description = UtilityHelper.getString(json, "Description");
		if (balanceAmount < amount) {
			throw new BankException("Insuffecient balance");
		}
		modifyMoney(accountNumber, balanceAmount - amount);
		long tId = System.currentTimeMillis();
		JSONObject hisJson = historyJson("debit", -amount, tId, accountNumber, description, balanceAmount - amount,null);
		putHistory(hisJson);
	}

	public void credit(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(accountNumber);
		long amount = UtilityHelper.getLong(json, "Amount");
		String description = UtilityHelper.getString(json, "Description");
		modifyMoney(accountNumber, balanceAmount + amount);
		long tId = System.currentTimeMillis();
		JSONObject hisJson = historyJson("credit", amount, tId, accountNumber, description, balanceAmount + amount,null);
		putHistory(hisJson);
	}

	public void moneyTransfer(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(accountNumber);
		long amount = UtilityHelper.getLong(json, "Amount");
		if (balanceAmount < amount) {
			throw new BankException("Insuffecient balance");
		}
		long trasactionAccountNumber = UtilityHelper.getLong(json, "TransactionAccountNumber");
		if (accountNumber == trasactionAccountNumber) {
			throw new BankException("money cannot be transfered withiin the same account");
		}
		String description = UtilityHelper.getString(json, "Description");
		String ifscCode = UtilityHelper.getString(json, "IfscCode");
		boolean inBank = checkInBank(ifscCode);
		System.out.println(inBank);
		if (!inBank) {
			long tId = System.currentTimeMillis();
			modifyMoney(accountNumber, balanceAmount - amount);
			JSONObject hisJson = historyJson("OBMoneyTransfer", -amount, tId, accountNumber, description,
					balanceAmount - amount, null);
			putHistory(hisJson);
		} else {
			checkAccNoForPrecence(UtilityHelper.put(new JSONObject(), "AccountNumber", trasactionAccountNumber));
			inBankTransfer(accountNumber, trasactionAccountNumber, amount, description);
		}
	}

	private void inBankTransfer(long accountNumber, long trasactionAccountNumber, long amount, String description)
			throws BankException, InputDefectException {

		String status = accountStatus(trasactionAccountNumber);
		long tId = System.currentTimeMillis();
		switch (status) {
		case "active":
			// getting balance
			long balanceAmount = getBalance(accountNumber);
			long tBalanceAmount = getBalance(trasactionAccountNumber);
			// updating money
			modifyMoney(accountNumber, balanceAmount - amount);
			modifyMoney(accountNumber, tBalanceAmount + amount);
			// updating History
			JSONObject hisJson = historyJson("moneyTransfer", -amount, tId, accountNumber, description,
					balanceAmount - amount, trasactionAccountNumber);
			putHistory(hisJson);
			hisJson = historyJson("moneyTransfer", amount, tId, trasactionAccountNumber, description,
					tBalanceAmount + amount, accountNumber);
			putHistory(hisJson);

			break;
		case "inactive":
			throw new BankException("your reciptant account is blocked");
		case "deleted":
			throw new BankException("your reciptant account is deleted");
		}
	}

	public JSONArray transactionHistory(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectAllFromWherePrep("transactionHistory",
				"AccountNumber=" + UtilityHelper.getLong(json, "AccountNumber"));
		JSONArray jArray = dtabase.bulkSelect(query);
		if (jArray.length() == 0) {
			throw new BankException("NO transacations made");
		}
		return jArray;
	}

	public JSONObject selectwhere(String tableName, String condition, String target) throws BankException {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere(tableName, condition, target);
		return dtabase.select(query);
	}

	// support methods
	protected void putHistory(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.addJsonPrepStatement("transactionHistory", json);
		dtabase.add(query, json);
	}

	protected JSONObject historyJson(String type, long amount, long transactionId, long accountNumber,
			String description, long balance, Long TransactionAccountNumber) throws BankException {
		String dateTime = null;
		JSONObject json = new JSONObject();
		UtilityHelper.put(json, "AccountNumber", accountNumber);
		UtilityHelper.put(json, "TransactionAccountNumber", TransactionAccountNumber);
		UtilityHelper.put(json, "Description", description);
		UtilityHelper.put(json, "TransactionAmount", amount);
		UtilityHelper.put(json, "TransactionType", type);
		UtilityHelper.put(json, "TransactionId", transactionId);
		UtilityHelper.put(json, "Balance", balance);
		UtilityHelper.put(json, "DateTime", dateTime);
		return json;
	}

	protected void modifyMoney(long accountNumber, long closingBalance) throws BankException {
		JSONObject json = new JSONObject();
		UtilityHelper.put(json, "AccountNumber", accountNumber);
		UtilityHelper.put(json, "Balance", closingBalance);
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.singleSetWhere("accounts", "Balance", "AccountNumber");
		dtabase.update(query, json);
	}

	private boolean checkInBank(String ifscCode) {
		return ifscCode.substring(0, 3).equals("rey");
	}

	protected JSONArray selectOne(String tableName, String fieldName) throws BankException {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFrom(tableName, fieldName);
		return dtabase.bulkSelect(query);
	}

	protected long getBalance(long accountNumber) throws BankException, InputDefectException {
		JSONObject json = new JSONObject();
		UtilityHelper.put(json, "AccountNumber", accountNumber);
		JSONObject json2 = getBalance(json);
		System.out.println(json2);
		return UtilityHelper.getLong(json2, "Balance");
	}

	protected void resolveAccountStatus(long accountNumber) throws BankException, InputDefectException {
		String status = accountStatus(accountNumber);
		switch (status) {
		case "inactive":
			throw new BankException("your account is blocked");
		case "deleted":
			throw new BankException("your account is deleted");
		}
	}

	protected long[] jArrayToArray(JSONArray jArray) throws BankException {
		int size = jArray.length();
		long[] array = new long[size];
		for (int iterator = 0; iterator < size; iterator++) {
			JSONObject json = UtilityHelper.getJsonObject(jArray, iterator);
			array[iterator] = UtilityHelper.getLong(json, "AccountNumber");
		}
		return array;
	}

	// check methods

	protected void checkLongAbsence(JSONObject json, String tableName, String fieldName, String selectionField)
			throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		long id = UtilityHelper.getLong(json, fieldName);
		JSONObject json2 = selectwhere(tableName, fieldName + "=" + id, selectionField);
		if (json2 != null) {
			throw new BankException(selectionField + "  : " + id + " is alreday present");
		}
	}

	protected void checkLongPresence(JSONObject json, String tableName, String fieldName, String selectionField)
			throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		long id;
		id = UtilityHelper.getLong(json, fieldName);
		JSONObject json2 = selectwhere(tableName, fieldName + "=" + id, selectionField);
		if (json2 == null) {
			throw new BankException(selectionField + " : " + id + " is not available");
		}
	}

	protected void checkIdUserPresence(JSONObject json) throws BankException, InputDefectException {
		checkLongPresence(json, "users", "Id", "Id");
	}

	protected void checkIdUserAbsence(JSONObject json) throws BankException, InputDefectException {
		checkLongAbsence(json, "users", "Id", "Id");
	}

	protected void checkIdCustomerAbsence(JSONObject json) throws BankException, InputDefectException {
		checkLongAbsence(json, "customers", "Id", "Id");
	}

	protected void checkIdCustomerPresence(JSONObject json) throws BankException, InputDefectException {
		checkLongPresence(json, "customers", "Id", "Id");
	}

	protected void checkAccNoForAbsence(JSONObject json) throws BankException, InputDefectException {
		checkLongAbsence(json, "accounts", "AccountNumber", "AccountNumber");
	}

	protected void checkAccNoForPrecence(JSONObject json) throws BankException, InputDefectException {
		checkLongPresence(json, "accounts", "AccountNumber", "AccountNumber");
	}
}
