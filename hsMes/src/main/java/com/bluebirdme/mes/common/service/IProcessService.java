package com.bluebirdme.mes.common.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年12月2日 下午9:07:01
 */
public interface IProcessService extends IBaseService {
    Map<String, Object> getLeafPart(String code, String ver);

    /**
     * 根据工艺代码和工艺版本获取bom的所有叶子节点部件以及其数量(List)
     *
     * @param code
     * @param ver
     * @return
     */
	List<Map<String, Object>> getLeafParts(String code, String ver);


    Map<String, Object> getProducedPartCount(Long ppdId);
}
