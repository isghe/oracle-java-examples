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
				conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password);
				assert null != conn: "conn is null";
				conn.setAutoCommit(false);
				Statement statement = null;
				statement = conn.createStatement();
				String sql = "INSERT INTO TEST_INSERT (id, description) " +
					"VALUES ((select nvl (max(id), 0) + 1 from TEST_INSERT), '"+exampleName+" "+(i+1)+"')";
				System.out.println("insertSql: " + sql);
				statement.executeUpdate(sql);  
				statement.close();
				System.err.format("commit\n");
				conn.commit ();
				conn.close (); // CLOSE the connection or you'll have SESSIONS_PER_USER error
				java.lang.Thread.sleep(1000);
			} catch (SQLException e) {
				System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
				try{
					if(conn!=null){
						System.err.format("rolling back\n");
						conn.rollback();
						conn.close (); // CLOSE the connection or you'll have SESSIONS_PER_USER error
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
}
