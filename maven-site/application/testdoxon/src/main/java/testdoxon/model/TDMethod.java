package testdoxon.model;

import testdoxon.util.TDGlobals;

public class TDMethod {
	private int picIndex;
	private String methodname;
	
	public TDMethod (String methodname, int picIndex) {
		this.methodname = methodname;
		this.picIndex = picIndex;
	}
	
	public int getPictureIndex () {
		return this.picIndex;
	}
	
	public String getMethodname () {
		return this.methodname;
	}
	
	public String getPictureHTML () {
		switch (this.picIndex) {
			case TDGlobals.TEST:
				return "<img src='pics/green.png' />";
			case TDGlobals.IGNORE:
				return "<img src='pics/gray.png' />";
			case TDGlobals.TEST_IGNORE:
				return "<img src='pics/yellow.png' />";
			case TDGlobals.NONE:
				return "<img src='pics/red.png' />";
			default:
				return "";
		}
	}
}
