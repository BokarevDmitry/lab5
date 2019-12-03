package bokarev;

public class TestToStore {
    private final String url;
    private final double time;

    public TestToStore(String url, double time) {
        this.url = url;
        this.time = time;
    }

    public String getUrl() { return url; }
    public double getTime() { return time; }
}