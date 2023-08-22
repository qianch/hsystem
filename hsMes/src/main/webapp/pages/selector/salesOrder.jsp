<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择生产计划明细信息</title>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        //设置数据表格的DetailFormatter内容
        $('#_common_salesOrder_dg').datagrid({
            onDblClickRow: function (index, row) {
                if (typeof _common_salesOrder_dbClickRow === "function") {
                    _common_salesOrder_dbClickRow(index, row);
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供_common_salesOrder_dbClickRow方法，参数为index,row");
                    }
                }
            },
            onLoadSuccess: function (data) {
                if (typeof _common_salesOrder_onLoadSuccess === "function") {
                    _common_salesOrder_onLoadSuccess(data);
                } else {
                    if (window.console) {
                        console.log("未定义用户选择界面加载完成的方法：_common_salesOrder_onLoadSuccess(data)");
                    }
                }
            }
        });
    });

    function orderDateFormat(value, row, index) {
        if (value === undefined) return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function exportFormat(value, row, index) {
        return value === 0 ? "外销" : "内销";
    }

    function orderTypeFormat(value, row, index) {
        //（3新品，2试样，1常规产品，-1未知）
        switch (value) {
            case 3:
                return "新品";
            case 2:
                return "试样";
            case 1:
                return "常规产品";
            default:
                return "未知";
        }
    }

    function _common_salesOrder_rowStyler(index, row) {
        return row.SALESORDERISEXPORT === 0 ? "background:rgba(255, 0, 0, 0.23);" : "";
    }

    function _common_salesOrder_filter() {
        EasyUI.grid.search("_common_salesOrder_dg", "_common_salesOrder_dg_form");
    }
</script>
<div id="_common_salesOrder_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <form action="#" id="_common_salesOrder_dg_form" autoSearchFunction="false">
            业务人员:<input type="text" name="filter[userName]" like="true" class="easyui-textbox">
            订单号码:<input type="text" name="filter[orderCode]" like="true" class="easyui-textbox">
            <br>
            订单类型:<input type="text" name="filter[orderType]" class="easyui-combobox"
                            data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'常规'},{'v':'2','t':'试样'},{'v':'3','t':'新品'},{'v':'-1','t':'未知'}],onSelect:_common_salesOrder_filter">
            内销外销:<input type="text" name="filter[export]" class="easyui-combobox"
                            data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'内销'},{'v':'0','t':'外销'}],onSelect:_common_salesOrder_filter">
            <br>
            客户名称:<input type="text" id="_common_consumerName" name="filter[consumerName]" like="true"
                            class="easyui-textbox">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
               onclick="_common_salesOrder_filter()">
                搜索
            </a>
        </form>
    </div>
</div>
<table id="_common_salesOrder_dg" idField="SALESORDERCODE" singleSelect="${empty singleSelect?'true':singleSelect }"
       class="easyui-datagrid" url="${path}salesOrder/list" toolbar="#_common_salesOrder_toolbar" pagination="true"
       data-options="rowStyler:_common_salesOrder_rowStyler" fit="true">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="SALESORDERCODE" sortable="true" width="15">订单号</th>
        <th field="SALESORDERDATE" sortable="true" width="15" data-options="formatter:orderDateFormat">下单日期</th>
        <th field="CONSUMERNAME" sortable="true" width="15">客户名称</th>
        <th field="USERNAME" sortable="true" width="15">业务员</th>
        <th field="SALESORDERISEXPORT" sortable="true" width="15" data-options="formatter:exportFormat">内销/外销</th>
        <th field="SALESORDERTYPE" sortable="true" width="15" data-options="formatter:orderTypeFormat">订单类型</th>
        <th field="SALESORDERTOTALMONEY" sortable="true" width="15">订单总金额</th>
        <th field="SALESORDERDELIVERYTIME" sortable="true" width="15" data-options="formatter:orderDateFormat">
            发货时间
        </th>
        <th field="SALESORDERREVIEWSTATE" sortable="true" width="15">订单审核状态</th>
        <th field="SALESORDERMEMO" sortable="true" width="15">订单备注</th>
    </tr>
    </thead>
</table>
