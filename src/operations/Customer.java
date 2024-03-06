package operations;

import org.json.JSONArray;
import org.json.JSONObject;

import bank.Authenticator;
import bank.ServiceFactory;
import database.CustomerServiceInterface;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class Customer {
	
	private CustomerServiceInterface customer = ServiceFactory.getCustomerService();

	// operation methods
	public long getBalance(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		return UtilityHelper.getLong(customer.getBalance(json),"Balance");
	}

	public void switchAccount(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		Authenticator.accountTag(accountNumber);
	}

	public long[] getAccounts(JSONObject json) throws BankException, InputDefectException {
		checkIdCustomerPresence(json);
		JSONArray jArray = customer.getAccounts(json);
		if (jArray.length() == 0) {
			throw new BankException("Accounts for this Id dosent exist");
		}
		return jArrayToArray(jArray);
	}

	public void resetPassword(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkIdUserPresence(json);
		String password = UtilityHelper.getString(json, "Password");
		String newPasswordHash = UtilityHelper.passHasher(password);
		UtilityHelper.put(json, "Password", newPasswordHash);
		customer.resetPassword(json);
	}

	public String accountStatus(long accountNumber) throws BankException, InputDefectException {
		JSONObject json=UtilityHelper.put(new JSONObject(), "AccountNumber", accountNumber);
		checkAccNoForPrecence(json);
		JSONObject resultJson = customer.accountStatus(json);
		return UtilityHelper.getString(resultJson, "Status");
	}

	public void debit(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(json);
		long amount = UtilityHelper.getLong(json, "Amount");
		String description = UtilityHelper.getString(json, "Description");
		balanceCheck(balanceAmount, amount);
		modifyMoney(accountNumber, balanceAmount - amount);
		long tId = System.currentTimeMillis();
		JSONObject hisJson = historyJson("debit", -amount, tId, accountNumber, description, balanceAmount - amount,null);
		customer.putHistory(hisJson);
	}

	public void credit(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		long accountNumber = UtilityHelper.getLong(json, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(json);
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
		long balanceAmount = getBalance(json);
		long amount = UtilityHelper.getLong(json, "Amount");
		balanceCheck(balanceAmount, amount);
		long trasactionAccountNumber = UtilityHelper.getLong(json, "TransactionAccountNumber");
		String description = UtilityHelper.getString(json, "Description");
		String ifscCode = UtilityHelper.getString(json, "IfscCode");
		boolean inBank = resolveTransaction(accountNumber, trasactionAccountNumber, ifscCode);
		if (!inBank) {
			long tId = System.currentTimeMillis();
			modifyMoney(accountNumber, balanceAmount - amount);
			JSONObject hisJson = historyJson("OBMoneyTransfer", -amount, tId, accountNumber, description,
					balanceAmount - amount, null);
			putHistory(hisJson);
		} 
		else {
			checkAccNoForPrecence(UtilityHelper.put(new JSONObject(), "AccountNumber", trasactionAccountNumber));
			inBankTransfer(accountNumber, trasactionAccountNumber, amount, description);
		}
	}	

	public JSONArray transactionHistory(JSONObject json,int quantity,int page,long searchMilli) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkAccNoForPrecence(json);
		JSONArray jArray=customer.getTransactionHistory(json,quantity ,page,searchMilli);
		if (jArray.length() == 0) {
			throw new BankException("NO transacations made");
		}
		return jArray;
	}
	
	public JSONObject viewProfile(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		checkIdCustomerPresence(json);
		return customer.viewProfile(json);
	}
	
	public int getPages(JSONObject json,int quantity,long searchMilli) throws BankException {
		return customer.pageCount(json, quantity,searchMilli);
	}
	
	public long daystomilly(int day) {
		return (day*86400000l);
		
	}
	
	public long searchRegion(long day) {
		return (System.currentTimeMillis()-day);
	}
	
	public void logout() {
		Authenticator.idTag(0);
		Authenticator.accountTag(0);
	}


	// support methods
	protected void balanceCheck(long balanceAmount,long amount) throws BankException {
		if (balanceAmount < amount) {
			throw new BankException("Insuffecient balance");
		}
	}
		
	protected void putHistory(JSONObject json) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(json);
		customer.putHistory(json);
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
		customer.modifyMoney(json);
	}

	private boolean checkInBank(String ifscCode) {
		return ifscCode.substring(0, 3).equals("rey");
	}

	
	protected long getBalance(long accountNumber) throws BankException, InputDefectException {
		JSONObject json = new JSONObject();
		UtilityHelper.put(json, "AccountNumber", accountNumber);
		return getBalance(json); 
	}
	 
	protected boolean resolveTransaction(long accountNumber,long trasactionAccountNumber ,String ifscCode) throws BankException {
		if (accountNumber == trasactionAccountNumber) {
			throw new BankException("money cannot be transfered withiin the same account");
		}
		return checkInBank(ifscCode);
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
	
	protected void inBankTransfer(long accountNumber, long trasactionAccountNumber, long amount, String description)
			throws BankException, InputDefectException {

		String status = accountStatus(trasactionAccountNumber);
		long tId = System.currentTimeMillis();
		switch (status) {
		case "active":
			long balanceAmount = getBalance(accountNumber);
			long tBalanceAmount = getBalance(trasactionAccountNumber);
			modifyMoney(accountNumber, balanceAmount - amount);
			modifyMoney(accountNumber, tBalanceAmount + amount);
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

	// check methods


	protected void checkIdUserPresence(JSONObject json) throws BankException, InputDefectException {
		customer.checkUserPresence(json, "Id");
	}

	protected void checkIdUserAbsence(JSONObject json) throws BankException, InputDefectException {
		customer.checkUserAbsence(json,"Id");
	}

	protected void checkIdCustomerAbsence(JSONObject json) throws BankException, InputDefectException {
		customer.checkCustomerAbsence(json,"Id");
	}

	protected void checkIdCustomerPresence(JSONObject json) throws BankException, InputDefectException {
		customer.checkCustomerPresence(json,"Id");
	}

	protected void checkAccNoForAbsence(JSONObject json) throws BankException, InputDefectException {
		customer.checkAccountAbsence(json, "AccountNumber");
	}

	protected void checkAccNoForPrecence(JSONObject json) throws BankException, InputDefectException {
		customer.checkAccountPrecence(json,"AccountNumber");
	}
}
