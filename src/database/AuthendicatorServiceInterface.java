package database;

import org.json.JSONObject;
import utility.BankException;

public interface AuthendicatorServiceInterface {

	public String getAuthority(long id) throws BankException;

	public JSONObject getPassword(long userId) throws BankException;

	public JSONObject getAttempts(long id) throws BankException;

	public boolean attemptUpdate(int attempt, long id) throws BankException;
	
	public JSONObject getUserStatus(long id) throws BankException; 

}
