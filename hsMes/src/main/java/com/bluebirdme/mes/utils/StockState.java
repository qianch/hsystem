package com.bluebirdme.mes.utils;

/**
 * 库存状态
 * 在库，不在库
 * @author Goofy
 * @Date 2016年10月31日 下午1:14:20
 */
public interface StockState {
	/**
	 * 待入库
	 */
	public final static int STOCKPENDING=2;
	/**
	 * 入库
	 */
	public final static int IN=1;

	/**
	 * 出库
	 */
	public final static int OUT=-1;

	/**
	 * 领料
	 */
	public final static int Pick=4;

	/**
	 * 退回车间
	 */
	public final static int back=5;


	/**
	 * 在途库
	 */
	public final static int OnTheWay=3;
}
