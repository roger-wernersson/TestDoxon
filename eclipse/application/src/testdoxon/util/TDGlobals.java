package testdoxon.util;

import java.util.Properties;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import testdoxon.model.TDFile;

public class TDGlobals {

	public static TDFile currentOpenFile;
	public static TDFile currentTestFile;
 
	public static String orgRootFolder = "";
	public static String rootFolder = "";
	public static int rootJumpbacks = 0;
	public static Properties prop;
	public static long ms_recursiveRead = 0;
	public static boolean shouldSort = true;
	
	public static final String CONFIG_FILE = "config.cfg";
	
	public static Image redDot;
	public static Image greenDot;
	public static Image grayDot;
	public static Image blueDot;
	public static Image yellowDot;
	
	// Icon made by <a href="https://www.flaticon.com/authors/gregor-cresnar">Gregor Cresnar</a> from www.flaticon.com
	// Configure button
	public static ImageDescriptor cog;
	
	// Icon made by <a href="https://www.flaticon.com/authors/freepik">Freepik</a> from www.flaticon.com
	// Statistics button
	public static ImageDescriptor stat;
	
	// Icon made by <a href="https://www.flaticon.com/authors/smashicons">Smashicons</a> from www.flaticon.com
	// List button 
	public static ImageDescriptor list;
	
	// Icon made by <a href="https://www.flaticon.com/authors/vectors-market">Vectors Market</a> from www.flaticon.com
	// Sort button
	public static ImageDescriptor sort;
	
	public static ImageDescriptor sortDisabled;
}
