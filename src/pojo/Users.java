package pojo;

import bank.ActiveStatus;
import bank.UserHirarchy;

public class Users implements BankMarker{
	private Long id;
	private String name;
	private String emailId;
	private Long phoneNumber;
	private UserHirarchy userType;
	private String password;
	private ActiveStatus status;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public UserHirarchy getUserType() {
		return userType;
	}
	public void setUserType(UserHirarchy userType) {
		this.userType = userType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ActiveStatus getStatus() {
		return status;
	}
	public void setStatus(ActiveStatus status) {
		this.status = status;
	}

}
