<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const findCutTcBomDetailByMainIdUrl = path + "bom/cutTcBom/findCutTcBomDetailByMainId";
    const listUrl = path + "bom/cutTcBom/list";
    const cutTcBomMainAddOrEditPageUrl = path + "bom/cutTcBom/cutTcBomMainAddOrEditPage";
    const saveCutTcBomMainUrl = path + "bom/cutTcBom/saveCutTcBomMain";
    const consumerUrl = path + "selector/consumer?singleSelect=false";
    const cancelUrl = path + "bom/cutTcBom/cancel";
    const effectUrl = path + "bom/cutTcBom/effect";
    const cutTcBomMainUploadUrl = path + "bom/cutTcBom/cutTcBomMainUpload";
    //提交导入文件内容
    const cutTcBomMainUploadFileUrl = path + "bom/cutTcBom/importcutTcBomMainUploadFile";
    //导出
    const exportcutTcBomMainUrl = path + "bom/cutTcBom/exportcutTcBomMain";
    const dialogWidth = 700, dialogHeight = 500;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "cutTcBomMainSearchForm");
    }

    function formatterAuditState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    $(function () {
        $('#dg').datagrid({
            url: listUrl,
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                        url: findCutTcBomDetailByMainIdUrl + "?mainId=" + row.ID,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        height: 'auto',
                        columns: [[
                            {
                                field: 'PARTNAME',
                                title: '部件名称',
                                width: 15,
                            }, {
                                field: 'DRAWNAME',
                                title: '图纸名称',
                                width: 15,
                            }, {
                                field: 'ORIENTATION',
                                title: '朝向',
                                width: 15,
                            }, {
                                field: 'PRODUCTMODEL',
                                title: '规格',
                                width: 15,
                            }
                            , {
                                field: 'PRODUCTWIDTH',
                                title: '门幅',
                                width: 15,
                            }, {
                                field: 'LENGTH',
                                title: '米长',
                                width: 15,
                            }, {
                                field: 'GRAMWEIGHT',
                                title: '克重',
                                width: 15,
                            }, {
                                field: 'PRODUCTIONRATE',
                                title: '制成率',
                                width: 15,
                            }
                            , {
                                field: 'UNITPRICE',
                                title: '单价',
                                width: 15,
                            }
                            , {
                                field: 'CREATETIME',
                                title: '创建时间',
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
                    }
                );
            }
            , rowStyler: function (index, row) {
                if (row.ONTHEWAYCOUNT > 0) {
                    return 'background-color:pink;color:blue;font-weight:bold;';
                }
            }
        });
        filter();
    });

    //右击添加工艺bom
    function importDetail() {
        dialogId = Dialog.open("导入", 550, 300, cutTcBomMainUploadUrl, [EasyUI.window.button("icon-save", "导入", function () {
            importcutTcBom();
            filter();
            Dialog.close(dialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {

        });
    }

    function importcutTcBom() {
        var cutTcBomUploadFile = $("#cutTcBomUploadFile");
        if (cutTcBomUploadFile.val().length <= 0) {
            alert("请选择文件");
            return false;
        }
        const filepath = cutTcBomUploadFile.val();
        const extStart = filepath.lastIndexOf(".");
        const ext = filepath.substring(extStart, filepath.length).toUpperCase();
        if (ext !== ".XLSX" && ext !== ".XLS" && ext !== ".XLSM") {
            alert("请上传excel格式文档");
            return false;
        }
        //获取到上传的文件信息
        // var data = cutTcBomUploadFile.files[0];
        const data = document.getElementById("cutTcBomUploadFile").files[0];
        const fromData = new FormData();
        if (data != null) {
            fromData.append("file", data);
            $.ajax({
                type: "post",
                url: cutTcBomMainUploadFileUrl,
                data: fromData,
                dataType: "json",
                contentType: false,
                processData: false,
                beforeSend: function () {
                    //dss.load(true);
                },
                complete: function () {
                    //dss.load(false);
                },
                success: function (data) {
                    alert("导入成功！");
                }
            });
        }
    }

    function exportDetail() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        window.open(exportcutTcBomMainUrl + "?id=" + r.ID);
    }

    //新增
    const add = function () {
        dialogId = Dialog.open("新增", dialogWidth, dialogHeight, cutTcBomMainAddOrEditPageUrl + "?id=", [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
            filter();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };

    //修改
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.STATE === 2) {
            Tip.warn("已作废裁剪套材不能修改");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, cutTcBomMainAddOrEditPageUrl
            + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
            filter();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };

    function saveForm() {
        const cutTcBomMain = JQ.getFormAsJson("cutTcBomMainForm");
        const listCutTcBomDetail = [];
        const cutTcBomDetails = $('#cutTcBomDetail_dg').datagrid('getData').rows;
        if (cutTcBomDetails.length === 0) {
            Tip.warn("请添加裁剪bom明细");
            return;
        }
        for (let i = 0; i < cutTcBomDetails.length; i++) {
            $("#cutTcBomDetail_dg").datagrid("endEdit", i);
            const r = {};
            r.id = cutTcBomDetails[i].ID;
            r.partName = cutTcBomDetails[i].PARTNAME;
            r.drawName = cutTcBomDetails[i].DRAWNAME;
            r.orientation = cutTcBomDetails[i].ORIENTATION;
            r.productModel = cutTcBomDetails[i].PRODUCTMODEL;
            r.length = cutTcBomDetails[i].LENGTH;
            r.gramWeight = cutTcBomDetails[i].GRAMWEIGHT;
            r.productionRate = cutTcBomDetails[i].PRODUCTIONRATE;
            r.unitPrice = cutTcBomDetails[i].UNITPRICE;
            r.upperSizeLimit = cutTcBomDetails[i].UPPERSIZELIMIT;
            r.lowerSizeLimit = cutTcBomDetails[i].LOWERSIZELIMIT;
            r.sizePercentage = cutTcBomDetails[i].SIZEPERCENTAGE;
            r.sizeAbsoluteValue = cutTcBomDetails[i].SIZEABSOLUTEVALUE;
            listCutTcBomDetail.push(r);
        }
        cutTcBomMain.listCutTcBomDetail = listCutTcBomDetail;
        Loading.show('保存中');
        $.ajax({
            url: saveCutTcBomMainUrl,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(cutTcBomMain),
            success: function (data) {
                filter();
                Loading.hide();
                Dialog.close(dialogId);
            },
            error: function (data) {
                Loading.hide();
            }
        });
    }

    //选择客户信息
    let selectConsumerWindowId;

    function selectConsumer() {
        selectConsumerWindowId = Dialog.open("选择客户", 900, 500, consumerUrl, [EasyUI.window.button("icon-ok", "选择", function () {
            const row = $("#_common_consumer_dg").datagrid("getChecked");
            if (row.length === 0) {
                Tip.warn("至少选择一个客户");
                return;
            }
            $("#customerName").searchbox("setValue", row[0].CONSUMERNAME);
            $("#customerCode").val(row[0].CONSUMERCODE);
            Dialog.close(selectConsumerWindowId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectConsumerWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectConsumerWindowId);
        });
    }

    function addDetail() {
        const _row = {
            "ID": "",
            "PARTNAME": "",
            "DRAWNAME": "",
            "ORIENTATION": "",
            "PRODUCTMODEL": "",
            "LENGTH": 0,
            "GRAMWEIGHT": 0,
            "PRODUCTIONRATE": 0,
            "UNITPRICE": 0,
            "UPPERSIZELIMIT": 0,
            "LOWERSIZELIMIT": 0,
            "SIZEPERCENTAGE": 0,
            "SIZEABSOLUTEVALUE": 0
        };
        $("#cutTcBomDetail_dg").datagrid("appendRow", _row);
        $("#cutTcBomDetail_dg").datagrid("beginEdit", EasyUI.grid.getRowIndex("cutTcBomDetail_dg", _row));
    }

    function deleteDetail() {
        // 获取选中行的Index的值
        const rowIndex = $('#cutTcBomDetail_dg').datagrid('getRowIndex', $('#cutTcBomDetail_dg').datagrid('getSelected'));
        $("#cutTcBomDetail_dg").datagrid("deleteRow", rowIndex);
    }

    function onClickRow(index, row) {
        $("#cutTcBomDetail_dg").datagrid("beginEdit", index);
    }

    //作废
    const cancel = function () {
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
            JQ.ajax(cancelUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    };

    //生效
    const effect = function () {
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
            JQ.ajax(effectUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    };
</script>
