package com.seuic.hayao.data.bean;

public class UserCorpToCorp {

    private String userCorpId;
    private String corpId;
    private String commonUse;
    private String updateFlag;

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getCommonUse() {
        return commonUse;
    }

    public void setCommonUse(String commonUse) {
        this.commonUse = commonUse;
    }

    public String getUserCorpId() {
        return userCorpId;
    }

    public void setUserCorpId(String userCorpId) {
        this.userCorpId = userCorpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
