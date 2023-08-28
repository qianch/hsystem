<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const findOutRecordUrl = path + "stock/productDeliveryRecord/findOutRecord";
    //导出竖排
    const exportUrlnew = path + "planner/deliveryPlan/exportnew";
    //导出横竖排
    const exportUrlnew1 = path + "planner/deliveryPlan/exportnew1";
    //导出横竖排2
    const exportUrlnew2 = path + "planner/deliveryPlan/exportnew2";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "productDeliveryRecordSearchForm");
    }

    function formatterAuditState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}stock/productDeliveryRecord/list",
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                    url: findOutRecordUrl + "?packingNumber=" + row.PACKINGNUMBER,
                    fitColumns: true,
                    singleSelect: true,
                    rownumbers: true,
                    loadMsg: '',
                    height: 'auto',
                    columns: [[
                        {
                            field: 'BARCODE',
                            title: '托条码',
                            width: 15,
                        }, {
                            field: 'PRODUCTMODEL',
                            title: '产品规格',
                            width: 15,
                        }, {
                            field: 'SALESORDERCODE',
                            title: '内部订单号',
                            width: 15,
                        }, {
                            field: 'SALESORDERSUBCODEPRINT',
                            title: '客户订单号',
                            width: 15,
                        }, {
                            field: 'BATCHCODE',
                            title: '批次号',
                            width: 15,
                        }, {
                            field: 'FACTORYPRODUCTNAME',
                            title: '厂内名称',
                            width: 15,
                        }, {
                            field: 'CONSUMERPRODUCTNAME',
                            title: '客户产品名称',
                            width: 15,
                        }, {
                            field: 'WAREHOUSEPOSCODE',
                            title: '库位编码',
                            width: 15,
                        }, {
                            field: 'WAREHOUSECODE',
                            title: '仓库编码',
                            width: 15,
                        }, {
                            field: 'WEIGHT',
                            title: '重量(Kg)',
                            width: 15,
                        }, {
                            field: 'OUTTIME',
                            title: '出库时间',
                            width: 20,
                        }

                    ]],
                    onResize: function () {
                        $('#dg').datagrid('fixDetailRowHeight', index);
                    },
                    onLoadSuccess: function () {
                        Loading.hide();
                        setTimeout(function () {
                            $('#dg').datagrid('fixDetailRowHeight', index);
                        }, 0);
                    }
                });
                //$('#dg').datagrid('fixDetailRowHeight',index);
            }
        });
    });
    const exportUrl1 = path + "/planner/deliveryPlan/export1";

    function export1() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportUrl1 + "?ids=" + ids);
    }

    const exportUrl2 = path + "/planner/deliveryPlan/export2";

    function export2() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportUrl2 + "?ids=" + ids);
    }

    const exportUrl3 = path + "/planner/deliveryPlan/export3";

    function export3() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportUrl3 + "?ids=" + ids);
    }

    const exportMyfdUrl = path + "planner/deliveryPlan/exportMyfd";

    //导出明阳风电
    function exportMyfd() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportMyfdUrl + "?ids=" + ids);
    }

    //导出竖排
    function exportExcelnew() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("请至少选择一行信息");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportUrlnew + "?ids=" + ids);
    }

    //导出横排
    function exportExcelnew1() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("请至少选择一行信息");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportUrlnew1 + "?ids=" + ids);
    }

    //导出横排2
    function exportExcelnew2() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("请至少选择一行信息");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportUrlnew2 + "?ids=" + ids);
    }
</script>