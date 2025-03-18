package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {
		
		//아이디,비밀번호 이름을 입력받아
		//아이디 ,비밀번호가 일치하는 사용자의
		//이름을 수정 UPDATE
		
		//1.PreparedStatemnet 이용하기
		//2.commit/rollback 처리하기
		// 3. 성공시 "수정성공!" 출력 / 실패 시 "아이디 또는 비밀번호 불일치 " 출력

		// 1) JDBC 참조변수 선언 + 키보드 입력용 객체 SC 선언
		Connection conn =null;
		PreparedStatement pstmt = null;
		Scanner sc = null;
		
		
		
		try {
			//2)Connerction 객체 생성 DriverManager를 통해서
			//2-1) OracleDriver 메모리에 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2-2)DB 연결정보 작성
						
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh";
			String password = "kh1234";			
			
			conn = DriverManager.getConnection(url,userName,password);
			
			//3.SQL + AutoCommit 끄기
			conn.setAutoCommit(false);
			
			sc=new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			System.out.print("수정할 이름 입력 : ");
			String name = sc.nextLine();
			
		
			String sql = """
					UPDATE TB_USER SET 
					USER_NAME = ?
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";
					
			// 4.PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 5 . ?에 알맞은 값 셋팅
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pw);
			
			//6. SQL 수행 후 결과값 반환받기
			//executeQuery():SELECT 수행 수 ResultSet 반환
			//executeUpdate(): DML 수행 후 결과 행의 갯수 반환(int)
			int result = pstmt.executeUpdate();
			
			//7.result 값에 따라 결과 처리 + commit/rollback
			
			if(result > 0) {
				System.out.println("수정 성공!");
				conn.commit();
				
			}else {
				System.out.println("아이디 또는 비밀번호 불일치!");
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//8.사용한 JDBC 객체 자원반환
			try {
				if(pstmt != null)pstmt.close();
				if(conn != null)conn.close();
				
				if(sc!=null)sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	


}
