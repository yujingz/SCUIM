/*************************************************************************************************
 * 
 * @author : 姚武平
 * @since : JDK 1.4
 * @date : 2009-10-8
 * @version : 1.2
 * @description : 打印类，格式化控制台打印格式。参考自《Java编程思想》
 * 
 *************************************************************************************************/

package scu.im.utils;

import java.io.*;

public class Print {

	public static void print(Object obj) {
		System.out.println("#####$$$$$>>>>>：" + obj);
	}

	public static void print(String text) {
		System.out.println("#####$$$$$>>>>>：" + text);
	}

	public static void print(String text, Object obj) {
		System.out.println("#####$$$$$>>>>>：" + text + obj);
	}

	public static void print(Object obj, String text) {
		System.out.println("#####$$$$$>>>>>：" + obj + text);
	}

	public static void print() {
		System.out.println();
	}

	public static void printnb(Object obj) {
		System.out.print(obj);
	}

	public static PrintStream printf(String format, Object... args) {
		return System.out.printf(format, args);
	}
}
