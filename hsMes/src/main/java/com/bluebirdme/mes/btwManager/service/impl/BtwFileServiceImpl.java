/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.service.impl;

import com.bluebirdme.mes.btwManager.dao.IBtwFileDao;
import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.btwManager.service.IBtwFileService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.printer.entity.BarCodePrintRecord;
import com.bluebirdme.mes.printer.service.impl.MergePrinterServiceImpl;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.store.dao.IBoxBarcodeDao;
import com.bluebirdme.mes.store.dao.IPartBarcodeDao;
import com.bluebirdme.mes.store.dao.IRollBarcodeDao;
import com.bluebirdme.mes.store.dao.ITrayBarCodeDao;
import com.bluebirdme.mes.store.entity.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */
@Service
@AnyExceptionRollback
public class BtwFileServiceImpl extends BaseServiceImpl implements IBtwFileService {

    @Resource
    IBtwFileDao btwFileDao;

    @Resource
    IRollBarcodeDao rollBarcodeDao;

    @Resource
    IPartBarcodeDao partBarcodeDao;

    @Resource
    ITrayBarCodeDao trayBarcodeDao;

    @Resource
    IBoxBarcodeDao boxBarcodeDao;

    public static String UPLOAD_PATH = new File(PathUtils.getClassPath()) + File.separator + "BtwFiles" + File.separator;


    @Override
    protected IBaseDao getBaseDao() {
        return btwFileDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return btwFileDao.findPageInfo(filter, page);
    }

