package hello.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import hello.jdbc.exception.basic.CheckedAppTest.Serivce;


public class CheckedAppTest {
	
	@Test
	void checked() {
		Controller controller = new Controller();
		assertThatThrownBy(() -> controller.request())
			.isInstanceOf(Exception.class);
	}
	
	static class Controller {
		Serivce service = new Serivce();
		
		public void request() throws ConnectException, SQLException {
			service.logic();
		}
	}

	static class Serivce {
		Repository repository = new Repository();
		NetworkClient networkClient = new NetworkClient();
		
		public void logic() throws SQLException, ConnectException {
			repository.call();
			networkClient.call();
		}
	}
	
	static class NetworkClient {
		public void call() throws ConnectException {
			throw new ConnectException("연결 실패");
		}
	}
	
	static class Repository {
		public void call() throws SQLException {
			throw new SQLException("ex");
		}
	}
	
}
