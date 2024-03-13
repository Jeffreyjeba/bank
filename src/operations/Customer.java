package operations;

import org.json.JSONArray;
import org.json.JSONObject;
import bank.Authenticator;
import bank.ServiceFactory;
import bank.TransactionType;
import database.CustomerServiceInterface;
import pojo.TransactionHistory;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;


public class Customer {
	
	private CustomerServiceInterface customer = ServiceFactory.getCustomerService();

	// operation methods
	
	
	public long getBalance(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long accountNumber=UtilityHelper.getLong(customerJson,"AccountNumber");
		checkAccNoForPresence(accountNumber);
		return UtilityHelper.getLong(customer.getBalance(accountNumber),"Balance");
	}

	public void switchAccount(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long accountNumber=UtilityHelper.getLong(customerJson,"AccountNumber");
		checkAccNoForPresence(accountNumber);
		Authenticator.accountTag(accountNumber);
	}

	public long[] getAccounts(JSONObject customerJson) throws BankException, InputDefectException {
		long id=UtilityHelper.getLong(customerJson,"Id");
		checkIdCustomerPresence(id);
		JSONArray jArray = customer.getAccounts(id);
		if (jArray.length() == 0) {
			throw new BankException("Accounts for this Id dosent exist");
		}
		return jArrayToArray(jArray);
	}

	public void resetPassword(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long id=UtilityHelper.getLong(customerJson,"Id");
		String password = UtilityHelper.getString(customerJson, "Password");
		checkIdUserPresence(id);
		String newPasswordHash = UtilityHelper.passHasher(password);
		customer.resetPassword(id,newPasswordHash);
	}

	public String accountStatus(long accountNumber) throws BankException, InputDefectException {
		checkAccNoForPresence(accountNumber);
		JSONObject resultJson = customer.accountStatus(accountNumber);
		return UtilityHelper.getString(resultJson, "Status");
	}

	public void debit(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		checkAccNoForPresence(accountNumber);
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(customerJson);
		long amount = UtilityHelper.getLong(customerJson, "Amount");
		String description = UtilityHelper.getString(customerJson, "Description");
		balanceCheck(balanceAmount, amount);
		modifyMoney(accountNumber, balanceAmount - amount);
		long tId = System.currentTimeMillis();
		TransactionHistory history = historyPojo("debit", -amount, tId, accountNumber, description, balanceAmount - amount,null);
		customer.putHistory(history);
	}

	public void credit(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		checkAccNoForPresence(accountNumber);
		resolveAccountStatus(accountNumber);
		long balanceAmount = getBalance(customerJson);
		long amount = UtilityHelper.getLong(customerJson, "Amount");
		String description = UtilityHelper.getString(customerJson, "Description");
		modifyMoney(accountNumber, balanceAmount + amount);
		long tId = System.currentTimeMillis();
		TransactionHistory history = historyPojo("credit", amount, tId, accountNumber, description, balanceAmount + amount,null);
		customer.putHistory(history);
	}

