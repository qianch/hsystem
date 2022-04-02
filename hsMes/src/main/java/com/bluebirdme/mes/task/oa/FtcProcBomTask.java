package com.bluebirdme.mes.task.oa;

import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.service.IFtcBomService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.oa.entity.FtcProcBomOa;
import com.bluebirdme.mes.oa.service.IFtcProcBomOaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FtcProcBomTask extends  AbstractBomTask {
    private static Logger log = LoggerFactory.getLogger(FtcProcBomTask.class);
    @Resource
    public IFtcProcBomOaService ftcProcBomOaService;
    @Resource
    public IFtcBomService ftcBomService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void creator() throws Exception {
        List<FtcProcBomOa> FtcProcBomOaList = ftcProcBomOaService.find(FtcProcBomOa.class, "status", "0");
        for (FtcProcBomOa fpo : FtcProcBomOaList) {
            addBom(fpo);
        }

    }

    public void addBom(FtcProcBomOa ftcProcBomOa) throws Exception {
        File file = new File(ftcProcBomOa.getTcProcBomPath());
        Long fileId = saveFile(file);
        FtcBom ftcBom = new FtcBom();
        BeanUtils.copyProperties(ftcProcBomOa,ftcBom);
        FtcBomVersion ftcBomVersion = new FtcBomVersion();
        ftcBomVersion.setFtcConsumerVersionCode(String.valueOf(ftcProcBomOa.getFtcConsumerVersionCode()));
        ftcBomVersion.setFtcProcBomVersionCode(String.valueOf(ftcProcBomOa.getFtcProcBomVersionCode()));
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ftcProcBomCode", ftcBom.getFtcProcBomCode());
        if (!ftcBomService.isExist(FtcBom.class, map)) {
            ExcelImportMessage eim=ftcBomService.doAddFtcBom(ftcBom, ftcBomVersion, fileId);
            if(eim != null && eim.hasError()){
                Map<String,String> excelErrorMsg=new HashMap<>();
                excelErrorMsg.put("excelErrorMsg",eim.getMessage());
            }
        } else {
            ftcProcBomOa.setReason("BOM代码重复！");
            ftcProcBomOa.setStatus("2");
            if (ftcProcBomOa.getReason().equals("")) {
                ftcProcBomOaService.update(ftcProcBomOa);
            }
        }

        ftcProcBomOa.setStatus("1");
        ftcProcBomOaService.update(ftcProcBomOa);
    }
}
