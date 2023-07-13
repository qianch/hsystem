<!--
作者:高飞
日期:2016-11-6 10:22:52
页面:PDA终端版本增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
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

    .uploadify {
        margin-left: 10px;
    }
</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--PDA终端版本表单-->
    <form id="appVersionForm" method="post" ajax="true"
          action="<%=basePath %>appVersion/${empty appVersion.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${appVersion.id}"/>
        <input type="hidden" id="isLatest" name="isLatest" value="1">
        <table width="100%">
            <tr>
                <td class="title">版本号:</td>
                <!--版本号-->
                <td><input type="text" id="version" name="version" value="${appVersion.version}" class="easyui-textbox"
                           required="true"></td>
            </tr>
            <tr>
                <td class="title">版本说明:</td>
                <!--路径-->
                <td>
                    <textarea name="versionMemo" maxlength="100"
                              style="width:100%;height:50px;resize:none;border-radius:5px;margin-left:5px;"></textarea>
                </td>
            </tr>
            <tr>
                <td class="title">路径:</td>
                <!--路径-->
                <td>
                    <input type="hidden" style="width:450px;" id="path" name="path" value="${appVersion.path}">
                    <div id="tips">请上传文件</div>
                </td>
            </tr>
            <tr style="height: 140px">
                <td class="title">上传APK文件:</td>
                <!--路径-->
                <td>
                    <div style="text-align: center;">
                        <div id="files"></div>
                        <hr style="border:none;">
                        <input style="width:100px;" type="file" id="file_upload"/>
                    </div>
                </td>
            </tr>
        </table>
    </form>

    <script type="text/javascript">
        $("#file_upload").uploadify({
            buttonText: '选择文件',
            buttonClass: '',
            auto: true,
            height: 30,
            swf: path + 'resources/uploadify/uploadify.swf',
            uploader: path + '/file/upload',
            width: 120,
            fileObjName: 'file',
            fileTypeExts: '*.apk',
            fileSizeLimit: '50MB',
            formData: {path: "app"},
            removeTimeout: 1,
            onUploadSuccess: function (file, data, response) {
                data = JSON.parse(data);
                if (Tip.hasError(data)) {
                    return;
                }
                $("#path").val("file/download?id=" + data.ID + "&md5=" + data.MD5);
                $("#tips").html("<a href='<%=basePath%>file/download?id=" + data.ID + "&md5=" + data.MD5 + "'><%=basePath%>file/download?id=" + data.ID + "&md5=" + data.MD5 + "</a>");
            }
        });
    </script>
</div>