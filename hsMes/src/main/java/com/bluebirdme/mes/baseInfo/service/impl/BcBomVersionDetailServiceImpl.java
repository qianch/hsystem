/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.bluebirdme.mes.baseInfo.dao.IBcBomVersionDetailDao;
import com.bluebirdme.mes.baseInfo.service.IBcBomVersionDetailService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-10-8 16:53:24
 */
@Service
@AnyExceptionRollback
public class BcBomVersionDetailServiceImpl extends BaseServiceImpl implements IBcBomVersionDetailService {

    @Resource
    IBcBomVersionDetailDao bcBomVersionDetailDao;

    @Override
    protected IBaseDao getBaseDao() {
        return bcBomVersionDetailDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return bcBomVersionDetailDao.findPageInfo(filter, page);
    }

    @Override
    /**
     * delete 方法的简述.
     * 根据传入的包材bom明细的id删除对应的包材bom明细<br>
     * @param ids 类型:String，多个id用‘,’号分割
     * @return 无
     */
    public void deleteAll(String ids) throws Exception {
        bcBomVersionDetailDao.delete(ids);
    }
}
