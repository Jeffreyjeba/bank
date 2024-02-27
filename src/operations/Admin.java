package operations;

import org.json.JSONArray;
import org.json.JSONObject;
import database.QueryBuilder;
import utility.BankException;
import utility.UtilityHelper;



public class Admin extends Employee{


	public void createBranch(JSONObject branch) throws Exception {
		UtilityHelper.nullCheck(branch);
		checkForBranchAbsence(branch);
		String tableName="branch";
		generalAdd(tableName, branch);
	}
	public void addAdmin(JSONObject admin) throws Exception {
		UtilityHelper.nullCheck(admin);
		checkIdForPresence(admin);
		checkForWorkersAbsence(admin);
		checkForBranchPresence(admin);
		String tableName="employees";
		generalAdd(tableName, admin);
	}
	public void addEmployee(JSONObject employee) throws Exception {
		UtilityHelper.nullCheck(employee);
		checkIdForPresence(employee);
		checkForWorkersAbsence(employee);
		checkForBranchPresence(employee);
		String tableName="employees";
		generalAdd(tableName, employee);
	}
	public void removeEmployee(JSONObject employee) throws Exception {
		UtilityHelper.nullCheck(employee);
		String tableName="employees";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.deleteFromJson(tableName, employee);
		dtabase.delete(query, employee);
	}
	public JSONArray getAllBranchId() throws Exception {
		return selectOne("branch","BranchId");
	}
	protected void checkForBranchAbsence(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long branchId=json.getLong("BranchId");
		JSONObject json2=selectwhere("branch","BranchId="+branchId,"BranchId");
		if(json2!=null) { 
			throw new BankException("Branch already exist with BranchId : "+branchId);
		}
	}
	protected void checkForBranchPresence(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long branchId=json.getLong("BranchId");
		JSONObject json2=selectwhere("branch","BranchId="+branchId,"BranchId");
		if(json2==null) { 
			throw new BankException("Branch already exist with BranchId : "+branchId);
		}
	}
	protected void checkForWorkersAbsence(JSONObject json) throws Exception {
		UtilityHelper.nullCheck(json);
		long id=json.getLong("Id");
		JSONObject json2=selectwhere("employees","Id="+id,"Id");
		if(json2!=null) { 
			throw new BankException("Employee already exist with id : "+id);
		}
	}
}
