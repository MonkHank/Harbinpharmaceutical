package com.seuic.hayao.data.bean;

import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.enums.StoreSort;

import java.io.Serializable;

public class StoreTypeInfo implements Serializable {

    private StoreKind StoreKind;
    private StoreSort StoreSort;
    private int StoreType;
    private String StoreTypeKey;
    private boolean HasBizCorp;
    private boolean HasRecCorp;
    private int UpStoreType;
    private int DownStoreType;
    private String StoreTypeText;

    public String getStoreTypeText() {
        return StoreTypeText;
    }

    public void setStoreTypeText(String storeTypeText) {
        StoreTypeText = storeTypeText;
    }

    public com.seuic.hayao.enums.StoreKind getStoreKind() {
        return StoreKind;
    }

    public void setStoreKind(com.seuic.hayao.enums.StoreKind storeKind) {
        StoreKind = storeKind;
    }

    public StoreSort getStoreSort() {
        return StoreSort;
    }

    public void setStoreSort(StoreSort storeSort) {
        StoreSort = storeSort;
    }

    public int getStoreType() {
        return StoreType;
    }

    public void setStoreType(int storeType) {
        StoreType = storeType;
    }

    public String getStoreTypeKey() {
        return StoreTypeKey;
    }

    public void setStoreTypeKey(String storeTypeKey) {
        StoreTypeKey = storeTypeKey;
    }

    public boolean isHasBizCorp() {
        return HasBizCorp;
    }

    public void setHasBizCorp(boolean hasBizCorp) {
        HasBizCorp = hasBizCorp;
    }

    public boolean isHasRecCorp() {
        return HasRecCorp;
    }

    public void setHasRecCorp(boolean hasRecCorp) {
        HasRecCorp = hasRecCorp;
    }

    public int getUpStoreType() {
        return UpStoreType;
    }

    public void setUpStoreType(int upStoreType) {
        UpStoreType = upStoreType;
    }

    public int getDownStoreType() {
        return DownStoreType;
    }

    public void setDownStoreType(int downStoreType) {
        DownStoreType = downStoreType;
    }
}
