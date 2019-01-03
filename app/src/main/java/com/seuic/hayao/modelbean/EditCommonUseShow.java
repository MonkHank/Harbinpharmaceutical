package com.seuic.hayao.modelbean;

import com.seuic.hayao.data.bean.SmartCorpInfo;
import com.seuic.hayao.data.bean.UserCorpToCorp;

public class EditCommonUseShow implements Comparable<EditCommonUseShow> {

    private SmartCorpInfo corpInfo;
    private UserCorpToCorp CheckStatInfo;
    private String originState;

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String originState) {
        this.originState = originState;
    }

    public SmartCorpInfo getCorpInfo() {
        return corpInfo;
    }

    public void setCorpInfo(SmartCorpInfo corpInfo) {
        this.corpInfo = corpInfo;
    }

    public UserCorpToCorp getCheckStatInfo() {
        return CheckStatInfo;
    }

    public void setCheckStatInfo(UserCorpToCorp checkStatInfo) {
        CheckStatInfo = checkStatInfo;
    }

    @Override
    public int compareTo(EditCommonUseShow another) {
        return 0;
    }
}
