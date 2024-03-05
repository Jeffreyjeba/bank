package database;

import org.json.JSONObject;

import utility.BankException;
import utility.UtilityHelper;

public class AuthendicatorService extends JDBC implements AuthendicatorServiceInterface{

	public AuthendicatorService(String url, String userName, String password) {
		super(url, userName, password);
	}
	
	Query queryBuilder=new QueryBuilderMySql();

	@Override
	public String getAuthority(long id) throws BankException {
		StringBuilder query= queryBuilder.selectFromWhere("users", "Id=" + id, "UserType");
		JSONObject json = select(query);
		return UtilityHelper.getString(json,"UserType");
	}

	
	public JSONObject getPassword(long userId) throws BankException {
		StringBuilder query= queryBuilder.selectFromWhere("users", "Id=" + userId, "Password");
		return select(query);
	}
	
	public JSONObject getAttempts(long id) throws BankException {
		StringBuilder query= queryBuilder.selectFromWhere("users", "Id=" + id, "Attempts");
		return select(query);
	}
	
	public boolean attemptUpdate(JSONObject json,long id) throws BankException {
		StringBuilder query= queryBuilder.singleSetWhere("users", "Attempts", "Id", Long.toString(id));
		return add(query,json);
	}

}
