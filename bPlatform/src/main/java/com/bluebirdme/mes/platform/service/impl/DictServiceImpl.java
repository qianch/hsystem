package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.IDictDao;
import com.bluebirdme.mes.platform.entity.Dict;
import com.bluebirdme.mes.platform.service.IDictService;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Service
@Transactional
public class DictServiceImpl extends BaseServiceImpl implements IDictService {
    @Resource
    IDictDao dictDao;

    @Override
    protected IBaseDao getBaseDao() {
        return dictDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return dictDao.findPageInfo(filter, page);
    }

    @Override
    public List<Map<String, Object>> queryDict(String rootcode) throws SQLTemplateException {
        return dictDao.queryDict(rootcode);
    }

    @Override
    public List<Map<String, String>> queryDictAll() throws SQLTemplateException {
        return dictDao.queryDictAll();
    }

    @Override
    public void delete(final String ids) {
        dictDao.delete(ids);
    }

    @Override
    public List<Map<String, Object>> combotree(final String code, final String selectedCode) throws SQLTemplateException {
        final List<Map<String, Object>> listMap = this.dictDao.combotree(code, selectedCode);
        final List<Map<String, Object>> list = new ArrayList();
        final Map<String, Object> unit1 = new HashMap();
        boolean hasRoot = false;
        for (final Map<String, Object> map : listMap) {
            if (MapUtils.getAsLongIgnoreCase(map, "PID") == null) {
                hasRoot = true;
                break;
            }
        }
        for (final Map<String, Object> map : listMap) {
            if (hasRoot) {
                if (MapUtils.getAsLongIgnoreCase(map, "PID") != null) {
                    continue;
                }
                unit1.put("id", MapUtils.getAsLongIgnoreCase(map, "ID"));
                if (1 == MapUtils.getAsIntIgnoreCase(map, "DEPRECATED")) {
                    unit1.put("text", MapUtils.getAsStringIgnoreCase(map, "NAME_ZH_CN") + "[\u5f03\u7528]");
                } else {
                    unit1.put("text", MapUtils.getAsStringIgnoreCase(map, "NAME_ZH_CN"));
                }
                unit1.put("children", getChildren(listMap, unit1));
            } else {
                final Map<String, Object> unit2 = new HashMap();
                unit2.put("id", MapUtils.getAsLongIgnoreCase(map, "ID"));
                if (1 == MapUtils.getAsIntIgnoreCase(map, "DEPRECATED")) {
                    unit1.put("text", MapUtils.getAsStringIgnoreCase(map, "NAME_ZH_CN") + "[\u5f03\u7528]");
                } else {
                    unit2.put("text", MapUtils.getAsStringIgnoreCase(map, "NAME_ZH_CN"));
                }
                unit2.put("children", getChildren(listMap, unit2));
                list.add(unit2);
            }
        }
        if (!unit1.isEmpty()) {
            list.add(0, unit1);
        }
        final Map<String, Object> nullMap = new HashMap<String, Object>();
        nullMap.put("id", null);
        nullMap.put("text", "");
        nullMap.put("iconCls", "tree-blank");
        list.add(0, nullMap);
        return list;
    }

    private List<Map<String, Object>> getChildren(final List<Map<String, Object>> list, final Map<String, Object> parent) {
        final List<Map<String, Object>> ret = new ArrayList();
        for (final Map<String, Object> map : list) {
            if (MapUtils.getAsLongIgnoreCase(map, "PID") == null) {
                continue;
            }
            if (MapUtils.getAsLongIgnoreCase(map, "PID") != (long) MapUtils.getAsLongIgnoreCase(parent, "ID")) {
                continue;
            }
            Map<String, Object> child = new HashMap();
            child.put("id", MapUtils.getAsLongIgnoreCase(map, "ID"));
            if (1 == MapUtils.getAsIntIgnoreCase(map, "DEPRECATED")) {
                child.put("text", MapUtils.getAsStringIgnoreCase(map, "NAME_ZH_CN") + "[弃用]");
            } else {
                child.put("text", MapUtils.getAsStringIgnoreCase(map, "NAME_ZH_CN"));
            }
            child.put("children", this.getChildren(list, map));
            ret.add(child);
        }
        return ret;
    }

    @Override
    public List<Map<String, Object>> findByCode(final String code) {
        return dictDao.findByCode(code);
    }

    @Override
    public void update(final Dict dict) {
        dictDao.update(dict);
    }
}
