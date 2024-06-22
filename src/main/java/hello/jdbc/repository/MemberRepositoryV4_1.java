package hello.jdbc.repository;

import java.sql.Connection;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.zaxxer.hikari.HikariDataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;

 /*
  *  예외 누수 문제 해결
  *  체크 예외를 언체크 예외(RuntimeException)로 변경
  *  interface MemberRepository를 사용할 것이다
  *  throws SQLException을 제거 할 것이다.
  */
@Slf4j
public class MemberRepositoryV4_1 implements MemberRepository {
	
	private final DataSource dataSource;
	
	public MemberRepositoryV4_1(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Member save(Member member) {
		String sql = "insert into member(member_id, money) values (?, ?)";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setInt(2, member.getMoney());
			pstmt.executeUpdate();
			return member;
		} catch (SQLException e) {
			throw new MyDbException(e);
		} finally {
			close(con, pstmt, null);
		}
		
	}
	@Override
	public Member findById(String memberId) {
		String sql = "select * from member where member_id = ?";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Member member = new Member();
				member.setMemberId(rs.getString("member_id"));
				member.setMoney(rs.getInt("money"));
				return member;
			} else {
				throw new NoSuchElementException("member not found memberId=" + memberId);
			} 
			
		} catch (SQLException e) {
			throw new MyDbException(e);
		} finally {
			close(con, pstmt, rs);
		}
		
	}
	
	@Override
	public void update(String memberId, int money) {
		String sql = "update member set money=? where member_id=?";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2, memberId);
			int resultSize = pstmt.executeUpdate();
			log.info("resultSize={}", resultSize);
		} catch (SQLException e) {
			throw new MyDbException(e);
		} finally {
			close(con, pstmt, null);
		}
		
	}
	
	@Override
	public void delete(String memberId) {
		String sql = "delete from member where member_id=?";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new MyDbException(e);
		} finally {
			close(con, pstmt, null);
		}
	}
	
	private void close(Connection con, Statement stmt, ResultSet rs) {
		JdbcUtils.closeResultSet(rs);
		JdbcUtils.closeStatement(stmt);
		DataSourceUtils.releaseConnection(con, dataSource);
	}
		

	private Connection getConnection() throws SQLException {
		//트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야
		Connection con = DataSourceUtils.getConnection(dataSource);
		log.info("get connection={}, class={}", con, con.getClass());
		return con;
	}
	
}























