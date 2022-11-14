/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.service;

import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.audit.entity.AuditProcessSetting;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.Map;

/**
 * @author 高飞
 * @Date 2016-10-25 13:52:50
 */
public interface IAuditInstanceService extends IBaseService {
    /**
     * 提交审核
     *
     * @param auditTitle   审核标题
     * @param auditCode    流程代码
     * @param submitUserId 提审人ID，当前登录用户
     * @param formUrl      表单url地址
     * @param formId       表单对应实体类ID
     * @param clazz        表单对应的实体类
     */
    <T> void submitAudit(String auditTitle, String auditCode, Long submitUserId, String formUrl, Long formId, Class<T> clazz) throws Exception;

    /**
     * 我的待审核任务
     *
     * @param filter
     * @param page
     * @return
     */
    Map<String, Object> auditTask(Filter filter, Page page);

    /**
     * 我提交的审核任务
     *
     * @param filter
     * @param page
     * @return
     */
    Map<String, Object> myAuditTask(Filter filter, Page page);

    /**
     * 已审核任务
     *
     * @param filter
     * @param page
     * @return
     */
    Map<String, Object> finishedAuditTask(Filter filter, Page page);

    void audit(AuditInstance audit, AuditProcessSetting aps, Long uid, Integer level, String msg, Integer result) throws Exception;

    <T> void updateByCondition(Class<T> clazz, Map<String, Object> condition, Map<String, Object> values);

    /**
     * type:1=销售订单，2=producePlan，3=weaveDailyPlan，4=cutDailyPlan
     *
     * @param id
     * @param type
     * @throws Exception
     */
    void reloadAudit(Long id, Integer type) throws Exception;
}
