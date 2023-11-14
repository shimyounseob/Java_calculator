package Calculator;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent ; 
import java.awt.event.ActionListener;
import java.util.ArrayList ;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;


import javax.swing.*; // 스윙이란 자바에서 그래픽 사용자 인터페이스 구현을 위해 제공되는 클래스
import javax.swing.JFrame ; // java swing 클래스의 일부이며 구현되는 하나의 화면 창
import javax.swing.JPanel ;  


public class calculator extends JFrame{
	
	private JTextField inputSpace ;// 텍스트 필드란 사용자가 문자와 숫자를 직접 입력할 수 있는 입력 컨트롤
	private String num = "" ; 
	private String prev_num = "" ; 
	private ArrayList<String> equation = new ArrayList<String>() ; 
	
	
	public calculator() { 
		setLayout(null) ; // 해당 컨테이너의 각 컴포넌트 (버튼, 텍스트필드등)의 위치와 크기를 개발자가 직접 지정
		
		// 숫자 입력 창
		inputSpace = new JTextField() ; 
		inputSpace.setEditable(false) ; 
		inputSpace.setBackground(Color.WHITE);
		inputSpace.setHorizontalAlignment(JTextField.RIGHT);
		inputSpace.setFont(new Font("Arial", Font.BOLD, 50) );
		inputSpace.setBounds(8, 10, 255, 70); // 위치와 크기 (x:8 y:10 290 x 70)
//		inputSpace.addKeyListener(new myKeyAdapter());
		
		add(inputSpace) ;
		
		// 패널 설정
		JPanel buttonPanel = new JPanel() ; // 버튼을 놓을 판
		buttonPanel.setLayout(new GridLayout(5, 4, 10, 10)); // 격자 형태 배치 (가로 칸 수, 세로 칸 수, 좌우 간격, 상하 간격)
		buttonPanel.setBounds(8, 90, 253, 300);
		
		String button_names[] = {"C", "^", "v", "=", "/", "*", "+", "-", "7", "8", "9", "←", "4", "5", "6", ".", "1", "2", "3","0" } ; 
		JButton button[] = new JButton[button_names.length] ; // 버튼 배열 생성
		
		for (int i = 0 ; i < button_names.length;i++) {
			button[i] = new JButton(button_names[i]) ; // 버튼 개별 선언 및 초기화
			button[i].setFont(new Font("Arial", Font.BOLD, 16));
			if (button_names[i] == "C") button[i].setBackground(Color.RED);
			else if (button_names[i] == "=") button[i].setBackground(Color.BLACK);
			else button[i].setBackground(Color.GRAY);
			button[i].setForeground(Color.WHITE) ;
			button[i].setBorderPainted(false);
			button[i].addActionListener(new PadActionListener()) ;
			buttonPanel.add(button[i]) ; 
		}
		
		add(buttonPanel) ; 
		
		// JFrame의 주요 메소드 (this. 생략할 수 있음)
		setTitle("Calculator") ;  
		setVisible(true) ; 
		setSize(285, 440) ; 
		setLocationRelativeTo(null) ; // 윈도우를 화면 중앙에 배치
		setResizable(false) ; 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ; // 윈도우를 닫을 때 애플리케이션이 같이 닫힘
		
	}
	
	// 패드의 버튼을 통한 입력 값 처리
	class PadActionListener implements ActionListener{ 
		// 이벤트 처리 메소드, 패드가 눌릴때 작동 방식
		public  void actionPerformed (ActionEvent event) { 
			String operation = event.getActionCommand() ; // 입력값 객체의 문자열을 가져온다
			
			if (operation.equals("C")) { 
				inputSpace.setText(""); 
			} else if (operation.equals("=")) {
				String result = Double.toString(calculate(inputSpace.getText())) ; 
				inputSpace.setText(""+result);
				num = "" ; 
			} else if (operation.equals("←")) {
				int length = inputSpace.getText().length();
				if (length > 0) {
					// 마지막 문자 제거
					inputSpace.setText(inputSpace.getText().substring(0, length - 1));
				}
			}
			// 입력 값이 연산자 일때
			  else if (operation.equals("+") || operation.equals("-") || operation.equals("*") ||operation.equals("/")|| operation.equals("^")|| operation.equals("v")) {
				// 음수 값 허용해주는 경우
				if (inputSpace.getText().equals("") && operation.equals("-")) {
					inputSpace.setText(inputSpace.getText()+ event.getActionCommand());	
				} // 텍스트 필드가 비어있으면 안되고 연산자가 중복으로 나오면 안됨
				else if (!inputSpace.getText().equals("") && !prev_num.equals("+") && !prev_num.equals("-") && !prev_num.equals("*") && !prev_num.equals("/")&& !prev_num.equals("^") && !prev_num.equals("v")){
					inputSpace.setText(inputSpace.getText()+ event.getActionCommand());
				}
			} else {
				inputSpace.setText(inputSpace.getText()+ event.getActionCommand());
			}
			prev_num = event.getActionCommand() ; 
		}
	}
		

