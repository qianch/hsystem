/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import com.bluebirdme.mes.siemens.order.dao.ICutTaskOrderDao;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.ListUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class CutTaskOrderDaoImpl extends BaseDaoImpl implements ICutTaskOrderDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "cutTaskOrder-list");
    }

    public String getSerial() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String preffix = "GD" + sdf.format(new Date());
        String hql = "From CutTaskOrder where ctoCode like '" + preffix + "%' order by id desc";
        CutTaskOrder cto = (CutTaskOrder) getSession().createQuery(hql).setMaxResults(1).uniqueResult();
        if (cto == null)
            return preffix + "001";
        StringBuilder sb = new StringBuilder("000" + (Integer.parseInt(cto.getCtoCode().replace(preffix, "")) + 1));
        return preffix + sb.substring(sb.length() - 3);
    }

    public List<CutTaskDrawings> getCutTaskDrawings(Long ctId) {
        String sql = SQL.get("cutTask-drawings-bom");
        return getSession().createQuery(sql).setParameter("ctId", ctId).list();
    }

    public void deleteTask(String id) throws Exception {
        CutTaskOrder cto = findById(CutTaskOrder.class, Long.parseLong(id));
        if (cto != null && cto.getPackedSuitCount() == 0) {
            delete(cto);
            CutTask ct = findById(CutTask.class, cto.getCtId());
            ct.setAssignSuitCount(ct.getAssignSuitCount() - cto.getAssignSuitCount());
            update(ct);
        } else {
            if (cto != null && cto.getPackedSuitCount() != 0) {
                throw new Exception("该派工单有产出，无法删除");
            }
        }
    }

    public void close(String id, Integer closed) throws Exception {
        CutTaskOrder cto = findById(CutTaskOrder.class, Long.parseLong(id));
        CutTask ct = findById(CutTask.class, cto.getCtId());
        if (closed == 1) {
            if (cto.getIsComplete() == 1) {
                throw new Exception("派工单已完成，无法关闭");
            }
        } else {
            if (ct.getIsClosed() == 1) {
                throw new Exception("任务单已关闭，无法启用次派工单");
            }
        }

        cto.setIsClosed(closed);
        update(cto);
    }

    public FragmentBarcode getLatestFragmentBarcode(String preffix) {
        return (FragmentBarcode) getSession().createQuery("From FragmentBarcode where barcode like '" + preffix + "%'  order by barcode desc").setMaxResults(1).uniqueResult();
    }

    public List<CutTaskOrderDrawings> getDrawings(String[] drawingsNo, Long ctoId) {
        String hql = "From CutTaskOrderDrawings where fragmentDrawingNo in (:drawingsNo) and ctoId=:ctoId order by printSort asc";
        return getSession().createQuery(hql).setParameterList("drawingsNo", drawingsNo).setParameter("ctoId", ctoId).list();
    }

    public void updatePrintedCount(Map<String, Integer> list, List<Long> dwIds, Long ctId, Long ctoId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("dwIds", ListUtils.toString(dwIds));
        map.put("map", list);

        map.put("tableName", "hs_siemens_cut_task_drawings");
        map.put("id", ctId);
        String sql = SQL.get(map, "cutTaskOrder-printedCount");

        System.out.println(sql);
        getSession().createSQLQuery(sql).executeUpdate();

        map.put("tableName", "hs_siemens_cut_task_order_drawings");
        map.put("id", ctoId);
        sql = SQL.get(map, "cutTaskOrder-printedCount");

        getSession().createSQLQuery(sql).executeUpdate();

    }

    public int getTotalPrintedCount(Long ctoId) {
        String sql = SQL.get("cutTaskOrder-totalPrintedCount");
        BigDecimal bd = (BigDecimal) getSession().createSQLQuery(sql).setParameter("ctoId", ctoId).uniqueResult();
        return bd.intValue();
    }

    public int[] getSuitCountPerDrawings(String[] drawingsNo, Long ctoId) {
        String sql = SQL.get("siemens-suitCoutPerDrawings-cutTaskOrder");
        List list = getSession().createSQLQuery(sql).setParameter("ctoId", ctoId).setParameterList("drawingsNo", drawingsNo).list();
        if (ListUtils.isEmpty(list)) {
            return new int[]{};
        }
        int[] nums = new int[list.size()];
        int i = 0;
        for (Object obj : list) {
            nums[i++] = (int) obj;
        }
        return nums;
    }

    @Override
    public String getDrawingNo(Long ctoId) {
        SQLQuery q = getSession().createSQLQuery("select GROUP_CONCAT(DISTINCT fragmentDrawingNo) from hs_siemens_cut_task_order_drawings where ctoId=:ctoId");
        q.setParameter("ctoId", ctoId);
        return (String) q.uniqueResult();
    }
}
