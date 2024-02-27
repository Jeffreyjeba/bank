package database;

import org.json.JSONException;
import org.json.JSONObject;

public class QueryBuilder {

	private StringBuilder stringBuilder;

	public QueryBuilder(StringBuilder stringBuilder) {
		this.stringBuilder=stringBuilder;
	}



	// select
	private void select(String ...input) {
		stringBuilder.append("select ");
		addWithComma(input);
	}
	private void from(String tableName) {
		stringBuilder.append(" from "+tableName+" ");
	}
	private void where(String input) {
		stringBuilder.append(" where ");
		stringBuilder.append(input);
	}	

	public void selectAll(String tableName) {
		stringBuilder.append("select * from "+tableName+";");

	}
	public void selectFrom(String tableName,String...fields) { // use pass
		select(fields);
		from(tableName);
		close();
	}
	public void selectFromWhere(String tableName,String conditionField,String...fields) { // use pass
		select(fields);
		from(tableName);
		where(conditionField);
		close();
	}
	public void selectFromWherePrep(String tableName,String conditionField,String...fields) { // use pass
		select(fields);
		from(tableName);
		stringBuilder.append("where");
		stringBuilder.append(conditionField);
		stringBuilder.append("=?");
		close();
	}
	
	public void selectItem(String tableName,String itemName) {
		stringBuilder.append("select "+itemName+" from "+tableName+";");
	}
	public void selectAllFromWherePrep(String tableName,String conditionField) { // use pass
		stringBuilder.append("select * ");
		from(tableName);
		stringBuilder.append("where ");
		stringBuilder.append(conditionField);
		close();
	}





	//add
	private void insert(String tableName) {
		stringBuilder.append("insert into ");
		stringBuilder.append(tableName);

	}
	private void fields(String...input) {
		stringBuilder.append("(");
		addWithComma(input);
		stringBuilder.append(")");
	}
	private void values(int length) {
		stringBuilder.append(" values (");
		addQMark(length);
		stringBuilder.append(")");
	}
	public void addJsonPrepStatement(String tableName,JSONObject json) {
		String[] field=JSONObject.getNames(json);
		insert(tableName);
		fields(field);
		values(field.length);
		close();
		System.out.println(stringBuilder);
	}












	// update
	private void update(String tableName) {
		stringBuilder.append(" update ");
		stringBuilder.append(tableName);
	}


	private void set(String target,String value) {
		stringBuilder.append(" set ");
		stringBuilder.append(target);
		stringBuilder.append(" = ");
		stringBuilder.append(value);
	}

	private void where(String input,String condition) {
		stringBuilder.append(" where ");
		stringBuilder.append(input);
		stringBuilder.append(condition);
	}	
@Deprecated
	public void singleSetWhereJson(String tableName,JSONObject json) {
		update(tableName);
		String[] field =JSONObject.getNames(json);
		set(field[0],"?");
		where(field[1],"=?");
		close();
	}
	public void singleSetWhere(String tableName,String field,String conditionField) {
		update(tableName);
		set(field,"?");
		where(conditionField,"=?");
		close();
	}
	public void singleSetWhere(String tableName,String field,String conditionField,String coditionValue) {
		update(tableName);
		set(field,"?");
		where(conditionField,"=");
		stringBuilder.append(coditionValue);
		close();
	}
	public void setAccountStatus(String tableName,JSONObject json) throws JSONException {
		update(tableName);
		set("Status",json.getString("Status"));
		json.remove("Status");
		String[] field =JSONObject.getNames(json);
		where(field[0],"=?");
		close();
	}





	//delete
	public void deleteFromJson(String tableName,JSONObject json) {
		String[] field =JSONObject.getNames(json);
		stringBuilder.append("delete from ");
		stringBuilder.append(tableName);
		stringBuilder.append(" where ");
		stringBuilder.append(field[0]);
		stringBuilder.append(" = ?");
		close();
	}





	//create
	public void createTable(String tableName,String[] parametre) {
		stringBuilder.append("create table ");
		stringBuilder.append(tableName);
		stringBuilder.append(" (");
		addWithComma(parametre);
		stringBuilder.append(" );");
	}




	private void close() {
		stringBuilder.append(" ;");
	}

	private void addWithComma(String...input) {
		int length=input.length;
		for(int loop=0;length>loop;loop++) {
			stringBuilder.append(input[loop]).append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
	}

	private void addQMark(int length) {
		for(int loop=0;length>loop;loop++) {
			stringBuilder.append("?,");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
	}





}

