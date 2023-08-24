<!--
作者:sunli
日期:2017-11-29 14:55:51
页面:西门子裁片导入
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                if (data == null || data === "") {
                    Tip.warn("上传Excel文件错误");
                    return;
                }
                if (data.error != null) {
                    Tip.warn(data.error);
                    return;
                }
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
                for (let i = 0; i < data.length; i++) {
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
                    $("#file_upload").uploadify('disable', false);

                    $("#list_" + id).hide(500, function () {
                        $(this).remove();
                    });
                }
            });
        }, "确认删除?");
    }
</script>