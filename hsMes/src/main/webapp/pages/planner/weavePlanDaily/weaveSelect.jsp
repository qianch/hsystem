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
<script type="text/javascript">
    function formatterType(value, row) {
        if (value === 1) {
            return "大卷产品";
        }
        if (value === 2) {
            return "中卷产品";
        }
        if (value === 3) {
            return "小卷产品";
        }
        if (value === 4) {
            return "其他产品";
        }
    }

    const selectedPlans = ${empty selectedPlans?"[]":selectedPlans};
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
</script>
<div>
    <!--日计划表单-->
    <form style="height:97%" id="weaveDailyPlanForm" method="post" ajax="true"
          action="<%=basePath %>weaveDailyPlan/${empty weaveDailyPlan.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" id="id" name="id" value="${weaveDailyPlan.id}"/>
        <table id="dgg2" toolbar="#dgg_toolbar" idField="ID" width="100%" singleSelect="false" class="easyui-datagrid"
               url="<%=basePath %>weaveDailyPlan/findWeavePlan?filter[workShop]=${workShop}" pagination="false"
               rownumbers="true" fitColumns="false" fit="true"
               data-options="onLoadSuccess:ddg2LoadSuccess,onDblClickRow:onDgg2DbClick,rowStyler:planIsSettledStyler"
               remoteFilter="false" remoteSort="false">
            <thead frozen="true">
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PRODUCTTYPE" sortable="true" width="100" formatter="formatterType">产品属性</th>
                <th field="PLANCODE" sortable="true" width="130">生产计划单号</th>
                <th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
            </tr>
            </thead>
            <thead>
            <tr>
                <th field="PRODUCTWIDTH" sortable="true" width="80">门幅(mm)</th>
                <th field="PRODUCTLENGTH" sortable="true" width="80">卷长(m)</th>
                <th field="PRODUCTWEIGHT" sortable="true" width="80">卷重(kg)</th>
                <th field="SALESORDERSUBCODE" sortable="true" width="120">销售订单号</th>
                <th field="BATCHCODE" sortable="true" width="100">批次号</th>
                <th field="DELEVERYDATE" sortable="true" width="90" formatter="orderDateFormat">出货日期</th>
                <th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
                <th field="PROCESSBOMVERSION" width="80">工艺版本</th>
                <th field="BCBOMCODE" width="130" styler="bvStyler">包装代码</th>
                <th field="BCBOMVERSION" width="80">包装版本</th>
                <th field="PACKREQ" sortable="true" width="15">包装需求</th>
                <th field="PROCREQ" sortable="true" width="15">工艺需求</th>
                <!-- <th field="PRODUCTROLLLENGTH" sortable="true" width="80">总托数</th> -->
                <th field="TOTALTRAYCOUNT" sortable="true" width="80">总托数数</th>
                <th field="REQUIREMENTCOUNT" sortable="true" width="80">总重量</th>
                <th field="TOTALROLLCOUNT" sortable="true" width="80">总卷数</th>
                <th field="CONSUMERNAME" sortable="true" width="200">客户名称</th>
            </tr>
            </thead>
        </table>
    </form>
</div>