<!--
作者:徐波
日期:2016-10-18 13:09:34
页面:生产计划明细增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
    font {
        font-family: 微软雅黑;
        color: #c31111;
    }

    @keyframes changed {
        from {
            color: white;
            background: rgb(214, 9, 9);
        }
        to {
            color: white;
            background: rgba(255, 0, 0, 0.55);
        }
    }
</style>
<script type="text/javascript">
    function unAllocateCount(value, row, index) {
        value = row.PRODUCTCOUNT - row.ALLOCATECOUNT;
        return value;
    }

    function alloCate() {
        const isCheck = $('#isAlloCate').is(':checked');
        if (isCheck) {
            const data = $('#orderProductSelect').datagrid('getRows');
            const newData = [];
            console.log(data);
            for (let a = 0; a < data.length; a++) {
                const row = data[a];
                if ((row.PRODUCTCOUNT - row.ALLOCATECOUNT) >= 0) {
                    newData.push(row);
                }
            }
            const daa = {
                total: newData.length,
                rows: newData
            };
            $('#orderProductSelect').datagrid('loadData', daa);
        } else {
            $('#orderProductSelect').datagrid('reload');
        }
    }

    function editTimesFormatter(value, row, index) {
        return (isEmpty(value) ? 0 : value) + "次";
    }

    function detailRowStyler(index, row, index) {
        let style = "";
        if (isEmpty(row.CLOSED) || row.CLOSED === 0) {
        } else {
            style += "text-decoration:line-through;";
        }
        return style;
    }
</script>
<table width="100%" id="orderProductSelect" idField="ID" toolbar="#produceSalesOrderSearchToolbar" singleSelect="true"
       title="" class="easyui-datagrid"
       url="${path}planner/tbp/order/list"
       fitColumns="false"
       rowStyler="detailRowStyler"
       fit="true"
       pagination="true"
       data-options="
				onLoadSuccess:orderProductSelectLoadSuccess,
				onDblClickRow:onOrderProductSelectDblClickRow,
				view:groupview,
                groupField:'SALESORDERCODE',
                remoteFilter:true,
                groupFormatter:function(value,rows){
                    return '订单编号<font color=red>'+value + '</font> - ' + rows.length + ' 产品';
                }">
    <thead frozen="true">
    <tr>
        <!-- 这是生产计划明细ID -->
        <th field="PID" checkbox=true></th>
        <th field="SALESORDERSUBCODE" sortable="true" width="110">订单号</th>
        <th field="BATCHCODE" sortable="true" width="110">批次号</th>
        <th field="CONSUMERPRODUCTNAME" width="200">客户产品名称</th>
    </tr>
    </thead>
    <thead>
    <tr>
        <th field="CONSUMERNAME" width="200">客户名称</th>
        <th field="FACTORYPRODUCTNAME" width="200">厂内名称</th>
        <th field="PRODUCTMODEL" width="80">产品型号</th>
        <th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
        <th field="PRODUCTROLLLENGTH" width="80">卷长(m)</th>
        <th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
        <th field="SALESORDERDATE" sortable="true" width="80" data-options="formatter:orderDateFormat">下单日期</th>
        <th field="USERNAME" width="80">业务员</th>
        <th field="SALESORDERISEXPORT" width="80" data-options="formatter:exportFormat">内销/外销</th>
        <th field="SALESORDERTYPE" width="80" data-options="formatter:orderTypeFormat">订单类型</th>
        <th field="PRODUCTPROCESSCODE" width="130" styler="vStyler">工艺标准代码</th>
        <th field="PRODUCTPROCESSBOMVERSION" width="80">工艺标准版本</th>
        <th field="PRODUCTPACKAGINGCODE" width="130" styler="bvStyler">包装标准代码</th>
        <th field="PRODUCTPACKAGEVERSION" width="80">包装标准版本</th>
        <!-- <th field="PRODUCTROLLCODE" width="80">卷标签代码</th>
        <th field="PRODUCTBOXCODE" width="80">箱唛头代码</th>
        <th field="PRODUCTTRAYCODE" width="80">托唛头代码</th> -->
        <th field="PRODUCTMEMO" width="80">备注</th>
    </tr>
    </thead>
</table>