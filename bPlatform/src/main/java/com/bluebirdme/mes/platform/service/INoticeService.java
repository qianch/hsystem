package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.platform.entity.Notice;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface INoticeService extends IBaseService {
    void save(final Notice p0) throws InterruptedException;

    void delete(final String p0);

    List<Map<String, Object>> findNotice();
}
