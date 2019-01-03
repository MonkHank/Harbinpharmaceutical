package com.seuic.hayao.enums;

public enum CorpType {

    Operation("运营中心"),
    Supervise("管理部门"),
    ProduceCorp("生产企业"),
    SalesCorp("流通企业"),
    PrintCorp("印刷企业");

    private String description;

    private CorpType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
