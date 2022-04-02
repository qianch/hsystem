package com.bluebirdme.mes.baseInfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import org.springframework.stereotype.Service;

import com.bluebirdme.mes.baseInfo.dao.IBomDao;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.BcBomVersionDetail;
import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBom;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.service.IBcBomService;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.baseInfo.service.IFtcBomService;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.produce.entity.FinishedProduct;

/**
 * BOM统一Service
 *
 * @author Goofy
 * @Date 2016年10月19日 下午2:36:05
 */
@Service
@AnyExceptionRollback
public class BomServiceImpl extends BaseServiceImpl implements IBomService {

    @Resource
    IBomDao bomDao;
    @Resource
    IBcBomService bcBomService;
    @Resource
    ITcBomService tcBomService;
    @Resource
    IFtcBomService ftcBomService;

    @Override
    protected IBaseDao getBaseDao() {
        return bomDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page)
            throws Exception {
        return null;
    }

    @Override
    public List<BCBomVersion> getBcVersions(String bcBomCode) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("packBomCode", bcBomCode);
        // 查询BOM信息
        BcBom bom = bcBomService.findUniqueByMap(BcBom.class, param);
        List<BCBomVersion> list = new ArrayList<BCBomVersion>();
        if (bom == null)
            return list;

        // 查询BOM的版本信息
        param.clear();
        param.put("packBomId", bom.getId());
        param.put("auditState", 2);
        param.put("packEnabled", 1);
        list = bcBomService.findListByMap(BCBomVersion.class, param);
        param.put("packEnabled", 0);
        list.addAll(bcBomService.findListByMap(BCBomVersion.class, param));

        return list == null ? new ArrayList<BCBomVersion>() : list;
    }

    @Override
    public List<TcBomVersion> getTcVersions(String tcBomCode) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tcProcBomCode", tcBomCode);
        // 查询BOM信息
        TcBom bom = tcBomService.findUniqueByMap(TcBom.class, param);
        List<TcBomVersion> list = new ArrayList<TcBomVersion>();
        if (bom == null)
            return list;
        // 查询BOM的版本信息
        param.clear();
        param.put("tcProcBomId", bom.getId());
        param.put("auditState", 2);
        param.put("tcProcBomVersionEnabled", 1);

        list = tcBomService.findListByMap(TcBomVersion.class, param);
        param.put("tcProcBomVersionEnabled", 0);
        list.addAll(tcBomService.findListByMap(TcBomVersion.class, param));
        return list == null ? new ArrayList<TcBomVersion>() : list;
    }

    @Override
    public List<FtcBomVersion> getFtcVersions(String ftcBomCode) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ftcProcBomCode", ftcBomCode);

        // 查询BOM信息
        FtcBom bom = ftcBomService.findUniqueByMap(FtcBom.class, param);
        List<FtcBomVersion> list = new ArrayList<FtcBomVersion>();
        if (bom == null)
            return list;

        // 查询BOM的版本信息
        param.clear();
        param.put("ftcProcBomId", bom.getId());
        param.put("auditState", 2);
        param.put("ftcProcBomVersionEnabled", 1);
        list = ftcBomService.findListByMap(FtcBomVersion.class, param);
        param.put("ftcProcBomVersionEnabled", 0);
        list.addAll(ftcBomService.findListByMap(FtcBomVersion.class, param));

        return list == null ? new ArrayList<FtcBomVersion>() : list;
    }

    @Override
    public <T> List<T> getBomDetails(Class<T> clazz, String bomCode, String bomVersionCode) {
        return bomDao.getBomDetails(clazz, bomCode, bomVersionCode);
    }

    @Override
    public List<FtcBomDetail> getBomDetails(FinishedProduct product) {
        String bomCode = product.getProductProcessCode();
        String bomVersion = product.getProductProcessBomVersion();
        List<FtcBomDetail> resultList = getBomDetails(FtcBomDetail.class, bomCode, bomVersion);
        return resultList;
    }

    @Override
    public void setDefult(String type, int defultType, Long id) {
        if (type.equals("tc")) {
            TcBomVersion version = findById(TcBomVersion.class, id);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("tcProcBomId", version.getTcProcBomId());
            List<TcBomVersion> saveList = new ArrayList<TcBomVersion>();

            if (defultType == 1) {
                List<TcBomVersion> versionList = findListByMap(TcBomVersion.class, map);
                for (TcBomVersion v : versionList) {
                    if (v.getTcProcBomVersionDefault() == 1) {
                        v.setTcProcBomVersionDefault(-1);
                        saveList.add(v);
                    }
                }
            }
            version.setTcProcBomVersionDefault(defultType);
            saveList.add(version);
            save(saveList.toArray(new TcBomVersion[]{}));
        } else if (type.equals("ftc")) {
            FtcBomVersion version = findById(FtcBomVersion.class, id);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ftcProcBomId", version.getFtcProcBomId());

            List<FtcBomVersion> saveList = new ArrayList<FtcBomVersion>();
            if (defultType == 1) {
                List<FtcBomVersion> versionList = findListByMap(
                        FtcBomVersion.class, map);
                for (FtcBomVersion v : versionList) {
                    if (v.getFtcProcBomVersionDefault() == 1) {
                        v.setFtcProcBomVersionDefault(-1);
                        saveList.add(v);
                    }
                }
            }
            version.setFtcProcBomVersionDefault(defultType);
            saveList.add(version);
            save(saveList.toArray(new FtcBomVersion[]{}));
        } else {
            BCBomVersion version = findById(BCBomVersion.class, id);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("packBomId", version.getPackBomId());

            List<BCBomVersion> saveList = new ArrayList<BCBomVersion>();
            if (defultType == 1) {
                List<BCBomVersion> versionList = findListByMap(BCBomVersion.class, map);
                for (BCBomVersion v : versionList) {
                    if (v.getPackIsDefault() == 1) {
                        v.setPackIsDefault(-1);
                        saveList.add(v);
                    }
                }
            }
            version.setPackIsDefault(defultType);
            saveList.add(version);
            save(saveList.toArray(new BCBomVersion[]{}));
        }
    }

    @Override
    public List<Map<String,Object>> findSalesOrderDetail(Long id,String c) throws Exception{
        return bomDao.findSalesOrderDetail(id,c);
    }

    @Override
    public List<Map<String, Object>> findSalesOrderDetail1(Long id)  throws Exception{
        return bomDao.findSalesOrderDetail1(id);
    }
}
