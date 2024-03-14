package database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pojo.BankMarker;
import query.Query;
import query.QueryBuilderMySql;
import utility.BankException;

abstract public class DataStorageService implements DataStorage {

	private String url;
	private String userName;
	private String password;

	Query builder = new QueryBuilderMySql();

	public DataStorageService(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}


	@Override
	public boolean add(CharSequence seq, BankMarker input) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, input);
				return preparedStatement.execute();
			}
		}
		catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}
	
	@Override
	public boolean bulkAdd(CharSequence seq, BankMarker input) throws BankException{
		
		return false;
	}

	@Override
	public JSONObject select(CharSequence seq) throws BankException {
		try (Connection connection = getConnection();) {
			try (Statement statement = connection.createStatement();) {
				try (ResultSet set = statement.executeQuery(seq.toString());) {
					if (set.next()) {
						return jsonMaker(set);
					}
					return null;
				}
			}
		}
		catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}

	@Override
	public JSONArray bulkSelect(CharSequence seq) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement statement = connection.prepareStatement(seq.toString());) {
				try (ResultSet set = statement.executeQuery(seq.toString());) {
					JSONArray jArray = new JSONArray();
					while (set.next()) {
						jArray.put(jsonMaker(set));
					}
					return jArray;
				}
			}
		}
		catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}
	
	@Override
	public boolean update(CharSequence seq,Object...input) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement,input);
				return preparedStatement.execute();
			}
		}
		catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}
	
	@Override
	public boolean delete(CharSequence seq,Object...input) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, input);
				return preparedStatement.execute();
			}
		}
		catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}
	
	// support 
	
	
	private JSONObject jsonMaker(ResultSet set) throws BankException {
		try {
			JSONObject json = new JSONObject();
			ResultSetMetaData metaData = set.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int loop = 1; columnCount >= loop; loop++) {
				String columnName = metaData.getColumnName(loop);
				Object value = set.getObject(loop);
				json.put(columnName, value);
			}
			return json;
		} catch (SQLException | JSONException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}

	protected Connection getConnection() throws BankException {
		try {
			Connection connection = DriverManager.getConnection(url, userName, password);
			return connection;
		} 
		catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}
	
	
	protected void setParameter(PreparedStatement statement,Object...input) throws BankException {
		try {
			int index=1;
			for(Object obj:input) {
				Object value = obj;
				if(value!=null) {
					String type = value.getClass().getName();
					switch (type) {
					case "java.lang.String":
						statement.setString(index, (String) value);
						break;
					case "java.lang.Integer":
						statement.setInt(index, (Integer) value);
						break;
					case "java.lang.Long":
						statement.setLong(index, (Long) value);
						break;
					default:
						throw new BankException("Type bounce");
					}
					index++;
				}
				else {
					statement.setObject(index,null);
					index++;
				}
			}
		} 
		catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected void setParameter(PreparedStatement statement,BankMarker data) throws BankException {
		try {
			Class className=data.getClass();
			Field[] field=className.getDeclaredFields();
			int index=1;
			for(Field temp:field) {
				temp.setAccessible(true);
				Object valueObject= temp.get(data);
				if(valueObject!=null) {
					String type=valueObject.getClass().getName();
					switch (type) {
					case "java.lang.String":
						statement.setString(index, (String) valueObject);
						break;
					case "java.lang.Integer":
						statement.setInt(index, (Integer) valueObject);
						break;
					case "java.lang.Long":
						statement.setLong(index, (Long) valueObject);
						break;
					case "bank.AccountStatus":
					case "bank.Priority":
					case "bank.EmployeeType":
					case "bank.TransactionType":
					case "bank.UserHirarchy":
					case "bank.ActiveStatus":
						statement.setString(index,valueObject.toString());
						break;
					default:
						throw new BankException("Type bounce");
					}
					index++;	
				}	
			}
		}catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			throw new BankException("technical error accured contact bank or technical support",e);
		}
	}
	
}
