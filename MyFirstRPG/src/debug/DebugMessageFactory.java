package debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugMessageFactory {

	public static boolean DEBUG_MODE = true;
	public static boolean WRITE_LOG_FILE = false;
	public static String LOG_FILE = "/home/sam/Desktop/GameServer.log";
	
	public static void printInfoMessage(String message) {
		String s = "[ INFO  ]("+getDateAndTimeAsString()+"):\t\t"+message;
		if(DEBUG_MODE) {
			System.out.println(s);
		}
		if(WRITE_LOG_FILE) {
			appendToFile(s+"\n");
		}
	}
	
	public static void printErrorMessage(String message) {
		String s = "[ ERROR ]("+getDateAndTimeAsString()+"):\t\t"+message;
		if(DEBUG_MODE) {
			System.out.println(s);
		}
		if(WRITE_LOG_FILE) {
			appendToFile(s+"\n");
		}
	}
	
	public static void printNormalMessage(String message) {
		String s = "[  OK   ]("+getDateAndTimeAsString()+"):\t\t"+message;
		if(DEBUG_MODE) {
			System.out.println(s);
		}
		if(WRITE_LOG_FILE) {
			appendToFile(s+"\n");
		}
	}
	
	private static void appendToFile(String s) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(LOG_FILE), true));
			bw.write(s);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDateAndTimeAsString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
}
