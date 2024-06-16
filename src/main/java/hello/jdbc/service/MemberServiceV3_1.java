package hello.jdbc.service;

import java.sql.Connection; 
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 트랜잭션 매니져
 * 
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

	private final MemberRepositoryV3 memberRepository;
	private final PlatformTransactionManager transactionManager;
	
	//계좌이체 로직 작성
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		//트랜잭션 시작
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {

			//비즈니스 로직
			Member fromMember = memberRepository.findById(fromId);
			Member toMember = memberRepository.findById(toId);
			
			memberRepository.update(fromId, fromMember.getMoney() - money);
			validation(toMember);
			memberRepository.update(toId, toMember.getMoney() + money);
			
			transactionManager.commit(status);     // 성공시 커밋
		} catch(Exception e) {
			transactionManager.rollback(status);   // 실패시 롤백
			throw new IllegalStateException();
		} 
		
	}
	
	private void validation(Member toMember) {
		if(toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체중 예외 발생");
		}
	}
	
}
















		