	public void moneyTransfer(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long accountNumber = UtilityHelper.getLong(customerJson, "AccountNumber");
		checkAccNoForPresence(accountNumber);
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
			TransactionHistory history = historyPojo("OBMoneyTransfer", -amount, tId, accountNumber, description,
					balanceAmount - amount, null);
			customer.putHistory(history);
		} 
		else {
			checkAccNoForPresence(trasactionAccountNumber);
			inBankTransfer(accountNumber, trasactionAccountNumber, amount, description);
		}
	}	

	public JSONArray transactionHistory(JSONObject customerJson,int quantity,int page,long searchMilli) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long accountNumber=UtilityHelper.getLong(customerJson, "AccountNumber");
		checkAccNoForPresence(accountNumber);
		JSONArray jArray=customer.getTransactionHistory(accountNumber,quantity ,page,searchMilli);
		if (jArray.length() == 0) {
			throw new BankException("NO transacations made");
		}
		return jArray;
	}
	
	public JSONObject viewProfile(JSONObject customerJson) throws BankException, InputDefectException {
		UtilityHelper.nullCheck(customerJson);
		long id = UtilityHelper.getLong(customerJson, "Id");
		checkIdCustomerPresence(id);
		return customer.viewProfile(id);
	}
	
	public int getPages(JSONObject customerJson,int quantity,long searchMilli) throws BankException {
		long accountNumber=UtilityHelper.getLong(customerJson, "AccountNumber");
		return customer.pageCount(accountNumber, quantity,searchMilli);
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
		long id=UtilityHelper.getLong(customerJson, "Id");
		checkIdCustomerPresence(id);
		return customer.getPrimaryAccount(id);
	}
	
	public void setPrimaryAccount(JSONObject customerJson) throws BankException, InputDefectException {
		long accountNumber=UtilityHelper.getLong(customerJson, "AccountNumber");
		checkAccNoForPresence(accountNumber);
		customer.setPrimaryAccount(accountNumber);
	}
	
	public void switchPrimaryAccount(JSONObject customerJson) throws BankException, InputDefectException {
		long accountNumber=UtilityHelper.getLong(customerJson, "AccountNumber");
		long id=UtilityHelper.getLong(customerJson, "Id");
		checkIdCustomerPresence(id);
		checkAccNoForPresence(accountNumber);
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
		long accountNumber=UtilityHelper.getLong(customerJson, "AccountNumber");
		customer.removePrimaryAccount(accountNumber);
	}


	// support methods
	protected void balanceCheck(long balanceAmount,long amount) throws BankException {
		if (balanceAmount < amount) {
			throw new BankException("Insuffecient balance");
		}
	}
		
	/*
	 * protected void putHistory(JSONObject customerJson) throws BankException,
	 * InputDefectException { UtilityHelper.nullCheck(customerJson);
	 * customer.putHistory(customerJson); }
	 */

	/*
	 * protected JSONObject historyJson(String type, long amount, long
	 * transactionId, long accountNumber, String description, long balance, Long
	 * TransactionAccountNumber) throws BankException { JSONObject json = new
	 * JSONObject(); UtilityHelper.put(json, "AccountNumber", accountNumber);
	 * UtilityHelper.put(json, "TransactionAccountNumber",
	 * TransactionAccountNumber); UtilityHelper.put(json, "Description",
	 * description); UtilityHelper.put(json, "TransactionAmount", amount);
	 * UtilityHelper.put(json, "TransactionType", type); UtilityHelper.put(json,
	 * "TransactionId", transactionId); UtilityHelper.put(json, "Balance", balance);
	 * return json; }
	 */
	
	protected TransactionHistory historyPojo(String type, long amount, long transactionId, long accountNumber,
			String description, long balance, Long TransactionAccountNumber) {
		TransactionHistory history = new TransactionHistory();
		history.setTransactionType(TransactionType.valueOf(type));
		history.setTransactionAmount(amount);
		history.setTransactionId(transactionId);
		history.setAccountNumber(accountNumber);
		history.setDescription(description);
		history.setBalance(balance);
		if(TransactionAccountNumber!=null) {
		history.setTransactionAccountNumber(TransactionAccountNumber);
		}
		return history;
		
		
		
		
	}

	protected void modifyMoney(long accountNumber, long closingBalance) throws BankException {
		customer.modifyMoney(accountNumber,closingBalance);
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
			TransactionHistory historySender = historyPojo("moneyTransfer", -amount, tId, accountNumber, description,
					balanceAmount - amount, trasactionAccountNumber);
			customer.putHistory(historySender);
			TransactionHistory historyReceiver = historyPojo("moneyTransfer", amount, tId, trasactionAccountNumber, description,
					tBalanceAmount + amount, accountNumber);
			customer.putHistory(historyReceiver);
			break;
		case "inactive":
			throw new BankException("your reciptant account is blocked");
		case "deleted":
			throw new BankException("your reciptant account is deleted");
		}
	}

	// check methods


	
	protected void checkIdUserPresence(long id) throws BankException, InputDefectException {
		customer.checkUserPresence(id, "Id");
	}

	protected void checkIdUserAbsence(long id) throws BankException, InputDefectException {
		customer.checkUserAbsence(id,"Id");
	}

	protected void checkIdCustomerAbsence(long id) throws BankException, InputDefectException {
		customer.checkCustomerAbsence(id,"Id");
	}

	protected void checkIdCustomerPresence(long id) throws BankException, InputDefectException {
		customer.checkCustomerPresence(id,"Id");
	}

	protected void checkAccNoForAbsence(long accountNumber) throws BankException, InputDefectException {
		customer.checkAccountAbsence(accountNumber, "AccountNumber");
	}

	protected void checkAccNoForPresence(long accountNumber) throws BankException, InputDefectException {
		customer.checkAccountPresence(accountNumber,"AccountNumber");
	}
}
