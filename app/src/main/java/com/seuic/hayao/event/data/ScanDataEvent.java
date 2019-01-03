package com.seuic.hayao.event.data;

public class ScanDataEvent {
    private String barCode;

    public ScanDataEvent() {
    }

    public ScanDataEvent(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
