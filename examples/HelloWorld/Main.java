import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import configuration.Credentials;

class Main{
// https://www.oracle.com/database/technologies/jdbc-drivers-12c-downloads.html 
	public static void main(String args[]){
		// Load Oracle JDBC Driver
		// DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		System.out.println("HelloWorld");
		System.out.println("Try connecting to: " + Credentials.url + " with user: " + Credentials.user);
		try (Connection conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password)){
			System.out.println("Connected to the database!");
			conn.setAutoCommit(false);
			try (Statement statement = conn.createStatement()){
				final String query = "SELECT TO_CHAR (SYSDATE, 'dd-mm-yyyy hh:mi:ss') FROM DUAL";
				System.out.println("query: " + conn.nativeSQL (query));
				try (ResultSet rs = statement.executeQuery(query)){
					if (rs.next()) {
						final String currentDate = rs.getString(1);
						System.out.println("currentDate: "+currentDate+ ";");
					}
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s - %s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
