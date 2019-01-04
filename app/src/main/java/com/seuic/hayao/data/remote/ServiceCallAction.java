package com.seuic.hayao.data.remote;

import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.SmartProfileInfo;
import com.seuic.hayao.data.bean.SmartStoreCodeInfo;
import com.seuic.hayao.data.bean.SmartUploadResult;
import com.seuic.hayao.data.bean.StoreTypeInfo;
import com.seuic.hayao.enums.CorpType;
import com.seuic.hayao.enums.StoreKind;
import com.seuic.hayao.enums.StoreSort;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceCallAction extends BaseServiceCall implements IServiceInterface {

    private static ServiceCallAction services;

    private ServiceCallAction() {
    }

    public static ServiceCallAction getInstance() {
        if (services == null) {
            services = new ServiceCallAction();
        }
        return services;
    }

    @Override
    public int GetBizCorpCount(int corpId) throws Exception {
        String methodName = "GetBizCorpCount";
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        soapObject.addProperty("corpId", corpId);
        SoapObject result = webServiceAccess(methodName, soapObject);
        return Integer.parseInt(result.getProperty("GetBizCorpCountResult").toString());
    }

    @Override
    public ArrayList<SmartCorpInfo> GetBizCorpsPage(int CorpId, int pageIndex, int pageSize) throws Exception {
        String methodName = "GetBizCorpsPage";
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        soapObject.addProperty("corpId", CorpId);
        soapObject.addProperty("pageIndex", pageIndex);
        soapObject.addProperty("pageSize", pageSize);
        SoapObject result = (SoapObject) webServiceAccess(methodName, soapObject).getProperty("GetBizCorpsPageResult");
        ArrayList<SmartCorpInfo> infos = new ArrayList<SmartCorpInfo>();
        for (int i = 0; i < result.getPropertyCount(); i++) {
            SoapObject corpObj = (SoapObject) result.getProperty(i);
            SmartCorpInfo info = new SmartCorpInfo();
            info.setCorpPinyin(corpObj.getProperty("CorpPinyin").toString());
            info.setCorpName(corpObj.getProperty("CorpName").toString());
            info.setCorpCode(corpObj.getProperty("CorpCode").toString());
            info.setCorpId(Integer.parseInt(corpObj.getProperty("CorpId").toString()));
            infos.add(info);
        }
        return infos;
    }

    @Override
    public Date GetCurrentTime() throws Exception {
        String methodName = "GetCurrentTime";
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        SoapObject result = webServiceAccess(methodName, soapObject);
        String time = result.getProperty(0).toString();//2016-02-24T19:42:26.56325+08:00
        time = time.replace("T", "").replace("+08:00", "");
        return new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSSS").parse(time);
    }

    @Override
    public SmartProfileInfo GetProfileInfo(String userName) throws Exception {
        String methodName = "GetProfileInfo";
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        soapObject.addProperty("userName", userName);
        SoapObject result = (SoapObject) webServiceAccess(methodName, soapObject).getProperty("GetProfileInfoResult");
        SmartProfileInfo info = new SmartProfileInfo();
        info.setUserId(Integer.parseInt(result.getProperty("UserId").toString()));
        info.setUserName(result.getProperty("UserName").toString());
        info.setUserDisplayName(result.getProperty("UserDisplayName").toString());
        info.setCorpId(Integer.parseInt(result.getProperty("CorpId").toString()));
        info.setCorpCode(result.getProperty("CorpCode").toString());
        info.setCorpName(result.getProperty("CorpName").toString());
        info.setCorpType(CorpType.valueOf(result.getProperty("CorpType").toString()));
        return info;
    }

    //第一次验证登陆要知道password

    /**
     * 获取当前用户相关信息 （需要身份验证）
     * @param userName
     * @param password
     * @return SmartProfileInfo
     * @throws Exception
     */
    public SmartProfileInfo GetProfileInfo(String userName, String password) throws Exception {
        String methodName = "GetProfileInfo";
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        soapObject.addProperty("userName", userName);
        //用户输入的用户名密码来初始化webservice请求头的验证，
        initHeader(userName, password);
        SoapObject result = (SoapObject) webServiceAccess(methodName, soapObject).getProperty("GetProfileInfoResult");
        SmartProfileInfo info = new SmartProfileInfo();
        info.setUserId(Integer.parseInt(result.getProperty("UserId").toString()));
        info.setUserName(result.getProperty("UserName").toString());
        info.setUserDisplayName(result.getProperty("UserDisplayName").toString());
        info.setCorpId(Integer.parseInt(result.getProperty("CorpId").toString()));
        info.setCorpCode(result.getProperty("CorpCode").toString());
        info.setCorpName(result.getProperty("CorpName").toString());
        info.setCorpType(CorpType.valueOf(result.getProperty("CorpType").toString()));
        info.setPassword(password);
        return info;
    }


    @Override
    public ArrayList<StoreTypeInfo> GetStoreTypes() throws Exception {
        String methodName = "GetStoreTypes";
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        SoapObject result = (SoapObject) webServiceAccess(methodName, soapObject).getProperty("GetStoreTypesResult");
        ArrayList<StoreTypeInfo> infos = new ArrayList<StoreTypeInfo>();
        for (int i = 0; i < result.getPropertyCount(); i++) {
            SoapObject storeTypes = (SoapObject) result.getProperty(i);
            StoreTypeInfo info = new StoreTypeInfo();
            info.setHasBizCorp(Boolean.parseBoolean(storeTypes.getProperty("HasBizCorp").toString()));
            info.setHasRecCorp(Boolean.parseBoolean(storeTypes.getProperty("HasRecCorp").toString()));
            info.setStoreKind(StoreKind.valueOf(storeTypes.getProperty("StoreKind").toString()));
            info.setStoreSort(StoreSort.valueOf(storeTypes.getProperty("StoreSort").toString()));
            info.setStoreType(Integer.parseInt(storeTypes.getProperty("StoreType").toString()));
            info.setStoreTypeKey(storeTypes.getProperty("StoreTypeKey").toString());
            info.setStoreTypeText(storeTypes.getProperty("StoreTypeText").toString());
            if (storeTypes.getProperty("DownStoreType") != null) {
                info.setDownStoreType(Integer.parseInt(storeTypes.getProperty("DownStoreType").toString()));
            }
            if (storeTypes.getProperty("UpStoreType") != null) {
                info.setUpStoreType(Integer.parseInt(storeTypes.getProperty("UpStoreType").toString()));
            }
            infos.add(info);
        }
        return infos;
    }

    @Override
    public SmartUploadResult UploadStore(int userId,
                                         com.seuic.hayao.data.bean.SmartStoreInfo info,
                                         ArrayList<SmartStoreCodeInfo> codes, ArrayList<SmartStoreCodeInfo> removecodes) throws Exception {

        //removecodes is null,do not pass parameters
        String methodName = "UploadStore";
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);

        //the first
        soapObject.addProperty("userId", userId);

        //the second
        SmartStoreInfo requstParmInfo = new SmartStoreInfo();
        requstParmInfo.setProperty(0, info.getCorpId());
        requstParmInfo.setProperty(1, info.getCorpCode());
        requstParmInfo.setProperty(2, info.getCorpName());
        requstParmInfo.setProperty(3, info.getStoreId());
        requstParmInfo.setProperty(4, info.getStoreNo());
        requstParmInfo.setProperty(5, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(info.getStoreDate()));
        requstParmInfo.setProperty(6, info.getStoreType());
        requstParmInfo.setProperty(7, info.getStoreTypeText());
        requstParmInfo.setProperty(8, info.getStoreKind().name());
        requstParmInfo.setProperty(9, info.getStoreMan());
        requstParmInfo.setProperty(10, info.getBizCorpId());
        requstParmInfo.setProperty(11, info.getBizCorpCode());
        requstParmInfo.setProperty(12, info.getBizCorpName());
        requstParmInfo.setProperty(13, info.getRecCorpId());
        requstParmInfo.setProperty(14, info.getRecCorpCode());
        requstParmInfo.setProperty(15, info.getRecCorpName());
        requstParmInfo.setProperty(16, info.getDescription());
        requstParmInfo.setProperty(17, info.getStoreStatus());
        requstParmInfo.setProperty(18, info.getStoreStatusText());
//        requstParmInfo.setProperty(19, info.getItems().toArray());
//        requstParmInfo.setProperty(20, info.getItemsEx().toArray());
        PropertyInfo infoP = new PropertyInfo();
        infoP.setName("info");
        infoP.setValue(requstParmInfo);
        infoP.setType(requstParmInfo.getClass());
        soapObject.addProperty(infoP);

        //the third
        SmartStoreCodeInfoSerializableList codesInfos = new SmartStoreCodeInfoSerializableList();
        for (int i = 0; i < codes.size(); i++) {
            com.seuic.hayao.data.bean.SmartStoreCodeInfo codeinfo = codes.get(i);
            SmartStoreCodeInfoSerializableList.SmartStoreCodeInfo requstParmCode = codesInfos.new SmartStoreCodeInfo();
            requstParmCode.setProperty(0, codeinfo.isByParent());
            requstParmCode.setProperty(1, codeinfo.getParentLevel());
            requstParmCode.setProperty(2, codeinfo.isParentCode());
            requstParmCode.setProperty(3, codeinfo.getCodeId());
            requstParmCode.setProperty(4, codeinfo.getSavedCodeId());
            requstParmCode.setProperty(5, codeinfo.getSavedCount());
            requstParmCode.setProperty(6, codeinfo.getSavedCodeLevel());
            requstParmCode.setProperty(7, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(codeinfo.getActTime()));
            requstParmCode.setProperty(8, codeinfo.getActor());
            codesInfos.setProperty(i, requstParmCode);
        }
        PropertyInfo codesP = new PropertyInfo();
        codesP.setName("codes");
        codesP.setValue(codesInfos);
        codesP.setType(codesInfos.getClass());
        soapObject.addProperty(codesP);

        //the 4th

        //add mapList
        List<Class<?>> mapList = new ArrayList<Class<?>>();
        mapList.add(requstParmInfo.getClass());
        mapList.add(codesInfos.getClass());
        mapList.add(SmartStoreCodeInfoSerializableList.SmartStoreCodeInfo.class);
        SoapObject result = (SoapObject) webServiceAccess(methodName, soapObject, mapList).getProperty("UploadStoreResult");
        SmartUploadResult uploadResult = new SmartUploadResult();
        uploadResult.setSucceed(Boolean.parseBoolean(result.getProperty("Succeed").toString()));
        String msg = "";
        try {
            msg = result.getProperty("Message").toString();
        } catch (Exception e) {
        }
        uploadResult.setMessage(msg);
        return uploadResult;
    }
}
