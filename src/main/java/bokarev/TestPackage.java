package bokarev;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class TestPackage {
    Integer packageId;
    String jsScript, functionName;
    ArrayList<OneTest> testsLists;

    @JsonCreator
    TestPackage(
            @JsonProperty("packageId") Integer packageId,
            @JsonProperty("jsScript") String jsScript,
            @JsonProperty("functionName") String functionName,
            @JsonProperty("tests") ArrayList<OneTest> testsLists) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.testsLists = testsLists;
    }

    TestPackage(TestForImpl test) {
        this.packageId = test.packageId;
        this.jsScript = test.jsScript;
        this.functionName = test.functionName;
        this.testsLists = new ArrayList<>();
        this.testsLists.add(test.oneTest);
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

    public ArrayList<OneTest> getTestsLists() {
        return testsLists;
    }
}