package bokarev;

public class TestForImpl {
    final Integer packageId;
    final String jsScript, functionName;
    OneTest oneTest;

    TestForImpl(TestPackage test, int indexOfTest) {
        this.packageId = test.packageId;
        this.jsScript = test.jsScript;
        this.functionName = test.functionName;

        this.oneTest = new OneTest(
                test.testsLists.get(indexOfTest).testName,
                test.testsLists.get(indexOfTest).expectedResult,
                test.testsLists.get(indexOfTest).params,
                null);
    }

    void setResult (Boolean result){
        this.oneTest.result = result;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public String getJsScript() {
        return jsScript;
    }

    public String getFunctionName() {
        return functionName;
    }

    public OneTest getOneTest() {
        return oneTest;
    }
}
