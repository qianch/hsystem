<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script type="text/javascript">
    //关闭计划
    const closeUrl = path + "common/close";
    //优先排序
    const sort = path + "planner/weavePlan/sort";
    //取消优先排序
    const cancelSorts = path + "planner/weavePlan/cancelSort";
    //添加编织计划
    const addUrl = path + "planner/weavePlan/add";
    //编织计划状态改成已完成
    const isFinish = path + "planner/weavePlan/isFinish";
    //设备上的计划列表
    const devicePlans = path + "/device/scheduling/plans";
    //删除计划
    const deleteUrl = path + "/device/scheduling/delete";
    //保存设备的计划
    const saveDevicePlansUrl = path + "/device/scheduling/save";
    //保存设备的计划(套材)
    const saveDevicePlansUrl2 = path + "/device/scheduling/savetwo";
    //选择编织计划
    const selectUrl = path + "planner/weavePlan/select";
    //选择编织计划(套材)
    const selectUrl2 = path + "planner/weavePlan/selecttwo";
    //打印
    const showPrinterPage = path + "printer/showPrinterPage";
    const showPrinterPageList = path + "printer/showPrinterPageList";
    const doPrinter = path + "printer/doPrintBarcode";
    const doPrinterList = path + "printer/doPrintBarcodeList";
    //个性化打印
    const showIndividualPrinterPage = path + "individualprinter/showIndividualPrinterPage";
    const doIndividualPrinter = path + "individualprinter/doIndividualPrintBarcode";
    //查看非套材包装任务信息
    const checkFtcPackTask = path + "device/scheduling/checkFtcPackTask";
    // const editProducePlanDetailPrintsUrl = path + "planner/producePlanDetail/editProducePlanDetailPrints";

    function filter() {
        $("#dg").datagrid("uncheckAll");
        EasyUI.grid.search("dg", "dgForm");
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}device/scheduling/list?all=1",
            onBeforeLoad: dgOnBeforeLoad,
        });
        const cal = new Calendar();
        cal.set(Calendar.field.DAY_OF_MONTH, 1);
        $("#start").datebox("setValue", cal.format("yyyy-MM-dd"));
        cal.add(Calendar.field.MONTH, 1);
        cal.add(Calendar.field.DAY_OF_MONTH, -1);
        $("#end").datebox("setValue", cal.format("yyyy-MM-dd"));
        filter();
    });

    function dateFormatter(value, index, row) {
        if (isEmpty(value))
            return "";
        return value.substring(0, 10);
    }

    function checkbjinfo() {
        const rows = $("#dg").datagrid("getSelections");
        if (rows.length == 0) {
            Tip.warn("请选择机台");
            return;
        }
        const wid = Dialog.open("查看信息", 550, 550, path + "planner/weavePlan/checkbjhzinfo?id=" + rows[0].ID, [
            EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
        });
        //$("#weight_dg1234").datagrid("loadData", yxdates);
    }

    function formatterDetail(val, row, index) {
        return '<a href="javascript:void(0)" onclick="checkDetail(\'' + row.DVID
            + '\',\'' + row.YX
            + '\',\'' + row.PARTNAME
            + '\')" >查看</a>';
    }

    //查看功能
    function checkDetail(id, yx, partname) {
        const wid = Dialog.open("查看", 700, 350, path + "planner/weavePlan/checkbjinfo"
            + "?id=" + id + "&yx=" + yx + "&partname=" + partname, [EasyUI.window.button("icon-cancel", "关闭",
            function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
            $("#" + dialogId).dialog("maximize");
            /* $("#dg_order").datagrid("loadData", orderDatas);
            $("#dg_product").datagrid("loadData", productDatas); */
        });
    }

    function dgLoadSuccess() {
        $("#weaveDg").datagrid("enableFilter", [
            {
                "field": "DEVCODE",
                "type": "textbox"
            },
            {
                "field": "SALESORDERSUBCODE",
                "type": "textbox"
            },
            {
                "field": "BATCHCODE",
                "type": "textbox"
            },
            {
                "field": "DELEVERYDATE",
                "type": "textbox"
            },
            {
                "field": "SORTING",
                "type": "textbox"
            },
            {
                "field": "PLANCODE",
                "type": "textbox"
            },
            {
                "field": "PRODUCTMODEL",
                "type": "textbox"
            },
            {
                "field": "PRODUCTWIDTH",
                "type": "textbox"
            },
            {
                "field": "PRODUCTLENGTH",
                "type": "textbox"
            },
            {
                "field": "PRODUCTWEIGHT",
                "type": "textbox"
            },
            {
                "field": "REQUIREMENTCOUNT",
                "type": "textbox"
            },
            {
                "field": "PRODUCEROLLCOUNT",
                "type": "textbox"
            },
            {
                "field": "PARTNAME",
                "type": "textbox"
            },
            {
                "field": "DRAWNO",
                "type": "textbox"
            },
            {
                "field": "PACKAGEDCOUNT",
                "type": "textbox"
            },
            {
                "field": "PRODUCEDTOTALWEIGHT",
                "type": "textbox"
            },
            {
                "field": "TOTALROLLCOUNT",
                "type": "textbox"
            },
            {
                "field": "TOTALTRAYCOUNT",
                "type": "textbox"
            },
            {
                "field": "CONSUMERSIMPLENAME",
                "type": "textbox"
            },
            {
                "field": "CONSUMERCODE",
                "type": "textbox"
            },
            {
                "field": "PRODUCTTYPE",
                "type": "textbox"
            },
            {
                "field": "PROCESSBOMCODE",
                "type": "textbox"
            },
            {
                "field": "PROCESSBOMVERSION",
                "type": "textbox"
            },
            {
                "field": "PROCREQ",
                "type": "textbox"
            },
            {
                "field": "BCBOMCODE",
                "type": "textbox"
            },
            {
                "field": "DRAWINGNO",
                "type": "textbox"
            },
            {
                "field": "ROLLNO",
                "type": "textbox"
            },
            {
                "field": "LEVELNO",
                "type": "textbox"
            },
            {
                "field": "BCBOMVERSION",
                "type": "textbox"
            },
            {
                "field": "REQCOUNT",
                "type": "textbox"
            },
            {
                "field": "PLANTOTALWEIGHT",
                "type": "textbox"
            },
            {
                "field": "PLANASSISTCOUNT",
                "type": "textbox"
            }
        ]);
        $(".datagrid-filter[name='XX']").remove();
    }

    function formatterIsFinish(value, row) {
        if (value === 1) {
            return '已完成';
        }
        if (value === -1) {
            return '未完成';
        }
    }

    function formatterIsClosed(value, row) {
        if (value === 0 || value == null) {
            return '未关闭';
        }
        if (value === 1) {
            return '已关闭';
        }
    }

    function rowStyler(index, row) {
        let style = "";
        if (row.WEAVEPLANPRODUCTTYPE === 1) {
            style += 'background-color:#FFD39B';
        }
        if (row.WEAVEPLANPRODUCTTYPE === 2) {
            style += 'background-color:#FFE7BA';
        }
        if (row.WEAVEPLANPRODUCTTYPE === 3) {
            style += 'background-color:#FFEFDB';
        }
        if (row.WEAVEPLANPRODUCTTYPE === 4) {
            style += 'background-color:#FFF8DC';
        }
        if (isEmpty(row.CLOSED) || row.CLOSED == 0) {
        } else {
            style += "text-decoration:line-through;background: #989696;";
        }
        return style;
    }

    //客户简称为裁剪车间时添加叶型
    function formatterC(value, row) {
        const yx = row.SALESORDERMEMO;
        if (value === "裁剪车间") {
            if (yx) {
                str = yx.split("叶型为：")[1];
            }
            if (str) {
                return value + " (" + str + ")";
            }
        }
        return row.CONSUMERSIMPLENAME;
        /* if (row.ISCOMEFROMTC == 1) {
            return '裁剪车间';
        }
        if (row.ISCOMEFROMTC == "" || row.ISCOMEFROMTC == null) {
            return row.CONSUMERSIMPLENAME;
        }
        if (row.ISCOMEFROMTC == 0) {
            return row.CONSUMERSIMPLENAME;
        } */
    }

    function formatterType(value, row) {
        if (value === 1) {
            return "大卷产品";
        }
        if (value === 2) {
            return "中卷产品";
        }
        if (value === 3) {
            return "小卷产品";
        }
        if (value === 4) {
            return "其他产品";
        }
    }

    function finish() {
        const r = EasyUI.grid.getSelections("weaveDg");
        if (r.length === 0) {
            Tip.warn("请至少选择一个计划");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(isFinish, "post", {
                ids: ids.toString()
            }, function (data) {
                doSelect();
            });
        }, '确认将编织计划设置为已完成，将从设备的任务列表中删除？');
    }

    function doClose() {
        const rows = $("#weaveDg").datagrid("getSelections");
        if (rows.length === 0) {
            Tip.warn("请至少选择一条记录");
            return;
        }
        const type = "WEAVE";
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        Dialog.confirm(function () {
            Loading.show();
            $.ajax({
                url: closeUrl + "?type=" + type + "&ids=" + ids.join(","),
                type: "get",
                dataType: "json",
                success: function (data) {
                    Loading.hide();
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("关闭成功");
                    doSelect();
                }
            });
        }, "确认关闭");
    }

    function ssort() {
        const r = EasyUI.grid.getOnlyOneSelected("weaveDg");
        if (r.CLOSED === 1) {
            Tip.warn("此编织计划已关闭");
            return;
        }
        if (r.ISFINISHED === 1) {
            Tip.warn("此编织计划已完成");
            return;
        }
        Dialog.confirm(function () {
            Loading.show();
            JQ.ajaxPost(sort, {
                id: r.ID
            }, function () {
                Loading.hide();
                doSelect();
            });
        }, "确认优先排序");
    }

    function cancelSort() {
        const r = EasyUI.grid.getOnlyOneSelected("weaveDg");
        if (r.CLOSED === 1) {
            Tip.warn("此编织计划已关闭");
            return;
        }
        if (r.ISFINISHED === 1) {
            Tip.warn("此编织计划已完成");
            return;
        }
        Dialog.confirm(function () {
            Loading.show();
            JQ.ajaxPost(cancelSorts, {
                id: r.ID
            }, function () {
                Loading.hide();
                doSelect();
            });
        }, "是否确认取消优先");
    }

    function doAdd() {
        const rows = $("#dg").datagrid("getSelections");
        if (rows.length === 0) {
            Tip.warn("请选择机台");
            return;
        }
        //selectUrl+"?workShop="+rows[0].NAME 如果区分车间
        const wid = Dialog.open("选择编织任务", 1000, 500, selectUrl + "?workShopCode=" + rows[0].CODE, [EasyUI.window.button("icon-save", "保存", function () {
            let i;
            let rows = $("#dg").datagrid("getSelections");
            const dids = [];
            for (i = 0; i < rows.length; i++) {
                dids.push(rows[i].ID);
            }
            rows = $("#dgg2").datagrid("getSelections");
            if (rows.length === 0) {
                Tip.warn("请选择编织任务");
                return;
            }
            const wids = [];
            for (i = 0; i < rows.length; i++) {
                wids.push(rows[i].ID);
            }
            Loading.show();
            JQ.ajaxPost(saveDevicePlansUrl, {dids: dids.join(","), wids: wids.join(",")}, function (data) {
                Tip.success("保存成功");
                Loading.hide();
                doSelect();
                Dialog.close(wid);
            });
            Dialog.close(selectWeavePlanDialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            Dialog.max(wid);
        });
    }

    function doAdd2() {
        const rows = $("#dg").datagrid("getSelections");
        if (rows.length === 0) {
            Tip.warn("请选择机台");
            return;
        }
        //selectUrl+"?workShop="+rows[0].NAME 如果区分车间
        const wid = Dialog.open("选择编织任务(套材)", 1000, 500, selectUrl2 + "?workShop=" + rows[0].NAME, [EasyUI.window.button("icon-save", "保存", function () {
            let i;
            let rows = $("#dg").datagrid("getSelections");
            const dids = [];
            for (i = 0; i < rows.length; i++) {
                dids.push(rows[i].ID);
            }
            rows = $("#dgg2").datagrid("getSelections");
            if (rows.length === 0) {
                Tip.warn("请选择编织任务");
                return;
            }
            const wids = [];
            for (i = 0; i < rows.length; i++) {
                wids.push(rows[i].PARTID);
            }
            Loading.show();
            JQ.ajaxPost(saveDevicePlansUrl2, {dids: dids.join(","), wids: wids.join(",")}, function (data) {
                Tip.success("保存成功");
                Loading.hide();
                //filter();
                doSelect();
                Dialog.close(wid);
            });
            Dialog.close(selectWeavePlanDialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            Dialog.max(wid);
        });
    }

    function loadSuccess(data) {
    }

    let clicked;

    function loadSuccess2(data2) {
        clicked = false;
    }

    clicked = false;

    function doClick(index, row) {
        if (clicked) return;
        clicked = true;
        const rows = $("#dg").datagrid("getSelections");
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        if (ids.length === 0) {
            Tip.warn("请选择设备");
            $("#weaveDg").datagrid("loadData", []);
            return;
        }
        JQ.ajaxPost(devicePlans, {dids: ids.join(",")}, function (data) {
            $("#weaveDg").datagrid("loadData", data);
        });
    }

    function doSelect(index, row) {
        const rows = $("#dg").datagrid("getSelections");
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        if (ids.length === 0) {
            Tip.warn("请选择设备");
            $("#weaveDg").datagrid("loadData", []);
            return;
        }
        JQ.ajaxPost(devicePlans, {dids: ids.join(",")}, function (data) {
            $("#weaveDg").datagrid("loadData", data);
        });
    }

    function onSelectAll() {
        const rows = $("#dg").datagrid("getSelections");
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        if (ids.length === 0) {
            Tip.warn("请选择设备");
            $("#weaveDg").datagrid("loadData", []);
            return;
        }
        JQ.ajaxPost(devicePlans, {dids: ids.join(",")}, function (data) {
            $("#weaveDg").datagrid("loadData", data);
        });
    }

    function unSelectAll() {
        //$("#dg").datagrid("unselectAll");
        $("#weaveDg").datagrid("loadData", []);
    }

    function doUnSelectAll() {
        $("#dg").datagrid("unselectAll");
        $("#weaveDg").datagrid("loadData", []);
    }

    function isProducing(index, row) {
        if (row.ISPRODUCING === 1) {
            return "background: #03b723; font-weight: bold; height: 25px; color: white;";
        } else {
            if (row.SORT) {
                return "color:orange;font-weight:bold;";
            }
            if (row.ISSTAMP === 1) {
                return "background :#c2c2c2;";
            }
        }
    }

    function singleSelect(single) {
        $("#dg").datagrid({singleSelect: single});
    }

    function doDelete() {
        Dialog.confirm(function () {
            let i;
            if ($("#weaveDg").datagrid("getSelections").length === 0) {
                Tip.warn("请至少选择一条记录");
                return;
            }
            let rows = $("#dg").datagrid("getSelections");
            const dids = [];
            for (i = 0; i < rows.length; i++) {
                dids.push(rows[i].ID);
            }
            rows = $("#weaveDg").datagrid("getSelections");
            if (rows.length === 0) {
                Tip.warn("请选择编织任务");
                return;
            }
            const wids = [];
            for (i = 0; i < rows.length; i++) {
                wids.push(rows[i].ID);
            }
            Loading.show();
            JQ.ajaxPost(deleteUrl, {dids: dids.join(","), wids: wids.join(",")}, function (data) {
                Tip.success("删除成功");
                Loading.hide();
                doSelect();
                Dialog.close(wid);
            });
        }, "确认删除?");
    }

    function beforeLoad(param) {
        if (isEmpty($("#start").datebox("getValue")) || isEmpty($("#end").datebox("getValue"))) {
            return false;
        }
        if (param['filter[dcodes]'] !== undefined) {
            param['filter[dcodes]'] = param['filter[dcodes]'].replace(new RegExp('，', 'gm'), ",");
        }
    }

    function doPrint() {
        const r = EasyUI.grid.getOnlyOneSelected("weaveDg");
        if (r == null) {
            Tip.warn("请选择编织任务");
            return;
        }
        const dialogId = Dialog.open("打印条码", 400, 200, showPrinterPage + "?weaveId=" + r.ID + "&departmentName=" + r.WORKSHOPCODE + "&devCode=" + r.DEVCODE + "&type=roll", [EasyUI.window.button("icon-ok", "打印", function () {
            EasyUI.form.submit("doPrintBarcodeForm", doPrinter, function (data) {
                if (data.url) {
                    document.getElementById("downLoad").innerHTML = '<a href="' + path.replace("mes/", "") + data.url + ' " target="_blank">' + path.replace("mes/", "") + data.url + '</a>';
                    Tip.success("打印成功");
                    Dialog.close(dialogId);
                } else {
                    Tip.error(data);
                }
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
        });
    }

    function doPrintList() {
        const rows = EasyUI.grid.getSelections("weaveDg");
        const departmentCode = "";
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
            if (i > 0 && (rows[i].BATCHCODE !== rows[i - 1].BATCHCODE)) {
                Tip.warn("不能批量打印不同批号的条码");
                return;
            }
        }

        const dialogId = Dialog.open("打印条码", 400, 200, showPrinterPageList + "?ids=" + ids + "&departmentCode=" + rows[0].WORKSHOPCODE + "&devCode=" + rows[0].DEVCODE + "&type=roll", [EasyUI.window.button("icon-ok", "打印", function () {
            EasyUI.form.submit("doPrintBarcodeForm", doPrinterList, function (data) {
                if (data === "") {
                    Tip.success("打印成功");
                    Dialog.close(dialogId);
                } else {
                    Tip.error(data);
                }
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
        });
    }

    function doRollPrint() {
        const r = EasyUI.grid.getOnlyOneSelected("weaveDg");
        if (r == null) {
            Tip.warn("请选择编织任务");
            return;
        }
        const dialogId = Dialog.open("打印卷条码", 400, 200, showIndividualPrinterPage + "?weaveId=" + r.ID + "&departmentCode=" + r.WORKSHOPCODE + "&devCode=" + r.DEVCODE + "&type=roll", [EasyUI.window.button("icon-ok", "打印", function () {
            EasyUI.form.submit("doPrintBarcodeForm", doIndividualPrinter, function (data) {
                if (data.url) {
                    document.getElementById("downLoad").innerHTML = '<a href="' + path.replace("mes/", "") + data.url + ' " target="_blank">' + path.replace("mes/", "") + data.url + '</a>';
                    Tip.success("打印成功");
                    Dialog.close(dialogId);
                } else {
                    Tip.error(data);
                }
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
        });
    }

    //查看非套材包装BOM信息窗口
    const viewPackageBOM = function () {
        const r = EasyUI.grid.getOnlyOneSelected("weaveDg");
        JQ.ajaxGet(path + "planner/weavePlan/getWeavePlanPackTask?wid=" + r.ID, function (data) {
            $("#packTaskInfoWin").window("open");
            $("#packTaskInfoWin").window("setTitle", "订单:[<font color=red>" + r.SALESORDERSUBCODE + "</font>]　　批次:[<font color=red>" + r.BATCHCODE + "</font>]　　产品:[<font color=red>" + r.CONSUMERPRODUCTNAME + "</font>]　　规格:[<font color=red>" + r.PRODUCTMODEL + "</font>]");
            $("#packTaskInfoWin").window("maximize");
            $("#packInfoDg").datagrid({"data": data});
        });
    };
    //查看非套材包装任务
    const viewPackageTask = function () {
        const r = EasyUI.grid.getOnlyOneSelected("weaveDg");
        const wid = Dialog.open("查看非套材包装任务", 1000, 300, checkFtcPackTask + "?ppdId=" + r.PRODUCEPLANDETAILID, [
            EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
        });
    };

    function showMore(vid, code) {
        JQ.ajaxGet(path + "bom/ftcBc/versionInfo?vid=" + vid, function (data) {
            $('#packCode').html("<font color=red>" + code + "</font>");
            $('#rollDiameter').html(data.ROLLDIAMETER);
            $('#rollsPerPallet').html(data.ROLLSPERPALLET);
            $('#palletLength').html(data.PALLETLENGTH);
            $('#palletWidth').html(data.PALLETWIDTH);
            $('#palletHeight').html(data.PALLETHEIGHT);
            $('#bcTotalWeight').html(data.BCTOTALWEIGHT);
            $('#requirement_suliaomo').html(data.REQUIREMENT_SULIAOMO);
            $('#requirement_baifang').html(data.REQUIREMENT_BAIFANG);
            $('#requirement_dabaodai').html(data.REQUIREMENT_DABAODAI);
            $('#requirement_biaoqian').html(data.REQUIREMENT_BIAOQIAN);
            $('#requirement_xiaobiaoqian').html(data.REQUIREMENT_XIAOBIAOQIAN);
            $('#requirement_juanbiaoqian').html(data.REQUIREMENT_JUANBIAOQIAN);
            $('#requirement_tuobiaoqian').html(data.REQUIREMENT_TUOBIAOQIAN);
            $('#requirement_chanrao').html(data.REQUIREMENT_CHANRAO);
            $('#requirement_other').html(data.REQUIREMENT_OTHER);
        });
    }

    const editPlanDetailPrints = function () {
        isPost = false;
        const r = EasyUI.grid.getSelections("weaveDg");
        if (r.length !== 1) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editProducePlanDetailPrintsUrl + "?ProducePlanDetailId=" + r[0].PRODUCEPLANDETAILID + "&tagType=roll", [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };
</script>
