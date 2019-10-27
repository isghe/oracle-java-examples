import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class Main{
	public static void main(String args[]){
		final String exampleName = "GoodWay";
		System.out.println(exampleName);
		Connection conn = null;
		try{
			// open the connection only one time.
			System.out.println("Opening the connection");
			conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password);
			System.out.println("Connection opened");
			assert null != conn: "conn is null";
			for (int i = 0; i < 60; ++i){
				try{
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
					java.lang.Thread.sleep(1000);
				} catch (SQLException e) {
					System.err.format("SQL State: %s - %s\n", e.getSQLState(), e.getMessage());
					try{
						if(conn!=null){
							System.err.format("rolling back\n");
							conn.rollback();
						}
					} catch(SQLException se2){
						System.err.format("SQL State se2: %s - %s\n", se2.getSQLState(), se2.getMessage());
						se2.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e){
			System.err.format("SQL State: %s - %s\n", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(conn!=null){
					// close the connection only one time, because it was opened only one time.
					System.out.println("Closing the connection");
					conn.close ();
					System.out.println("Connection closed");
				}
			} catch(SQLException se2){
				System.err.format("SQL State se2: %s - %s\n", se2.getSQLState(), se2.getMessage());
				se2.printStackTrace();
			}
		}
	}
}
