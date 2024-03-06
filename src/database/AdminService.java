package database;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.BankException;
import utility.InputDefectException;

public class AdminService extends EmployeeService implements AdminServiceInterface{
	
	public AdminService(String url, String userName, String password) {
		super(url, userName, password);
	}

	public void createBranch(JSONObject branch) throws BankException,InputDefectException  {
		generalAdd("branch", branch);
	}
	
	public void addAdmin(JSONObject admin) throws BankException,InputDefectException  {
		generalAdd("employees", admin);
	}
	
	public void addEmployee(JSONObject employee) throws BankException,InputDefectException  {
		generalAdd("employees", employee);
	}
	
	public void removeEmployee(JSONObject employee) throws BankException,InputDefectException  {
		StringBuilder query= queryBuilder.deleteFromJson("employees", employee);
		delete(query, employee);
	}
	
	public JSONArray getAllBranchId() throws BankException  {
		return selectOne("branch","BranchId");
	}

	public void checkBranchAbsence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongAbsence(json, "breanch", field, field);
	}

	public void checkBranchPrecence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongPresence(json, "branch", field, field);
	}
	
	public void checkEmployeeAbsence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongAbsence(json, "employees", field, field);
	}

	public void checkEmployeePrecence(JSONObject json, String field) throws BankException, InputDefectException {
		checkLongPresence(json, "employees", field, field);
	}
	
	
}
