<!--
作者:宋黎明
日期:2016-10-18 13:35:17
页面:裁剪计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    var datagridUrl = path + "planner/cutPlan/list";
    //添加裁剪计划
    var addUrl = path + "planner/cutPlan/add";
    //编辑裁剪计划
    var editUrl = path + "planner/cutPlan/edit";
    //更新排序字段
    var sortEditUrl = path + "planner/cutPlan/sortEdit";
    //删除裁剪计划
    var deleteUrl = path + "planner/cutPlan/delete";
    //裁剪计划状态改成已完成
    var isFinish = path + "planner/cutPlan/isFinish";
    var copyProductUrl = path + "planner/cutPlan/copyProduct";
    var saveFabricUrl = path + "finishProduct/add";
    //选择用户url
    var chooseUser = path + "selector/cuser?singleSelect=false";

    var tcBomPartTreeUrl = path + "planner/cutPlan/findParts";

    var closeUrl = path + "common/close";

    var tasks = {};
    var taskId = 0;

    function doClose() {
        var rows = $("#dg").datagrid("getSelections");
        if (rows.length == 0) {
            Tip.warn("请至少选择一条记录");
            return;
        }
        var type = "CUT";
        var ids = [];
        for (var i = 0; i < rows.length; i++) {
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

    var chooseProducePlan = path + "selector/producePlan";
    var userWindow = null;
    var producePlanWindow = null;
    var flag = true;
    var _row = null;
    var _index = null;
    var ddv = null;
    var ddv1 = null;
    var cutPlanId = null;

    function filter() {
        //$("#dg").datagrid("reload");
        loadCutPlan(_index, _row);
    }

    $(function () {
        loadProducePlans();
        $("#dg").datagrid({
            url: "${path}",
            onBeforeLoad: dgOnBeforeLoad,
            rowStyler: closeStyler,
            view: detailview,
            onLoadSuccess: function (data) {
                var rows = data.rows;
                for (var i = 0; i < rows.length; i++) {
                    $(this).datagrid('expandRow', i);
                }
            },
            detailFormatter: function (index, row) {
                return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                    url: path + 'bom/plan/tc/ver/parts/' + row.FROMSALESORDERDETAILID + '/' + row.PRODUCEPLANDETAILID,
                    fitColumns: true,
                    singleSelect: true,
                    rownumbers: true,
                    autoRowHeight: true,
                    width: 500,
                    loadMsg: '',
                    height: 'auto',
                    columns: [[{
                        field: 'partName',
                        title: '部件名称',
                        width: 30
                    }, {
                        field: 'planPartCount',
                        title: '计划数量',
                        width: 25
                    }, {
                        field: 'partCount',
                        title: '订单数量',
                        width: 25
                    }, {
                        field: 'partBomCount',
                        title: 'BOM数量',
                        width: 25
                    }, {
                        field: 'createCutTask',
                        title: '操作',
                        width: 45,
                        formatter: function (v, r, i) {
                            if (v == undefined || v == 0) {
                                var now = new Calendar().format('yyyy-MM-dd');
                                var task = {
                                    id: index + '_' + i,
                                    pcId: r.id,
                                    order: row.SALESORDERCODE,
                                    batch: row.BATCHCODE,
                                    partName: r.partName,
                                    partId: r.partId,
                                    consumerSimpleName: row.CONSUMERSIMPLENAME,
                                    consumerId: row.CONSUMERID,
                                    consumerCategory: row.CONSUMERCATEGORY,
                                    suitCount: r.planPartCount,
                                    deliveryDate: row.DELEVERYDATE.substring(0, 10),
                                    createTime: now,
                                    createUserName: '${userName }',
                                    cpId: row.ID
                                }
                                taskId++;
                                tasks[taskId] = task;
                                return '<button id=\'' + task.id + '\' onclick=createCutTask(' + taskId + ') >生成任务单</button>';
                            } else {
                                return '<font color=green>任务单已创建</font>';
                            }
                        }
                    }]],
                    onResize: function () {
                        $('#dg').datagrid('fixDetailRowHeight', index);
                    },
                    onLoadSuccess: function () {
                        setTimeout(function () {
                            $('#dg').datagrid('fixDetailRowHeight', index);
                        }, 0);
                    }
                });
                $('#dg').datagrid('fixDetailRowHeight', index);
            }
        });
    });

    /**
     * 加载左侧 DataList数据，如果无查询条件，默认最新20条数据
     */
    function loadProducePlans() {
        Loading.show("加载中");
        var t = $("#searchInput").searchbox("getValue");
        var kh = $("#searchInput1").searchbox("getValue");
        var productname = $("#searchInput2").searchbox("getValue");
        var batchcode = $("#searchInput3").searchbox("getValue");
        var salesOrderCode = $("#searchInput4").searchbox("getValue");
        if (t == "" && kh == "" && productname == "" && batchcode == "" && salesOrderCode == "") {
            $.ajax({
                url: path + "planner/cutPlan/datalist",
                type: "post",
                dataType: "json",
                data: {
                    "all": 0,
                    "rows": 20
                },
                success: function (data) {
                    loadDataList(data);
                },
                error: function () {
                    Loading.hide();
                }
            });
        } else {
            $.ajax({
                url: path + "planner/cutPlan/datalist",
                type: "post",
                dataType: "json",
                data: {
                    "all": 4,
                    "filter[simplename]": "like:" + kh,
                    "filter[code]": "like:" + t,
                    "filter[productname]": "like:" + productname,
                    "filter[batchcode]": "like:" + batchcode,
                    "filter[salesOrderCode]": "like:" + salesOrderCode
                },
                success: function (data) {
                    loadDataList(data);
                },
                error: function () {
                    Loading.hide();
                }
            });
        }

        //内部函数
        function loadDataList(data) {
            var rs = data.rows;
            for (var i = 0; i < rs.length; i++) {
                rs[i]["CREATETIME"] = rs[i]["CREATETIME"].substring(0, 10);
            }
            $("#dl").datalist(
                {
                    "data": rs,
                    groupFormatter: function (value, rows) {
                        return value + " <font color=red>" + rows.length
                            + "条计划</font>";
                    }/* ,
                    textFormatter : function(value, row, index) {
                    	if(row.SALESORDERCODE==null){
                    		return value + " [临时计划,无订单号]";
                    	}else{
                    		return value + " [订单号" + row.SALESORDERCODE + "]";       																																																																																																																																																																																																																																																																											} */
                });
            //默认选中第一行的生产计划
            if (rs.length != 0) {
                $("#dl").datalist("selectRow", 0);
            }
            Loading.hide();
        }
    }

    //选中计划单号加载裁剪计划
    function loadCutPlan(index, row) {
        _index = index;
        _row = row;
        Loading.show("加载中");
        $.ajax({
            url: path + "planner/cutPlan/list",
            type: "post",
            dataType: "json",
            data: {
                "planCode": row.PRODUCEPLANCODE
            },
            success: function (data) {
                Loading.hide();
                $("#dg").datagrid("loadData", data);
            }
        });
    }

    function formatterState(value, row, index) {
        if (value == null) {
            return "未生成裁剪日计划";
        }
        return auditStateFormatter(row.AUDITSTATE);
    }

    //拖动行更新数据
    function updateData() {
        var data = $("#dg").datagrid('getRows');
        for (var i = 0; i < data.length; i++) {
            $.ajax({
                url: sortEditUrl,
                type: 'post',
                dataType: 'json',
                data: {
                    index: $("#dg").datagrid('getRowIndex', data[i]),
                    id: data[i].ID
                },
                success: function (data) {
                    //editIndex = undefined;
                    filter();
                }
            });
        }
    }

    var userIds = [];

    //选择用户
    function ChooseUser() {
        userIds = [];
        userWindow = Dialog.open("选择人员信息", 850, 450, chooseUser, [
            EasyUI.window.button("icon-save", "确认", function () {
                var r = EasyUI.grid.getSelections("_common_user_dg");
                var userName = "";
                var a = "";
                for (var i = 0; i < r.length; i++) {
                    if (i === 0) {
                        a = "";
                    } else {
                        a = ",";
                    }
                    userName += a + r[i].USERNAME;
                    userIds.push(r[i].ID);
                }
                $('#userName').searchbox('setValue', userName);
                JQ.setValue('#userIds', userIds);
                Dialog.close(userWindow);
            }),
            EasyUI.window.button("icon-cancel",
                "<spring:message code="Button.Cancel" />", function () {
                    Dialog.close(userWindow);
                })]);
    }

    function _common_user_onLoadSuccess(data) {
        var array = $("#userIds").val().split(",");
        for (var i = 0; i < array.length; i++) {
            var rs = $("#_common_user_dg").datagrid('getRows');
            for (var a = 0; a < rs.length; a++) {
                if (rs[a].ID == parseInt(array[i])) {
                    $("#_common_user_dg").datagrid("selectRow", a);
                }
            }
        }
    }

    //选择用户双击事件
    function _common_user_dbClickRow(index, row) {
        $('#userName').searchbox('setValue', row.USERNAME);
        JQ.setValue('#userIds', row.ID);
        Dialog.close(userWindow);
    }

    //部件指定人员
    function editPart(row, cutPlanId) {
        if (row.children != "") {
            Tip.warn("该部件无法指定人员！");
            return;
        }
        var wid = Dialog.open("编辑", 380, 155, editUrl + "?partsId=" + row.id
            + "&cutPlanId=" + cutPlanId + "&userIds=" + row.userId, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("cutPlanForm", editUrl, function (data) {
                    //filter();
                    row.userName = $("#userName").searchbox("getValue");
                    row.userId = $("#userIds").val();
                    ddv.treegrid('update', {
                        id: row.id,
                        row: row
                    });
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })]);
    }

    //选择生产订单号
    function ChooseProducePlan() {
        producePlanWindow = Dialog
            .open(
                "选择生产订单号",
                850,
                450,
                chooseProducePlan,
                [
                    EasyUI.window
                        .button(
                            "icon-save",
                            "确认",
                            function () {
                                var r = EasyUI.grid
                                    .getOnlyOneSelected("_common_producePlan_dg");
                                $('#producePlan')
                                    .searchbox(
                                        'setValue',
                                        r.PRODUCEPLANCODE);
                                Dialog
                                    .close(producePlanWindow);
                            }),
                    EasyUI.window
                        .button(
                            "icon-cancel",
                            "<spring:message code="Button.Cancel" />",
                            function () {
                                Dialog
                                    .close(producePlanWindow);
                            })]);
    }

    //选择订单信息双击事件
    function _common_producePlan_dbClickRow(index, row) {
        $('#producePlan').searchbox('setValue', row.PRODUCEPLANCODE);
        Dialog.close(producePlanWindow);
    }

    function formatterIsFinish(value, row) {
        if (value == 1) {
            return '已完成';
        }
        if (value == -1) {
            return '未完成';
        }
    }

    function finish() {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        var ids = [];
        for (var i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(isFinish, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        }, '确认更改该裁剪计划的完成状态？');
    }

    function addOrder() {
        var r = EasyUI.grid.getSelections("dg");
        var row = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        if (r.length > 1) {
            Tip.warn("请选择唯一数据");
            return;
        }
        /* if (row.AUDITSTATE == 1 || row.AUDITSTATE == -1 || row.AUDITSTATE == 2) {
            Tip.warn("审核中或审核通过的记录不能编辑！");
            return;
        } */
        //需要增加胚布订单的裁剪计划
        var id = r[0].ID;

        if (r[0].ISCREATWEAVE === 1) {
            //获取添加订单页面，默认客户为裁剪车间，默认订单备注为裁剪任务的批次号和生产任务单号，产品为BOM中的胚布
            var wid = Dialog.open("胚布订单", 380, 155,
                addUrl + "?cutPlanId=" + id, [EasyUI.window.button(
                    "icon-cancel", "关闭", function () {
                        Dialog.close(wid);
                    })], function () {
                    $("#" + wid).dialog("maximize");
                    $("#product_dg").datagrid("loadData", details);
                    Loading.show();
                    $.ajax({
                        url: path + "salesOrder/serial",
                        type: "post",
                        data: {
                            export: -1
                        },
                        dataType: "text",
                        success: function (data) {
                            Loading.hide();
                            $("#salesOrderCode").textbox("setValue", data);
                        },
                        error: function () {
                            Loading.hide();
                        }
                    });
                });
        } else {
            //获取添加订单页面，默认客户为裁剪车间，默认订单备注为裁剪任务的批次号和生产任务单号，产品为BOM中的胚布
            var wid = Dialog.open("胚布订单", 380, 155,
                addUrl + "?cutPlanId=" + id, [
                    EasyUI.window.button("icon-save", "保存", function () {
                        saveForm(wid);
                    }),
                    EasyUI.window.button("icon-cancel", "关闭",
                        function () {
                            Dialog.close(wid);
                        })], function () {
                    $("#" + wid).dialog("maximize");
                    $("#product_dg").datagrid("loadData", details);
                    Loading.show();
                    $.ajax({
                        url: path + "salesOrder/serial",
                        type: "post",
                        data: {
                            export: -1
                        },
                        dataType: "text",
                        success: function (data) {
                            Loading.hide();
                            $("#salesOrderCode").textbox("setValue", data);
                        },
                        error: function () {
                            Loading.hide();
                        }
                    });
                });
        }
    }

    var selectProductWindowId;

    function selectProduct() {
        var productModel = null;
        var productWidth = null;
        var prossBom = null;
        var rows = $("#product_dg").datagrid("getSelections");
        if (rows.length > 1) {
            Tip.warn("最多只能选择一条产品");
            return;
        }
        if (rows.length === 1) {
            productModel = rows[0].productModel;
            productWidth = rows[0].productWidth;
            prossBom = rows[0].productProcessCode;
        }
        if (productModel != null) {
            isContainsCondition = true;
        }
        if (productWidth != null) {
            isContainsCondition = true;
        }
        if (prossBom != null) {
            isContainsCondition = true;
        }
        var comsumerId = $("#finalConsumerId").val();
        //TODO 修改选择产品页面，不适用通用页面,只显示胚布，需要原客户,页面加入复制按钮，加入筛选栏
        selectProductWindowId = Dialog
            .open(
                "选择产品",
                900,
                400,
                encodeURI(path
                    + "planner/cutPlan/selectProductPage?singleSelect=false&showCode=false&finalConsumerId="
                    + comsumerId),
                [
                    EasyUI.window
                        .button("icon-ok", "选择",
                            function () {
                                var rows = $("#_common_product_dg").datagrid("getChecked");
                                if (rows.length === 0) {
                                    Tip.warn("至少选择一个产品");
                                    return;
                                }
                                /* $('#product_dg').datagrid('loadData',[]); */
                                for (var i = 0; i < rows.length; i++) {
                                    addToProductDg(rows[i]);
                                }
                                Dialog.close(selectProductWindowId);
                            }),
                    EasyUI.window
                        .button(
                            "icon-cancel",
                            "关闭",
                            function () {
                                Dialog
                                    .close(selectProductWindowId);
                            })], function () {
                    if (productModel != null) {
                        /* url+=("&filter[productModel]=like:"+productModel); */
                        $("#productModel").textbox("setValue",
                            productModel);
                    }
                    if (productWidth != null) {
                        /* url+=("&filter[productWidth]=like:"+productWidth); */
                        $("#productWidth").textbox("setValue",
                            productWidth);
                    }
                    if (prossBom != null) {
                        /* url+=("&filter[productProcessCode]=like:"+prossBom); */
                        $("#procCode").textbox("setValue", prossBom);
                    }
                    /*var consumerId = $("#finalConsumerId").val();
                    $("#consumerId").val(consumerId);
                    $(".datagrid .datagrid-pager").hide();
                    $("#_common_product_dg_Form").hide();*/
                    //var url=path+"finishProduct/list?filter[productIsTc]=-1&filter[consumerId]="+comsumerId;
                    /* var url=path+"finishProduct/list";
                    $('#_common_product_dg').datagrid({url:url}); */
                    //$("#_common_product_dg").datagrid("reload");
                    //cosole.log(url);
                    //_common_product_filter();
                    $("#" + selectProductWindowId).dialog("maximize");
                    isContainsCondition = false;
                    //_common_product_filter();
                }, function () {
                    Dialog.close(selectProductWindowId);
                });
    }

    var isContainsCondition = false;

    function addToProductDg(r) {
        var code = $("#code").val();
        var _row = {
            "productId": r.ID,
            "productBatchCode": "",
            "salesOrderSubCode": "cj-" + code,
            "consumerProductName": r.CONSUMERPRODUCTNAME,
            "factoryProductName": r.FACTORYPRODUCTNAME,
            "productWidth": r.PRODUCTWIDTH,
            "productRollLength": r.PRODUCTROLLLENGTH,
            "productRollWeight": r.PRODUCTROLLWEIGHT,
            "productProcessCode": r.PRODUCTPROCESSCODE,
            "productProcessBomVersion": r.PRODUCTPROCESSBOMVERSION,
            "productPackagingCode": r.PRODUCTPACKAGINGCODE,
            "productPackageVersion": r.PRODUCTPACKAGEVERSION,
            "productRollCode": r.PRODUCTROLLCODE,
            "productBoxCode": r.PRODUCTBOXCODE,
            "productTrayCode": r.PRODUCTTRAYCODE,
            "productModel": r.PRODUCTMODEL,
            "productMemo": r.PRODUCTMEMO,
            "productCount": 1,
            "productIsTc": r.PRODUCTISTC,
            "deliveryTime": "",
            "produceCount": 0,
            "procBomId": r.PROCBOMID,
            "packBomId": r.PACKBOMID,
            "packagingCount": 0
        };
        $("#product_dg").datagrid("appendRow", _row);
    }

    function _common_product_dbClickRow(index, row) {
        addToProductDg(row);
        Dialog.close(selectProductWindowId);
    }

    function removeProduct() {
        var rows = $("#product_dg").datagrid("getSelections");
        for (var i = 0; i < rows.length; i++) {
            $("#product_dg").datagrid("deleteRow",
                $("#product_dg").datagrid("getRowIndex", rows[i]));
        }
    }

    function changeSerial(r) {
        Loading.show();
        $.ajax({
            url: path + "salesOrder/serial",
            type: "post",
            data: {
                export: r.v
            },
            dataType: "text",
            success: function (data) {
                Loading.hide();
                $("#salesOrderCode").textbox("setValue", data);
            },
            error: function () {
                Loading.hide();
            }
        });
    }

    //查看工艺bom明细
    function bomVersionView(value, row, index) {
        if (value == null) {
            return "";
        } else {
            return "<a href='#' title='" + value
                + "' class='easyui-tooltip' onclick='_bomVersionView("
                + row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value
                + "</a>"
        }
    }

    var editingIndex = -1;

    function clickRow(index, row) {
        if (editingIndex != -1) {
            if ($("#product_dg").datagrid("validateRow", editingIndex)) {

                $("#product_dg").datagrid("endEdit", editingIndex);

                editingIndex = index;
                $("#product_dg").datagrid("beginEdit", index);
            }
        } else {
            editingIndex = index;
            $("#product_dg").datagrid("beginEdit", index);
        }
    }

    function saveForm(wid) {
        if ($("#salesOrderForm").form("validate")) {
            if ($("#product_dg").datagrid("getRows").length != 0) {
                if (endEdit()) {
                    var order = JQ.getFormAsJson("salesOrderForm");
                    order.details = $("#product_dg").datagrid("getData").rows;
                    Loading.show();
                    $.ajax({
                        url: path + "planner/cutPlan/add",
                        type: 'post',
                        dataType: 'json',
                        contentType: 'application/json',
                        data: JSON.stringify(order),
                        success: function (data) {

                            Loading.hide();
                            if (Tip.hasError(data)) {
                                return;
                            }
                            if (data == "保存成功") {
                                Tip.warn(data);
                                filter();
                                Dialog.close(wid);
                                return;
                            } else {
                                Tip.error(data);
                                return;
                            }

                            //filter();
                        }
                    });
                }
            } else {
                Tip.warn("请选择订单产品！");
            }
        }
    }

    function endEdit() {
        var rows = $("#product_dg").datagrid("getRows");
        for (var i = 0; i < rows.length; i++) {
            editingIndex = i;
            $("#product_dg").datagrid("beginEdit", i);
            if (!$("#product_dg").datagrid("validateRow", i)) {
                return false;
            } else {
                $("#product_dg").datagrid("endEdit", i);
            }
            $("#product_dg").datagrid("endEdit", i);
            /* $("#product_dg").datagrid("endEdit", j);
            for (var j = rows.length - 1; j > i; j--) {
                if (rows[i].salesOrderSubCode == rows[j].salesOrderSubCode
                        && rows[i].productId == rows[j].productId) {
                    Tip.warn("相同的订单号:" + rows[i].salesOrderSubCode + "；产品:"
                            + rows[i].productModel);
                    return false;
                }
            } */
        }
        editingIndex = -1;
        return true;
    }

    function copyProduct() {
        var rows = EasyUI.grid.getSelections("_common_product_dg");
        if (rows.length == 0) {
            Tip.warn("请选择一条数据进行复制");
            return;
        }
        if (rows.length > 1) {
            Tip.warn("请选择单行数据");
            return;
        }
        var id = rows[0].ID;
        var finalConsumerId = $("#finalConsumerId").val();
        //打开产品信息编辑页面
        var wid = Dialog.open("复制产品", 380, 155, copyProductUrl + "?productId=" + id + "&finalConsumerId=" + finalConsumerId, [
            EasyUI.window.button("icon-save", "保存", function () {
                //saveForm();
                EasyUI.form.submit("finishProductForm", saveFabricUrl,
                    function (data) {
                        _common_product_filter();
                        Dialog.close(wid);
                    });
                Dialog.close(wid);
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
        });
    }

    function formatterIsCreateWeave(value, row, index) {
        if (value == 1) {
            return "已生成";
        } else {
            return "未生成";
        }
    }

    function resetDate() {
        var d = $("#sss").datebox("getValue");
        var datas = $("#product_dg").datagrid('getData');
        for (var a = 0; a < datas.rows.length; a++) {
            datas.rows[a].deliveryTime = d;
        }
        $("#product_dg").datagrid('loadData', datas);
    }

    //批量修改订单号
    function resetSales() {
        var d = $("#ssss").textbox("getValue");
        var datas = $("#product_dg").datagrid('getData');
        for (var a = 0; a < datas.rows.length; a++) {
            datas.rows[a].salesOrderSubCode = d;
        }
        $("#product_dg").datagrid('loadData', datas);
    }

    function orderDateFormat(value, row, index) {
        if (value == undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }
    var buttonId = undefined;
    //部件计划数量明细ID，订单号,批次号，部件名称，部件ID，客户简称，客户ID，客户大类，套数，发货日期，创建时间，创建人，裁剪计划ID
    function createCutTask(taskId) {
        var task = tasks[taskId];
        buttonId = task.id;
        //log(pcId,order,batch,partName,partId,consumerSimpleName,consumerId,consumerCategory,suitCount,deliveryDate,createTime,createUserName,cpId);
        Loading.show("正在获取单号");
        $.ajax({
            url: path + "siemens/cutTask/serial",
            type: "get",
            dataType: "text",
            success: function (data) {
                Loading.hide();
                $("#dlg").dialog("open");
                $("#taskCode").textbox("setValue", data);
                $("#deliveryDate").textbox("setValue", task.deliveryDate);
                $("#orderCode").textbox("setValue", task.order);
                $("#batchCode").textbox("setValue", task.batch);
                $("#consumerSimpleName").textbox("setValue", task.consumerSimpleName);
                $("#consumerCategory").val(task.consumerCategory);
                $("#partName").textbox("setValue", task.partName);
                $("#suitCount").textbox("setValue", task.suitCount);
                $("#partId").val(task.partId);
                $("#consumerId").val(task.consumerId);
                $("#cutPlanId").val(task.cpId);
                $("#pcId").val(task.pcId);
                if (task.consumerCategory == "1") {
                    $("#taskConsumerCategoryX").textbox("setValue", "国内");
                }
                if (task.consumerCategory == "2") {
                    $("#taskConsumerCategoryX").textbox("setValue", "国外");
                }
            }
        });
    }

    function saveCutTaskForm() {
        EasyUI.form.submit("cutTaskForm", path + "siemens/cutTask/add", function (data) {
            Tip.success("保存成功");
            $("#dlg").dialog("close");
            $("#" + buttonId).remove();
        })
    }
</script>