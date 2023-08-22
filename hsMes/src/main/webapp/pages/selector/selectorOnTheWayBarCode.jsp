<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script>
    function filter2() {
        alert(2);
        EasyUI.grid.search("SelectorOnTheWayBarCode_dg", "OnTheWayBarCode_searchForm");
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
        <div id="category_toolbar">
            <div style="border-top:1px solid #DDDDDD">
                <form action="#" id="OnTheWayBarCode_searchForm" autoSearchFunction="false">
                    <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                       onclick="filter2()">搜索
                    </a>
                </form>
            </div>
        </div>
        <table id="SelectorOnTheWayBarCode_dg" singleSelect="false" title="" class="easyui-datagrid"
               url="${path}planner/deliveryOnTheWayPlan/findDeliveryOnTheWayPlanDetails?deliveryId=${deliveryId}"
               toolbar="#toolbar1" pagination="false" rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="BARCODE" sortable="true" width="15">托条码</th>
                <th field="SALESORDERSUBCODE" sortable="true" width="15">客户订单号</th>
                <th field="BATCHCODE" sortable="true" width="15">批次号</th>
                <th field="PARTNAME" sortable="true" width="15">厂内名称</th>
                <th field="CONSUMERPRODUCTNAME" sortable="true" width="15">客户产品名称</th>
                <th field="FACTORYPRODUCTNAME" sortable="true" width="15">部位</th>
                <th field="WEIGHT" sortable="true" width="15">重量(Kg)</th>
                <th field="STOCKSTATE" hidden="true" sortable="true" width="15">条码状态</th>
                <th field="STOCKSTATETEXT" sortable="true" width="15">条码状态</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
