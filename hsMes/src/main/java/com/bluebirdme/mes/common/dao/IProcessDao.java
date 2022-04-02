package com.bluebirdme.mes.common.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

/**
 * 
 * @author Goofy
 * @Date 2016年12月2日 下午9:10:26
 */
public interface IProcessDao extends IBaseDao {
	
	public List<Map<String,Object>> getLeafPart(String code,String ver);
	public List<Map<String,Object>> getProducedPartCount(Long producePlanId);

}
