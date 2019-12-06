package bokarev;

public class TestWithResult {
    private UrlWithCount url;
    private Long result;

    TestWithResult(UrlWithCount url, long result) {
        this.url = url;
        this.result = result;
    }

    public UrlWithCount getUrl() { return url; }
    public long getResult() { return result; }
}
