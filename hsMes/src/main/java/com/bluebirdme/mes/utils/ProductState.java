package com.bluebirdme.mes.utils;

/**
 * 成品状态
 * 合格、不合格、冻结、退货、超产
 * @author Goofy
 * @Date 2016年10月31日 下午1:21:31
 */
public interface ProductState {

	/**
	 * 合格
	 */
	public final static int VALID = 0;

	/**
	 * 不合格
	 */
	public final static int INVALID = -1;

	/**
	 * 冻结
	 */
	public final static int FROZEN = 3;

	/**
	 * 退货
	 */
	public final static int RETURN = 5;
	
	/**
	 * 超产
	 */
	public final static int OVER_PRODUCE=6;
}
