package com.seuic.hayao.data.bean;

import com.seuic.hayao.enums.CorpType;

public class SmartProfileInfo {
    /**
     * 用户编号
     */
    private int UserId;
    /**
     * 用户名
     */
    private String UserName;
    /**
     * 显示用户名
     */
    private String UserDisplayName;
    /**
     * 企业Id
     */
    private int CorpId;
    /**
     * 企业编码
     */
    private String CorpCode;
    /**
     * 企业名称
     */
    private String CorpName;
    /**
     * 企业类型
     */
    private CorpType CorpType;
    /**
     * 用户密码
     */
    private String Password;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserDisplayName() {
        return UserDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        UserDisplayName = userDisplayName;
    }

    public int getCorpId() {
        return CorpId;
    }

    public void setCorpId(int corpId) {
        CorpId = corpId;
    }

    public String getCorpCode() {
        return CorpCode;
    }

    public void setCorpCode(String corpCode) {
        CorpCode = corpCode;
    }

    public String getCorpName() {
        return CorpName;
    }

    public void setCorpName(String corpName) {
        CorpName = corpName;
    }

    public CorpType getCorpType() {
        return CorpType;
    }

    public void setCorpType(CorpType corpType) {
        CorpType = corpType;
    }
}
