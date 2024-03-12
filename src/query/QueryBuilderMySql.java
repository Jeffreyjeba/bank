package query;

import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import pojo.BankMarker;
import utility.BankException;

public class QueryBuilderMySql implements Query {

	// select
	private void select(StringBuilder stringBuilder, String... input) {
		stringBuilder.append("select ");
		addWithComma(stringBuilder, input);
	}

	private void from(StringBuilder stringBuilder, String tableName) {
		stringBuilder.append(" from " + tableName + " ");
	}

	private void where(StringBuilder stringBuilder, String input) {
		stringBuilder.append(" where ");
		stringBuilder.append(input);
	}
	@Override
	public StringBuilder selectAll(String tableName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select * from " + tableName + ";");
		return stringBuilder;

	}
	@Override
	public StringBuilder selectFrom(String tableName, String... fields) { // use pass
		StringBuilder stringBuilder = new StringBuilder();
		select(stringBuilder, fields);
		from(stringBuilder, tableName);
		close(stringBuilder);
		return stringBuilder;
	}
	@Override
	public StringBuilder selectFromWhere(String tableName, String conditionField, String... fields) { // use pass
		StringBuilder stringBuilder = new StringBuilder();
		select(stringBuilder, fields);
		from(stringBuilder, tableName);
		where(stringBuilder, conditionField);
		close(stringBuilder);
		return stringBuilder;
	}
	@Override
	public StringBuilder selectFromWherePrep(String tableName, String conditionField, String... fields) { // use pass
		StringBuilder stringBuilder = new StringBuilder();
		select(stringBuilder, fields);
		from(stringBuilder, tableName);
		stringBuilder.append("where");
		stringBuilder.append(conditionField);
		stringBuilder.append("=?");
		close(stringBuilder);
		return stringBuilder;
	}
	@Override
	public StringBuilder selectItem(String tableName, String itemName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select " + itemName + " from " + tableName + ";");
		return stringBuilder;
	}
	@Override
	public StringBuilder selectAllFromWherePrep(String tableName, String conditionField) { // use pass
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select * ");
		from(stringBuilder, tableName);
		stringBuilder.append("where ");
		stringBuilder.append(conditionField);
		close(stringBuilder);
		return stringBuilder;
	}
	public StringBuilder selectAllCountFromWherePrep(String tableName, String conditionField) { // use pass
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select count(*) ");
		from(stringBuilder, tableName);
		stringBuilder.append("where ");
		stringBuilder.append(conditionField);
		close(stringBuilder);
		return stringBuilder;
	}
	@Override
	public StringBuilder viewCustomerProfile(long id) {
		return new StringBuilder("select users.Id,users.Name,users.EmailId,users.PhoneNumber,"
														+ "customers.Address from users left join c"
														+ "ustomers on users.Id=customers.Id where users.Id="+id+";");
	}
	
	
	
	

	// add
	private void insert(StringBuilder stringBuilder, String tableName) {
		stringBuilder.append("insert into ");
		stringBuilder.append(tableName);

	}

	private void fields(StringBuilder stringBuilder, String... input) {
		stringBuilder.append("(");
		addWithComma(stringBuilder, input);
		stringBuilder.append(")");
	}

	private void values(StringBuilder stringBuilder, int length) {
		stringBuilder.append(" values (");
		addQMark(stringBuilder, length);
		stringBuilder.append(")");
	}
	@Override
	public StringBuilder addJsonPrepStatement(String tableName, JSONObject json) {
		StringBuilder stringBuilder = new StringBuilder();
		String[] field = JSONObject.getNames(json);
		insert(stringBuilder, tableName);
		fields(stringBuilder, field);
		values(stringBuilder, field.length);
		close(stringBuilder);
		return stringBuilder;
	}

	// update
	private void update(StringBuilder stringBuilder, String tableName) {
		stringBuilder.append(" update ");
		stringBuilder.append(tableName);
	}

	private void set(StringBuilder stringBuilder, String target, String value) {
		stringBuilder.append(" set ");
		stringBuilder.append(target);
		stringBuilder.append(" = ");
		stringBuilder.append(value);
	}
	
	private void charSet(StringBuilder stringBuilder, String target, String value) {
		stringBuilder.append(" set ");
		stringBuilder.append(target);
		stringBuilder.append(" = ");
		stringBuilder.append("'");
		stringBuilder.append(value);
		stringBuilder.append("'");
	}

	private void where(StringBuilder stringBuilder, String input, String condition) {
		stringBuilder.append(" where ");
		stringBuilder.append(input);
		stringBuilder.append(condition);
	}
	@Override
	public StringBuilder singleSetWhere(String tableName, String field, String conditionField) {
		StringBuilder stringBuilder = new StringBuilder();
		update(stringBuilder, tableName);
		set(stringBuilder, field, "?");
		where(stringBuilder, conditionField, "=?");
		close(stringBuilder);
		return stringBuilder;
	}
	@Override
	public StringBuilder singleSetWhere(String tableName, String field, String conditionField, String coditionValue) {
		StringBuilder stringBuilder = new StringBuilder();
		update(stringBuilder, tableName);
		set(stringBuilder, field, "?");
		where(stringBuilder, conditionField, "=");
		stringBuilder.append(coditionValue);
		close(stringBuilder);
		return stringBuilder;
	}
	@Override
	public StringBuilder setStatus(String tableName, JSONObject json) throws BankException {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			update(stringBuilder, tableName);
			charSet(stringBuilder, "Status", json.getString("Status"));
			json.remove("Status");
			String[] field = JSONObject.getNames(json);
			where(stringBuilder, field[0], "=?");
			close(stringBuilder);
			return stringBuilder;
		} catch (JSONException e) {
			throw new BankException("Error 2 contact bank");
		}
	}

	// delete
	@Override
	public StringBuilder deleteFromJson(String tableName, JSONObject json) {
		StringBuilder stringBuilder = new StringBuilder();
		String[] field = JSONObject.getNames(json);
		stringBuilder.append("delete from ");
		stringBuilder.append(tableName);
		stringBuilder.append(" where ");
		stringBuilder.append(field[0]);
		stringBuilder.append(" = ?");
		close(stringBuilder);
		return stringBuilder;
	}

	// create
	@Override
	public StringBuilder createTable(String tableName, String[] parametre) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("create table ");
		stringBuilder.append(tableName);
		stringBuilder.append(" (");
		addWithComma(stringBuilder, parametre);
		stringBuilder.append(" );");
		return stringBuilder;
	}

	private void close(StringBuilder stringBuilder) {
		stringBuilder.append(" ;");
	}

	private void addWithComma(StringBuilder stringBuilder, String... input) {
		int length = input.length;
		for (int loop = 0; length > loop; loop++) {
			stringBuilder.append(input[loop]).append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
	}

	private void addQMark(StringBuilder stringBuilder, int length) {
		for (int loop = 0; length > loop; loop++) {
			stringBuilder.append("?,");
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
	}
	
	
	
	public void pojoToAddQuery(String tableName,BankMarker data) {
		StringBuilder field=new StringBuilder();
		StringBuilder value=new StringBuilder();
		field.append("Insert into table ");
		field.append(tableName);
		value.append(") values (");
	}
	
	private void setFieldName(BankMarker data,StringBuilder value) { //TODO
		Class className=data.getClass();
		Field[] field=className.getDeclaredFields();
		for(Field temp:field) {
			temp.setAccessible(true);
			String type = temp.getType().toString();
			if(new ArrayList<String>("int","long"))
		}
	}
	
	private String fieldToSetMethod(String fieldName) {
		return "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);	
	}
	
	
}
