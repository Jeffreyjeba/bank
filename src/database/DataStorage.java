package database;

import org.json.JSONArray;
import org.json.JSONObject;



public interface DataStorage {
	public boolean bulkAdd(JSONArray json)throws Exception;
	public boolean add(CharSequence seq,JSONObject json)throws Exception;
	public JSONArray bulkSelect(CharSequence seq) throws Exception;
	public JSONObject select(CharSequence seq)throws Exception;
	public boolean bulkUpdate(JSONArray json) throws Exception;
	public boolean update(CharSequence seq,JSONObject json) throws Exception;
	public boolean delete(CharSequence seq,JSONObject json) throws Exception; 
}
