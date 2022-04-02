package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface INoticeDao extends IBaseDao {
    void delete(final String p0);

    List<Map<String, Object>> findNotice();
}
