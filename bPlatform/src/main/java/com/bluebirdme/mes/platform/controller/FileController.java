package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.platform.service.IAttachmentService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xdemo.superutil.j2se.MD5Utils;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonExclusion;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Controller
@RequestMapping({"/file"})
public class FileController {
    @Resource
    IAttachmentService attachmentService;

    public FileController() {
    }

    @NoAuth
    @ResponseBody
    @RequestMapping(value = {"list"}, method = {RequestMethod.POST})
    public String list(String path) {
        Map<String, Object> map = new HashMap<>();
        map.put("shortFilePath", path);
        List<Attachment> list = this.attachmentService.findListByMap(Attachment.class, map);
        return (new GsonBuilder()).serializeNulls().setExclusionStrategies(new GsonExclusion(new String[]{"filePath"})).create().toJson(list);
    }

    @NoLogin
    @RequestMapping(value = {"download"}, method = {RequestMethod.GET})
    public void download(Attachment attachment, HttpServletResponse response) throws IOException, BusinessException, NoSuchAlgorithmException, DigestException {
        Attachment attach = attachmentService.findById(Attachment.class, attachment.getId());
        if (attach == null) {
            throw new BusinessException("找不到请求的文件");
        }
        if (!attachment.getMd5().equalsIgnoreCase(attach.getMd5())) {
            throw new BusinessException("无效文件MD5");
        }
        File file = new File(attach.getFilePath());
        if (!file.exists()) {
            throw new BusinessException("文件已被删除(ID:" + attach.getId() + ")");
        }
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(attach.getFileName(), StandardCharsets.UTF_8));
        FileInputStream in = new FileInputStream(attach.getFilePath());
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    @NoAuth
    @ResponseBody
    @RequestMapping(value = {"upload"}, method = {RequestMethod.POST})
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, String path, HttpServletRequest request) throws IOException, BusinessException, NoSuchAlgorithmException, DigestException {
        if (StringUtils.isBlank(path)) {
            throw new BusinessException("必须指定上传路径");
        }
        if (RuntimeVariable.UPLOAD_PATH == null) {
            RuntimeVariable.UPLOAD_PATH = request.getSession().getServletContext().getRealPath("/") + File.separator;
            File _file = new File(RuntimeVariable.UPLOAD_PATH);
            if (!_file.exists()) {
                _file.mkdirs();
            }
        }
        String fileName = file.getOriginalFilename();
        String id = UUID.randomUUID().toString();
        Long size = file.getSize();
        String suffix = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        String filePath = RuntimeVariable.UPLOAD_PATH + File.separator + path + File.separator;
        if (!(new File(filePath)).exists()) {
            (new File(filePath)).mkdirs();
        }
        filePath = filePath + id + "." + suffix;
        File target = new File(filePath);
        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(target));
        String md5 = MD5Utils.getFileMD5(filePath);
        Attachment attachment = new Attachment();
        attachment.setFileName(fileName);
        attachment.setFilePath(filePath);
        attachment.setShortFilePath(path);
        attachment.setFileUUIDName(id);
        attachment.setFileSize(size);
        attachment.setSuffix(suffix);
        attachment.setMd5(md5);
        attachment.setUploadTime(new Date());
        this.attachmentService.save(attachment);
        return (new GsonBuilder()).serializeNulls().setExclusionStrategies(new GsonExclusion(new String[]{"filePath"})).create().toJson(attachment);
    }

    @Journal(name = "删除文件")
    @NoAuth
    @ResponseBody
    @RequestMapping(value = {"delete"}, method = {RequestMethod.POST})
    public String delete(Attachment attachment) throws IOException, BusinessException, NoSuchAlgorithmException, DigestException {
        Attachment _attachment = attachmentService.findById(Attachment.class, attachment.getId());
        if (_attachment == null) {
            throw new BusinessException("找不到请求的文件");
        }
        if (!attachment.getMd5().equalsIgnoreCase(_attachment.getMd5())) {
            throw new BusinessException("无效的文件MD5");
        }
        File file = new File(_attachment.getFilePath());
        if (file.exists()) {
            file.delete();
        }
        this.attachmentService.delete(_attachment);
        return "{}";
    }
}
