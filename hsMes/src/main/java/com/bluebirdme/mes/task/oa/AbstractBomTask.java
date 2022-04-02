package com.bluebirdme.mes.task.oa;

import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.platform.entity.Attachment;
import com.bluebirdme.mes.platform.service.IAttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.xdemo.superutil.j2se.MD5Utils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

public abstract class AbstractBomTask {
    @Resource
    IAttachmentService attachmentService;

    protected Long saveFile(File file) throws IOException, NoSuchAlgorithmException, DigestException, BusinessException {
        String path = file.getPath();
        if (StringUtils.isBlank(path)) {
            throw new BusinessException("必须指定上传路径");
        } else {
            String fileName = file.getName();
            String fileUUIDName = UUID.randomUUID().toString();
            Long size = file.length();
            String suffix = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
            String filePath = RuntimeVariable.UPLOAD_PATH + fileUUIDName + "\\";
            if (!(new File(filePath)).exists()) {
                (new File(filePath)).mkdirs();
            }

            filePath = filePath + fileUUIDName + "." + suffix;
            File target = new File(filePath);
            FileCopyUtils.copy(file, target);
            String md5 = MD5Utils.getFileMD5(filePath);
            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            attachment.setFilePath(filePath);
            attachment.setShortFilePath(path);
            attachment.setFileUUIDName(fileUUIDName);
            attachment.setFileSize(size);
            attachment.setSuffix(suffix);
            attachment.setMd5(md5);
            attachment.setUploadTime(new Date());
            this.attachmentService.save(new Object[]{attachment});
            return attachment.getId();
        }
    }
}
