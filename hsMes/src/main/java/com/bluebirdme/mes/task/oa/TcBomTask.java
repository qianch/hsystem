package com.bluebirdme.mes.task.oa;

import com.bluebirdme.mes.baseInfo.entity.TcBom;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.oa.entity.TcBomOa;
import com.bluebirdme.mes.oa.service.ITcBomOaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;

@Component
public class TcBomTask extends AbstractBomTask {
    private static Logger log = LoggerFactory.getLogger(TcBomTask.class);

    @Resource
    public ITcBomOaService tcBomOaService;
    @Resource
    public ITcBomService tcBomService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void creator() {
        //status 0:Excel文件未添加 1:已添加
        List<TcBomOa> tcBomOaList = tcBomOaService.find(TcBomOa.class, "status", 0);
        try {
            if (tcBomOaList.size() > 0) {
                for (TcBomOa tcBomOa : tcBomOaList) {
                    File file = new File(tcBomOa.getTcProcBomPath());
                    //获取文件id
                    Long fileId = saveFile(file);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("tcProcBomCode", tcBomOa.getTcProcBomCode());
                    TcBomVersion tcBomVersion = new TcBomVersion();
                    tcBomVersion.setTcProcBomVersionCode(tcBomOa.getTcProcBomVersionCode());
                    tcBomVersion.setTcConsumerVersionCode(tcBomOa.getTcConsumerVersionCode());
                    TcBom tcBom = new TcBom();
                    tcBom.setTcProcBomName(tcBomOa.getTcProcBomName());
                    tcBom.setIsTestPro(tcBomOa.getIsTestPro());
                    tcBom.setTcProcBomCode(tcBomOa.getTcProcBomCode());
                    tcBom.setTcProcBomConsumerId(tcBomOa.getTcProcBomConsumerId());
                    if (!tcBomOaService.isExist(TcBom.class, map)) {
                        ExcelImportMessage eim = tcBomService.doAddTcBom(tcBom, tcBomVersion, fileId);
                        tcBomService.savePdfFile(tcBom, tcBomVersion, fileId, eim);
                        //更新status状态
                        tcBomOa.setStatus(1);
                        tcBomOaService.update(tcBomOa);
                    } else {
                        //保存失败原因
                        tcBomOa.setReason("BOM代码重复");
                        //锁定
                        tcBomOa.setStatus(3);
                        tcBomOaService.update(tcBomOa);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(),e);
        }
    }
}
