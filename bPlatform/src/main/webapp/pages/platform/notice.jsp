<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>系统公告</title>
	<%@ include file="../base/meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/ueditor/themes/default/css/ueditor.css">
	
	<script type="text/javascript" src="<%=basePath%>resources/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/ueditor/ueditor.all.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/platform/notice.js"></script>
	<script type="text/javascript">
		const userId=${userId};
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative;">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="delete" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="删除" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="doDelete()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="noticeFilter">
					标题名称：<input type="text" name="filter[t.name]" id="name" value="" like="true" class="easyui-textbox">
					内容:<input type="text" name="filter[t.content]" value="" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
							搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" title="" class="easyui-datagrid" url="<%=basePath%>notice/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
			<thead>
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="TITLE" width="12">标题</th>
					<th field="PUBLISHER1" width="10" formatter="formatterPublisher">发布者</th>
					<th field="INPUTTIME" width="10" sortable=true>发布时间</th>
					<th field="CONTENTTXT" width="20" formatter="showView">内容</th> 
				</tr>
			</thead>
		</table>
	</div>
</body>
<script type="text/javascript">
	function formatterPublisher(value){
		if(value==null){
			value="administrator";
			return value;
		}
		return value;
	}
	
	function GetLength (str) {
		let realLength = 0, len = str.length, charCode = -1;
		for (let i = 0; i < len; i++) {
	        charCode = str.charCodeAt(i);
	        if (charCode >= 0 && charCode <= 128) realLength += 1;
	        else realLength += 2;
	    }
	    return realLength;
	};

	function cutstr(str, len) {
		let str_length = 0;
		let str_len = 0;
		let str_cut = new String();
		str_len = str.length;
		let a;
		for (let i = 0; i < str_len; i++) {
			a = str.charAt(i);
			str_length++;
			if (escape(a).length > 4) {
				//中文字符的长度经编码之后大于4
				str_length++;
			}
			str_cut = str_cut.concat(a);
			if (str_length >= len) {
				str_cut = str_cut.concat(" ...");
				return str_cut;
			}
		}
	    //如果给定字符串小于指定长度，则返回源字符串；  
	    if (str_length < len) {
	        return str;
	    }
	}
	function showView(value,row,index){
		if(value!=undefined){
			if (GetLength(value) > 30) {
				const txt = cutstr(value, 30);
				value="<a id='notice"+row.ID+"' ondblclick='openView("+row.ID+")'>"+txt+"</a>";
		       return value; 
		     } 
		}
		return value;
	}
	
	function openView(id){
		const wid = Dialog.open("系统公告", 1000, 520, path + "notice/openview?id=" + id + "&userId=" + userId);
	}
</script>
</html>
