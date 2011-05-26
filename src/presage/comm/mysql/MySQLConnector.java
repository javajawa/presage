package presage.comm.mysql;


import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileWriter;


public class MySQLConnector {

	Connection  databaseConnection;
	int queries, updates = 0;

	public MySQLConnector(String dbName, String host) {

		testJDBCDriver();

		databaseConnection = getMySQLConnection(host);

		wipe(dbName, host);

		createDatabase(dbName, host);

		databaseConnection = getDatabaseConnection(dbName, host);

	}

	/**
	 * Checks whether the MySQL JDBC Driver is installed
	 */
	public void testJDBCDriver() {
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName("org.gjt.mm.mysql.Driver");
			System.out.println("MySQL Driver Found");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("MySQL JDBC Driver not found ... " + e);	
		}
	}

	/**
	 * Wipes and destroys a MySQL database named databaseName on host 'host'!.
	 */
	public void wipe(String databaseName, String host) {

		System.out
		.println("Trying to remove databases and tables from MySQL.....");

		Connection sqlCon = getMySQLConnection(host);

		try {

			// Now cleanup by deleting the database
			executeUpdate("DROP DATABASE IF EXISTS "
					+ databaseName);

			/* finally, we close the connection */
			sqlCon.close();
			System.out.println("Connections and databases Removed");

		} catch (java.sql.SQLException e) {
			System.err
			.println("Connections to Database couldn't be Closed and Dropped"
					+ e);


		}
	}

	public byte[] getDatabaseData(String db_Name, String host) {

		File cmdfile = new File(db_Name + ".bat");
		File datafile = new File(db_Name + ".sqldb");

		System.out.println(cmdfile.getAbsolutePath());
		System.out.println(datafile.getAbsolutePath());
		
		// Create the Backup file 
		try {

			// Write the batch file
			String cmd = "mysqldump " + db_Name + " > " + db_Name + ".sqldb";
			RandomAccessFile cmdfile_out = new RandomAccessFile(cmdfile, "rw");		
			cmdfile_out.writeBytes(cmd);				
			cmdfile_out.close();

			// Execute the batch file
			Runtime rt = Runtime.getRuntime();		
			Process child = rt.exec("cmd.exe /c " + cmdfile.getAbsolutePath());
			child.waitFor();

			// Tidy up.
			cmdfile.delete();

		} catch (Exception e) {
			System.err.println("Error" +e);
		}

		// Read in the Backup
		try {

			RandomAccessFile datafile_in = new RandomAccessFile(datafile, "r");
			byte[] data = new byte[(int)datafile_in.length()];
			datafile_in.readFully(data);

			// Tidy up.
			datafile_in.close();		
			datafile.delete();

			return data;

		} catch (Exception e) {
			System.err.println("error" +e);
		}

		return null;

	}

	public void restoreDataBase(String db_Name, byte[] data){
		
		File cmdfile = new File(db_Name + ".bat");
		File datafile = new File(db_Name + ".sqldb");
		
		System.out.println(cmdfile.getAbsolutePath());
		System.out.println(datafile.getAbsolutePath());
		
		try {

			// Store the data to a local file
			RandomAccessFile  datafile_out = new RandomAccessFile(datafile, "rw");	
			datafile_out.write(data);
			datafile_out.close();

			// make sure a database exisits that you can insert data into.
			executeUpdate("create database if not exists "
					+ db_Name);

			String cmd = "mysql " + db_Name + " < " + datafile.getAbsolutePath(); 
			RandomAccessFile cmdfile_out = new RandomAccessFile(cmdfile, "rw");		
			cmdfile_out.writeBytes(cmd);		
			cmdfile_out.close();

			
			Runtime rt = Runtime.getRuntime();		
			Process child = rt.exec("cmd.exe /c " + cmdfile.getAbsolutePath());
			child.waitFor();
			
			cmdfile.delete();
			datafile.delete();

		} catch (Exception e) {
			System.err.println("error" +e);
		}
	}


	/**
	 * Creates a MySQL database named databaseName on host 'host'!.
	 */
	public void createDatabase(String databaseName, String host) {

		String url = "jdbc:mysql://" + host + "/";
		Connection sqlCon;

		try {
			sqlCon = DriverManager.getConnection(url);

			executeUpdate("create database if not exists "
					+ databaseName);

			System.out.println("SQLModule: Database Created: " + databaseName
					+ "...");

			sqlCon.close();

		} catch (Exception e) {
			System.err.println("SQLModule.createDatabase(" + databaseName
					+ ", " + host
					+ ") Database couldn't be created \nor Connection to url '"
					+ url + "' failed.\n" + e);

		}
	}

	/**
	 * Returns a connection to the MySQL Server!.
	 */
	private Connection getMySQLConnection(String host) {

		String url = "jdbc:mysql://" + host + "/";
		Connection con = null;

		try {
			con = DriverManager.getConnection(url);
			System.out.println("Connection established to " + url + "...");
		} catch (Exception e) {
			System.err.println("Could not get Mysql server connection" + url);

		}
		return con;
	}

	
	public void setDatabaseConnection(String databaseName, String host) {		
		databaseConnection = getDatabaseConnection(databaseName, host);		
	}

	
	/**
	 * Returns a connection to MySQL database!.
	 */
	private Connection getDatabaseConnection(String databaseName, String host) {

		String url = "jdbc:mysql://" + host + "/" + databaseName;
		Connection con = null;

		try {
			con = DriverManager.getConnection(url);
			System.out.println("Connection established to " + url + "...");
		} catch (Exception e) {
			System.err.println("SQLModule.getDatabaseConnection("
					+ databaseName + ", " + host
					+ ") Could not get database connection from '" + url + "'");

		}
		return con;
	}

	public void closeConnection(Connection con) {
		try {
			con.close();
			con = null;
		} catch (SQLException e) {
			System.err
			.println("SQLModule.closeConnection: Unable to close connection - "
					+ e);


		}
	}

	public void executeUpdate(String sqlStatement) {

		Statement s = null;

		try {

			s = databaseConnection.createStatement();

			s.execute(sqlStatement);

			s.close();

		} catch (SQLException e) {
			System.err
			.println("ToolBox.executeUpdate - Error executing sql statement: "
					+ sqlStatement);
			System.err.println(e);

		} 

		updates++;

	}

	public QueryResult executeQuery(String sqlStatement) {

		ResultSet rs = null;
		Statement s = null;

		try {

			s = databaseConnection.createStatement();
			rs = s.executeQuery(sqlStatement);

		} catch (SQLException e) {
			System.err
			.println("ToolBox.executeQuery - Error executing sql statement: "
					+ sqlStatement);
			System.err.println(e);

		}

		queries++;
		return new QueryResult(rs, s);
	}

//	public void executeUpdate(String sqlStatement) {
//	executeUpdate(databaseConnection, sqlStatement);
//	}

//	public QueryResult executeQuery(String sqlStatement) {
//	return executeQuery(databaseConnection, sqlStatement);
//	}

	public void dropTable(String tableName) {
		executeUpdate("DROP TABLE IF EXISTS " + tableName);
	}

	public void printTable(String tableName) {
		QueryResult result = executeQuery("SELECT * FROM "
				+ tableName);

		try {
			result.outputToscreen();
		} catch (SQLException e) {
			System.err.println(e);

		}
		result.close();
	}

	public void addColumn(String table, String columnName,
			String columnType) {
		executeUpdate("ALTER TABLE " + table + " Add " + columnName
				+ " " + columnType);
	}

	public void addColumnNotNull(String table, String columnName,
			String columnType) {
		executeUpdate("ALTER TABLE " + table + " Add " + columnName
				+ " " + columnType + " not null");
	}

	public void addColumnNotNull(String table, String columnName,
			String columnType, String defaultvalue) {
		executeUpdate("ALTER TABLE " + table + " Add " + columnName
				+ " " + columnType + " DEFAULT " + defaultvalue + " not null");
	}
}
