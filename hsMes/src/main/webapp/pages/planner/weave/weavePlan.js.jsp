<!--
作者:肖文彬
日期:2016-10-18 13:38:47
页面:编织计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const datagridUrl = path + "planner/weavePlan/list";
    //更新index
    const updateIndex = path + "planner/weavePlan/update";
    //添加编织计划
    const addUrl = path + "planner/weavePlan/add";
    //编辑编织计划
    const editUrl = path + "planner/weavePlan/edit";
    //删除编织计划
    const deleteUrl = path + "planner/weavePlan/delete";
    //编织计划状态改成已完成
    const isFinish = path + "planner/weavePlan/isFinish";
    //设备
    const Device = path + "selector/device?singleSelect=false";
    //分配的设备
    const addDevice = path + "planner/weavePlan/addDevice";
    //优先排序
    const sort = path + "planner/weavePlan/sort";
    const chooseProducePlan = path + "selector/producePlan";
    const finishProduce = path + "planner/weavePlan/finishProduce";
    //取消完成
    const iscloseFinish = path + "planner/weavePlan/iscloseFinish";
    //取消关闭
    const isCancelClose = path + "planner/weavePlan/isCancelClose";
    let producePlanWindow = null;
    const dialogWidth = 930, dialogHeight = 450;
    let index1 = "";
    let row1 = "";
    let _data = null;
    const closeUrl = path + "common/close";

    function doClose() {
        const rows = $("#dg").datagrid("getSelections");
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
                    filter();
                }
            });
        }, "确认关闭");
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "wpSearchForm");
        //$("#dg").datagrid('reLoad');
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}planner/weavePlan/weaveList",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

    function ssort() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.CLOSED === 1) {
            Tip.warn("此编制计划已关闭");
            return;
        }
        if (r.ISFINISHED === 1) {
            Tip.warn("此编制计划已完成");
            return;
        }
        JQ.ajaxPost(sort, {
            id: r.ID
        }, function () {
            //$("#dg").datagrid('reLoad');
            $.ajax({
                url: path + "planner/weavePlan/weaveList",
                type: "post",
                dataType: "json",
                data: {
                    //"all" : 1,
                    "sort": "sort"
                },
                success: function (data) {
                    $('#dg').datagrid('loadData', data);
                    Loading.hide();
                },
                error: function () {
                    Loading.hide();
                }
            });
        })
    }

    let action = "";

    //生产任务完成
    function finish() {
        const rows = $("#dg").datagrid("getSelections");
        if (rows.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        action = "finish";
        $("#w").dialog("open");
        $("#w").panel({title: "完成编织任务"});
    }

    function loadData() {
        const rows = $("#dg").datagrid("getSelections");
        $("#dg_view").datagrid("loadData", rows);
    }

    function okEvent() {
        const r = $("#dg_view").datagrid("getRows");
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        if (action === "finish") {
            Dialog.confirm(function () {
                Loading.show();
                JQ.ajax(isFinish, "post", {
                    ids: ids.toString()
                }, function (data) {
                    Loading.hide();
                    Tip.success("操作成功");
                    filter();
                    $("#w").dialog("close");
                });
            }, '确认将编织计划设置为已完成，将从设备的任务列表中删除？');
        } else {
            Dialog.confirm(function () {
                Loading.show();
                $.ajax({
                    url: closeUrl + "?type=WEAVE&ids=" + ids.join(","),
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        Loading.hide();
                        if (Tip.hasError(data)) {
                            return;
                        }
                        Tip.success("关闭成功");
                        filter();
                    }
                });
                $("#w").dialog("close");
            }, "确认关闭");
        }
    }

    //生产任务关闭
    function closeTask() {
        const rows = $("#dg").datagrid("getSelections");
        if (rows.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        action = "close";
        $("#w").dialog("open");
        $('#w').panel({title: "关闭编织任务"});
    }

    function doSelect(record) {
        loadProducePlans();
    }

    /**
     * 加载左侧 DataList数据，如果无查询条件，默认最新100条数据
     */
    function loadProducePlans() {
        filter();
    }

    //内部函数
    function loadDataList(data) {
        const rs = data.rows;
        for (let i = 0; i < rs.length; i++) {
            rs[i]["CREATETIME"] = rs[i]["CREATETIME"].substring(0, 10);
        }
        $("#dl").datalist({
            "data": rs,
            groupFormatter: function (value, rows) {
                return value + " <font color=red>" + rows.length + "条计划</font>";
            }
        });
        //默认选中第一行的生产计划
        if (rs.length !== 0) {
            $("#dl").datalist("selectRow", 0);
        }
        Loading.hide();
    }

    function loadMrp(index, row) {
        index1 = index;
        row1 = row;
        Loading.show("加载中");
        $.ajax({
            url: path + "planner/weavePlan/list",
            type: "post",
            dataType: "json",
            data: {
                "planCode": row.PRODUCEPLANCODE
            },
            success: function (data) {
                Loading.hide();
                _data = data;
                $("#dg").datagrid("loadData", data);
            }
        });
    }

    function _common_device_onLoadSuccess() {
        const array = $("#device").val().split(",");
        for (let i = 0; i < array.length; i++) {
            const rs = $("#_common_device_dg").datagrid('getRows');
            for (let a = 0; a < rs.length; a++) {
                if (rs[a].ID === parseInt(array[i])) {
                    $("#_common_device_dg").datagrid("selectRow", a);
                }
            }
        }
    }

    //更新index
    function updateSort() {
        const data = $("#dg").datagrid('getRows');
        for (let i = 0; i < data.length; i++) {
            const index = $("#dg").datagrid('getRowIndex', data[i]);
            $.ajax({
                url: updateIndex,
                type: "post",
                dataType: "json",
                data: {
                    index: index,
                    id: data[i].ID
                },
                success: function (data) {
                    editIndex = undefined;
                    Loading.hide();
                    $("#dg").datagrid('reLoad');
                    if (Tip.hasError(data)) {
                        return;
                    }
                }
            });
        }
    }

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", 500, 240, editUrl + "?id=" + row.ID, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("weavePlanForm", editUrl, function (data) {
                loadMrp(index1, row1);
                Dialog.close(wid);

            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })]);
    };

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
            return '正常';
        } else if (value === 1) {
            return '已关闭';
        } else if (value === 3) {
            return "";
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
        if (isEmpty(row.CLOSED) || row.CLOSED === 0) {
        } else {
            style += "text-decoration:line-through;background: #b8b5bd;";
        }
        if (row.ISFINISHED === 1 && row.CLOSED !== 1) {
            style += "background: #8edd9b;";
        }
        return style;
    }

    function formatterC(index, row, value) {
        if (row.ISCOMEFROMTC === 1) {
            return '裁剪车间';
        }
        if (row.ISCOMEFROMTC === "" || row.ISCOMEFROMTC == null) {
            return row.CONSUMERNAME;
        }
        if (row.ISCOMEFROMTC === 0) {
            return row.CONSUMERNAME;
        }
    }

    //选择生产订单号
    function ChooseProducePlan() {
        producePlanWindow = Dialog.open("选择生产订单号", 850, 450, chooseProducePlan, [EasyUI.window.button("icon-save", "确认", function () {
            const r = EasyUI.grid.getOnlyOneSelected("_common_producePlan_dg");
            $('#producePlan').searchbox('setValue', r.PRODUCEPLANCODE);
            Dialog.close(producePlanWindow);
        }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
            Dialog.close(producePlanWindow);
        })]);
    }

    //选择订单信息双击事件
    function _common_producePlan_dbClickRow(index, row) {
        $('#producePlan').searchbox('setValue', row.PRODUCEPLANCODE);
        Dialog.close(producePlanWindow);
    }

    //出货时间
    function orderDateFormat(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    //查看工艺bom明细
    function bomVersionView(value, row, index) {
        if (value == null) {
            return "";
        } else if (row.PROCESSBOMCODE == null || row.PRODUCTPROCESSBOMVERSION === "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_bomVersionView(" + row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value + "</a>"
        }
    }

    let dialogId;

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

    function editDevices(index, row) {
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, addDevice + "?id=" + row.ID,
            [EasyUI.window.button("icon-save", "保存", function () {
                if ($("#weavePlanForm").form("validate")) {
                    Tip.warn("请输入必填项");
                    return;
                }
                if (getTabCount() === 0) {
                    Tip.warn("请分配机台");
                    return;
                }
                Dialog.close(wid);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
                Dialog.max(wid);
            }
        );
    }

    function export1() {
        let condition = "";
        x = $("form").serializeArray();
        $.each(x, function (i, field) {
            if (field.value !== '' && field.value != null) {
                if (field.name === "searchType") {
                    condition += (field.name + "=" + field.value + "&");
                } else {
                    condition += (field.name.substring(7, field.name.length - 1) + "=" + field.value + "&");
                }
            }
        });
    }

    function isPlanedFormatter(value, row, index) {
        if (value == null || value === 0) {
            return "未分配";
        } else if (value === 1) {
            return "已分配";
        } else if (value === 3) {
            return "";
        }
    }

    //取消完成
    function closefinish() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("请至少选择一个计划");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].ISFINISHED !== 1) {
                Tip.warn("此编制计划未完成");
                return;
            }
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(iscloseFinish, "post", {
                ids: ids.toString()
            }, function (data) {
                doSelect();
            });
        }, '确认将编织计划改为取消完成？');
    }

    function cancelClose() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("请至少选择一个计划");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].CLOSED === 0 || r[i].CLOSED == null) {
                Tip.warn("此编制计划未关闭");
                return;
            }
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(isCancelClose, "post", {
                ids: ids.toString()
            }, function (data) {
                doSelect();
            });
        }, '确认将编织计划改为取消关闭？');
    }

    function export2() {
        //alert(JQ.getFormAsString("wpSearchForm"));
        location.href = encodeURI(path + "planner/weavePlan/export?" + JQ.getFormAsString("wpSearchForm"));
        //location.href=path + "stock/productInRecord/export1";//+JQ.getFormAsString("productInRecordSearchForm");
        /* var orders = JQ.getFormAsJson("productInRecordSearchForm");
        log(orders)
            $.ajax({
                url : path + "stock/productInRecord/export1",
                type : 'post',
                dataType : 'json',
                data : orders,
                success : function() {
                    log(111)
                }
            }); */
    }

    //客户简称为裁剪车间时添加叶型
    function formatterAddYX(value, row) {
        const yx = row.YX;
        if (value === "裁剪车间") {
            if (yx) {
                str = yx.split("叶型为：")[1];
            }
            if (str) {
                return value + " (" + str + ")";
            }
        }
        return value;
    }
</script>