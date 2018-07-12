package testdoxon.utils;

import testdoxon.model.TDFile;

import java.util.Properties;

public class TDStatics {
    public static TDFile currentOpenFile = null;
    public static TDFile currentTestFile = null;

    public static String rootFolder = "";
    public static String orgRootFolder = "";
    public static int rootJumpbacks = 0;

    public static String CONFIG_FILE = "config.cfg";

    public static long ms_recursiveRead = 0;

    public static Properties prop = new Properties();

    public static boolean sortMethodList = false;
}
