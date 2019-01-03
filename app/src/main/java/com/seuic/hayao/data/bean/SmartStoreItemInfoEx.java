package com.seuic.hayao.data.bean;

public class SmartStoreItemInfoEx {

    private double UploadAmount;
    private String PackageCascade;
    private String PackageName;
    private double InputAmount;
    private String InputProductUnit;
    private int InputRate;

    public double getUploadAmount() {
        return UploadAmount;
    }

    public void setUploadAmount(double uploadAmount) {
        UploadAmount = uploadAmount;
    }

    public String getPackageCascade() {
        return PackageCascade;
    }

    public void setPackageCascade(String packageCascade) {
        PackageCascade = packageCascade;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public double getInputAmount() {
        return InputAmount;
    }

    public void setInputAmount(double inputAmount) {
        InputAmount = inputAmount;
    }

    public String getInputProductUnit() {
        return InputProductUnit;
    }

    public void setInputProductUnit(String inputProductUnit) {
        InputProductUnit = inputProductUnit;
    }

    public int getInputRate() {
        return InputRate;
    }

    public void setInputRate(int inputRate) {
        InputRate = inputRate;
    }
}
