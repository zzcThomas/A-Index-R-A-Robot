package Users;

import java.io.*;
import java.util.Scanner;

import src.IndexManager;

public class UserIO {
	
	public static void main(String [] args) {
		String str1, str2, str3, str4;
		
		//Scanner INPUT = new Scanner(System.in);
		//str = INPUT.next();
		//System.out.println(str);
		//INPUT.close();
		
		str1 = "�����������Ƕ���";
		str2 = "С�׷�������ʲôʱ��";
		str3 = "ʲôʱ���ǹ���";
		str4 = "�ܲ��տ�ݵ�ַ";
		
		System.out.println(IndexManager.ask(str3));
	}
}
