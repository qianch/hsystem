package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface ISerialService extends IBaseService {
    String getSerialNumber(final String p0, final int p1);
}
