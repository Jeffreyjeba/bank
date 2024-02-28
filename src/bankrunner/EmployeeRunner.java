package bankrunner;


//import org.json.JSONException;
import org.json.JSONObject;
import bank.Authenticator;
import operations.Employee;
import utility.BankException;

public class EmployeeRunner extends CustomerRunner {
	
	Employee employee=new Employee();

	public void createUser() throws Exception {
		JSONObject json=new JSONObject();
		long id=getLong("Enter new Id  : ");
		String name=getString("Enter the name : ");
		String emailId=getString("Enter the EmailId : ");
		long phoneNumber=getLong("Enter the phone number : ");
		String type="customer";
		String password=null;
		json.put("Id",id);
		json.put("Name",name);
		json.put("EmailId",emailId);
		json.put("PhoneNumber",phoneNumber);
		json.put("UserType",type);
		json.put("Password",password);
		employee.addUsers(json);
	}
	
	public void addCustomers() throws Exception {
		JSONObject json=new JSONObject();
		Long customerId=null;
		int id=getNumber("Enter user id : ");
		long aadharNumber=getLong("Enter the Aathar number : ");
		String panNumber=getString("Enter the Pan number : ");
		String address=getString("Enter the address : ");
		json.put("CustomerId",customerId);
		json.put("Id",id);
		json.put("AadharNumber",aadharNumber);
		json.put("PanNumber",panNumber);
		json.put("Address",address);
		employee.addCustomers(json);
	}
	
	public void createAccount() throws Exception {
		JSONObject json=new JSONObject();
		long accountNumber=getLong("Enter the new account number : ");
		long id=getLong("Enter the user Id : ");
		int branchId=getBranchId();
		long balance=getLong("Enter the initial amount : ");
		String status="active";
		json.put("AccountNumber",accountNumber);
		json.put("Id",id);
		json.put("BranchId",branchId);
		json.put("Balance",balance);
		json.put("Status",status);	
		employee.createAccount(json);
	}
	
	public void deleteAccount() throws Exception {
		JSONObject json=new JSONObject();
		long accountNumber=getLong("Enter the AccountNumber");
		json.put("Status","deleted");
		json.put("AccountNumber",accountNumber);
		employee.deleteAccount(json);
	}
	
	public void deactivateAccount() throws Exception {
		JSONObject json=new JSONObject();
		long accountNumber=getLong("Enter the AccountNumber : ");
		json.put("Status","'inactive'");
		json.put("AccountNumber",accountNumber);
		employee.deleteAccount(json);
	}
	
	public void activateAccount() throws Exception {
		JSONObject json=new JSONObject();
		long accountNumber=getLong("Enter the AccountNumber");
		json.put("Status","'active'");
		json.put("AccountNumber",accountNumber);
		employee.deleteAccount(json);
	}
	
	@Override
	protected long getId() {
		return getLong("Enter the customers Id : ");
	}
	
	@Override
	protected long getAccountNumber() throws Exception {
		long accountNumber= getLong("Enter the customers Account Number : ");
		accountStatus(accountNumber);
		return accountNumber;
	}	
	
	protected int getBranchId() throws Exception {
		long empId=Authenticator.id.get();
		JSONObject json= employee.selectwhere("employees","Id="+empId,"BranchId");
		if(json==null) {
			throw new BankException("Please register employee");
		}
		return json.getInt("BranchId");
	}
}
