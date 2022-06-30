package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelExportHandler;
import com.bluebirdme.mes.core.excel.ExcelImportHandler;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.UUID;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Controller
@RequestMapping({"/excel"})
public class ExcelController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    public ExcelController() {
    }

    @NoAuth
    @RequestMapping({"template/{templateName}/{columns}"})
    public void template(@PathVariable String templateName, @PathVariable String[] columns) {
        try {
            Workbook wb = new SXSSFWorkbook();
            Sheet sheet;
            Row row;
            Cell cell;
            sheet = wb.createSheet();
            wb.setSheetName(0, templateName);
            row = sheet.createRow(0);
            CellStyle cs = wb.createCellStyle();
            cs.setDataFormat(wb.createDataFormat().getFormat("@"));
            for (int i = 0; i < columns.length; ++i) {
                sheet.setDefaultColumnStyle(i, cs);
                cell = row.createCell(i);
                cell.setCellValue(columns[i]);
            }
            download(wb, templateName);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    @NoAuth
    @RequestMapping({"/upload/{handler}/"})
    public ModelAndView upload(@PathVariable String handler) throws ClassNotFoundException {
        return (new ModelAndView("platform/excel")).addObject("handler", handler);
    }

    @Journal(name = "导入Excel文件", logType = LogType.CONSOLE)
    @NoAuth
    @ResponseBody
    @RequestMapping(value = {"/import"}, method = {RequestMethod.POST})
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, String handler) throws Exception {
        if (RuntimeVariable.UPLOAD_PATH == null) {
            RuntimeVariable.UPLOAD_PATH = this.request.getSession().getServletContext().getRealPath("/") + File.separator + "upload";
        }

        File uploadFile = new File(RuntimeVariable.UPLOAD_PATH);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        String id = UUID.randomUUID().toString();
        String suffix = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        String filePath = RuntimeVariable.UPLOAD_PATH + File.separator;
        if (!(new File(filePath)).exists()) {
            (new File(filePath)).mkdirs();
        }

        filePath = filePath + id + "." + suffix;
        File target = new File(filePath);
        byte[] bytes = file.getBytes();
        FileCopyUtils.copy(bytes, target);
        ExcelImportHandler xlsxImport = (ExcelImportHandler) Class.forName(handler).newInstance();
        ExcelContent xlsxData = xlsxImport.getContent(filePath);
        ExcelImportMessage msg = xlsxImport.check(xlsxData);
        if (msg.hasError()) {
            return this.ajaxError(msg.getMessage());
        }
        msg = xlsxImport.toDB(xlsxData);
        return msg.hasError() ? this.ajaxError(msg.getMessage()) : "{}";
    }

    @Journal(name = "导出当页数据至Excel文件", logType = LogType.CONSOLE)
    @NoAuth
    @ResponseBody
    @RequestMapping({"/export/page/{id}/{excelName}/{handler}"})
    public void export(@PathVariable String[] id, @PathVariable String excelName, @PathVariable String handler) throws Exception {
        ExcelExportHandler export = (ExcelExportHandler) Class.forName(handler).newInstance();
        ExcelContent content = export.getContent(id);
        download(export.getExcel(content), excelName);
    }

    @Journal(name = "导出全部数据到Excel文件", logType = LogType.CONSOLE)
    @NoAuth
    @ResponseBody
    @RequestMapping({"/export/{excelName}/{handler}"})
    public void export(@PathVariable String excelName, @PathVariable String handler) throws Exception {
        ExcelExportHandler export = (ExcelExportHandler) Class.forName(handler).newInstance();
        ExcelContent content = export.getContent();
        download(export.getExcel(content), excelName);
    }

    @Journal(name = "根据条件导出数据到Excel文件", logType = LogType.CONSOLE)
    @NoAuth
    @ResponseBody
    @RequestMapping(value = {"/export/{excelName}/{handler}/filter"}, method = {RequestMethod.GET})
    public void export(@PathVariable String excelName, @PathVariable String handler, Filter filter) throws Exception {
        ExcelExportHandler export = (ExcelExportHandler) Class.forName(handler).newInstance();
        ExcelContent content = export.getContent(filter);
        download(export.getExcel(content), excelName);
    }

    public void download(Workbook wb, String excelName) throws Exception {
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(excelName.getBytes("gbk"), "iso8859-1") + ".xlsx");
        response.setContentType("application/msexcel");
        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
