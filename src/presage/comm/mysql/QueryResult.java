/*
 * Created on 17-Jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package presage.comm.mysql;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

/**
 * @author Brendan
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class QueryResult {

	public ResultSet resultset;
	public Statement statement;

	public QueryResult(ResultSet r, Statement s) {
		resultset = r;
		statement = s;

		// try {
		// outputToscreen();
		// } catch (SQLException e) {
		// System.err.println("QueryResult ERROR" + e);
		// }
	}

	public int rowCount() {

		int rowCount = 0;
		try {
			resultset.last();
			rowCount = resultset.getRow();
			resultset.first();
		} catch (SQLException e) {
			System.err.println("QueryResult.rowCount(): " + e);
		}
		return rowCount;
	} // ends the rowCount method

	public void outputToscreen() throws SQLException {

		System.out.println("QueryResult screen output: " + rowCount()
				+ " rows.");

		ResultSetMetaData md = resultset.getMetaData();
		int columns = md.getColumnCount();

		// Get column names
		String row = "";

		for (int i = 1; i <= columns; i++) {
			row += "	" + md.getColumnName(i);
		}

		System.out.println(row);

		// Get row data
		resultset.beforeFirst();
		while (resultset.next()) {
			row = "";
			for (int i = 1; i <= columns; i++) {
				row += "	" + resultset.getObject(i);
			}
			System.out.println(row);
		}
	}

	public boolean contains(Object o1, String columnId) {
		Object o2;
		try {
			resultset.beforeFirst();
			while (resultset.next()) {
				o2 = resultset.getObject(columnId);
				if (o1.equals(o2)) {
					// System.out.println(o1 + "==" + o2);
					return true;
				}
				// System.out.println(o1 + "!=" + o2);
			}
		} catch (SQLException e) {
			System.err.println("QueryResult.contains( " + o1 + ", " + columnId
					+ "): " + e);
		}
		return false;
	}

	public ArrayList colToArrayList(String col_Id) {

		ArrayList list = new ArrayList();
		try {
			resultset.beforeFirst();
			while (resultset.next()) {
				resultset.getObject(col_Id);
				list.add(resultset.getObject(col_Id));
			}
		} catch (SQLException e) {
			System.err.println("QueryResult.toArray(): " + e);
		}
		return list;
	}

	public boolean empty() {
		// count the rows in the result.
		if (rowCount() == 0) {
			return true;
		} else {
			return false;
		}
	}

	// public boolean empty() {
	// try {
	// resultset.last();
	// return false;
	// } catch (Exception e) {
	// return true;
	// }
	// }

	public void absolute(int i) throws SQLException {
		resultset.absolute(i);
	}
	
	public void beforeFirst() throws SQLException {
		resultset.beforeFirst();
	}

	public boolean first() throws SQLException {
		return resultset.first();
	}

	public boolean last() throws SQLException {
		return resultset.last();
	}

	public boolean next() throws SQLException {
		
		return resultset.next();
		
		}

	public double getDouble(String s) throws SQLException {
		return resultset.getDouble(s);
	}

	public double getDouble(int s) throws SQLException {
		return resultset.getDouble(s);
	}

	public Object getObject(String s) throws SQLException {
		return resultset.getObject(s);
	}

	public Object getObject(int i) throws SQLException {
		return resultset.getObject(i);
	}

	public boolean wasNull() throws SQLException {
		return resultset.wasNull();
	}

	public String getString(String s) throws SQLException {
		return resultset.getString(s);
	}

	public int getInt(String s) throws SQLException {
		return resultset.getInt(s);
	}

	public int getInt(int i) throws SQLException {
		return resultset.getInt(i);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return resultset.getMetaData();
	}

	public void close() {
		try {
			resultset.close();
			statement.close();

			resultset = null;
			statement = null;

		} catch (SQLException e) {
			System.err.println("QueryResult.close(): " + e);
		}
	}
}