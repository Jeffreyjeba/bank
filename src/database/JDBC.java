package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

public class JDBC implements DataStorage {

	private String url;
	private String userName;
	private String password;

	public JDBC(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public boolean add(CharSequence seq, JSONObject json) throws SQLException, JSONException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, json);
				System.out.println(preparedStatement);
				return preparedStatement.execute();
			}
		}
	}

	@Override
	public boolean bulkAdd(JSONArray json) {
		// TODO
		return false;
	}

	@Override
	public JSONObject select(CharSequence seq) throws SQLException, JSONException {
		try (Connection connection = getConnection();) {
			try (Statement statement = connection.createStatement();) {
				try (ResultSet set = statement.executeQuery(seq.toString());) {
					if(set.next()) {
					return jsonMaker(set);
					}
					return null;
					
				}
			}
		}
	}

	@Override
	public JSONArray bulkSelect(CharSequence seq) throws SQLException, JSONException {
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
		}
	}

	@Override
	public boolean update(CharSequence seq, JSONObject json) throws Exception {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, json);
				System.out.println(preparedStatement);
				return preparedStatement.execute();
			}
		}
	}

	@Override
	public boolean bulkUpdate(JSONArray json) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(CharSequence seq, JSONObject json) throws Exception {
		try (Connection connection = getConnection();) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(seq.toString());) {
				setParameter(preparedStatement, json);
				return preparedStatement.execute();
			}
		}
	}

	private JSONObject jsonMaker(ResultSet set) throws SQLException, JSONException {
		JSONObject json = new JSONObject();
		ResultSetMetaData metaData = set.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int loop = 1; columnCount >= loop; loop++) {
			String columnName = metaData.getColumnName(loop);
			Object value = set.getObject(loop);
			json.put(columnName, value);
		}
		return json;
	}

	private Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(url, userName, password);
		return connection;
	}

	@SuppressWarnings("unused")
	private ResultSet getFieldsData(Connection connection, String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		return metaData.getColumns(null, null, tableName, null);
	}

	@SuppressWarnings("unchecked")
	private void setParameter(PreparedStatement statement, JSONObject json) throws JSONException, SQLException {
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
				System.out.println("Type bounce");
				break;
			}
			index++;

		}

	}

}
