<!--
作者:徐波
日期:2016-10-18 12:56:44
页面:生产计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/meta.jsp" %>
<script type="text/javascript">
    //JS代码
    let cutPlans = ${cutPlans};
    let weavePlans = ${weavePlans};
    $(document).ready(function () {
        let a;
        console.log('cutPlansLength', cutPlans.length);
        if (cutPlans.length === 0) {
            $('#plan_detail').layout('hidden', 'east');
        } else {
            $('#plan_detail').layout('show', 'east');
        }
        for (a = 0; a < cutPlans.length; a++) {
            add_cutPlan_data(cutPlans[a]);
        }
        for (a = 0; a < weavePlans.length; a++) {
            add_weavePlan_data(weavePlans[a]);
        }
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

    function add_cutPlan_data(r) {
        const _row = {
            "PRODUCEPLANCODE": r.PRODUCEPLANCODE,
            "SALESORDERCODE": r.SALESORDERCODE,
            "BATCHCODE": r.BATCHCODE,
            "PRODUCTMODEL": r.PRODUCTMODEL,
            "PRODUCTPACKAGINGCODE": r.PRODUCTPACKAGINGCODE,
            "PRODUCTPACKAGINGVERSION": r.PRODUCTPACKAGINGVERSION,
            "CONSUMERNAME": r.CONSUMERNAME
        };
        $("#dg_cutPlan").datagrid("appendRow", _row);
    }

    function add_weavePlan_data(r) {
        const _row = {
            "WEAVEPLANPRODUCTTYPE": r.WEAVEPLANPRODUCTTYPE,
            "PRODUCEPLANCODE": r.PRODUCEPLANCODE,
            "SALESORDERC": r.SALESORDERC,
            "BATCHCODE": r.BATCHCODE,
            "PRODUCTMODEL": r.PRODUCTMODEL,
            "PRODUCTPACKAGINGCODE": r.PRODUCTPACKAGINGCODE,
            "PRODUCTPACKAGINGVERSION": r.PRODUCTPACKAGINGVERSION,
            "PRODUCTROLLLENGTH": r.PRODUCTROLLLENGTH,
            "PRODUCTROLLWEIGHT": r.PRODUCTROLLWEIGHT,
            "CONSUMERNAME": r.CONSUMERNAME
        };
        $("#dg_weavePlan").datagrid("appendRow", _row);
    }
</script>
<div>
    <!--生产计划表单-->
    <form id="producePlanForm" method="post" ajax="true"
          action="<%=basePath %>planner/produce/${empty producePlan.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${producePlan.id}"/>
        <input type="hidden" name="producePlanIsAutoCreate" value="2"/>
        <input type="hidden" name="auditState" value="${producePlan.auditState}"/>
        <input type="hidden" name="producePlanDate" value="${producePlan.producePlanDate}"/>
        <table width="100%">
            <tr>
                <td class="title">销售订单号:</td>
                <!--销售订单ID-->
                <td><input type="text" id="salesOrderCode" name="salesOrderCode" value="${producePlan.salesOrderCode}"
                           class="easyui-textbox" readonly="true"></td>
            </tr>
            <tr>
                <td class="title">生产计划单号:</td>
                <!--生产计划单号-->
                <td>
                    <input type="text" id="producePlanCode" name="producePlanCode"
                           value="${producePlan.producePlanCode}" class="easyui-textbox" readonly="true">
                </td>
            </tr>
            <tr>
                <td class="title">是否临时计划:</td>
                <!--是否临时计划 -1:临时计划,1:非临时计划-->
                <td>
                    <input type="text" id="producePlanIsTemp" name="producePlanIsTemp"
                           value="${producePlan.producePlanIsTemp==1?'非临时计划':'临时计划'}" class="easyui-textbox"
                           readonly="true">
                </td>
            </tr>
            <tr>
                <td class="title">是否废弃:</td>
                <!--是否废弃  -1:废弃,其他状态为非废弃-->
                <td>
                    <input type="text" id="producePlanIsDeprecated" name="producePlanIsDeprecated"
                           value="${producePlan.producePlanIsDeprecated==1?'非废弃':'废弃'}" class="easyui-textbox"
                           readonly="true">
                </td>
            </tr>
            <!--选择产品-->
        </table>
    </form>
    <div id="plan_detail" style="widht:100%;" class="easyui-layout" fit="true">
        <div id="weavePlan" class="easyui-layout" region="center" fit="true">
            <table id="dg_weavePlan" singleSelect="false" title="编织计划" class="easyui-datagrid" url=""
                   rownumbers="true" fitColumns="true" fit="true">
                <thead>
                <tr>
                    <th field="WEAVEPLANPRODUCTTYPE" sortable="true" width="15"
                        formatter="formatterType">产品属性
                    </th>
                    <th field="PRODUCEPLANCODE" sortable="true" width="15">生产计划单号</th>
                    <th field="SALESORDERC" sortable="true" width="15">销售订单号</th>
                    <th field="BATCHCODE" sortable="true" width="15">批次号</th>
                    <th field="PRODUCTMODEL" sortable="true" width="15">产品规格</th>
                    <th field="PRODUCTPACKAGINGCODE" sortable="true" width="15">包装代码</th>
                    <th field="PRODUCTPACKAGINGVERSION" sortable="true" width="15">包装版本</th>
                    <th field="PRODUCTROLLLENGTH" sortable="true" width="15">米长</th>
                    <th field="PRODUCTROLLWEIGHT" sortable="true" width="15" formatter="processNumberFormatter">重量
                    </th>
                    <th field="CONSUMERNAME" sortable="true" width="15">客户名称</th>
                </tr>
                </thead>
            </table>
        </div>
        <div id="cutPlan" class="easyui-layout" region="east"
             style="width: 50%">
            <table id="dg_cutPlan" singleSelect="true" title="裁剪计划" class="easyui-datagrid" url="" rownumbers="true"
                   fitColumns="true" fit="true">
                <thead>
                <tr>
                    <th field="PRODUCEPLANCODE" width="15">生产计划单号</th>
                    <th field="SALESORDERCODE" width="15">订单号</th>
                    <th field="BATCHCODE" width="15">批次号</th>
                    <th field="PRODUCTMODEL" width="15">产品规格</th>
                    <th field="PRODUCTPACKAGINGCODE" width="15">包装代码</th>
                    <th field="PRODUCTPACKAGINGVERSION" width="15">包装版本</th>
                    <th field="CONSUMERNAME" width="15">客户名称</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>