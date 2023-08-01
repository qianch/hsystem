<!--
作者:肖文彬
日期:2016-11-24 11:02:50
页面:日计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
</style>
<script type="text/javascript">
    let selectedPlans =${empty selectedPlans?"[]":selectedPlans};
    let boolean = false;

    function enableDgFilter() {
        if (!boolean) {
            $("#dgg2").datagrid('enableFilter');
        }
        boolean = true;
    }

    function ddg2LoadSuccess(data) {
        enableDgFilter();
        const rs = $("#dgg").datagrid("getRows");
        for (let i = 0; i < rs.length; i++) {
            $("#dgg2").datagrid("selectRecord", rs[i].ID);
        }
    }

    function filter() {
        EasyUI.grid.search("dgg2", "cutDailyPlanForm");
    }
</script>
<div data-options="region:'center',border:false" style="position: relative; height: 100%">
    <div id="dgg_toolbar">
        <!--日计划表单-->
        <form id="cutDailyPlanForm" method="post" ajax="true" autocomplete="off" autoSearchFunction="false">
            批次号：<input type="text" id="batchCode" name="filter[batchCode]" like="true" class="easyui-textbox">
            销售订单号：<input type="text" id="salesOrderCode" name="filter[salesOrderCode]" like="true"
                              class="easyui-textbox">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small"
               iconcls="icon-search" onclick="filter()"> 搜 索 </a>&nbsp;&nbsp;
        </form>
    </div>
    <%--		<table id="dgg2" toolbar="#dgg_toolbar" idField="ID" singleSelect="false"--%>
    <%--			class="easyui-datagrid" url="<%=basePath %>cutDailyPlan/findCutPlan" width="100%" pagination="true" fit="true"  rownumbers="true"  fitColumns="true"--%>
    <%--			data-options="onLoadSuccess:ddg2LoadSuccess,onDblClickRow:onDgg2DbClick,rowStyler:planIsSettledStyler">--%>
    <table id="dgg2" toolbar="#dgg_toolbar" idField="ID" singleSelect="false"
           class="easyui-datagrid" url="<%=basePath %>cutDailyPlan/findCutPlan" pagination="true" rownumbers="true"
           fitColumns="false" fit="true" remoteSort="true">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <!-- <th field="PRODUCTTYPE" sortable="true" width="80" formatter="formatterType">产品属性</th> -->
            <th field="PLANCODE" sortable="true" width="130">生产计划单号</th>
            <th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="SALESORDERCODE" sortable="true" width="120">销售订单号</th>
            <th field="BATCHCODE" sortable="true" width="100">批次号</th>
            <th field="DELEVERYDATE" sortable="true" width="90" formatter="orderDateFormat">出货日期</th>
            <th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
            <th field="PROCESSBOMVERSION" width="80">工艺版本</th>
            <th field="BCBOMCODE" width="130" styler="bvStyler">包装代码</th>
            <th field="BCBOMVERSION" width="80">包装版本</th>
            <th field="REQUIREMENTCOUNT" sortable="true" width="80">总套数</th>
            <th field="CONSUMERNAME" sortable="true" width="200">客户名称</th>
        </tr>
        </thead>
    </table>
</div>