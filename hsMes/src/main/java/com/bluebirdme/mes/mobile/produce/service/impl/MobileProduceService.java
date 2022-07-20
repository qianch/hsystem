package com.bluebirdme.mes.mobile.produce.service.impl;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.device.entity.Device;
import com.bluebirdme.mes.device.service.IDeviceService;
import com.bluebirdme.mes.mobile.produce.dao.IFeedingRecordDao;
import com.bluebirdme.mes.mobile.produce.service.IMobileProduceService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.utils.ProductState;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AnyExceptionRollback
public class MobileProduceService extends BaseServiceImpl implements IMobileProduceService {
    @Resource
    IFeedingRecordDao feedingRecordDao;
    @Resource
    IDeviceService deviceService;

    @Override
    public <T> Map<String, Object> findPageInfo(Filter arg0, Page arg1) throws Exception {
        return null;
    }

    @Override
    protected IBaseDao getBaseDao() {
        return feedingRecordDao;
    }

    @Override
    public void updateRoll(Roll roll, Iplan w1, ProducePlanDetail producePlanDetail) {
        roll.setState(ProductState.VALID);
        if (w1 instanceof WeavePlan) {
            // 更新编织生产进度
            String bladeProfile = "";
            if (producePlanDetail.getPartId() != null) {
                SalesOrderDetail salesOrderDetail = feedingRecordDao.findById(SalesOrderDetail.class, producePlanDetail.getFromSalesOrderDetailId());
                bladeProfile = salesOrderDetail.getBladeProfile();
            } else if (producePlanDetail.getProductIsTc() == 1) {
                bladeProfile = producePlanDetail.getConsumerProductName();
            }

            WeavePlan w = feedingRecordDao.findById(WeavePlan.class, w1.getId());
            roll.getRollDeviceCode();

            Map<String, Object> map = new HashMap<>();
            map.put("deviceCode", roll.getRollDeviceCode());
            Device device = findUniqueByMap(Device.class, map);
            deviceService.setProducing(device.getId(), w.getId());
            roll.setRollOutputTime(new Date());
            // 保存卷信息
            feedingRecordDao.save(roll);
            TotalStatistics ts = new TotalStatistics();
            ts.setBarcodeType("roll");
            ts.setRollBarcode(roll.getRollBarcode());
            ts.setDeviceCode(roll.getRollDeviceCode());
            ts.setCONSUMERNAME(w.getConsumerName());
            ts.setSalesOrderCode(w.getSalesOrderCode());
            ts.setRollOutputTime(new Date());
            ts.setBatchCode(w.getBatchCode());
            ts.setProductModel(w.getProductModel());
            ts.setProducePlanCode(w.getPlanCode());
            FinishedProduct fp = feedingRecordDao.findById(FinishedProduct.class, w1.getProductId());
            ts.setProductWidth(fp.getProductWidth());
            ts.setProductLength(fp.getProductRollLength());
            ts.setProductWeight(null);
            ts.setRollWeight(roll.getRollWeight());
            ts.setProductName(fp.getFactoryProductName());
            ts.setRollQualityGradeCode(roll.getRollQualityGradeCode());
            User user = feedingRecordDao.findById(User.class, roll.getRollUserId());
            ts.setLoginName(user.getUserName());
            Department d = feedingRecordDao.findById(Department.class, user.getDid());
            ts.setName(d.getName());
            ts.setWorkShopCode(d.getCode());
            ts.setState(0);
            ts.setIsLocked(-1);
            ts.setIsPacked(0);
            ts.setBladeProfile(bladeProfile);
            // 保存生产信息
            feedingRecordDao.save(ts);
            feedingRecordDao.update(w);
        }
        if (w1 instanceof CutPlan) {
            CutPlan w = (CutPlan) w1;
            roll.setRollOutputTime(new Date());
            // 保存卷信息
            feedingRecordDao.save(roll);
            TotalStatistics ts = new TotalStatistics();
            ts.setBarcodeType("part");
            ts.setRollBarcode(roll.getPartBarcode());
            ts.setProductWeight(roll.getRollWeight());
            HashMap<String, Object> map = new HashMap<>();
            map.put("barcode", roll.getPartBarcode());
            PartBarcode pb = feedingRecordDao.findUniqueByMap(PartBarcode.class, map);
            if (pb != null) {
                TcBomVersionParts tvp = feedingRecordDao.findById(TcBomVersionParts.class, pb.getPartId());
                ts.setRollWeight(tvp.getTcProcBomVersionPartsWeight());
            }
            ts.setDeviceCode(roll.getRollDeviceCode());
            ts.setCONSUMERNAME(w.getConsumerName());
            ts.setSalesOrderCode(w.getSalesOrderCode());
            ts.setRollQualityGradeCode(roll.getRollQualityGradeCode());
            ts.setRollOutputTime(new Date());
            ts.setBatchCode(w.getBatchCode());
            ts.setProductModel(w.getProductModel());
            ts.setProducePlanCode(w.getPlanCode());
            User user = feedingRecordDao.findById(User.class, roll.getRollUserId());
            ts.setLoginName(user.getUserName());
            Department d = feedingRecordDao.findById(Department.class, user.getDid());
            ts.setName(d.getName());
            ts.setWorkShopCode(d.getCode());
            ts.setState(0);
            ts.setIsLocked(-1);
            ts.setIsPacked(0);
            ts.setBladeProfile(producePlanDetail.getConsumerProductName());
            // 保存生产信息
            feedingRecordDao.save(ts);
        }
    }

