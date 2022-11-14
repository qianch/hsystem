/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionPartsFinishedWeightEmbryoClothDao;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionPartsFinishedWeightEmbryoClothService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 徐秦冬
 * @Date 2017-11-27 18:57:36
 */
@Service
@AnyExceptionRollback
public class TcBomVersionPartsFinishedWeightEmbryoClothServiceImpl extends BaseServiceImpl implements ITcBomVersionPartsFinishedWeightEmbryoClothService {

    @Resource
    ITcBomVersionPartsFinishedWeightEmbryoClothDao tcBomVersionPartsFinishedWeightEmbryoClothDao;

    @Override
    protected IBaseDao getBaseDao() {
        return tcBomVersionPartsFinishedWeightEmbryoClothDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return tcBomVersionPartsFinishedWeightEmbryoClothDao.findPageInfo(filter, page);
    }

    public void delete(String ids) {
        tcBomVersionPartsFinishedWeightEmbryoClothDao.delete(ids);
    }

    @Override
    public Map<String, Object> findPageInfo1(Filter filter, Page page) {
        return tcBomVersionPartsFinishedWeightEmbryoClothDao.findPageInfo1(filter, page);
    }

}
