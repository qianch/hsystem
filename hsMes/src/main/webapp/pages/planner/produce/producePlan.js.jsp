<!--
作者:高飞
日期:2016-11-28 21:25:48
页面:生产任务单JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加生产任务单
    const addUrl = path + "planner/produce/add";
    //编辑生产任务单
    const editUrl = path + "planner/produce/edit";
    //删除生产任务单
    const deleteUrl = path + "planner/produce/delete";
    const exportUrl = path + "planner/produce/export";
    const exportUrl1 = path + "planner/produce/export1";
    const viewStockUrl = path + "stock/productStock/viewStock";
    //打开提交审核页面
    const _auditCommitUrl = path + "selector/commitAudit";
    const auditCommitUrl = path + "planner/produce/commitAudit";
    const add_req_detailUrl = path + "selector/addReq";
    //查看审核过的生产任务
    const look = path + "planner/produce/look";
    //关闭生产计划查看审核状态
    const closeU = path + "planner/produce/close";
    //关闭生产计划查看编制是否关闭
    const wclose = path + "planner/produce/wclose";
    const dialogWidth = 700, dialogHeight = 420;
    const _innerParam = null;
    let producePlanId = "";
    let pCode = null;
    const closeUrl = path + "common/close";
    const noclose = path + "planner/produce/noClose";
    const forceEditUrl = path + "planner/produce/forceEdit";

    //查看工艺bom明细
    function bomVersionView(value, row, index) {
        if (value == null) {
            return "";
        } else if (row.PROCESSBOMVERSION == null || row.PROCESSBOMVERSION === "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_bomVersionView(" + row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value + "</a>"
        }
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}planner/produce/list",
        });
    });

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
        dialogId = Dialog.open("查看工艺bom明细", 700, 400, viewUrl, [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            $("#" + dialogId).dialog("maximize");
            if (isTc !== 1) {
                for (var a = 0; a < details.length; a++) {
                    _common_bomDetail_data(details[a]);
                }
            }
        });
    }

    function doClose() {
        const r = EasyUI.grid.getSelections("dg");
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        const rows = $("#producePlanDetails").datagrid("getSelections");
        const userName = "${userName}";
        if (rows.length === 0) {
            Tip.warn("请至少选择一条记录");
            return;
        }
        if (row.CREATEUSERNAME !== userName) {
            Tip.warn("不能关闭他人下达的计划");
            return;
        }
        const type = "PRODUCE";
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        $.ajax({
            url: wclose + "?ids=" + ids.join(","),
            type: "get",
            dataType: "json",
            success: function (data) {
                Dialog.confirm(function () {
                    Loading.show();
                    $.ajax({
                        url: closeUrl + "?type=" + type + "&ids=" + ids.join(","),
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            if (Tip.hasError(data)) {
                                return;
                            }
                            Tip.success("关闭成功");
                            filter();
                            Loading.hide();
                        }
                    });
                }, "确认关闭");
            }
        });
    }

    function cancelClose() {
        const r = EasyUI.grid.getSelections("dg");
        const rows = $("#producePlanDetails").datagrid("getSelections");
        if (rows[0].CLOSED != 1) {
            Tip.warn("只有关闭的订单才能恢复");
            return;
        }
        if (rows.length == 0) {
            Tip.warn("请至少选择一条记录");
            return;
        }
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        Dialog.confirm(function () {
            Loading.show();
            $.ajax({
                url: noclose + "?Ids=" + ids.join(","),
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("恢复成功");
                    filter();
                    Loading.hide();
                }
            });
        }, "确认恢复?");
    }

    let editingIndex;

    function editReqDetails(v) {
        let wid1;
        let r;
        if (isEmpty(v)) {
            r = EasyUI.grid.getOnlyOneSelected("produceProducts");
            if (r == null) {
                Tip.warn("请选择订单产品");
                return;
            }
            editingIndex = $("#produceProducts").datagrid("getRowIndex", r);
            $("#produceProducts").datagrid("beginEdit", editingIndex);
            if (!$("#produceProducts").datagrid("validateRow", editingIndex)) {
                Tip.warn("请先输入批次号等信息");
                return;
            }
            $("#produceProducts").datagrid("endEdit", editingIndex);
            wid1 = Dialog.open("包装和工艺要求", dialogWidth, dialogHeight, add_req_detailUrl + "?pid=" + (isEmpty(r.id) ? "-1" : r.id) + "&productId=" + r.productId,
                [EasyUI.window.button("icon-save", "保存",
                    function () {
                        const packReq = $('#packReq').val();
                        const procReq = $('#procReq').val();
                        //updateRow
                        $("#produceProducts").datagrid("updateRow", {
                            index: $("#produceProducts").datagrid("getRowIndex", r),
                            row: {
                                procReq: procReq,
                                packReq: packReq
                            }
                        });
                        Dialog.close(wid1);
                    }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid1);
                })], function () {
                    $("#" + wid1).dialog("maximize");
                    $('#packReq').val(r.packReq);
                    $('#procReq').val(r.procReq);
                });
        } else {
            r = $("#producePlanDetails").datagrid("getSelected");
            if (r == null) return;
            wid1 = Dialog.open(r.PRODUCTMODEL + " 的包装和工艺要求", dialogWidth, dialogHeight, add_req_detailUrl + "?pid=-1&productId=-1", [EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid1);
            })], function () {
                $("#" + wid1).dialog("maximize");
                $('#packReq').val(r.PACKREQ);
                $('#procReq').val(r.PROCREQ);
            });
        }
    }

    function packageTask() {
        const row = $("#producePlanDetails").datagrid("getSelected");
        if (row == null) {
            Tip.warn("请选择生产计划");
            return;
        }
        if (row.PRODUCTISTC !== 2) {
            Tip.warn("无需设置包装任务");
            return;
        }
        const wid1 = Dialog.open("包装和工艺要求", dialogWidth, dialogHeight, path + "planner/produce/pack/?producePlanDetailId=" + row.ID + "&bcBomId=" + row.PACKBOMID,
            [EasyUI.window.button("icon-save", "保存",
                function () {
                    let i;
                    let rows = $("#pack_task_dg").datagrid("getRows");
                    const list = [];
                    for (i = 0; i < rows.length; i++) {
                        $("#pack_task_dg").datagrid("beginEdit", i);
                        if (!$("#pack_task_dg").datagrid("validateRow", i)) {
                            Tip.warn("请输入必填项");
                            return;
                        }
                        $("#pack_task_dg").datagrid("endEdit", i);
                    }
                    rows = $("#pack_task_dg").datagrid("getRows");
                    log(rows)
                    for (i = 0; i < rows.length; i++) {
                        list.push({
                            "producePlanDetailId": pid,
                            "packageBomId": rows[i].PACKBOMID,
                            "trayCount": rows[i].TRAYCOUNT
                        });
                    }
                    $.ajax({
                        url: path + "planner/produce/pack/add",
                        type: "post",
                        dataType: "json",
                        data: JSON.stringify(list),
                        contentType: "application/json",
                        success: function (data) {
                            log(data)
                        }
                    });
                    Dialog.close(wid1);
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid1);
            })]);
    }

    function changeReqDetail(r, packReq, procReq) {
        r.packReq = packReq;
        r.procReq = procReq;
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "producePlanSearchForm");
    }

    let wid;

    //添加生产任务单
    const add = function () {
        wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [EasyUI.window.button("icon-save", "保存", function () {
            let i;
            const rs = $("#produceProducts").datagrid("getRows");
            for (i = 0; i < rs.length; i++) {
                if (rs[i].view !== 1) {
                    Tip.warn("请查看第" + (i + 1) + "行产品的库存")
                    return;
                }
            }
            for (i = 0; i < rs.length; i++) {
                delete rs[i].view;
            }
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })], function () {
            Dialog.more(wid);
            $("#" + wid).dialog("maximize");
        });
    };

    //date扩展
    Date.prototype.Format = function (fmt) { //author: meizz
        const o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()
            //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (const k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    //编辑生产任务单
    const edit = function () {
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (row.CREATEUSERNAME !== userName) {
            Tip.warn("不能编辑他人下达的计划");
            return;
        }
        producePlanId = row.ID;
        if (row.AUDITSTATE <= 0) {
            wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [EasyUI.window.button("icon-save", "保存", function () {
                var rs = $("#produceProducts").datagrid("getRows");
                for (var i = 0; i < rs.length; i++) {
                    delete rs[i].view;
                }
                saveForm();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })], function () {
                $("#" + wid).dialog("maximize");
                //$("#produceProducts").datagrid("loadData", details);
            });
        } else {
            wid = Dialog.open("查看(不可保存)", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
                $("#" + wid).dialog("maximize");
            });
        }
    };

    const forceEdit = function () {
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (row.CREATEUSERNAME !== userName) {
            Tip.warn("不能变更他人下达的计划");
            return;
        }
        if (row.AUDITSTATE !== 2) {
            Tip.warn("审核通过才能强制变更");
            return;
        }
        producePlanId = row.ID;
        /* wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id="
                + r.ID, [ EasyUI.window.button("icon-save", "保存", function() {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function() {
            Dialog.close(wid)
        }) ], function() {
            $("#" + wid).dialog("maximize");
            $("#produceProducts").datagrid("loadData", details);
        }); */

        wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID + "&force=true", [EasyUI.window.button("icon-save", "保存", function () {
            saveforceEdit();
            /* var details = [];
            var rows = $("#produceProducts").datagrid("getRows");
            for (var i = 0; i < rows.length; i++) {
                rows[i].deleveryDate=rows[i].deleveryDate.substr(0,10);
                $("#produceProducts").datagrid("endEdit", i);
                console.log(rows[i].deleveryDate);
                details.push(rows[i]);
            }
            Loading.show();
            $.ajax({
                url : path + "planner/produce/forceEdit",
                type : 'post',
                dataType : 'json',
                contentType : 'application/json',
                data : JSON.stringify(details),
                success : function(data) {
                    Loading.hide();
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Dialog.close(wid);
                    filter();
                }
            }); */
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })], function () {
            $("#" + wid).dialog("maximize");
            //$("#produceProducts").datagrid("loadData", details);
        });
    };

    function saveForm() {
        let ws1;
        let i;
        const array = [];
        if (!EasyUI.form.valid("producePlanForm")) {
            return;
        }
        const rs = $("#produceProducts").datagrid("getRows");
        if (rs.length == 0) {
            Tip.warn("请添加产品信息");
            return;
        }
        //-------------------------------------------------------------------------------------------------
        const workShopCode = $("#ws").combobox("getValue");
        const producePlanCode = $("#producePlanCode").textbox("getValue");
        let time = new Date().Format("yyyyMMdd");
        //sj-
        const sj = producePlanCode.substring(0, 3);
        //-bj1-
        const ws = producePlanCode.substring(11, 16);
        //计划单号时间
        const t = producePlanCode.substring(3, 11);
        const year = t.substring(0, 4);
        const month = t.substring(4, 6);
        const day = t.substring(6, 8);
        //当前时间
        time = new Date().Format("yyyyMMdd");
        const year1 = time.substring(0, 4);
        const month1 = time.substring(4, 6);
        const day1 = time.substring(6, 8);
        const a = producePlanCode.split("-");
        for (i = 0; i < a.length; i++) {
            array.push(a[i]);
        }
        if (sj !== "JD-") {
            Tip.warn("");
            return;
        }
        if (year !== year) {
            Tip.warn("单号格式不对");
            return;
        }
        if (t.length !== 8) {
            Tip.warn("单号格式不对");
            return;
        }
        if (isNaN(t)) {
            Tip.warn("单号格式不对");
            return;
        }
        if (workShopCode === "00116") {
            for (i = 0; i < rs.length; i++) {
                if (rs[i].productIsTc !== 1) {
                    Tip.warn(rs[i].factoryProductName + "不是套材,计划不能下到裁剪车间");
                    return;
                }
            }
            ws1 = producePlanCode.substring(11, 16);
            if (ws1 !== "-CJ1-") {
                Tip.warn("单号格式不对");
                return;
            }
        }
        if (workShopCode === "00117") {
            for (i = 0; i < rs.length; i++) {
                if (rs[i].productIsTc !== 1) {
                    Tip.warn(rs[i].factoryProductName + "不是套材,计划不能下到裁剪车间");
                    return;
                }
            }
            ws1 = producePlanCode.substring(11, 16);
            if (ws1 !== "-CJ2-") {
                Tip.warn("单号格式不对");
                return;
            }
        }
        if (array[3].length > 3) {
            Tip.warn("单号格式不对");
            return;
        }
        if (array.length !== 4) {
            Tip.warn("单号格式不对");
            return;
        }
        if (array[3].length === 1) {
            pCode = array[0] + '-' + array[1] + '-' + array[2] + '-00' + array[3];
        }
        if (array[3].length === 2) {
            pCode = array[0] + '-' + array[1] + '-' + array[2] + '-0' + array[3];
        }
        if (array[3].length === 3) {
            pCode = array[0] + '-' + array[1] + '-' + array[2] + '-' + array[3];
        }
        const batchCode = [];
        for (i = 0; i < rs.length; i++) {
            editingIndex = i;
            valid = true;
            $("#produceProducts").datagrid("beginEdit", editingIndex);
            if (!$("#produceProducts").datagrid("validateRow", editingIndex)) {
                Loading.hide();
                return;
            }
            $("#produceProducts").datagrid("endEdit", editingIndex);
            tempCount = 0;
            batchCode.push(rs[i].batchCode);
        }
        if (batchCode.join(",") === "") {
            Tip.warn("请输入批次号");
            return;
        }
        Loading.show();
        url = $("#producePlanForm").attr("action");
        $.ajax({
            url: path + "planner/produce/valid/batchcode/" + batchCode.join(",") + "?producePlanId=" + producePlanId,
            type: "get",
            dataType: "json",
            success: function (data) {
                Loading.hide();
                if (data.ret === "") {
                    const id = $("input[name=id]").val();
                    const workShopCode = $("#ws").combobox("getValue");
                    const list = $("#produceProducts").datagrid("getRows");
                    const url = $("#producePlanForm").attr("action");
                    let subgrid;
                    let subgridRows;
                    let tempCount = 0;
                    for (let i = 0; i < list.length; i++) {
                        list[i].list = [];
                        if (list[i].productIsTc !== 1) continue;
                        subgrid = $("#produceProducts").datagrid('getRowDetail', i).find('table.ddv');
                        subgridRows = subgrid.datagrid("getRows");
                        for (let j = 0; j < subgridRows.length; j++) {
                            subgrid.datagrid("beginEdit", j);
                            subgrid.datagrid("endEdit", j);
                            tempCount += subgrid.datagrid("getRows")[j].planPartCount;
                            list[i].list = subgrid.datagrid("getRows");
                        }
                        if (tempCount === 0) {
                            Tip.warn("无效的单个部件数量");
                            return;
                        }
                    }
                    $.ajax({
                        url: url,
                        type: "post",
                        dataType: "json",
                        data: JSON.stringify({
                            id: id,
                            producePlanCode: pCode,
                            workShopCode: workShopCode,
                            createUserName: '${userName }',
                            createUserId:${userId },
                            list: list
                        }),
                        contentType: "application/json",
                        success: function (data) {
                            Loading.hide();
                            if (Tip.hasError(data))
                                return;
                            Tip.success("保存成功");
                            filter();
                            Dialog.close(wid);
                        }
                    });
                } else {
                    Dialog.confirm(function () {
                        const id = $("input[name=id]").val();
                        const workShopCode = $("#ws").combobox("getValue");
                        const list = $("#produceProducts").datagrid("getRows");
                        const url = $("#producePlanForm").attr("action");
                        let subgrid;
                        let subgridRows;
                        let tempCount = 0;
                        for (let i = 0; i < list.length; i++) {
                            list[i].list = [];
                            if (list[i].productIsTc !== 1) continue;
                            subgrid = $("#produceProducts").datagrid('getRowDetail', i).find('table.ddv');
                            subgridRows = subgrid.datagrid("getRows");
                            tempCount = 0;
                            for (let j = 0; j < subgridRows.length; j++) {
                                subgrid.datagrid("beginEdit", j);
                                subgrid.datagrid("endEdit", j);
                                tempCount += subgrid.datagrid("getRows")[j].planPartCount;
                                list[i].list = subgrid.datagrid("getRows");
                                log(list[i].list)
                            }
                            if (tempCount === 0) {
                                Tip.warn("无效的单个部件数量");
                                return;
                            }
                        }
                        $.ajax({
                            url: url,
                            type: "post",
                            dataType: "json",
                            data: JSON.stringify({
                                id: id,
                                producePlanCode: pCode,
                                workShopCode: workShopCode,
                                createUserName: '${userName }',
                                createUserId:${userId },
                                list: list
                            }),
                            contentType: "application/json",
                            success: function (data) {
                                Loading.hide();
                                if (Tip.hasError(data))
                                    return;
                                Tip.success("保存成功");
                                producePlanId = "";
                                filter();
                                Dialog.close(wid);
                            }
                        });
                    }, "批次号已存在：<br>" + data.ret + "是否继续？")
                }
            }
        });
    }

    function saveforceEdit() {
        let tempCount;
        let i;
        const array = [];
        if (!EasyUI.form.valid("producePlanForm")) {
            return;
        }
        const rs = $("#produceProducts").datagrid("getRows");
        if (rs.length === 0) {
            Tip.warn("请添加产品信息");
            return;
        }
        //-------------------------------------------------------------------------------------------------
        let workShopCode = $("#ws").combobox("getValue");
        const producePlanCode = $("#producePlanCode").textbox("getValue");
        for (i = 0; i < rs.length; i++) {
            editingIndex = i;
            valid = true;
            $("#produceProducts").datagrid("beginEdit", editingIndex);
            if (!$("#produceProducts").datagrid("validateRow", editingIndex)) {
                Loading.hide();
                return;
            }
            $("#produceProducts").datagrid("endEdit", editingIndex);
            tempCount = 0;
        }
        Loading.show();
        const id = $("input[name=id]").val();
        workShopCode = $("#ws").combobox("getValue");
        const list = $("#produceProducts").datagrid("getRows");
        const url = $("#producePlanForm").attr("action");
        let subgrid;
        let subgridRows;
        tempCount = 0;
        for (i = 0; i < list.length; i++) {
            list[i].list = [];
            if (list[i].productIsTc !== 1) continue;
            subgrid = $("#produceProducts").datagrid('getRowDetail', i).find('table.ddv');
            subgridRows = subgrid.datagrid("getRows");
            tempCount = 0;
            for (let j = 0; j < subgridRows.length; j++) {
                subgrid.datagrid("beginEdit", j);
                subgrid.datagrid("endEdit", j);
                tempCount += subgrid.datagrid("getRows")[j].planPartCount;
                list[i].list = subgrid.datagrid("getRows");
            }
            if (tempCount === 0) {
                Tip.warn("无效的单个部件数量");
                return;
            }
        }
        $.ajax({
            url: forceEditUrl,
            type: "post",
            dataType: "json",
            data: JSON.stringify({
                id: id,
                producePlanCode: producePlanCode,
                workShopCode: workShopCode,
                list: list
            }),
            contentType: "application/json",
            success: function (data) {
                Loading.hide();
                if (Tip.hasError(data))
                    return;
                Tip.success("保存成功");
                filter();
                Dialog.close(wid);
            }
        });
    }

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        producePlanId = row.ID;
        if (row.AUDITSTATE <= 0) {
            wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [EasyUI.window.button("icon-save", "保存", function () {
                saveForm();
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })], function () {
                $("#" + wid).dialog("maximize");
                $("#produceProducts").datagrid("loadData", details);
            });
        } else {
            wid = Dialog.open("查看", dialogWidth, dialogHeight, look + "?id=" + row.ID, [EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
                $("#" + wid).dialog("maximize");
                $("#produceProducts").datagrid("loadData", details);
            });

        }
    };

    //删除生产任务单
    const doDelete = function () {
        const r1 = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r1.CREATEUSERNAME !== userName) {
            Tip.warn("不能删除他人下达的计划");
            return;
        }
        if (r1.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录，不能删除！");
            return;
        }

        var r = EasyUI.grid.getSelections("dg");
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

    function unCompleteFormatter(value, row, index) {
        return row.ORDERCOUNT - row.REQUIREMENTCOUNT;
    }

    function stateFormatter(value, row, index) {
        if (value == null || value === 0) {
            return "<label style='background:green;width:100%;display: inline-block;color:white;text-align:center;'>正常</label>";
        }
        return "<label style='background:red;width:100%;display: inline-block;color:white;text-align:center;'>关闭</label>";
    }

    function rowStyler(index, row) {
        let style = row.SALESORDERISEXPORT === 0 ? "background:rgba(255, 0, 0, 0.23);" : "";
        if (isEmpty(row.CLOSED) || row.CLOSED === 0) {
        } else {
            style += "text-decoration:line-through;background: #989696;";
        }
        return style;
    }

    function rowStyler1(index, row) {
        let style = row.SALESORDERISEXPORT === 0 ? "background:rgba(255, 0, 0, 0.23);" : "";
        if (isEmpty(row.CLOSED) || row.CLOSED === 0) {
        } else {
            style += "text-decoration:line-through;background: #989696;";
        }
        return style;
    }

    function orderDateFormat(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function exportFormat(value, row, index) {
        return value === 0 ? "外销" : "内销";
    }

    let orderProductSelectId;

    function orderProductSelect() {
        if ($("#ws").combobox("getValue") === '' || $("#ws").combobox("getValue") == null) {
            Tip.warn("请先指定车间");
            return;
        }
        selectSalesProductsId = Dialog.open("选择订单产品", 1000, 400, path + "/planner/produce/order/select?workShopCode=" + $("#ws").combobox("getValue"),
            [EasyUI.window.button("icon-save", "添加", function () {
                const rs = $("#orderProductSelect").datagrid("getChecked");
                if (rs.length === 0) {
                    return;
                }
                const _row = {};
                for (let i = 0; i < rs.length; i++) {
                    appendRow(rs[i]);
                }
                Dialog.close(selectSalesProductsId);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(selectSalesProductsId);
            })]);
    }

    function contains(rowData) {
        return false;
    }

    function appendRow(r) {
        const _row = {};
        //产品id
        _row.productId = r.PRODUCTID;
        //客户id
        _row.consumerId = r.CONSUMERID;
        //客户名称
        _row.consumerName = r.CONSUMERNAME;
        //订单号
        _row.salesOrderCode = r.SALESORDERSUBCODE;
        //客户订单号
        _row.salesOrderSubcodePrint = r.SALESORDERSUBCODEPRINT;
        //批次号
        _row.batchCode = r.PRODUCTBATCHCODE;
        //客户产品名称
        _row.consumerProductName = r.CONSUMERPRODUCTNAME;
        //厂内名称
        _row.factoryProductName = r.FACTORYPRODUCTNAME;
        //产品规格
        _row.productModel = r.PRODUCTMODEL;
        _row.packReq = r.PACKREQ;
        _row.procReq = r.PROCREQ;
        //订单总重
        _row.orderTotalWeight = r.TOTALWEIGHT;
        //订单总米长
        _row.orderTotalMetres = r.TOTALMETRES;
        //产品名称
        //_row.productName=r.FACTORYPRODUCTNAME;
        //门幅
        _row.productWidth = r.PRODUCTWIDTH;
        //卷重
        _row.productWeight = r.PRODUCTROLLWEIGHT;
        //卷长
        _row.productLength = r.PRODUCTROLLLENGTH;
        _row.drawNo = r.DRAWNO;
        _row.levelNo = r.LEVELNO;
        _row.rollNo = r.ROLLNO;
        _row.partId = r.PARTID;
        _row.partName = r.PARTNAME;

        //来自订单明细ID
        _row.fromSalesOrderDetailId = r.ID;
        //订单数量(kg/套)
        _row.orderCount = r.PRODUCTCOUNT;
        //工艺版本
        _row.processBomVersion = r.PRODUCTPROCESSBOMVERSION;
        //工艺代码
        _row.processBomCode = r.PRODUCTPROCESSCODE;
        //包装版本
        _row.bcBomVersion = r.PRODUCTPACKAGEVERSION;
        //包装代码
        _row.bcBomCode = r.PRODUCTPACKAGINGCODE;
        //总卷数
        //_row.totalRollCount = r.BCROLLCOUNT;
        let _totalRollCount = null;
        if (r.PRODUCTISTC === 2) {
            if (r.PRODUCTROLLLENGTH == null && r.PRODUCTROLLWEIGHT != null) {//当有卷重时（订单数量/卷重）
                _totalRollCount = Calc.div(r.PRODUCTCOUNT, r.PRODUCTROLLWEIGHT, 0);
            } else if (r.PRODUCTROLLWEIGHT == null && r.PRODUCTROLLLENGTH != null) {//当有卷长时（订单数量/卷长）
                _totalRollCount = Calc.div(r.PRODUCTCOUNT, r.PRODUCTROLLLENGTH, 0);
            } else {//都为null时，不做计算
                _totalRollCount = "";
            }
        }
        _row.totalRollCount = _totalRollCount;
        //总托数
        _row.totalTrayCount = r.BCTRAYCOUNT;
        //需要生产数量(kg/套)
        _row.requirementCount = r.PRODUCTCOUNT;
        //已生产数量(kg/套)
        _row.producedCount = 0;
        //出货日期
        _row.deleveryDate = Assert.isEmpty(r.DELIVERYTIME) ? "" : r.DELIVERYTIME.substring(0, 10);
        //机台号
        _row.deviceCode = "";
        //备注
        _row.comment = r.PRODUCTMEMO;
        //是否已完成
        _row.isFinished = -1;
        //排序
        _row.sort = 0;
        //产品属性
        _row.productType = null;
        //打包数量
        _row.packagedCount = 0;
        //来自套材
        _row.fromTcId = r.PRODUCTID;
        //来自套材名称
        _row.fromTcName = r.FACTORYPRODUCTNAME;
        //是否套材
        _row.productIsTc = r.PRODUCTISTC;
        _row.procBomId = r.PROCBOMID;
        _row.packBomId = r.PACKBOMID;
        if (!contains(_row)) {
            $("#produceProducts").datagrid("appendRow", _row);
            if (_row.productIsTc === 1)
                $("#produceProducts").datagrid("expandRow", $("#produceProducts").datagrid("getRows").length - 1);
        }
    }

    let enableFilterd = false;

    function orderProductSelectLoadSuccess() {
        $(".datagrid-filter[name='X']").remove();
        if (enableFilterd) return;
        enableFilterd = true;
        //$(this).datagrid("enableFilter");
        /*[{
            field:'listprice',
            type:'numberbox',
            options:{precision:1},
            op:['equal','notequal','less','greater']
        }] */
        var rules2 = [{
            "field": "UNALLOCATECOUNT",
            "type": "textbox"
        }, {
            "field": "EDITTIMES",
            "type": "textbox"

        }, {
            "field": "SALESORDERSUBCODE",
            "type": "textbox"

        }, {
            "field": "PRODUCTBATCHCODE",
            "type": "textbox"

        }, {
            "field": "LEVELNO",
            "type": "textbox"

        }, {
            "field": "DRAWNO",
            "type": "textbox"

        }, {
            "field": "ROLLNO",
            "type": "textbox"

        }, {
            "field": "SALESORDERSUBCODEPRINT",
            "type": "textbox"

        }, {
            "field": "CONSUMERNAME",
            "type": "textbox"

        }, {
            "field": "CONSUMERPRODUCTNAME",
            "type": "textbox"

        }, {
            "field": "FACTORYPRODUCTNAME",
            "type": "textbox"

        }, {
            "field": "PRODUCTCOUNT",
            "type": "textbox"

        }, {
            "field": "ALLOCATECOUNT",
            "type": "textbox"

        }, {
            "field": "PRODUCEDROLLS",
            "type": "textbox"

        }, {
            "field": "PRODUCEDTRAYS",
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
            "field": "SALESORDERDATE",
            "type": "textbox"

        }, {
            "field": "USERNAME",
            "type": "textbox"
        },/*
		        	  {
		        	    "field": "SALESORDERISEXPORT",
		        	    "type": "textbox"

		        	  },
		        	  {
		        	    "field": "SALESORDERTYPE",
		        	    "type": "textbox"

		        	  }, */
            {
                "field": "SALESORDERTOTALMONEY",
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
                "field": "ISPLANED",
                "type": "combobox",
                "options": {
                    icons: [],
                    valueField: "v",
                    textField: "t",
                    data: [{v: "", t: "默认"}, {v: "-1", t: "全部"}, {v: "0", t: "未分配"}, {
                        v: "1",
                        t: "部分已分配"
                    }, {v: "2", t: "全部已分配"}],
                    onChange: function (nv, ov) {
                        $("#orderProductSelect").datagrid('addFilterRule', {
                            field: 'ISPLANED',
                            op: 'equal',
                            value: nv
                        });
                        $("#orderProductSelect").datagrid('doFilter');
                    }
                }
            }
        ];
        $(this).datagrid("enableFilter", rules2);
        $(".datagrid-filter[name='X']").remove();
        $(".datagrid-filter[name='UNALLOCATECOUNT']").next().remove();
        $(".datagrid-filter[name='RC']").remove();
        $(".datagrid-filter[name='TC']").remove();
        $(".datagrid-filter[name='PRODUCECOUNT']").remove();
    }

    function onOrderProductSelectDblClickRow(index, row) {
        appendRow(row);
        Dialog.close(selectSalesProductsId);
    }

    function reloadProduceOrder() {
        EasyUI.grid.search("orderProductSelect", "produceSalesOrderForm");
    }

    editingIndex = -1;

    /* function clickRow(index,row){
        if(row.packReq==""&&row.procReq==""){
            JQ.ajaxGet(path + "planner/produce/requirements?productId=" + row.productId, function(data) {
                console.log(data);
                $("#addOrEditPackReq").html(data.PACKREQ);
                $("#addOrEditProcReq").html(data.PROCREQ);
            });
        }else{
            $("#addOrEditPackReq").html(row.packReq);
            $("#addOrEditProcReq").html(row.procReq);
        }
    }
    function dbClickRow(index, row) {
        if (editingIndex != -1) {
            //if ($("#produceProducts").datagrid("validateRow", editingIndex)) {

            //	$("#produceProducts").datagrid("endEdit", editingIndex);

            editingIndex = index;
            $("#produceProducts").datagrid("beginEdit", index);
            //}
        } else {
            editingIndex = index;
            $("#produceProducts").datagrid("beginEdit", index);
        }
        clickRow(index,row);
    } */

    function onBeforeLoad_process_code(param) {
        var row = EasyUI.grid.getRowByIndex("produceProducts", editingIndex);
        param.bomType = (row.productIsTc === 1 ? "tc" : "ftc");
    }

    function processLoadSuccess(data) {
        onProcessSelect(null);
    }

    function onProcessSelect(record) {
        const ver = $("#produceProducts").datagrid('getEditor', {
            index: editingIndex,
            field: 'processBomVersion'
        });
        const code = $("#produceProducts").datagrid('getEditor', {
            index: editingIndex,
            field: 'processBomCode'
        });
        const row = EasyUI.grid.getRowByIndex("produceProducts", editingIndex);
        $(ver.target).combobox("clear");
        $(ver.target).combobox("options").url = path + "bom/version/?bomType=" + (row.productIsTc == 1 ? "tc" : "ftc") + "&code=" + $(code.target).combobox("getValue");
        $(ver.target).combobox("reload");
    }

    function processVersionLoaded() {
        const ver = $("#produceProducts").datagrid('getEditor', {
            index: editingIndex,
            field: 'processBomVersion'
        });
        const items = $(ver.target).combobox("getData");
        for (let i = 0; i < items.length; i++) {
            if (items[i].t.indexOf("默认") !== -1) {
                $(ver.target).combobox("select", items[i].v);
            }
        }
    }

    function onBcBomSelected(record) {
        const ver = $("#produceProducts").datagrid('getEditor', {
            index: editingIndex,
            field: 'bcBomVersion'
        });
        const code = $("#produceProducts").datagrid('getEditor', {
            index: editingIndex,
            field: 'bcBomCode'
        });
        $(ver.target).combobox("clear");
        $(ver.target).combobox("options").url = path + "bom/version/?bomType=bc" + "&code=" + $(code.target).combobox("getValue");
        $(ver.target).combobox("reload");
    }

    function bcLoadSuccess(data) {
        bcVersionLoaded();
    }

    function bcVersionLoaded() {
        const ver = $("#produceProducts").datagrid('getEditor', {
            index: editingIndex,
            field: 'bcBomVersion'
        });
        const items = $(ver.target).combobox("getData");
        for (let i = 0; i < items.length; i++) {
            if (items[i].t.indexOf("默认") !== -1) {
                $(ver.target).combobox("select", items[i].v);
            }
        }
    }

    function dateFormatter(value, row, index) {
        if (Assert.isEmpty(value))
            return "";
        return value.substring(0, 10);
    }

    function onClickRow(index, row) {
        JQ.ajaxGet(path + "planner/produce/details1?id=" + row.ID, function (data) {
            if (Tip.hasError(data))
                return;
            $("#producePlanDetails").datagrid("loadData", data);
        });
    }

    function onLoadSuccess(data) {
        $("#dg").datagrid("selectRow", 0);
        onClickRow(0, $("#dg").datagrid("getRows")[0]);
    }

    function orderProductRemove() {
        const rs = $("#produceProducts").datagrid("getChecked");
        for (var i = 0; i < rs.length; i++) {
            $("#produceProducts").datagrid("deleteRow", EasyUI.grid.getRowIndex("produceProducts", rs[i]));
        }
    }

    //查看审核
    const viewUrl = path + "audit/SC/{id}/state";

    function view() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r == null)
            return;
        dialogId = Dialog.open("查看审核状态", dialogWidth, dialogHeight, viewUrl.replace("{id}", r.ID), [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            //$("#" + dialogId).dialog("maximize");
        });
    }

    const reloadAuditUrl = path + "planner/produce/reloadAudit";

    function reloadAudit() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        Dialog.confirm(function () {
            JQ.ajax(reloadAuditUrl, "post", {
                id: r.ID,
                type: 2
            }, function (data) {
                filter();
            });
        });
    }

    //审核生产订单
    const doAudit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.CREATEUSERNAME !== userName) {
            Tip.warn("不能提审他人下达的计划");
            return;
        }
        if (r.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录，不能在提交审核！");
            return;
        }
        const wid = Dialog.open("审核", 480, 120, _auditCommitUrl + "?id=" + r.ID, [EasyUI.window.button("icon-ok", "提交审核", function () {
            EasyUI.form.submit("editAuditProduce", auditCommitUrl, function (data) {
                filter();
                Dialog.close(wid);
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            $("#editAuditProduce #name").textbox("setValue", "生产任务单审核，单号：" + r.PRODUCEPLANCODE);
        });
    };

    function getWorkShopCode(value, row, index) {
        return row.WORKSHOP;
    }

    function resultFormatter(value, row, index) {
        if (value == null || value === 0) {
            return "<div style='width:100%;text-align:center;'><label style='background: gray; width: 20px; height: 20px; border-radius: 20px; font-weight: bold; display: inline-block; color: white; text-align: center;'>▬</label></div>";
        } else if (value === -1) {
            return "<div style='width:100%;text-align:center;'><label style='background: #ff0000; width: 20px; height: 20px; border-radius: 20px; font-weight: bold; display: inline-block; color: white; text-align: center;'>✘</label></div>";
        } else if (value === 1) {
            return "<div style='width:100%;text-align:center;'><label style='background: green; width: 20px; height: 20px; border-radius: 20px; font-weight: bold; display: inline-block; color: white; text-align: center;'>✔</label></div>";
        } else if (value === 2) {
            return "<div style='width:100%;text-align:center;'><label style='background: #ff0000; width: 20px; height: 20px; border-radius: 20px; font-weight: bold; display: inline-block; color: white; text-align: center;'>✘</label></div>";
        } else if (value === 3) {
            return "已创建过编织计划";
        } else if (value === 4) {
            return "已创建过裁剪计划";
        }
    }

    let rec = null;

    function onSelect(record) {
        if (rec !== record.v) {
            $("#produceProducts").datagrid('loadData', []);
        }
        rec = record.v;
        Loading.show();
        $.ajax({
            url: path + "planner/produce/serial?workShopCode=" + record.v,
            type: 'get',
            dataType: 'text',
            success: function (data) {
                $("#producePlanCode").textbox("setValue", data);
                Loading.hide();
            }
        });
    }

    function _export() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        window.open(exportUrl + "?id=" + r.ID);
    }

    function _export1() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        console.log(r.AUDITSTATE);
        if (r.AUDITSTATE < 2) {
            Tips.warn("只能导出已审核通过的套材编织计划");
        }
        window.open(exportUrl1 + "?id=" + r.ID);
    }

    //查看生产物料需求
    function checkProcDetails() {
        /* 		var r= EasyUI.grid.getSelections("dg");
         var ids = [];
         for (var i = 0; i < r.length; i++) {
         ids.push(r[i].ID);
         } */
        const wid = Dialog.open("生产物料需求", 900, 500, path + "selector/user?singleSelect=true", [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            $("#" + wid).dialog("maximize");
        });
    }

    const turnBagContentUrl = path + "planner/tbp/add";
    let fbWinId;

    /**
     * 设置翻包领料单
     */
    function turnBagContent() {
        const r = EasyUI.grid.getOnlyOneSelected("producePlanDetails");
        const r2 = EasyUI.grid.getOnlyOneSelected("dg");
        if (!r.ISTURNBAGPLAN || r.ISTURNBAGPLAN === "生产") {
            Tip.warn("无需设置翻包领料单");
            return;
        }
        const buttons = [];
        if (r2.AUDITSTATE <= 0) {
            buttons.push(EasyUI.window.button("icon-save", "保存", function () {
                    //保存翻包领料
                    const rows = $("#oldOrders").datagrid("getRows");
                    const details = [];
                    const repeat = {};
                    for (let i = 0; i < rows.length; i++) {
                        $("#oldOrders").datagrid("beginEdit", i);
                        if (!$("#oldOrders").datagrid("validateRow", i)) {
                            return;
                        }
                        $("#oldOrders").datagrid("endEdit", i);
                        if (!isEmpty(repeat[rows[i].SALESORDERDETAILID])) {
                            if (repeat[rows[i].SALESORDERDETAILID] === rows[i].BATCHCODE) {
                                Tip.warn("重复的批次号");
                                return;
                            }
                        }
                        repeat[rows[i].SALESORDERDETAILID] = rows[i].BATCHCODE;
                        details.push({
                            salesOrderDetailId: rows[i].SALESORDERDETAILID,
                            producePlanDetailId: r.ID,
                            batchCode: rows[i].BATCHCODE,
                            turnBagCount: rows[i].TURNBAGCOUNT,
                            takeOutCountFromWareHouse: rows[i].TAKEOUTCOUNTFROMWAREHOUSE,
                            takeOutCount: 0,
                            fromProducePlanDetailId: rows[i].FROMPRODUCEPLANDETAILID,
                            memo: rows[i].MEMO
                        });
                    }
                    if (details.length === 0) {
                        Tip.warn("请添加待翻包的订单");
                        return;
                    }
                    Dialog.confirm(function () {
                        Loading.show();
                        $.ajax({
                            url: turnBagContentUrl,
                            type: 'post',
                            dataType: 'json',
                            contentType: 'application/json',
                            data: JSON.stringify(details),
                            success: function (data) {
                                Loading.hide();
                                if (Tip.hasError(data)) {
                                    return;
                                }
                                Tip.success("保存成功");
                                Dialog.close(fbWinId);
                                filter();
                            }
                        });
                    }, "确认保存?");
                })
            );
        }

        buttons.push(
            EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(fbWinId)
            })
        );

        /* buttons.push(
                EasyUI.window.button("icon-print", "打印", function() {
                    $("#"+fbWinId).printArea({popTitle:"翻包领料单",popClose:true});
                })
        ); */

        fbWinId = Dialog.open("翻包领料", dialogWidth, dialogHeight, turnBagContentUrl + "?id=" + r.ID, buttons, function () {
            loadTempData();
            Dialog.max(fbWinId);
        });
    }

    function _loadTask() {
        const row = EasyUI.grid.getOnlyOneSelected("producePlanDetails");
        loadTask(row.ID, row.FROMSALESORDERDETAILID);
    }
</script>
