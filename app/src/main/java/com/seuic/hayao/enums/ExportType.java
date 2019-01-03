package com.seuic.hayao.enums;

public enum ExportType {

    /*
        生产入库：ProduceWareHouseIn
        采购入库：PurchaseWareHouseIn
        退货入库：ReturnWareHouseIn
        调拨入库：AllocateWareHouseIn
        销售出库：SalesWareHouseOut
        退货出库：ReturnWareHouseOut
        调拨出库：AllocateWareHouseOut
        返工出库：ReworkWareHouseOut
        销毁出库：DestoryWareHouseOut
        抽检出库: CheckWareHouseOut
        直调出库: DirectAllocateWareHouseOut
     */

    ProduceWareHouseIn("生产入库"),

    PurchaseWareHouseIn("采购入库"),

    ReturnWareHouseIn("退货入库"),

    AllocateWareHouseIn("调拨入库"),

    SalesWareHouseOut("销售出库"),

    ReturnWareHouseOut("退货出库"),

    AllocateWareHouseOut("调拨出库"),

    ReworkWareHouseOut("返工出库"),

    DestoryWareHouseOut("销毁出库"),

    CheckWareHouseOut("抽检出库"),

    DirectAllocateWareHouseOut("直调出库");


    private String description;

    private ExportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
