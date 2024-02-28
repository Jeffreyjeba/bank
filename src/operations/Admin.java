package operations;

import org.json.JSONArray;
import org.json.JSONObject;
import database.QueryBuilder;
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
		checkIdUserPresence(admin);
		checkForWorkersAbsence(admin);
		checkForBranchPresence(admin);
		String tableName="employees";
		generalAdd(tableName, admin);
	}
	public void addEmployee(JSONObject employee) throws Exception {
		UtilityHelper.nullCheck(employee);
		checkIdUserPresence(employee);
		checkForWorkersAbsence(employee);
		checkForBranchPresence(employee);
		String tableName="employees";
		generalAdd(tableName, employee);
	}
	public void removeEmployee(JSONObject employee) throws Exception {
		UtilityHelper.nullCheck(employee);
		checkForWorkersPresence(employee);
		String tableName="employees";
		StringBuilder query=new StringBuilder();
		QueryBuilder builder =new QueryBuilder(query);
		builder.deleteFromJson(tableName, employee);
		dtabase.delete(query, employee);
	}
	public JSONArray getAllBranchId() throws Exception {
		return selectOne("branch","BranchId");
	}
	// checkers methods
	protected void checkForBranchAbsence(JSONObject json) throws Exception {
		checkLongAbsence(json, "branch", "BranchId", "BranchId");
	}
	protected void checkForBranchPresence(JSONObject json) throws Exception {
		checkLongPresence(json, "branch", "BranchId", "BranchId");
	}
	protected void checkForWorkersAbsence(JSONObject json) throws Exception {
		checkLongAbsence(json,"employees","Id","Id");
	}
	protected void checkForWorkersPresence(JSONObject json) throws Exception {
		checkLongPresence(json,"employees","Id","Id");
	}
}
