package testdoxon.model;

public class TDTableItem {
    public static final int NONE = 0;
    public static final int TEST = 1;
    public static final int IGNORE = 2;
    public static final int BOTH_TEST_IGNORE = 3;

    private String methodName;
    private boolean gotTest;
    private boolean gotIgnore;

    public TDTableItem() {
        this.methodName = null;
        this.gotTest = false;
        this.gotIgnore = false;
    }

    public TDTableItem(String methodName, boolean gotTest, boolean gotIgnore) {
        this.methodName = methodName;
        this.gotTest = gotTest;
        this.gotIgnore = gotIgnore;
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

    public int getPictureIndex() {
        if (this.gotTest && this.gotIgnore) {
            return TDTableItem.BOTH_TEST_IGNORE;
        } else if (this.gotTest) {
            return TDTableItem.TEST;
        } else if (this.gotIgnore) {
            return TDTableItem.IGNORE;
        }
        return TDTableItem.NONE;
    }

    public String compareTo(TDTableItem other) {
        if (this.gotTest && this.gotIgnore) {
            return "@test, @Ignore";
        } else if (this.gotTest) {
            return "@test";
        } else if (this.gotIgnore) {
            return "@Ignore";
        }
        return "Missing: @test, @Ignore";
    }
}
