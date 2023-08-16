<!--
作者:肖文彬
日期:2016-11-24 11:02:50
页面:日计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加日计划
    const addUrl = path + "weaveDailyPlan/add";
    //编辑日计划
    const editUrl = path + "weaveDailyPlan/edit";
    //删除日计划
    const deleteUrl = path + "weaveDailyPlan/delete";
    //编辑编织计划
    const editW = path + "planner/weavePlan/edit";
    //选择编织计划
    const selectUrl = path + "weaveDailyPlan/select";
    //打开提交审核页面
    const _auditCommitUrl = path + "selector/commitAudit";
    const auditCommitUrl = path + "weaveDailyPlan/commitAudit";
    const device = path + "weaveDailyPlan/devices";
    const updateD = path + "weaveDailyPlan/updateD";
    const dialogWidth = 700, dialogHeight = 600;
    const weaveId = [];
    const flag = false;
    let _workShop = "";
    //设备
    const Device = path + "selector/device?singleSelect=false";
    //打印
    const showPrinter = path + "printer/showPrinterPage";
    const doPrinter = path + "printer/doPrintBarcode";
    const exportUrl = path + "weaveDailyPlan/export";
    const add_req_detailUrl = path + "selector/addReq";
    const reloadAuditUrl = path + "planner/produce/reloadAudit";

    function onchange(newvalue, oldvalue) {
        filter();
    }

    function reloadAudit() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能关闭他人下达的计划");
            return;
        }
        if (r.ISCLOSED === 1) {
            Tip.error("计划已关闭");
            return;
        }
        if (r.AUDITSTATE < 2) {
            Tip.warn("审核中或未提交或审核不通过的无法关闭计划");
            return;
        }

        Dialog.confirm(function () {
            JQ.ajax(reloadAuditUrl, "post", {
                id: r.ID,
                type: 3
            }, function (data) {
                filter();
                $('#dailyDetails').datagrid('loadData', []);
                $('#dailyDeviceDg').datagrid('loadData', []);
                $("#comment_panel").panel({"content": "<pre>" + "</pre>"});

            });
        });
    }

    //查看工艺明细
    function check_details() {
        const r = EasyUI.grid.getOnlyOneSelected("dailyDetails");
        wid = Dialog.open("工艺/包装 需求", dialogWidth, dialogHeight, add_req_detailUrl + "?pid=" + r.PRODUCEPLANDETAILID, [
            EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
        });
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "deviceSearchForm");
    }

    function _export() {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        window.open(exportUrl + "?id=" + r.ID);
    }

    let dialogId;

    function print() {
        const r = EasyUI.grid.getOnlyOneSelected("dailyDetails");
        /* var r1 = EasyUI.grid.getOnlyOneSelected("dg");
        if (r1.AUDITSTATE != 2) {
            Tip.warn("审核中或未提交或审核不通过的无法打印条码");
            return;
        }
        if(r1.ISCLOSED==1){
            Tip.error("计划已关闭");
            return;
        } */
        dialogId = Dialog.open("打印", 400, 200, showPrinter + "?weaveId=" + r.ID + "&departmentName=" + r1.WORKSHOP, [EasyUI.window.button("icon-ok", "打印", function () {
            EasyUI.form.submit("doPrintBarcodeForm", doPrinter, function (data) {
                console.log($('#partId').val());
                if (data.url != null && data.url !== undefined) {
                    //window.open(path.replace("hsmes/","")+data.url);
                    document.getElementById("downLoad").innerHTML = '<a href="' + path.replace("mes/", "") + data.url + ' " target="_blank">' + path.replace("mes/", "") + data.url + '</a>';
                    Tip.success("打印成功");
                    Dialog.close(dialogId);
                }
                //filter();
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
        });
    }

    const doAudit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能提审他人下达的计划");
            return;
        }
        if (r.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录，不能在提交审核！");
            return;
        }
        const wid = Dialog.open("审核", dialogWidth, 120, _auditCommitUrl + "?id=" + r.ID, [EasyUI.window.button("icon-ok", "提交审核", function () {
            EasyUI.form.submit("editAuditProduce", auditCommitUrl + "?workShop=" + r.WORKSHOP, function (data) {
                filter();
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            $("#editAuditProduce #name").textbox("setValue", "编织日计划审核，时间：" + r.PLANDATE);
        });
    };

    function editable(r) {
        return !(r.AUDITSTATE != null && r.AUDITSTATE > 0);
    }

    let codeType = "";

    function view() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r == null) {
            return;
        }
        if (r.WORKSHOP === "编织一车间") {
            codeType = "RJH1";
        } else if (r.WORKSHOP === "编织二车间") {
            codeType = "RJH2";
        } else {
            codeType = "RJH3";
        }
        const viewUrl = path + "audit/" + codeType + "/{id}/state";
        dialogId = Dialog.open("查看审核状态", 700, 400, viewUrl.replace("{id}", r.ID), [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
        });
    }

    //选择机台
    function ChooseDevice() {
        deviceWindow = Dialog.open("选择机台信息", 850, 450, Device + "&workShop=" + _workShop, [EasyUI.window.button("icon-save", "确认", function () {
            const rs = EasyUI.grid.getSelections("_common_device_dg");
            let row = {};
            for (let i = 0; i < rs.length; i++) {
                row = {};
                row.DEVICEID = rs[i].ID;
                row.DEVICENAME = rs[i].DEVICENAME;
                row.DEVICECODE = rs[i].DEVICECODE;
                row.PRODUCECOUNT = 1;
                if (!contains("deviceDg", row)) {
                    $("#deviceDg").datagrid("appendRow", row);
                    $("#weavePlanForm").append("<input id='did" + row.DEVICEID + "' type='hidden' name='did' value='" + row.DEVICEID + "' />");
                    $("#weavePlanForm").append("<input id='dCount" + row.DEVICEID + "' type='hidden' name='dCount' value='" + row.PRODUCECOUNT + "' />");
                }
            }
            Dialog.close(deviceWindow);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(deviceWindow);
        })], function () {

        });
    }

    function deleteDevice() {
        const rows = $("#deviceDg").datagrid("getChecked");
        for (let i = rows.length - 1; i >= 0; i--) {
            $("#did" + rows[i].DEVICEID).remove();
            $("#dCount" + rows[i].DEVICEID).remove();
            $("#deviceDg").datagrid("deleteRow", $("#deviceDg").datagrid("getRowIndex", rows[i]));
        }
    }

    function _common_device_onLoadSuccess() {
        const rows = $("#deviceDg").datagrid("getRows");
        for (var i = rows.length - 1; i >= 0; i--) {
            $("#_common_device_dg").datagrid("selectRecord", rows[i].DEVICEID);
        }
    }

    //添加日计划
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [EasyUI.window.button("icon-save", "保存", function () {
            const rs = $("#dgg").datagrid("getRows");
            if (rs.length === 0) {
                Tip.warn("请至少选择一个计划");
                return;
            }
            const ids = [];
            const deviceCodes = [];
            const types = [];
            const comments = [];
            for (let i = 0; i < rs.length; i++) {
                if (!rs[i].DEVICECODES) {
                    Tip.warn("请指定机台生产任务");
                    return;
                }
                if (!rs[i].PRODUCTTYPE) {
                    Tip.warn("请指定产品属性");
                    return;
                }
                types.push(rs[i].PRODUCTTYPE);
                ids.push(rs[i].ID);
                deviceCodes.push(rs[i].DEVICECODES);
                comments.push(rs[i].COMMENT === "" ? "　" : rs[i].COMMENT);
            }
            JQ.ajaxPost(addUrl, {
                ids: ids.join(","),
                deviceCodes: deviceCodes.join(","),
                productTypes: types.join(","),
                planDate: $("#planDate").datebox("getValue"),
                workShop: $("#workShop").combobox("getValue"),
                comments: comments.join(",")
            }, function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success("保存成功");
                Dialog.close(wid);
                filter();
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })], function () {
            Dialog.more(wid);
            $("#" + wid).dialog("maximize");
            $("#planDate").datebox().datebox('calendar').calendar({
                validator: function (date) {
                    const now = new Date();
                    const d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                    return d2 <= date;
                }
            });
        });
    };

    //编辑日计划
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能编辑他人下达的计划");
            return;
        }
        if (r.ISCLOSED === 1) {
            Tip.error("计划已关闭");
            return;
        }
        if (!editable(r)) {
            Tip.error("审核中或审核通过的计划无法编辑");
            return;
        }
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            const rs = $("#dgg").datagrid("getRows");
            if (rs.length === 0) {
                Tip.warn("请至少选择一个编织任务");
                return;
            }
            const ids = [];
            const deviceCodes = [];
            const comments = [];
            const types = [];
            for (let i = 0; i < rs.length; i++) {
                if (!rs[i].DEVICECODES) {
                    Tip.warn("请指定机台生产任务");
                    return;
                }
                if (!rs[i].PRODUCTTYPE) {
                    Tip.warn("请指定产品属性");
                    return;
                }
                types.push(rs[i].PRODUCTTYPE);
                ids.push(rs[i].ID);
                deviceCodes.push(rs[i].DEVICECODES);
                comments.push(rs[i].COMMENT === "" ? "　" : rs[i].COMMENT);
            }
            JQ.ajaxPost(editUrl, {
                id: $("#id").val(),
                ids: ids.join(","),
                deviceCodes: deviceCodes.join(","),
                productTypes: types.join(","),
                planDate: $("#planDate").datebox("getValue"),
                workShop: $("#workShop").combobox("getValue"),
                comments: comments.join(",")
            }, function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success("保存成功");
                Dialog.close(wid);
                filter();
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })], function () {
            $("#" + wid).dialog("maximize");
            $("#dgg").datagrid("loadData", selectedPlans);
        });
    };

    let selectWeavePlanDialogId;

    function onDgg2DbClick(index, row) {
        row.DEVICECODE = "";
        if (!EasyUI.grid.contains("dgg", row, "ID")) {
            $("#dgg").datagrid("appendRow", row);
        }
        Dialog.close(selectWeavePlanDialogId);
    }

    let _innerParam = null;
    let editingIndex;
    //双击编辑编织计划
    const dbClickEditW = function (index, row) {
        _innerParam = row;
        if ($("#planDate").datebox("getValue") == '' || $("#planDate").datebox("getValue") == null) {
            Tip.warn("请选择时间后编辑");
            return;
        }
        if ($("#workShop").combobox("getValue") == '' || $("#workShop").combobox("getValue") == null) {
            Tip.warn("请指定车间后再指定机台");
            return;
        }
        const wid = Dialog.open("编辑", 695, 495, device + "?id=" + row.ID + "&date=" + $("#planDate").datebox("getValue") + "&workshop=" + $("#workShop").combobox("getValue"),
            [EasyUI.window.button("icon-save", "保存", function () {
                $("#deviceDg").datagrid("endEdit", editingIndex);
                //在FORM中临时插入，车间，日期，计划
                if ($("#deviceDg").datagrid("getRows").length === 0) {
                    Tip.warn("请指定机台以及数量");
                    return;
                }
                if ($("#productType").combobox("getValue") === '') {
                    Tip.warn("请指定产品属性");
                    return;
                }
                $("#weavePlanForm").append("<input type='hidden' name='wid' value='" + row.ID + "' />");
                $("#weavePlanForm").append("<input type='hidden' name='date' value='" + $("#planDate").datebox("getValue") + "' />");
                $("#weavePlanForm").append("<input type='hidden' name='workshop' value='" + $("#workShop").combobox("getValue") + "' />");
                const rs = $("#deviceDg").datagrid("getRows");
                let deviceCode = "";
                let deviceName = "";
                for (let i = 0; i < rs.length; i++) {
                    deviceCode += (i === 0 ? "" : "，") + rs[i].DEVICECODE + "(" + rs[i].PRODUCECOUNT + ")";
                    deviceName += (i === 0 ? "" : "###") + rs[i].DEVICENAME;
                }
                row.DEVICECODES = deviceCode;
                row.DEVICENAMES = deviceName;
                row.PRODUCTTYPE = $("#productType").combobox("getValue");
                const c = $("#_comment").val();
                row.COMMENT = c === "" ? "" : c;
                $("#dgg").datagrid("updateRow", {index: index, row: row});
                Dialog.close(wid);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })],
            function () {
                let row;
                let i;
                $("#" + dialogId).dialog("maximize");
                $("#_comment").val(_innerParam.COMMENT);
                $("#productType").combobox("select", _innerParam.PRODUCTTYPE);
                if (deviceDgData.length === 0) {
                    if (!isEmpty(_innerParam.DEVICENAMES)) {
                        const names = _innerParam.DEVICENAMES.split("###");
                        const codes = _innerParam.DEVICECODES.split("，");
                        for (i = 0; i < codes.length; i++) {
                            row = {};
                            row.DEVICEID = 0;
                            row.DEVICENAME = names[i];
                            row.DEVICECODE = codes[i].substring(0, codes[i].lastIndexOf("("));
                            row.PRODUCECOUNT = codes[i].replace(row.DEVICECODE + "(", "").replace(")", "");
                            $("#deviceDg").datagrid("appendRow", row);
                        }
                    }
                } else {
                    $("#deviceDg").datagrid("loadData", deviceDgData);
                }
                $("#weavePlanForm").append("<input id='time' type='hidden' name='time' value='" + $("#planDate").datebox("getValue") + "' />");
                for (i = 0; i < deviceDgData.length; i++) {
                    row = deviceDgData[i];
                    $("#weavePlanForm").append("<input id='did" + row.DEVICEID + "' type='hidden' name='did' value='" + row.DEVICEID + "' />");
                    $("#weavePlanForm").append("<input id='dCount" + row.DEVICEID + "' type='hidden' name='dCount' value='" + row.PRODUCECOUNT + "' />");
                }
            });
    };

    //删除日计划
    const doDelete = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能删除他人下达的计划");
            return;
        }
        if (!editable(r)) {
            Tip.error("审核中或审核通过的计划无法删除");
            return;
        }
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: r.ID,
            }, function (data) {
                filter();
            });
        });
    };

    editingIndex = -1;

    function deviceRowClick(index, row) {
        if (editingIndex !== -1) {
            if ($("#deviceDg").datagrid("validateRow", editingIndex)) {
                $("#deviceDg").datagrid("endEdit", editingIndex);
                $("#deviceDg").datagrid("beginEdit", index);
                editingIndex = index;
            }
        } else {
            $("#deviceDg").datagrid("beginEdit", index);
            editingIndex = index;
        }
    }

    function onAfterEdit(index, row, changes) {
        if (row.PRODUCECOUNT === "" || row.PRODUCECOUNT == null || row.PRODUCECOUNT == 0) {
            row.PRODUCECOUNT = 1;
        }
        $("#dCount" + row.DEVICEID).val(row.PRODUCECOUNT);
    }

    function selectWeavePlan() {
        if ($("#planDate").datebox("getValue") == '' || $("#planDate").datebox("getValue") == null) {
            Tip.warn("请先选择时间");
            return;
        }
        if ($("#workShop").combobox("getValue") == '' || $("#workShop").combobox("getValue") == null) {
            Tip.warn("请先指定车间");
            return;
        }
        _workShop = $("#workShop").combobox("getValue");
        selectWeavePlanDialogId = Dialog.open("选择编织任务", 1000, 500, selectUrl + "?workShop=" + _workShop, [EasyUI.window.button("icon-save", "保存", function () {
            const rows = $("#dgg2").datagrid("getSelections");
            for (let i = 0; i < rows.length; i++) {
                rows[i].DEVICECODE = "";
                if (!EasyUI.grid.contains("dgg", rows[i], "ID")) {
                    $("#dgg").datagrid("appendRow", rows[i]);
                }
            }
            Dialog.close(selectWeavePlanDialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectWeavePlanDialogId);
        })], function () {
            $("#deviceDg").datagrid("loadData", deviceDgData);
            $("#weavePlanForm").append("<input id='time' type='hidden' name='time' value='" + $("#planDate").datebox("getValue") + "' />");
            for (var i = 0; i < deviceDgData.length; i++) {
                var row = deviceDgData[i];
                $("#weavePlanForm").append("<input id='did" + row.DEVICEID + "' type='hidden' name='did' value='" + row.DEVICEID + "' />");
                $("#weavePlanForm").append("<input id='dCount" + row.DEVICEID + "' type='hidden' name='dCount' value='" + row.PRODUCECOUNT + "' />");
            }
        });
    }

    function editWeavePlan() {
        //dbClickEditW index,row
        const rs = $("#dgg").datagrid("getChecked");
        if (rs.length > 1) {
            Tip.warn('只能同时编辑一行');
            return;
        }
        if (rs.length === 0) {
            Tip.warn('请选择编辑的行');
            return;
        }
        dbClickEditW(EasyUI.grid.getRowIndex("dgg", rs[0]), rs[0]);
    }

    function removeWeavePlan() {
        const rows = $("#dgg").datagrid("getSelections");
        for (var i = rows.length - 1; i >= 0; i--) {
            $("#dgg").datagrid("deleteRow", EasyUI.grid.getRowIndex("dgg", rows[i]));
        }
    }

    /**
     * 日计划列表点击事件
     */
    function dgRowClick(index, row) {
        JQ.ajaxPost(path + "weaveDailyPlan/findW", {
            id: row.ID
        }, function (data) {
            $("#dailyDetails").datagrid("loadData", data);
        });
    }

    /**
     * 日计划明细点击事件
     */
    function dailyRowClick(index, row) {
        $("#rCount").text(row.COUNT);
        $("#tCount").text(row.PRODUCTTRAYCOUNT);
        $("#wCount").text(row.WEIGHT);
        $("#comment_panel").panel({"content": "<pre>" + (row.COMMENT == undefined ? "无" : row.COMMENT) + "</pre>"});
        JQ.ajaxPost(path + "planner/weavePlan/device", {
            //wid : row.ID,
            wid: ($("#dg").datagrid("getChecked")[0]).ID,
            id: row.ID,
            date: ($("#dg").datagrid("getChecked")[0]).DATE,
            workshop: ($("#dg").datagrid("getChecked")[0]).WORKSHOP
        }, function (data) {
            $("#dailyDeviceDg").datagrid("loadData", data);
        });
    }

    function dgLoadSuccess(data) {
        $("#dg").datagrid("selectRow", 0);
        dgRowClick(0, $("#dg").datagrid("getRows")[0]);
    }

    function dailyLoadSuccess(data) {
        //enableDgFilter();
        $(".datagrid-filter[name='DELEVERYDATE']").parent().remove();
        if ($("#dailyDetails").datagrid('getRows').length !== 0) {
            $("#dailyDetails").datagrid("selectRow", 0);
            dailyRowClick(0, $("#dailyDetails").datagrid("getRows")[0]);
        } else {
            for (var i = 0; i < $("#dailyDeviceDg").datagrid('getRows').length; i++) {
                $("#dailyDeviceDg").datagrid('deleteRow', i);
                $("#comment_panel").panel({"content": "<pre>" + '' + "</pre>"});
            }
        }
    }

    let boolean = false;

    function enableDgFilter() {
        if (!boolean) {
            $("#dailyDetails").datagrid('enableFilter');
        }
        boolean = true;
    }

    function addPlan() {
        const rs = $("#dg").datagrid("getChecked");
        if (rs.length === 0) {
            Tip.warn("请选择一个日计划");
            return;
        }
        const wid = Dialog.open("选择计划", 700, 600, path + "/weaveDailyPlan/addPlan",
            [EasyUI.window.button("icon-ok", "提交审核", function () {

            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })],
            function () {
            });
    }

    function contains(grid, rowData) {
        const data = $("#" + grid).datagrid("getData");
        if (data.total === 0) {
            return false;
        } else {
            for (let i = 0; i < data.rows.length; i++) {
                if (data.rows[i]["DEVICEID"] === rowData["DEVICEID"]) {
                    return true
                }
            }
            return false;
        }
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

    function formatterState(value, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    function formatterIsClosed(index, row) {
        let style = "";
        style += "text-decoration:line-through;";
        if (row.ISCLOSED === 1) {
            return style;
        }
    }

    //出货时间
    function orderDateFormat(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }
</script>