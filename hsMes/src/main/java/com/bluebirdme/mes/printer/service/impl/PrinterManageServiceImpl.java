package com.bluebirdme.mes.printer.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.printer.dao.IPrinterManageDao;
import com.bluebirdme.mes.printer.service.IPrinterManageSerivice;
import com.bluebirdme.mes.utils.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrinterManageServiceImpl extends BaseServiceImpl implements IPrinterManageSerivice {
    @Resource
    IPrinterManageDao printerManageDao;

    @Override
    protected IBaseDao getBaseDao() {
        return printerManageDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page)
            throws Exception {
        return printerManageDao.findPageInfo(filter, page);
    }

    @Override
    public List<Map<String, Object>> findALLDepartment() {
        List<Map<String, Object>> list = printerManageDao.findALLDepartment();
        Map<String, Object> ret;
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            ret = new HashMap<>();
            ret.put("id", MapUtils.getAsString(map, "ID"));
            ret.put("text", MapUtils.getAsStringIgnoreCase(map, "DEPARTMENT"));
            result.add(ret);
        }
        return result;
    }
}
