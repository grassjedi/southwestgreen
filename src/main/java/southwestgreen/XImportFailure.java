package southwestgreen;

public class XImportFailure extends Exception {
    public XImportFailure(String message) {
        super(message);
    }

    public XImportFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
