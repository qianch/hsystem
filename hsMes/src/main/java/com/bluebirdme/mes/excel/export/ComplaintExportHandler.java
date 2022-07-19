package com.bluebirdme.mes.excel.export;

import com.bluebirdme.mes.complaint.service.IComplaintService;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelExportHandler;
import com.bluebirdme.mes.core.spring.SpringCtx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 投诉导出功能
 *
 * @author Goofy
 * @Date 2016年11月26日 下午12:43:28
 */
public class ComplaintExportHandler extends ExcelExportHandler {
    private static final Logger logger = LoggerFactory.getLogger(ComplaintExportHandler.class);

    @Override
    public ExcelContent getContent() {
        return null;
    }

    @Override
    public ExcelContent getContent(String[] id) {
        return null;
    }

    @Override
    public ExcelContent getContent(Filter filter) {
        ExcelContent content = new ExcelContent();
        String[] titles = new String[]{"投诉代码", "客户名称", "供货规格", "8D报告",
                "客户来源", "基地", "投诉日期", "信息来源", "供货状态", "出货数量", "发货日期",
                "生成日期", "责任单位", "投诉类型", "措施验收", "赔偿金额", "处理进度",
                "问题描述", "原因分析", "措施验收", "整改措施", "最后操作人", "最后操作时间"};

        List<String> titleList = new ArrayList<String>();
        Collections.addAll(titleList, titles);
        content.setTitles(new ArrayList<>(titleList));
        IComplaintService service = (IComplaintService) SpringCtx.getBean("complaintServiceImpl");

        Page page = new Page();
        page.setAll(1);

        try {
            List<Map<String, Object>> rs = (List<Map<String, Object>>) service.findPageInfo(filter, page).get("rows");
            List<String[]> data = new ArrayList<String[]>();
            String[] temp;
            for (Map<String, Object> map : rs) {
                temp = new String[23];
                temp[0] = map.get("COMPLAINTCODE").toString();
                temp[1] = map.get("CONSUMERNAME").toString();
                temp[2] = map.get("PRODUCTMODEL").toString();
                temp[3] = map.get("D8REPORT") == null ? "" : map.get("D8REPORT").toString();
                temp[4] = map.get("CONSUMERFROM").toString();
                temp[5] = map.get("BASEPLACE") == null ? "" : map.get("BASEPLACE").toString();
                temp[6] = map.get("COMPLAINTDATE").toString();
                temp[7] = map.get("INFOFROM").toString();
                temp[8] = map.get("SUPPLYSTATE").toString();
                temp[9] = map.get("SUPPLYCOUNT").toString();
                temp[10] = map.get("DELIVERYDATE").toString();
                temp[11] = map.get("PRODUCEDATE").toString();
                temp[12] = map.get("BUSINESSUNIT") == null ? "" : map.get("BUSINESSUNIT").toString();
                temp[13] = map.get("COMPLAINTTYPE").toString();
                temp[14] = map.get("CHECKMESURES").toString();
                temp[15] = map.get("COMPENSATION") == null ? "" : map.get("COMPENSATION").toString();
                temp[16] = map.get("HANDLINGPROGRESS") == null ? "" : map.get("HANDLINGPROGRESS").toString();
                temp[17] = map.get("QUESTIONDESC") == null ? "" : map.get("QUESTIONDESC").toString();
                temp[18] = map.get("ANALYSISOFCAUSES") == null ? "" : map.get("ANALYSISOFCAUSES").toString();
                temp[19] = map.get("CHECKMESURES") == null ? "" : map.get("CHECKMESURES").toString();
                temp[20] = map.get("CORRECTIVEMEASURES") == null ? "" : map.get("CORRECTIVEMEASURES").toString();
                temp[21] = map.get("LASTUPDATEUSER") == null ? "" : map.get("LASTUPDATEUSER").toString();
                temp[22] = map.get("LASTUPDATEDATE") == null ? "" : map.get("LASTUPDATEDATE").toString();
                data.add(temp);
                content.setData(data);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return content;
    }
}
