/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.service.impl;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.btwManager.entity.BtwFilePrint;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.deliveryontheway.dao.IDeliveryOnTheWayPlanDao;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlan;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlanDetails;
import com.bluebirdme.mes.planner.produce.dao.IProducePlanDetailDao;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPrint;
import com.bluebirdme.mes.planner.produce.service.IProducePlanDetailService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.stock.entity.StockMove;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.dao.ITrayBarCodeDao;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
@Service
@AnyExceptionRollback
@Transactional
public class ProducePlanDetailServiceImpl extends BaseServiceImpl implements IProducePlanDetailService {

    @Resource
    IProducePlanDetailDao producePlanDetailDao;


    @Override
    protected IBaseDao getBaseDao() {
        return producePlanDetailDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {

        Map<String, Object> findPageInfo = producePlanDetailDao.findPageInfo(filter, page);
        return findPageInfo;
    }

    @Override
    public <T> Map<String, Object> findProducePlanDetail(Filter filter, Page page) throws Exception {

        Map<String, Object> findPageInfo = producePlanDetailDao.findProducePlanDetail(filter, page);
        return findPageInfo;
    }

    public List<Map<String, Object>> findProducePlanDetailPrints(Long ProducePlanDetailId) throws Exception {
        return producePlanDetailDao.findProducePlanDetailPrints(ProducePlanDetailId);
    }

    public List<Map<String, Object>> findPlanDetailPrintsBybtwFileId(Long ProducePlanDetailId, long btwFileId) throws Exception {
        return producePlanDetailDao.findPlanDetailPrintsBybtwFileId(ProducePlanDetailId, btwFileId);
    }

    @Override
    public String saveProducePlanDetailPrints(long producePlanDetailId, long btwFileId, String planDetailPrintsData) throws Exception {
        try {
            Gson gson = new Gson();
            List<ProducePlanDetailPrint> list = gson.fromJson(planDetailPrintsData, new TypeToken<List<ProducePlanDetailPrint>>() { }.getType());

            List<String> producePlanDetailPrints=new ArrayList<>();
            List<ProducePlanDetailPrint> listProducePlanDetailPrintadd = new ArrayList<ProducePlanDetailPrint>();
            for (ProducePlanDetailPrint entity : list) {
                if (producePlanDetailPrints.contains(entity.getPrintAttribute())) {
                    continue;
                }
                if (entity.getPrintAttribute().equals("xuhao")){
                    String regex="\\d\\d-\\d\\d\\d";
                    boolean matches = entity.getPrintAttributeContent().matches(regex);
                    if(!matches){
                        throw new Exception("序号的打印内容不符合规则，正确示例为：01-001；");
                    }
                }
                producePlanDetailPrints.add(entity.getPrintAttribute());
                listProducePlanDetailPrintadd.add(entity);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("btwFileId", btwFileId);
            map.put("producePlanDetailId", producePlanDetailId);
            delete(ProducePlanDetailPrint.class, map);

            if (listProducePlanDetailPrintadd != null && listProducePlanDetailPrintadd.size() > 0) {
                producePlanDetailDao.save(listProducePlanDetailPrintadd.toArray(new ProducePlanDetailPrint[]{}));
            }
        } catch (Exception ex) {
            return "fail:" + ex.getMessage();
        }
        return "";
    }


    @Override
    public  String createProducePlanDetailPrints(Long producePlanDetailId, Long btwFileId) throws Exception {


        if (producePlanDetailId == 0 || btwFileId == 0) {
            return "fail";
        }

        synchronized (buildLock(producePlanDetailId+"_"+btwFileId)) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("btwFileId", btwFileId);
            map.put("producePlanDetailId", producePlanDetailId);
            Map<String, ProducePlanDetailPrint> detailMap = new HashMap<>();
            findListByMap(ProducePlanDetailPrint.class, map).forEach(p -> detailMap.put(p.getPrintAttribute(), p));

            map.clear();
            map.put("btwFileId", btwFileId);
            List<BtwFilePrint> listBtwFilePrint = findListByMap(BtwFilePrint.class, map);

            List<ProducePlanDetailPrint> listProducePlanDetailPrintadd = new ArrayList<ProducePlanDetailPrint>();
            for (BtwFilePrint entity : listBtwFilePrint) {

                ProducePlanDetailPrint detailPrintOld = detailMap.get(entity.getPrintAttribute());
                ProducePlanDetailPrint detailPrint=new ProducePlanDetailPrint();
                detailPrint.setBtwFileId(entity.getBtwFileId());
                detailPrint.setProducePlanDetailId(producePlanDetailId);
                detailPrint.setPrintAttribute(entity.getPrintAttribute());
                detailPrint.setPrintAttributeName(entity.getPrintAttributeName());

                if (detailPrintOld != null) {
                    detailPrint.setPrintAttributeContent(detailPrintOld.getPrintAttributeContent());
                    listProducePlanDetailPrintadd.add(detailPrint);
                    continue;
                }
                listProducePlanDetailPrintadd.add(detailPrint);
            }

            map.clear();
            map.put("btwFileId", btwFileId);
            map.put("producePlanDetailId", producePlanDetailId);
            delete(ProducePlanDetailPrint.class, map);
            if (listProducePlanDetailPrintadd != null && listProducePlanDetailPrintadd.size() > 0) {
                producePlanDetailDao.saveList(listProducePlanDetailPrintadd);
            }
        }

        return "ok";
    }

    private static String buildLock(String str) {
        StringBuilder sb = new StringBuilder(str);
        String lock = sb.toString().intern();
        return lock;
    }


}
