package database;

import org.json.JSONObject;

import query.Query;
import query.QueryBuilderMySql;
import utility.BankException;
import utility.UtilityHelper;

public class AuthendicatorService extends DataStorageService implements AuthendicatorServiceInterface{

	private AuthendicatorService(String url, String userName, String password) {
		super(url, userName, password);
	}
	
	private static class BillpoughAuthendicator{
		private static final AuthendicatorService authendicatorService=new AuthendicatorService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
	
	public static AuthendicatorService getAuthendicatorService() {
		return BillpoughAuthendicator.authendicatorService;
	}
	
	Query builder=new QueryBuilderMySql();

	@Override
	public String getAuthority(long id) throws BankException {
		StringBuilder query= builder.selectFromWhere("users", "Id=" + id, "UserType");
		JSONObject json = select(query);
		return UtilityHelper.getString(json,"UserType");
	}

	@Override
	public JSONObject getPassword(long userId) throws BankException {
		StringBuilder query= builder.selectFromWhere("users", "Id=" + userId, "Password");
		return select(query);
	}
	
	@Override
	public JSONObject getAttempts(long id) throws BankException {
		StringBuilder query= builder.selectFromWhere("users", "Id=" + id, "Attempts");
		return select(query);
	}
	
	@Override
	public boolean attemptUpdate(int attempt,long id) throws BankException {
		StringBuilder query= builder.singleSetWhere("users", "Attempts", "Id", Long.toString(id));
		return update(query,attempt);
	}

	@Override
	public JSONObject getUserStatus(long id) throws BankException {
		StringBuilder query= builder.selectFromWhere("users","Id=" +id,"Status");
		return select(query);
	}
	
	

}
