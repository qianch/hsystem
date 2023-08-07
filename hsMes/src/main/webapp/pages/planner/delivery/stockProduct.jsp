<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<title>查看货物位置</title>
<%@ include file="../../base/jstl.jsp" %>
<script type="text/javascript">
    /* 	var dpo=${dpo};
	 var dp=${dp}; */
    function rowStyler(index, row) {
        let style = "";
        if (row.PRODUCTSHELFLIFE < inDays(null, row)) {
            style += 'color:red';
        }
        if (!isEmpty(row.NEWBATCHCODE)) {
            style += 'background:#f9ff00;';
        }
        return style;
    }

    function orderFormatter(value, row, index) {
        if (!isEmpty(row.NEWSALESORDERCODE)) {
            return row.NEWSALESORDERCODE;
        }
        return value;
    }

    function batchFormatter(value, row, index) {
        if (!isEmpty(row.NEWBATCHCODE)) {
            return row.NEWBATCHCODE;
        }
        return value;
    }

    function inDays(value, row) {
        if (isEmpty(row.INTIME))
            return "";
        var intimes = row.INTIME + "";
        var begintime_ms = new Date(intimes.replace(/-/g, "/"));
        var nowtimes = new Date();
        var millions = nowtimes.getTime() - begintime_ms.getTime();
        var days = Math.floor(millions / (24 * 3600 * 1000))
        return days;
    }

    function weightFormatter(v, i, r) {
        return new Number(v).toFixed(2);
    }

    function formatterState(value, row) {
        if (value === 2) {
            return "不合格";
        } else if (value === 3) {
            return "冻结";
        } else if (value === 5) {
            return "退货";
        } else if (value === 6) {
            return "超产";
        } else {
            return "合格";
        }
    }

    let stock = ${empty stock?"[]":stock};
</script>
<div style="border-top:1px solid #DDDDDD">
    客 户：
    <input type="text" readonly="true" class="easyui-textbox" value="${dp.deliveryTargetCompany}"><br>出货单号：
    <input type="text" readonly="true" class="easyui-textbox" value="${dp.deliveryCode}"><br> 装箱序号：
    <input type="text" readonly="true" class="easyui-textbox" value="${dpo.pn}"><br>
</div>
<table id="stock_dg" singleSelect="false" title="" style="width:100%;" class="easyui-datagrid" pagination="true"
       rownumbers="true" fitColumns="false" fit="false" remoteSort="true"
       data-options="rowStyler:rowStyler,data:stock">
    <thead frozen="true">
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="BARCODE" sortable="true" width="120">条码号</th>
        <th field="SALESORDERCODE" sortable="true" width="130">订单号</th>
        <th field="BATCHCODE" sortable="true" width="100">批次号</th>
    </tr>
    </thead>
    <thead>
    <tr>
        <th field="CONSUMERPRODUCTNAME" sortable="true" width="210">客户产品名称</th>
        <th field="FACTORYPRODUCTNAME" sortable="true" width="210">厂内产品名称</th>
        <th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="210">部件名称</th>
        <th field="PRODUCTMODEL" sortable="true" width="210">产品型号</th>
        <th field="WEIGHT" sortable="true" width="70" formatter="weightFormatter">重量(KG)</th>
        <th field="WAREHOUSECODE" sortable="true" width="70">仓库</th>
        <th field="WAREHOUSEPOSCODE" sortable="true" width="70">库位</th>
        <th field="INTIME" sortable="true" width="140">入库时间</th>
        <th field="DAYS" sortable="true" width="140" formatter="inDays">在库天数</th>
        <th field="PRODUCTSHELFLIFE" sortable="true" width="90">保质期(天)</th>
        <th field="STATE" sortable="true" width="70">状态</th>
    </tr>
    </thead>
</table>

