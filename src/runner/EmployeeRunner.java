package runner;

import org.json.JSONObject;

import bank.ActiveStatus;
import bank.Authenticator;
import operations.Employee;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class EmployeeRunner extends CustomerRunner {
	
	Employee employee=new Employee();

	public void createUser() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long id=getUserId();
		String name=getString("Enter the name : ");
		String emailId=getMailId();
		long phoneNumber=getPhoneNumber();
		String type=getType();
		UtilityHelper.put(json,"Id",id);
		UtilityHelper.put(json,"Name",name);
		UtilityHelper.put(json,"EmailId",emailId);
		UtilityHelper.put(json,"PhoneNumber",phoneNumber);
		UtilityHelper.put(json,"UserType",type);
		employee.addUsers(json);
	}
	
	public void addCustomers() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long id=getUserId();
		long aadharNumber=getAadharNumber();
		String panNumber=getPanId();
		String address=getString("Enter the address : ");
		UtilityHelper.put(json,"Id",id);
		UtilityHelper.put(json,"AadharNumber",aadharNumber);
		UtilityHelper.put(json,"PanNumber",panNumber);
		UtilityHelper.put(json,"Address",address);
		employee.addCustomers(json);
	}
	
	public void createAccount() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long accountNumber=getAccountNum();
		long id=getUserId();
		int branchId=getBranchId();
		long balance=getLong("Enter the initial amount : ");
		String status="active";
		UtilityHelper.put(json,"AccountNumber",accountNumber);
		UtilityHelper.put(json,"Id",id);
		UtilityHelper.put(json,"BranchId",branchId);
		UtilityHelper.put(json,"Balance",balance);
		UtilityHelper.put(json,"Status",status);	
		employee.createAccount(json);
	}
	
	public void deleteAccount() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long accountNumber=getAccountNum();
		UtilityHelper.put(json,"Status","deleted");
		UtilityHelper.put(json,"AccountNumber",accountNumber);
		employee.deleteAccount(json);
	}
	
	public void deactivateAccount() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long accountNumber=getAccountNum();
		UtilityHelper.put(json,"Status",ActiveStatus.inactive.name());
		UtilityHelper.put(json,"AccountNumber",accountNumber);
		employee.deactivateAccount(json);
	}
	
	public void activateAccount() throws BankException,InputDefectException {
		JSONObject json=new JSONObject();
		long accountNumber=getAccountNum();
		UtilityHelper.put(json,"Status",ActiveStatus.active.name());
		UtilityHelper.put(json,"AccountNumber",accountNumber);
		employee.activateAccount(json);
	}
	
	public void deactivateCustomer() throws BankException, InputDefectException {
		JSONObject userJson= new JSONObject();
		long id=getId();
		UtilityHelper.put(userJson, "Status",ActiveStatus.inactive.name());
		UtilityHelper.put(userJson,"Id",id);
		employee.deactivateCustomer(userJson);
	}
	
	public void activateCustomer() throws BankException, InputDefectException {
		JSONObject userJson= new JSONObject();
		long id=getId();
		UtilityHelper.put(userJson, "Status",ActiveStatus.active.name());
		UtilityHelper.put(userJson,"Id",id);
		employee.activateCustomer(userJson);
	}
	
	//over ridden
	@Override
	protected long getId() {
		return getUserId();
	}
	@Override
	protected long getAccountNumber() throws BankException, InputDefectException {
		long accountNumber= getLong("Enter the customers Account Number : ");
		accountStatus(accountNumber);
		return accountNumber;
	}
	
	//to be over ridden
	protected int getBranchId() throws BankException,InputDefectException {
		long empId=Authenticator.id.get();
		JSONObject json= employee.getBranchId(empId);
		if(json==null) {
			throw new BankException("Please register employee");
		}
		return UtilityHelper.getInt(json,"BranchId");
	}
	protected String getType() {
		return "customer";
	}
	
}
