package pers.dlx.mpstreaking.model;

public class RequestResult {
    private boolean success;

    private String errorCode;

    private String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public RequestResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public RequestResult setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public RequestResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
