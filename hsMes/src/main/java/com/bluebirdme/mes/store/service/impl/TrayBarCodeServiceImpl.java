/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service.impl;

import com.bluebirdme.mes.btwManager.service.IBtwFileService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.store.dao.ITrayBarCodeDao;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.store.service.ITrayBarCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-8 14:59:26
 */
@Service
@AnyExceptionRollback
public class TrayBarCodeServiceImpl extends BaseServiceImpl implements ITrayBarCodeService {
    @Resource
    ITrayBarCodeDao trayBarCodeDao;

    @Resource
    IBtwFileService btwFileService;

    @Override
    protected IBaseDao getBaseDao() {
        return trayBarCodeDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return trayBarCodeDao.findPageInfo(filter, page);
    }

    @Override
    public List<Map<String, Object>> findSalesOrderByRollcode(String code) {
        return trayBarCodeDao.findSalesOrderByRollcode(code);
    }

    @Override
    public List<Map<String, Object>> findSalesOrderByBoxcode(String code) {
        return trayBarCodeDao.findSalesOrderByBoxcode(code);
    }

    @Override
    public List<Map<String, Object>> findSalesOrderByTraycode(String code) {
        return trayBarCodeDao.findSalesOrderByTraycode(code);
    }

    @Override
    public List<Map<String, Object>> findProductByRollcode(String code) {
        return trayBarCodeDao.findProductByRollcode(code);
    }

    @Override
    public List<Map<String, Object>> findProductByBoxcode(String code) {
        return trayBarCodeDao.findProductByBoxcode(code);
    }

    @Override
    public FinishedProduct findProductByTraycode(String productIsTc, String trayCode) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> bbmap = new HashMap<>();
        Map<String, Object> brmap = new HashMap<>();
        Map<String, Object> pbmap = new HashMap<>();
        Map<String, Object> rbmap = new HashMap<>();
        FinishedProduct finishedProduct = new FinishedProduct();
        map.put("trayBarcode", trayCode);
        List<TrayBoxRoll> li = trayBarCodeDao.findListByMap(TrayBoxRoll.class, map);
        //遍历托箱卷关系
        for (TrayBoxRoll tbr : li) {
            if (productIsTc.equals("1")) {
                //套材
                if (tbr.getBoxBarcode() != null) {
                    //(托-拖箱卷-箱-箱中卷信息-部件条码-产品)
                    bbmap.put("boxBarcode", tbr.getBoxBarcode());
                    List<Box> bbli = trayBarCodeDao.findListByMap(Box.class, bbmap);
                    //遍历箱表，获取箱条码，根据箱条码，查询箱中卷信息表
                    for (Box b : bbli) {
                        brmap.put("boxBarcode", b.getBoxBarcode());
                        List<BoxRoll> brli = trayBarCodeDao.findListByMap(BoxRoll.class, brmap);
                        //遍历箱中卷信息表，获取部件条码,根据部件条码，查询部件条码表
                        for (BoxRoll br : brli) {
                            pbmap.put("barcode", br.getPartBarcode());
                            List<PartBarcode> pbli = trayBarCodeDao.findListByMap(PartBarcode.class, pbmap);
                            //遍历部件条码表，根据产品id获取，产品信息
                            for (PartBarcode pb : pbli) {
                                finishedProduct = trayBarCodeDao.findById(FinishedProduct.class, pb.getSalesProductId());
                                //fpList.add(finishedProduct);
                            }
                        }
                    }
                } else { //(托-拖箱卷-部件条码-产品)
                    pbmap.put("barcode", tbr.getPartBarcode());
                    List<PartBarcode> pbli = trayBarCodeDao.findListByMap(PartBarcode.class, pbmap);
                    //遍历部件条码表，根据产品id获取，产品信息
                    for (PartBarcode pb : pbli) {
                        finishedProduct = trayBarCodeDao.findById(FinishedProduct.class, pb.getSalesProductId());
                        //fpList.add(finishedProduct);
                    }
                }
            } else {
                //非套材
                if (tbr.getBoxBarcode() != null) {
                    //(托-拖箱卷-箱-卷条码-产品)
                    bbmap.put("boxBarcode", tbr.getBoxBarcode());
                    List<Box> bbli = trayBarCodeDao.findListByMap(Box.class, bbmap);
                    //遍历箱表，获取箱条码，根据箱条码，查询箱中卷信息表
                    for (Box b : bbli) {
                        brmap.put("boxBarcode", b.getBoxBarcode());
                        List<BoxRoll> brli = trayBarCodeDao.findListByMap(BoxRoll.class, brmap);
                        //遍历箱中卷信息表，获取卷条码,根据卷条码，查询卷条码表
                        for (BoxRoll br : brli) {
                            rbmap.put("barcode", br.getRollBarcode());
                            List<RollBarcode> rbli = trayBarCodeDao.findListByMap(RollBarcode.class, rbmap);
                            //遍历卷条码表，根据产品id获取，产品信息
                            for (RollBarcode rb : rbli) {
                                finishedProduct = trayBarCodeDao.findById(FinishedProduct.class, rb.getSalesProductId());
                                //fpList.add(finishedProduct);
                            }
                        }
                    }
                } else {
                    //(托-拖箱卷-卷条码-产品)
                    rbmap.put("barcode", tbr.getRollBarcode());
                    List<RollBarcode> rbli = trayBarCodeDao.findListByMap(RollBarcode.class, rbmap);
                    //遍历卷条码表，根据产品id获取，产品信息
                    for (RollBarcode rb : rbli) {
                        finishedProduct = trayBarCodeDao.findById(FinishedProduct.class, rb.getSalesProductId());
                    }
                }
            }
        }
        return finishedProduct;
    }

    public String clearTray(String ids) throws Exception {
        String[] ids_temp = ids.split(",");
        for (String s : ids_temp) {
            TrayBarCode trayBarcode = findById(TrayBarCode.class, Long.parseLong(s));
            btwFileService.clearBacode(trayBarcode);
        }
        return "";
    }

    @Override
    public List<Map<String, Object>> findMaxTrayBarCodeCount() {
        return trayBarCodeDao.findMaxTrayBarCodeCount();
    }

    @Override
    public List<Map<String, Object>> findMaxTrayPartBarCodeCount() {
        return trayBarCodeDao.findMaxTrayPartBarCodeCount();
    }
}
