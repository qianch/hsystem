/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.service.impl;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.siemens.bom.entity.Drawings;
import com.bluebirdme.mes.siemens.bom.entity.Suit;
import com.bluebirdme.mes.siemens.bom.service.IDrawingsService;
import com.bluebirdme.mes.siemens.order.dao.ICutTaskDao;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskSuit;
import com.bluebirdme.mes.siemens.order.service.ICutTaskService;
import com.bluebirdme.mes.utils.MathTool;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.ListUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-7-26 10:56:16
 */
@Service
@AnyExceptionRollback
public class CutTaskServiceImpl extends BaseServiceImpl implements ICutTaskService {
    @Resource
    ICutTaskDao cutTaskDao;

    @Resource
    IDrawingsService drawingsService;

    @Override
    protected IBaseDao getBaseDao() {
        return cutTaskDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return cutTaskDao.findPageInfo(filter, page);
    }

    public String getSerial() {
        return cutTaskDao.getSerial();
    }

    @Override
    public void save(CutTask task, Long pcId) throws Exception {
        TcBomVersionParts part = findById(TcBomVersionParts.class, task.getPartId());
        if (part.getDrawingsComplete() == null || part.getDrawingsComplete() == 0) {
            throw new Exception("该部件的图纸BOM尚不可用，请联系维护图纸BOM工艺");
        }
        if (part.getSuitComplete() == null || part.getSuitComplete() == 0) {
            throw new Exception("该部件的组套BOM尚不可用，请联系维护组套BOM工艺");
        }
        ProducePlanDetailPartCount pc = findById(ProducePlanDetailPartCount.class, pcId);
        if (pc.getCreateCutTask() != null && pc.getCreateCutTask() == 1) {
            throw new Exception("已创建任务单，无法再次创建！");
        }

        List<Drawings> dwList = find(Drawings.class, "partId", task.getPartId());
        if (ListUtils.isEmpty(dwList)) {
            throw new RuntimeException("该部件尚未设置图纸BOM!");
        }

        List<Suit> suitList = find(Suit.class, "partId", task.getPartId());

        if (ListUtils.isEmpty(suitList)) {
            throw new RuntimeException("该部件尚未设置组套BOM!");
        }

        int lcm = getLCM(task.getPartId());

        if (task.getSuitCount() % lcm != 0) {
            throw new Exception("套数不合理，应该为" + lcm + "的整数倍");
        }
        task.setTaskCode(getSerial());
        save(task);
        List<CutTaskDrawings> ctdList = new ArrayList<>();
        CutTaskDrawings ctd;
        for (Drawings dw : dwList) {
            ctd = dw.convert(CutTaskDrawings.class);
            ctd.setId(null);
            ctd.setCtId(task.getId());
            ctd.setDwId(dw.getId());
            ctd.setIsDeleted(0);
            ctd.setNeedToPrintCount(dw.getFragmentCountPerDrawings() * (task.getSuitCount() / ctd.getSuitCountPerDrawings()));
            ctd.setExtraPrintCount(0);
            ctd.setPrintedCount(0);
            ctd.setRePrintCount(0);
            ctd.setTaskOrderCount(0);
            ctd.setFragmentCode(dw.getFragmentCode());
            ctd.setFragmentCountPerDrawings(dw.getFragmentCountPerDrawings());
            ctd.setFragmentDrawingNo(dw.getFragmentDrawingNo());
            ctd.setFragmentDrawingVer(dw.getFragmentDrawingVer());
            ctd.setFragmentId(dw.getFragmentId());
            ctd.setFragmentLength(dw.getFragmentLength());
            ctd.setFragmentMemo(dw.getFragmentMemo());
            ctd.setFragmentName(dw.getFragmentName());
            ctd.setFragmentWeight(dw.getFragmentWeight());
            ctd.setFragmentWidth(dw.getFragmentWidth());
            ctdList.add(ctd);
        }
        //保存BOM
        saveList(ctdList);
        List<CutTaskSuit> ctsList = new ArrayList<>();
        CutTaskSuit cts;
        for (Suit suit : suitList) {
            cts = suit.convert(CutTaskSuit.class);
            cts.setId(null);
            cts.setCtId(task.getId());
            ctsList.add(cts);
        }
        saveList(ctsList);
        pc.setCreateCutTask(1);
        update(pc);
    }

    public int getLCM(Long partId) {
        return MathTool.LCM(drawingsService.getSuitCountPerDrawings(partId));
    }

    public void close(Integer closed, String id) throws Exception {
        //关闭
        if (closed == 1) {
            List<CutTaskOrder> list = find(CutTaskOrder.class, "ctId", Long.parseLong(id));
            for (CutTaskOrder cto : list) {
                if (cto.getIsClosed() == 0 && cto.getIsComplete() == 0) {
                    throw new Exception("派工单未关闭且未完成，无法关闭该任务单");
                }
            }
        }
        CutTask ct = findById(CutTask.class, Long.parseLong(id));
        ct.setIsClosed(closed);
        update(ct);
    }

    public int[] getSuitCountPerDrawings(Long partId, Long ctId) {
        return cutTaskDao.getSuitCountPerDrawings(partId, ctId);
    }
}
