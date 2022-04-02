package com.bluebirdme.mes.platform.ueditor;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Controller
@RequestMapping({"/customUeditor"})
@Journal(name = "ueditController")
public class CustomEditorController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CustomEditorController.class);

    @ResponseBody
    @Journal(name = "ueditor自定义附件上传")
    @RequestMapping(value = {"uplaod"}, method = {RequestMethod.POST})
    public Object uploadImg(@RequestParam(value = "upfile", required = false) final MultipartFile file, final String path) throws Exception {
        final Map<String, Object> map = new HashMap();
        if (RuntimeVariable.UPLOAD_PATH == null) {
            RuntimeVariable.UPLOAD_PATH = this.request.getSession().getServletContext().getRealPath("/") + File.separator;
            final File uploadFile = new File(RuntimeVariable.UPLOAD_PATH);
            if (!uploadFile.exists()) {
                uploadFile.mkdirs();
            }
        }
        final String fileName = file.getOriginalFilename();
        final String fileUUIDName = UUID.randomUUID().toString();
        final Long size = file.getSize();
        final String suffix = (fileName.lastIndexOf(".") == -1) ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        String filePath = RuntimeVariable.UPLOAD_PATH + File.separator + "ueditor/attachmen" + File.separator;
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        filePath = filePath + fileUUIDName + "." + suffix;
        final File target = new File(filePath);
        logger.debug(filePath);
        final byte[] bytes = file.getBytes();
        FileCopyUtils.copy(bytes, target);
        filePath = "/img" + File.separator + "ueditor/attachmen" + File.separator + fileUUIDName + "." + suffix;
        map.put("state", "SUCCESS");
        map.put("url", filePath);
        map.put("size", size);
        map.put("original", file.getOriginalFilename());
        map.put("title", file.getName());
        map.put("type", file.getContentType());
        return map;
    }
}
