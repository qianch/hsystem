package com.bluebirdme.mes.store.entity;

/**
 * 条码操作
 * @author Goofy
 * @Date 2017年3月20日 下午7:59:24
 */
public interface BarCodeOperateAction {
	
	/**
	 * 打包
	 */
	public final int PKG=1;
	
	/**
	 * 废弃
	 */
	public final int ABANDON=2;
	
	/**
	 * 登记
	 */
	public final int REG=3;
	
	/**
	 * 拆包
	 */
	public final int OPEN=4;
	
	/**
	 * 翻包
	 */
	public final int TURNBAG=5;
	
	/**
	 * 出库
	 */
	public final int OUT=6;
	
	/**
	 * 入库
	 */
	public final int IN=7;
	
	/**
	 * 冻结
	 */
	public final int FROZEN=8;
	
	/**
	 * 解冻
	 */
	public final int UNFROZEN=9;
	
	/**
	 * 判级
	 */
	public final int GRADE=10;
	
}
