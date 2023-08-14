<!--
作者:肖文彬
日期:2016-10-18 13:38:47
页面:编织计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
    textarea {
        height: 50px;
        resize: none;
        padding: 2px;
        border: none;
    }
</style>
<script type="text/javascript">
    let deviceDgData = ${empty deviceDg?"[]":deviceDg};
</script>
<div>
    <!--编织计划表单-->
    <form id="weavePlanForm" method="post" ajax="true"
          action="<%=basePath %>weavePlan/${empty weavePlan.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${weavePlan.id}"/>
        <table width="100%">
            <tr>
                <td class="title">车间:</td>
                <!--车间-->
                <td>
                    <input type="text" id="workShop" name="workShop"
                           value="${weaveDailyPlan.workShop}" ${empty weaveDailyPlan.workShop?"":"readonly"}
                           class="easyui-combobox" required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave'">
                </td>
                <td class="title">产品属性:</td>
                <!--产品属性-->
                <td><input type="text" id="productType" name="productType" value="${weavePlan.productType}"
                           class="easyui-combobox" required="true"
                           data-options="data: [
		                        {value:'1',text:'大卷产品'},
		                        {value:'2',text:'中卷产品'},
		                        {value:'3',text:'小卷产品'},
		                        {value:'4',text:'其他产品'}]"></td>
                <td class="title">总托数:</td>
                <td><input type="text" id="productTrayCount" name="totalTrayCount" value="${weavePlan.totalTrayCount}"
                           readonly="true" precision="0" min="1" class="easyui-numberbox"></td>
            </tr>
            <tr>
                <td class="title">总卷数:</td>
                <td><input type="text" id="count" name="totalRollCount"
                           value="${weavePlan.totalRollCount}" ${empty weavePlan.totalRollCount?"":"readonly"} min="1"
                           class="easyui-numberbox" precision="0" required="true"></td>
                <td class="title">总重量:</td>
                <td><input type="text" id="weight" name="requirementCount"
                           value="${weavePlan.requirementCount}" ${empty weavePlan.requirementCount?"":"readonly"}
                           class="easyui-numberbox" min="1" required="true"></td>
                <td class="title">备注:</td>
                <td><textarea id="_comment" name="_comment" style="width:99%;" placeholder="最多输入1000字"></textarea>
                </td>
            </tr>
        </table>
        <!-- <div id="cal" style="padding: 5px; background: #fafafa; border-bottom: 1px solid #dddddd; text-align: center;"></div>
        <div id="deviceToolbar" class="datagrid-toolbar">
            <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="ChooseDevice()" id="addDevice" iconCls="icon-add">增加机台</a>
            <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="deleteDevice()" id="deleteDevice" iconCls="icon-remove">移除机台</a>
            <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="syncDevice()" id="syncDevice" iconCls="platform-refresh1">同步机台</a>
        </div> -->
        <!-- <div class="easyui-tabs" id="deviceTabs" style="width:100%;height:auto" data-options="onBeforeClose:doCloseTab"></div> -->
        <!-- <table class="easyui-datagrid" width="100%" fitColumns="true" rownumbers="true" pagination="false" data-options="onDblClickRow:ChooseDevice">

        </table> -->
    </form>
</div>

<script type="text/javascript" charset="utf-8">
    deviceWindow = Dialog.open("选择机台信息", 850, 450, path + "planner/weavePlan/device", [EasyUI.window.button("icon-save", "确认", function () {
        const rs = EasyUI.grid.getSelections("_common_device_dg");
        for (let i = 0; i < rs.length; i++) {
            const devices = $("#" + dg).datagrid("getRows");
            repeat = false;
            for (var j = 0; j < devices.length; j++) {
                if (devices[j].deviceId === rs[i].ID) {
                    repeat = true;
                }
            }
            if (!repeat) {
                row = {DEVICEID: rs[i].ID, DEVICENAME: rs[i].DEVICENAME, DEVICECODE: rs[i].DEVICECODE, PRODUCECOUNT: 1};
                $("#" + dg).datagrid("appendRow", row);
            }
        }
        Dialog.close(deviceWindow);
    }), EasyUI.window.button("icon-cancel", "取消", function () {
        Dialog.close(deviceWindow);
    })], function () {

    })
</script>
