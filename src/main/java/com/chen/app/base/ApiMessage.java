package com.chen.app.base;

/**
 * Created by: ccong
 * Date: 18/8/25 下午8:56
 */
public class ApiMessage {
    private int code;
    private String message;
    private Object data;
    private boolean more;

    public ApiMessage() {
        this.code = Status.SUCCESS.code;
        this.message = Status.SUCCESS.standarMessage;
    }

    public ApiMessage(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public enum Status {
        SUCCESS(200,"OK"),
        BAD_REQUEST(400,"Bad Request"),
        NOT_FOUND(404,"Not Found"),
        INTERNAL_SERVER_ERROR(500,"Server Error"),
        NOT_VALID_PARAM(40005,"Not Valid Param"),
        NOT_SUPPORTED_OPERATION(40006,"Not Supported Operation"),
        NOT_LOGIN(50000,"Not Login");

        private int code;
        private String standarMessage;

        Status(int code, String standarMessage) {
            this.code = code;
            this.standarMessage = standarMessage;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandarMessage() {
            return standarMessage;
        }

        public void setStandarMessage(String standarMessage) {
            this.standarMessage = standarMessage;
        }
    }

    public static ApiMessage ofMessage(int code, String message) {
        return new ApiMessage(code,message,null);
    }
    public static ApiMessage ofSuccess(Object data) {
        ApiMessage am = new ApiMessage();
        am.setData(data);
        return am;
    }
    public static ApiMessage ofStatus(Status status) {
        return new ApiMessage(status.code,status.standarMessage,null);
    }
}
