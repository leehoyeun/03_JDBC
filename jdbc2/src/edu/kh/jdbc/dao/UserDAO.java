package edu.kh.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
//클래스명.메서드명()이 아닌 메서드명()만 작성해도 호출 가능하게 함.
import static edu.kh.jdbc.common.JDBCTemplate.*;
import edu.kh.jdbc.dto.User;

//(Model 중 하나)DAO (Data Access Object)
//데이터가 저장된 곳에 접근하는 용도의객체
//->DB에 접근하여 java에서 원하는 결과를 얻기위해
// SQL을 수행하고 결과를 반환 받는 역활
public class UserDAO {

	// 필드
	// -DB접근 관련한 JDBC 객체 참조 변수 미리 선언

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 메서드

	/**
	 * 전달받은 Connection을 이용해서 DB에 접근하여 전달받은 아이디(input)와 일치하는 User 정보를 DB조회하기
	 * 
	 * @param conn  : Service에서 생성한 Connection 객체
	 * @param input : View에서 입력받은 아이디
	 * @return 아이디가 일치하는 회원의 User 또는 null
	 */
	public User seletId(Connection conn, String input) {

		// 1.결과 저장용 변수 선언
		User user = null;

		try {

			// 2.SQL작성
			String sql = "SELECT * FROM TB_USER WHERE USER_ID = ?";

			// 3.PreparedStatement
			pstmt = conn.prepareStatement(sql);

			// 4.(위치홀더) 에 알맞은 값 세팅
			pstmt.setString(1, input);

			// 5.SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();

			// 6.조회 결과가 있을 경우
			// + 중복되는 아이디가 없다고 가정
			// ->1행만 조회되기 때문에 while문 보다는 if를 사용하는게 효과적

			if (rs.next()) {
				// 첫 행에 데이터가 존재한다면

				// 각 컬럼의 값 얻어오기
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");

				Date enrollDate = rs.getDate("ENROLL_DATE");

				// 조회된 컬럼값을 이용하여 User라는 객체를 생성한다
				new User(userNo, userId, userPw, userName, enrollDate.toString());
				// 아까 만든 User.java에서 만든 dto 역할을 하는 클래스가 이래서 필요하다.
				// 실제로 User 클래스에 필드를 보면 여기서 반환하는 타입과 타입이 동일함
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 사용한 JDBC 객체 자원 반환(close)
			// JDBCTemplate.colse(rs);
			// JDBCTemplate.close(pstmt);
			close(rs);
			close(pstmt);
			// 위 import 구간에서 static

			// Connection 객체는 생성된 Service에서 close!

		}

		return user; // 결과 반환(생성된 User 객체 또는 null)
	}

	/**
	 * 유저 등록하는 DAO
	 * 
	 * @param conn : DB연결정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id,pw,name 이 셋팅된 객체
	 * @return 인서트한 결과행의 개수
	 */
	public int insertUser(Connection conn, User user) throws Exception {

		// SQL 수행 중 발생하는 예외를
		// catch로 처리하지 않고 , throws를 이용해서 호출부로 던져 처리
		// -> catch 문 필요없다!

		// 1.결과 저장용 변수 선언
		int result = 0;

		try {
			// 2.SQL 작성
			String sql = """
					INSERT INTO TB_USER
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)""";

			// 3.PreparedStatement
			pstmt = conn.prepareStatement(sql);

			// 4.?(위치홀더) 알맞은값 대입

			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());

			// 5.SQL(INSERT) 수행(emecuteUpdate)후 결과(삽입된 행의갯수) 반환 받기
			result = pstmt.executeUpdate();

		} finally {

			// 6.사용한 JDBC 객체 자원 반환(close)
			close(pstmt);

		}

		// 결과 저장용 변수에 저장된 값 반환
		return result;
	}

