/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.bluebirdme.mes.baseInfo.dao.IFtcBcBomVersionDao;
import com.bluebirdme.mes.baseInfo.dao.IFtcBcBomVersionDetailDao;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomVersionService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.sales.entity.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐秦冬
 * @Date 2017-12-6 16:26:52
 */
@Service
@AnyExceptionRollback
public class FtcBcBomVersionServiceImpl extends BaseServiceImpl implements IFtcBcBomVersionService {
    private static final Logger logger = LoggerFactory.getLogger(FtcBcBomVersionServiceImpl.class);
    @Resource
    IFtcBcBomVersionDao ftcBcBomVersionDao;
    @Resource
    IFtcBcBomVersionDetailDao ftcBcBomVersionDetailDao;

    @Override
    protected IBaseDao getBaseDao() {
        return ftcBcBomVersionDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return ftcBcBomVersionDao.findPageInfo(filter, page);
    }

    // 根据id删除版本和对应版本下面的明细
    @Override
    /**
     * delete 方法的简述.
     * 根据传入的包材bom版本的id删除对应的包材bom版本和明细<br>
     * @param ids 类型:String，多个id用‘,’号分割
     * @return 无
     */
    public void deleteAll(String ids) throws Exception {
        ftcBcBomVersionDao.delete(ids);
        ftcBcBomVersionDetailDao.deleteByPid();
    }

    // 根据id获取bom版本的json
    /**
     * getBcBomJson 方法的简述. 根据传入的包材bom版本的id获得对应的json数据用于创建treeview节点<br>
     *
     * @param id 类型:String，单个id
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> getFtcBcBomJson(String id, String productType) {
        List<FtcBcBomVersion> listMap;
        try {
            listMap = ftcBcBomVersionDao.getFtcBcBomJson(id, productType);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (FtcBcBomVersion fbbv : listMap) {
            ret = new HashMap<>();
            ret.put("id", fbbv.getId());
            ret.put("text", fbbv.getVersion());
            Map<String, Object> map = new HashMap<>();
            map.put("nodeType", "version");
            map.put("version", fbbv.getVersion());
            map.put("bcTotalWeight", fbbv.getBcTotalWeight());
            map.put("productType", fbbv.getProductType());
            map.put("consumerId", fbbv.getConsumerId());
            map.put("rollDiameter", fbbv.getRollDiameter());
            map.put("palletLength", fbbv.getPalletLength());
            map.put("palletWidth", fbbv.getPalletWidth());
            map.put("rollsPerPallet", fbbv.getRollsPerPallet());
            map.put("palletHeight", fbbv.getPalletHeight());
            map.put("requirement_suliaomo", fbbv.getRequirement_suliaomo());
            map.put("requirement_baifang", fbbv.getRequirement_baifang());
            map.put("requirement_dabaodai", fbbv.getRequirement_dabaodai());
            map.put("requirement_biaoqian", fbbv.getRequirement_biaoqian());
            map.put("requirement_xiaobiaoqian", fbbv.getRequirement_xiaobiaoqian());
            map.put("requirement_juanbiaoqian", fbbv.getRequirement_juanbiaoqian());
            map.put("requirement_tuobiaoqian", fbbv.getRequirement_tuobiaoqian());
            map.put("requirement_chanrao", fbbv.getRequirement_chanrao());
            map.put("requirement_other", fbbv.getRequirement_other());
            map.put("AUDITSTATE", fbbv.getAuditState());
            map.put("enabled", fbbv.getEnabled());
            Consumer c = ftcBcBomVersionDao.findById(Consumer.class, fbbv.getConsumerId());
            map.put("consumerName", c.getConsumerName());
            FtcBcBom ftcBcBom = ftcBcBomVersionDao.findById(FtcBcBom.class, fbbv.getBid());
            map.put("packCode", ftcBcBom.getCode());
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }
}
