import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

class Main{
// https://www.oracle.com/database/technologies/jdbc-drivers-12c-downloads.html 
	public static void main(String args[]){
		try {
			// Load Oracle JDBC Driver
			// DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("HelloWorld");
			System.out.println("Try connecting to: " + Credentials.url + " with user: " + Credentials.user);
			Connection conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password);
			assert null != conn: "conn is null";
			System.out.println("Connected to the database!");
			conn.setAutoCommit(false);
			Statement statement = conn.createStatement();
			final String query = "SELECT TO_CHAR (SYSDATE, 'dd-mm-yyyy hh:mi:ss') FROM DUAL";
			System.out.println("query: " + query);
			ResultSet rs = statement.executeQuery(query);

			if (rs.next()) {
				final String currentDate = rs.getString(1);
				System.out.println("currentDate: "+currentDate+ ";");
			}
			rs.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s - %s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
