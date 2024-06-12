package hello.jdbc.repository;

import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MemberRepositoryV0Test {

	MemberRepositoryV0 repository = new MemberRepositoryV0();
	
	@Test
	void crud() throws SQLException {
		//save
		Member member = new Member("memberV4", 10000);
		repository.save(member);
		
		//findById
		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember={}", findMember);
		log.info("member == findmember {}", member == findMember);
		log.info("member equals findMember {}", member.equals(findMember));
		Assertions.assertThat(findMember).isEqualTo(member);
	}
}

















