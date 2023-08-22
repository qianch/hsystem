<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单JS文件
-->
<%@page import="com.bluebirdme.mes.core.constant.Constant" %>
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
    const closeUrl = path + "common/close";
    const dialogWidth = 700, dialogHeight = 500;
    const exportUrl = path + "salesOrder/export";
    let action = "add";
    //复制销售订单
    const copy1 = path + "salesOrder/copy";
    const currentConsumerCode = "";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "salesOrderSearchForm");
    }

    function timeFormatter(value, index, row) {
        return value.substring();
    }

    function editTimesFormatter(value, index, row) {
        return (isEmpty(value) ? 0 : value) + "次";
    }

    let timeout = null;
    $(function () {
        $(".textbox-text").keyup(function () {
            $("input[name='" + $(this).parent().prev().attr("textboxname") + "']").val($(this).val());
            clearTimeout(timeout);
            timeout = setTimeout(filter, 500);
        });
        Loading.show("数据加载中");
        $('#dg').datagrid({
            view: detailview,
            fit: true,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table id="ddv-p-' + index + '"></table></div>';
            },
            onExpandRow: function (index, row) {
                $("#ddv-p-" + index).datagrid({
                    view: detailview,
                    url: path + 'salesOrder/product?orderId=' + row.ID,
                    width: '100%',
                    fitColumns: false,
                    singleSelect: true,
                    rownumbers: true,
                    loadMsg: '',
                    onClickRow: function (index, row) {
                        $("#salesOrderMemo").panel({
                            content: "<pre>" + (isEmpty(row.PRODUCTMEMO) ? "无" : row.PRODUCTMEMO) + "</pre>"
                        });
                    },
                    rowStyler: detailRowStyler,
                    columns: [[{
                        field: 'SALESORDERSUBCODE',
                        title: '订单号'
                    }, {
                        field: 'PRODUCTBATCHCODE',
                        title: '批次号'
                    }, {
                        field: 'SALESORDERSUBCODEPRINT',
                        title: '客户订单号'
                    }, {
                        field: 'CONSUMERPRODUCTNAME',
                        width: "120px",
                        title: '客户产品名称'
                    }, {
                        field: 'FACTORYPRODUCTNAME',
                        width: 120,
                        title: '厂内名称'
                    }, {
                        field: 'PRODUCTMODEL',
                        width: 120,
                        title: '产品型号'
                    },
                        {
                            field: 'PRODUCTCOUNT',
                            width: 120,
                            title: '数量',
                            styler: processStyler,
                            formatter: countFormatter
                        }, {
                            field: 'TOTALWEIGHT',
                            width: 120,
                            title: '总重',
                            formatter: totalWeightFormatter2
                        }, {
                            field: 'TOTALMETRES',
                            width: 120,
                            title: '总米长',
                            formatter: totalMetresFormatter
                        }, {
                            field: 'RC',
                            title: '生产卷数',
                            width: 80,
                            formatter: rcFormatter
                        }, {
                            field: 'TC',
                            title: '打包托数',
                            width: 80,
                            formatter: tcFormatter
                        }, {
                            field: 'PRODUCECOUNT',
                            title: '生产进度',
                            width: 80,
                            formatter: processFormatter2
                        }, {
                            field: 'DELIVERYTIME',
                            title: '发货时间',
                            formatter: function (value, row, index) {
                                if (value == undefined)
                                    return null;
                                return new Calendar(value).format("yyyy-MM-dd");
                            }
                        }, {
                            field: 'PRODUCTWIDTH',
                            width: 80,
                            title: '门幅'
                        }, {
                            field: 'PRODUCTROLLLENGTH',
                            width: 80,
                            title: '卷长'
                        }, {
                            field: 'PRODUCTROLLWEIGHT',
                            width: 80,
                            title: '卷重'
                        }, {
                            field: 'DRAWNO',
                            width: 80,
                            title: '图号'
                        }, {
                            field: 'ROLLNO',
                            width: 80,
                            title: '卷号'
                        }, {
                            field: 'LEVELNO',
                            width: 80,
                            title: '层号'
                        }, {
                            field: 'PRODUCTPROCESSCODE',
                            width: 120,
                            title: '工艺标准代码'
                        }, {
                            field: 'PRODUCTPROCESSBOMVERSION',
                            width: 120,
                            title: '工艺标准版本',
                            /* formatter:vFormatter,
                            styler:vStyler */
                        }, {
                            field: 'PRODUCTPACKAGINGCODE',
                            width: 120,
                            title: '包装标准代码',
                            formatter: packBomView
                        }, {
                            field: 'PRODUCTPACKAGEVERSION',
                            width: 120,
                            title: '包装标准版本'
                        }, {
                            field: 'PRODUCTMEMO',
                            title: '备注',
                            width: 50
                        }

                    ]],
                    detailFormatter: function (index2, row2) {
                        return '<div style="padding:2px"><table id="ddv-p-' + index + '-' + index2 + '"></table></div>';
                    },
                    onCollapseRow: function (index2, row2) {
                        setTimeout(function () {
                            $("#ddv-p-" + index).datagrid('fixDetailRowHeight', index2);
                            $("#ddv-p-" + index).datagrid('fixRowHeight', index2);
                            $("#dg").datagrid('fixDetailRowHeight', index);
                            $("#dg").datagrid('fixRowHeight', index);
                        }, 0);
                    },
                    onExpandRow: function (index2, row2) {
                        if (row2.PRODUCTISTC != 1) {
                            setTimeout(function () {
                                $("#ddv-p-" + index).datagrid('fixDetailRowHeight', index2);
                                $("#dg").datagrid('fixDetailRowHeight', index);
                            }, 0);
                            return;
                        }
                        $("#ddv-p-" + index + "-" + index2).datagrid({
                            url: path + "/bom/tc/ver/parts/" + row2.PROCBOMID + "/" + row2.PRODUCTCOUNT + "/" + row2.ID,
                            rownumbers: true,
                            width: '300',
                            fitColumns: true,
                            nowrap: false,
                            singleSelect: true,
                            loadMsg: '',
                            height: 'auto',
                            autoRowHeight: true,
                            rowStyler: danxiangbu,
                            columns: [[{
                                field: 'partName',
                                title: '部件名称',
                                width: 15
                            }, {
                                field: 'partCount',
                                title: '订单数量',
                                width: 15
                            }, {
                                field: 'partBomCount',
                                title: 'BOM数量',
                                width: 15
                            }]],
                            onResize: function () {
                                setTimeout(function () {
                                    $("#ddv-p-" + index).datagrid('fixDetailRowHeight', index2);
                                    $("#ddv-p-" + index).datagrid('fixRowHeight', index2);
                                    $("#dg").datagrid('fixDetailRowHeight', index);
                                    $("#dg").datagrid('fixRowHeight', index);
                                }, 0);
                            },
                            onLoadSuccess: function () {
                                setTimeout(function () {
                                    $("#ddv-p-" + index).datagrid('fixDetailRowHeight', index2);
                                    $("#ddv-p-" + index).datagrid('fixRowHeight', index2);
                                    $("#dg").datagrid('fixDetailRowHeight', index);
                                    $("#dg").datagrid('fixRowHeight', index);

                                }, 0);
                            }
                        });
                        $("#ddv-p-" + index).datagrid('fixDetailRowHeight', index2);
                        $("#dg").datagrid('fixDetailRowHeight', index);
                    },
                    onResize: function () {
                        $('#dg').datagrid('fixDetailRowHeight', index);
                    },
                    onLoadSuccess: function () {
                        Loading.hide();
                        setTimeout(function () {
                            $('#dg').datagrid('fixDetailRowHeight', index);
                        }, 0);
                    }
                });
                $('#dg').datagrid('fixDetailRowHeight', index);
            }
        });
    });

    const onDgLoadSuccess = function (data) {
        setTimeout(function () {
            Loading.hide();
        }, 100);
    };
    //复制已关闭的销售订单
    const copy = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能复制他人下达的订单");
            return;
        }
        if (r.AUDITSTATE !== 2) {
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
    };

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
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能编辑他人下达的订单");
            return;
        }
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
        });
    };

    const forceEdit = function () {
        action = "edit";
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能变更他人下达的订单");
            return;
        }
        if (r.AUDITSTATE !== 2) {
            Tip.warn("审核通过才能强制变更");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID + "&force=true", [EasyUI.window.button("icon-save", "保存", function () {
            const details = [];
            const rows = $("#product_dg").datagrid("getRows");
            for (let i = 0; i < rows.length; i++) {
                $("#product_dg").datagrid("endEdit", i);
                const d = {
                    "id": rows[i].id,
                    "deliveryTime": rows[i].deliveryTime.substring(0, 10),
                    "productCount": rows[i].productCount,
                    "productMemo": rows[i].productMemo
                };
                if (rows[i].productIsTc === 1) {
                    subgrid = $("#product_dg").datagrid('getRowDetail', i).find('table.ddv');
                    subgridRows = subgrid.datagrid("getRows");
                    tempCount = 0;
                    for (var j = 0; j < subgridRows.length; j++) {
                        subgrid.datagrid("beginEdit", j);
                        subgrid.datagrid("endEdit", j);
                        if (isEmpty(subgrid.datagrid("getRows")[j].partCount)) {
                            Tip.warn("部件订单数量不能为空!");
                            return;
                        }
                        tempCount += subgrid.datagrid("getRows")[j].partCount;
                    }
                    if (tempCount === 0) {
                        Tip.warn("无效的单个部件数量");
                        return;
                    }
                    d.partsCountList = subgrid.datagrid("getRows");
                }
                details.push(d);
            }
            Loading.show();
            $.ajax({
                url: path + "salesOrder/forceEdit",
                type: 'post',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(details),
                success: function (data) {
                    Loading.hide();
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Dialog.close(dialogId);
                    filter();
                }
            });

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
    let t = 0;

    function saveForm() {
        if ($("#salesOrderForm").form("validate")) {
            if ($("#product_dg").datagrid("getRows").length !== 0) {
                if (endEdit()) {
                    const order = JQ.getFormAsJson("salesOrderForm");
                    order.details = $("#product_dg").datagrid("getData").rows;
                    let subgrid;
                    let subgridRows;
                    let tempCount = 0;
                    for (let i = 0; i < order.details.length; i++) {
                        if (order.details[i].productIsTc !== 1) {
                            if ((order.details[i].productRollWeight == null && (order.details[i].productWidth == null || order.details[i].productRollLength == null))) {
                                Tip.warn("工艺信息不完整，请维护");
                                return;
                            }
                        }
                        if (order.details[i].productIsTc === 2) continue;
                        subgrid = $("#product_dg").datagrid('getRowDetail', i).find('table.ddv');
                        subgridRows = subgrid.datagrid("getRows");
                        tempCount = 0;
                        for (let j = 0; j < subgridRows.length; j++) {
                            subgrid.datagrid("beginEdit", j);
                            subgrid.datagrid("endEdit", j);
                            if (isEmpty(subgrid.datagrid("getRows")[j].partCount)) {
                                Tip.warn("部件订单数量不能为空!");
                                return;
                            }
                            tempCount += subgrid.datagrid("getRows")[j].partCount;
                        }
                        if (tempCount === 0) {
                            Tip.warn("无效的单个部件数量");
                            return;
                        }
                        order.details[i].partsCountList = subgrid.datagrid("getRows");
                    }
                    if (t === 0) {
                        /* $.messager.show({
                            title:'订单信息确认',
                            msg:'<font style="font-weight:bold;color:red;">请仔细核对订单信息,无误后请再次点击保存</font>',
                            showType:'slide',
                            style:{
                                right:'',
                                top:document.body.scrollTop+document.documentElement.scrollTop,
                                bottom:''
                            }
                        }); */
                        Tip.info('请仔细核对订单信息,无误后请再次点击保存', 5000);
                        t++;
                        return;
                    }
                    t = 0;
                    Dialog.confirm(function () {
                        Loading.show();
                        $.ajax({
                            url: path + "salesOrder/" + action,
                            type: 'post',
                            dataType: 'json',
                            contentType: 'application/json',
                            data: JSON.stringify(order),
                            success: function (data) {
                                Loading.hide();
                                if (Tip.hasError(data)) {
                                    return;
                                }
                                if (data === "保存成功") {
                                    Tip.success(data, 3000);
                                    Dialog.close(dialogId);
                                } else {
                                    Tip.error(data);
                                    return;
                                }
                                filter();
                            }
                        });
                    }, "请核对订单信息，确认保存?");
                }
            } else {
                Tip.warn("请选择订单产品！");
            }
        }
    }

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (rowIndex, field, value) {
        if (field === "ID" || field === "_expander") {
            return;
        }
        action = "edit";
        const userName = "${userName}";
        const rows = $('#dg').datagrid('getRows');
        const row = rows[rowIndex];
        if (row.USERNAME !== userName) {
            Tip.warn("不能编辑他人下达的订单");
            return;
        }
        if (row.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录不能编辑！");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
            Dialog.close(dialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
            $("#product_dg").datagrid("loadData", details);
        });
    };

    //删除销售订单
    const doDelete = function () {
        const r = EasyUI.grid.getSelections("dg");
        const userName = "${userName}";
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        let index = null;
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].USERNAME !== userName) {
                Tip.warn("不能删除他人下达的订单");
                return;
            }
            ids.push(r[i].ID);
            if (r[i].AUDITSTATE > 0) {
                const rs = $("#dg").datagrid('getRows');
                for (let a = 0; a < rs.length; a++) {
                    if (rs[a].ID === r[i].ID) {
                        index = a + 1;
                        Tip.warn("第" + index + "行为审核中或审核通过的记录，不能删除！");
                        return;
                    }
                }
            }
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    };

    function orderDateFormat(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function orderPlaceFormat(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function exportFormat(value, row, index) {
        if (value === 0) {
            return "外销";
        }
        if (value === 1) {
            return "内销";
        }
        if (value === -1) {
            return "胚布";
        }
        return value;
    }

    function orderTypeFormat(value, row, index) {
        //（3新品，2试样，1常规产品，-1未知）
        if (value === 3) {
            return "新品";
        } else if (value === 2 || value === 0) {
            return "试样";
        } else if (value === 1) {
            return "常规产品";
        } else if (value === -1) {
            return "未知";
        }
    }

    function rowStyler(index, row) {
        return row.SALESORDERISEXPORT === 0 ? "background:rgba(255, 0, 0, 0.23);" : "";
    }

    let selectProductWindowId;

    function selectProduct() {
        if ($("#salesOrderConsumerId").val() === "") {
            Tip.warn("请先选择客户信息");
            selectConsumer();
            return;
        }
        selectProductWindowId = Dialog.open("选择产品", 900, 400, path + "selector/product?singleSelect=false&showCode=false&isShow=1", [EasyUI.window.button("icon-ok", "选择", function () {
            const rows = $("#_common_product_dg").datagrid("getChecked");
            if (rows.length === 0) {
                Tip.warn("至少选择一个产品");
                return;
            }
            /* $('#product_dg').datagrid('loadData',[]); */
            for (let i = 0; i < rows.length; i++) {
                addToProductDg(rows[i]);
            }
            Dialog.close(selectProductWindowId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectProductWindowId);
        })], function () {
            Dialog.max(selectProductWindowId);
            //选试样产品,只列出试样产品
            const salesOrderType = $('#salesOrderType').combobox('getValue');
            if (salesOrderType === 2) {
                $("#isCommon").val(0);
            }
            const consumerId = $("#salesOrderConsumerId").val();
            $("#consumerId").val(consumerId);
            $("#auditState").val(2);//只能添加已通过审核的产品
            $("#" + selectProductWindowId + " .datagrid .datagrid-pager").hide();
            $("#_common_product_dg_Form").hide();
            _common_product_filter();
        }, function () {
            Dialog.close(selectProductWindowId);
        });
    }

    function _commons_product_dg_onBeforeLoad_callback() {
        return !Assert.isEmpty($("#consumerId").val());
    }

    function _common_product_dbClickRow(index, row) {
        addToProductDg(row);
        Dialog.close(selectProductWindowId);
    }

    function addToProductDg(r) {
        const _row = {
            "productId": r.ID,
            "productBatchCode": "",
            "salesOrderSubCode": "",
            "salesOrderSubCodePrint": "",
            "consumerProductName": r.CONSUMERPRODUCTNAME,
            "factoryProductName": r.FACTORYPRODUCTNAME,
            "productWidth": r.PRODUCTWIDTH,
            "productRollLength": r.PRODUCTROLLLENGTH,
            "productRollWeight": r.PRODUCTROLLWEIGHT,
            "productProcessCode": r.PRODUCTPROCESSCODE,
            "productProcessBomVersion": r.PRODUCTPROCESSBOMVERSION,
            "productPackagingCode": r.PRODUCTPACKAGINGCODE,
            "productPackageVersion": r.PRODUCTPROCESSBOMVERSION,
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
        //$("td[field='_expander'] span").hide();
        if (r.PRODUCTISTC === 1)
            $("#product_dg").datagrid("expandRow", $("#product_dg").datagrid("getRows").length - 1);
    }

    function contains(row) {
        const data = $("#product_dg").datagrid("getData");
        if (data.total === 0) {
            return false;
        } else {
            for (let i = 0; i < data.rows.length; i++) {
                if (data.rows[i]["productId"] === row["productId"]) {
                    return true
                }
            }
            return false;
        }
    }

    /* 	function _common_product_onLoadSuccess(data) {
     var data = $("#product_dg").datagrid("getData");
     for (var i = 0; i < data.rows.length; i++) {
     $("#_common_product_dg").datagrid("selectRecord",
     data.rows[i]["productId"]);
     }
     } */

    let bomVersion, bcVersion;

    /* ////1:是套材  2:非套材
     function beforeEdit(index,row){
     Loading.show("加载工艺BOM版本信息");
     $.ajax({
     url:path+"bom/"+(row.productIsTc==1?"":"f")+"tc/"+row.productProcessCode,
     type:"get",
     async:false,
     dataType:"json",
     success:function(data){
     bomVersion=data;
     }
     });
     Loading.hide();
     Loading.show("加载包材BOM版本信息");
     $.ajax({
     url:path+"bom/packaging/"+row.productPackagingCode,
     type:"get",
     async:false,
     dataType:"json",
     success:function(data){
     bcVersion=data;
     }
     });
     Loading.hide();
     }
     */

    function onBeforeLoad(param) {
        const row = EasyUI.grid.getRowByIndex("product_dg", editingIndex);
        param.code = row.productProcessCode;
        param.bomType = (row.productIsTc === 1 ? "tc" : "ftc");
    }

    function onBeforeLoad_bc(param) {
        var row = EasyUI.grid.getRowByIndex("product_dg", editingIndex);
        param.code = row.productPackagingCode;
        param.bomType = 'bc';
    }

    let valid = false;

    function endEdit() {
        const rows = $("#product_dg").datagrid("getRows");
        for (var i = 0; i < rows.length; i++) {
            editingIndex = i;
            valid = true;
            $("#product_dg").datagrid("beginEdit", i);
            if (!$("#product_dg").datagrid("validateRow", i)) {
                return false;
            } else {
                $("#product_dg").datagrid("endEdit", i);
            }
            $("#product_dg").datagrid("endEdit", i);
            $("#product_dg").datagrid("endEdit", j);
            /* for (var j = rows.length - 1; j > i; j--) {
                if (rows[i].salesOrderSubCode == rows[j].salesOrderSubCode && rows[i].productId == rows[j].productId) {
                    Tip.warn("相同的订单号:" + rows[i].salesOrderSubCode + "；产品:" + rows[i].productModel);
                    return false;
                }
            } */
        }
        editingIndex = -1;
        return true;
    }

    //选择业务员信息
    let selectUserWindowId;

    function selectUser() {
        selectUserWindowId = Dialog.open("选择用户", 900, 500, path + "selector/user?singleSelect=true", [EasyUI.window.button("icon-ok", "选择", function () {
            const row = $("#_common_user_dg").datagrid("getChecked");
            if (row.length === 0) {
                Tip.warn("至少选择一个用户");
                return;
            }
            $("#salesOrderBizUserName").searchbox("setValue", row[0].USERNAME);
            $("#salesOrderBizUserId").val(row[0].ID);
            Dialog.close(selectUserWindowId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectUserWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectUserWindowId)
        });
    }

    function _common_user_dbClickRow(index, row) {
        $("#salesOrderBizUserName").searchbox("setValue", row.USERNAME);
        $("#salesOrderBizUserId").val(row.ID);
        Dialog.close(selectUserWindowId);
    }

    function _common_user_onLoadSuccess(data) {
        const rows = $("#_common_user_dg").datagrid("getRows");
        for (let i = 0; i < rows.length; i++) {
            if (rows[i].ID === $("#salesOrderBizUserId").val()) {
                $("#_common_user_dg").datagrid("checkRow", i);
            }
        }
    }

    const consumerProducts = {};

    //选择客户信息
    let selectConsumerWindowId;

    function selectConsumer() {
        selectConsumerWindowId = Dialog.open("选择客户", 900, 500, path + "selector/consumer?singleSelect=true", [EasyUI.window.button("icon-ok", "选择", function () {
            let r;
            const row = $("#_common_consumer_dg").datagrid("getChecked");
            if (row.length === 0) {
                Tip.warn("至少选择一个客户");
                return;
            }

            if (row[0].ID != $("#salesOrderConsumerId").val() && $("#salesOrderConsumerId").val() !== "") {
                Dialog.confirm(function () {
                    //备份数据
                    consumerProducts[$("#salesOrderConsumerId").val()] = $("#product_dg").datagrid("getData").rows;
                    //切换数据
                    $("#product_dg").datagrid("loadData", consumerProducts[row[0].ID] ? consumerProducts[row[0].ID] : []);
                    $("#salesOrderConsumerName").searchbox("setValue", row[0].CONSUMERNAME);
                    $("#salesOrderConsumerId").val(row[0].ID);
                    $("#consumerCode").val(row[0].CONSUMERCODE);
                    Dialog.close(selectConsumerWindowId);
                    //国外客户
                    if (row[0].CONSUMERCATEGORY === 2) {
                        $("#salesOrderIsExport").combobox("select", 0);
                    } else {
                        $("#salesOrderIsExport").combobox("select", 1);
                    }
                }, "选择了不同的客户信息，会变更产品列表信息，是否继续？");
            } else {
                $("#salesOrderConsumerName").searchbox("setValue", row[0].CONSUMERNAME);
                $("#salesOrderConsumerId").val(row[0].ID);
                $("#consumerCode").val(row[0].CONSUMERCODE);
                Dialog.close(selectConsumerWindowId);
                //国外客户
                if (row[0].CONSUMERCATEGORY === 2) {
                    $("#salesOrderIsExport").combobox("select", 0);
                    r = {};
                    r.v = 0;
                    changeSerial(r);
                } else {
                    $("#salesOrderIsExport").combobox("select", 1);
                    r = {};
                    r.v = 1;
                    changeSerial(r);
                }
            }
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectConsumerWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectConsumerWindowId)
        });
    }

    function _common_consumer_dbClickRow(index, row) {
        if (row.ID !== $("#salesOrderConsumerId").val() && $("#salesOrderConsumerId").val() !== "") {
            Dialog.confirm(function () {
                //备份数据
                consumerProducts[$("#salesOrderConsumerId").val()] = $("#product_dg").datagrid("getData").rows;
                //切换数据
                $("#product_dg").datagrid("loadData", consumerProducts[row.ID] ? consumerProducts[row.ID] : []);
                $("#salesOrderConsumerName").searchbox("setValue", row.CONSUMERNAME);
                $("#salesOrderConsumerId").val(row.ID);
                $("#consumerCode").val(row.CONSUMERCODE);
                Dialog.close(selectConsumerWindowId);
                if (row.CONSUMERCATEGORY === 2) {
                    $("#salesOrderIsExport").combobox("select", 0);
                    row.v = 0;
                    changeSerial(row);
                } else {
                    $("#salesOrderIsExport").combobox("select", 1);
                    row.v = 1;
                    changeSerial(row);
                }
            }, "选择了不同的客户信息，会变更产品列表信息，是否继续？");
        } else {
            $("#salesOrderConsumerName").searchbox("setValue", row.CONSUMERNAME);
            $("#salesOrderConsumerId").val(row.ID);
            $("#consumerCode").val(row.CONSUMERCODE);
            Dialog.close(selectConsumerWindowId);
            if (row.CONSUMERCATEGORY === 2) {
                $("#salesOrderIsExport").combobox("select", 0);
                row.v = 0;
                changeSerial(row);
            } else {
                $("#salesOrderIsExport").combobox("select", 1);
                row.v = 1;
                changeSerial(row);
            }
        }
    }

    function _common_consumer_onLoadSuccess(data) {
        const rows = $("#_common_consumer_dg").datagrid("getRows");
        for (let i = 0; i < rows.length; i++) {
            if (rows[i].ID == $("#salesOrderConsumerId").val()) {
                $("#_common_consumer_dg").datagrid("checkRow", i);
            }
        }
    }

    function removeProduct() {
        const rows = $("#product_dg").datagrid("getSelections");
        for (let i = 0; i < rows.length; i++) {
            $("#product_dg").datagrid("deleteRow", $("#product_dg").datagrid("getRowIndex", rows[i]));
            delete indexData[$("#product_dg").datagrid("getRowIndex", rows[i])];
        }
    }

    //审核销售订单
    let flag = null;
    const doAudit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const userName = "${userName}";
        if (r.USERNAME !== userName) {
            Tip.warn("不能提审他人下达的订单");
            return;
        }
        if (r.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录，不能在提交审核！");
            return;
        }
        const wid = Dialog.open("审核", 600, 120, _auditCommitUrl + "?id=" + r.ID, [EasyUI.window.button("icon-ok", "提交审核", function () {
            if (flag !== r.ID) {
                flag = r.ID;
                EasyUI.form.submit("editAuditProduce", auditCommitUrl + "?userId=" + r.SALESORDERBIZUSERID, function (data) {
                    filter();
                    Dialog.close(wid);
                });
            } else {
                Dialog.close(wid);
            }
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            let yx;
            let index;
            if (r.SALESORDERMEMO != null) {
                index = r.SALESORDERMEMO.lastIndexOf("叶型为：");
                if (index > 0) {
                    yx = r.SALESORDERMEMO.substring(index + 4);
                }
            }
            $("#editAuditProduce #name").textbox("setValue", "销售订单审核，单号：" + r.SALESORDERCODE + (isEmpty(yx) ? "" : " ；叶型：" + yx));
        });
    };

    function formatterReviewState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    function closeOrder() {
        const r = EasyUI.grid.getSelections("dg");
        const userName = "${userName}";
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const index = null;
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].USERNAME !== userName) {
                Tip.warn("不能关闭他人下达的订单");
                return;
            }
            ids.push(r[i].ID);
            if (r[i].AUDITSTATE !== 2) {
                Tip.warn("审核通过的订单才可以关闭");
                return;
            }
        }
        $("#dd").dialog("open");
    }

    function cancelcloseOrder() {
        const r = EasyUI.grid.getSelections("dg");
        const userName = "${userName}";
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const index = null;
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].USERNAME !== userName) {
                Tip.warn("不能取消关闭他人下达的订单");
                return;
            }
            ids.push(r[i].ID);
            if (r[i].AUDITSTATE !== 2) {
                Tip.warn("审核通过的订单才可以关闭");
                return;
            }
        }
        $("#ddd").dialog("open");
    }

    function onOpen() {
        Loading.show();
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        $('#dgg').datagrid({
            url: path + 'salesOrder/product?orderId=' + r.ID,
            width: '100%',
            fitColumns: false,
            fit: true,
            singleSelect: false,
            rownumbers: true,
            loadMsg: '',
            height: 'auto',
            rowStyler: detailRowStyler,
            frozenColumns: [[{
                field: "ID",
                checkbox: true
            }, {
                field: 'SALESORDERSUBCODEPRINT',
                title: '客户订单号'
            }, {
                field: 'SALESORDERSUBCODE',
                title: '订单号'
            }]],
            columns: [[{
                field: 'CONSUMERPRODUCTNAME',
                width: "120px",
                title: '客户产品名称'
            }, {
                field: 'FACTORYPRODUCTNAME',
                width: 120,
                title: '厂内名称'
            }, {
                field: 'PRODUCTMODEL',
                width: 120,
                title: '产品型号'
            }, {
                field: 'PRODUCTCOUNT',
                width: 120,
                title: '数量',
                styler: processStyler,
                formatter: countFormatter
            }, {
                field: 'ALLOCATECOUNT',
                width: 120,
                title: '已分配数量',
                styler: processStyler,
                formatter: countFormatter
            }, {
                field: 'X',
                width: 100,
                title: '未完成数量',
                styler: function (value, index, row) {
                    return "background:rgb(221, 0, 35);color:white;"
                },
                formatter: unCompleteCount
            }, {
                field: 'PRODUCEDROLLS',
                title: '生产卷数',
                width: 80,
                styler: processStyler,
                formatter: rollFormatter
            }, {
                field: 'PRODUCEDTRAYS',
                title: '打包托数',
                width: 80,
                styler: processStyler,
                formatter: trayFormatter
            }, {
                field: 'PRODUCECOUNT',
                title: '生产进度',
                width: 80,
                styler: processStyler,
                formatter: processFormatter
            }, {
                field: 'DELIVERYTIME',
                title: '发货时间',
                formatter: function (value, row, index) {
                    if (value == undefined)
                        return null;
                    return new Calendar(value).format("yyyy-MM-dd");
                }
            }, {
                field: 'PRODUCTWIDTH',
                width: 80,
                title: '门幅'
            }, {
                field: 'PRODUCTROLLLENGTH',
                width: 80,
                title: '卷长'
            }, {
                field: 'PRODUCTROLLWEIGHT',
                width: 80,
                title: '卷重'
            }, {
                field: 'PRODUCTPROCESSCODE',
                width: 120,
                title: '工艺代码'
            }, {
                field: 'PRODUCTPROCESSBOMVERSION',
                width: 120,
                title: '工艺版本'
            }, {
                field: 'PRODUCTPACKAGINGCODE',
                width: 120,
                title: '包装代码'
            }, {
                field: 'PRODUCTPACKAGEVERSION',
                width: 120,
                title: '包装版本'
            }, {
                field: 'PRODUCTROLLCODE',
                width: 120,
                title: '卷标签代码'
            }, {
                field: 'PRODUCTBOXCODE',
                width: 120,
                title: '箱唛头代码'
            }, {
                field: 'PRODUCTTRAYCODE',
                width: 120,
                title: '托唛头代码'
            }, {
                field: 'PRODUCTMEMO',
                title: '备注',
                width: 50
            }

            ]],
            onLoadSuccess: function () {
                Loading.hide();
                $("#dd").dialog("maximize");
            }
        });
    }

    function onOpen1() {
        Loading.show();
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        $('#dggg').datagrid({
            url: path + 'salesOrder/product?orderId=' + r.ID,
            width: '100%',
            fitColumns: false,
            fit: true,
            singleSelect: false,
            rownumbers: true,
            loadMsg: '',
            height: 'auto',
            rowStyler: detailRowStyler,
            frozenColumns: [[{
                field: "ID",
                checkbox: true
            }, {
                field: 'SALESORDERSUBCODEPRINT',
                title: '客户订单号'
            }, {
                field: 'SALESORDERSUBCODE',
                title: '订单号'
            }]],
            columns: [[{
                field: 'CONSUMERPRODUCTNAME',
                width: "120px",
                title: '客户产品名称'
            }, {
                field: 'FACTORYPRODUCTNAME',
                width: 120,
                title: '厂内名称'
            }, {
                field: 'PRODUCTMODEL',
                width: 120,
                title: '产品型号'
            }, {
                field: 'PRODUCTCOUNT',
                width: 120,
                title: '数量',
                styler: processStyler,
                formatter: countFormatter
            }, {
                field: 'ALLOCATECOUNT',
                width: 120,
                title: '已分配数量',
                styler: processStyler,
                formatter: countFormatter
            }, {
                field: 'X',
                width: 100,
                title: '未完成数量',
                styler: function (value, index, row) {
                    return "background:rgb(221, 0, 35);color:white;"
                },
                formatter: unCompleteCount
            }, {
                field: 'PRODUCEDROLLS',
                title: '生产卷数',
                width: 80,
                styler: processStyler,
                formatter: rollFormatter
            }, {
                field: 'PRODUCEDTRAYS',
                title: '打包托数',
                width: 80,
                styler: processStyler,
                formatter: trayFormatter
            }, {
                field: 'PRODUCECOUNT',
                title: '生产进度',
                width: 80,
                styler: processStyler,
                formatter: processFormatter
            }, {
                field: 'DELIVERYTIME',
                title: '发货时间',
                formatter: function (value, row, index) {
                    if (value == undefined)
                        return null;
                    return new Calendar(value).format("yyyy-MM-dd");
                }
            }, {
                field: 'PRODUCTWIDTH',
                width: 80,
                title: '门幅'
            }, {
                field: 'PRODUCTROLLLENGTH',
                width: 80,
                title: '卷长'
            }, {
                field: 'PRODUCTROLLWEIGHT',
                width: 80,
                title: '卷重'
            }, {
                field: 'PRODUCTPROCESSCODE',
                width: 120,
                title: '工艺标准代码'
            }, {
                field: 'PRODUCTPROCESSBOMVERSION',
                width: 120,
                title: '工艺标准版本'
            }, {
                field: 'PRODUCTPACKAGINGCODE',
                width: 120,
                title: '包装标准代码'
            }, {
                field: 'PRODUCTPACKAGEVERSION',
                width: 120,
                title: '包装标准版本'
            }, {
                field: 'PRODUCTROLLCODE',
                width: 120,
                title: '卷标签代码'
            }, {
                field: 'PRODUCTBOXCODE',
                width: 120,
                title: '箱唛头代码'
            }, {
                field: 'PRODUCTTRAYCODE',
                width: 120,
                title: '托唛头代码'
            }, {
                field: 'PRODUCTMEMO',
                title: '备注',
                width: 50
            }

            ]],
            onLoadSuccess: function () {
                Loading.hide();
                $("#ddd").dialog("maximize");
            }
        });
    }
    const wclose = path + "salesOrder/wclose";

    function doCloseOrder() {
        const rows = EasyUI.grid.getSelections("dgg");
        if (rows.length === 0) {
            Tip.warn("请至少选择一条记录");
            return;
        }
        const type = "ORDER";
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        Dialog.confirm(function () {
            $.ajax({
                url: wclose + "?ids=" + ids.join(","),
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (data === 1) {
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
                                $("#dd").dialog("close");
                            }
                        });
                    }
                }
            });
        }, "确认关闭?");
    }

    //反关闭
    const cancelcloseUrl = path + "salesOrder/cancelclose";

    function doCancelCloseOrder() {
        const rows = EasyUI.grid.getSelections("dggg");
        if (rows.length === 0) {
            Tip.warn("请至少选择一条记录");
            return;
        }
        const type = "ORDER";
        const ids = [];
        for (let i = 0; i < rows.length; i++) {
            ids.push(rows[i].ID);
        }
        Dialog.confirm(function () {
            Loading.show();
            $.ajax({
                url: cancelcloseUrl + "?ids=" + ids.join(","),
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("关闭成功");
                    filter();
                    Loading.hide();
                    $("#ddd").dialog("close");
                }
            });
        }, "确认恢复？");
    }

    function detailRowStyler(index, row) {
        let style = "";
        if (isEmpty(row.CLOSED) || row.CLOSED === 0) {
        } else {
            style += "text-decoration:line-through;";
        }
        return style;
    }

    function stateFormatter(value, row, index) {
        if (value == null || value === 0) {
            return "<label style='background:green;width:100%;display: inline-block;color:white;text-align:center;'>正常</label>";
        }
        return "<label style='background:red;width:100%;display: inline-block;color:white;text-align:center;'>关闭</label>";
    }

    const checkTypeUrl = path + "salesOrder/checkDp";

    function view() {
        let salesType = "";
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r == null)
            return;
        if (r.SALESORDERISEXPORT === 0) {
            salesType = "XS2"; //国外
        } else if (r.SALESORDERISEXPORT === 1) {
            salesType = "XS1"; //国内
        } else {
            salesType = "PBOrder"; //胚布
        }
        const viewUrl = path + "audit/" + salesType + "/{id}/state";
        dialogId = Dialog.open("查看审核状态", dialogWidth, dialogHeight, viewUrl.replace("{id}", r.ID), [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            //$("#" + dialogId).dialog("maximize");
        });
    }

    function orderRowClick(index, row) {
        $("#salesOrderMemo").panel({
            content: "<pre>" + (isEmpty(row.SALESORDERMEMO) ? "无" : row.SALESORDERMEMO) + "</pre>"
        });
    }

    function exportExcel() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        window.open(exportUrl + "?id=" + r.ID);
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
            for (var a = 0; a < details.length; a++) {
                _common_bcBomDetail_data(details[a]);
            }
        });
    }
</script>