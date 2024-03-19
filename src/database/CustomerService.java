package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import bank.Priority;
import pojo.BankMarker;
import pojo.TransactionHistory;
import utility.BankException;
import utility.InputDefectException;
import utility.UtilityHelper;

public class CustomerService extends DataStorageService implements CustomerServiceInterface {

	protected CustomerService(String url, String userName, String password) {
		super(url, userName, password);
	}
	
	private static class BillpoughCustomer{
		private static final CustomerService customerService=new CustomerService("jdbc:mysql://localhost:3306/rey_bank", "root", "0000");
	}
	
	public static CustomerService getCustomerService() {
		return BillpoughCustomer.customerService;
	}

	public JSONObject getBalance(long accountNumber) throws BankException {
		StringBuilder query = builder.selectFromWhere("accounts","AccountNumber=" + accountNumber, "Balance");
		return select(query);
	}

	public JSONArray getAccounts(long id) throws BankException {
		StringBuilder query = builder.selectFromWhere("accounts", "Id=" + id, "AccountNumber");
		return bulkSelect(query);
	}

	public void resetPassword(long id,String password) throws BankException {
		StringBuilder query = builder.singleSetWhere("users", "Password", "Id", Long.toString(id));
		update(query, password);
	}

	public JSONObject accountStatus(long accountNumber) throws BankException {
		return selectWhere("accounts", "AccountNumber=" + accountNumber, "Status");
	}


	public JSONArray getTransactionHistory(long accountNumber,int quantity ,int page,long searchMilli) throws BankException {
		StringBuilder query = builder.selectAllFromWherePrep("transactionHistory",
								"AccountNumber=" + accountNumber+ " and TransactionId > "
								+searchMilli+" order by TransactionId asc limit "
								+quantity+" offset "+(page-1)*quantity);
		return bulkSelect(query);
	}
	
	public int pageCount(long accountNumber,int quantity,long searchMilli) throws BankException {
		StringBuilder countQuery = builder.selectAllCountFromWherePrep("transactionHistory",
									"AccountNumber=" + accountNumber
									+ " and TransactionId > "+searchMilli);
		float count= UtilityHelper.getInt(select(countQuery),"count(*)");
		return (int) Math.ceil(count/quantity); 
	}

	public JSONObject viewProfile(long id) throws BankException {
		StringBuilder query=builder.viewCustomerProfile(id);
		JSONObject jsonResult= select(query);
		JSONArray jsonArray=getAccounts(id);
		return UtilityHelper.put(jsonResult,"AccountNumber", jsonArray);
	}
	
	public JSONObject getPrimaryAccount(long id) throws BankException {
		StringBuilder query =builder.selectFromWhere("accounts","Id = "+id+" and Priority='primary'","AccountNumber");
		return select(query);
	}
	
	//TOAADD
	
	
	public void creditDebitOutBank(TransactionHistory history) throws BankException {
		Connection connection=null;
		try {
			connection=getConnection();
			connection.setAutoCommit(false);
			updateHistory(connection,history);
			updateMoney(connection, history);
			connection.commit();
		}
		catch (BankException | SQLException e) {
			try {
				connection.rollback();
				throw new BankException("technical error accured contact bank or technical support",e);
			}catch (SQLException er) {
				er.printStackTrace();
				throw new BankException("rollback error accured contact bank or technical support",er);
			}
		}
		finally {
			try {
				if(connection!=null) {
					connection.close();
				}
			}
			catch(Exception e) {
				throw new BankException("cannot close resource" ,e);
			}
		}	
	}
	
