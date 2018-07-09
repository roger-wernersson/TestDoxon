package testdoxon.model;

public class TDTableItem {
	public static final int NONE = 0;
	public static final int TEST = 1;
	public static final int IGNORE = 2;
	public static final int BOTH_TEST_IGNORE = 3;
	public static final int BAD_METHOD_NAME = 4;
	
	private String methodName;
	private boolean gotTest;
	private boolean gotIgnore;
	private boolean hasTestInName;
	private boolean hasFirstCharUppercase;
	
	public TDTableItem () {
		this.methodName = null;
		this.gotTest = false;
		this.gotIgnore = false;
		this.hasTestInName = false;
		this.hasFirstCharUppercase = false;
	}
	
	public TDTableItem(String methodName, boolean gotTest, boolean gotIgnore, boolean hasTestInName, boolean hasFirstCharUppercase) {
		this.methodName = methodName;
		this.gotTest = gotTest;
		this.gotIgnore = gotIgnore;
		this.hasTestInName = hasTestInName;
		this.hasFirstCharUppercase = hasFirstCharUppercase;
	}

	public String getMethodName() {
		return methodName;
	}

	public boolean isGotTest() {
		return gotTest;
	}

	public boolean isGotIgnore() {
		return gotIgnore;
	}
	
	public int getPictureIndex () {
		if(this.gotTest && this.gotIgnore && this.hasTestInName && this.hasFirstCharUppercase) {
			return TDTableItem.BOTH_TEST_IGNORE;
		} else if (!this.hasTestInName || !this.hasFirstCharUppercase) {
			return TDTableItem.BAD_METHOD_NAME;
		} else if (this.gotTest) {
			return TDTableItem.TEST;
		} else if(this.gotIgnore) {
			return TDTableItem.IGNORE;
		}
		return TDTableItem.NONE;
	}
	
	public String compareTo(TDTableItem other) {
		if(this.gotTest && this.gotIgnore && this.hasTestInName && this.hasFirstCharUppercase) {
			return "@test, @Ignore";
		} else if (!this.hasTestInName || !this.hasFirstCharUppercase) {
			return "Bad method name";
		} else if (this.gotTest) {
			return "@test";
		} else if(this.gotIgnore) {
			return "@Ignore";
		}
		return "Missing: @test, @Ignore, test in method name";
	}
	
}
