package com.bluebirdme.mes.tracings.entity;

public class TracingType {
    //	 public static final int sale=1;//：销售
//	 public static final int plan=2;//：：计调
//	 public static final int dailyplan=3;//：：排产
    public static final int produce = 4;//：：产出
    public static final int packing = 5;//：：打包
    public static final int instock = 6;//：：入库
    public static final int outstock = 7;//：：出库
    //	 public static final int repacking=8;//：：翻包
    public static final int lock = 9;//：：冻结
    public static final int unlock = 10;//：：解冻
    public static final int feeding = 11;//：：投料
    //	 public static final int tuiku=12;//：：退库
//	 public static final int stockFabricMove=13;//胚布领料
    public static final int fabricFeed = 14;//胚布投料
    public static final int turnbag = 15;//翻包
}
