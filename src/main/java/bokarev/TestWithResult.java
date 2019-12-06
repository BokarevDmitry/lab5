package bokarev;

public class TestWithResult {
    private String url;
    private Long result;

    TestWithResult(String url, long result) {
        this.url = url;
        this.result = result;
    }

    public String getUrl() { return url; }
    public long getResult() { return result; }
}
