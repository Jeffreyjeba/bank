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
	public long getBalance(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkAccNoForPresence(customerJson);
		return UtilityHelper.getLong(customer.getBalance(customerJson),"Balance");
	}

	public void switchAccount(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkAccNoForPresence(customerJson);
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		Authenticator.accountTag(accountNumber);
	}

	public long[] getAccounts(JSONObject customerJson) throws BankException, InputDefectException {
		checkIdCustomerPresence(customerJson);
		JSONArray jArray = customer.getAccounts(customerJson);
		if (jArray.length() == 0) {
			throw new BankException("Accounts for this Id dosent exist");
		}
		return jArrayToArray(jArray);
	}

	public void resetPassword(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkIdUserPresence(customerJson);
		String password = UtilityHelper.getString(customerJson, "Password");
		String newPasswordHash = UtilityHelper.passHasher(password);
		UtilityHelper.put(customerJson, "Password", newPasswordHash);
		customer.resetPassword(customerJson);
	}

	public String accountStatus(long accountNumber) throws BankException, InputDefectException {
		JSONObject json=UtilityHelper.put(new JSONObject(), "AccountNumber", accountNumber);
		checkAccNoForPresence(json);
		JSONObject resultJson = customer.accountStatus(json);
		return UtilityHelper.getString(resultJson, "Status");
	}

	public void debit(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkAccNoForPresence(customerJson);
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(customerJson);
		long amount = UtilityHelper.getLong(customerJson, "Amount");
		String description = UtilityHelper.getString(customerJson, "Description");
		balanceCheck(balanceAmount, amount);
		modifyMoney(accountNumber, balanceAmount - amount);
		long tId = System.currentTimeMillis();
		JSONObject hisJson = historyJson("debit", -amount, tId, accountNumber, description, balanceAmount - amount,null);
		customer.putHistory(hisJson);
	}

	public void credit(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkAccNoForPresence(customerJson);
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(customerJson);
		long amount = UtilityHelper.getLong(customerJson, "Amount");
		String description = UtilityHelper.getString(customerJson, "Description");
		modifyMoney(accountNumber, balanceAmount + amount);
		long tId = System.currentTimeMillis();
		JSONObject hisJson = historyJson("credit", amount, tId, accountNumber, description, balanceAmount + amount,null);
		putHistory(hisJson);
	}

	public void moneyTransfer(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkAccNoForPresence(customerJson);
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(customerJson);
		long amount = UtilityHelper.getLong(customerJson, "Amount");
		balanceCheck(balanceAmount, amount);
		long trasactionAccountNumber = UtilityHelper.getLong(customerJson, "TransactionAccountNumber");
		String description = UtilityHelper.getString(customerJson, "Description");
		String ifscCode = UtilityHelper.getString(customerJson, "IfscCode");
		boolean inBank = resolveTransaction(accountNumber, trasactionAccountNumber, ifscCode);
		if (!inBank) {
			long tId = System.currentTimeMillis();
			modifyMoney(accountNumber, balanceAmount - amount);
			JSONObject hisJson = historyJson("OBMoneyTransfer", -amount, tId, accountNumber, description,
					balanceAmount - amount, null);
			putHistory(hisJson);
		} 
		else {
			checkAccNoForPresence(UtilityHelper.put(new JSONObject(), "AccountNumber", trasactionAccountNumber));
			inBankTransfer(accountNumber, trasactionAccountNumber, amount, description);
		}
	}	

	public JSONArray transactionHistory(JSONObject customerJson,int quantity,int page,long searchMilli) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkAccNoForPresence(customerJson);
		JSONArray jArray=customer.getTransactionHistory(customerJson,quantity ,page,searchMilli);
		if (jArray.length() == 0) {
			throw new BankException("NO transacations made");
		}
		return jArray;
	}
	
	public JSONObject viewProfile(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		checkIdCustomerPresence(customerJson);
		return customer.viewProfile(customerJson);
	}
	
	public int getPages(JSONObject customerJson,int quantity,long searchMilli) throws BankException {
		return customer.pageCount(customerJson, quantity,searchMilli);
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
	
	public JSONObject getPrimaryAccount(JSONObject customerJson) throws BankException, InputDefectException {
		checkIdCustomerPresence(customerJson);
		return customer.getPrimaryAccount(customerJson);
	}
	
	public void setPrimaryAccount(JSONObject customerJson) throws BankException, InputDefectException {
		checkAccNoForPresence(customerJson);
		customer.setPrimaryAccount(customerJson);
	}
	
	public void switchPrimaryAccount(JSONObject customerJson) throws BankException, InputDefectException {
		checkIdCustomerPresence(customerJson);
		checkAccNoForPresence(customerJson);
		JSONObject primaryJson= getPrimaryAccount(customerJson);
		if(primaryJson==null) {
			setPrimaryAccount(customerJson);
		}
		else {
			removePrivateAccount(primaryJson);
			setPrimaryAccount(customerJson);
		}
	}
	
	private void removePrivateAccount(JSONObject customerJson) throws BankException {
		customer.removePrimaryAccount(customerJson);
	}


	// support methods
	protected void balanceCheck(long balanceAmount,long amount) throws BankException {
		if (balanceAmount < amount) {
			throw new BankException("Insuffecient balance");
		}
	}
		
	protected void putHistory(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		customer.putHistory(customerJson);
	}

	protected JSONObject historyJson(String type, long amount, long transactionId, long accountNumber,
			String description, long balance, Long TransactionAccountNumber) throws BankException {
		JSONObject json = new JSONObject();
		UtilityHelper.put(json, "AccountNumber", accountNumber);
		UtilityHelper.put(json, "TransactionAccountNumber", TransactionAccountNumber);
		UtilityHelper.put(json, "Description", description);
		UtilityHelper.put(json, "TransactionAmount", amount);
		UtilityHelper.put(json, "TransactionType", type);
		UtilityHelper.put(json, "TransactionId", transactionId);
		UtilityHelper.put(json, "Balance", balance);
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


	protected void checkIdUserPresence(JSONObject customerJson) throws BankException, InputDefectException {
		customer.checkUserPresence(customerJson, "Id");
	}

	protected void checkIdUserAbsence(JSONObject customerJson) throws BankException, InputDefectException {
		customer.checkUserAbsence(customerJson,"Id");
	}

	protected void checkIdCustomerAbsence(JSONObject customerJson) throws BankException, InputDefectException {
		customer.checkCustomerAbsence(customerJson,"Id");
	}

	protected void checkIdCustomerPresence(JSONObject customerJson) throws BankException, InputDefectException {
		customer.checkCustomerPresence(customerJson,"Id");
	}

	protected void checkAccNoForAbsence(JSONObject customerJson) throws BankException, InputDefectException {
		customer.checkAccountAbsence(customerJson, "AccountNumber");
	}

	protected void checkAccNoForPresence(JSONObject customerJson) throws BankException, InputDefectException {
		customer.checkAccountPresence(customerJson,"AccountNumber");
	}
}
