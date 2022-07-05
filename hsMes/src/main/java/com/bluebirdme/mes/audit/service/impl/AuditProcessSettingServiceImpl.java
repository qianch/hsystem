/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.audit.service.IAuditProcessSettingService;
import com.bluebirdme.mes.audit.dao.IAuditProcessSettingDao;
import com.bluebirdme.mes.audit.entity.AuditProcessSetting;
import com.bluebirdme.mes.audit.entity.AuditUsers;

/**
 * @author 高飞
 * @Date 2016-10-24 14:51:44
 */
@Service
@AnyExceptionRollback
public class AuditProcessSettingServiceImpl extends BaseServiceImpl implements IAuditProcessSettingService {
    @Resource
    IAuditProcessSettingDao auditProcessSettingDao;

    @Override
    protected IBaseDao getBaseDao() {
        return auditProcessSettingDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return auditProcessSettingDao.findPageInfo(filter, page);
    }

    @Override
    public void saveAuditSetting(AuditProcessSetting setting) {
        super.update(setting);
        Map<String, Object> param = new HashMap<>();
        param.put("auditCode", setting.getAuditCode());

        List<AuditUsers> aus = new ArrayList<>();
        AuditUsers au;
        if (setting.getFirstLevelUsers() != null && setting.getFirstLevelUsers().length != 0) {
            param.put("auditLevel", 1);
            this.delete(AuditUsers.class, param);
            for (Long uid : setting.getFirstLevelUsers()) {
                au = new AuditUsers();
                au.setAuditCode(setting.getAuditCode());
                au.setAuditLevel(1);
                au.setUserId(uid);
                aus.add(au);
            }
            auditProcessSettingDao.save(aus.toArray(new AuditUsers[]{}));
        }

        if (setting.getAuditLevel() == 2) {
            if (setting.getSecondLevelUsers() != null && setting.getSecondLevelUsers().length != 0) {
                aus.clear();
                param.put("auditLevel", 2);
                this.delete(AuditUsers.class, param);
                for (Long uid : setting.getSecondLevelUsers()) {
                    au = new AuditUsers();
                    au.setAuditCode(setting.getAuditCode());
                    au.setAuditLevel(2);
                    au.setUserId(uid);
                    aus.add(au);
                }
                auditProcessSettingDao.save(aus.toArray(new AuditUsers[]{}));
            }
        }
    }
}
