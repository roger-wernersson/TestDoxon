package testdoxon.util;

import java.util.Properties;

public interface TDGlobals {
	public final short TEST = 1;
	public final short IGNORE = 2;
	public final short TEST_IGNORE = 3;
	public final short NONE = 4;
	public final short BLUE_DOT = 5;
	public final String CONFIG_FILE = "./config.cfg";
	
	public static Properties prop = new Properties();
	public static String JAVA_DOC_FILEPATH = ".";
	public static String JAVA_DOC_REPORT_OUTPUT_DIR = System.getProperty("user.dir") + "\\target\\site\\apidocs\\testdoxonTestFiles";
	public static String JAVA_DOC_DESTINATION_DIR = System.getProperty("user.dir") + "\\target\\site\\apidocs\\testdoxonTestFiles";
	public static String ROOT_FOLDER = System.getProperty("user.dir");
}
