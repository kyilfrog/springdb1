package hello.jdbc.connection;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtilTest {

	@Test
	void connection() {
		Connection connection = DBConnectionUtil.getConnection();
		assertThat(connection).isNotNull();
		
	}
}
