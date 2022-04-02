<!--
	作者:徐秦冬
	日期:2017-12-4 16:00:00
	页面:非套材包材bom增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
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
	width:100px;
	white-space:nowrap;overflow:hidden;text-overflow:ellipsis;
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
	width:15px;
	white-space:nowrap;overflow:hidden;text-overflow:ellipsis;
}

.upload_file_list {
	margin: 2px;
	padding: 2px;
	display: inline-block;
}

.upload_file_list a {
	text-decoration:none;
}
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--非套材包材bom表单-->
	<form id="ftcBcBomForm" method="post" ajax="true" action="<%=basePath %>bcBom/${empty ftcBcBom.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${ftcBcBom.id}" />
		<input type="hidden" name="level" value="${ftcBcBom.level}"/>
		<input type="hidden" name="pid" value="${ftcBcBom.pid}"/>
		<table width="100%">
				<tr>
					<c:choose>
						<c:when test="${ftcBcBom.level eq 1}">
							<td class="title">产品规格:</td>
						</c:when>
						<c:when test="${ftcBcBom.level eq 2}">
							<td class="title">厂内名称:</td>
						</c:when>
						<c:when test="${ftcBcBom.level eq 3}">
							<td class="title">产品包装名称:</td>
						</c:when>
					</c:choose>
					<td>
						<input type="text" style="width:360px" id="name" name="name" value="${ftcBcBom.name}" class="easyui-textbox" required="true" data-options="validType:['length[1,100]']">
					</td>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${ftcBcBom.level eq 1}">
							<td class="title">包装标准代码:</td>
						</c:when>
						<c:when test="${ftcBcBom.level eq 2}">
							<td class="title">二级包装工艺代码:</td>
						</c:when>
						<c:when test="${ftcBcBom.level eq 3}">
							<td class="title">三级包装工艺代码:</td>
						</c:when>
					</c:choose>
					<td>
						<input type="text" style="width:360px" id="code" name="code" value="${ftcBcBom.code}" class="easyui-textbox" required="true" data-options="validType:['length[1,100]']">
					</td>

				</tr>
				<c:if test="${ftcBcBom.level eq 2 }">
					<tr>
						<td class="title">工艺文件上传:</td>
						<!--工艺文件上传-->
						<td>
							<input type="hidden" name="fileId" id="fileId" value="">
							<div>
								<div id="files"></div>
								<hr style="border:none;">
								<input style="width:100px;" type="file" name="file_upload" id="file_upload" />
							</div>
						</td>
					</tr>
					<c:if test="${not empty ftcBcBom.id}">
					<tr>
						<td class="title"></td>
						<td style="color:red;">
							注意事项：工艺文件上传会新增三级包装和版本，但重复三级包装工艺代码和版本会忽略
						</td>
					</tr>
					</c:if>
				</c:if>
		</table>
	</form>
</div>
<script type="text/javascript">

var icons={"jpg":"icon-jpg","bmp":"icon-bmp","txt":"icon-txt","zip":"icon-zip","doc":"icon-doc","docx":"icon-docx","xls":"icon-xls","xlsx":"icon-xlsx","ppt":"icon-ppt","pptx":"icon-pptx","gif":"icon-gif","rar":"icon-rar","text":"icon-text"};
var fpath="<%=basePath%>";
var fp="${filePath}";
$(function() {
	$("#file_upload").uploadify({
		buttonText		: 	'选择文件',
		/* buttonImage		:	fpath+'resources/uploadify/upload_bg.png', */
		buttonClass		:	'',
		auto			:	true,
		height			: 	30,
		swf 			: 	fpath + 'resources/uploadify/uploadify.swf',
		uploader 		: 	fpath + '/file/upload',
		width 			:	120,
		fileObjName 	:	'file',
		fileTypeExts	:	'*.xls;*.xlsx;',
		fileSizeLimit	:	'50MB',
		formData		:	{path:fp},
		removeTimeout	:	1,
// 		uploadLimit		:	1,			//上传文件的数量
		queueSizeLimit		:	1,
		multi			:	false,		//设置是否允许一次选择多个文件，true为允许，false不允许
		onUploadSuccess : function(file, data, response) {
			data=JSON.parse(data);
			if(data == null || data == ""){
				Tip.warn("上传Excel文件错误");
				return;
			}
			if(data.error!=null){
				Tip.warn(data.error);
				return;
			}
			
			$("#fileId").val(data.ID);
				
			$("#file_upload").uploadify('disable',true);
			$("#files").append("<span id=\"list_"+data.ID+"\" class=\"upload_file_list\"><span class=\"icon-icon "+(icons[data.SUFFIX]==null?'icon-file':icons[data.SUFFIX])+"\"></span><a id=\"file_"+data.ID+"\" title=\""+data.FILENAME+"\" class=\"file\" href=\"<%=basePath%>file/download?id="+data.ID+"&md5="+data.MD5+"\">"+data.FILENAME+"</a><a title=\"删除\" class=\"file_remove\" href=\"javascript:void(0)\" onclick=\"deleteFile("+data.ID+",'"+data.MD5+"')\">删</a></span>");
		}
	});
	list();
});

function list(){
	$.ajax({
		url:fpath+"file/list",
		type:"post",
		data:{path:fp},
		dataType:"json",
		success:function(data){
			if(Tip.hasError(data)){
				return;
			}
			for(var i=0;i<data.length;i++){
				$("#files").append("<span id=\"list_"+data[i].ID+"\" class=\"upload_file_list\"><span class=\"icon-icon "+(icons[data[i].SUFFIX]==null?'icon-file':icons[data[i].SUFFIX])+"\"></span><a id=\"file_"+data[i].ID+"\" title=\""+data[i].FILENAME+"\" class=\"file\" href=\"<%=basePath%>file/download?id="+data[i].ID+"&md5="+data[i].MD5+"\">"+data[i].FILENAME+"</a><a title=\"删除\" class=\"file_remove\" href=\"javascript:void(0)\" onclick=\"deleteFile("+data[i].ID+",'"+data[i].MD5+"')\">删</a></span>");
			}
		}
	});
}

function deleteFile(id,md5){
	Dialog.confirm(function(){
		$.ajax({
			url : fpath + "file/delete",
			type : "POST",
			data:{id:id,md5:md5},
			dataType : "json",
			success : function(data) {
				if(Tip.hasError(data)){
					return;
				}
				Tip.success("删除成功");
				$("#fileId").val('');
				$("#file_upload").uploadify('disable',false);
				
				$("#list_"+id).hide(500,function(){
					$(this).remove();
				});
			}
		});
	},"确认删除?");
}
</script>