package com.seuic.hayao.data.bean;

import java.util.Date;

public class SmartStoreCodeInfo {

    private boolean ByParent;
    private int ParentLevel;
    private boolean IsParentCode;
    private String CodeId;
    private String SavedCodeId;
    private int SavedCount;
    private int SavedCodeLevel;
    private Date ActTime;
    private String Actor;

    public boolean isByParent() {
        return ByParent;
    }

    public void setByParent(boolean byParent) {
        ByParent = byParent;
    }

    public int getParentLevel() {
        return ParentLevel;
    }

    public void setParentLevel(int parentLevel) {
        ParentLevel = parentLevel;
    }

    public boolean isParentCode() {
        return IsParentCode;
    }

    public void setIsParentCode(boolean isParentCode) {
        IsParentCode = isParentCode;
    }

    public String getCodeId() {
        return CodeId;
    }

    public void setCodeId(String codeId) {
        CodeId = codeId;
    }

    public String getSavedCodeId() {
        return SavedCodeId;
    }

    public void setSavedCodeId(String savedCodeId) {
        SavedCodeId = savedCodeId;
    }

    public int getSavedCount() {
        return SavedCount;
    }

    public void setSavedCount(int savedCount) {
        SavedCount = savedCount;
    }

    public int getSavedCodeLevel() {
        return SavedCodeLevel;
    }

    public void setSavedCodeLevel(int savedCodeLevel) {
        SavedCodeLevel = savedCodeLevel;
    }

    public Date getActTime() {
        return ActTime;
    }

    public void setActTime(Date actTime) {
        ActTime = actTime;
    }

    public String getActor() {
        return Actor;
    }

    public void setActor(String actor) {
        Actor = actor;
    }
}
