<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<title>角色信息设置</title>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<div id="roletoolbar">
    <div>
        角色名称：<input type="text" name="filter[name]" id="name" value="" editable="true" class="easyui-searchbox"
                        data-options="searcher:roleFilter">
    </div>
</div>
<table id="roleTable" idField="ID" singleSelect="false" class="easyui-datalist" textField="NAME" valueField="ID"
       lines="true" checkbox="true" url="<%=basePath%>role/list?all=1" toolbar="#roletoolbar" rownumbers="true"
       fit="true" data-options="onLoadSuccess:onLoadSuccess">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="NAME">角色名称</th>
    </tr>
    </thead>
</table>
<script>
    const roles = "${roles}";

    function roleFilter() {
        $('#roleTable').datagrid('reload', {
            "filter[name]": "like:" + $("#name").val()
        });
    }

    function onLoadSuccess(data) {
        if (roles !== "") {
            var roleArray = roles.split(",");
            for (var i = 0; i < roleArray.length; i++) {
                $('#roleTable').datagrid("selectRecord", roleArray[i]);
            }
        }
    }

    /**
     * 获取选中的角色
     */
    function getSelected() {
        const rows = $('#roleTable').datagrid('getSelections');
        const ids = [];
        if (rows.length === 0) {
            return ids;
        }
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        return ids;
    }
</script>