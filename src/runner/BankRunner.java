package runner;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import bank.Authenticator;
import bank.UserHirarchy;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;
import utility.Validation;

public class BankRunner {

	protected static Logger logger = Logger.getLogger("Bank");
	static {
		try {
			UtilityHelper.logSetter("JDBC", true, logger);
		}
		catch (BankException e) {
			logger.log(Level.WARNING, "An error occured at log setting", e);
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		BankRunner runner = new BankRunner();
		System.out.println("----------------------------");
		System.out.println("-----Welcome to REY Bank----");
		System.out.println("----------------------------");
		runner.run();
	}

	Authenticator auth = new Authenticator();

	private void run() {
		try {
			long userId = getUserId();
			auth.validateUser(userId);
			String password = getPassword();
			boolean access = auth.checkPassword(userId, password);
			if (access == false) {
				throw new BankException("wrong combination");
			}
			Authenticator.idTag(userId);
			UserHirarchy level = UserHirarchy.valueOf(auth.getAuthority(userId));

			switch (level) {
			case customer:
				customer(Authenticator.id.get());
				break;
			case employee:
				employee(Authenticator.id.get());
				break;
			case admin:
				admin(Authenticator.id.get());
				break;
			default:
				logger.info("Access miss match contact the bank");
			}
		} 
		catch (BankException e) {
			logger.log(Level.WARNING, e.getMessage());
			run();
		}
		catch (Exception e) {
			logger.log(Level.WARNING, "Something went wrong", e);
		}
	}
	// customer runner calling method
	private void customer(long userId) throws Exception {
		System.out.println("----------------------------");
		System.out.println("------welcome Customer------");
		System.out.println("----------------------------");
		CustomerRunner customer = new CustomerRunner();
		try {
			long[] accountArray = customer.getAccounts();
		    tagPrimaryAccount(customer, accountArray);
		} 
		catch (InputDefectException e) {
			logger.warning(e.getMessage());
		}
		catch (BankException e) {
			logger.warning(e.getMessage());
			run();
		}
		boolean cont = true;
		while (cont) {
			int option = getNumber(" \n 1-GetBalance\n 2-GetAccounts\n 3-SwitchAccounts\n" + " 4-debit\n"
					+ " 5-credit\n 6-Transfer\n 7-ResetPassword \n 8-Get History\n 9-view profile\n 10-switch Primary account"
					+ " \n 50-logout \n Enter your option : ");
			switch (option) {
			case 1:
				try {
					customer.getBalance();
				} 
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 2:
				try {
					long[] accounts = customer.getAccounts();
					System.out.println(Arrays.toString(accounts));
				} 
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 3:
				try {
					customer.switchAccount();
				} 
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					customer.debit();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 5:
				try {
					customer.credit();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 6:
				try {
					customer.moneyTransfer();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 7:
				try {
				customer.resetPassword();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 8:
				try {
					customer.transactionHistory();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 9:
				try {
					customer.viewProfile();;
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 10:
				try {
					customer.switchPrimaryAccount();;
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
					e.printStackTrace();
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
				
			case 50:
				customer.logout();
				run();
				break;
				
			default:
				System.out.println("Enter a valid option");

			}
			cont = getNumber("Enter 1 to continue : ") == 1;
		}

	}
	// employee runner calling method

	private void employee(long userId) throws Exception {
		System.out.println("----------------------------");
		System.out.println("------welcome Employee------");
		System.out.println("----------------------------");
		
		EmployeeRunner employee = new EmployeeRunner();
		boolean cont = true;
		while (cont) {
			int option = getNumber(
					" \n 1-Get Balance\n 2-Get Accounts\n 3-Switch Accounts\n 4-debit\n 5-credit\n 6-Transfer\n 7-Reset Password\n"
							+ " 8-Create user\n 9-Add Customers\n 10-Create Account\n 11-Deactivate Account\n 12-Delete Account\n "
							+ "13-Activate account\n 14-Transaction History\n 15-view profile \n 16-switch primary account\n"
							+ " 17-Activate Customer\n 18-Deactivate Customer\n  50-logout\n Enter any option : ");
			switch (option) {
			case 1:
				try {
					employee.getBalance();
				} 
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 2:
				try {
					long[] accounts = employee.getAccounts();
					System.out.println(Arrays.toString(accounts));
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 3:
				try {
				employee.switchAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 4:
				try {
					employee.debit();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 5:
				try {
					employee.credit();
				} 
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 6:
				try {
					employee.moneyTransfer();
				} 
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 7:
				try {
				employee.resetPassword();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 8:
				try {
				employee.createUser();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 9:
				try {
					employee.addCustomers();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 10:
				try {
					employee.createAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 11:
				try {
					employee.deactivateAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 12:
				try {
					employee.deleteAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 13:
				try {
					employee.activateAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 14:
				try {
					employee.transactionHistory();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 15:
				try {
					employee.viewProfile();;
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 16:
				try {
					employee.switchPrimaryAccount();;
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
					e.printStackTrace();
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 17:
				try {
					employee.activateCustomer();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 18:
				try {
					employee.deactivateCustomer();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 50:
				employee.logout();
				run();
				break;
			default:
				System.out.println("Enter a valid option");
			}
			int num = getNumber("Enter 1 if you want to continue : ");
			cont = (num == 1);
		}
	}
	// ADMIN runner calling method

	private void admin(long userId) throws Exception {
		System.out.println("----------------------------");
		System.out.println("welcome Admin");
		System.out.println("----------------------------");
		AdminRunner admin = new AdminRunner();
		boolean cont = true;
		while (cont) {
			int option = getNumber(
					"\n 1-Get Balance\n 2-Get Accounts\n 3-Switch Accounts\n 4-debit\n 5-credit\n 6-Transfer\n "
							+ "7-Reset Password\n 8-Create user\n 9-Add Customers\n 10-Create Account\n 11-Deactivate Account\n "
							+ "12-Delete Account\n 13-Activate account\n 14-Create Branch \n 15-Add Admin \n 16-Add Employee \n "
							+ "17-Remove Employee \n 18-Transaction History\n 19-view profile \n 20-switch primary account \n "
							+ "21-Activate Customer\n 22-Deactivate Customer\n  50-logout\n Enter any option :");

			switch (option) {
			case 1:
				try {
					admin.getBalance();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 2:
				try {
					long[] accounts = admin.getAccounts();
					System.out.println(Arrays.toString(accounts));
				} 
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 3:
				try {
				admin.switchAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 4:
				try {
					admin.debit();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 5:
				try {
					admin.credit();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 6:
				try {
					admin.moneyTransfer();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 7:
				admin.resetPassword();
				break;
			case 8:
				try {
				admin.createUser();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 9:
				try {
					admin.addCustomers();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 10:
				try {
					admin.createAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 11:
				try {
					admin.deactivateAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 12:
				try {
					admin.deleteAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 13:
				try {
					admin.activateAccount();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 14:
				try {
					admin.createBranch();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 15:
				try {
					admin.addAdmin();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 16:
				try {
					admin.addemployee();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 17:
				try {
				admin.removeEmployee();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 18:
				try {
					admin.transactionHistory();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 19:
				try {
					admin.viewProfile();;
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 20:
				try {
					admin.switchPrimaryAccount();;
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
					e.printStackTrace();
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			
			case 21:
				try {
					admin.activateCustomer();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 22:
				try {
					admin.deactivateCustomer();
				}
				catch (BankException e) {
					logger.warning(e.getMessage());
				}
				catch (InputDefectException e) {
					logger.warning(e.getMessage());
				}
				break;
			case 50:
				admin.logout();
				run();
				break;
			default:
				System.out.println("Enter a valid option");

			}
			int num = getNumber("Enter 1 if you want to continue : ");
			cont = (num == 1);
		}
	}
	// method to check the status of account

	private void statusCheck(long accountNumber) throws Exception {
		String status = new CustomerRunner().accountStatus(accountNumber);
		switch (status) {
		case "inactive":
			throw new BankException("This account is inactive ");
		case "deleted":
			throw new BankException("This account is deleted");
		}
	}
	// input validation methods
	
	protected String getPassword() {
		String password;
		do {
			password=getString("Please enter the password :");
			if (!Validation.validatePassword(password)) {
				System.out.println("Enter a valid password");
				System.out.println("Password must must atleast \n * A special "
						+ "character\n * A capital letter\n * A number\n * A small leter\n * "
						+ "Atleast it must have 8 characters");
			}		
		}while(!Validation.validatePassword(password));
		return password;
	}
	
	
	protected void tagPrimaryAccount(CustomerRunner customer,long[] accountArray) throws Exception {
		JSONObject json= customer.getPrimaryAccount();
		if(json==null) {
			int length = accountArray.length;
			if (length == 1) {
				Authenticator.accountTag(accountArray[0]);
				customer.setPrimaryAccount();
				statusCheck(accountArray[0]);
				
			}
			else {
				for (int loop = 0; loop < length; loop++) {
					System.out.println((loop + 1) + " Account Number --  " + accountArray[loop]);
				}
				int option = getNumber("Please enter the account you need to set as primary account");
				UtilityHelper.lengthIndexCheck(length, option );
				statusCheck(accountArray[option-1]);
				Authenticator.accountTag(accountArray[option - 1]);
				customer.setPrimaryAccount();
				
			}
		}
		else{
			Long primaryAccount= UtilityHelper.getLong(json,"AccountNumber");
			Authenticator.accountTag(primaryAccount);
			
		}
	}
	
	
	
	
	protected long getUserId() {
		long userId = getLong("Enter the user Id :");
		if (!Validation.validateUserId(Long.toString(userId))) {
			System.out.println("Enter the valid user Id");
			getUserId();
		}
		return userId;
	}
	protected String getMailId() {
		String mailId = getString("Enter the MailId :");
		if (!Validation.validateEmail(mailId)) {
			System.out.println("Enter the valid MailId");
			getMailId();
		}
		return mailId;
	}
	protected long getPhoneNumber() {
		long phoneNumber = getLong("Enter the PhoneNumber :");
		if (!Validation.validatePhoenNo(Long.toString(phoneNumber))){
			System.out.println("Enter the valid PhoenNumber");
			getPhoneNumber();
		}
		return phoneNumber;
	}
	protected long getAadharNumber() {
		long aadharNumber=getLong("Enter your aaddhar number : ");
		if (!Validation.validateAdharNumber(Long.toString(aadharNumber))){
			System.out.println("Enter the valid aadhar Number");
			getAadharNumber();
		}
		return aadharNumber;
	}
	protected String getPanId() {
		String panId = getString("Enter the PanId :");
		if (!Validation.validatePanNumber(panId)) {
			System.out.println("Enter the valid PanId");
			getPanId();
		}
		return panId;
	}
	protected long getAccountNum() {
		long accountNumber =getLong("Enter The Account Number");
		if (!Validation.validateAccountNumber(Long.toString(accountNumber))){ 
			System.out.println("Enter the valid Account Number");
			getAccountNum();
		}
		return accountNumber;
	}

	Scanner scan = new Scanner(System.in);
	// input methods
	protected int getNumber() {
		try {

			int temp = scan.nextInt();
			scan.nextLine();
			return temp;
		}
		catch (InputMismatchException x) {
			logger.warning("Please eneter an integer");
			System.out.print("Enter again : ");
			scan.nextLine();
			return getNumber();
		}
	}
	protected long getLong() {
		try {

			long temp = scan.nextLong();
			scan.nextLine();
			return temp;
		}
		catch (InputMismatchException x) {
			logger.warning("Please eneter an integer");
			System.out.print("Enter again : ");
			scan.nextLine();
			return getLong();
		}
	}
	protected long getLong(String str) {
		System.out.print(str);
		Long temp = getLong();
		return temp;
	}
	protected int getNumber(String str) {
		System.out.print(str);
		int temp = getNumber();
		return temp;
	}
	protected String getString() {
		return scan.nextLine();
	}
	protected String getString(String str) {
		System.out.print(str);
		return getString();
	}

}
