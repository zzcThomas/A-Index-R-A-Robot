package src;

import java.math.*;

/**
 * 计算两个字符串的相似度
 * @author thomas
 *
 */


public class StrDistance {
	
	public static int Min(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}
	
	public static int calcdis(String A, int Abegin, int Aend, String B, int Bbegin, int Bend, int temp[][]) {
		int a, b, c;
		if (Abegin > Aend) {
			if (Bbegin > Bend) {
				return 0;
			} else {
				return (Bend - Bbegin + 1);
			}
		}
		if (Bbegin > Bend) {
			if (Abegin > Aend) {
				return 0;
			} else {
				return Aend - Abegin + 1;
			}
		}
		
		if (A.charAt(Abegin) == B.charAt(Bbegin)) {
			if (temp[Abegin + 1][Bbegin + 1] != 0) {
				a = temp[Abegin + 1][Bbegin + 1];
			} else {
				a = calcdis(A, Abegin + 1, Aend, B, Bbegin + 1, Bend, temp);
			}
			return a;
		} else {
			if (temp[Abegin + 1][Bbegin + 1] != 0) {
				a = temp[Abegin + 1][Bbegin + 1];
 			} else {
 				a = calcdis(A, Abegin + 1, Aend, B, Bbegin, Bend, temp);
 				temp[Abegin + 1][Bbegin + 1] = a;
 			}
			
			if (temp[Abegin + 1][Bbegin] != 0) {
				b = temp[Abegin + 1][Bbegin];
			} else {
				b = calcdis(A, Abegin + 1, Aend, B, Bbegin, Bend, temp);
				temp[Abegin + 1][Bbegin] = b;
			}
			
			if (temp[Abegin][Bbegin + 1] != 0) {
				c = temp[Abegin][Bbegin + 1];
			} else {
				c = calcdis(A, Abegin, Aend, B, Bbegin + 1, Bend, temp);
			}
			
			return Min(a, b, c) + 1;
		}
	}
	
	public static int CalcAns(String A, String B) {
		int temp[][] = new int[A.length()][B.length()];
		//int temp[][] = {0};
		for (int i = 0; i < A.length(); i++) {
			for (int j = 0; j < B.length(); j++) {
				temp[i][j] = 0;
			}
		}
		int ans = calcdis(A, 0, A.length() - 1, B, 0, B.length() - 1, temp);
		return ans;
	}
	
	public static void main(String [] args) {
		String A = "redmi is a great phone";
		String B = "what's redmi";
		System.out.println(CalcAns(A, B));
	}
}
