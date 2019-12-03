package bokarev;

public class Request {
    int packageId;

    TestGetter(int packageId) {
        this.packageId = packageId;
    }

    public int getPackageId() {
        return packageId;
    }
}
