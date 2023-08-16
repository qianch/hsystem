<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/meta.jsp" %>
<style>
    .title {
        width: 130px;
    }
</style>
<script type="text/javascript">
    const weaveDailyPlan = path + "weaveDailyPlan/findW";
    $(function () {
        const id = $("#ids").val();
        const workShop = $('#workShop').combobox('getValue');
        const nowTime = $('#planDate').datebox('getValue');
        JQ.ajaxPost(weaveDailyPlan, {
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
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                    url: path + "planner/weavePlan/device?wid=" + row.DAILYID + "&&workshop=" + workShop + "&&date=" + nowTime + "&&id=" + row.ID,
                    fitColumns: true,
                    singleSelect: true,
                    rownumbers: true,
                    loadMsg: '',
                    height: 'auto',
                    columns: [[{
                        field: 'DEVICEASSETCODE',
                        title: '设备编号',
                        width: 15
                    }, {
                        field: 'PRODUCECOUNT',
                        title: '生产数量',
                        width: 15
                    }]],
                    onResize: function () {
                        $('#dgg').datagrid(
                            'fixDetailRowHeight',
                            index);
                    },
                    onLoadSuccess: function () {
                        setTimeout(function () {
                            $('#dgg').datagrid('fixDetailRowHeight', index);
                        }, 100);
                    }
                });
                $('#dgg').datagrid('fixDetailRowHeight', index);
            }
        });
    });

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
</script>
<div>
    <form style="height:98%" id="weaveDailyPlanForm" method="post" ajax="true"
          action="<%=basePath %>weaveDailyPlan/${empty weaveDailyPlan.id ?'add':'edit'}" autocomplete="off"
          autoSearchFunction="false">
        <input type="hidden" id="ids" name="id" value="${weaveDailyPlan.id}"/>
        <table width="100%">
            <tr>
                <td class="title">日计划计划时间:</td>
                <td>
                    <input type="text" id="planDate" name="planDate" value="${weaveDailyPlan.planDate}"
                           class="easyui-datebox" required="true">
                </td>
                <td class="title">车间:</td>
                <td>
                    <input type="text" id="workShop" name="workShop" value="${weaveDailyPlan.workShop}"
                           class="easyui-combobox" required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave'">
                </td>
            </tr>
        </table>
        <table id="dgg" singleSelect="false" title="编织计划列表" class="easyui-datagrid" url="" pagination="false"
               rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="DAILYID" hidden='true' sortable="true" width="15">计划ID</th>
                <th field="PRODUCTTYPE" sortable="true" width="15" formatter="formatterType">产品属性</th>
                <th field="PLANCODE" sortable="true" width="15">生产计划单号</th>
                <th field="SALESORDERCODE" sortable="true" width="15">销售订单号</th>
                <th field="BATCHCODE" sortable="true" width="15">批次号</th>
                <th field="PRODUCTMODEL" sortable="true" width="15">产品规格</th>
                <th field="BCBOMCODE" sortable="true" width="15">包装代码</th>
                <th field="BCBOMVERSION" sortable="true" width="15">包装版本</th>
                <th field="PRODUCTLENGTH" sortable="true" width="15">卷长</th>
                <th field="PRODUCTWEIGHT" sortable="true" width="15" formatter="processNumberFormatter">重量</th>
                <th field="REQUIREMENTCOUNT" sortable="true" width="15">数量</th>
                <th field="CONSUMERNAME" sortable="true" width="15">客户名称</th>
            </tr>
            </thead>
        </table>
    </form>
</div>