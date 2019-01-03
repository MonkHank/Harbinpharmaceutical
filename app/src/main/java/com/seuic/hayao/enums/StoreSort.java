package com.seuic.hayao.enums;

public enum StoreSort {

    ProduceIn("生产入库"),
    ReworkOut("返工出库"),
    PurchaseIn("采购入库"),
    ReturnOut("退货出库"),
    SaleOut("销售出库"),
    ReturnIn("退货入库"),
    SaleOutX("零售出库"),
    ReturnInX("零售退货"),
    AllocateIn("调拨入库"),
    AllocateOut("调拨出库"),
    CheckIn("盘点盈余"),
    CheckOut("盘点亏损"),
    DestroyOut("销毁出库"),
    TestingOut("抽检出库"),
    MissingOut("报失出库"),
    OtherIn("直调入库"),
    OtherOut("直调出库");

    private String description;

    private StoreSort(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
