<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>角色信息设置</title>
    <%@ include  file="../base/meta.jsp" %>
    <script type="text/javascript" src="<%=basePath %>/resources/platform/role.js"></script>
  </head>
  
<body style="margin:0;">
	<div id="toolbar" >
        <jsp:include page="../base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="delete" name="ids"/>
				<jsp:param value="permission" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="icon-lock" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="删除" name="names"/>
				<jsp:param value="权限分配" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="deleteRole()" name="funs"/>
				<jsp:param value="permission()" name="funs"/>
			</jsp:include>
        	<div>
				<form action="#" id="roleFilter">
					角色名称：<input type="text" name="filter[name]" id="name" like="true" value="" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
							搜索
					</a>
				</form>
			</div>
    </div>
    <table id="dg" fit="true" title="" singleSelect="true" class="easyui-datagrid"  url="<%=basePath %>role/list"  toolbar="#toolbar" pagination="true"
      rownumbers="true" fitColumns="true" singleSelect="false">
        <thead>
            <tr>
            	<th field="ID" checkbox=true></th>
                <th field="NAME" width="10">角色名称</th>
            </tr>
        </thead>
    </table>
</body>
</html>
