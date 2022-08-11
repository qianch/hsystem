package com.bluebirdme.mes.siemens.excel;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelExportHandler;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.siemens.barcode.service.IFragmentBarcodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.MapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 类注释
 *
 * @author Goofy
 * @Date 2017年8月4日 下午12:21:18
 */
public class FragmentBarcodeExportHandler extends ExcelExportHandler {
    private static final Logger logger = LoggerFactory.getLogger(FragmentBarcodeExportHandler.class);

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
        ExcelContent excel = new ExcelContent();
        String[] titles = {"条码号", "任务单号", "派工单号", "订单号", "批次号", "客户名称", "客户大类", "部件名称", "裁片名称", "图号", "图纸版本", "出图顺序", "备注", "长度M", "宽度MM", "重量", "组套状态", "组套时间", "组套操作人", "组别", "机长", "机台", "车间", "胚布规格", "打印时间", "打印人员", "打印类型", "重打原因", "补打时间", "补打人员", "补打原因"};
        excel.setTitles(Arrays.asList(titles));
        IFragmentBarcodeService fbcService = (IFragmentBarcodeService) SpringCtx.getBean("fragmentBarcodeServiceImpl");

        Page page = new Page();
        page.setAll(1);

        try {
            Map<String, Object> result = fbcService.findPageInfo(filter, page);
            List<String[]> data = new ArrayList<>();
            List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("rows");
            String[] rowData;
            for (Map<String, Object> row : rows) {
                rowData = new String[titles.length];
                rowData[0] = MapUtils.getAsStringIgnoreCase(row, "BARCODE");
                rowData[1] = MapUtils.getAsStringIgnoreCase(row, "CTCODE");
                rowData[2] = MapUtils.getAsStringIgnoreCase(row, "CTOCODE");
                rowData[3] = MapUtils.getAsStringIgnoreCase(row, "ORDERCODE");
                rowData[4] = MapUtils.getAsStringIgnoreCase(row, "BATCHCODE");
                rowData[5] = MapUtils.getAsStringIgnoreCase(row, "CONSUMERNAME");
                rowData[6] = MapUtils.getAsStringIgnoreCase(row, "CONSUMERCATEGORY");
                rowData[7] = MapUtils.getAsStringIgnoreCase(row, "PARTNAME");
                rowData[8] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTNAME");
                rowData[9] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTDRAWINGNO");
                rowData[10] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTDRAWINGVER");
                rowData[11] = MapUtils.getAsStringIgnoreCase(row, "PRINTSORT");
                rowData[12] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTMEMO");
                rowData[13] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTLENGTH");
                rowData[14] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTWIDTH");
                rowData[15] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTWEIGHT");
                rowData[16] = MapUtils.getAsStringIgnoreCase(row, "ISPACKED");
                rowData[16] = rowData[16].equals("1") ? "已组套" : "未组套";
                rowData[17] = MapUtils.getAsStringIgnoreCase(row, "PACKEDTIME");
                rowData[18] = MapUtils.getAsStringIgnoreCase(row, "PACKUSERNAME");
                rowData[19] = MapUtils.getAsStringIgnoreCase(row, "GROUPNAME");
                rowData[20] = MapUtils.getAsStringIgnoreCase(row, "GROUPLEADER");
                rowData[21] = MapUtils.getAsStringIgnoreCase(row, "DEVICECODE");
                rowData[22] = MapUtils.getAsStringIgnoreCase(row, "WORKSHOP");
                rowData[23] = MapUtils.getAsStringIgnoreCase(row, "FARBICMODEL");
                rowData[24] = MapUtils.getAsStringIgnoreCase(row, "PRINTTIME");
                rowData[25] = MapUtils.getAsStringIgnoreCase(row, "PRINTUSER");
                rowData[26] = MapUtils.getAsStringIgnoreCase(row, "PRINTTYPE");
                rowData[27] = MapUtils.getAsStringIgnoreCase(row, "REPRINTREASON");
                rowData[28] = MapUtils.getAsStringIgnoreCase(row, "EXTRAPRINTTIME");
                rowData[29] = MapUtils.getAsStringIgnoreCase(row, "EXTRAPRINTUSER");
                rowData[30] = MapUtils.getAsStringIgnoreCase(row, "EXTRAPRINTREASON");

                data.add(rowData);
            }
            excel.setData(data);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return excel;
    }
}
