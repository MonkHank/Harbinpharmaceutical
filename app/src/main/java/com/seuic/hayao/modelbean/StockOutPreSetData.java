package com.seuic.hayao.modelbean;

import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.StoreTypeInfo;

import java.io.Serializable;
import java.util.Date;

public class StockOutPreSetData implements Serializable {
    private String billNumb;
    private StoreTypeInfo billType;
    private Date createTime;
    private SmartCorpInfo contactCompany;

    public StockOutPreSetData() {
    }

    public StockOutPreSetData(String billNumb, StoreTypeInfo billType, SmartCorpInfo contactCompany, Date createTime) {
        this.billNumb = billNumb;
        this.billType = billType;
        this.contactCompany = contactCompany;
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBillNumb() {

        return billNumb;
    }

    public void setBillNumb(String billNumb) {
        this.billNumb = billNumb;
    }

    public StoreTypeInfo getBillType() {
        return billType;
    }

    public void setBillType(StoreTypeInfo billType) {
        this.billType = billType;
    }

    public SmartCorpInfo getContactCompany() {
        return contactCompany;
    }

    public void setContactCompany(SmartCorpInfo contactCompany) {
        this.contactCompany = contactCompany;
    }
}
