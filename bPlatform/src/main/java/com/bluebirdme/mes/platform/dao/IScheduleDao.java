package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.platform.entity.Schedule;

import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface IScheduleDao extends IBaseDao {
    Schedule getScheduleByClazz(final String p0);

    List<Schedule> findUncompleteSchedule();
}
