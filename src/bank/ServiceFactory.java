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
		return CustomerService.getCustomerService();
	}
	
	public static EmployeeServiceInterface getEmployeeService() {
		return EmployeeService.getEmployeeService();
	}
	
	public static AdminServiceInterface getAdminService() {
		return AdminService.getAdminService();
	}
	
	public static AuthendicatorServiceInterface getAuthendicatorService() {
		return AuthendicatorService.getAuthendicatorService();
	}
}
