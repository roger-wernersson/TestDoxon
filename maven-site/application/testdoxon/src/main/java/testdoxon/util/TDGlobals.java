package testdoxon.util;

public class TDGlobals {
	public static final short TEST = 1;
	public static final short IGNORE = 2;
	public static final short TEST_IGNORE = 3;
	public static final short NONE = 4;
	public static final short BLUE_DOT = 5;
	public static final short MISSING_TEST_IN_NAME = 6;
	
	public static final String CONFIG_FILE = "./config.cfg";

	public static final String JAVA_DOC_REPORT_OUTPUT_DIR = System.getProperty("user.dir") + "\\target\\site";
	public static final String JAVA_DOC_DESTINATION_DIR = "apidocs";

	public static String DESTINATION = "";
	public static String SOURCE = System.getProperty("user.dir");
	
	public static String getPackage (String filepath) {
		String[] parts;
		if (System.getProperty("os.name").contains("Windows")) {
			parts = filepath.split("\\\\");
		} else {
			filepath.replaceAll("( )", "\\$0");
			parts = filepath.split("/");
		}
		
		String packageName = "";
		boolean startCopy = false;
		for (int i = 0; i < parts.length - 1; i++) {
			if (parts[i].equals("java")) {
				startCopy = true;
				continue;
			}
			
			if (startCopy) {
				packageName += parts[i] + "/";
			}
		}
		
		return packageName;
	}
}
