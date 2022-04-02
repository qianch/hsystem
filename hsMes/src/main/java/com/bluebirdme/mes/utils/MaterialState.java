package com.bluebirdme.mes.utils;

/**
 * 物料状态
 * @author Goofy
 * @Date 2016年10月31日 下午1:17:14
 */
public interface MaterialState {

	/**
	 * 待检
	 */
	public final static int AWAITING_INSPECTION=0;
	
	
	/**
	 * 合格
	 */
	public final static int VALID=1;
	
	/**
	 * 不合格
	 */
	public final static int INVALID=2;
	
	/**
	 * 冻结
	 */
	public final static int FROZEN=3;
	
	/**
	 * 放行
	 */
	public final static int RELEASED=4;
	
	/**
	 * 退货
	 */
	public final static int RETURN=5;
}
