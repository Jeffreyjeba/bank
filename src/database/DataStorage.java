package database;

import org.json.JSONArray;
import org.json.JSONObject;

import pojo.BankMarker;
import utility.BankException;

public interface DataStorage {
	public boolean add(CharSequence seq, BankMarker input)throws BankException;
	public boolean bulkAdd(CharSequence seq, BankMarker input) throws BankException;
	
	public JSONArray bulkSelect(CharSequence seq) throws BankException;
	public JSONObject select(CharSequence seq)throws BankException;
	public boolean update(CharSequence seq,Object...input) throws BankException;
	public boolean delete(CharSequence seq,Object...input) throws BankException; 
}
