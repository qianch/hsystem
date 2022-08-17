package com.bluebirdme.mes.stock.service.impl;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.stock.dao.IMaterialStockDao;
import com.bluebirdme.mes.stock.entity.*;
import com.bluebirdme.mes.stock.service.IMaterialStockService;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AnyExceptionRollback
public class MaterialStockServiceImpl extends BaseServiceImpl implements IMaterialStockService {
    @Resource
    IMaterialStockDao msDao;

    @Override
    public Material findMaterial(String produceCategory, String materialModel) {
        return msDao.findMaterial(produceCategory, materialModel);
    }

    @Override
    public String getSerial() throws Exception {
        return null;
    }

    @Override
    protected IBaseDao getBaseDao() {
        return msDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public void mIn(MaterialStockState mss, MaterialInRecord mir) {
        mss.setState(1);
        save(mss);
        mir.setMssId(mss.getId());
        mir.setInTime(System.currentTimeMillis());
        save(mir);
    }

    public void back(MaterialInRecord mir, String palletCode) {
        MaterialStockState mss = findOne(MaterialStockState.class, "palletCode", palletCode);
        mir.setMssId(mss.getId());
        save(mir);
        mss.setStockState(0);
        update(mss);
    }

    public void out(MaterialStockOut mso, Long[] mssIds) throws Exception {
        String codes = msDao.validMaterialStockState(mssIds);
        if (!StringUtils.isBlank(codes)) {
            throw new Exception(codes + "已出库，不可再次出库");
        }

        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
        String outOrderCode = "";
        String optUser = mso.getOutOptUser();
        if (now.get(Calendar.HOUR_OF_DAY) < 8) {
            now.add(Calendar.DAY_OF_MONTH, -1);
        }
        if (mso.getWorkShop().equals("编织一车间")) {
            outOrderCode = "BZ1BB" + format.format(now.getTime());
        }
        if (mso.getWorkShop().equals("编织二车间")) {
            outOrderCode = "BZ2BB" + format.format(now.getTime());
        }
        if (mso.getWorkShop().equals("编织三车间")) {
            outOrderCode = "BZ3BB" + format.format(now.getTime());
        }

        if (mso.getWorkShopCode() != null) {
            List<Department> listDepartment = find(Department.class, "code", mso.getWorkShopCode());
            if (listDepartment != null && listDepartment.size() > 0) {
                outOrderCode = listDepartment.get(0).getPrefix() + "BB" + format.format(now.getTime());
            }
        }

        // 根据领料单号判断是否存在已有领料单
        HashMap<String, Object> isExistMap = new HashMap<String, Object>();
        isExistMap.put("outOrderCode", outOrderCode);
        MaterialStockOut _mso = findUniqueByMap(MaterialStockOut.class, isExistMap);
        if (_mso != null) {
            mso = _mso;
        } else {
            mso.setOutOrderCode(outOrderCode);
            save(mso);
        }

        List<Long> mssIdList = new ArrayList<>();
        for (Long mssId : mssIds) {
            if (!mssIdList.contains(mssId)) {
                mssIdList.add(mssId);
            }
        }
        mssIds = mssIdList.toArray(new Long[mssIdList.size()]);
        Object[] objs = new Object[mssIds.length];
        int i = 0;
        for (Long mssId : mssIds) {
            objs[i++] = new MaterialOutOrderDetail(mso.getId(), mssId, optUser);
        }
        save(objs);
    }

    @Override
    public String validMaterialStockState(Long[] ids) {
        return msDao.validMaterialStockState(ids);
    }

    public void backToJuShi(MaterialForceOutRecord out) throws Exception {
        MaterialStockState mss = findById(MaterialStockState.class, out.getMssId());
        if (mss.getStockState() == 1) {
            throw new Exception("该托原料不在库，无法退回");
        }
        save(out);
    }

    public void move(Long ids[], String warehouseCode, String warehousePosCode) {
        msDao.move(ids, warehouseCode, warehousePosCode);
    }

    public void checkResult(StockCheck sc) {
        msDao.checkResult(sc);
    }

    public String getOutWorkShop(String palletCode) {
        return msDao.getOutWorkShop(palletCode);
    }
}
