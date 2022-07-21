package com.bluebirdme.mes.mobile.stock.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 类注释
 *
 * @author Goofy
 * @Date 2017年2月11日 下午3:49:43
 */
public interface IMobileTurnBagService extends IBaseService {
    void turnbag(Long tid, Long[] dids, String[] codes) throws Exception;
}
