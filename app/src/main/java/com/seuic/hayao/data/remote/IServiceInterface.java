package com.seuic.hayao.data.remote;

import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.SmartProfileInfo;
import com.seuic.hayao.data.bean.SmartStoreCodeInfo;
import com.seuic.hayao.data.bean.SmartStoreInfo;
import com.seuic.hayao.data.bean.SmartUploadResult;
import com.seuic.hayao.data.bean.StoreTypeInfo;

import java.util.ArrayList;
import java.util.Date;

public interface IServiceInterface {

    /**
     * 获取企业往来企业数量[需要身份认证]
     *
     * @param corpId 企业Id，登录用户的企业Id
     * @return 和企业Id相同的往来企业的数量
     */
    public int GetBizCorpCount(int corpId) throws Exception;

    /**
     * 分页获取往来企业信息[需要身份认证]
     *
     * @param CorpId    企业Id，登录用户的企业Id
     * @param pageIndex 当前页
     * @param pageSize  页面大小
     * @return 往来企业集合
     */
    public ArrayList<SmartCorpInfo> GetBizCorpsPage(int CorpId, int pageIndex, int pageSize) throws Exception;

    /**
     * 获取服务器时间
     *
     * @return 服务器时间
     */
    public Date GetCurrentTime() throws Exception;

    /**
     * 通过用户名获取用户信息
     *
     * @param userName 登录用户的账号,首先要通过身份验证
     * @return 企业类型
     */
    public SmartProfileInfo GetProfileInfo(String userName) throws Exception;

    /**
     * 获取系统所有的单据类型(出入库类型)[需要身份认证]
     *
     * @return
     */
    public ArrayList<StoreTypeInfo> GetStoreTypes() throws Exception;

    /**
     * 上传单据[需要身份认证]
     *
     * @param userId      用户Id
     * @param info        单据信息
     * @param codes       上传码列表
     * @param removecodes 删除码列表
     * @return 上传结果
     */
    public SmartUploadResult UploadStore(int userId, SmartStoreInfo info, ArrayList<SmartStoreCodeInfo> codes, ArrayList<SmartStoreCodeInfo> removecodes) throws Exception;

}
