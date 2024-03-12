package pojo;

import bank.TransactionType;

public class TransactionHistory implements BankMarker{
	private long transactionId;
	private long accountNumber;
	private long transactionAccountNumber;
	private TransactionType transactionType;
	private long transactionAmount;
	private long balance;
	private String Description;
	private String dateTime;
	
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public long getTransactionAccountNumber() {
		return transactionAccountNumber;
	}
	public void setTransactionAccountNumber(long transactionAccountNumber) {
		this.transactionAccountNumber = transactionAccountNumber;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public long getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
