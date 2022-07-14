/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.controller;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.btwManager.service.IBtwFileService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.sales.entity.Consumer;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * btw文件Controller
 *
 * @author 徐波
 * @Date 2016-11-26 23:01:34
 */
@Controller
@RequestMapping("/btwFile")
@Journal(name = "btw文件")
public class BtwFileController extends BaseController {
    // btw文件页面
    final String index = "btwManager/btwFile";
    final String addOrEdit = "btwManager/btwFileAddOrEdit";
    final String btwFileUploadUrl = "btwManager/btwFileUpload";

    @Resource
    IBtwFileService btwFileService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取btw文件列表信息")
    @RequestMapping("list")
    public String getBtwFile(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(btwFileService.findPageInfo(filter, page));
    }


    @Journal(name = "添加btw文件页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(BtwFile btwFile) {
        return new ModelAndView(addOrEdit, model.addAttribute("btwFile", btwFile));
    }

    @ResponseBody
    @Journal(name = "保存btw文件", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(BtwFile btwFile) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        return GsonTools.toJson(btwFileService.saveBtwFilePrints(btwFile, userId));
    }

    @Journal(name = "编辑btw文件页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(BtwFile btwFile) {
        btwFile = btwFileService.findById(BtwFile.class, btwFile.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("btwFile", btwFile));
    }

    @ResponseBody
    @Journal(name = "编辑btw文件", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(BtwFile btwFile) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        return GsonTools.toJson(btwFileService.saveBtwFilePrints(btwFile, userId));
    }

    @ResponseBody
    @Journal(name = "删除btw文件", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(String ids) throws Exception {
        String ids_temp[] = ids.split(",");
        for (String s : ids_temp) {
            BtwFile btwFile = btwFileService.findById(BtwFile.class, Long.parseLong(s));
            btwFile.setState(2);
            btwFileService.update(btwFile);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "判断是否有同名的文件", logType = LogType.DB)
    @RequestMapping(value = "fileExsit", method = RequestMethod.POST)
    public String fileExsit(String fileName, String consumerCode) throws Exception {
        String fileUrl = UPLOAD_PATH + consumerCode + "\\" + fileName;
        File f = new File(fileUrl);
        if (f.exists()) {
            return ajaxError("同名文件");
        }
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "添加用户模板", logType = LogType.DB)
    @RequestMapping(value = "test", method = RequestMethod.GET
    )
    public String test() throws Exception {
        List<Consumer> conList = btwFileService.findAll(Consumer.class);
        for (Consumer c : conList) {
            BtwFile btw = new BtwFile();
            btw.setConsumerId(c.getId());
            btw.setConsumerName(c.getConsumerName());
            btw.setUploadDate(new Date());
            btw.setUploadUser("administrator");
            System.out.println("保存" + c.getConsumerName());
            btwFileService.save(btw);
        }
        return ajaxSuccess();
    }

    public static String UPLOAD_PATH = new File(PathUtils.getClassPath()) + File.separator + "BtwFiles" + File.separator;

    @Journal(name = "上传文件")
    @NoAuth
    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, String path) throws IOException, BusinessException, NoSuchAlgorithmException, DigestException {
        if (UPLOAD_PATH == null) {
            UPLOAD_PATH = request.getSession().getServletContext().getRealPath("/") + File.separator;
            File _file = new File(UPLOAD_PATH);
            if (!_file.exists()) {
                _file.mkdirs();
            }
        }
        String fileName = file.getOriginalFilename();
        String filePath = UPLOAD_PATH + path + "\\";
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        filePath += fileName;
        System.out.println(filePath);
        File target = new File(filePath);
        byte[] bytes = file.getBytes();
        FileCopyUtils.copy(bytes, target);

        HashMap<String, Object> map = new HashMap<>();
        map.put("fileName", file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
        map.put("uploadUser", session.getAttribute(Constant.CURRENT_USER_ID));
        map.put("uploadDate", new Date());
        return GsonTools.toJson(map);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取卷代码列表信息")
    @RequestMapping({"queryBtwFile"})
    public String queryBtwFile(String weavePlanId, String type) throws Exception {
        return btwFileService.queryBtwFile(weavePlanId, type);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取卷代码列表信息含标准版")
    @RequestMapping({"queryBtwFilebyCustomerId"})
    public String queryBtwFilebyCustomerId(String customerId, String type) throws Exception {
        List<Map<String, Object>> combobox = btwFileService.getBtwFilebyCustomerId(customerId, type, true);
        return GsonTools.toJson(combobox);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取卷代码列表信息无标准版")
    @RequestMapping({"getBtwFilebyCustomerId"})
    public String getBtwFilebyCustomerId(String customerId, String type) throws Exception {
        List<Map<String, Object>> combobox = btwFileService.getBtwFilebyCustomerId(customerId, type, false);
        return GsonTools.toJson(combobox);
    }

    @Journal(name = "上传btw页面")
    @RequestMapping(value = "btwFileUpload", method = RequestMethod.GET)
    public ModelAndView btwFileUpload(long id) {
        BtwFile btwFile = btwFileService.findById(BtwFile.class, id);
        return new ModelAndView(btwFileUploadUrl, model.addAttribute("btwFile", btwFile));
    }


    @ResponseBody
    @Journal(name = "导入btw", logType = LogType.DB)
    @RequestMapping(value = "importbtwFileUpload")
    public String importbtwFileUpload(@RequestParam(value = "file") MultipartFile file, long btwFileId) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        return GsonTools.toJson(btwFileService.importbtwFileUpload(file, btwFileId, userId, request));
    }
}
