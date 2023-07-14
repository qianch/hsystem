<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>人员信息设置</title>
    <%@ include file="../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/zTree.ext.js"></script>
    <script type="text/javascript" src="<%=basePath %>resources/platform/user.js"></script>
    <style>
    </style>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" title="部门列表" style="width:200px;">
    <ul id="zTreeUser" class="ztree"></ul>
</div>
<div data-options="region:'center',border:false" style="overflow: auto;position: relative;">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="reset" name="ids"/>
            <jsp:param value="enable" name="ids"/>
            <jsp:param value="role" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-lock" name="icons"/>
            <jsp:param value="icon-disable" name="icons"/>
            <jsp:param value="icon-grant-role" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="重置密码" name="names"/>
            <jsp:param value="启用/禁用" name="names"/>
            <jsp:param value="角色分配" name="names"/>
            <jsp:param value="addUser()" name="funs"/>
            <jsp:param value="editUser()" name="funs"/>
            <jsp:param value="deleteUser()" name="funs"/>
            <jsp:param value="reset()" name="funs"/>
            <jsp:param value="enable()" name="funs"/>
            <jsp:param value="setRoles()" name="funs"/>
        </jsp:include>

        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="userFilter">
                <label class="panel-title">用户搜索：</label>
                <input type="hidden" id="did" name="filter[u.did]" in="true">
                用户名：<input type="text" id="userName" name="filter[u.userName]" like="true" value=""
                              class="easyui-textbox">
                登录名称：<input type="text" id="loginName" name="filter[u.loginName]" like="true" value=""
                                class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" fit="true" title="人员信息列表" class="easyui-datagrid" url="<%=basePath %>user/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="false">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="USERNAME" width="15">姓名</th>
            <th field="USERCODE" width="15">编号</th>
            <th field="LOGINNAME" width="15">登录帐号</th>
            <th field="SEX" width="10" data-options="field:'sex',formatter:sexFormatter">性别</th>
            <th field="PHONE" width="15">电话</th>
            <th field="EMAIL" width="20">邮箱</th>
            <th field="NAME" width="20">所在部门</th>
            <th field="STATUS" width="20" data-options="field:'status',formatter:statusFormatter">状态</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>