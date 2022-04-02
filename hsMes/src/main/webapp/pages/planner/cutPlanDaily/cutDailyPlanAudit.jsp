<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/meta.jsp" %>
<style type="text/css">
    .title {
        width: 130
    }
</style>
<script type="text/javascript">
    var tcBomPartTreeUrl = path + "planner/cutPlan/findParts";

    function orderDateFormat(value, row, index) {
        if (value == undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    $(function () {
        var id = $("#id").val();
        JQ.ajaxPost(path + "cutDailyPlan/findC", {
            id: id
        }, function (data) {
            $('#dgg').datagrid("loadData", data);
        })

        $('#dgg').datagrid({
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                console.log(row);
                cutPlanId = row.ID;
                ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.treegrid({
                    rownumbers: true,
                    animate: true,
                    collapsible: true,
                    fitColumns: true,
                    url: tcBomPartTreeUrl + "?id=" + id,
                    method: 'get',
                    idField: 'text',
                    treeField: 'text',
                    showFooter: true,
                    columns: [[{
                        field: 'text',
                        title: '部件',
                        width: 4
                    }, {
                        field: 'userName',
                        title: '指定人员',
                        width: 15
                    }]]
                });
                $('#dgg').datagrid('fixDetailRowHeight', index);
            }
        });
    });
</script>
<div style="height:100%;width:100%">
    <div style="height:6%;">
        <!--日计划表单-->
        <form id="cutPlanForm" method="post" ajax="true"
              action="<%=basePath %>cutDailyPlan/${empty cutDailyPlan.id ?'add':'edit'}" autocomplete="off">
            <input type="hidden" id="id" name="id" value="${cutDailyPlan.id}"/>
            <table width="100%">
                <tr>
                    <td class="title">时间:</td>
                    <td>
                        <input type="text" id="planDate" name="planDate"
                               value="${cutDailyPlan.planDate}" ${empty cutDailyPlan.planDate?"":"readonly"}
                               class="easyui-datebox" required="true">
                    </td>
                    <td class="title">车间:</td>
                    <!--车间-->
                    <td>
                        <input type="text" id="workShop" name="workShop"
                               value="${cutDailyPlan.workShop}" ${empty cutDailyPlan.workShop?"":"readonly"}
                               class="easyui-combobox" required="true" data-options="data: [
		                        {value:'裁剪车间',text:'裁剪车间'}]">
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div style="height:95%;">
        <table id="dgg" idField="ID" singleSelect="false" title="裁剪计划列表"
               class="easyui-datagrid" url="" height="98%" width="100%" pagination="false" rownumbers="true"
               fitColumns="false" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <!-- <th field="PRODUCTTYPE" sortable="true" width="100" formatter="formatterType">产品属性</th> -->
                <th field="PLANCODE" sortable="true" width="130">生产计划单号</th>
                <th field="SALESORDERCODE" sortable="true" width="100">销售订单号</th>
                <th field="BATCHCODE" sortable="true" width="100">批次号</th>
                <th field="PRODUCTMODEL" sortable="true" width="100">产品规格</th>
                <th field="DELEVERYDATE" sortable="true" width="80"
                    formatter="orderDateFormat">出货日期
                </th>
                <th field="PROCESSBOMCODE" sortable="true" width="100">工艺代码</th>
                <th field="PROCESSBOMVERSION" sortable="true" width="100">工艺版本</th>
                <th field="BCBOMCODE" sortable="true" width="100">包装代码</th>
                <th field="BCBOMVERSION" sortable="true" width="100">包装版本</th>
                <th field="REQUIREMENTCOUNT" sortable="true" width="50">总套数</th>
                <th field="CONSUMERNAME" sortable="true" width="100">客户名称</th>
            </tr>
            </thead>
        </table>
    </div>
</div>