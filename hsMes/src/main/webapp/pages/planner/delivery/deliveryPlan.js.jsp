<!--
作者:徐波
日期:2016-11-2 9:30:07
页面:出货计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String hostPath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : (":" + request.getServerPort()));
%>
<script>


    //添加出货计划
    var addUrl = path + "planner/deliveryPlan/add";
    var addtcUrl = path + "planner/deliveryPlan/addTc";
    //编辑出货计划
    var editUrl = path + "planner/deliveryPlan/edit";
    var cannelUrl = path + "planner/deliveryPlan/cannel";
    var editTcUrl = path + "planner/deliveryPlan/editTc";
    var editOrderUrl = path + "planner/deliveryPlan/editOrder";
    var searchUrl = path + "planner/deliveryPlan/searchProduct";
    var checkUrl = path + "planner/deliveryPlan/chekDetail";
    //删除出货计划
    var deleteUrl = path + "planner/deliveryPlan/delete";
    var closeUrl = path + "planner/deliveryPlan/doClose";
    var commitUrl = path + "planner/deliveryPlan/commit";
    var commitAudit = path + "selector/commitAudit";
    //导出
    var exportUrl = path + "planner/deliveryPlan/export";
    var exportDeliveryExcelUrl = path + "planner/deliveryPlan/exportDeliveryExcel";
    var downLoadUrl = path + "planner/deliveryPlan/downLoadImage";
    var dialogWidth = 700, dialogHeight = 350;
    //添加出货订单关联
    var addUrl_order = path + "planner/deliveryPlanSalesOrders/add";
    //	var addUrl_order = path + "planner/produce/order/select";
    //编辑出货订单关联
    var editUrl_order = path + "planner/deliveryPlanSalesOrders/edit";
    //删除出货订单关联
    var deleteUrl_order = path + "planner/deliveryPlanSalesOrders/delete";
    var deliveryProduct = path + "planner/produce/product/select";
    var deliveryProductTc = path + "planner/deliveryPlan/product/select";
    var timeout = null;
    var chooseOrder = path + "planner/produce/order/deliverySelect";
    //添加出货详情列表
    var addUrl_product = path + "planner/deliveryPlanDetails/add";
    //编辑出货详情列表
    var editUrl_product = path + "planner/deliveryPlanDetails/edit";
    //删除出货详情列表
    var deleteUrl_product = path + "planner/deliveryPlanDetails/delete";
    //内销
    var nxUrl = path + "audit/CK/{id}/state";
    //外销
    var wxUrl = path + "audit/CK1/{id}/state";
    //复制
    var copy1 = path + "planner/deliveryPlan/copy";

    var releaseDeliveryPlanUrl = path + "planner/deliveryPlan/releaseDeliveryPlan";

    $(function () {
        $('#dg').datagrid({
            url: "${path}planner/deliveryPlan/list",
        });
        $(".textbox-text").keyup(
            function () {
                $(
                    "input[name='"
                    + $(this).parent().prev().attr(
                    "textboxname") + "']").val(
                    $(this).val());
                clearTimeout(timeout);
                timeout = setTimeout(filter, 500);
            });
    });

    var selectConsumerWindowId;

    function selectConsumer() {
        selectConsumerWindowId = Dialog.open("选择客户", 900, 500, path
            + "selector/consumer?singleSelect=true", [
            EasyUI.window.button("icon-ok", "选择", function () {
                var row = $("#_common_consumer_dg").datagrid("getChecked");
                if (row.length == 0) {
                    Tip.warn("至少选择一个客户");
                    return;
                }
                if (row.length > 1) {
                    Tip.warn("只能选择一个客户");
                    return;
                }
                $("#deliveryTargetCompany").searchbox("setValue",
                    row[0].CONSUMERNAME);
                $('#consumerId').val(row[0].ID);
                Dialog.close(selectConsumerWindowId);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(selectConsumerWindowId);
            })], function () {
        }, function () {
            Dialog.close(selectConsumerWindowId);
        });
    }

    function clickRow(index, row) {
        if (editingIndex != -1) {
            if ($("#dg_product").datagrid("validateRow", editingIndex)) {

                $("#dg_product").datagrid("endEdit", editingIndex);

                editingIndex = index;
                $("#dg_product").datagrid("beginEdit", index);
            }
        } else {
            editingIndex = index;
            $("#dg_product").datagrid("beginEdit", index);
        }
    }

    var editingIndex = -1;

    function endEdit() {
        var rows = $("#dg_product").datagrid("getRows");
        for (var i = 0; i < rows.length; i++) {
            editingIndex = i;
            $("#dg_product").datagrid("beginEdit", i);
            if (!$("#dg_product").datagrid("validateRow", i)) {
                return false;
            } else {
                $("#dg_product").datagrid("endEdit", i);
            }
            $("#dg_product").datagrid("endEdit", i);
            /*$("#dg_product").datagrid("endEdit", j);
             for (var j = rows.length - 1; j > i; j--) {
                if (rows[i].salesOrderSubCode == rows[j].salesOrderSubCode && rows[i].productId == rows[j].productId) {
                    Tip.warn("相同的订单号:" + rows[i].salesOrderSubCode + "；产品:" + rows[i].productModel);
                    return false;
                }
            } */
        }
        editingIndex = -1;
        return true;
    }

    function uuid() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
    }

    function reloadProduceOrder() {
        EasyUI.grid.search("orderProductSelect", "produceSalesOrderForm");
    }

    function rowStyler(index, row) {
        return row.SALESORDERISEXPORT == 0 ? "background:rgba(255, 0, 0, 0.23);"
            : "";
    }

    function onOrderProductSelectDblClickRow(index, row) {
    }

    function orderProductSelectLoadSuccess(data) {
    }

    function onBerforeLoad() {
        if ($('#_consumerName_salesOrder_detail').val() != null
            && $('#_consumerName_salesOrder_detail').val() != undefined
            && $('#_consumerName_salesOrder_detail').val() != "") {
            return true;
        } else {
            return false;
        }
    }

    function delete_order_products() {

        // 获取选中行的Index的值
        var rowIndex = $('#dg_product').datagrid('getRowIndex', $('#dg_product').datagrid('getSelected'));
        $("#dg_product").datagrid("deleteRow", rowIndex);
    }

    function add_order_products_tc() {
        var r_order = EasyUI.grid.getOnlyOneSelected("dg_order");
        if (isEmpty(r_order)) {
            return;
        }
        if (isEmpty($('#consumerId').val())) {
            Tip.warn("请选择发货客户");
            return;
        }
        var wid_product = Dialog
            .open(
                "选择产品",
                850,
                450,
                deliveryProductTc + "?consumerId="
                + $('#consumerId').val(),
                [
                    EasyUI.window
                        .button(
                            "icon-save",
                            "确认",
                            function () {

                                var rows = $(
                                    "#_common_product_dg")
                                    .datagrid(
                                        "getSelections");
                                console.log(rows);
                                for (var a = 0; a < rows.length; a++) {
                                    var r = rows[a];
                                    var _row = {
                                        "PACKINGNUMBER": r_order.PACKINGNUMBER,
                                        "PN": r_order.PN,
                                        "PRODUCTID": r.PRODUCTID,
                                        "SALESORDERSUBCODE": r.SALESORDERSUBCODE,
                                        "SALESORDERDETAILID": r.ID,
                                        "BATCHCODE": r.BATCHCODE,
                                        "DELIVERYCOUNT": 0,
                                        "DELIVERYSUITCOUNT": 0,
                                        "MEMO": "",
                                        "FACTORYPRODUCTNAME": r.FACTORYPRODUCTNAME,
                                        "CONSUMERPRODUCTNAME": r.CONSUMERPRODUCTNAME,
                                        "PRODUCTMODEL": r.PRODUCTMODEL,
                                        "PARTNAME": r.PARTNAME,
                                        "PARTID": r.PARTID,
                                        "PRODUCTROLLWEIGHT": r.PRODUCTROLLWEIGHT,
                                        "PRODUCTROLLLENGTH": r.PRODUCTROLLLENGTH,
                                        "PRODUCTWIDTH": r.PRODUCTWIDTH,
                                        "PRODUCTSHELFLIFE": r.PRODUCTSHELFLIFE,
                                        "PRODUCTPROCESSCODE": r.PRODUCTPROCESSCODE,
                                        "PRODUCTPROCESSBOMVERSION": r.PRODUCTPROCESSBOMVERSION,
                                        "PRODUCTPACKAGINGCODE": r.PRODUCTPACKAGINGCODE,
                                        "PRODUCTPACKAGEVERSION": r.PRODUCTPACKAGEVERSION,
                                        "CUSTOMERMATERIALCODEOFFP" : r.CUSTOMERMATERIALCODEOFFP
                                    };

                                    var dg_productrow = $("#dg_product").datagrid("getRows");
                                    var pid = _row.SALESORDERDETAILID + "_" + _row.BATCHCODE.replace(/[\s*\+\*\/()]+/g, "")+"_";
                                    if (_row.PARTID != null) {
                                        pid += _row.PARTID;
                                    }
                                    var flag = true;
                                    for (var j = 0; j < dg_productrow.length; j++) {
                                        //获取每一行的数据

                                        var jypid = dg_productrow[j].SALESORDERDETAILID + "_" + dg_productrow[j].BATCHCODE.replace(/[\s*\+\*\/()]+/g, "")+"_";
                                        if (dg_productrow[j].PARTID != null) {
                                            jypid += dg_productrow[j].PARTID;
                                        }
                                        if (pid ===jypid) {
                                            flag = false;
                                            break;
                                        }
                                    }
                                    $("#dg_product")
                                        .datagrid(
                                            "appendRow",
                                            _row);

                                    console.log(r.ID);
                                    if (flag) {

                                    } else {
                                        Tip.warn("已经包含该生产计划明细" + pid);
                                    }

                                }

                                Dialog.close(wid_product);
                            }),
                    EasyUI.window
                        .button(
                            "icon-cancel",
                            "<spring:message code="Button.Cancel" />",
                            function () {
                                Dialog.close(wid_product);
                            })], function () {

                    _common_product_filter();
                });
    }

    function ChooseOrder() {
        var _row = {
            "ID": "",
            "PN": pnNumber,
            "PACKINGNUMBER": uuid(),
            "LADINGCODE": $("#ladingCode").val(),
            "BOXNUMBER": $("#boxNumber").val(),
            "SERIALNUMBER": $("#serialNumber").val(),
            "COUNT": $("#count").val(),
            "WEIGHT": $("#weight").val(),
            "SIZE": $("#size").val()
        };
        $("#dg_order").datagrid("appendRow", _row);
        $("#dg_order").datagrid("beginEdit",
            EasyUI.grid.getRowIndex("dg_order", _row));
        pnNumber++;

    }

    function productValueAdd(row) {
        $("#productId").textbox('setValue', row.PRODUCTID);
        $("#deliveryCount").textbox('setValue', row.DELIVERYCOUNT);
        $("#deliverySuitCount").textbox('setValue', row.DELIVERYSUITCOUNT);
        $("#memo").textbox('setValue', row.MEMO);
        $("#consumerProductName").textbox('setValue', row.CONSUMERPRODUCTNAME);
        $("#factoryProductName").textbox('setValue', row.FACTORYPRODUCTNAME);
    }

    //编辑出货订单关联
    var edit_order = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dg_order");

        var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl_order, [
            EasyUI.window.button("icon-save", "修改", function () {
                appendOrder(r);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })], function () {
            orderValuesAdd(r);
        });
    }

    function cannel() {
        var r = EasyUI.grid.getOnlyOneSelected("dg");

        Dialog.confirm(function () {
            JQ.ajax(cannelUrl, "post", {
                ids: r.ID
            }, function (data) {
                filter();
            });
        });
    }

    //删除出货订单关联
    var doDelete_order = function () {
        var rs = EasyUI.grid.getSelections("dg_order");
        if (rs.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        for (var i = rs.length - 1; i >= 0; i--) {
            var index = $("#dg_order").datagrid("getRowIndex", rs[i]);

            var _rs = $("#dg_product").datagrid("getRows");
            for (var j = _rs.length - 1; j >= 0; j--) {
                //删除订单的产品
                if (_rs[j].PACKINGNUMBER == rs[i].PACKINGNUMBER) {
                    $("#dg_product").datagrid("deleteRow", j);
                }
            }
            $("#dg_order").datagrid("deleteRow", index);
        }
        /* for (var i = rs.length - 1; i >= 0; i--) {
            var _rs = $("#dg_product").datagrid("getRows");
            console.log(rs);
            for (var j = _rs.length - 1; j >= 0; j--) {
                //删除订单的产品
                if (_rs[j].SALESORDERSUBCODE == rs[i].SALESORDERCODE) {
                    $("#dg_product").datagrid("deleteRow", j);
                }
            }
            //删除订单

        } */
    }

    function onClickRow(index, row) {
        $("#dg_order").datagrid("beginEdit", index);
    }

    function saveData() {
        if (!$("#deliveryPlanForm").form("validate")) {
            Tip.warn("请填写发货时间/要货公司");
            return;
        }
        console.log(endEdit());
        if (!endEdit()) {
            return;
        }
        /* if($('#deliveryDate').textbox('getValue')!=null&&$('#deliveryDate').textbox('getValue')!=undefined&&$('#deliveryDate').textbox('getValue')!=""){
            Tip.warn("请填写发货时间");
            return;
        } */
        var rows = $('#dg_product').datagrid('getData').rows;

        if (rows.length == 0) {
            Tip.warn("请添加要发货的产品");
            return;
        }
        for (var i = 0; i < rows.length; i++) {
            $("#dg_product").datagrid("endEdit", i);
            if ((rows[i].DELIVERYCOUNT == 0 || rows[i].DELIVERYCOUNT == null) && rows[i].DELIVERYSUITCOUNT == 0) {
                Tip.warn("订单号" + rows[i].SALESORDERSUBCODE + "出货数量不能全部为0");
                return;
            }
        }

        var data = JQ.getFormAsJson("deliveryPlanForm");
        data.deliveryDate = new Date(data.deliveryDate+" 08:00:00");
        var orders = [];

        var orderData = $('#dg_order').datagrid('getData').rows;

        for (var i = 0; i < orderData.length; i++) {
            $("#dg_order").datagrid("endEdit", i);
            var r = {};
            r.pn = orderData[i].PN;
            r.isFinished = orderData[i].ISFINISHED;
            r.packingNumber = orderData[i].PACKINGNUMBER;
            r.ladingCode = orderData[i].LADINGCODE;
            r.boxNumber = orderData[i].BOXNUMBER;
            r.serialNumber = orderData[i].SERIALNUMBER;
            r.count = orderData[i].COUNT;
            r.weight = orderData[i].WEIGHT;
            r.size = orderData[i].SIZE;
            r.plate = orderData[i].PLATE;
            orders.push(r);
        }
        data.orderDatas = orders;

        var products = $('#dg_product').datagrid('getData').rows;
        var productData = [];

        for (var i = 0; i < products.length; i++) {
            $("#dg_product").datagrid("endEdit", i);
            var r = {};
            r.pn = products[i].PN;
            r.packingNumber = products[i].PACKINGNUMBER;
            r.salesOrderDetailId = products[i].SALESORDERDETAILID;
            r.salesOrderSubCode = products[i].SALESORDERSUBCODE;
            r.batchCode = products[i].BATCHCODE;
            r.productId = products[i].PRODUCTID;
            r.memo = products[i].MEMO;
            r.deliveryId = products[i].DELIVERYID;
            r.deliveryCount = products[i].DELIVERYCOUNT;
            r.deliverySuitCount = products[i].DELIVERYSUITCOUNT;
            r.partName = products[i].PARTNAME;
            r.partID = products[i].PARTID;
            r.consumerProductName = products[i].CONSUMERPRODUCTNAME;
            r.factoryProductName = products[i].FACTORYPRODUCTNAME;
            r.productModel = products[i].PRODUCTMODEL;
            r.customerMaterialCodeOfFP = products[i].CUSTOMERMATERIALCODEOFFP;
            productData.push(r);
        }

        data.productDatas = productData;
        console.log(data.productDatas);
        Loading.show('保存中');
        $.ajax({
            url: path + "planner/deliveryPlan/add",
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (data) {
                filter();
                Loading.hide();
                Dialog.close(dialogId);

            },
            error: function (data) {
                Loading.hide();
            }

        });
    }

    //添加出货详情列表
    var add_product = function () {
        var wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl_product,
            [EasyUI.window.button("icon-save", "保存", function () {
                appendProduct();
                Dialog.close(wid);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })], function () {

                Dialog.more(wid);
            });
    }

    //编辑出货详情列表
    var edit_product = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dg_product");
        var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl_product,
            [EasyUI.window.button("icon-save", "修改", function () {
                appendProduct(r);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
                productValueAdd(r);
            });
    }
    /* 	//删除出货详情列表
     var doDelete_product = function() {
     var r = EasyUI.grid.getSelections("dg_product");
     if (r.length == 0) {
     Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
	 return;
	 }
	 for (var a = 0; a < r.length; a++) {
	 var row = r[a];
	 var rowIndex = $("#dg_product").datagrid("getRowIndex", row);
	 $("#dg_product").datagrid('deleteRow', rowIndex);
	 }

	 } */
    //查询
    function filter() {
        EasyUI.grid.search("dg", "deliveryPlanSearchForm");
    }

    var dialogId;
    //添加出货计划
    var add = function () {
        dialogId = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
            EasyUI.window.button("icon-save", "保存", function () {
                saveData();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId)
            })], function () {
            Dialog.more(dialogId);
            $("#" + dialogId).dialog("maximize");
            // Loading.show();
            // $.ajax({
            // 	url : path + "planner/deliveryPlan/serial/"
            // 			+ $("#deliveryType").val(),
            // 	type : "get",
            // 	dataType : "text",
            // 	success : function(data) {
            // 		$("#deliveryCode").textbox("setValue", data);
            // 		Loading.hide();
            // 	},
            // 	error : function() {
            // 		Loading.hide();
            // 	}
            // });
        });
    }

    var addTc = function () {
        dialogId = Dialog.open("添加", dialogWidth, dialogHeight, addtcUrl, [
            EasyUI.window.button("icon-save", "保存", function () {
                saveData();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId)
            })], function () {
            Dialog.more(dialogId);
            $("#" + dialogId).dialog("maximize");
            // Loading.show();
            // $.ajax({
            // 	url : path + "planner/deliveryPlan/serial/"
            // 			+ $("#deliveryType").val(),
            // 	type : "get",
            // 	dataType : "text",
            // 	success : function(data) {
            // 		$("#deliveryCode").textbox("setValue", data);
            // 		Loading.hide();
            // 	},
            // 	error : function() {
            // 		Loading.hide();
            // 	}
            // });
        });
    }

    function product_rowStyle(index, row) {
        if (parseInt(row.DELIVERYCOUNT) > parseInt(row.KCL)) {
            return 'background-color:#9cd9f5;';
        } else {
            return 'background-color:#6293BB;color:#fff;';
        }
        if (parseInt(row.DELIVERYSUITCOUNT) > parseInt(row.KCL)) {
            return 'background-color:#9cd9f5;';
        } else {
            return 'background-color:#6293BB;color:#fff;';
        }
    }

    //编辑出货计划
    var edit = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.AUDITSTATE > 0) {
            Tip.warn("不能编辑审查状态为审核中和已通过的计划");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl
            + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                saveData();
                filter();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
            $("#" + dialogId).dialog("maximize");
            $("#dg_order").datagrid("loadData", orderDatas);
            $("#dg_product").datagrid("loadData", productDatas);

        });
    }
    var editTc = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.AUDITSTATE > 0) {
            Tip.warn("不能编辑审查状态为审核中和已通过的计划");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editTcUrl
            + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                saveData();
                filter();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
            $("#" + dialogId).dialog("maximize");
            $("#dg_order").datagrid("loadData", orderDatas);
            $("#dg_product").datagrid("loadData", productDatas);

        });
    }
    var editOrder = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dg");

        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editOrderUrl
            + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                saveData();
                filter();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
            $("#" + dialogId).dialog("maximize");
            $("#dg_order").datagrid("loadData", orderDatas);
            $("#dg_product").datagrid("loadData", productDatas);

        });
    }

    /**
     * 双击行，弹出编辑
     */
    var dbClickEdit = function (index, row) {
        if (row.AUDITSTATE > 0) {
            Tip.warn("不能编辑审查状态为审核中和已通过的计划");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl
            + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                saveData();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
            $("#" + dialogId).dialog("maximize");
            $("#dg_order").datagrid("loadData", orderDatas);
            $("#dg_product").datagrid("loadData", productDatas);
        });
    }

    //删除出货计划
    var doDelete = function () {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }

        var ids = [];
        for (var i = 0; i < r.length; i++) {
            if (r[i].AUDITSTATE > 0) {
                Tip.warn("不能删除审查状态为审核中和已通过的计划");
                return;
            }
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    //关闭出货计划
    var doClose = function () {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length != 1) {
            Tip.warn("请选择唯一出货计划");
            return;
        }

        if (r[0].ISCLOSED == 1) {
            Tip.warn("该出库计划单已经关闭!");
            return;
        }

        if (r[0].ISCOMPLETE == 1) {
            Tip.warn("该出库计划已完成!");
            return;
        }

        var userName = "${userName}";
        if (r[0].USERNAME != userName) {
            Tip.warn("不能关闭他人下达的出库计划!");
            return;
        }

        Dialog.confirm(function () {
            JQ.ajax(closeUrl, "post", {
                id: r[0].ID
            }, function (data) {
                filter();
            });
        });
    }

    var doReleaseDeliveryPlan = function () {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length != 1) {
            Tip.warn("请选择唯一出货计划");
            return;
        }

        Dialog.confirm(function () {
            JQ.ajax(releaseDeliveryPlanUrl, "post", {
                id: r[0].ID
            }, function (data) {
                Tip.warn("恢复成功");
                filter();
            });
        });
    }

    var copy = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        var userName = "${userName}";
        if (r.USERNAME != userName) {
            Tip.warn("不能复制他人下达的订单");
            return;
        }

        if (r.AUDITSTATE != 2) {
            Tip.warn("审核通过的订单才可以复制");
            return;
        }
        Dialog.confirm(function () {
            JQ.ajax(copy1, "get", {
                id: r.ID
            }, function (data) {
                filter();
            });
        });
    }
    var unbindingUrl = path + "/planner/deliveryPlan/unbinding";
    var unbinding = function () {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        if (r.length != 1) {
            Tip.warn("只能解绑选中行");
            return;
        }
        var id = r[0].ID;
        JQ.ajax(unbindingUrl, "post", {
            id: id
        }, function (data) {
            Tip.success("解绑成功");
        });
    };

    function formatterDetail(val, row, index) {
        return '<a href="javascript:void(0)" onclick="checkDetail(\'' + row.ID
            + '\')" >查看</a>';
    }

    function formatterReviewState(val, row, index) {
        if (row.AUDITSTATE == 1) {
            return "审核中";
        } else if (row.AUDITSTATE == 2) {
            return "已通过";
        } else if (row.AUDITSTATE == 0) {
            return "未审核";
        } else if (row.AUDITSTATE == -1) {
            return "未通过";
        }
    }

    var selectUserWindowId;

    function selectUser() {
        selectUserWindowId = Dialog.open("选择用户", 900, 500, path
            + "selector/user?singleSelect=true", [
            EasyUI.window.button("icon-ok", "选择", function () {
                var row = $("#_common_user_dg").datagrid("getChecked");
                if (row.length == 0) {
                    Tip.warn("至少选择一个用户");
                    return;
                }
                $("#deliveryBizUserName").searchbox("setValue",
                    row[0].USERNAME);
                $("#deliveryBizUserId").val(row[0].ID);
                Dialog.close(selectUserWindowId);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(selectUserWindowId);
            })], function () {
        }, function () {
            Dialog.close(selectUserWindowId)
        });
    }

    function _common_user_dbClickRow(index, row) {
        $("#deliveryBizUserName").searchbox("setValue", row.USERNAME);
        $("#deliveryBizUserId").val(row.ID);
        Dialog.close(selectUserWindowId);
    }

    //查看审核
    var dialogId;

    function view() {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r == null)
            return;
        var code = r.DELIVERYCODE.substring(0, 2);
        if (code == "NX") {
            dialogId = Dialog.open("查看审核状态", 700, 400, nxUrl.replace("{id}",
                r.ID), [EasyUI.window.button("icon-cancel", "关闭",
                function () {
                    Dialog.close(dialogId);
                })], function () {
                $("#" + dialogId).dialog("maximize");
            });
        } else if (code == "WX") {
            dialogId = Dialog.open("查看审核状态", 700, 400, wxUrl.replace("{id}",
                r.ID), [EasyUI.window.button("icon-cancel", "关闭",
                function () {
                    Dialog.close(dialogId);
                })], function () {
                $("#" + dialogId).dialog("maximize");
            });
        }

    }

    function _commons_product_dg_onBeforeLoad_callback() {
        if (Assert.isEmpty($("#commonProductIds").val())) {
            return false;
        }
        return true;
    }

    //导出
    function exportExcel() {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        // var id=r.ID
        // alert(id);
        /* var ids = [];
        for (var i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        alert(ids.length); */
        window.open(exportUrl + "?id=" + r.ID);
    }

    function exportDeliveryExcel() {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }

        var ids = [];
        for (var i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }

        window.open(exportDeliveryExcelUrl + "?ids=" + ids);
    }

    function downLoadImage() {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        var ids = [];
        for (var i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(downLoadUrl + "?ids=" + ids);
    }

    function orderDateFormat(value, row, index) {
        if (value == undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function exportFormat(value, row, index) {
        return value == 0 ? "外销" : "内销";
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

    function getSerial() {
        Loading.show();
        $.ajax({
            url: path + "planner/deliveryPlan/serial/"
                + $("#deliveryType").val(),
            type: "get",
            dataType: "text",
            success: function (data) {
                $("#deliveryCode").textbox("setValue", data);
                Loading.hide();
            },
            error: function () {
                Loading.hide();
            }
        });
    }

    function commit() {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        var userName = "${userName}";
        if (r.USERNAME != userName) {
            Tip.warn("不能提审他人下达的订单");
            return;
        }
        if (r.AUDITSTATE > 0) {
            Tip.warn("不能重复提交审核");
            return;
        }
        var id = r.ID;
        dialogId = Dialog.open("提交审核", 400, 120, commitAudit + "?id=" + id, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("editAuditProduce", commitUrl, function (
                    data) {
                    filter();
                    if (Dialog.isMore(dialogId)) {
                        Dialog.close(dialogId);
                    } else {
                        Dialog.close(dialogId);
                    }
                })

            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
            var code = r.DELIVERYCODE;
            console.log(code.substring(0, 2));
            if (code.substring(0, 2) == "NX") {
                $("#editAuditProduce #name").textbox("setValue",
                    "内销出货计划审核，单号：" + r.DELIVERYCODE);
            } else {
                $("#editAuditProduce #name").textbox("setValue",
                    "外销出货计划审核，单号：" + r.DELIVERYCODE);
            }

        });
    }

    function formatterDate(value, row, index) {
        if (value == undefined)
            return "";
        return value.substring(0, 10);
    }

    function formatterState(value, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    function formatterDetail(val, row, index) {
        return '<a href="javascript:void(0)" onclick="checkDetail(\'' + row.ID
            + '\')" >查看</a>';
    }

    //查看功能
    function checkDetail(id) {
        var wid = Dialog.open("查看", dialogWidth, dialogHeight, checkUrl
            + "?id=" + id, [EasyUI.window.button("icon-cancel", "关闭",
            function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
            $("#" + dialogId).dialog("maximize");
            $("#dg_order").datagrid("loadData", orderDatas);
            $("#dg_product").datagrid("loadData", productDatas);
        });
    }

    var editingRow;

    function onBeforeEdit(index, row) {
        editingRow = row;
    }

    function loadOldBatchCode(param) {
        param.orderId = editingRow.SALESORDERDETAILID;
        param.partId = editingRow.PARTID;
        param.isNew = false;
    }

    function onSelect(index, row) {
        <%-- var img=document.getElementById("image");
        img.src='<%=hostPath%>/barcodePic/'+row.PACKINGNUMBER+".PNG"; --%>
    }

    function searchProduct() {
        var r = EasyUI.grid.getOnlyOneSelected("dg_order");
        var wid = Dialog.open("查看", dialogWidth, dialogHeight, searchUrl
            + "?id=" + r.ID, [EasyUI.window.button("icon-cancel", "关闭",
            function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
            $("#" + dialogId).dialog("maximize");

        });
    }

    //给关闭的出库单价灰色的下划线

    function rowStyler(index, row) {
        var style = "";
        if (row.ISCLOSED == 1) {
            style += "text-decoration:line-through;background: #b8b5bd;";
        } else if (row.ISRED == 1) {
            style += "background: #9cd9f5;";
        } else if (row.ISDJ == 1) {
            style += "background: #FFB6C1;";
        }

        return style;

    }

    /**********************************************以上为非套材发货，以下为套材发货*******************************************************/
</script>
