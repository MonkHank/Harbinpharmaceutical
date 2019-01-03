package com.seuic.hayao.data.bean;

public class SmartUploadResult {

    /**
     * 上传错误时的错误信息
     */
    private String Message;
    /**
     * 是否上传成功
     */
    private boolean Succeed;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isSucceed() {
        return Succeed;
    }

    public void setSucceed(boolean succeed) {
        Succeed = succeed;
    }
}
