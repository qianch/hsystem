<!--
作者:徐波
日期:2016-12-3 16:35:51
页面:箱条码JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加箱条码
    const addUrl = path + "boxBarcode/add";
    //编辑箱条码
    const editUrl = path + "boxBarcode/edit";
    //删除箱条码
    const deleteUrl = path + "boxBarcode/delete";
    const showReplayBarcodePrinterPage = path + "individualprinter/showReplayBarcodePrinterPage";
    const clearBarCodeUrl = path + "boxBarcode/clearBox";
    const doPrinter = path + "individualprinter/doReplayPrintBarcode";
    const reIndividualPrint = path + "individualprinter/reIndividualPrint";
    const dialogWidth = 700, dialogHeight = 350;
    const FindPrintsUrl = path + "boxBarcode/FindPrints";
    const editBacodeUrl = path + "boxBarcode/editBacode";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "boxBarcodeSearchForm");
    }

    function dgLoadSuccess() {
        $("#dg").datagrid("enableFilter");
        $(".datagrid-filter[name='CUSTOMERBARCODE']").remove();
        $(".datagrid-filter[name='AGENTBARCODE']").remove();
        $(".datagrid-filter[name='SALESORDERCODE']").remove();
        $(".datagrid-filter[name='BATCHCODE']").remove();
        $(".datagrid-filter[name='PRINTDATE']").remove();
        $(".datagrid-filter[name='PARTNAME']").remove();
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}boxBarcode/list",
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                        url: FindPrintsUrl + "?id=" + row.ID,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        height: 'auto',
                        columns: [[
                            {
                                field: 'KEY',
                                title: '打印属性',
                                width: 15,
                            }, {
                                field: 'VALUE',
                                title: '打印内容',
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
                            if (row.length === 0) {
                                return 'background-color:yellow;color:blue;font-weight:bold;';
                            }
                        }
                    }
                );
            },
            rowStyler: function (index, row) {
                if (row.ONTHEWAYCOUNT > 0) {
                    return 'background-color:pink;color:blue;font-weight:bold;';
                }
            }
        });
    });

    const printUrl = path + "printer/rePrint";

    function print() {
        if ($('#pName').combobox("getValue") === '') {
            Tip.warn("请选择打印机");
            return;
        }
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(printUrl, "post", {
                ids: ids.toString(),
                pName: $('#pName').combobox("getValue"),
                type: 'box'
            }, function (data) {
                /* filter(); */
                Tip.warn(data);
            });
        });
    }

    //添加箱条码
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("boxBarcodeForm", addUrl, function (data) {
                        filter();
                        if (Dialog.isMore(wid)) {
                            Dialog.close(wid);
                            add();
                        } else {
                            Dialog.close(wid);
                        }
                    })
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid)
                })], function () {
                Dialog.more(wid);
            }
        );
    };

    //编辑箱条码
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("boxBarcodeForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("boxBarcodeForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除箱条码
    const doDelete = function () {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    };

    let selectConsumerWindowId;

    function selectConsumer() {
        selectConsumerWindowId = Dialog.open("选择客户", 900, 500, path + "selector/consumer?singleSelect=false", [EasyUI.window.button("icon-ok", "选择", function () {
            const row = $("#_common_consumer_dg").datagrid("getChecked");
            if (row.length === 0) {
                Tip.warn("至少选择一个客户");
                return;
            }
            $("#consumerName").searchbox("setValue", row[0].CONSUMERNAME);
            $("#consumerId").val(row[0].ID);
            $.ajax({
                type: "post",
                url: path + "btwFile/queryBtwFilebyCustomerId?customerId=" + row[0].ID + "&type=",//请求后台数据
                dataType: "json",
                success: function (json) {
                    $("#btwfileSelect").combobox({//往下拉框塞值
                        data: json,
                        valueField: "v",//value值
                        textField: "t",//文本值
                        panelHeight: "150",
                        onLoadSuccess: function (data) {
                            if (data.length > 0) {
                                $('#btwfileSelect').combobox('select', data[0].value);
                            }
                        }
                    });
                }
            });
            filter();
            Dialog.close(selectConsumerWindowId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectConsumerWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectConsumerWindowId);
        });
    }

    function doPrintBox() {
        if ($('#pName').combobox("getValue") === '') {
            Tip.warn("请选择打印机");
            return;
        }
        if ($('#btwfileSelect').combobox("getText") === '') {
            Tip.warn("请选择标签模版");
            return;
        }
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        Dialog.confirm(function () {
            for (let i = 0; i < r.length; i++) {
                PrintBarCode(r[i].ID);
            }
            filter();
        }, "确认打印标签模版:" + $('#btwfileSelect').combobox("getText"));
    }

    function PrintBarCode(id) {
        JQ.ajax(reIndividualPrint, "post", {
            id: id,
            pName: $('#pName').combobox("getValue"),
            type: 'box',
            btwfileId: $('#btwfileSelect').combobox("getValue"),
            printCount: $('#printCount').numberbox("getValue") || 1,
        }, function (data) {
            Tip.warn(data);
        });
    }

    function clearBox() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(clearBarCodeUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    function editBacode() {
        if ($('#btwfileSelect').combobox("getText") === '') {
            Tip.warn("请选择标签模版");
            return;
        }
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        Dialog.confirm(function () {
            createbacode(r.ID);
        });
    }

    function createbacode(id) {
        JQ.ajax(editBacodeUrl, "post", {
            id: id,
            customerBarCodeRecord: $('#customerBarCodeRecord').val(),
            agentBarCodeRecord: $('#agentBarCodeRecord').val(),
            btwfileId: $('#btwfileSelect').combobox("getValue"),
        }, function (data) {
            filter();
            Tip.warn(data);
        });
    }

    const editPlanDetailPrints = function () {
        isPost = false;
        if ($('#btwfileSelect').combobox("getText") === '') {
            Tip.warn("请选择标签模版");
            return;
        }
        const r = EasyUI.grid.getSelections("dg");
        if (r.length !== 1) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editProducePlanDetailPrintsUrl + "?ProducePlanDetailId=" + r[0].PRODUCEPLANDETAILID + "&btwFileId=" + $('#btwfileSelect').combobox("getValue") + "&tagType=box", [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };
</script>
