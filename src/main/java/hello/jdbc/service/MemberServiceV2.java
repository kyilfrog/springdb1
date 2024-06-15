package hello.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 * MemberRepositoryV2와 연결되어있으니 참조
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

	private final MemberRepositoryV2 memberRepository;
	
	private final DataSource dataSource;
	
	//계좌이체 로직 작성
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		Connection con = dataSource.getConnection();
		try {
			con.setAutoCommit(false);    //트랜잭션 시작
			
			//비즈니스 로직
			Member fromMember = memberRepository.findById(con, fromId);
			Member toMember = memberRepository.findById(con, toId);
			
			memberRepository.update(con, fromId, fromMember.getMoney() - money);
			validation(toMember);
			memberRepository.update(con, toId, toMember.getMoney() + money);
			con.commit();     // 성공시 커밋
		} catch(Exception e) {
			con.rollback();   // 실패시 롤백
			throw new IllegalStateException();
		} finally {
			if (con!=null) {
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (Exception e) {
					log.info("error", e);
				}
			}
		}
		
	}
		
		
		
		
	
	private void validation(Member toMember) {
		if(toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체중 예외 발생");
		}
	}
	
}
















		