<%-- 上传页面，包括文件显示--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/uploadify/uploadify.css">
<script type="text/javascript" src="<%=basePath%>/resources/uploadify/jquery.uploadify.min.js"></script>
<style>
    .file {
        color: blue;
        height: 16px;
        line-height: 16px;
        display: inline-block;
        font-size: 16px;
        margin: 0;
        padding: 0;
        width: 50px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .file_remove {
        color: rgb(174, 171, 173);
        font-size: 12px;
    }

    .upload_file_list {
        border-bottom: 1px solid rgb(0, 0, 255);
        margin: 2px;
        padding: 2px;
        display: inline-block;
    }

    .upload_file_list a {
        text-decoration: none;
    }
</style>
<script type="text/javascript">
    const icons = {
        "jpg": "icon-jpg",
        "bmp": "icon-bmp",
        "txt": "icon-txt",
        "zip": "icon-zip",
        "doc": "icon-doc",
        "docx": "icon-docx",
        "xls": "icon-xls",
        "xlsx": "icon-xlsx",
        "ppt": "icon-ppt",
        "pptx": "icon-pptx",
        "gif": "icon-gif",
        "rar": "icon-rar",
        "text": "icon-text"
    };
    const fpath = "<%=basePath%>";
    $(function () {
        $("#file_upload").uploadify({
            buttonText: '选择文件',
            /* buttonImage		:	fpath+'resources/uploadify/upload_bg.png', */
            buttonClass: '',
            auto: true,
            height: 30,
            swf: fpath + 'resources/uploadify/uploadify.swf',
            uploader: fpath + '/file/upload',
            width: 120,
            fileObjName: 'file',
            fileTypeExts: '*.gif; *.jpg; *.png;*.zip;*.doc;*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.bmp;*.txt;*.rar;*.zip;',
            fileSizeLimit: '100GB',
            formData: {path: "<%=request.getParameter("path")%>"},
            removeTimeout: 1,
            onUploadSuccess: function (file, data, response) {
                data = JSON.parse(data);
                $("#files").append("<span id=\"list_" + data.ID + "\" class=\"upload_file_list\"><span class=\"icon-icon " + (icons[data.SUFFIX] == null ? 'icon-file' : icons[data.SUFFIX]) + "\"></span><a id=\"file_" + data.ID + "\" title=\"" + data.FILENAME + "\" class=\"file\" href=\"<%=basePath%>file/download?id=" + data.ID + "&md5=" + data.MD5 + "\">" + data.FILENAME + "</a><a title=\"删除\" class=\"file_remove\" href=\"javascript:void(0)\" onclick=\"deleteFile(" + data.ID + ",'" + data.MD5 + "')\">删</a></span>");
            }
        });
        list();
    });

    function list() {
        $.ajax({
            url: fpath + "file/list",
            type: "post",
            data: {path: "<%=request.getParameter("path")%>"},
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                for (var i = 0; i < data.length; i++) {
                    $("#files").append("<span id=\"list_" + data[i].ID + "\" class=\"upload_file_list\"><span class=\"icon-icon " + (icons[data[i].SUFFIX] == null ? 'icon-file' : icons[data[i].SUFFIX]) + "\"></span><a id=\"file_" + data[i].ID + "\" title=\"" + data[i].FILENAME + "\" class=\"file\" href=\"<%=basePath%>file/download?id=" + data[i].ID + "&md5=" + data[i].MD5 + "\">" + data[i].FILENAME + "</a><a title=\"删除\" class=\"file_remove\" href=\"javascript:void(0)\" onclick=\"deleteFile(" + data[i].ID + ",'" + data[i].MD5 + "')\">删</a></span>");
                }
            }
        });
    }

    function deleteFile(id, md5) {
        Dialog.confirm(function () {
            $.ajax({
                url: fpath + "file/delete",
                type: "POST",
                data: {id: id, md5: md5},
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("删除成功");
                    $("#list_" + id).hide(500, function () {
                        $(this).remove();
                    });
                }
            });
        }, "确认删除?");
    }
</script>
<div>
    <div id="files">
    </div>
    <hr style="border:none;">
    <input style="width:100px;" type="file" name="file_upload" id="file_upload"/>
</div>