    @Override
    public String queryBtwFile(String weavePlanId, String type) throws Exception {
        if (StringUtils.isBlank(weavePlanId)) {
            return "";
        }
        // 先根据id查询出信息
        WeavePlan w = findById(WeavePlan.class, Long.parseLong(weavePlanId));
        FinishedProduct fp = findById(FinishedProduct.class, w.getProductId());
        long consumerId = fp.getProductConsumerId();
        List<Map<String, Object>> list = btwFileDao.queryBtwFilebyCustomerId(consumerId, type);
        List<Map<String, Object>> combobox = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("t", "标准版");
        map.put("v", "0");
        combobox.add(map);
        for (Map<String, Object> m : list) {
            map = new HashMap<>();
            map.put("t", m.get("TAGNAME"));
            map.put("v", m.get("ID"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }

    @Override
    public List<Map<String, Object>> getBtwFilebyCustomerId(String customerId, String type, Boolean hasStandard) throws Exception {
        long long_customerId = 0L;
        if (!StringUtils.isEmpty(customerId)) {
            long_customerId = Long.parseLong(customerId);
        }
        List<Map<String, Object>> list = btwFileDao.queryBtwFilebyCustomerId(long_customerId, type);
        List<Map<String, Object>> combobox = new ArrayList<>();
        Map<String, Object> map;
        if (hasStandard) {
            map = new HashMap<>();
            map.put("t", "标准版");
            map.put("v", "0");
            combobox.add(map);
        }
        for (Map<String, Object> m : list) {
            map = new HashMap<>();
            map.put("t", m.get("TAGNAME"));
            map.put("v", m.get("ID"));
            combobox.add(map);
        }
        return combobox;
    }

    @Override
    public String editBacode(IBarcode iBarcode, Integer customerBarCodeRecord, Integer agentBarCodeRecord, long btwfileId) {
        if (iBarcode.getIndividualOutPutString() == null || iBarcode.getIndividualOutPutString() == "") {
            return "个性化条码不存在，请先打印个性化条码";
        }
        BtwFile btwFile = findById(BtwFile.class, btwfileId);
        if (customerBarCodeRecord > 0) {
            if (customerBarCodeRecord > btwFile.getConsumerBarCodeRecord()) {
                return "客户条码不能大于" + btwFile.getTagName() + btwFile.getConsumerBarCodeRecord();
            }
            String customerBarCode = MergePrinterServiceImpl.GetMaxBarCode(btwFile.getConsumerBarCodePrefix() == null ? "" : btwFile.getConsumerBarCodePrefix(), customerBarCodeRecord, btwFile.getConsumerBarCodeDigit() == null ? 0 : btwFile.getConsumerBarCodeDigit());
            Map<String, Object> map = new HashMap<>();
            map.put("btwfileId", btwfileId);
            map.put("customerBarCode", customerBarCode);
            List<RollBarcode> list = this.findListByMap(RollBarcode.class, map);
            if (list.size() == 0) {
                iBarcode.setCustomerBarCode(customerBarCode);
            }
        }

        if (agentBarCodeRecord > 0) {
            if (agentBarCodeRecord > btwFile.getAgentBarCodeRecord()) {
                return "供销商条码不能大于" + btwFile.getTagName() + btwFile.getAgentBarCodeRecord();
            }
            String agentBarCode = MergePrinterServiceImpl.GetMaxBarCode(btwFile.getAgentBarCodePrefix() == null ? "" : btwFile.getAgentBarCodePrefix(), agentBarCodeRecord, btwFile.getAgentBarCodeDigit() == null ? 0 : btwFile.getAgentBarCodeDigit());
            Map<String, Object> map = new HashMap<>();
            map.put("btwfileId", btwfileId);
            map.put("agentBarCode", agentBarCode);
            List<RollBarcode> list = this.findListByMap(RollBarcode.class, map);
            if (list.size() == 0) {
                iBarcode.setAgentBarCode(agentBarCode);
            }
        }

        Gson gson = new Gson();
        List<BarCodePrintRecord> list = gson.fromJson(iBarcode.getIndividualOutPutString(), new TypeToken<List<BarCodePrintRecord>>() {
        }.getType());
        List<BarCodePrintRecord> barCodePrintRecordData = new ArrayList<>();
        for (BarCodePrintRecord barCodePrintRecord : list) {
            if (barCodePrintRecord.getKey() == "CustomerBarCode" || barCodePrintRecord.getKey() == "AgentBarCode") {
                continue;
            }
            barCodePrintRecordData.add(barCodePrintRecord);
        }

        barCodePrintRecordData.add(new BarCodePrintRecord("CustomerBarCode", iBarcode.getCustomerBarCode()));
        barCodePrintRecordData.add(new BarCodePrintRecord("AgentBarCode", iBarcode.getAgentBarCode()));
        iBarcode.setIndividualOutPutString(GsonTools.toJson(barCodePrintRecordData));
        iBarcode.setBtwfileId(btwfileId);
        if (iBarcode instanceof RollBarcode) {
            update((RollBarcode) iBarcode);
        } else if (iBarcode instanceof BoxBarcode) {
            update((BoxBarcode) iBarcode);
        } else if (iBarcode instanceof TrayBarCode) {
            update((TrayBarCode) iBarcode);
        } else if (iBarcode instanceof PartBarcode) {
            update((PartBarcode) iBarcode);
        }
        return "更新条码成功";
    }

    @Override
    public String clearBacode(IBarcode iBarcode) {
        iBarcode.setCustomerBarCode("");
        iBarcode.setAgentBarCode("");
        iBarcode.setIndividualOutPutString(null);
        if (iBarcode instanceof RollBarcode) {
            update((RollBarcode) iBarcode);
        } else if (iBarcode instanceof BoxBarcode) {
            update((BoxBarcode) iBarcode);
        } else if (iBarcode instanceof TrayBarCode) {
            update((TrayBarCode) iBarcode);
        } else if (iBarcode instanceof PartBarcode) {
            update((PartBarcode) iBarcode);
        }

        if (iBarcode.getBtwfileId() != null) {
            BtwFile btwFile = findById(BtwFile.class, iBarcode.getBtwfileId());
            if (btwFile != null) {
                List<Map<String, Object>> list = new ArrayList<>();
                if (iBarcode instanceof RollBarcode) {
                    list = rollBarcodeDao.findMaxRollBarCode(iBarcode.getBtwfileId());
                } else if (iBarcode instanceof BoxBarcode) {
                    list = boxBarcodeDao.findMaxBoxBarCode(iBarcode.getBtwfileId());
                } else if (iBarcode instanceof TrayBarCode) {
                    list = trayBarcodeDao.findMaxTrayBarCode(iBarcode.getBtwfileId());
                } else if (iBarcode instanceof PartBarcode) {
                    list = partBarcodeDao.findMaxPartBarCode(iBarcode.getBtwfileId());
                }
                String maxAgentBarCode = list.get(0).get("MAXAGENTBARCODE").toString();
                String maxCustomerBarCode = list.get(0).get("MAXCUSTOMERBARCODE").toString();
                maxAgentBarCode = maxAgentBarCode.replace(btwFile.getAgentBarCodePrefix(), "").toString();
                maxAgentBarCode = maxAgentBarCode.equals("") ? "0" : maxAgentBarCode;
                maxCustomerBarCode = maxCustomerBarCode.replace(btwFile.getConsumerBarCodePrefix(), "").toString();
                maxCustomerBarCode = maxCustomerBarCode.equals("") ? "0" : maxCustomerBarCode;
                btwFile.setAgentBarCodeRecord(Integer.parseInt(maxAgentBarCode));
                btwFile.setConsumerBarCodeRecord(Integer.parseInt(maxCustomerBarCode));
                update(btwFile);
            }
        }
        return "";
    }

    @Override
    public String saveBtwFilePrints(BtwFile btwFile, String userId) {
        if (btwFile.getId() == null) {
            btwFile.setCreateTime(new Date());
            btwFile.setCreater(userId);
        }
        btwFile.setModifyTime(new Date());
        btwFile.setModifyUser(userId);
        if (btwFile.getConsumerId() == null) {
            return "请选择客户";
        }

        if (btwFile.getConsumerBarCodeDigit() < 0) {
            return "客户条码位数不能小于0";
        }

        if (btwFile.getAgentBarCodeDigit() < 0) {
            return "供销商条码位数不能小于0";
        }

        if (btwFile.getConsumerBarCodeRecord() < 0) {
            return "客户条码数值不能小于0";
        }

        if (btwFile.getAgentBarCodeRecord() < 0) {
            return "供销商条码数值不能小于0";
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("consumerId", btwFile.getConsumerId());
        map.put("tagType", btwFile.getTagType());
        map.put("agentBarCodePrefix", btwFile.getAgentBarCodePrefix());
        map.put("State", 1);
        if (has(BtwFile.class, map, btwFile.getId())) {
            return "该用户已有该标签类型的供销商条码前缀，请先作废后重新上传模版，重新上传模版时候记录数和原模版要保持一致";
        }
        map.clear();
        map.put("consumerId", btwFile.getConsumerId());
        map.put("tagType", btwFile.getTagType());
        map.put("consumerBarCodePrefix", btwFile.getConsumerBarCodePrefix());
        map.put("State", 1);
        if (has(BtwFile.class, map, btwFile.getId())) {
            return "该用户已有该标签类型的客户条码前缀，请先作废后重新上传模版，重新上传模版时候记录数和原模版要保持一致";
        }
        if (btwFile.getId() == null) {
            save(btwFile);
        } else {
            update(btwFile);
        }
        return "ok";
    }

    @Override
    public String importbtwFileUpload(MultipartFile file, long btwFileId, String userId, HttpServletRequest request) throws Exception {
        BtwFile btwFile = findById(BtwFile.class, btwFileId);
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = btwFile.getTagName() + sdf.format(d) + ".btw";
        if (UPLOAD_PATH == null) {
            UPLOAD_PATH = request.getSession().getServletContext().getRealPath("/") + File.separator;
            File _file = new File(UPLOAD_PATH);
            if (!_file.exists()) {
                _file.mkdirs();
            }
        }
        String filePath = UPLOAD_PATH + btwFile.getConsumerCode() + "\\";
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        filePath += fileName;
        File target = new File(filePath);
        byte[] bytes = file.getBytes();
        FileCopyUtils.copy(bytes, target);
        btwFile.setTagActName(fileName);
        btwFile.setUploadDate(new Date());
        btwFile.setUploadUser(userId);
        update(btwFile);
        return "ok";
    }
}
