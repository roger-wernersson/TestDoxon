package testdoxon.model;

import testdoxon.util.TDGlobals;

public class TDMethod {
	private int picIndex;
	private String methodname;
	private String _package;
	
	public TDMethod (String methodname, int picIndex, String _package) {
		this.methodname = methodname;
		this.picIndex = picIndex;
		this._package = _package;
	}
	
	public int getPictureIndex () {
		return this.picIndex;
	}
	
	public String getMethodname () {
		return this.methodname;
	}
	
	public String getPackage () {
		return this._package;
	}
	
	public String getPictureHTML () {
		String[] parts = this.getPackage().split("/");
		String imageFolder = "";
		for (int i = 0; i < parts.length; i++) {
			imageFolder += "../";
		}
		imageFolder += "td_pics/";
		
		switch (this.picIndex) {
			case TDGlobals.MISSING_TEST_IN_NAME:
				return "<img src='" + imageFolder + "blue.png' />";
			case TDGlobals.TEST:
				return "<img src='" + imageFolder + "green.png' />";
			case TDGlobals.IGNORE:
				return "<img src='" + imageFolder + "gray.png' />";
			case TDGlobals.TEST_IGNORE:
				return "<img src='" + imageFolder + "yellow.png' />";
			case TDGlobals.NONE:
				return "<img src='" + imageFolder + "red.png' />";
			default:
				return "";
		}
	}
	
	public String getToolTip () {
		switch (this.picIndex) {
		case TDGlobals.MISSING_TEST_IN_NAME:
			return "Missing test in method name";
		case TDGlobals.TEST:
			return "Present: @Test, Missing: @Ignore";
		case TDGlobals.IGNORE:
			return "Present: @Ignore, Missing: @Test";
		case TDGlobals.TEST_IGNORE:
			return "Present: @Test, @Ignore, Missing:";
		case TDGlobals.NONE:
			return "Present: , Missing: @Test, @Ignore";
		default:
			return "";
	}
	}
}
