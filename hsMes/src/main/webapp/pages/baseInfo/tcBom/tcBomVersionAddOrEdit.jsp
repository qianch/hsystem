<!--
	作者:肖文彬
	日期:2016-10-9 16:10:05
	页面:套材Bom版本增加或修改页面
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
	<!--套材Bom版本表单-->
	<form id="tcBomVersionForm" method="post" ajax="true" action="<%=basePath %>tcBomVersion/${empty tcBomVersion.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${tcBomVersion.id}" />
		<input type="hidden" id="tcProcBomId" name="tcProcBomId" value="${tcBom.id}"  >
		<table width="100%">
				<tr>
					<td class="title">版本号:</td>
					<!--版本号-->
					<td>
						<input type="text" data-options="required:true,validType:'length[1,100]'" id="tcProcBomVersionCode" name="tcProcBomVersionCode" value="${tcBomVersion.tcProcBomVersionCode}" class="easyui-textbox" style="width:100%;" >
					</td>
				</tr>
			    <tr>
					<td class="title">客户版本号:</td>
				<!--客户版本号-->
				<td>
					<input type="text" data-options="validType:'length[1,100]'" id="tcConsumerVersionCode" name="tcConsumerVersionCode" value="${tcBomVersion.tcConsumerVersionCode}" class="easyui-textbox" style="width:100%;" >
				</td>
			    </tr>
				<c:if test="${not empty tcBomVersion.id}">
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
					<tr>
					<td class="title"></td>
					<td style="color:red;">
						注意事项：工艺文件上传会覆盖(删除并新增)套材BOM部件
					</td>
				</tr>
				</c:if>
				<%-- <tr>
					<td class="title">是否启用:</td>
					<!--是否启用-->
					<td>
						<input type="text" id="tcProcBomVersionEnabled" name="tcProcBomVersionEnabled" value="${tcBomVersion.tcProcBomVersionEnabled}" class="easyui-combobox"  panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'启用'},
	                        {value:'-1',text:'未启用'}
                    	]"  style="width:100%;" >
					</td>
				</tr>
				<tr>
					<td class="title">是否默认:</td>
					<!--是否默认-->
					<td>
						<input type="text" id="tcProcBomVersionDefault" name="tcProcBomVersionDefault" value="${tcBomVersion.tcProcBomVersionDefault}" class="easyui-combobox"  panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'是'},
	                        {value:'-1',text:'否'}
                    	]"   style="width:100%;" >
					</td>
				</tr> --%>
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