<!--
作者:宋黎明
日期:2016-11-24 11:02:50
页面:日计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
</style>
<script type="text/javascript">
    var selectedPlans = ${empty selectedPlans?"[]":selectedPlans};
    var indexData = {};
    $(function () {
        $("#dgg").datagrid({
            onDblClickRow: dbClickEditC,
            data: selectedPlans,
            view: detailview,
            onLoadSuccess: function (data) {
                var rows = data.rows;
                for (var i = 0; i < rows.length; i++) {
                    $(this).datagrid("expandRow", i);
                }
            },
            detailFormatter: function (index, row) {
                return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
            },
            onCollapseRow: function (index, row) {
                /* var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                try {
                    var rows = ddv.datagrid("getRows");
                    for (var i = 0; i < rows.length; i++) {
                        ddv.datagrid("endEdit", i);
                    }
                    indexData[index] = rows;
                } catch (e) {
                    indexData[index] = [];
                } */
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                if (indexData[index] == undefined) {
                    Loading.show();
                    var planId = row.PRODUCEPLANDETAILID;
                    if (!row.PRODUCEPLANDETAILID) {
                        planId = -1;
                    }

                    JQ.ajaxPost(path + "bom/plan/tc/ver/parts/" + row.FROMSALESORDERDETAILID + "/" + planId, {}, function (data) {
                        Loading.hide();
                        indexData[index] = data;
                        initSubGrid(ddv, index, row);
                    }, function (data) {
                        Loading.hide();
                    });

                } else {
                    initSubGrid(ddv, index, row);
                }
            }
        });
    });

    function initSubGrid(ddv, index, row) {
        ddv.datagrid({
            data: indexData[index],
            fitColumns: true,
            singleSelect: true,
            rownumbers: true,
            loadMsg: '',
            width: '650',
            autoUpdateDetail: false,
            height: 'auto',
            rowStyler: danxiangbu,
            columns: [[{
                field: 'partName',
                title: '部件名称',
                width: 20
            }, {
                field: 'planPartCount',
                title: '计划数量',
                width: 15
            }, {
                field: 'partCount',
                title: '订单数量',
                width: 15
            }, {
                field: 'partBomCount',
                title: 'BOM数量',
                width: 15
            }, {
                field: '_',
                title: '数量分配',
                formatter: cutDailyUsersCounts,
                width: 30
            }]],
            onResize: function () {
                $('#dgg').datagrid('fixDetailRowHeight', index);
                $('#dgg').datagrid('fixRowHeight', index);
            },
            onLoadSuccess: function (data) {
                var ddv, subRows;
                $('#dgg').datagrid('fixDetailRowHeight', index);
                $('#dgg').datagrid('fixRowHeight', index);
                ddv = $("#dgg").datagrid('getRowDetail', index).find('table.ddv');
                subRows = ddv.datagrid("getRows");
                //partsNames SS面[杨凤燕(1)，聂亚军(1)，钟凯(1)]##SS面
                var ps = row.USERANDCOUNT.split("##");
                var pname;
                for (var i = 0; i < ps.length; i++) {
                    pname = ps[i].substring(0, ps[i].indexOf("["));
                    puc = ps[i].substring(ps[i].indexOf("[") + 1, ps[i].lastIndexOf("]"));
                    for (var j = 0; j < subRows.length; j++) {
                        if (pname == subRows[j].partName) {
                            ddv.datagrid("updateRow", {index: j, row: {_: puc}});
                        }
                    }
                }
            }
        });
        indexData = {};
        $('#dgg').datagrid('fixDetailRowHeight', index);
        $('#dgg').datagrid('fixRowHeight', index);
    }
</script>
<div style="height:100%;">
    <!--日计划表单-->
    <form id="cutPlanForm" method="post" ajax="true"
          action="<%=basePath %>cutDailyPlan/${empty cutDailyPlan.id ?'add':'edit'}" autocomplete="off">

        <input type="hidden" id="id" name="id" value="${cutDailyPlan.id}"/>

        <table width="100%">
            <tr>
                <td class="title">时间:</td>
                <td><input type="text" id="planDate" name="planDate" value="${cutDailyPlan.planDate}"
                           class="easyui-datebox" required="true"></td>

                <%--				<td class="title">车间:</td>--%>
                <%--				<!--车间-->--%>
                <%--				<td><input type="text" id="workShopCode" name="workShopCode" ${empty cutDailyPlan.workShopCode?"":"readonly"} class="easyui-combobox" required="true" data-options="data: [--%>
                <%--		                        {value:'00116',text:'裁剪一车间'},{value:'00117',text:'裁剪二车间'}]"></td>--%>
            </tr>
        </table>
    </form>
    <div id="dgg_add_toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-add"
           onclick="selectCutPlan()">添加</a> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
                                               iconcls="icon-edit" onclick="editCutPlan()">编辑</a> <a
            href="javascript:void(0)" class="easyui-linkbutton"
            plain="true" iconcls="icon-remove" onclick="removeCutPlan()">删除</a>
    </div>
    <table id="dgg" idField="ID" singleSelect="false" title="裁剪排产列表(双击行编辑)" style="width:98%;"
           toolbar="#dgg_add_toolbar" pagination="false" rownumbers="true" fitColumns="false">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="DELEVERYDATE" sortable="true" width="90" formatter="orderDateFormat">出货日期</th>
            <!-- <th field="PRODUCTTYPE" sortable="true" width="100" formatter="formatterType">产品属性</th> -->
            <th field="WORKSHOP" sortable="true" width="150">排产车间</th>
            <th field="PLANCODE" sortable="true" width="150">生产计划单号</th>
            <th field="SALESORDERCODE" sortable="true" width="150">销售订单号</th>
            <th field="BATCHCODE" sortable="true" width="150">批次号</th>
            <th field="PRODUCTNAME" sortable="true" width="120">产品名称</th>
            <th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
            <th field="PROCESSBOMCODE" sortable="true" width="120">工艺代码</th>
            <th field="PROCESSBOMVERSION" sortable="true" width="120">工艺版本</th>
            <th field="BCBOMCODE" sortable="true" width="120">包装代码</th>
            <th field="BCBOMVERSION" sortable="true" width="120">包装版本</th>
            <th field="DELEVERYDATE" sortable="true" width="70">出货时间</th>
            <th field="REQUIREMENTCOUNT" sortable="true" width="80">总套数</th>
            <th field="CONSUMERNAME" sortable="true" width="150">客户名称</th>
            <th field="WORKSHOPCODE" sortable="true" width="150">排产车间编码</th>
        </tr>
        </thead>
    </table>
</div>