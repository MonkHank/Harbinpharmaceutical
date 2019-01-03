package com.seuic.hayao.data.bean;

import com.seuic.hayao.enums.StoreKind;

import java.util.ArrayList;
import java.util.Date;

public class SmartStoreInfo {

    /**
     * 企业ID 必填
     */
    private int CorpId;

    /**
     * 企业编码	必填
     */
    private String CorpCode;

    /**
     * 企业名称
     */
    private String CorpName;

    /**
     * 单据ID 必填
     */
    private String StoreId;

    /**
     * 单据号 必填
     */
    private String StoreNo;

    /**
     * 单据时间 必填
     */
    private Date StoreDate;

    /**
     * 单据类型 必填
     */
    private int StoreType;

    /**
     * 单据类型文本 必填
     */
    private String StoreTypeText;

    /**
     * 类型
     */
    private StoreKind StoreKind;

    /**
     * 指派人 必填
     */
    private String StoreMan;

    /**
     * 往来企业 必填
     */
    private int BizCorpId;

    /**
     * 往来企业编码 必填
     */
    private String BizCorpCode;

    /**
     * 往来企业名称 必填
     */
    private String BizCorpName;

    /**
     * 接收企业（针对直调出库）否
     */
    private int RecCorpId;

    /**
     * 接收企业编码（针对直调出库）否
     */
    private String RecCorpCode;

    /**
     * 接收企业名称（针对直调出库）否
     */
    private String RecCorpName;

    /**
     * 描述
     */
    private String Description;

    private int StoreStatus;

    private String StoreStatusText;

    /**
     * 单据明细 否
     */
    private ArrayList<SmartStoreItemInfo> Items;

    private ArrayList<SmartStoreItemInfoEx> ItemsEx;


    public String getCorpName() {
        return CorpName;
    }

    public void setCorpName(String corpName) {
        CorpName = corpName;
    }

    public com.seuic.hayao.enums.StoreKind getStoreKind() {
        return StoreKind;
    }

    public void setStoreKind(com.seuic.hayao.enums.StoreKind storeKind) {
        StoreKind = storeKind;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getStoreStatus() {
        return StoreStatus;
    }

    public void setStoreStatus(int storeStatus) {
        StoreStatus = storeStatus;
    }

    public String getStoreStatusText() {
        return StoreStatusText;
    }

    public void setStoreStatusText(String storeStatusText) {
        StoreStatusText = storeStatusText;
    }

    public ArrayList<SmartStoreItemInfoEx> getItemsEx() {
        return ItemsEx;
    }

    public void setItemsEx(ArrayList<SmartStoreItemInfoEx> itemsEx) {
        ItemsEx = itemsEx;
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

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public String getStoreNo() {
        return StoreNo;
    }

    public void setStoreNo(String storeNo) {
        StoreNo = storeNo;
    }

    public Date getStoreDate() {
        return StoreDate;
    }

    public void setStoreDate(Date storeDate) {
        this.StoreDate = storeDate;
    }

    public int getStoreType() {
        return StoreType;
    }

    public void setStoreType(int storeType) {
        StoreType = storeType;
    }

    public String getStoreTypeText() {
        return StoreTypeText;
    }

    public void setStoreTypeText(String storeTypeText) {
        StoreTypeText = storeTypeText;
    }

    public String getStoreMan() {
        return StoreMan;
    }

    public void setStoreMan(String storeMan) {
        StoreMan = storeMan;
    }

    public int getBizCorpId() {
        return BizCorpId;
    }

    public void setBizCorpId(int bizCorpId) {
        BizCorpId = bizCorpId;
    }

    public String getBizCorpCode() {
        return BizCorpCode;
    }

    public void setBizCorpCode(String bizCorpCode) {
        BizCorpCode = bizCorpCode;
    }

    public String getBizCorpName() {
        return BizCorpName;
    }

    public void setBizCorpName(String bizCorpName) {
        BizCorpName = bizCorpName;
    }

    public int getRecCorpId() {
        return RecCorpId;
    }

    public void setRecCorpId(int recCorpId) {
        RecCorpId = recCorpId;
    }

    public String getRecCorpCode() {
        return RecCorpCode;
    }

    public void setRecCorpCode(String recCorpCode) {
        RecCorpCode = recCorpCode;
    }

    public String getRecCorpName() {
        return RecCorpName;
    }

    public void setRecCorpName(String recCorpName) {
        RecCorpName = recCorpName;
    }

    public ArrayList<SmartStoreItemInfo> getItems() {
        return Items;
    }

    public void setItems(ArrayList<SmartStoreItemInfo> items) {
        Items = items;
    }

}
