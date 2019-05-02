package com.mo2christian.dico.api;

public class Status {

    public static final String OK = "OK", ERROR = "ERROR";
    private String code;

    private String message;

    public Status(){
    }

    public Status(String message) {
        this.code = Status.OK;
        this.message = message;
    }

    public Status(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
