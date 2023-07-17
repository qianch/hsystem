<%--加载文件列表页面--%>
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
        width: 150px;
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
                    $("#files").append("<span id=\"list_" + data[i].id + "\" class=\"upload_file_list\"><span class=\"icon-icon " + (icons[data[i].suffix] == null ? 'icon-file' : icons[data[i].suffix]) + "\"></span><a id=\"file_" + data[i].id + "\" onmouseout=\"closeTips()\" onmouseover=\"showTips(" + data[i].id + ",'" + data[i].fileName + "')\" class=\"file\" href=\"<%=basePath%>file/download?id=" + data[i].id + "&md5=" + data[i].md5 + "\">" + data[i].fileName + "</a></span>");
                }
            }
        });
    }

    let index = null;

    function showTips(id, name) {
        index = layer.tips(name, '#file_' + id, {
            tips: [1, 'rgb(0, 159, 149)'] //还可配置颜色
        });
    }

    function closeTips() {
        if (index != null) {
            layer.close(index);
        }
    }
</script>
<div>
    <div id="files">
    </div>
</div>
