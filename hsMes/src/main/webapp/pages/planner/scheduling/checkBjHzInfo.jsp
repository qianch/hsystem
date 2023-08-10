<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
        <table id="weight_dg1234" singleSelect="true" title="" class="easyui-datagrid" toolbar="#category_toolbar"
               url="<%=basePath %>planner/weavePlan/viewBjhzInfo?id=${id}"
               pagination="fasle" rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="DEVCODE" sortable="true" width="60">设备代码</th>
                <th field="YX" sortable="true" width="180">叶型名称</th>
                <th field="PARTNAME" sortable="true" width="100">部件名称</th>
                <th field="caozuo" width="90" formatter="formatterDetail">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>