	public void inBank(TransactionHistory historySender,TransactionHistory historyReceiver) throws BankException {
		Connection connection=null;
		try {
			connection=getConnection();
			connection.setAutoCommit(false);
			updateHistory(connection,historySender );
			updateMoney(connection, historySender);
			updateHistory(connection, historyReceiver);
			updateMoney(connection, historyReceiver);
			connection.commit();
		}
		catch (BankException | SQLException e) {
			try {
				connection.rollback();
				throw new BankException("technical error accured contact bank or technical support",e);
			}catch (SQLException er) {
				throw new BankException("rollback error accured contact bank or technical support",er);
			}
		}
		finally {
			try {
				if(connection!=null) {
					connection.close();
				}
			}
			catch(Exception e) {
				throw new BankException("cannot close resource" ,e);
			}
		}	
	}
	
	public void setPrimaryAccount(long accountNumber) throws BankException {
		updatePriority(accountNumber, Priority.primary);
	}
	
	public void removePrimaryAccount(long accountNumber) throws BankException {
		updatePriority(accountNumber, null);
	}
	

	public void checkUserPresence(long value, String field) throws BankException, InputDefectException {
		checkLongPresence(value, "users", field, field);
	}

	public void checkUserAbsence(long value, String field) throws BankException, InputDefectException {
		checkLongAbsence(value, "users", field, field);
	}

	public void checkCustomerAbsence(long value, String field) throws BankException, InputDefectException {
		checkLongAbsence(value, "customers", field, field);
	}

	public void checkCustomerPresence(long value, String field) throws BankException, InputDefectException {
		checkLongPresence(value, "customers", field, field);
	}

	public void checkAccountAbsence(long value, String field) throws BankException, InputDefectException {
		checkLongAbsence(value, "accounts", field, field);
	}

	public void checkAccountPresence(long value, String field) throws BankException, InputDefectException {
		checkLongPresence(value, "accounts", field, field);
	}
	

	private void updateHistory(Connection connection,TransactionHistory history) throws BankException, SQLException {
		StringBuilder historyQuery=builder.pojoToAddQuery("transactionHistory",history);
		try(PreparedStatement historyStatement=connection.prepareStatement(historyQuery.toString())) {
			setParameter(historyStatement, history);
			historyStatement.execute();
		}
	}
	
	private void updateMoney(Connection connection,TransactionHistory history) throws SQLException, BankException {
		long accountNumber=history.getAccountNumber();
		long balance=history.getBalance();
		StringBuilder modifyMoneyQuery = builder.singleSetWhere("accounts", "Balance", "AccountNumber");
		try(PreparedStatement updtaeStatement=connection.prepareStatement(modifyMoneyQuery.toString())){
			setParameter(updtaeStatement,balance,accountNumber);
			updtaeStatement.execute();	
		}
	}
	
	// support methods
	protected void checkLongAbsence(long value, String tableName, String fieldName, String selectionField)throws BankException {
		JSONObject resultJson = selectWhere(tableName, fieldName + "=" + value, selectionField);
		if (resultJson != null) {
			throw new BankException(selectionField + "  : " + value + " is alreday present");
		}
	}

	protected void checkLongPresence(long value, String tableName, String fieldName, String selectionField)throws BankException{
		JSONObject resultJson = selectWhere(tableName, fieldName + "=" + value, selectionField);
		if (resultJson == null) {
			throw new BankException(selectionField + " : " + value + " is not available");
		}
	}
	
	
	protected JSONArray selectOne(String tableName, String fieldName) throws BankException {
		StringBuilder query = builder.selectFrom(tableName, fieldName);
		return bulkSelect(query);
	}

	protected void generalAdd(String tableName, BankMarker data) throws BankException {
		StringBuilder query = builder.pojoToAddQuery(tableName, data);
		add(query, data);
	}
	
	protected JSONObject selectWhere(String tableName, String condition, String target) throws BankException {
		StringBuilder query =builder.selectFromWhere(tableName, condition, target);
		return select(query);
	}
	
	protected void updatePriority(long accountNumber,Priority priority) throws BankException {
		StringBuilder query=builder.singleSetWhere("accounts","Priority","AccountNumber",Long.toString(accountNumber));
		if(priority!=null) {
			update(query, priority.name()); 
		}
		else {
			String nullString=null;
			update(query,nullString);
		}
	}

}
