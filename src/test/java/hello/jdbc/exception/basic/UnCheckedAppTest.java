package hello.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import hello.jdbc.exception.basic.UnCheckedAppTest.Serivce;


public class UnCheckedAppTest {
	
	@Test
	void unchecked() {
		Controller controller = new Controller();
		assertThatThrownBy(() -> controller.request())
			.isInstanceOf(Exception.class);
	}
	
	static class Controller {
		Serivce service = new Serivce();
		
		public void request() {
			service.logic();
		}
	}

	static class Serivce {
		Repository repository = new Repository();
		NetworkClient networkClient = new NetworkClient();
		
		public void logic() {
			repository.call();
			networkClient.call();
		}
	}
	
	static class NetworkClient {
		public void call() {
			throw new RuntimeConnectException("연결 실패");
		}
	}
	
	static class Repository {
		public void call() {
			try {
				runSQL();
			} catch (SQLException e) {
				throw new runtimeSQLException(e);
			}
		}
		
		public void runSQL() throws SQLException {
			throw new SQLException();
		}
	}
	
	static class RuntimeConnectException extends RuntimeException {
		public RuntimeConnectException(String message) {
			super(message);
		}
	}
	
	static class runtimeSQLException extends RuntimeException {
		public runtimeSQLException(Throwable cause) {
			super(cause);
		}
	}

			
		
	
}





















