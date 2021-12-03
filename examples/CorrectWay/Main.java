import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Savepoint;
import configuration.Credentials;

class Main{
	public static void main(String args[]){
		for (int i = 0; i < 60; ++i){
			final String exampleName = "CorrectWay ";
			System.out.println(exampleName);
			System.out.println("Opening the connection");

			try (Connection conn = DriverManager.getConnection(Credentials.url, Credentials.user, Credentials.password)){
				System.out.println("Autocloseable Connection opened");
				java.lang.Thread.sleep(1000);
				assert null != conn: "conn is null";
				conn.setAutoCommit(false);
				Savepoint savepoint = conn.setSavepoint (); // 99999 - could not set a Savepoint with auto-commit on
				final String sql = "INSERT INTO TEST_INSERT (id, description) " +
					"VALUES ((select nvl (max(id), 0) + 1 from TEST_INSERT), ? || (select nvl (max(id), 0) + 1 from TEST_INSERT))";
				try (PreparedStatement statement = conn.prepareStatement (sql)){
					System.out.println("Autocloseable PreparedStatement opened");
					statement.setString (1, exampleName);
					System.out.println("insertSql: " + conn.nativeSQL (sql));
					statement.executeUpdate();
					conn.commit ();
				}
				catch (SQLException e){
					System.err.format("inner SQL State: %s - %s\n", e.getSQLState(), e.getMessage());
					// from https://docs.oracle.com/javase/7/docs/api/java/sql/Connection.html#close()
					// It is strongly recommended that an application explicitly commits or rolls back an active transaction prior to calling the close method. If the close method is called and there is an active transaction, the results are implementation-defined.
					conn.rollback(savepoint);
					e.printStackTrace();
				}
				catch (Exception e){
					System.err.format("inner State: - %s\n", e.getMessage());
					// from https://docs.oracle.com/javase/7/docs/api/java/sql/Connection.html#close()
					// It is strongly recommended that an application explicitly commits or rolls back an active transaction prior to calling the close method. If the close method is called and there is an active transaction, the results are implementation-defined.
					conn.rollback(savepoint);
					e.printStackTrace();
				}
			}
			catch (SQLException e){
				System.err.format("out SQL State: %s - %s\n", e.getSQLState(), e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e){
				System.err.format("out State: - %s\n", e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
