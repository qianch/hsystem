<%-- 上传页面，包括文件显示--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String fpath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/";
%>
<link rel="stylesheet" type="text/css" href="<%=fpath%>/resources/uploadify/uploadify.css">
<script type="text/javascript" src="<%=fpath%>/resources/uploadify/jquery.uploadify.min.js"></script>
<style>
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

    var fpath="<%=fpath%>";
	$(function() {
		$("#file_upload").uploadify({
			buttonText		: 	'选择文件',
			buttonClass		:	'',
			auto			:	true,
			height			: 	30,
			swf 			: 	fpath + 'resources/uploadify/uploadify.swf',
			uploader 		: 	fpath + '/excel/import',
			width 			:	120,
			fileObjName 	:	'file',
			fileTypeExts	:	'*.xls;*.xlsx;',
			fileSizeLimit	:	'100MB',
			formData		:	{handler:"com.bluebirdme.mes.platform.controller.DepartmentImport"},
			removeTimeout	:	1,
			multi			: 	false,
			onUploadSuccess : 	function(file, data, response) {
				data=JSON.parse(data);
				if(Tip.hasError(data)){
					return;
				}
				Tip.success("导入成功");
			}
		});
	});
	
</script>

<div>
	<input style="width:100px;" type="file" name="file_upload" id="file_upload" />
	<hr style="border:none;">
</div>
