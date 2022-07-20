package com.bluebirdme.mes.mobile.produce.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.store.entity.Roll;

import java.util.List;
import java.util.Map;

public interface IMobileProduceService extends IBaseService {
    //保存卷的产出进度
    void updateRoll(Roll roll, Iplan w, ProducePlanDetail producePlanDetail) throws Exception;

    //保存部件的产出
    @Deprecated
    void updatePart(Roll roll);

    List<Map<String, Object>> querylist(String deviceCode);

    List<Map<String, Object>> querylist2(String deviceCode);

    List<Map<String, Object>> queryYlbh(String ylbh, String oldweaveId, String deviceCode);

    void deleteYlbh(String ylbh, String oldweaveId, String deviceCode);


    void editInfo(String weaveid, String userid, String tlids);
}
