package bankrunner;


import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import bank.Authenticator;
import operations.Customer;
import utility.BankException;
import utility.UtilityHelper;

public class CustomerRunner extends BankRunner {
	
	Customer customer= new Customer();
	
	public void getBalance() throws Exception {
		JSONObject json=new JSONObject();
		json.put("AccountNumber",getAccountNumber());
		JSONObject resultJson=customer.getBalance(json);
		if(resultJson==null) {
			throw new BankException("No such account exist");
		}
		long balance= resultJson.getLong("Balance");
		System.out.println("Your balance is : "+ balance);
	}

	public long[] getAccounts() throws Exception {
		return customer.getAccounts(getId());
	}

	public void resetPassword() throws Exception {
		System.out.println("Old password");
		String oldPassword=getPassword();
		auth.checkPassword(Authenticator.id.get(), oldPassword);
		System.out.println("New pass word");
		String newPassword=getPassword();
		String confoPassword=getPassword();
		if(!newPassword.equals(confoPassword)) {
			System.out.println("New passwords dosent match");
			resetPassword();
		}
		JSONObject json= new JSONObject();
		json.put("Id", Authenticator.id.get());
		json.put("Password",UtilityHelper.passHasher(confoPassword));
		customer.resetPassword(json);
	}
	
	public void switchAccount() throws Exception {
		long[] account =getAccounts();
		if (account.length == 1) {
			System.out.println("User only hava one account");
		}
		else {
			System.out.println(Arrays.toString(account));
			int index=getNumber("Enter the position of the account : ")-1;
			UtilityHelper.lengthIndexCheck(account.length,index);
			long accountNumber=account[index];
			JSONObject json=new JSONObject();
			json.put("AccountNumeber",accountNumber);
			customer.switchAccount(json);
			System.out.println("Account switched to "+accountNumber);
		}
	}

	public void debit() throws Exception {
		String password=getPassword();
		boolean pass=auth.checkPassword(getId(), password);
		if(!pass) {
			logger.warning("Wrong pass word\nTry again");
			debit();
		}
		else {
			long amount=getLong("Enter the amount you want to debit : ");
			String description=getString("Enter the description : ");
			JSONObject json=new JSONObject();
			json.put("AccountNumber",getAccountNumber());
			json.put("Amount",amount);
			json.put("Description",description);
			customer.debit(json);
		}
	}
	
	public void credit() throws Exception {
		String password=getPassword();
		boolean pass=auth.checkPassword(getId(), password);
		if(!pass) {
			System.out.println("Wrong pass word\nTry again");
			credit();
		}
		else {
			long amount=getLong("Enter the amount you want to credit : ");
			String description=getString("Enter the description : ");
			JSONObject json=new JSONObject();
			json.put("AccountNumber",getAccountNumber());
			json.put("Description", description);
			json.put("Amount",amount);
			customer.credit(json);
		}
	}

	public void moneyTransfer() throws Exception {
		String password=getPassword();
		boolean pass=auth.checkPassword(getId(), password);
		if(!pass) {
			System.out.println("Wrong pass word\nTry again");
			moneyTransfer();
		}
		else {
			long amount=getLong("Enter the amount you want to transfer : ");
			String description=getString("Enter the description : ");
			Long toaccoun=getLong("Enter the account number you want to credit : ");
			// TODO String ifscCode=getString("Enter the ifsc code : ");
			JSONObject json=new JSONObject();
			json.put("AccountNumber",getAccountNumber());
			json.put("Description", description);
			json.put("Amount",amount);
			json.put("TransactionAccountNumber",toaccoun);
			customer.moneyTransfer(json);
		}
	}
	
	public String accountStatus(long accountNumber) throws Exception {
		return customer.accountStatus(accountNumber);
	}
	
	public void transactionHistory() throws Exception {
		long accountNumber=getAccountNumber();
		JSONObject json	=new JSONObject();
		json.put("AccountNumber",accountNumber);
		JSONArray jArray= customer.transactionHistory(json);
		printJarray(jArray);
	}
	// support method
	protected void printJarray(JSONArray jArray) throws JSONException {
		int length=jArray.length();
		int index=0;
		while (length>index) {
			System.out.println(jArray.getJSONObject(index));
			index++;
		}	
	}
	//methods to be over ridden for employee compatibility
	protected long getId() {
		return Authenticator.id.get();
	}
	
	protected long getAccountNumber() throws Exception {
		return Authenticator.accountNumber.get();
	}

}
