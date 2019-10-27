import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

class Main{  
	public static void main(String args[]){
		final String connectionString = Credentials.url;
		Connection conn = null;
		try {
			// Load Oracle JDBC Driver
			// DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("WrongWay");
			for (int i = 0; i < 60; ++i){
				conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password);
				assert null != conn: "conn is null";
				conn.setAutoCommit(false);
				Statement statement = null;
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery("SELECT TO_CHAR (SYSDATE, 'dd-mm-yyyy hh:mi:ss') FROM DUAL");

				if (rs.next()) {
					// Date currentDate = rs.getDate(1); // get first column returned
					final String currentDate = rs.getString(1); // get first column returned
					System.out.println(""+(i+1) +". "+currentDate);
				}
				rs.close();
				String SQL = "INSERT INTO isghe_test (id, description) " +
				"VALUES ((select max(id) + 1 from isghe_test), 'd "+(i+1)+"')";
				statement.executeUpdate(SQL);  
				// statement.close();
				System.err.format("commit\n");
				conn.commit ();
				conn.close();
				java.lang.Thread.sleep(1000);
			}
			
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			try{
				if(conn!=null){
					System.err.format("rolling back\n");
					conn.rollback();
					conn.close();
				}
			}catch(SQLException se2){
				System.err.format("SQL State se2: %s\n%s", se2.getSQLState(), se2.getMessage());
				se2.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
