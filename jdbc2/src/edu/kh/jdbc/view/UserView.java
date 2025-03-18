package edu.kh.jdbc.view;

import java.util.Scanner;

import edu.kh.jdbc.dto.User;
import edu.kh.jdbc.service.UserService;

//View : 사용자와 직접 상호작용하는 화면 (UI)를 담당,
//입력을 받고 결과를 출력하는 역할
public class UserView {

	//필드
	private Scanner sc = new Scanner(System.in);
	private UserService service = new UserService();
			
	
	//메서드
	/** User 관리 프로그램 메인 메뉴
	 */
//	public void mainMenu() {
//		
//		int input = 0;
//		
//		do {
//			try {
//				
//				System.out.println("\n===== User 관리 프로그램 =====\n");
//				System.out.println("1. User 등록(INSERT)");
//				System.out.println("2. User 전체 조회(SELECT)");
//				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
//				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
//				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
//				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
//				System.out.println("7. User 등록(아이디 중복 검사)");
//				System.out.println("8. 여러 User 등록하기");
//				System.out.println("0. 프로그램 종료");
//				
//				System.out.print("메뉴 선택 : ");
//				input = sc.nextInt();
//				sc.nextLine(); // 버퍼에 남은 개행문자 제거
//				
//				switch(input) {
//				case 1: insertUser(); break;
//				case 2: selectAll(); break;
//				case 3: selectName(); break;
//				case 4: selectUser(); break;
//				case 5: deleteUser(); break;
//				case 6: updateName(); break;
//				case 7: insertUser2(); break;
//				case 8: multiInsertUser(); break;
//				
//				case 0 : System.out.println("\n[프로그램 종료]\n"); break;
//				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
//				}
//				
//				System.out.println("\n-------------------------------------\n");
//				
//			} catch (InputMismatchException e) {
//				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
//				System.out.println("\n***잘못 입력 하셨습니다***\n");
//				
//				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
//				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거
//				
//			} catch (Exception e) {
//				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
//				e.printStackTrace();
//			}
//			
//		}while(input != 0);
//		
//	} // mainMenu() 종료

	
	
	
	/**
	 * JDBCTemplate 사용 테스트
	 */
	public void test() {
		
		//입력된 ID와 일치하는 USER 정보 조회
		System.out.print("ID 입력 : ");
		String input = sc.next();
		
		//서비스 호출 후 결과 반환 받기
		User user = service.selectId(input);
		
		//결과에 따라 사용자에게 보여줄 응답화면 결정
		if(user==null) {
			System.out.println("없어용");
		}else {
			System.out.println(user);
		}
		
		
	}
	
	
	
	
	
	
	
	
	
}
