package com.bluebirdme.mes.store.entity;

/**
 * 条码操作
 *
 * @author Goofy
 * @Date 2017年3月20日 下午7:59:24
 */
public interface BarCodeOperateAction {

    /**
     * 打包
     */
    int PKG = 1;

    /**
     * 废弃
     */
    int ABANDON = 2;

    /**
     * 登记
     */
    int REG = 3;

    /**
     * 拆包
     */
    int OPEN = 4;

    /**
     * 翻包
     */
    int TURNBAG = 5;

    /**
     * 出库
     */
    int OUT = 6;

    /**
     * 入库
     */
    int IN = 7;

    /**
     * 冻结
     */
    int FROZEN = 8;

    /**
     * 解冻
     */
    int UNFROZEN = 9;

    /**
     * 判级
     */
    int GRADE = 10;

}