	// 계산 기능 구현을 위해 ArrayList에 숫자와 연산 기호를 하나씩 구분해 담아줌
	private void fullTextParsing(String inputText) {
		equation.clear(); // ArrayList 내의 모든 요소를 제거 해 객체의 크기를 0으로 만들고 그 안의 모든 요소 제거
	
		// 반복문을 통해 inputText를 하나씩 읽어줌
		for (int i = 0 ; i < inputText.length() ; i++) {
			char ch = inputText.charAt(i) ; 
			
			// char가 연산자인 경우 구분해줌 & 각각의 char가 연산자를 만나면 하나의 숫자로 만들어줌
			if (ch == '-' | ch == '+' |ch == '*' |ch == '/'|ch == '^'|ch == 'v' ) {
				equation.add(num) ; 
				num = "" ; 
				equation.add(ch+"") ; // ch를 String으로 변환
			}else {
				num = num + ch ; // ch가 숫자일 경우
			}
		}
		equation.add(num) ; 
		equation.remove("") ;  
	}
	
	public double calculate(String inputText) {
		fullTextParsing(inputText) ; 
		
		// 초깃값을 추가해 첫 연산이 뺄셈일때 맨 처음 0에서 첫 숫자를 더하고 빼기 연산 할 수 있게 해줌
		equation.add(0, "0");
	    equation.add(1, "+");
		
		double res = 0 ; // 누적되는 결과 값
		double current = 0 ; // 현재 처리되고 있는 숫자
		String mode = "add" ; // 모드는 add로 초기화 
		
		// 곱셈 나눗셈 연산자 부터 먼저 계산하도록
		for (int i = 0 ; i < equation.size() ; i++) {
			String s = equation.get(i) ; 	
			
			if (s.equals("^")) {
			    double base = Double.parseDouble(equation.get(i - 1));
			    double exponent = Double.parseDouble(equation.get(i + 1));
			    double result = Math.pow(base, exponent);
			    equation.set(i - 1, Double.toString(result));
			    equation.remove(i); // "^" 제거
			    equation.remove(i); // 지수 제거
			    i --;
			} 
			else if (s.equals("v")) {
	            double number = Double.parseDouble(equation.get(i - 1));
	            equation.set(i - 1, Double.toString(Math.sqrt(number)));
	            equation.remove(i); // "^" 제거
	            equation.remove(i); // 지수 제거
	            i--;
	        }
	    
			
			// 어떤 연산자가 나오는지에 따라 모드가 변환됨
			if (s.equals("+")) {
				mode = "add" ; 
			}else if (s.equals("-")) {
				mode = "sub" ; 
			}else if (s.equals("*")) {
				mode = "mul" ; 
			}else if (s.equals("/")) {
				mode = "div" ; 
			}else {
				if ((mode.equals("mul")|| mode.equals("div")) && !mode.equals("+") && !mode.equals("-") && !mode.equals("*") &&!prev_num.equals("/")) {
					Double one_numb = Double.parseDouble(equation.get(i-2)) ;
					Double two_numb = Double.parseDouble(equation.get(i)) ;
					Double result = 0.0 ; 
					
					if (mode.equals("mul")) {
						result = one_numb * two_numb ; 
					}
					else if (mode.equals("div")) {
						result = one_numb / two_numb ; 
					}
					
					equation.add(i+1, Double.toString(result)) ;  
					
					for(int j =0 ; j < 3; j++) {
						equation.remove(i-2) ; 
					}
					i -= 2 ;  // ArrayList equation에서 계산한 만큼의 인덱스를 없앰 
				}
			}
		}
		
		// 어떤 연산자가 나오는지에 따라 모드가 변환됨
		for (String s : equation) {
			if (s.equals("+")) {
				mode = "add" ; 
			}else if (s.equals("-")) {
				mode = "sub" ;  
			}else {
				current = Double.parseDouble(s) ; // 현재 처리되고 있는 숫자를 double로 타입 변환
				if (mode == "add") {
					res += current ; 
				} else if (mode == "sub") {
					res -= current ; 
				}  else {
					res = current ; 
				}
				
			} 	
			res = Math.round(res*1000000) / 1000000.0 ; 
		}  
		
		return res ;	
	}
	
}


	