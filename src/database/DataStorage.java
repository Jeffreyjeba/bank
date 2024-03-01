package database;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.BankException;




public interface DataStorage {
	public boolean bulkAdd(JSONArray json)throws BankException;
	public boolean add(CharSequence seq,JSONObject json)throws BankException;
	public JSONArray bulkSelect(CharSequence seq) throws BankException;
	public JSONObject select(CharSequence seq)throws BankException;
	public boolean bulkUpdate(JSONArray json) throws BankException;
	public boolean update(CharSequence seq,JSONObject json) throws BankException;
	public boolean delete(CharSequence seq,JSONObject json) throws BankException; 
}
