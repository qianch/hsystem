<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<title>选择产品信息</title>
<%@ include file="../../base/jstl.jsp" %>
<script type="text/javascript">
    //添加成品信息
    const _common_product_addUrl = path + "finishProduct/add";
    //选择客户信息
    const chooseConsumer = path + "selector/consumer";
    //选择套材/非套材BOM信息
    const chooseBomUrl = path + "finishProduct/chooseBom";
    //选择包装BOM信息
    const choosePackingBomUrl = path + "selector/packingBom";
    const consumerWindow = null;
    const bomWindow = null;
    const packingBomWindow = null;
    const productIsTc = null;
    let isFilter = false;

    //查询
    function _common_product_filter() {
        isFilter = true;
        EasyUI.grid.search("_common_product_dg", "_common_product_dg_form");
    }

    $(document).ready(function () {
        let autoSearch = null;
        let autoSearchFunction;
        $(function () {
            $(".textbox-text").keyup(function () {
                $("input[name='" + $(this).parent().prev().attr("textboxname") + "']").val($(this).val());
                clearTimeout(autoSearch);
                autoSearchFunction = $(this).parent().parent().attr("autoSearchFunction");
                if (autoSearchFunction) {
                    autoSearch = setTimeout(window[autoSearchFunction](), 500);
                } else {
                    autoSearch = setTimeout(_common_product_filter, 500);
                }
            });
        });
        $('#_common_product_dg').datagrid({
            onDblClickRow: function (index, row) {
                if (typeof _common_product_dbClickRow === "function") {
                    _common_product_dbClickRow(index, row);
                } else {
                    if (window.console) {
                        console.log("没有为产品选择界面提供_common_product_dbClickRow方法，参数为index,row");
                    }
                }
            },
            onLoadSuccess: function (data) {
                $(this).datagrid("enableFilter");
                if (typeof _common_product_onLoadSuccess === "function") {
                    _common_product_onLoadSuccess(data);
                } else {
                    if (window.console) {
                        console.log("未定义产品选择界面加载完成的方法：_common_product_onLoadSuccess(data)");
                    }
                }
            }
        });
    });

    function formatterIsTc(value, row) {
        if (value === '1') {
            return "套材";
        } else if (value === '2') {
            return "非套材";
        } else {
            return "胚布";
        }
    }

    function _commons_product_dg_onBeforeLoad() {
        /* if (typeof _commons_product_dg_onBeforeLoad_callback == "function") {
            return _commons_product_dg_onBeforeLoad_callback();
        } else {
            if (window.console) {
                console
                        .log("未定义产品选择界面加载完成的方法：_commons_product_dg_onBeforeLoad_callback()");
            }
            return true;
        } */
        console.log($('#commonProductIds').val());
        return isFilter;
    }

    //产品属性
    function formatterIscommon(value, row, index) {
        if (value === 0) {
            return "试样";
        } else if (value === 1) {
            return "常规";
        }
    }

    //审核状态
    function formatterReviewState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }
</script>
<div id="_common_product_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <form action="#" id="_common_product_dg_form">
            <input type="hidden" id="consumerId" in="false"
                   name="filter[consumerId]" value="${consumerId}">
        </form>
    </div>
</div>
<table id="_common_product_dg" width="100%" height="98%"
       data-options="onBeforeLoad:_commons_product_dg_onBeforeLoad,pageSize:100,rowStyler:productStyler,remoteFilter:true"
       class="easyui-datagrid"
       url="<%=basePath%>planner/deliveryPlan/listDelivery" pagination="true"
       rownumbers="true" fitColumns="false" fit="true">
    <thead frozen="true">
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="SALESORDERSUBCODE" width="60" sortable="true">订单号</th>
        <th field="SALESORDERCODE" width="60" sortable="true">订单编号</th>
        <th field="BATCHCODE" width="60" sortable="true">批次号</th>
        <th field="FACTORYPRODUCTNAME" width="140" sortable="true">厂内名称</th>
        <th field="PARTNAME" width="140" sortable="true">部件名称</th>
        <th field="PARTTYPE" sortable="true" width="70">部件类型</th>
    </tr>
    </thead>
    <thead>
    <tr>
        <th field="CONSUMERPRODUCTNAME" width="150" sortable="true">客户产品名称</th>
        <th field="PRODUCTMODEL" width="130" sortable="true">产品型号</th>
        <th field="PRODUCTPROCESSCODE" sortable="true" width="200">工艺标准代码</th>
        <th field="PRODUCTPROCESSBOMVERSION" sortable="true" width="100">工艺标准版本</th>
        <th field="PRODUCTPACKAGINGCODE" sortable="true" width="200">包装标准代码</th>
        <th field="PRODUCTPACKAGEVERSION" sortable="true" width="100">包装标准版本</th>
        <th field="PRODUCTISTC" width="60" sortable="true" formatter="formatterIsTc">类型</th>
        <th field="PRODUCTMEMO" width="100">备注</th>
    </tr>
    </thead>
</table>
