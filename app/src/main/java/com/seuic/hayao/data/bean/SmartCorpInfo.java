package com.seuic.hayao.data.bean;

import java.io.Serializable;

public class SmartCorpInfo implements Serializable {

    /**
     * 企业编码
     */
    private String CorpCode;
    /**
     * 企业Id
     */
    private int CorpId;
    /**
     * 企业名称
     */
    private String CorpName;
    /**
     * 企业名称拼音简码
     */
    private String CorpPinyin;

    public String getCorpCode() {
        return CorpCode;
    }

    public void setCorpCode(String corpCode) {
        CorpCode = corpCode;
    }

    public int getCorpId() {
        return CorpId;
    }

    public void setCorpId(int corpId) {
        CorpId = corpId;
    }

    public String getCorpName() {
        return CorpName;
    }

    public void setCorpName(String corpName) {
        CorpName = corpName;
    }

    public String getCorpPinyin() {
        return CorpPinyin;
    }

    public void setCorpPinyin(String corpPinyin) {
        CorpPinyin = corpPinyin;
    }
}
