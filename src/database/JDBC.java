package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utility.BankException;
import utility.InputDefectException;

abstract public class JDBC implements DataStorage {

	private String url;
	private String userName;
	private String password;

	Query queryBuilder = new QueryBuilderMySql();

	public JDBC(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	public JSONArray selectOne(String tableName, String fieldName) throws BankException {
		StringBuilder query = queryBuilder.selectFrom(tableName, fieldName); // to be removed
		return bulkSelect(query);
	}

	public void generalAdd(String tableName, JSONObject employee) throws BankException, InputDefectException {
		StringBuilder query = queryBuilder.addJsonPrepStatement(tableName, employee);
		add(query, employee);
	}
	
	public JSONObject selectwhere(String tableName, String condition, String target) throws BankException {
		StringBuilder query =queryBuilder.selectFromWhere(tableName, condition, target);
		return select(query);
	}

	@Override
	public boolean add(CharSequence seq, JSONObject json) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, json);
				return preparedStatement.execute();
			}
		} catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support");
		}
	}

	@Override
	public boolean bulkAdd(JSONArray json) {
		// TODO
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
		} catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support");
		}
	}

	@Override
	public JSONArray bulkSelect(CharSequence seq) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement statement = connection.prepareStatement(seq.toString());) {
				System.out.println(seq);
				try (ResultSet set = statement.executeQuery(seq.toString());) {
					JSONArray jArray = new JSONArray();
					while (set.next()) {
						jArray.put(jsonMaker(set));
					}
					return jArray;
				}
			}
		} catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support");
		}
	}

	@Override
	public boolean update(CharSequence seq, JSONObject json) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, json);
				return preparedStatement.execute();
			}
		} catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support");
		}
	}

	@Override
	public boolean bulkUpdate(JSONArray json) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(CharSequence seq, JSONObject json) throws BankException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, json);
				return preparedStatement.execute();
			}
		} catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support");
		}
	}

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
			throw new BankException("technical error accured contact bank or technical support");
		}
	}

	private Connection getConnection() throws BankException {
		try {
			Connection connection = DriverManager.getConnection(url, userName, password);
			return connection;
		} catch (SQLException e) {
			throw new BankException("technical error accured contact bank or technical support");
		}
	}

	@SuppressWarnings("unchecked")
	private void setParameter(PreparedStatement statement, JSONObject json) throws BankException {
		try {
			Iterator<String> keys = json.keys();
			int index = 1;
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = json.get(key);
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
		} catch (SQLException | JSONException e) {
			throw new BankException("technical error accured contact bank or technical support");
		}

	}
}
