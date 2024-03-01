package bank;

import org.json.JSONException;
import org.json.JSONObject;

import database.DataStorage;
import database.JDBC;
import database.QueryBuilder;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class Authenticator {
	DataStorage dtabase = new JDBC("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");

	public String getAuthority(long id) throws Exception { // w
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere("users", "Id=" + id, "UserType");
		JSONObject json = dtabase.select(query);
		return json.getString("UserType");
	}

	public boolean checkPassword(long userId, String password) throws BankException,InputDefectException {
		password = UtilityHelper.passHasher(password);
		String originalPassword = getPassword(userId);
		return password.equals(originalPassword);
	}

	@SuppressWarnings("unused")
	private boolean attemptCheck(long id) throws BankException {
		int attempt = getAttempts(id);
		if (attempt >= 3) {
			throw new BankException("You cannot access this account \n contact bank");
		}
		return true;
	}

	@SuppressWarnings("unused")
	private void attemptUpdate(long id) throws JSONException, Exception {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		int attempt = getAttempts(id);
		attempt++;
		builder.singleSetWhere("users", "Attempts", "Id", Long.toString(id));
		dtabase.add(query, new JSONObject().put("Attempts", attempt));
	}

	private int getAttempts(long id) throws BankException {
		try {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere("users", "Id=" + id, "Attempts");
		return dtabase.select(query).getInt("Attempts");
		}
		catch (JSONException e) {
			throw new BankException("Error 2 conatct bank");
		}
	}

	private String getPassword(long userId) throws BankException { // w
		try {
		StringBuilder query = new StringBuilder();
		QueryBuilder builder = new QueryBuilder(query);
		builder.selectFromWhere("users", "Id=" + userId, "Password");
		JSONObject json = dtabase.select(query);
		if (json == null) {
			throw new BankException("wrong combination");
		}
		return json.getString("Password");
		}
		catch (JSONException e) {
			throw new BankException("Error 2 contact bank");
		}
	}

	public static ThreadLocal<Long> id = new ThreadLocal<Long>();
	public static ThreadLocal<Long> accountNumber = new ThreadLocal<Long>();

	public void idTag(long userId) {
		id.set(userId);
	}

	public void accountTag(long accountNum) {
		accountNumber.set(accountNum);
	}

}
