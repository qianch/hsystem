/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.bluebirdme.mes.baseInfo.dao.IBcBomDao;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.BcBomVersionDetail;
import com.bluebirdme.mes.baseInfo.service.IBcBomService;
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
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-10-8 16:53:24
 */
@Service
@AnyExceptionRollback
public class BcBomServiceImpl extends BaseServiceImpl implements IBcBomService {
    private static final Logger logger = LoggerFactory.getLogger(BcBomServiceImpl.class);
    @Resource
    IBcBomDao bcBomDao;

    @Override
    protected IBaseDao getBaseDao() {
        return bcBomDao;
    }

    /**
     * getBcBomJson 方法的简述.
     * 获取包材bom的json格式数据，用于组装treeview<br>
     *
     * @param data
     * @return 类型:List<Map<String, Object>>，返回组装完成的包材bom的json数据
     */
    public List<Map<String, Object>> getBcBomJson(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = bcBomDao.getBcBomJson(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        List<Consumer> consumers = findAll(Consumer.class);
        HashMap<Long, Consumer> consumerMap = new HashMap<>();
        for (Consumer consume : consumers) {
            consumerMap.put(consume.getId(), consume);
        }
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "packBomGenericName".toUpperCase()) + "/" + MapUtils.getAsString(map, "packBomCode".toUpperCase()));
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            Consumer consumer = consumerMap.get(MapUtils.getAsLong(map, "packBomConsumerId".toUpperCase()));
            if (consumer != null) {
                map.put("comsumerName", consumer.getConsumerName());
            } else {
                map.put("comsumerName", "该客户不存在");
            }
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return bcBomDao.findPageInfo(filter, page);
    }

    @Override
    /**
     * delete 方法的简述.
     * 根据传入的包材bom的id删除对应的包材bom和明细<br>
     * @param ids 类型:String，多个id用‘,’号分割
     * @return 无
     */
    public void deleteAll(String ids) throws Exception {
        String[] id = ids.split(",");
        for (String s : id) {
            BcBom bc = findById(BcBom.class, Long.valueOf(s));
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("packBomId", bc.getId());
            List<BCBomVersion> versionList = findListByMap(BCBomVersion.class, map);
            for (BCBomVersion version : versionList) {
                map.clear();
                map.put("packVersionId", version.getId());
                List<BcBomVersionDetail> detailList = findListByMap(BcBomVersionDetail.class, map);
                delete(detailList.toArray(new BcBomVersionDetail[]{}));
            }
            delete(versionList.toArray(new BCBomVersion[]{}));
            delete(bc);
        }
    }

    @Override
    public List<Map<String, Object>> getBcBomJsonTest(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = bcBomDao.getBcBomJsonTest(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "packBomGenericName".toUpperCase()) + "/" + MapUtils.getAsString(map, "packBomCode".toUpperCase()));
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            Consumer comsumer = findById(Consumer.class, MapUtils.getAsLong(map, "packBomConsumerId".toUpperCase()));
            if (comsumer != null) {
                map.put("comsumerName", comsumer.getConsumerName());
            } else {
                map.put("comsumerName", "该客户不存在");
            }
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }

    @Override
    public List<Map<String, Object>> getBcBomJsonTest1(String data) {
        List<Map<String, Object>> listMap;
        try {
            listMap = bcBomDao.getBcBomJsonTest1(data);
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Map<String, Object> ret;
        List<Map<String, Object>> _ret = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsLong(map, "ID"));
            ret.put("text", MapUtils.getAsString(map, "packBomGenericName".toUpperCase()) + "/" + MapUtils.getAsString(map, "packBomCode".toUpperCase()));
            ret.put("state", "closed");
            map.put("nodeType", "bom");
            Consumer comsumer = findById(Consumer.class, MapUtils.getAsLong(map, "packBomConsumerId".toUpperCase()));
            if (comsumer != null) {
                map.put("comsumerName", comsumer.getConsumerName());
            } else {
                map.put("comsumerName", "该客户不存在");
            }
            ret.put("attributes", map);
            _ret.add(ret);
        }
        return _ret;
    }
}
