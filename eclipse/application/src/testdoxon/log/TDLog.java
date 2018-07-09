package testdoxon.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TDLog {

	private static final String LOG_NAME = "TDLog.log";
	
	public static final int ERROR = 0;
	public static final int INFORMATION = 1;
	public static final int WARNING = 2;
	
	public TDLog () {
		
	}
	
	public static void info (String message, int type) {
		PrintWriter out = null;
		
		try {
			String printText = "[" + TDLog.getTime() + "] ";
			
			switch (type) {
			case TDLog.ERROR:
				printText += "ERROR: ";
				break;
			case TDLog.INFORMATION:
				printText += "INFO: ";
				break;
			case TDLog.WARNING:
				printText += "WARNING: ";
			}
			printText += message;
			System.out.println(printText);
			
			out = new PrintWriter(new BufferedWriter(new FileWriter(LOG_NAME, true)));
			out.println(printText);
			
		} catch (IOException e) {
			
		} finally {
			if (out != null) {
				out.close();
			}
		}
		
	}
	
	private static String getTime () {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
		return simpleDateFormat.format(calendar.getTime());
	}
	
}
