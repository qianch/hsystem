/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.cut.dao.ICutDailyPlanDetailDao;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlan;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlanDetail;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlanUsers;
import com.bluebirdme.mes.planner.cut.service.ICutDailyPlanDetailService;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-28 10:32:39
 */
@Service
@AnyExceptionRollback
public class CutDailyPlanDetailServiceImpl extends BaseServiceImpl implements ICutDailyPlanDetailService {
    @Resource
    ICutDailyPlanDetailDao cutDailyPlanDetailDao;

    @Override
    protected IBaseDao getBaseDao() {
        return cutDailyPlanDetailDao;
    }


    @Override
    public Map<String, Object> findNofinish(Filter filter, Page page) {
        return cutDailyPlanDetailDao.findNofinish(filter, page);
    }

    @Override
    public void saveD(CutDailyPlan cutDailyPlan, Long cids[], Integer counts[], String partsNames[], String pCounts[], String partNames[], String comments[], String uids[], String partids[]) throws Exception {
        if (cutDailyPlan.getId() == null) {
            super.save(cutDailyPlan);
        } else {
            super.update2(cutDailyPlan);
            deletePlanDetails(cutDailyPlan.getId());
        }


        CutDailyPlanDetail daily;
        CutDailyPlanUsers dailyUser;

        for (int i = 0; i < cids.length; i++) {
            daily = new CutDailyPlanDetail();

            daily.setCount(counts[i]);
            daily.setCutPlanDailyId(cutDailyPlan.getId());
            daily.setCutPlanId(cids[i]);
            daily.setUserAndCount(partsNames[i]);
            if (comments.length > i) {
                daily.setComment(comments[i]);
            }
            daily.setUserIds(uids[i]);
            daily.setCounts(pCounts[i]);
            daily.setPartNames(partNames[i]);
            daily.setPartids(partids[i]);
            super.save(daily);

            String userIds[] = uids[i].split("##");//23##23
            String _partNames[] = partNames[i].split("##");
            String _partids[] = partids[i].split("##");
            String _pCounts[] = pCounts[i].split("##");

            System.out.println(GsonTools.toJson(userIds));
            for (int j = 0; j < userIds.length; j++) {
                if (userIds[j].equals("undefined")) {
                    continue;
                }

                String uid[] = userIds[j].split("，");

                //String _parts[]=_partNames[j].split("##");
                String _counts[] = _pCounts[j].split("，");

                for (int k = 0; k < uid.length; k++) {
                    if (uid[k].equals("undefined")) {
                        continue;
                    }
                    dailyUser = new CutDailyPlanUsers();

                    dailyUser.setCutPlanDailyPlanId(cutDailyPlan.getId());
                    dailyUser.setCutPlanId(daily.getId());
                    dailyUser.setUserId(Long.parseLong(uid[k]));
                    dailyUser.setCount(Integer.parseInt(_counts[k]));
                    dailyUser.setPartName(_partNames[j]);
                    if (_partids[j].equals("undefined")) continue;
                    dailyUser.setPartId(Long.parseLong(_partids[j]));

                    super.save(dailyUser);
                }
            }
        }

    }

    @Override
    public List<Map<String, Object>> findC(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        return cutDailyPlanDetailDao.findC(ids);
    }

    public void deletePlanDetails(Long id) {
        Map<String, Object> condition = new HashMap<>();

        condition.put("cutPlanDailyPlanId", id);
        cutDailyPlanDetailDao.delete(CutDailyPlanUsers.class, condition);

        condition.clear();
        condition.put("cutPlanDailyId", id);
        cutDailyPlanDetailDao.delete(CutDailyPlanDetail.class, condition);
    }

    @Override
    public void deletePlans(String ids) throws Exception {
        Map<String, Object> condition = new HashMap<>();

        condition.put("cutPlanDailyPlanId", Long.parseLong(ids));
        cutDailyPlanDetailDao.delete(CutDailyPlanUsers.class, condition);

        condition.clear();
        condition.put("cutPlanDailyId", Long.parseLong(ids));
        cutDailyPlanDetailDao.delete(CutDailyPlanDetail.class, condition);

        cutDailyPlanDetailDao.delete(CutDailyPlan.class, ids);
    }

    public List<Map<String, Object>> findPlanUsers(Long cutPlanDetailId) {
        return cutDailyPlanDetailDao.findPlanUsers(cutPlanDetailId);
    }

    @Override
    public List<Map<String, Object>> findWorkShop(String cutPlanId) {
        return cutDailyPlanDetailDao.findWorkShop(cutPlanId);
    }

}
