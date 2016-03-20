package com.sanousun.mdweather.model;

public class BaseResponse<T> {

    public final static int SUC = 0;

    private int errNum;
    private String errMsg;
    private T retData;

    public T getRetData() {
        return retData;

    }

    public void setRetData(T retData) {
        this.retData = retData;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public Boolean isSuc() {
        return errNum == SUC;
    }

}
