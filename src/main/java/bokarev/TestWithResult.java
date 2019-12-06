package bokarev;

import java.util.Optional;

public class TestWithResult {
    private UrlWithCount url;
    private Long result;

    TestWithResult(UrlWithCount url, long result) {
        this.url = url;
        this.result = result;
    }

    public UrlWithCount getUrl() { return url; }
    public long getResult() { return result; }

    public Optional<TestWithResult> getOptResult() {
        if (result != null) {
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }
}
