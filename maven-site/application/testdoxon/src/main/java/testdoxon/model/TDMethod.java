package testdoxon.model;

import testdoxon.util.TDConstants;

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
			case TDConstants.TEST:
				return "<img src='pics/gray.png' />";
			case TDConstants.IGNORE:
				return "<img src='pics/blue.png' />";
			case TDConstants.TEST_IGNORE:
				return "<img src='pics/green.png' />";
			case TDConstants.NONE:
				return "<img src='pics/red.png' />";
			default:
				return "";
		}
	}
}
