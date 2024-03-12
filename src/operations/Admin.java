package operations;

import org.json.JSONArray;
import org.json.JSONObject;

import bank.ServiceFactory;
import database.AdminServiceInterface;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;



public class Admin extends Employee{
	
	AdminServiceInterface admin=ServiceFactory.getAdminService();

	public void createBranch(JSONObject branch) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(branch);
		checkForBranchAbsence(branch);
		admin.createBranch(branch);
	}
	public void addAdmin(JSONObject admin) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(admin);
		checkIdUserPresence(admin);
		checkForWorkersAbsence(admin);
		checkForBranchPresence(admin);
		this.admin.addAdmin(admin);
	}
	public void addEmployee(JSONObject employee) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(employee);
		checkIdUserPresence(employee);
		checkForWorkersAbsence(employee);
		checkForBranchPresence(employee);
		admin.addEmployee(employee);
	}
	public void removeEmployee(JSONObject employee) throws BankException,InputDefectException  {
		UtilityHelper.nullCheck(employee);
		checkForWorkersPresence(employee);
		admin.removeEmployee(employee);
	}
	public JSONArray getAllBranchId() throws BankException  {
		return admin.getAllBranchId();
	}
	// checkers methods
	protected void checkForBranchAbsence(JSONObject branch) throws BankException,InputDefectException  {
		admin.checkBranchAbsence(branch,"BranchId");
	}
	protected void checkForBranchPresence(JSONObject branch) throws BankException,InputDefectException  {
		admin.checkBranchAbsence(branch, "BranchId");
	}
	protected void checkForWorkersAbsence(JSONObject employee) throws BankException,InputDefectException  {
		admin.checkEmployeeAbsence(employee,"Id");
	}
	protected void checkForWorkersPresence(JSONObject employee) throws BankException,InputDefectException  {
		admin.checkEmployeePrecence(employee,"Id");
	}
}
