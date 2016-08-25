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
		
		str1 = "红米销售量是多少";
		str2 = "小米发布会是什么时候";
		str3 = "什么时候涨工资";
		str4 = "总参收快递地址";
		
		System.out.println(IndexManager.ask(str3));
	}
}
