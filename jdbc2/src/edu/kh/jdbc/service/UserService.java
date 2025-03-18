package edu.kh.jdbc.service;

import java.sql.Connection;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

//(Model 중 하나)Service :비즈니스 로직을 처리하는 계층,
//데이터를 가공하고 틀랜잭션(commit,rollback)관리 수행
public class UserService {

	//필드
	private UserDAO dao = new UserDAO();

	/**전달받은 아이디와 일치하는 User 정보 반환 서비스
	 * @param input (뷰단에서 입련된 아이디)
	 * @return 아이디가 일치하는 회원 정보가 담긴 User 객체,
	 * 			없다면 null 반환
	 */
	public User selectId(String input) {
		
		//1.커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		//2.데이터 가공(할게 없으면 넘어감)
		
		//3.DAO 메서드를 호출하고 결과 반환
		User user = dao.seletId(conn,input);
		
		//4.DML (commit/rollback) DML이아니면 넘어가도됨
		
		//5.다 쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		//6.결과를 View 에게리턴
		
		return user;
	}
	
	
	
	//메서드
	
}
