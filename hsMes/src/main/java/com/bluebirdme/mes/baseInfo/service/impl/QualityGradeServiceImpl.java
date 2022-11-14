/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.bluebirdme.mes.baseInfo.dao.IQualityGradeDao;
import com.bluebirdme.mes.baseInfo.entity.QualityGrade;
import com.bluebirdme.mes.baseInfo.service.IQualityGradeService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016-10-12 10:34:41
 */
@Service
@AnyExceptionRollback
public class QualityGradeServiceImpl extends BaseServiceImpl implements IQualityGradeService {
    @Resource
    IQualityGradeDao qualityGradeDao;

    @Override
    protected IBaseDao getBaseDao() {
        return qualityGradeDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return qualityGradeDao.findPageInfo(filter, page);
    }

    @Override
    public List<HashMap<String, Object>> getQualitySelections() {
        List<QualityGrade> list = findAll(QualityGrade.class);
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (QualityGrade qg : list) {
            HashMap<String, Object> m = new HashMap<>();
            m.put("value", qg.getId());
            m.put("text", qg.getGradeName());
            result.add(m);
        }
        return result;
    }
}
