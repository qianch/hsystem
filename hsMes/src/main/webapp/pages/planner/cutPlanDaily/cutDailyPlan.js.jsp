<!--
作者:肖文彬
日期:2016-11-24 11:02:50
页面:日计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加日计划
    const addUrl = path + "cutDailyPlan/add";
    //编辑日计划
    const editUrl = path + "cutDailyPlan/edit";
    //删除日计划
    const deleteUrl = path + "cutDailyPlan/delete";
    //编辑部件人员
    const editUserUrl = path + "planner/cutPlan/edit";
    //选择用户url
    const chooseUser = path + "selector/cuser?singleSelect=false";
    //打印
    const showPrinter = path + "printer/showPrinterPage1";
    const doPrinter = path + "printer/doPrintBarcode";
    //个性化打印
    const showCutPrinter = path + "individualprinter/showCutPrinterPage";
    const doIndividualPrinter = path + "individualprinter/doIndividualPrintBarcode";
    //选择裁剪计划
    const selectUrl = path + "cutDailyPlan/select";
    //打开提交审核页面
    const _auditCommitUrl = path + "selector/commitAudit";
    const auditCommitUrl = path + "cutDailyPlan/commitAudit";
    //指定人员
    const userUrl = path + "cutDailyPlan/users";
    const _editUserUrl = path + "cutDailyPlan/editUsers";
    const _editUserNumUrl = path + "cutDailyPlan/editUsersNum";
    //excelURl
    const exportUrl = path + "cutDailyPlan/export";
    const reloadAuditUrl = path + "planner/produce/reloadAudit";
    // const dialogWidth = 700, dialogHeight = 600;
    const weaveId = [];
    let cutPlanId;
    const flag = false;
    const wid = null;
    const userWindow = null;
    const userIds = [];
    let editingIndex;
    const _userWindow = null;

    // const editProducePlanDetailPrintsUrl = path + "planner/producePlanDetail/editProducePlanDetailPrints";

    function formatterIsClosed(index, row) {
        let style = "";
        style += "text-decoration:line-through;";
        if (row.ISCLOSED === 1) {
            return style;
        }
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
        /* if (r.AUDITSTATE < 2) {
            Tip.warn("审核中或未提交或审核不通过的无法关闭计划");
            return;
        }  */
        Dialog.confirm(function () {
            JQ.ajax(reloadAuditUrl, "post", {
                id: r.ID,
                type: 4
            }, function (data) {
                filter();
                $('#dailyDetails').datagrid('loadData', []);
                $('#partCounts').datagrid('loadData', []);
                $("#commentPanel").panel({"content": "<pre>" + "</pre>"});
            });
        });
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "cutDailyPlanSearchForm");
    }

    editingIndex = -1;

    function clickRowPrint(index, row) {
        if (editingIndex !== -1) {
            if ($("#dg_part_print").datagrid("validateRow", editingIndex)) {
                $("#dg_part_print").datagrid("endEdit", editingIndex);
                editingIndex = index;
                $("#dg_part_print").datagrid("beginEdit", index);
            }
        } else {
            editingIndex = index;
            $("#dg_part_print").datagrid("beginEdit", index);
        }
    }

    function endEdit() {
        const rows = $("#dg_part_print").datagrid("getRows");
        for (let i = 0; i < rows.length; i++) {
            editingIndex = i;
            $("#dg_part_print").datagrid("beginEdit", i);
            if (!$("#dg_part_print").datagrid("validateRow", i)) {
                return false;
            } else {
                $("#dg_part_print").datagrid("endEdit", i);
            }
            $("#dg_part_print").datagrid("endEdit", i);
        }
        editingIndex = -1;
        return true;
    }

    function doCutPrint(dialogId) {
        if ($("#doPrintBarcodeForm").form("validate")) {
            const r = EasyUI.grid.getOnlyOneSelected("dg_part_print");
            if (r) {
                if (endEdit()) {
                    const order = JQ.getFormAsJson("doPrintBarcodeForm");
                    /* var details = r;
                    var totalPrintSize=details.length; */
                    order.partName = r.partName;
                    order.count = r.printCount;
                    $.ajax({
                        url: path + "printer/doPrintBarcode",
                        type: 'post',
                        dataType: 'json',
                        data: order,
                        success: function (data) {
                            if (data.url) {
                                window.open(path.replace("hsmes/", "") + data.url);
                                Tip.success("打印成功");
                                Dialog.close(dialogId);
                            } else {
                                Tip.error(data);
                            }
                        }
                    });
                }
            } else {
                Tip.warn("请选择打印部件！");
            }
        }
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
            EasyUI.form.submit("editAuditProduce", auditCommitUrl, function (data) {
                filter();
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            $("#editAuditProduce #name").textbox("setValue", "裁剪日计划审核，时间：" + r.PLANDATE);
        });
    };

    let dialogId;
    //查看审核
    const viewUrl = path + "audit/CRJH/{id}/state";

    function view() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r == null)
            return;
        dialogId = Dialog.open("查看审核状态", 700, 400, viewUrl.replace("{id}", r.ID), [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
        });
    }

    function cutDailyUsersCounts(value) {
        if (value === undefined || value === "undefined") {
            return "";
        } else {
            return value;
        }
    }

    //添加日计划
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [EasyUI.window.button("icon-save", "保存", function () {
            if (!EasyUI.form.valid("cutPlanForm")) {
                return;
            }
            const rs = $("#dgg").datagrid("getRows");
            if (rs.length === 0) {
                Tip.warn("请双击计划编辑人员分配详情");
                return;
            }

            const cids = [];
            const partsNames = [];
            const partIds = [];
            const comments = [];
            const uids = [];
            //部件
            const partNames = [];
            //数量套
            const counts = [];
            const workShopCode = [];
            const pCounts = [];
            for (let i = 0; i < rs.length; i++) {
                if (isEmpty(rs[i].count)) {
                    Tip.warn("请填写第" + (i + 1) + "行裁剪计划总托数");
                    return;
                }
                if (isEmpty(rs[i].partsNames)) {
                    Tip.warn("请填写第" + (i + 1) + "行裁剪计划人员分配信息");
                    return;
                }
                workShopCode[i] = rs[i].WORKSHOPCODE;
                cids[i] = rs[i].ID;
                partsNames[i] = rs[i].partsNames;
                uids[i] = rs[i].uids;
                comments[i] = isEmpty(rs[i].comment) ? "　" : rs[i].comment;
                counts[i] = rs[i].count;
                pCounts[i] = rs[i].counts;
                partNames[i] = rs[i].partNames;
                partIds[i] = rs[i].partIds;
            }
            const cidLength = cids.length;
            if (partsNames.length < cidLength) {
                Tip.error("请确认部件明细信息");
                return;
            } else {
                for (let i = 0; i < partsNames.length; i++) {
                    if (partsNames[i] === undefined) {
                        Tip.error("请确认部件明细信息");
                        return;
                    }
                }
            }
            if (partIds.length < cidLength) {
                Tip.error("请确认部件明细信息");
                return;
            } else {
                for (let i = 0; i < partIds.length; i++) {
                    if (partIds[i] === undefined) {
                        Tip.error("请确认部件明细信息");
                        return;
                    }
                }
            }
            if (uids.length < cidLength) {
                Tip.error("请确认分配人员信息");
                return;
            } else {
                for (let i = 0; i < uids.length; i++) {
                    if (uids[i] === undefined) {
                        Tip.error("请确认分配人员信息");
                        return;
                    }
                }
            }
            if (counts.length < cidLength) {
                Tip.error("请确认总托数");
                return;
            } else {
                for (let i = 0; i < counts.length; i++) {
                    if (counts[i] === undefined) {
                        Tip.error("请确认总托数");
                        return;
                    }
                }
            }
            if (pCounts.length < cidLength) {
                Tip.error("请确认部件数量");
                return;
            } else {
                for (let i = 0; i < pCounts.length; i++) {
                    if (pCounts[i] === undefined) {
                        Tip.error("请确认部件数量");
                        return;
                    }
                }
            }
            JQ.ajaxPost(addUrl, {
                planDate: $("#planDate").datebox("getValue"),
                // workShop 	: 	$("#workShop").combobox("getValue"),
                workShopCode: workShopCode.join(","),
                cids: cids.join(","),
                comments: comments.join(","),
                counts: counts.join(","),
                partsNames: partsNames.join(","),
                uids: uids.join(","),
                partNames: partNames.join(","),
                pCounts: pCounts.join(","),
                partids: partIds.join(",")
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

    function editable(r) {
        return !(r.AUDITSTATE != null && r.AUDITSTATE > 0);
    }

    //编辑日计划
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能编辑他人下达的计划");
            return;
        }
        /* if (!editable(r)) {
            Tip.error("审核中或审核通过的计划无法编辑");
            return;
        } */
        let str = new Date(r.PLANDATE);
        str = str.setTime(str.getTime() - 8000 * 60 * 60);
        let date1 = new Date();
        date1 = date1.setDate(date1.getDate() - 1);
        if (str < date1) {
            Tip.warn("此日计划无法进行编辑！");
            return;
        }
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            if (!EasyUI.form.valid("cutPlanForm")) {
                return;
            }
            const rs = $("#dgg").datagrid("getRows");
            if (rs.length === 0) {
                Tip.warn("请至少选择一个计划");
                return;
            }
            const cids = [];
            const partsNames = [];
            const comments = [];
            const uids = [];
            //部件
            const partNames = [];
            const partIds = [];
            //数量 套
            const counts = [];
            const pCounts = [];
            for (let i = 0; i < rs.length; i++) {
                if (isEmpty(rs[i].count)) {
                    Tip.warn("请填写第" + (i + 1) + "行裁剪计划总托数");
                    return;
                }
                if (isEmpty(rs[i].partsNames)) {
                    Tip.warn("请填写第" + (i + 1) + "行裁剪计划人员分配信息");
                    return;
                }
                cids[i] = rs[i].ID;
                partsNames[i] = rs[i].partsNames;
                uids[i] = rs[i].uids;
                comments[i] = isEmpty(rs[i].comment) ? "　" : rs[i].comment;
                counts[i] = rs[i].count;
                pCounts[i] = rs[i].counts;
                partNames[i] = rs[i].partNames;
                partIds[i] = rs[i].partIds;
            }

            JQ.ajaxPost(addUrl, {
                id: $("#id").val(),
                planDate: $("#planDate").datebox("getValue"),
                // workShop 	: 	$("#workShop").combobox("getValue"),
                workShopCode: $("#workShopCode").combobox("getValue"),
                cids: cids.join(","),
                comments: comments.join(","),
                counts: counts.join(","),
                partsNames: partsNames.join(","),
                uids: uids.join(","),
                partNames: partNames.join(","),
                partids: partIds.join(","),
                pCounts: pCounts.join(",")
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
            const rows = selectedPlans;
            for (let i = 0; i < selectedPlans.length; i++) {
                rows[i].partsNames = selectedPlans[i].USERANDCOUNT;
                rows[i].uids = selectedPlans[i].USERIDS;
                rows[i].counts = selectedPlans[i].COUNTS;
                rows[i].partNames = selectedPlans[i].PARTNAMES;
                rows[i].count = selectedPlans[i].COUNT;
                rows[i].comment = selectedPlans[i].COMMENT2;
                rows[i].partIds = selectedPlans[i].PARTID;
            }
            $("#dgg").datagrid("loadData", rows);
            /* $("#_comment").val(row.COMMENT2);
            $("#userDg").datagrid("loadData", userDgData);
            var us=$("#userDg").datagrid("getRows");
            if(!isEmpty(row.partsNames)){
                var ucount=row.partsNames.split("##");
                var uids=row.uids.split("##");
                var counts=row.counts.split("##");
                var tr=null;
                for(var x=0;x<us.length;x++){
                    tr=us[x];
                    $("#userDg").datagrid("updateRow",{
                        index:x,
                        row:{
                            USERCOUNT:ucount[x].replace(tr.TCPROCBOMVERSIONPARTSNAME,"").replace("[","").replace("]",""),
                            USERIDS:uids[x],
                            USERCOUNTS:counts[x]
                        }
                    });
                }
            } */

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
        /* if (!editable(r)) {
            Tip.error("审核中或审核通过的计划无法删除");
            return;
        } */
        let str = new Date(r.PLANDATE);
        str = str.setTime(str.getTime() - 8000 * 60 * 60);
        let date1 = new Date();
        date1 = date1.setDate(date1.getDate() - 1);
        if (str < date1) {
            Tip.warn("此日计划无法进行删除！");
            return;
        }
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: r.ID
            }, function (data) {
                filter();
            });
        });
    };

    /**
     * 日计划列表点击事件
     */
    function dgRowClick(index, row) {
        JQ.ajaxPost(path + "cutDailyPlan/findC", {
            id: row.ID
        }, function (data) {
            $("#dailyDetails").datagrid("loadData", data);
        });
    }

    /**
     * 日计划明细点击事件
     */
    function dailyRowClick(index, row) {
        $("#commentPanel").panel({content: "<pre>" + (isEmpty(row.COMMENT2) ? "" : row.COMMENT2) + "</pre>"});
        const _rows = row.USERANDCOUNT.replace(/##/g, "|").replace(/\[/g, ";;").replace(/\]/g, "").split("|");
        const rows = [];
        for (let i = 0; i < _rows.length; i++) {
            const user = _rows[i].split(";;");
            rows.push({"part": user[0], "user": user[1]});
        }
        $("#partCounts").datagrid("loadData", rows);
    }

    function dgLoadSuccess(data) {
        $("#dg").datagrid("selectRow", 0);
        dgRowClick(0, $("#dg").datagrid("getRows")[0]);
    }

    function dailyLoadSuccess(data) {
        for (let i = 0; i < data.rows.length; i++) {
            $("#dailyDetails").datagrid('expandRow', i);
        }
        $("#dailyDetails").datagrid("selectRow", 0);
        const daily = $("#dailyDetails").datagrid('getSelected');
        cutPlanId = daily.ID;
        dailyRowClick(0, $("#dailyDetails").datagrid("getRows")[0]);
    }

    function formatterState(value, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    const widUser = null;

    //部件指定人员
    function dbClickEditUser(row) {
        if (row.children !== "") {
            Tip.warn("该部件无法指定人员！");
            return;
        }
        const widUser = Dialog.open("编辑", 380, 155, editUserUrl + "?partsId=" + row.id + "&cutPlanId=" + cutPlanId + "&userIds=" + row.userId, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("cutPlanForm", editUserUrl, function (data) {
                row.userName = $("#userName").searchbox("getValue");
                row.userId = $("#userIds").val();
                $('#dailyPartDg').treegrid('reload');
                Dialog.close(widUser);
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(widUser);
        })], function () {
            $("#productTrayCount").numberbox("setValue", row.count);
        });
    }

    //人员选择界面选中
    function _common_user_onLoadSuccess(data) {
        const array = $("#userIds").val().split(",");
        for (let i = 0; i < array.length; i++) {
            const rs = $("#_common_user_dg").datagrid('getRows');
            for (var a = 0; a < rs.length; a++) {
                if (rs[a].ID === parseInt(array[i])) {
                    $("#_common_user_dg").datagrid("selectRow", a);
                }
            }
        }
    }

    //打印
    function print() {
        const r = EasyUI.grid.getOnlyOneSelected("dailyDetails");
        const r1 = EasyUI.grid.getOnlyOneSelected("dg");
        const cutPlanId = r.ID;
        const cutDailyPlanId = r1.ID;
        const departmentName = r1.WORKSHOPCODE;
        dialogId = Dialog.open("打印", 400, 220, showPrinter + "?cutPlanId=" + cutPlanId + "&cutDailyPlanId=" + cutDailyPlanId + "&departmentName=" + departmentName, [EasyUI.window.button("icon-ok", "打印", function () {
            /* doCutPrint(dialogId); */
            EasyUI.form.submit("doPrintBarcodeForm", doPrinter, function (data) {
                if (data.url) {
                    Tip.success("打印成功");
                    Dialog.close(dialogId);
                } else {
                    Tip.error(data);
                }
                //filter();
            })
            //将选择的打印内容提交
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {

        });
    }

    function doPrintPart() {
        const cutDailyPlan = EasyUI.grid.getOnlyOneSelected("dg");
        dialogId = Dialog.open("打印", 400, 220, showCutPrinter + "?cutPlanId=" + cutDailyPlan.CUTPLANID + "&cutDailyPlanId=" + cutDailyPlan.ID + "&departmentCode=" + cutDailyPlan.WORKSHOPCODE + "&type=part", [EasyUI.window.button("icon-ok", "打印", function () {
            EasyUI.form.submit("doPrintBarcodeForm", doIndividualPrinter, function (data) {
                if (data.url) {
                    Tip.success("打印成功");
                    Dialog.close(dialogId);
                } else {
                    Tip.error(data);
                }
            })
            //将选择的打印内容提交
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
        });
    }

    let selectCutPlanDialogId;

    function selectCutPlan() {
        if ($("#planDate").datebox("getValue") == '' || $("#planDate").datebox("getValue") == null) {
            Tip.warn("请先选择时间");
            return;
        }
        selectCutPlanDialogId = Dialog.open("选择裁剪任务", 1000, 500, selectUrl, [EasyUI.window.button("icon-save", "保存", function () {
            let i;
            let rows = $("#dgg2").datagrid("getSelections");
            for (i = 0; i < rows.length; i++) {
                rows[i].DEVICECODE = "";
                if (!EasyUI.grid.contains("dgg", rows[i], "ID")) {
                    $("#dgg").datagrid("appendRow", rows[i]);
                }
            }
            Dialog.close(selectCutPlanDialogId);
            rows = $("#dgg").datagrid("getRows");
            for (i = 0; i < rows.length; i++) {
                $("#dgg").datagrid("expandRow", i);
            }
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectCutPlanDialogId);
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


    //双击编辑裁剪计划
    const dbClickEditC = function (index, row) {
        _innerRow = row;
        log(row)
        var wid = Dialog.open("编辑", 695, 495, userUrl + "?id=" + row.PRODUCEPLANDETAILID + "&date=" + $("#planDate").datebox("getValue") + "&workshopCode=" + row.WORKSHOPCODE, [EasyUI.window.button("icon-save", "保存", function () {
            //设置裁剪计划行上的信息
            var rows = $("#userDg").datagrid("getRows");
            var ddv = $("#dgg").datagrid('getRowDetail', index).find('table.ddv');
            var subRows = ddv.datagrid("getRows");
            //包含部件，人员，数量
            var partsNames = "";
            var uids = "";
            var counts = "";
            //部件名称
            var partNames = "";
            var partIds = "";
            for (var i = 0; i < rows.length; i++) {
                /* 				if(rows[i].USERCOUNTS==null){
                                    Tip.warn("请分配人员以及数量");
                                    return;
                                } */
                partsNames += (i == 0 ? "" : "##") + rows[i].TCPROCBOMVERSIONPARTSNAME + "[" + rows[i].USERCOUNT + "]";
                uids += (i == 0 ? "" : "##") + rows[i].USERIDS;
                partNames += (i == 0 ? "" : "##") + rows[i].TCPROCBOMVERSIONPARTSNAME;
                counts += (i == 0 ? "" : "##") + rows[i].USERCOUNTS;
                partIds += (i == 0 ? "" : "##") + rows[i].PARTID;
                for (var j = 0; j < subRows.length; j++) {
                    if (rows[i].TCPROCBOMVERSIONPARTSNAME == subRows[j].partName) {
                        ddv.datagrid("updateRow", {index: j, row: {_: rows[i].USERCOUNT}});
                    }
                }
            }
            _innerRow.partNames = partNames;
            _innerRow.partIds = partIds;
            _innerRow.uids = uids;
            _innerRow.partsNames = partsNames;
            _innerRow.count = $("#productTrayCount").numberbox("getValue");
            _innerRow.comment = $("#_comment").val();
            _innerRow.counts = counts;
            $("#dgg").datagrid("updateRow", {
                index: index,
                row: _innerRow,
            });
            Dialog.close(wid);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            if (row.COUNT != null && row.COUNT !== 0 && row.COUNT !== "") {
                $("#productTrayCount").numberbox("setValue", row.COUNT);
            } else {
                $("#productTrayCount").numberbox("setValue", row.REQUIREMENTCOUNT);
            }
            $("#_comment").val(row.COMMENT2);
            $("#userDg").datagrid("loadData", userDgData);
            const us = $("#userDg").datagrid("getRows");
            if (!isEmpty(row.partsNames)) {
                /* var temp=row.partsNames.substring(row.partsNames.indexOf("[")+1,row.partsNames.indexOf("]"));
                var ucount=temp.split("##"); */
                const ucount = row.partsNames.split("##");
                const uids = row.uids.split("##");
                const counts = row.counts.split("##");
                const parts = row.partsNames.split("##");
                for (let i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].substring(0, parts[i].indexOf("["));
                    log(parts[i])
                    let tr = null;
                    for (let x = 0; x < us.length; x++) {
                        tr = us[x];
                        if (tr.TCPROCBOMVERSIONPARTSNAME === parts[i]) {
                            $("#userDg").datagrid("updateRow", {
                                index: x,
                                row: {
                                    USERCOUNT: ucount[i] == undefined ? "" : ucount[i].replace(tr.TCPROCBOMVERSIONPARTSNAME, "").replace("[", "").replace("]", ""),
                                    USERIDS: uids[i] == undefined ? "" : uids[i],
                                    USERCOUNTS: counts[i] == undefined ? "" : counts[i]
                                }
                            });
                            break;
                        }
                    }
                }
            }
        });
    };

    //编辑裁剪计划
    function editCutPlan() {
        const rs = $("#dgg").datagrid("getChecked");
        if (rs.length > 1) {
            Tip.warn('只能同时编辑一行');
            return;
        }
        if (rs.length === 0) {
            Tip.warn('请选择编辑的行');
            return;
        }
        dbClickEditC(EasyUI.grid.getRowIndex("dgg", rs[0]), rs[0]);
    }

    let _innerParam = null;
    //双击添加人员以及指定数量
    const _widUser = null;

    function userRowClick(index, row) {
        const _r = row;
        const _widUser = Dialog.open("指定人员及数量", 450, 325, _editUserNumUrl, [EasyUI.window.button("icon-save", "保存", function () {
            $("#userNumDg").datagrid("endEdit", editingIndex);
            const datas = $("#userNumDg").datagrid("getData");
            const userIds = [];
            const rowData = [];
            const counts = [];
            for (let i = 0; i < datas.rows.length; i++) {
                rowData.push(datas.rows[i].USERNAME + "(" + datas.rows[i].NUM + ")");
                userIds.push(datas.rows[i].ID);
                counts.push(datas.rows[i].NUM);
            }
            $('#userDg').datagrid('updateRow', {
                index: index,
                row: {
                    'USERCOUNT': rowData.join("，"),
                    'USERIDS': userIds.join("，"),
                    'USERCOUNTS': counts.join("，")
                }
            });
            Dialog.close(_widUser);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(_widUser);
        })], function () {
            if (!isEmpty(_r.USERCOUNT)) {
                const rows = [];
                const counts = _r.USERCOUNTS.split("，");
                const uids = _r.USERIDS.split("，");
                const users = _r.USERCOUNT.split("，");
                for (let x = 0; x < counts.length; x++) {
                    users[x] = users[x].substring(0, users[x].indexOf("("));
                    if (users[x] !== "" && users[x] != null && users[x] !== undefined) {
                        rows.push({ID: uids[x], USERNAME: users[x], NUM: counts[x]});
                    }
                }
                $("#userNumDg").datagrid("loadData", rows);
            }
        });
    }

    //删除带出的裁剪计划
    function removeCutPlan() {
        const rows = $("#dgg").datagrid("getSelections");
        if (rows === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        for (let i = rows.length - 1; i >= 0; i--) {
            $("#dgg").datagrid("deleteRow", EasyUI.grid.getRowIndex("dgg", rows[i]));
        }
    }

    //双击带出的裁剪计划
    function onDgg2DbClick(index, row) {
        //row.DEVICECODE="";
        if (!EasyUI.grid.contains("dgg", row, "ID")) {
            $("#dgg").datagrid("appendRow", row);
        }
        Dialog.close(selectCutPlanDialogId);
    }

    //选择人员
    function ChooseUser() {
        const _userWindow = Dialog.open("编辑", 850, 450, chooseUser + "&did=12", [EasyUI.window.button("icon-save", "保存", function () {
            const rs = $("#_common_user_dg").datagrid("getSelections");
            for (let i = 0; i < rs.length; i++) {
                if (!EasyUI.grid.contains("userNumDg", rs[i], "ID")) {
                    $('#userNumDg').datagrid('appendRow', {
                        'ID': rs[i].ID,
                        'USERNAME': rs[i].USERNAME,
                        'NUM': 1
                    });
                }
            }
            Dialog.close(_userWindow);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(_userWindow);
        })]);
    }

    //双击选择人员
    /* function _common_user_dbClickRow(index,row){
         $('#userNumDg').datagrid('appendRow',{
            'USERNAME': row.USERNAME
          });
         Dialog.close(_userWindow);
    } */

    //行编辑指定数量
    function userNumRowClick(index, row) {
        if (editingIndex !== -1) {
            if ($("#userNumDg").datagrid("validateRow", editingIndex)) {
                $("#userNumDg").datagrid("endEdit", editingIndex);
                $("#userNumDg").datagrid("beginEdit", index);
                editingIndex = index;
            }
        } else {
            $("#userNumDg").datagrid("beginEdit", index);
            editingIndex = index;
        }
    }

    //删除选定人员
    function deleteUser() {
        const rows = $("#userNumDg").datagrid("getSelections");
        for (let i = rows.length - 1; i >= 0; i--) {
            $("#userNumDg").datagrid("deleteRow", EasyUI.grid.getRowIndex("userNumDg", rows[i]));
        }
    }

    function orderDateFormat(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
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

    //日计划导出
    function _export() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        window.open(exportUrl + "?id=" + r.ID);
    }

    const editPlanDetailPrints = function () {
        isPost = false;
        const r = EasyUI.grid.getSelections("dailyDetails");
        if (r.length !== 1) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editProducePlanDetailPrintsUrl + "?ProducePlanDetailId=" + r[0].PRODUCEPLANDETAILID + "&tagType=part", [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };
</script>
