<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const findProducePlanDetailPrintsUrl = path + "planner/producePlanDetail/findProducePlanDetailPrints";
    const selectorPrintBtwFilePrintUrl = path + "selector/selectorBtwFilePrint";
    const editProducePlanDetailPrintsUrl = path + "planner/producePlanDetail/editProducePlanDetailPrints";
    const addPlanPrintsUrl = path + "planner/producePlanDetail/saveProducePlanDetailPrints";
    const createProducePlanDetailPrintsUrl = path + "planner/producePlanDetail/createProducePlanDetailPrints";
    const dialogWidth = 700, dialogHeight = 500;

    $(function () {
        $('#producePlanDetaildg').datagrid({
            url: path + "planner/producePlanDetail/list",
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                        url: findProducePlanDetailPrintsUrl + "?ProducePlanDetailId=" + row.ID,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        height: 'auto',
                        columns: [[
                            {
                                field: 'TAGNAME',
                                title: '标签名称',
                                width: 15,
                            },
                            {
                                field: 'TAGTYPETEXT',
                                title: '标签类型',
                                width: 15,
                            },
                            {
                                field: 'PRINTATTRIBUTE',
                                title: '打印属性',
                                width: 15,
                            }, {
                                field: 'PRINTATTRIBUTENAME',
                                title: '打印属性名称',
                                width: 15,
                            }, {
                                field: 'PRINTATTRIBUTECONTENT',
                                title: '打印内容',
                                width: 15,
                            }
                        ]],
                        onResize: function () {
                            $('#producePlanDetaildg').datagrid('fixDetailRowHeight', index);
                        },
                        onLoadSuccess: function () {
                            Loading.hide();
                            setTimeout(function () {
                                $('#producePlanDetaildg').datagrid('fixDetailRowHeight', index);
                            }, 0);
                        }
                        , rowStyler: function (index, row) {
                            if (row.length === 0) {
                                return 'background-color:yellow;color:blue;font-weight:bold;';
                            }
                        }
                    }
                );
            }, rowStyler: function (index, row) {
                if (row.ONTHEWAYCOUNT > 0) {
                    return 'background-color:pink;color:blue;font-weight:bold;';
                }
            }
        });
        producePlanDetailfilter();
    });

    //查询
    function producePlanDetailfilter() {
        EasyUI.grid.search("producePlanDetaildg", "producePlanSearchForm");
    }

    //编辑销售订单
    const editProducePlanDetailPrints = function () {
        action = "edit";
        const r = EasyUI.grid.getOnlyOneSelected("producePlanDetaildg");
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editProducePlanDetailPrintsUrl + "?ProducePlanDetailId=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };

    function saveForm() {
        if ($('#btwFileId').val() === 0) {
            Tip.warn("请先选择标签模板");
            return;
        }
        const rows = $("#dg_PlanDetailPrint").datagrid('getRows');//获得所有行
        const planDetailPrintsData = [];
        const producePlanDetailId = $("#producePlanDetailId").val();
        for (let i = 0; i < rows.length; i++) {
            $("#dg_PlanDetailPrint").datagrid("endEdit", i);
            const r = {};
            r.producePlanDetailId = producePlanDetailId;
            r.btwFileId = $('#btwFileId').val();
            r.printAttribute = rows[i].PRINTATTRIBUTE;
            r.printAttributeName = rows[i].PRINTATTRIBUTENAME;
            r.printAttributeContent = rows[i].PRINTATTRIBUTECONTENT;
            planDetailPrintsData.push(r);
        }
        const params = {};
        params.producePlanDetailId = producePlanDetailId;
        params.btwFileId = $('#btwFileId').val();
        params.planDetailPrintsData = JSON.stringify(planDetailPrintsData);
        const postdata = $.param(params);
        Dialog.confirm(function () {
            Loading.show();
            $.post(addPlanPrintsUrl, postdata, function (data) {
                Loading.hide();
                if (Tip.hasError(data)) {
                    Tip.error("error1:" + data);
                    return;
                }
                if (data.length === 2) {
                    Tip.success("保存", 3000);
                    Dialog.close(dialogId);
                } else {
                    Tip.error("error2:" + data);
                    return;
                }
                filter();
            });
        }, "请核对打印信息，确认保存打印信息?");
    }

    let selectorPrintBtwFileId;

    function selectorPrintBtwFile() {
        if ($('#btwFileId').val() === 0) {
            Tip.warn("请先选择标签模板");
            return;
        }
        selectorPrintBtwFileId = Dialog.open("选择打印标签属性", 900, 500, selectorPrintBtwFilePrintUrl + "?btwFileId=" + $('#btwFileId').val(), [EasyUI.window.button("icon-ok", "选择", function () {
            const rows = $("#selectorBtwFilePrint_dg").datagrid("getChecked");
            const message = "";
            for (let i = 0; i < rows.length; i++) {
                addTodg_PlanDetailPrint(rows[i]);
            }
            if (message !== "") {
                Tip.warn(message);
            }
            Dialog.close(selectorPrintBtwFileId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectorPrintBtwFileId);
        })], function () {
        }, function () {
            Dialog.close(selectorPrintBtwFileId)
        });
    }

    function addTodg_PlanDetailPrint(r) {
        const rows = $("#dg_PlanDetailPrint").datagrid('getRows');//获得所有行
        for (let i = 0; i < rows.length; i++) {
            if (rows[i].PRINTATTRIBUTE === r.PRINTATTRIBUTE) {
                Tip.warn(r.PRINTATTRIBUTE + "已经存在!");
                return;
            }
        }
        const _row = {
            "ID": r.ID,
            "PRINTATTRIBUTE": r.PRINTATTRIBUTE,
            "PRINTATTRIBUTENAME": r.PRINTATTRIBUTENAME
        };
        $("#dg_PlanDetailPrint").datagrid("appendRow", _row);
    }

    function removePrintBtwFile() {
        // 获取选中行的Index的值
        const rowIndex = $('#dg_PlanDetailPrint').datagrid('getRowIndex', $('#dg_PlanDetailPrint').datagrid('getSelected'));
        $("#dg_PlanDetailPrint").datagrid("deleteRow", rowIndex);
    }

    function onClickRow(index, row) {
        $("#dg_PlanDetailPrint").datagrid("beginEdit", index);
    }

    function loadTagType() {
        $("#tagTypeSelect").combobox({
            url: path + "dict/queryDict?rootcode=TagType",
            valueField: 'v',
            textField: 't',
            onLoadSuccess: function (data) {
                if ($('#tagType').val() === '') {
                    $('#tagTypeSelect').combobox('select', data[0].v);
                    $('#tagType').val(data[0].v);
                } else {
                    $('#tagTypeSelect').combobox('select', $('#tagType').val());
                }
            },
            onSelect: function (rec) {
                $('#tagType').val(rec.v);
                loadbtwFileCmb();
            }
        })
    }

    let isPost = false;

    function loadbtwFileCmb() {
        $("#btwFileCmb").combobox({
            url: path + "btwFile/queryBtwFilebyCustomerId?customerId=" + $('#customerId').val() + "&type=" + $('#tagType').val(),
            valueField: 'v',
            textField: 't',
            onLoadSuccess: function (data) {
                if (data.length > 0) {
                    if ($('#btwFileId').val() === '') {
                        $('#btwFileCmb').combobox('select', data[0].v);
                        $('#btwFileId').val(data[0].v);
                    } else {
                        $('#btwFileCmb').combobox('select', $('#btwFileId').val());
                    }
                    createProducePlanDetailPrints($('#producePlanDetailId').val(), $('#btwFileId').val());
                } else {
                    $('#btwFileId').val(0);
                }
            },
            onSelect: function (rec) {
                $('#btwFileId').val(rec.v);
                if (isPost) {
                    createProducePlanDetailPrints($('#producePlanDetailId').val(), $('#btwFileId').val());
                }
                isPost = true;
            }
        })
    }

    function loadPlanDetailPrint() {
        $('#dg_PlanDetailPrint').datagrid({
            url: path + "planner/producePlanDetail/findPlanDetailPrintsBybtwFileId?ProducePlanDetailId=" + $('#producePlanDetailId').val() + '&btwFileId=' + $('#btwFileId').val(),
            onBeforeLoad: dgOnBeforeLoad, rowStyler: function (index, row) {
                if (row.ONTHEWAYCOUNT > 0) {
                    return 'background-color:pink;color:blue;font-weight:bold;';
                }
            }
        })
    }

    function createProducePlanDetailPrints(producePlanDetailId, btwFileId) {
        if (producePlanDetailId != null && btwFileId != null) {
            const params = {};
            params.producePlanDetailId = producePlanDetailId;
            params.btwFileId = btwFileId;
            const postdata = $.param(params);
            $.post(createProducePlanDetailPrintsUrl, postdata, function (data) {
                loadPlanDetailPrint();
            });
        } else {
            loadPlanDetailPrint();
        }
    }
</script>
