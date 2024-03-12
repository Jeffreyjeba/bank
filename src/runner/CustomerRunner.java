package runner;

import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import bank.Authenticator;
import operations.Customer;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class CustomerRunner extends BankRunner {

	Customer customer = new Customer();

	public void getBalance() throws BankException, InputDefectException {
		JSONObject json = new JSONObject();
		UtilityHelper.put(json, "AccountNumber", getAccountNumber());
		long balance = customer.getBalance(json);
		System.out.println("Your balance is : " + balance);
	}

	public long[] getAccounts() throws BankException, InputDefectException {
		return getAccounts(getId());
	}
	
	private long[] getAccounts(long id) throws BankException, InputDefectException {
		JSONObject json = new JSONObject();
		UtilityHelper.put(json,"Id", id);
		return customer.getAccounts(json);
	}

	public void resetPassword() throws BankException, InputDefectException {
		System.out.println("Old password");
		String oldPassword = getPassword();
		auth.checkPassword(Authenticator.id.get(), oldPassword);
		System.out.println("New pass word");
		String newPassword = getPassword();
		String confoPassword = getPassword();
		if (!newPassword.equals(confoPassword)) {
			System.out.println("New passwords dosent match");
			resetPassword();
		}
		JSONObject json = new JSONObject();
		UtilityHelper.put(json,"Id", Authenticator.id.get());
		UtilityHelper.put(json,"Password",confoPassword);
		customer.resetPassword(json);
	}

	public void switchAccount() throws BankException, InputDefectException {
		long[] account = getAccounts();
		if (account.length == 1) {
			System.out.println("User only hava one account");
		} 
		else {
			System.out.println(Arrays.toString(account));
			int index = getNumber("Enter the position of the account : ") - 1;
			UtilityHelper.lengthIndexCheck(account.length, index);
			long accountNumber = account[index];
			customer.accountStatus(accountNumber);
			JSONObject json = new JSONObject();
			UtilityHelper.put(json,"AccountNumber", accountNumber);
			customer.switchAccount(json);
			System.out.println("Account switched to " + accountNumber);
		}
	}

	public void debit() throws BankException, InputDefectException {
		String password = getPassword();
		boolean pass = auth.checkPassword(Authenticator.id.get(), password);
		if (!pass) {
			logger.warning("Wrong pass word\nTry again");
			debit();
		} else {
			long accountNumber = getAccountNumber();
			long amount = getLong("Enter the amount you want to debit : ");
			String description = getString("Enter the description : ");
			JSONObject json = new JSONObject();
			UtilityHelper.put(json,"AccountNumber", accountNumber); 
			UtilityHelper.put(json,"Amount", amount); 
			UtilityHelper.put(json,"Description", description); 
			customer.debit(json);
		}
	}

	public void credit() throws BankException, InputDefectException {
		String password = getPassword();
		boolean pass = auth.checkPassword(Authenticator.id.get(), password);
		if (!pass) {
			System.out.println("Wrong pass word\nTry again");
			credit();
		} 
		else {
			long accountNumber = getAccountNumber();
			long amount = getLong("Enter the amount you want to credit : ");
			String description = getString("Enter the description : ");
			JSONObject json = new JSONObject();
			UtilityHelper.put(json,"AccountNumber", accountNumber); 
			UtilityHelper.put(json,"Description", description);
			UtilityHelper.put(json,"Amount", amount);
			customer.credit(json);
		}
	}

	public void moneyTransfer() throws BankException, InputDefectException {
		String password = getPassword();
		boolean pass = auth.checkPassword(Authenticator.id.get(), password);
		if (!pass) {
			System.out.println("Wrong pass word\nTry again");
			moneyTransfer();
		}
		else {
				JSONObject json = new JSONObject();
				UtilityHelper.put(json,"AccountNumber", getAccountNumber());
				long amount = getLong("Enter the amount you want to transfer : ");
				String description = getString("Enter the description : ");
				Long toAccount = getLong("Enter the account number you want to credit : ");
				String ifscCode = getString("Enter the ifsc code : ");
				UtilityHelper.put(json,"Description", description);
				UtilityHelper.put(json,"Amount", amount);
				UtilityHelper.put(json,"TransactionAccountNumber", toAccount);
				UtilityHelper.put(json,"IfscCode", ifscCode);
				customer.moneyTransfer(json);
		}
	}

	public String accountStatus(long accountNumber) throws BankException, InputDefectException {
		return customer.accountStatus(accountNumber);
	}

	public void transactionHistory() throws BankException, InputDefectException { 
			long accountNumber = getAccountNumber();
			JSONObject json = new JSONObject();
			UtilityHelper.put(json,"AccountNumber", accountNumber);
			int days= getNumber("Enter specified days of history viewed : ");
			long searchMilli=customer.searchRegion(customer.daystomilly(days));
			int totalPage=customer.getPages(json,2,searchMilli);
			if(totalPage<=0) {
				throw new BankException("no transactions made");
			}
			else if(totalPage==1) {
				JSONArray jArray= customer.transactionHistory(json, 2,1, searchMilli);
				printJarray(jArray);
				throw new BankException("One page availabele");
			}
			boolean iterator=true;
			do {
			int currentPage=getNumber("Enter a page between 1 to "+totalPage+" : ");
			UtilityHelper.lengthIndexCheck(totalPage,currentPage);
			JSONArray jArray= customer.transactionHistory(json, 2, currentPage, searchMilli);
			printJarray(jArray);
			iterator=getNumber("Enter 2 to continue ")==2;
			}while(iterator);
	}
	
	public void switchPrimaryAccount() throws BankException, InputDefectException {
		long id=getId();
		long[] accounts=getAccounts(id);
		System.out.println(Arrays.toString(accounts));
		int position=getNumber("Enter the position of the primary account : ");
		UtilityHelper.lengthIndexCheck(accounts.length, position);
		long accountnumber= accounts[position-1];
		JSONObject json= new JSONObject();
		UtilityHelper.put(json,"Id",id);
		UtilityHelper.put(json,"AccountNumber",accountnumber);
		customer.switchPrimaryAccount(json);
		// to tag account
	}
	
	public void logout() {
		customer.logout();
	}
	
	public void viewProfile() throws BankException, InputDefectException{
		try {
		JSONObject json= new JSONObject();
		UtilityHelper.put(json,"Id",getId());
		JSONObject resultJson= customer.viewProfile(json);
		System.out.println(resultJson.toString(4));
		}
		catch (JSONException e) {
			throw new BankException("Error 2 contact bank");
		}
	}
	
	public JSONObject getPrimaryAccount() throws BankException, InputDefectException {
		long userId=getId();
		JSONObject json = new JSONObject();
		UtilityHelper.put(json,"Id",userId);
		return customer.getPrimaryAccount(json);
	}
	
	public void setPrimaryAccount() throws BankException, InputDefectException {
		long accountNumber=getAccountNumber();
		JSONObject json = new JSONObject();
		UtilityHelper.put(json,"AccountNumber",accountNumber);
		customer.setPrimaryAccount(json);
	}
	
	// support method
	protected void printJarray(JSONArray jArray) throws BankException {
		try {
			int length = jArray.length();
			int index = 0;
			while (length > index) {
				System.out.println(jArray.getJSONObject(index).toString(4));
				index++;
			}
		} catch (JSONException e) {
			throw new BankException("Error 2 contact bank");
		}
	}
	

	// methods to be over ridden for employee compatibility
	protected long getId() {
		return Authenticator.id.get();
	}

	protected long getAccountNumber() throws BankException, InputDefectException {
		return Authenticator.accountNumber.get();
	}

}
