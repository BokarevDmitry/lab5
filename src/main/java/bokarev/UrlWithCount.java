package bokarev;

public class UrlWithCount {
    private String url;
    private int count;

    UrlWithCount(String url, int count) {
        this.url = url;
        this.count = count;
    }

    public String getUrl() { return url; }
    public int getCount() { return count; }
}
