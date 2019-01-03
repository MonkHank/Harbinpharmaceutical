package com.seuic.hayao.data.remote;

import com.seuic.hayao.data.bean.SmartStoreItemInfo;
import com.seuic.hayao.data.bean.SmartStoreItemInfoEx;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.seuic.hayao.enums.StoreKind;

import java.util.Date;
import java.util.Hashtable;

public class SmartStoreInfo implements KvmSerializable {

    private int CorpId;
    private String CorpCode;
    private String CorpName;
    private String StoreId;
    private String StoreNo;
    private String StoreDate;
    private int StoreType;
    private String StoreTypeText;
    private String StoreKind;
    private String StoreMan;
    private String BizCorpId;
    private String BizCorpCode;
    private String BizCorpName;
    private int RecCorpId;
    private String RecCorpCode;
    private String RecCorpName;
    private String Description;
    private int StoreStatus;
    private String StoreStatusText;
    private SmartStoreItemInfo[] Items;
    private SmartStoreItemInfoEx[] ItemsEx;

    @Override
    public int getPropertyCount() {
        return 21;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return CorpId;
            case 1:
                return CorpCode;
            case 2:
                return CorpName;
            case 3:
                return StoreId;
            case 4:
                return StoreNo;
            case 5:
                return StoreDate;
            case 6:
                return StoreType;
            case 7:
                return StoreTypeText;
            case 8:
                return StoreKind;
            case 9:
                return StoreMan;
            case 10:
                return BizCorpId;
            case 11:
                return BizCorpCode;
            case 12:
                return BizCorpName;
            case 13:
                return RecCorpId;
            case 14:
                return RecCorpCode;
            case 15:
                return RecCorpName;
            case 16:
                return Description;
            case 17:
                return StoreStatus;
            case 18:
                return StoreStatusText;
            case 19:
                return Items;
            case 20:
                return ItemsEx;
            default:
                break;
        }
        return null;
    }


    @Override
    public void setProperty(int i, Object o) {
        if (o == null) {
            return;
        }
        switch (i) {
            case 0:
                CorpId = (int) o;
                break;
            case 1:
                CorpCode = o.toString();
                break;
            case 2:
                CorpName = o.toString();
                break;
            case 3:
                StoreId = o.toString();
                break;
            case 4:
                StoreNo = o.toString();
                break;
            case 5:
                StoreDate = o.toString();
                break;
            case 6:
                StoreType = (int) o;
                break;
            case 7:
                StoreTypeText = o.toString();
                break;
            case 8:
                StoreKind = o.toString();
                break;
            case 9:
                StoreMan = o.toString();
                break;
            case 10:
                BizCorpId = o.toString();
                break;
            case 11:
                BizCorpCode = o.toString();
                break;
            case 12:
                BizCorpName = o.toString();
                break;
            case 13:
                RecCorpId = (int) o;
                break;
            case 14:
                RecCorpCode = o.toString();
                break;
            case 15:
                RecCorpName = o.toString();
                break;
            case 16:
                Description = o.toString();
                break;
            case 17:
                StoreStatus = (int) o;
                break;
            case 18:
                StoreStatusText = o.toString();
                break;
            case 19:
                Items = (SmartStoreItemInfo[]) o;
                break;
            case 20:
                ItemsEx = (SmartStoreItemInfoEx[]) o;
                break;
            default:
                break;
        }

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i) {
            case 0:
                propertyInfo.type = Integer.class;
                propertyInfo.name = "CorpId";
                break;
            case 1:
                propertyInfo.type = String.class;
                propertyInfo.name = "CorpCode";
                break;
            case 2:
                propertyInfo.type = String.class;
                propertyInfo.name = "CorpName";
                break;
            case 3:
                propertyInfo.type = String.class;
                propertyInfo.name = "StoreId";
                break;
            case 4:
                propertyInfo.type = String.class;
                propertyInfo.name = "StoreNo";
                break;
            case 5:
                propertyInfo.type = Date.class;
                propertyInfo.name = "StoreDate";
                break;
            case 6:
                propertyInfo.type = Integer.class;
                propertyInfo.name = "StoreType";
                break;
            case 7:
                propertyInfo.type = String.class;
                propertyInfo.name = "StoreTypeText";
                break;
            case 8:
                propertyInfo.type = String.class;
                propertyInfo.name = "StoreKind";
                break;
            case 9:
                propertyInfo.type = String.class;
                propertyInfo.name = "StoreMan";
                break;
            case 10:
                propertyInfo.type = Integer.class;
                propertyInfo.name = "BizCorpId";
                break;
            case 11:
                propertyInfo.type = String.class;
                propertyInfo.name = "BizCorpCode";
                break;
            case 12:
                propertyInfo.type = String.class;
                propertyInfo.name = "BizCorpName";
                break;
            case 13:
                propertyInfo.type = Integer.class;
                propertyInfo.name = "RecCorpId";
                break;
            case 14:
                propertyInfo.type = String.class;
                propertyInfo.name = "RecCorpCode";
                break;
            case 15:
                propertyInfo.type = String.class;
                propertyInfo.name = "RecCorpName";
                break;
            case 16:
                propertyInfo.type = String.class;
                propertyInfo.name = "Description";
                break;
            case 17:
                propertyInfo.type = Integer.class;
                propertyInfo.name = "StoreStatus";
                break;
            case 18:
                propertyInfo.type = String.class;
                propertyInfo.name = "StoreStatusText";
                break;
            case 19:
                propertyInfo.type = SmartStoreItemInfo[].class;
                propertyInfo.name = "Items";
                break;
            case 20:
                propertyInfo.type = SmartStoreItemInfoEx[].class;
                propertyInfo.name = "ItemsEx";
                break;
            default:
                break;
        }
    }
}
