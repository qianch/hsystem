<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加销售订单
    const addUrl = path + "salesOrder/add";
    //编辑销售订单
    const editUrl = path + "salesOrder/edit";
    //删除销售订单
    const deleteUrl = path + "salesOrder/delete";
    //打开提交审核页面
    const _auditCommitUrl = path + "selector/commitAudit";
    //发送提交审核数据
    const auditCommitUrl = path + "salesOrder/commitAudit";
    //关闭订单
    const closeUrl = path + "salesOrder/close";
    //修改完成状态
    const finish = path + "salesOrder/complete";
    const planed = path + "salesOrder/isPlaned";
    const dialogWidth = 700, dialogHeight = 500;
    const exportUrl = path + "salesOrder/export";
    let action = "add";
    const orderProduct = path + "planner/produce/order/list2";
    const currentConsumerCode = "";
    //获取要完成订单的详情
    const showProduce = path + "planner/produce/showProduce";
    const setAllocated = path + "planner/produce/allocated";

    //查询
    function filter() {
        //EasyUI.grid.search("dg", "salesOrderSearchForm");
        EasyUI.grid.search("orderProductSelect", "produceSalesOrderForm");
    }

    function reloadProduceOrder() {
        /* console.log(111);
        EasyUI.grid.search("orderProductSelect", "produceSalesOrderForm"); */
        const info = JQ.getFormAsJson("produceSalesOrderForm");
        if (info["filter[complete]"] === 1) {
            JQ.ajaxGet(orderProduct + "?info=" + 1, function (data) {
                $("#orderProductSelect").datagrid("loadData", data);
            });
        } else if (info["filter[complete]"] === 0) {
            JQ.ajaxGet(orderProduct + "?info=" + 0, function (data) {
                $("#orderProductSelect").datagrid("loadData", data);
            });
        } else {
            JQ.ajaxGet(orderProduct + "?info=" + null, function (data) {
                $("#orderProductSelect").datagrid("loadData", data);
            });
        }
    }

    let dialogId;

    //添加销售订单
    const add = function () {
        action = "add";
        dialogId = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            Dialog.more(dialogId);
            $("#" + dialogId).dialog("maximize");
            $("#salesOrderDate").datebox("setValue", new Calendar().format("yyyy-MM-dd"));
        });
    };

    //编辑销售订单
    const edit = function () {
        action = "edit";
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录不能编辑！");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
            $("#product_dg").datagrid("loadData", details);
        });
    };

    /**
     * 保存表单信息
     * 保存表单的时候，前端会报错，是因为验证表格行的时候，
     * 首先使行进入编辑状态，验证单元格中的编辑器的值，是否符合要求，
     * 这时候会加载combobox，但是验证的速度，远大于combobox的请求速度，
     * 所以验证通过后，这行就结束编辑了，
     * 这时候combobox的值再返回回来，找不到对应的combobox了，
     * 所以会报错，可以不管
     */
    function saveForm() {
        if ($("#salesOrderForm").form("validate")) {
            if ($("#product_dg").datagrid("getRows").length != 0) {
                if (endEdit()) {
                    const order = JQ.getFormAsJson("salesOrderForm");
                    order.details = $("#product_dg").datagrid("getData").rows;
                    $.ajax({
                        url: path + "salesOrder/" + action,
                        type: 'post',
                        dataType: 'json',
                        contentType: 'application/json',
                        data: JSON.stringify(order),
                        success: function (data) {
                            Dialog.close(dialogId);
                            filter();
                        }
                    });
                }
            } else {
                Tip.warn("请选择订单产品！");
            }
        }
    }

    let enableFilterd = false;

    function dgLoadSuccess() {
        $("#orderProductSelect").datagrid("clearSelections");
        if (enableFilterd) return;
        enableFilterd = true;
        $("#orderProductSelect").datagrid("enableFilter", [{
            "field": "CONSUMERNAME",
            "type": "textbox"
        }, {
            "field": "CONSUMERPRODUCTNAME",
            "type": "textbox"
        }, {
            "field": "FACTORYPRODUCTNAME",
            "type": "textbox"
        }, {
            "field": "SALESORDERDATE",
            "type": "textbox"
        }, {
            "field": "PRODUCTCOUNT",
            "type": "textbox"
        }, {
            "field": "PRODUCEDROLLS",
            "type": "textbox"
        }, {
            "field": "PRODUCEDTRAYS",
            "type": "textbox"
        }, {
            "field": "TOTALMETRES",
            "type": "textbox"
        }, {
            "field": "PRODUCECOUNT",
            "type": "textbox"
        }, {
            "field": "PRODUCTMODEL",
            "type": "textbox"
        }, {
            "field": "PRODUCTWIDTH",
            "type": "textbox"
        }, {
            "field": "PRODUCTROLLLENGTH",
            "type": "textbox"
        }, {
            "field": "PRODUCTROLLWEIGHT",
            "type": "textbox"
        }, {
            "field": "USERNAME",
            "type": "textbox"
        }, {
            "field": "SALESORDERISEXPORT",
            "type": "textbox"
        }, {
            "field": "SALESORDERTYPE",
            "type": "textbox"
        }, {
            "field": "PRODUCTPROCESSCODE",
            "type": "textbox"
        }, {
            "field": "PRODUCTPROCESSBOMVERSION",
            "type": "textbox"
        }, {
            "field": "PRODUCTPACKAGINGCODE",
            "type": "textbox"
        }, {
            "field": "PRODUCTPACKAGEVERSION",
            "type": "textbox"
        }, {
            "field": "PRODUCTROLLCODE",
            "type": "textbox"
        }, {
            "field": "PRODUCTBOXCODE",
            "type": "textbox"
        }, {
            "field": "PRODUCTTRAYCODE",
            "type": "textbox"
        }, {
            "field": "DELIVERYTIME",
            "type": "textbox"
        }, {
            "field": "PRODUCTMEMO",
            "type": "textbox"
        }, {
            "field": "SALESORDERMEMO",
            "type": "textbox"
        }, {
            "field": "EDITTIMES",
            "type": "textbox"
        }, {
            "field": "SALESORDERSUBCODE",
            "type": "textbox"
        }, {
            "field": "SALESORDERSUBCODEPRINT",
            "type": "textbox"
        }, {
            "field": "ISCOMPLETE",
            "type": "combobox",
            "options": {
                icons: [],
                valueField: "v",
                textField: "t",
                data: [{v: "", t: "全部"}, {v: "0", t: "未完成"}, {v: "1", t: "已完成"}],
                onChange: function (nv, ov) {
                    $("#orderProductSelect").datagrid('addFilterRule', {
                        field: 'ISCOMPLETE',
                        op: 'equal',
                        value: nv
                    });
                    $("#orderProductSelect").datagrid('doFilter');
                }
            }
        }, {
            "field": "ISPLANED",
            "type": "combobox",
            "options": {
                icons: [],
                valueField: "v",
                textField: "t",
                data: [{v: "", t: "全  部"}, {v: "0", t: "未分配"}, {v: "1", t: "部分已分配"}, {
                    v: "2",
                    t: "全部已分配"
                }],
                onChange: function (nv, ov) {
                    $("#orderProductSelect").datagrid('addFilterRule', {
                        field: 'ISPLANED',
                        op: 'equal',
                        value: nv
                    });
                    $("#orderProductSelect").datagrid('doFilter');
                }
            }
        }]);
        $(".datagrid-filter[name='X']").remove();
        $(".datagrid-filter[name='TOTALWEIGHT']").remove();
        $(".datagrid-filter[name='RC']").remove();
        $(".datagrid-filter[name='TC']").remove();
        $(".datagrid-filter[name='PRODUCECOUNT']").remove();
        //$(".datagrid-filter[name='SALESORDERDATE']").remove();
    }

    function orderDateFormat(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function exportFormat(value, row, index) {
        //（1胚布订单,0外销,-1内销）
        switch (value) {
            case 1:
                return "内销";
            case 0:
                return "外销";
            case -1:
                return "胚布订单";
        }
        /* return value == 0 ? "外销" : "内销"; */
    }

    function editTimesFormatter(value, index, row) {
        return (isEmpty(value) ? 0 : value) + "次";
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

    function detailRowStyler(index, row) {
        let style = "";
        if (isEmpty(row.CLOSED) || row.CLOSED === 0) {
        } else {
            style += "text-decoration:line-through;";
        }
        if (row.EDITTIMES != null && row.EDITTIMES > 0) {
            style += "animation: changed 1s infinite ease-in-out;animation-direction: alternate;";
        }
        if (row.ISCOMPLETE === 1 && row.ISPLANED === 1) {
            style += "background: #8edd9b;";
        }
        return style;
    }


    function formatterReviewState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    function closeOrder() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const index = null;
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
            if (r[i].AUDITSTATE !== 2) {
                Tip.warn("审核通过的订单才可以关闭");
                return;
            }
        }
        Dialog.confirm(function () {
            JQ.ajaxGet(closeUrl + "?_ids=" + ids.join(","), function (data) {
                Tip.success("操作成功");
                filter();
            })
        });
    }

    function stateFormatter(value, row, index) {
        if (value == null || value === 0) {
            return "<label style='background:green;width:100%;display: inline-block;color:white;text-align:center;'>正常</label>";
        }
        return "<label style='background:red;width:100%;display: inline-block;color:white;text-align:center;'>关闭</label>";
    }

    function isCompleteFormatter(value, row, index) {
        if (value == null || value === 0) {
            return "未完成";
        } else if (value === 1) {
            return "已完成";
        }
    }

    /* 	function orderRowClick(index, row) {
            $("#salesOrderMemo").panel({
                content : "<pre>" + row.SALESORDERMEMO + "</pre>"
            });
        } */

    function completeOrder() {
        const r = EasyUI.grid.getSelections("orderProductSelect");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajaxPost(finish, {
                _ids: ids.toString()
            }, function () {
                filter();
            })
        }, "确定该订单已分配？");
    }

    //生产完成
    function finishProduct() {
        let i;
        const r = EasyUI.grid.getSelections("orderProductSelect");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        for (i = 0; i < r.length; i++) {
            if (r[i].ISCOMPLETE === 1) {
                Tip.warn("所选订单中有已经完成订单");
                return;
            }
            if (r[i].ISPLANED !== 1) {
                Tip.warn("所选订单中有未分配订单");
                return;
            }
        }
        const ids = [];
        for (i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        dialogId = Dialog.open("完成以下订单?", 600, 400, showProduce + "?Ids=" + ids.toString(), [EasyUI.window.button("icon-ok", "完成", function () {
            JQ.ajaxPost(finish, {
                _ids: ids.toString()
            }, function () {
                filter();
                Dialog.close(dialogId);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {

        });
    }

    //分配完成
    function hasPlan() {
        const r = EasyUI.grid.getSelections("orderProductSelect");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajaxGet(setAllocated + "?ids=" + ids.toString(), function (data) {
                Tip.success("操作成功");
                filter();
            });
        }, "确认已完成?");
    }

    //查看工艺bom明细
    function bomVersionView(value, row, index) {
        if (value == null) {
            return "";
        } else if (row.PRODUCTPROCESSBOMVERSION == null || row.PRODUCTPROCESSBOMVERSION === "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_bomVersionView(" + row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value + "</a>"
        }
    }

    function _bomVersionView(procBomId, isTc) {
        if (procBomId == null) {
            Tip.error("工艺版本错误，请重新编辑产品");
            return;
        }
        let viewUrl = "";
        if (isTc === 1) {
            viewUrl = path + "selector/view/tc?procBomId=" + procBomId;
        } else {
            viewUrl = path + "selector/view/ftc?procBomId=" + procBomId;
        }
        dialogId = Dialog.open("查看工艺bom明细", 700, 400, viewUrl, [EasyUI.window.button("icon-cancel", "关闭",
            function () {
                Dialog.close(dialogId);
            })], function () {
            $("#" + dialogId).dialog("maximize");
            if (isTc !== 1) {
                for (let a = 0; a < details.length; a++) {
                    _common_bomDetail_data(details[a]);
                }
            }
        });
    }

    //查看包装bom明细
    function packBomView(value, row, index) {
        if (value == null) {
            return "";
        } else if (value === "无包装") {
            return "";
        } else if (row.PRODUCTPACKAGEVERSION == null || row.PRODUCTPACKAGEVERSION === "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_packBomView(" + row.PACKBOMID + ")'>" + value + "</a>"
        }
    }

    function orderRowClick(index, row) {
        /* $("#salesOrderMemo").panel({
            content:"<pre>"+(isEmpty($.trim(row.SALESORDERMEMO))||$.trim(row.SALESORDERMEMO) =="" ? "订单备注:</br>无":"销售订单备注:</br>"+$.trim(row.SALESORDERMEMO))+(isEmpty($.trim(row.PRODUCTMEMO))||$.trim(row.PRODUCTMEMO) =="" ? "</br></br></br>产品备注:</br>无":"</br></br></br>产品备注:</br>"+$.trim(row.PRODUCTMEMO))+"</pre>"
        }); */
        $("#orderMemo").html(isEmpty($.trim(row.SALESORDERMEMO)) ? "" : ("<pre>" + $.trim(row.SALESORDERMEMO) + "</pre>"));
        $("#productMemo").html(isEmpty($.trim(row.PRODUCTMEMO)) ? "" : ("<pre>" + $.trim(row.PRODUCTMEMO) + "</pre>"));
        $("#partCounts").datagrid("loadData", []);
        if (row.PRODUCTISTC !== 1) return;
        Loading.show();
        JQ.ajaxPost(path + "bom/tc/ver/parts/" + row.PROCBOMID + "/" + row.PRODUCTCOUNT + "/" + row.ID, {}, function (data) {
            Loading.hide();
            $("#partCounts").datagrid("loadData", data);
        }, function (data) {
            Loading.hide();
        });
    }

    function _packBomView(packBomId) {
        if (packBomId == null || packBomId === "") {
            Tip.error("包装工艺错误，请重新编辑产品");
            return;
        }
        const viewUrl = path + "selector/view/bc?packBomId=" + packBomId;
        dialogId = Dialog.open("查看包装bom明细", 700, 400, viewUrl, [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            $("#" + dialogId).dialog("maximize");
            for (let a = 0; a < details.length; a++) {
                _common_bcBomDetail_data(details[a]);
            }
        });
    }

    function isPlanedFormatter(value, row, index) {
        if (!row.ALLOCATECOUNT || row.ALLOCATECOUNT === 0) {
            return "未分配";
        } else if (row.ALLOCATECOUNT > 0 && row.ALLOCATECOUNT < row.TOTALWEIGHT) {
            return "部分已分配";
        } else {
            return "全部已分配";
        }
    }
</script>