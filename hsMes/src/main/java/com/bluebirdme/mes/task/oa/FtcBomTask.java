package com.bluebirdme.mes.task.oa;

import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomService;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomVersionService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.oa.entity.FtcBomOa;
import com.bluebirdme.mes.oa.service.IFtcBomOaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Component
public class FtcBomTask extends AbstractBomTask {
    private static final Logger log = LoggerFactory.getLogger(FtcBomTask.class);
    @Resource
    public IFtcBomOaService ftcBomOaService;
    @Resource
    IFtcBcBomService ftcBcBomService;
    @Resource
    IFtcBcBomVersionService ftcBcBomVersionService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void creator() throws Exception {
        List<FtcBomOa> FtcBomOaList = ftcBomOaService.find(FtcBomOa.class, "status", "0");
        for (FtcBomOa fbo : FtcBomOaList) {
            addBom(fbo);
        }

    }

    public void addBom(FtcBomOa ftcBomOa) throws Exception {
        File file = new File(ftcBomOa.getFtcPackBomPath());
        Long fileId = saveFile(file);
        //第一层入库
        FtcBcBom ftcBcBom1 = new FtcBcBom();
        Long pid = saveFtcBcBom(ftcBcBom1, ftcBomOa, fileId, "1", null);
        //第二层入库
        FtcBcBom ftcBcBom2 = new FtcBcBom();
        Long pid1 = saveFtcBcBom(ftcBcBom2, ftcBomOa, fileId, "2", pid);
        //第三层入库
        FtcBcBom ftcBcBom3 = new FtcBcBom();
        saveFtcBcBom(ftcBcBom3, ftcBomOa, fileId, "3", pid1);


        ftcBomOa.setStatus("1");
        ftcBomOaService.update(ftcBomOa);
    }

    private Long saveFtcBcBom(FtcBcBom ftcBcBom, FtcBomOa ftcBomOa, Long fileId, String s, Long pid) throws Exception {

        if (s.equals("1")) {
            ftcBcBom.setName(ftcBomOa.getLevel1Name());
            ftcBcBom.setCode(ftcBomOa.getLevel1Code());
            ftcBcBom.setLevel(Integer.valueOf(s));
        } else if (s.equals("2")) {
            ftcBcBom.setName(ftcBomOa.getLevel2Name());
            ftcBcBom.setCode(ftcBomOa.getLevel2Code());
            ftcBcBom.setPid(pid);
            ftcBcBom.setLevel(Integer.valueOf(s));
        } else {
            ftcBcBom.setName(ftcBomOa.getLevel3Name());
            ftcBcBom.setCode(ftcBomOa.getLevel3Code());
            ftcBcBom.setPid(pid);
            ftcBcBom.setLevel(Integer.valueOf(s));

            FtcBcBomVersion ftcBcBomVersion = new FtcBcBomVersion();
            BeanUtils.copyProperties(ftcBomOa, ftcBcBomVersion);
            ftcBcBomVersion.setBcTotalWeight(ftcBomOa.getBcTotalWeight());
            List<FtcBcBomVersion> ftcBcBomVersionList = ftcBcBomVersionService.findAll(FtcBcBomVersion.class);
            List<Long> bidList = new ArrayList<>();
            for (FtcBcBomVersion ftcV : ftcBcBomVersionList) {
                bidList.add(ftcV.getBid());
            }
            ftcBcBomVersion.setBid(Collections.max(bidList) + 1);

            ftcBcBomVersion.setEnabled(0);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("bid", ftcBcBomVersion.getBid());

            List<FtcBcBomVersion> li = ftcBcBomService.findListByMap(FtcBcBomVersion.class, map);
            for (FtcBcBomVersion bcv : li) {
                if (bcv.getVersion().equals(ftcBcBomVersion.getVersion())) {
                    ftcBomOa.setReason("已经有相同名字的版本");
                    ftcBomOa.setStatus("2");
                    if (ftcBomOa.getReason().equals("")) {
                        ftcBomOaService.update(ftcBomOa);
                    }
                }
            }
            Map<String, Object> param = new HashMap<>();
            param.put("bid", ftcBcBomVersion.getBid());
            param.put("productType", ftcBcBomVersion.getProductType());
            List<FtcBcBomVersion> fbbvL = ftcBcBomVersionService.findListByMap(FtcBcBomVersion.class, param);
            for (FtcBcBomVersion fbbv : fbbvL) {
                if (fbbv.getAuditState() < 2) {
                    ftcBomOa.setReason("已有[未提交]、[不通过]或[审核中]的版本！");
                    ftcBomOa.setStatus("2");
                    if (ftcBomOa.getReason().equals("")) {
                        ftcBomOaService.update(ftcBomOa);
                    }
                }
            }
            if (ftcBcBomVersion.getEnabled() == null) {
                ftcBcBomVersion.setEnabled(1);
            }
            System.out.println("===========ftcBcBomVersion:" + ftcBcBomVersion);
            ftcBcBomService.save(ftcBcBomVersion);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", ftcBcBom.getCode());
        map.put("level", ftcBcBom.getLevel());

        if (ftcBcBomService.isExist(FtcBcBom.class, map, true)) {
            ftcBomOa.setReason("已经有同名非套材包材BOM");
            if (ftcBomOa.getReason().equals("")) {
                ftcBomOaService.update(ftcBomOa);
            }
        }
        if (ftcBcBom.getLevel() != 2) {
            ftcBcBomService.save(ftcBcBom);
        } else {//目录BOM是第2层和有fileId(要导入的Excel文件ID)
            ExcelImportMessage eim = ftcBcBomService.doAddFtcBcBom(ftcBcBom, fileId);
            ftcBcBomService.savePdfFile(ftcBcBom, fileId, eim);
        }

        List<FtcBcBom> ftcBcBomList = ftcBcBomService.find(FtcBcBom.class, "code", ftcBcBom.getCode());
        return ftcBcBomList.get(0).getId();
    }
}
