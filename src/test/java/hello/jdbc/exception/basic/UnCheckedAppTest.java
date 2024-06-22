package hello.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import hello.jdbc.exception.basic.UnCheckedAppTest.Serivce;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnCheckedAppTest {
	
	@Test
	void unchecked() {
		Controller controller = new Controller();
		assertThatThrownBy(() -> controller.request())
			.isInstanceOf(Exception.class);
	}
	
	@Test
	void printEx() {
		Controller controller = new Controller();
		try {
			controller.request();
		} catch (Exception e) {
			log.info("ex", e);
		}
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
		/*
		 * 1 SQLException이 발생
		 * 2 catch블록에서 이 예외를 runtimeSQLException으로 감싸서 던짐
		 * 3 runtimeSQLException의 생성자가 호출되면서 SQLException객체가 cause로 전달됨
		 * 4 runtimeSQLException의 생성자에서 super(cause)가 호출되어 RuntimeException
		 *   클래스의 생성자에 cause가 전달된다.
		 * 5 이렇게 전달된 cause는 RuntimeException 내부에 저장된다.  
		 */
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
		//Throwable cause: 예외의 원인을 뜻한다.
		//super(cause) : RuntimeException(부모 클래스)의 생성자를 호출하는 코드이다.
		public runtimeSQLException(Throwable cause) {
			super(cause);
		}
	}

}
			