    @Deprecated
    @Override
    public void updatePart(Roll roll) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("partBarcode", roll.getPartBarcode());
        List<PartBarcode> partBarcode = feedingRecordDao.findListByMap(PartBarcode.class, map);
        partBarcode.get(0);
        CutPlan w = feedingRecordDao.findById(CutPlan.class, partBarcode.get(0).getPlanId());
        TotalStatistics ts = new TotalStatistics();
        ts.setBarcodeType("part");
        ts.setRollBarcode(roll.getRollBarcode());
        ts.setRollWeight(roll.getRollWeight());
        ts.setDeviceCode(roll.getRollDeviceCode());
        ts.setCONSUMERNAME(w.getConsumerName());
        ts.setSalesOrderCode(w.getSalesOrderCode());
        ts.setRollOutputTime(new Date());
        ts.setBatchCode(w.getBatchCode());
        ts.setProductModel(w.getProductModel());
        ts.setProducePlanCode(w.getPlanCode());
        ts.setRollQualityGradeCode(roll.getRollQualityGradeCode());
        User user = feedingRecordDao.findById(User.class, roll.getRollUserId());
        ts.setLoginName(user.getUserName());
        Department d = feedingRecordDao.findById(Department.class, user.getDid());
        ts.setName(d.getName());
        ts.setState(0);
        ts.setIsLocked(-1);
        feedingRecordDao.save(ts);
    }

    @Override
    public List<Map<String, Object>> querylist(String deviceCode) {
        return feedingRecordDao.querylist(deviceCode);
    }

    @Override
    public void editInfo(String weaveid, String userid, String tlids) {
        feedingRecordDao.editInfo(weaveid, userid, tlids);
    }

    @Override
    public List<Map<String, Object>> querylist2(String deviceCode) {
        return feedingRecordDao.querylist2(deviceCode);
    }

    @Override
    public List<Map<String, Object>> queryYlbh(String ylbh, String oldweaveId, String deviceCode) {
        return feedingRecordDao.queryYlbh(ylbh, oldweaveId, deviceCode);
    }

    @Override
    public void deleteYlbh(String ylbh, String oldweaveId, String deviceCode) {
        feedingRecordDao.deleteYlbh(ylbh, oldweaveId, deviceCode);
    }
}
