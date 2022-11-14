/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.baocai.service.impl;


import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.cut.baocai.dao.IBaoCaiDao;
import com.bluebirdme.mes.cut.baocai.entity.BaoCaiKu;
import com.bluebirdme.mes.cut.baocai.service.IBaoCaiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
@Service
@AnyExceptionRollback
public class BaoCaiServiceImpl extends BaseServiceImpl implements IBaoCaiService {
    @Resource
    IBaoCaiDao baoCaiDao;

    @Override
    protected IBaseDao getBaseDao() {
        return baoCaiDao;
    }

    @Override
    public List<Map<String, Object>> findPackingDetailByPackingID(Long mainId) {
        return baoCaiDao.findPackingDetailByPackingID(mainId);
    }

    @Override
    public String saveBaoCaiKu(BaoCaiKu baoCaiKu, String userName) throws Exception {
        return "";
    }
}
