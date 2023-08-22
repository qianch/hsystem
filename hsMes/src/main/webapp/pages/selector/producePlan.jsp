<!--
作者:徐波
日期:2016-10-18 12:56:44
页面:生产计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<title>生产计划</title>
<script type="text/javascript">
    //查询
    function _common_common_producePlan_filter() {
        EasyUI.grid.search("_common_producePlan_dg", "_common_producePlan_dg_form");
    }

    $(document).ready(function () {
        //设置数据表格的DetailFormatter内容
        $('#_common_producePlan_dg').datagrid({
            onDblClickRow: function (index, row) {
                if (typeof _common_producePlan_dbClickRow === "function") {
                    _common_producePlan_dbClickRow(index, row);
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供_common_producePlan_dbClickRow方法，参数为index,row");
                    }
                }
            },
            onLoadSuccess: function (data) {
                if (typeof _common_producePlan_onLoadSuccess === "function") {
                    _common_producePlan_onLoadSuccess(data);
                } else {
                    if (window.console) {
                        console.log("未定义用户选择界面加载完成的方法：_common_producePlan_onLoadSuccess(data)");
                    }
                }
            }
        });
    });

    function _common_producePlan_formatterReviewState(val, row, index) {
        if (row.AUDITSTATE === 0) {
            return "审核中";
        } else if (row.AUDITSTATE > 0) {
            return "已审核";
        } else {
            return "未审核";
        }
    }

    function _common_producePlan_formatterTime(value, row) {
        if (value !== undefined) {
            if (value.length === 10)
                value += " 00:00:01";
            else if (value.length > 10)
                value = value.substring(0, 10);
        }
        return value;
    }
</script>
<div id="_common_producePlan_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <form action="#" id="_common_producePlan_dg_form" autoSearchFunction="false">
            <label class="panel-title">搜索：</label>
            生产计划单号:<input type="text" name="filter[PRODUCEPLANCODE]" like="true" class="easyui-textbox">
            销售订单号:<input type="text" name="filter[SALESORDERID]" like="true" class="easyui-textbox">
            产品名称:<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
               onclick="_common_common_producePlan_filter()">搜索
            </a>
        </form>
    </div>
</div>
<table id="_common_producePlan_dg" singleSelect="${empty singleSelect?'true':singleSelect }" class="easyui-datagrid"
       url="${path}planner/produce/list" toolbar="#_common_producePlan_toolbar" pagination="true" rownumbers="true"
       fitColumns="true" fit="true" data-options="onDblClickRow:dbClickEdit">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="SALESORDERID" sortable="true" width="15">销售订单号</th>
        <th field="AUDITSTATE" sortable="true" width="15" formatter="_common_producePlan_formatterReviewState">
            审核状态
        </th>
        <th field="PRODUCEPLANDATE" sortable="true" width="15" formatter="_common_producePlan_formatterTime">下达日期
        </th>
        <th field="PRODUCEPLANCODE" sortable="true" width="15">生产计划单号</th>
        <th field="PRODUCEPLANISTEMP" sortable="true" width="15">是否临时计划</th>
        <th field="PRODUCEPLANISAUTOCREATE" sortable="true" width="15">是否自动生成</th>
        <th field="PRODUCEPLANISDEPRECATED" sortable="true" width="15">是否废弃</th>
    </tr>
    </thead>
</table>
