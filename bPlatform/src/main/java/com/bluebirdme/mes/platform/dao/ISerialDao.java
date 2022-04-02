package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.platform.entity.Serial;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface ISerialDao extends IBaseDao {
    Serial getSerial(final String p0);
}
