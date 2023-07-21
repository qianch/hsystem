<!--
作者:宋黎明
日期:2016-10-8 13:36:52
页面:非套材工艺BOM增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/uploadify/uploadify.css">
<script type="text/javascript" src="<%=basePath%>/resources/uploadify/jquery.uploadify.min.js"></script>
<style type="text/css">
    .file {
        color: black;
        height: 16px;
        line-height: 16px;
        display: inline-block;
        font-size: 12px;
        margin: 0;
        padding: 0;
        width: 100px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .file_remove {
        color: red;
        height: 16px;
        line-height: 16px;
        display: inline-block;
        font-size: 12px;
        font-family: cursive;
        margin: 0;
        padding: 0;
        width: 15px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .upload_file_list {
        margin: 2px;
        padding: 2px;
        display: inline-block;
    }

    .upload_file_list a {
        text-decoration: none;
    }
</style>
<script type="text/javascript">
</script>
<div>
    <!--非套材工艺BOM表单-->
    <form id="fTc_BomForm" method="post" ajax="true" action="<%=basePath %>fTc_Bom/${empty fTc_Bom.id ?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${fTc_Bom.id}"/>
        <!--客户信息ID-->
        <input type="hidden" id="ftcProcBomConsumerId" name="ftcProcBomConsumerId"
               value="${fTc_Bom.ftcProcBomConsumerId}"/>
        <table width="100%">
            <tr>
                <td class="title">BOM名称:</td>
                <!--BOM名称-->
                <td>
                    <input type="text" id="ftcProcBomName" name="ftcProcBomName" value="${fTc_Bom.ftcProcBomName}"
                           style="width: 100%" class="easyui-textbox" required="true"
                           data-options="validType:['length[0,100]']">
                </td>
            </tr>
            <tr>
                <td class="title">BOM代码:</td>
                <!--BOM代码-->
                <td>
                    <input type="text" id="ftcProcBomCode" name="ftcProcBomCode" value="${fTc_Bom.ftcProcBomCode}"
                           style="width: 100%" class="easyui-textbox" required="true"
                           data-options="validType:['length[0,100]']">
                </td>
            </tr>
            <tr>
                <td class="title">客户信息:</td>
                <!--客户信息-->
                <td>
                    <%-- <input type="text" id="ftcProcBomConsumerId" name="ftcProcBomConsumerId" value="${fTc_Bom.ftcProcBomConsumerId}" class="textbox" required="true" > --%>
                    <input id="ftcProcBomConsumer" class="easyui-searchbox" value="${consumer.consumerName}"
                           style="width: 100%" editable="false" required="true" data-options="searcher:ChooseConsumer">
                </td>
            </tr>
            <tr>
                <td class="title">试样工艺:</td>
                <!--客户信息-->
                <td>
                    <%-- <input type="text" id="ftcProcBomConsumerId" name="ftcProcBomConsumerId" value="${fTc_Bom.ftcProcBomConsumerId}" class="textbox" required="true" > --%>
                    <input type="text" id="isTestPro" name="isTestPro" value="${fTc_Bom.isTestPro}"
                           class="easyui-combobox" style="width: 100%" panelHeight="auto" editable="false"
                           required="true" data-options="data: [
	                        {value:'1',text:'常规产品'},
	                        {value:'-1',text:'变更试样'},
	                        {value:'2',text:'新品试样'}
                    	]">
                </td>
            </tr>
            <c:if test="${empty fTc_Bom.id}">
                <tr>
                    <td class="title">版本号:</td>
                    <!--版本号-->
                    <td>
                        <input type="text" id="ftcProcBomVersionCode" name="ftcProcBomVersionCode" style="width: 100%"
                               class="easyui-textbox" data-options="validType:['length[0,100]']">
                    </td>
                </tr>
                <tr>
                    <td class="title">客户版本号:</td>
                    <!--客户版本号-->
                    <td>
                        <input type="text" data-options="validType:'length[0,100]'" id="ftcConsumerVersionCode"
                               name="ftcConsumerVersionCode" class="easyui-textbox" style="width: 100%">
                    </td>
                </tr>
                <tr>
                    <td class="title">工艺文件上传:</td>
                    <!--工艺文件上传-->
                    <td>
                        <input type="hidden" name="fileId" id="fileId" value="">
                        <div>
                            <div id="files"></div>
                            <hr style="border:none;">
                            <input style="width:100px;" type="file" name="file_upload" id="file_upload"/>
                        </div>
                    </td>
                </tr>
            </c:if>
        </table>
    </form>
</div>
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
    const fp = "${filePath}";
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
            fileTypeExts: '*.xls;*.xlsx;',
            fileSizeLimit: '50MB',
            formData: {path: fp},
            removeTimeout: 1,
// 		uploadLimit		:	1,			//上传文件的数量
            queueSizeLimit: 1,
            multi: false,		//设置是否允许一次选择多个文件，true为允许，false不允许
            onUploadSuccess: function (file, data, response) {
                data = JSON.parse(data);
                if (data == null || data == "") {
                    Tip.warn("上传Excel文件错误");
                    return;
                }
                if (data.error != null) {
                    Tip.warn(data.error);
                    return;
                }
                $("#ftcProcBomVersionCode").textbox({required: true});
                $("#fileId").val(data.ID);
                $("#file_upload").uploadify('disable', true);
                $("#files").append("<span id=\"list_" + data.ID + "\" class=\"upload_file_list\"><span class=\"icon-icon " + (icons[data.SUFFIX] == null ? 'icon-file' : icons[data.SUFFIX]) + "\"></span><a id=\"file_" + data.ID + "\" title=\"" + data.FILENAME + "\" class=\"file\" href=\"<%=basePath%>file/download?id=" + data.ID + "&md5=" + data.MD5 + "\">" + data.FILENAME + "</a><a title=\"删除\" class=\"file_remove\" href=\"javascript:void(0)\" onclick=\"deleteFile(" + data.ID + ",'" + data.MD5 + "')\">删</a></span>");
            }
        });
        list();
    });

    function list() {
        $.ajax({
            url: fpath + "file/list",
            type: "post",
            data: {path: fp},
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
                    $("#fileId").val('');
                    $("#ftcProcBomVersionCode").textbox({required: false});
                    $("#file_upload").uploadify('disable', false);

                    $("#list_" + id).hide(500, function () {
                        $(this).remove();
                    });
                }
            });
        }, "确认删除?");
    }
</script>