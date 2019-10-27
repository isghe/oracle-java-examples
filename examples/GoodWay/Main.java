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
			conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password);;
			for (int i = 0; i < 60; ++i){
				try{
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
					java.lang.Thread.sleep(1000);
				} catch (SQLException e) {
					System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e){
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(conn!=null){
					conn.close(); // close the connection only one time, because it was opened only one time.
				}
			}catch(SQLException se2){
				System.err.format("SQL State se2: %s\n%s", se2.getSQLState(), se2.getMessage());
				se2.printStackTrace();
			}
		}
	}
}
