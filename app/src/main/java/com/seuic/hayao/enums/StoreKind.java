package com.seuic.hayao.enums;

public enum StoreKind {

    In("入库"),
    Out("出库");

    private String description;

    private StoreKind(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
