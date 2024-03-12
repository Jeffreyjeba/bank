package database;

import org.json.JSONArray;
import org.json.JSONObject;

import utility.BankException;
import utility.InputDefectException;

public class AdminService extends EmployeeService implements AdminServiceInterface{
	
	public AdminService(String url, String userName, String password) {
		super(url, userName, password);
	}

	public void createBranch(JSONObject branch) throws BankException  {
		generalAdd("branch", branch);
	}
	
	public void addAdmin(JSONObject admin) throws BankException  {
		generalAdd("employees", admin);
	}
	
	public void addEmployee(JSONObject employee) throws BankException {
		generalAdd("employees", employee);
	}
	
	public void removeEmployee(JSONObject employee) throws BankException  {
		StringBuilder query= builder.deleteFromJson("employees", employee);
		delete(query, employee);
	}
	
	public JSONArray getAllBranchId() throws BankException  {
		return selectOne("branch","BranchId");
	}

	public void checkBranchAbsence(JSONObject branch, String field) throws BankException, InputDefectException {
		checkLongAbsence(branch, "breanch", field, field);
	}

	public void checkBranchPrecence(JSONObject branch, String field) throws BankException, InputDefectException {
		checkLongPresence(branch, "branch", field, field);
	}
	
	public void checkEmployeeAbsence(JSONObject employee, String field) throws BankException, InputDefectException {
		checkLongAbsence(employee, "employees", field, field);
	}

	public void checkEmployeePrecence(JSONObject employee, String field) throws BankException, InputDefectException {
		checkLongPresence(employee, "employees", field, field);
	}
	
	
}
