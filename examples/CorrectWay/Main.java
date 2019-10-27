import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class Main{
	public static void main(String args[]){
		final String exampleName = "CorrectWay";
		System.out.println(exampleName);
		for (int i = 0; i < 60; ++i){
			Connection conn = null;
			try{
				System.out.println("Opening the connection");
				conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password);
				System.out.println("Connection opened");

				assert null != conn: "conn is null";
				conn.setAutoCommit(false);
				Statement statement = null;
				statement = conn.createStatement();
				String sql = "INSERT INTO TEST_INSERT (id, description) " +
					"VALUES ((select nvl (max(id), 0) + 1 from TEST_INSERT), '"+exampleName+" "+(i+1)+"')";
				System.out.println("insertSql: " + sql);
				statement.executeUpdate(sql);  
				statement.close();
				System.out.println("committing");
				conn.commit ();
				System.out.println("committed");
				System.out.println("Closing the connection");
				conn.close (); // CLOSE the connection or you'll have SESSIONS_PER_USER error
				System.out.println("Connection closed");
				java.lang.Thread.sleep(1000);
			} catch (SQLException e) {
				System.err.format("SQL State: %s - %s\n", e.getSQLState(), e.getMessage());
				try{
					if(conn!=null){
						System.err.format("rolling back\n");
						conn.rollback();
						conn.close (); // CLOSE the connection or you'll have SESSIONS_PER_USER error
					}
				}catch(SQLException se2){
					System.err.format("SQL State se2: %s - %s\n", se2.getSQLState(), se2.getMessage());
					se2.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
