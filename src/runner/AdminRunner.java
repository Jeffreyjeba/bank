package runner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import operations.Admin;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class AdminRunner extends EmployeeRunner {
	
	private Admin admin=new Admin();
	
	public void createBranch() throws BankException,InputDefectException {
		
		JSONObject json=new JSONObject();
		int branchId=getNumber("Enter the branch id : ");
		String ifscCode="rey"+String.format("%05d",branchId);
		String branchName=getString("Enter the branch name : ");
		String address=getString("Enter the branch address : ");
		UtilityHelper.put(json,"BranchId", branchId);
		UtilityHelper.put(json,"IfscCode",ifscCode);
		UtilityHelper.put(json,"BranchName", branchName);
		UtilityHelper.put(json,"Address",address);
		admin.createBranch(json);
	}
	
	public void addemployee() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long id=getLong("Enter the id : ");
		int branchId=getNumber("enter the branch id : ");
		UtilityHelper.put(json,"Id",id);
		UtilityHelper.put(json,"BranchId",branchId);
		UtilityHelper.put(json,"Type","employee");
		admin.addEmployee(json);
	}
	
	public void addAdmin() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long id=getLong("Enter the id : ");
		int branchId=getNumber("enter the branch id : ");
		UtilityHelper.put(json,"Id",id);
		UtilityHelper.put(json,"BranchId",branchId);
		UtilityHelper.put(json,"Type","admin");
		admin.addAdmin(json);
	}
	
	public void removeEmployee() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long id=getLong("Enter the employee id : ");
		UtilityHelper.put(json,"Id",id);
		admin.removeEmployee(json);
	}
	
	@Override
	public int getBranchId() throws BankException, InputDefectException {
		JSONArray branchArray=admin.getAllBranchId();
		System.out.println(branchArray+"");
		int position=getNumber("Enter the position of the branch id : ");
		UtilityHelper.lengthIndexCheck(branchArray.length(),position-1);
		try {
		return (branchArray.getJSONObject(position-1)).getInt("BranchId");
		}
		catch (JSONException e) {
			throw new BankException("Error level 2 contact bank");
		}
	}
	
	@Override
	protected String getType() {
		System.out.println(" 1-customer\n 2-employee\n 3-admin");
		int option =getNumber("Enter a number between 1-3 ");
		switch(option) {
		case 1:
			return "customer";
		case 2:
			return "employee";
		case 3:
			return "admin";
		default:
			System.out.println("please enter a option between 1-3");
			getType();
			return "";
		}
	}
}
