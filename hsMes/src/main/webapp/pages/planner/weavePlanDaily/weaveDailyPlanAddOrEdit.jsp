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

    const selectedPlans =${empty selectedPlans?"[]":selectedPlans};
</script>
<div>
    <!--日计划表单-->
    <form style="height:98%" id="weaveDailyPlanForm" method="post" ajax="true"
          action="<%=basePath %>weaveDailyPlan/${empty weaveDailyPlan.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" id="id" name="id" value="${weaveDailyPlan.id}"/>
        <table width="100%">
            <tr>
                <td class="title">时间:</td>
                <td>
                    <input type="text" id="planDate" name="planDate"
                           value="${weaveDailyPlan.planDate}" ${empty weaveDailyPlan.planDate?"":"readonly"}
                           class="easyui-datebox" required="true">
                </td>
                <td class="title">车间:</td>
                <td>
                    <input type="text" id="workShop" name="workShop"
                           value="${weaveDailyPlan.workShop}" ${empty weaveDailyPlan.workShop?"":"readonly"}
                           class="easyui-combobox" required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave'">
                </td>
            </tr>
        </table>
        <div>
            <div id="dgg_add_toolbar">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-add"
                   onclick="selectWeavePlan()">添加</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-edit"
                   onclick="editWeavePlan()">编辑</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-remove"
                   onclick="removeWeavePlan()">删除</a>
            </div>
        </div>
        <table id="dgg" toolbar="#dgg_add_toolbar" idField="ID" singleSelect="true"
               title="编织任务(双击行编辑产品属性、指定机台)"
               class="easyui-datagrid" width="100%" pagination="false" rownumbers="true" fitColumns="false"
               data-options="onDblClickRow:dbClickEditW">
            <thead frozen="true">
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="DELEVERYDATE" sortable="true" width="90" formatter="orderDateFormat">出货日期</th>
                <th field="PRODUCTTYPE" sortable="true" width="100" formatter="formatterType">产品属性</th>
                <th field="DEVICECODES" sortable="true" width="150">机台</th>
                <th field="PLANCODE" sortable="true" width="100">生产计划单号</th>
                <th field="SALESORDERSUBCODE" sortable="true" width="100">销售订单号</th>
                <th field="PRODUCTMODEL" sortable="true" width="100">产品规格</th>
                <th field="BATCHCODE" sortable="true" width="100">批次号</th>
            </tr>
            </thead>
            <thead>
            <tr>
                <th field="PROCESSBOMCODE" sortable="true" width="80">工艺代码</th>
                <th field="PROCESSBOMVERSION" sortable="true" width="80">工艺版本</th>
                <th field="BCBOMCODE" sortable="true" width="80">包装代码</th>
                <th field="BCBOMVERSION" sortable="true" width="80">包装版本</th>
                <th field="TOTALTRAYCOUNT" sortable="true" width="80">总托数</th>
                <th field="REQUIREMENTCOUNT" sortable="true" width="80">总重量</th>
                <th field="TOTALROLLCOUNT" sortable="true" width="80">总卷数</th>
                <th field="CONSUMERNAME" sortable="true" width="200">客户名称</th>
                <th field="COMMENT" sortable="true" width="100">备注</th>
            </tr>
            </thead>
        </table>
    </form>
</div>