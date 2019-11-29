package bokarev;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OneTest {
    String testName;
    Double expectedResult;
    Object[] params;
    Boolean result;

    @JsonCreator
    OneTest(@JsonProperty("testName") String testName,
            @JsonProperty("expectedResult") Double expectedResult,
            @JsonProperty("params") Object[] params,
            @JsonProperty("result") Boolean result) {
        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
        this.result = null;
    }

    public String getTestName() {
        return testName;
    }

    public Double getExpectedResult() {
        return expectedResult;
    }

    public Object[] getParams() {
        return params;
    }

    public Boolean getResult() {
        return result;
    }
}
