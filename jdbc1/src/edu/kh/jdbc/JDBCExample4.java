package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {

	public static void main(String[] args) {
		
		// 부서명을 입력받아
		// 해당 부서에 근무하는 사원의
		// 사번, 이름, 부서명, 직급명을
		// 직급코드 오름차순으로 조회
		
		// [실행화면]
		// 부서명 입력 : 총무부
		// 200 / 선동일 / 총무부 / 대표
		// 202 / 노옹철 / 총무부 / 부사장
		// 201 / 송종기 / 총무부 / 부사장
		
		// 부서명 입력 : 개발팀
		// 일치하는 부서가 없습니다!
		
		// hint : SQL 에서 문자열은 양쪽 '' (홑따옴표) 필요
		// ex) 총무부 입력 -> '총무부'
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		Scanner sc = null;
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh";
			String password = "kh1234";
			
			conn = DriverManager.getConnection(url, userName, password);
			
			sc = new Scanner(System.in);
			System.out.print("부서명 입력 : ");
			String input = sc.nextLine();
			
			String sql = """
					SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME 
					FROM EMPLOYEE
					JOIN JOB ON(EMPLOYEE.JOB_CODE = JOB.JOB_CODE)
					LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID)
					WHERE DEPT_TITLE = '""" + input + "' ORDER BY EMPLOYEE.JOB_CODE";
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			/*
			 * flag 이용법 
			 * 
			boolean flag = true;
			// 조회결과 있다면 false, 없으면 true
			
			while(rs.next()) {
				flag = false;
				
				String empId = rs.getString("EMP_ID"); 
				String empName = rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s \n",
									empId, empName, deptTitle, jobName);
			}
			
			if(flag) { 
				System.out.println("일치하는 부서가 없습니다!");
			}
			*/
			
			// return 이용법
			if(!rs.next()) { // 조회 결과가 없다면
				System.out.println("일치하는 부서가 없습니다!");
				return;
			}
			
			do {
				String empId = rs.getString("EMP_ID"); 
				String empName = rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s \n",
									empId, empName, deptTitle, jobName);
			} while(rs.next());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				if(sc != null) sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