	/**
	 * 2. User 전체 조회 Dao
	 * 
	 * @param conn
	 * @return user 객체가 담긴 UserList
	 */
	public List<User> selectAll(Connection conn) throws Exception {

		// 1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<User>();

		try {
			// 2. SQL 작성하기
			String sql = """
					SELECT USER_NO AS 번호 ,USER_ID AS 아이디,USER_PW AS 비번,USER_NAME AS 이름,
						TO_CHAR(ENROLL_DATE,'YYYY"년"MM"월"DD"일"') as 가입날짜
						FROM TB_USER
						ORDER BY USER_NO
						""";

			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);

			// 4.?에 알맞은 값 대입 (없으면 패스)

			// 5.SQL(SELECT)수행(executeQuery) 후 결과 반환(ResultSet)받기
			rs = pstmt.executeQuery();

			// 6.조회 결과를 한행씩 접근하여 컬럼 값 얻어오기
			// 몇행이 조회될지 모른다 -> While
			// 무조건 1행만 조회된다 -> if
			while (rs.next()) {

				int userNo = rs.getInt("번호");
				String userId = rs.getString("아이디");
				String userPw = rs.getString("비번");
				String userName = rs.getString("이름");
				String enrollDate = rs.getString("가입날짜");
				// java.sql.Date 타입으로 값을 저장하지 않은 이유
				// ->SELECT 문에서 TO_CHAR()를 이용하여 문자열로 변환해 조회했기 때문에

				// User 객체 새로 생성하여 컬럼값 세팅하기
				User user = new User(userNo, userId, userPw, userName, enrollDate);

				userList.add(user);
			}

		} finally {
			close(rs);
			close(pstmt);
		}
		// 조회 결과가 담긴 List 반환
		return userList;
	}

	/**
	 * 이름에 검색어가 포함되는 회원 모두 조회하는 DAO
	 * 
	 * @param conn
	 * @param keyword
	 * @return searchList
	 */
	public List<User> seletName(Connection conn, String keyword) throws Exception {

		// 결과 저장용 변수 선언
		List<User> searchList = new ArrayList<User>();

		try {

			// SQL 작성

			String sql = """
					SELECT USER_NO AS 번호 ,USER_ID AS 아이디,USER_PW AS 비번,USER_NAME AS 이름,
					TO_CHAR(ENROLL_DATE,'YYYY"년"MM"월"DD"일"') as 가입날짜
					FROM TB_USER
					WHERE USER_NAME LIKE '%'||?||'%'
					ORDER BY USER_NO
					""";

			// PreparedStratement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// ? 위치 홀더 값셋팅
			pstmt.setString(1, keyword);

			// DB수행 후 결과 반환 받기
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int userNo = rs.getInt("번호");
				String userId = rs.getString("아이디");
				String userPw = rs.getString("비번");
				String userName = rs.getString("이름");
				String enrollDate = rs.getString("가입날짜");

				User user = new User(userNo, userId, userPw, userName, enrollDate);

				searchList.add(user);
			}

		} finally {
			close(rs);
			close(pstmt);
		}

		return searchList;
	}

	/**
	 * USER_NO를 입력받아 일치하는 User 조회 DAO
	 * 
	 * @param conn
	 * @param input
	 * @return user 객체 OR null
	 */
	public User selectUser(Connection conn, int input) throws Exception {

		User user = null;

		try {

			String sql = """
					SELECT USER_NO AS 번호 ,USER_ID AS 아이디,USER_PW AS 비번,USER_NAME AS 이름,
					TO_CHAR(ENROLL_DATE,'YYYY"년"MM"월"DD"일"') as 가입날짜
					FROM TB_USER
					WHERE USER_NO = ?
					""";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, input);

			rs = pstmt.executeQuery();

			if (rs.next()) {

				int userNo = rs.getInt("번호");
				String userId = rs.getString("아이디");
				String userPw = rs.getString("비번");
				String userName = rs.getString("이름");
				String enrollDate = rs.getString("가입날짜");

				user = new User(userNo, userId, userPw, userName, enrollDate);

			}

		} finally {
			close(rs);
			close(pstmt);
		}

		return user;

	}

	/**
	 * USER_NO를 입력 받아 일치하는 User 삭제 DAO
	 * 
	 * @param conn
	 * @param input
	 * @return result
	 */
	public int deleteUser(Connection conn, int input) throws Exception {

		int result = 0;

		try {
			String sql = """
					DELETE FROM TB_USER
					WHERE USER_NO=?
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, input);
			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);
		}
		return result;
	}

	/**
	 * ID,PW가 일치하는 회원은 USER_NO 조회 DAO
	 * 
	 * @param conn
	 * @param userId
	 * @param userPw
	 * @return
	 */
	public int selectUser(Connection conn, String userId, String userPw) throws Exception {

		int userNo = 0;

		try {

			String sql = """
					SELECT USER_NO
					FROM TB_USER
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);

			rs = pstmt.executeQuery();

			// 조회된 1행이 있을 경우
			if (rs.next()) {
				userNo = rs.getInt("USER_NO");
			}

		} finally {
			close(rs);
			close(pstmt);
		}

		return userNo;// 조회성공 USER_NO , 실패 0 반환
	}

	/**
	 * userNo 일치하는 회원의 이름 수정 DTO
	 * 
	 * @param conn
	 * @param userName
	 * @param userNo
	 * @return result
	 */
	public int updateName(Connection conn, String userName, int userNo) throws Exception {

		int result = 0;

		try {

			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_NO =?
					""";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setInt(2, userNo);

			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);
		}

		return result;
	}

	/**아이디 중복확인해주는 DAO
	 * @param conn
	 * @param userId
	 * @return count
	 * @throws Exception
	 */
	public int idCheck(Connection conn, String userId)throws Exception {
		
		int count = 0;
		
		try {
			String sql = """
					SELECT COUNT(*)
					FROM TB_USER 
					WHERE USER_ID =?
					""";
		pstmt=conn.prepareStatement(sql);
		pstmt.setString(1, userId);
		
		rs=pstmt.executeQuery();
		
		if(rs.next()) {
			count =rs.getInt(1);//조회된 컬럼 순서번호를 이용해
								//컬럼값 얻어오기
		}
		} finally {
			
			close(rs);
			close(pstmt);
		}
		
		return count;
	}
}
