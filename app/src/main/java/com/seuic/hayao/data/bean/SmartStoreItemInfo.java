package com.seuic.hayao.data.bean;

public class SmartStoreItemInfo {

    private int ProductId;
    private String ProductCode;
    private String ProductName;
    private String ProductUnit;
    private int ProduceCorpId;
    private String ProduceCorpCode;
    private String ProduceCorpName;
    private String ProduceBatchNo;
    private double DisplayAmount;
    private String DisplayProductUnit;
    private double Amount;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductUnit() {
        return ProductUnit;
    }

    public void setProductUnit(String productUnit) {
        ProductUnit = productUnit;
    }

    public int getProduceCorpId() {
        return ProduceCorpId;
    }

    public void setProduceCorpId(int produceCorpId) {
        ProduceCorpId = produceCorpId;
    }

    public String getProduceCorpCode() {
        return ProduceCorpCode;
    }

    public void setProduceCorpCode(String produceCorpCode) {
        ProduceCorpCode = produceCorpCode;
    }

    public String getProduceCorpName() {
        return ProduceCorpName;
    }

    public void setProduceCorpName(String produceCorpName) {
        ProduceCorpName = produceCorpName;
    }

    public String getProduceBatchNo() {
        return ProduceBatchNo;
    }

    public void setProduceBatchNo(String produceBatchNo) {
        ProduceBatchNo = produceBatchNo;
    }

    public double getDisplayAmount() {
        return DisplayAmount;
    }

    public void setDisplayAmount(double displayAmount) {
        DisplayAmount = displayAmount;
    }

    public String getDisplayProductUnit() {
        return DisplayProductUnit;
    }

    public void setDisplayProductUnit(String displayProductUnit) {
        DisplayProductUnit = displayProductUnit;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
}
