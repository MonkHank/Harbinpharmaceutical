package com.seuic.hayao.modelbean;

/**
 * 用户名和密码 对象；
 */
public class LoginInfo {

    private String account;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
