package com.bluebirdme.mes.task.oa;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.service.IBCBomVersionService;
import com.bluebirdme.mes.baseInfo.service.IBcBomService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.oa.entity.BcBomOa;
import com.bluebirdme.mes.oa.service.IBcBomOaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;

@Component
public class BcBomTask extends AbstractBomTask {
    private static final Logger log = LoggerFactory.getLogger(BcBomTask.class);
    @Resource
    public IBcBomOaService bcBomOaService;
    @Resource
    public IBcBomService bcBomService;
    @Resource
    public IBCBomVersionService bCBomVersionService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void creator() {
        try {
            //0表示未添加
            List<BcBomOa> bcBomOaList = bcBomOaService.find(BcBomOa.class, "status", 0);
            if (bcBomOaList.size() > 0) {
                for (BcBomOa bcBomOa : bcBomOaList) {
                    File file = new File(bcBomOa.getPackBomPath());
                    Long fileId = saveFile(file);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("packBomGenericName", bcBomOa.getPackBomGenericName());
                    if (bcBomOaService.isExist(BcBom.class, map)) {
                        //保存失败原因
                        bcBomOa.setReason("已经有同名包材BOM");
                        //锁定
                        bcBomOa.setStatus(3);
                        bcBomOaService.update(bcBomOa);
                    } else {
                        BcBom bcBom = new BcBom();
                        BeanUtils.copyProperties(bcBomOa, bcBom);
                        bcBomService.save(bcBom);
                        //更新status状态
                        bcBomOa.setStatus(1);
                        bcBomOaService.update(bcBomOa);
                        //保存包材bom版本信息
                        addBomVersion(bcBomOa, fileId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("creator()====异常：" + e.getMessage());
        }
    }

    public void addBomVersion(BcBomOa bcBomOa, Long fileId) throws Exception {
        BcBom bcBom = new BcBom();
        bcBom.setPackBomCode(bcBomOa.getPackBomCode());
        BcBom packBom = bcBomService.findOne(BcBom.class, "packBomCode", bcBom.getPackBomCode());
        BCBomVersion bcBomVersion = new BCBomVersion();
        bcBomVersion.setPackVersion(bcBomOa.getPackVersion());
        bcBomVersion.setPackBomId(packBom.getId());
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("packBomId", bcBomVersion.getPackBomId());
        List<BCBomVersion> li = bCBomVersionService.findListByMap(BCBomVersion.class, map);
        for (BCBomVersion bv : li) {
            if (bv.getPackVersion().equals(bcBomVersion.getPackVersion())) {
                //保存失败原因
                bcBomOa.setReason("已经有相同名字的版本");
                //锁定
                bcBomOa.setStatus(3);
                bcBomOaService.update(bcBomOa);
            }
        }
        if (bcBomVersion.getPackEnabled() == null) {
            //1：启用
            bcBomVersion.setPackEnabled(1);
        }
        if (bcBomVersion.getPackIsDefault() == null) {
            //1:默认
            bcBomVersion.setPackIsDefault(1);
        }
        bCBomVersionService.save(bcBomVersion);
        bCBomVersionService.savePdfFile(bcBomVersion, fileId, new ExcelImportMessage());
    }
}
