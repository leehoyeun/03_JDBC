package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

//(Model 중 하나)Service :비즈니스 로직을 처리하는 계층,
//데이터를 가공하고 틀랜잭션(commit,rollback)관리 수행
public class UserService {

	//필드
	private UserDAO dao = new UserDAO();

	
	
	//메서드
	
	
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
		
	/**User 등록 서비스
	 * @param user : 입력받은 ID와 PW와 NAME이 셋팅된 객체
	 * @return 결과 행의 갯수
	 */
	public int inserUser (User user) throws Exception {
		
		//1. 커낵션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		//2. 데이터 가공 (할게 없으면 넘어감)
		
		//3.DAO 메서드 호출 후 결과 반환받기
		int result =dao.insertUser(conn,user);
		
		//4.DML(INSERT)수행 결과에 따라 트랜잭션 제어 처리
		if(result > 0) {//INSERT 성공
			JDBCTemplate.commit(conn);
		}else {// INSERT 실패
			JDBCTemplate.rollback(conn);
		}
		//5.Connection 반환하기
		JDBCTemplate.close(conn);
		
		//6.결과 반환
		return result;
	}

	/**2. User 전체 조회하는 서비스
	 * @return 조회된 User 들이 담긴 List
	 */
	public List<User> selectAll()throws Exception {
		
		//1. Connection 생성하기
		Connection conn = JDBCTemplate.getConnection();
				
		//2.Dao 메서드 호출 (SELECT)후 결과반환 (List<User>)받기
		List<User> userList = dao.selectAll(conn);
		
		//3.Connection 반환
		JDBCTemplate.close(conn);
		
		//4.결과 반환
		return userList;
	}

	/**User 중 이름에 검색어가 포함된 회원 조회 서비스
	 * @param keyword : 입력한 키워드
	 * @return searchList : 조회된 회원 리스트
	 */
	public List<User> selectName(String keyword)throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> searchList = dao.seletName(conn,keyword);
		
		JDBCTemplate.close(conn);
		
		
		return searchList;
	}

	/**User No 를 입력받아 User 조회 서비스
	 * @param input
	 * @return user(조회된 회원 정보 OR null)
	 */
	public User selectUser(int input) throws Exception {
		
		Connection conn =JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn,input);
		
		
		JDBCTemplate.close(conn);
		
		
		return user;
	}

	/**USER_NO를 입력 받아 일치하는 User 삭제 서비스
	 * @param input
	 * @return result
	 */
	public int deleteUser(int input)throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteUser(conn,input);
		
		//결과에 따른 트랜잭션 처리
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	/**ID, PW가 일치하는 USER_NO 조회 서비스
	 * @param userId
	 * @param userPw
	 * @return
	 */
	public int selectUserNo(String userId, String userPw)throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int userNo = dao.selectUser(conn,userId,userPw);
		
		JDBCTemplate.close(conn);
		
		return userNo;
	}

	/**userNo가 일치하는 회원의 이름 수정 서비스
	 * @param userName
	 * @param userNo
	 * @return result
	 */
	public int updateName(String userName, int userNo)throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateName(conn,userName,userNo);
		
		if(result>0) JDBCTemplate.commit(conn);
		else JDBCTemplate.rollback(conn);
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	/**아이디 중복을 확인하는 메서드
	 * @param userId
	 * @return count
	 * @throws Exception
	 */
	public int idCheck(String userId)throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int count = dao.idCheck(conn,userId);
		
		JDBCTemplate.close(conn);
		
		return count;
	}

	/** userList 에있는 모든 User 객체를 INSERT 하는 서비스
	 * @param userList
	 * @return 
	 */
	public int multiInsertuser(List<User> userList)throws Exception {
		
		//다중 INSERT 방법
		//1)SQL 을 이용한 다중 INSERT
		//2)Java 반복문을 이용한 다중 INSERT (이거사용!)
		
		Connection conn = JDBCTemplate.getConnection();
		
		int count = 0; //삽입 성공한 행의 개수 count
		
		//1행씩 삽입
		
		for(User user :userList) {
			int result = dao.insertUser(conn, user);
			count += result; //삽입 성공한 행의 개수를 count 누적
		}
		
		// 트랜잭션 제어 처리
		// 전체 삽입 성공 시 commit / 아니면 rollback(일부삽입,전체실패)
		
		if(count==userList.size()) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return count;
	}
	
	
	
	
	
}
