package database;

import org.json.JSONArray;
import pojo.Branch;
import pojo.Employees;
import utility.BankException;
import utility.InputDefectException;

public class AdminService extends EmployeeService implements AdminServiceInterface{
	
	private AdminService(String url, String userName, String password) {
		super(url, userName, password);
	}
	
	private static class BillpoughAdmin{
		private static final AdminService adminService=new AdminService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
	
	public static AdminService getAdminService() {
		return BillpoughAdmin.adminService;
	}

	public void createBranch(Branch branch) throws BankException  {
		generalAdd("branch", branch);
	}
	
	public void addAdmin(Employees admin) throws BankException  {
		addEmployee(admin);
	}
	
	public void addEmployee(Employees employee) throws BankException {
		generalAdd("employees", employee);
	}
	
	public void removeEmployee(long id) throws BankException  {
		StringBuilder query= builder.deleteFrom("employees","Id");
		delete(query,id);
	}
	
	public JSONArray getAllBranchId() throws BankException  {
		return selectOne("branch","BranchId");
	}

	public void checkBranchAbsence(long value , String field) throws BankException, InputDefectException {
		checkLongAbsence(value, "branch", field, field);
	}

	public void checkBranchPrecence(long value, String field) throws BankException, InputDefectException {
		checkLongPresence(value, "branch", field, field);
	}
	
	public void checkEmployeeAbsence(long value, String field) throws BankException, InputDefectException {
		checkLongAbsence(value, "employees", field, field);
	}

	public void checkEmployeePrecence(long value, String field) throws BankException, InputDefectException {
		checkLongPresence(value, "employees", field, field);
	}
	
	
	
}
