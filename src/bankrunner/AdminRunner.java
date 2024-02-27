package bankrunner;

import org.json.JSONArray;
import org.json.JSONObject;

import operations.Admin;

public class AdminRunner extends EmployeeRunner {
	
	private Admin admin=new Admin();
	
	public void createBranch() throws Exception {
		JSONObject json=new JSONObject();
		int branchId=getNumber("Enter the branch id : ");
		String ifscCode="rey"+String.format("%05d",branchId);
		String branchName=getString("Enter the branch name : ");
		String address=getString("Enter the branch address : ");
		json.put("BranchId", branchId);
		json.put("IfscCode",ifscCode);
		json.put("BranchName", branchName);
		json.put("Address",address);
		admin.createBranch(json);
	}
	public void addemployee() throws Exception {
		JSONObject json=new JSONObject();
		long id=getLong("Enter the id : ");
		int branchId=getNumber("enter the branch id : ");
		json.put("Id",id);
		json.put("BranchId",branchId);
		json.put("Type","employee");
		admin.addEmployee(json);
	}
	public void addAdmin() throws Exception {
		JSONObject json=new JSONObject();
		long id=getLong("Enter the id : ");
		int branchId=getNumber("enter the branch id : ");
		json.put("Id",id);
		json.put("BranchId",branchId);
		json.put("Type","admin");
		admin.addAdmin(json);
	}
	public void removeEmployee() throws Exception {
		JSONObject json=new JSONObject();
		long id=getLong("Enter the employee id : ");
		json.put("Id",id);
		admin.removeEmployee(json);
	}
	@Override
	public void createAccount() throws Exception {
		JSONObject json=new JSONObject();
		long accountNumber=getLong("Enter the account number : ");
		long id=getLong("Enter the Id : ");
		JSONArray branchArray=admin.getAllBranchId();
		System.out.println(branchArray+"");
		int position=getNumber("Enter the position of the branch id : ");
		int branchId=(branchArray.getJSONObject(position-1)).getInt("BranchId");
		long balance=getLong("Enter the initial amount : ");
		String status="active";
		json.put("AccountNumber",accountNumber);
		json.put("Id",id);
		json.put("BranchId",branchId);
		json.put("Balance",balance);
		json.put("Status",status);	
		employee.createAccount(json);
	}
}
