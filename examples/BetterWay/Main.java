import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import configuration.Credentials;

class Main{
	public static void main(String args[]){
		final String exampleName = "BetterWay";
		System.out.println(exampleName);
		Connection conn = null;
		try{
			// open the connection only one time.
			System.err.format("Opening the connection\n");
			conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password);;
			assert null != conn: "conn is null";
			System.err.format("Connection opened\n");
			conn.setAutoCommit(false);
			for (int i = 0; i < 60; ++i){
					Statement statement = null;
					statement = conn.createStatement();
					String sql = "INSERT INTO TEST_INSERT (id, description) " +
						"VALUES ((select nvl (max(id), 0) + 1 from TEST_INSERT), '"+exampleName+" "+(i+1)+"')";
					System.out.println("insertSql: " + sql);
					statement.executeUpdate(sql);  
					statement.close();
					java.lang.Thread.sleep(1000);
			}
			System.err.format("commit\n");
			conn.commit ();
		} catch (SQLException e){
			System.err.format("SQL State: %s - %s", e.getSQLState(), e.getMessage());
			try{
				if(conn!=null){
					System.err.format("rolling back\n");
					conn.rollback();
				}
			}catch(SQLException se2){
				System.err.format("SQL State se2: %s - %s", se2.getSQLState(), se2.getMessage());
				se2.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(conn!=null){
					System.err.format("Closing the connection\n");
					conn.close(); // close the connection only one time, because it was opened only one time.
				}
			}catch(SQLException se2){
				System.err.format("SQL State se2: %s - %s", se2.getSQLState(), se2.getMessage());
				se2.printStackTrace();
			}
		}
	}
}
