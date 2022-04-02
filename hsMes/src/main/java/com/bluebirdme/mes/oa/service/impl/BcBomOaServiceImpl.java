package com.bluebirdme.mes.oa.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.oa.dao.IBcBomOaDao;
import com.bluebirdme.mes.oa.service.IBcBomOaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class BcBomOaServiceImpl extends BaseServiceImpl implements IBcBomOaService {
 @Resource
 public IBcBomOaDao bcBomOaDao;
 @Override
 protected IBaseDao getBaseDao() {
  return bcBomOaDao;
 }

 @Override
 public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
  return bcBomOaDao.findPageInfo(filter,page);
 }
}
