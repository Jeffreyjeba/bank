package bank;

import database.AdminService;
import database.AdminServiceInterface;
import database.AuthendicatorService;
import database.AuthendicatorServiceInterface;
import database.CustomerService;
import database.CustomerServiceInterface;
import database.EmployeeService;
import database.EmployeeServiceInterface;

public class ServiceFactory {
	
	public static CustomerServiceInterface getCustomerService() {
		return new CustomerService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
	
	public static EmployeeServiceInterface getEmployeeService() {
		return new EmployeeService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
	
	public static AdminServiceInterface getAdminService() {
		return new AdminService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
	
	public static AuthendicatorServiceInterface getAuthendicatorService() {
		return new AuthendicatorService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
}
