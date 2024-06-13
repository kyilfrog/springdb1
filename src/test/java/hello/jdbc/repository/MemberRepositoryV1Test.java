package hello.jdbc.repository;

import java.sql.SQLException; 
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariDataSource;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response;
import static hello.jdbc.connection.ConnectionConst.*;
@Slf4j
public class MemberRepositoryV1Test {

	MemberRepositoryV1 repository;
	
	@BeforeEach
	void beforeEach() {
		//기본 DriverManager - 항상 새로운 커넥션을 획득
		//DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
		
		//커넥션 풀링
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		repository = new MemberRepositoryV1(dataSource);
	}
	
	@Test
	void crud() throws SQLException {
		//save
		Member member = new Member("memberV10", 10000);
		repository.save(member);
		
		//findById
		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember={}", findMember);
		Assertions.assertThat(findMember).isEqualTo(member);
		
		//update money: 10000 -> 20000
		repository.update(member.getMemberId(), 20000);
		Member updatedMember = repository.findById(member.getMemberId());
		Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);
		
		//delete
		repository.delete(member.getMemberId());
		//삭제한것을 검증하려는 경우에는 이렇게
		Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))
			.isInstanceOf(NoSuchElementException.class);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

















