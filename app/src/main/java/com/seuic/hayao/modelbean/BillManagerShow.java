package com.seuic.hayao.modelbean;

public class BillManagerShow {

    private String billNumber;
    private String contactCorpName;
    private String billType;
    private String codeNumber;
    private boolean isUpload;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getContactCorpName() {
        return contactCorpName;
    }

    public void setContactCorpName(String contactCorpName) {
        this.contactCorpName = contactCorpName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }


}
