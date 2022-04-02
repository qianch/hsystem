<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    var findOutRecordUrl = path + "planner/deliveryOnTheWayPlan/findDeliveryOnTheWayPlanDetails";

    var selectorOnTheWayBarCodeUrl = path + "planner/deliveryOnTheWayPlan/selectorOnTheWay";

    var moveUrl = path + "mobile/stock2/product/pMove";

    var action = "pmove";
    //移库
    var pMoveUrl = path + "planner/deliveryOnTheWayPlan/pMove";

    var dialogWidth = 700, dialogHeight = 500;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "productDeliveryRecordSearchForm");
    }

    function formatterAuditState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}planner/deliveryOnTheWayPlan/list",
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');

                ddv.datagrid({
                        url: findOutRecordUrl + "?deliveryId=" + row.ID,
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
                                field: 'SALESORDERSUBCODE',
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
                                field: 'PARTNAME',
                                title: '部位',
                                width: 15,
                            }, {
                                field: 'WEIGHT',
                                title: '重量(Kg)',
                                width: 15,
                            }
                            , {
                                field: 'STOCKSTATETEXT',
                                title: '状态',
                                width: 15,
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
                        , rowStyler: function (index, row) {
                            if (row.STOCKSTATE == 3) {
                                return 'background-color:yellow;color:blue;font-weight:bold;';
                            }
                        }
                    }
                );
            }
            , rowStyler: function (index, row) {
                if (row.ONTHEWAYCOUNT > 0) {
                    return 'background-color:pink;color:blue;font-weight:bold;';
                }
            }

        });
    });


    var exportUrl3 = path + "/planner/deliveryOnTheWayPlan/export3";

    function export3() {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }

        var ids;
        for (var i = 0; i < r.length; i++) {
            ids = r[i].ID;
        }
        window.open(exportUrl3 + "?deliveryId=" + ids);
    }

    function exportDetail() {
        location.href = encodeURI(path + "/planner/deliveryOnTheWayPlan/exportDetail?" + JQ.getFormAsString("productDeliveryRecordSearchForm"));
    }

    //编辑销售订单
    var PMove = function () {
        action = "edit";
        var r = EasyUI.grid.getOnlyOneSelected("dg");

        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, pMoveUrl + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    }

    function saveForm() {
        var barcodes = "";
        var rows = $("#pmoveOnTheWayDetail_dg").datagrid('getRows');//获得所有行

        for (var i = 0; i < rows.length; i++) {
            barcodes += rows[i].BARCODE + ',';
        }

        var rStr = {
            newWarehouseCode: $("#warehouseCode").val(),
            newWarehousePosCode: $("#warehousePosCode").val(),
            moveUserId:  ${userId },
            code: barcodes
        };

        Dialog.confirm(function () {
            Loading.show();

            $.post(moveUrl, rStr, function (data, status) {
                Loading.hide();
                if (Tip.hasError(data)) {
                    Tip.error("error1:" + data);
                    return;
                }
                if (data.length == 2) {
                    Tip.success("移库成功", 3000);
                    Dialog.close(dialogId);
                } else {
                    Tip.error("error2:" + data);
                    return;
                }
                filter();
            });
        }, "请核对移库信息，确认移库?");
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "productDeliveryRecordSearchForm");
    }

    //选择客户信息
    var selectWareHousePosWindowId;

    function selectWareHousePos() {
        selectWareHousePosWindowId = Dialog.open("选择库位", 900, 500, path + "selector/warehousePos?singleSelect=true", [EasyUI.window.button("icon-ok", "选择", function () {
            var row = $("#_common_warehousePos_dg").datagrid("getChecked");
            if (row.length == 0) {
                Tip.warn("至少选择一个库位");
                return;
            }
            $("#warehousePosCode").searchbox("setValue", row[0].WAREHOUSEPOSCODE);
            $("#warehouseCode").val(row[0].WAREHOUSECODE);
            Dialog.close(selectWareHousePosWindowId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectWareHousePosWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectWareHousePosWindowId)
        });
    }

    var selectselectOnTheWayDetailId;

    function selectOnTheWayDetail() {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        var ids = r[0].ID;
        selectselectOnTheWayDetailId = Dialog.open("选择条码", 900, 500, selectorOnTheWayBarCodeUrl + "?deliveryId=" + ids, [EasyUI.window.button("icon-ok", "选择", function () {
            var rows = $("#SelectorOnTheWayBarCode_dg").datagrid("getChecked");
            var message = "";
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].STOCKSTATE != 3) {
                    message += rows[i].BARCODE + "非在途无法挑选；";
                    continue;
                }
                addToPmoveOnTheWayDetail_dg(rows[i]);
            }
            if (message != "") {
                Tip.warn(message);
            }
            Dialog.close(selectselectOnTheWayDetailId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectselectOnTheWayDetailId);
        })], function () {
        }, function () {
            Dialog.close(selectselectOnTheWayDetailId)
        });
    }

    function addToPmoveOnTheWayDetail_dg(r) {
        var rows = $("#pmoveOnTheWayDetail_dg").datagrid('getRows');//获得所有行

        for (var i = 0; i < rows.length; i++) {
            if (rows[i].BARCODE == r.BARCODE) {
                return;
            }
        }

        var _row = {
            "ID": r.ID,
            "BARCODE": r.BARCODE,
            "SALESORDERSUBCODE": r.SALESORDERSUBCODE,
            "BATCHCODE": r.BATCHCODE,
            "PARTNAME": r.PARTNAME,
            "CONSUMERPRODUCTNAME": r.CONSUMERPRODUCTNAME,
            "FACTORYPRODUCTNAME": r.FACTORYPRODUCTNAME,
            "WEIGHT": r.WEIGHT,
            "STOCKSTATE": r.STOCKSTATE,
            "STOCKSTATETEXT": r.STOCKSTATETEXT
        };
        $("#pmoveOnTheWayDetail_dg").datagrid("appendRow", _row);
    }

    function removeOnTheWayDetail() {
        var rows = $("#pmoveOnTheWayDetail_dg").datagrid("getSelections");
        for (var i = 0; i < rows.length; i++) {
            $("#pmoveOnTheWayDetail_dg").datagrid("deleteRow", $("#pmoveOnTheWayDetail_dg").datagrid("getRowIndex", rows[i]));
            delete indexData[$("#pmoveOnTheWayDetail_dg").datagrid("getRowIndex", rows[i])];
        }
    }

</script>
