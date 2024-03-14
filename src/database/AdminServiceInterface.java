package database;

import org.json.JSONArray;
import pojo.Branch;
import pojo.Employees;
import utility.BankException;
import utility.InputDefectException;

public interface AdminServiceInterface extends EmployeeServiceInterface{
	
	// ADMIN operations
	
	public void createBranch(Branch branch) throws BankException;

	public void addAdmin(Employees admin) throws BankException;

	public void addEmployee(Employees employee) throws BankException;

	public void removeEmployee(long id) throws BankException;

	public JSONArray getAllBranchId() throws BankException;
	
	public void checkBranchAbsence(long value , String field) throws BankException, InputDefectException ;

	public void checkBranchPrecence(long value , String field) throws BankException, InputDefectException ;
	
	public void checkEmployeeAbsence(long value , String field) throws BankException, InputDefectException ;

	public void checkEmployeePrecence(long value , String field) throws BankException, InputDefectException ;

}
