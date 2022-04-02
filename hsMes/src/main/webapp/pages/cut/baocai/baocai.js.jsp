<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    var baoCaiKuPackingUrl = path + "cut/baocai/findPackingDetailByPackingID";

    var listUrl = path + "cut/baocai/list";

    var baoCaiKuAddOrEditPageUrl = path + "cut/baocai/baoCaiKuAddOrEditPage";

    var saveCutTcBomMainUrl = path + "cut/baocai/saveCutTcBomMain";

    var consumerUrl = path + "selector/consumer?singleSelect=false"

    var cancelUrl = path + "cut/baocai/cancel";

    var effectUrl = path + "cut/baocai/effect";

    var cutTcBomMainUploadUrl = path + "cut/baocai/cutTcBomMainUpload";

    var dialogWidth = 400, dialogHeight = 300;

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
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');

                ddv.datagrid({
                        url: baoCaiKuPackingUrl + "?packingID=" + row.ID,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        height: 'auto',
                        columns: [[
                            {
                                field: 'PACKINGID',
                                title: '领料ID',
                                width: 5,
                            },
                            {
                                field: 'TYPE',
                                title: '类别',
                                width: 15,
                            }, {
                                field: 'SPECS',
                                title: '包装材料',
                                width: 15,
                            }, {
                                field: 'SIZE',
                                title: '尺寸',
                                width: 15,
                            }, {
                                field: 'PACKINGDATE',
                                title: '领料日期',
                                width: 15,
                            },{
                                field: 'PACKINGNUMBER',
                                title: '领料数量',
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


    //导入
    function importDetail() {
        dialogId = Dialog.open("导入", dialogWidth, dialogHeight, cutTcBomMainUploadUrl, [EasyUI.window.button("icon-save", "导入", function () {

            filter();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {

        });
    }

    function exportDetail() {

    }

    //新增
    var add = function() {
        var wid = Dialog.open("添加", dialogWidth, dialogHeight,baoCaiKuAddOrEditPageUrl, [
            EasyUI.window.button("icon-save", "保存", function() {
                EasyUI.form.submit("consumerForm",addUrl, function(data){
                    filter();
                    if(Dialog.isMore(wid)){
                        Dialog.close(wid);
                        add();
                    }else{
                        Dialog.close(wid);
                    }
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function() {
                Dialog.close(wid)
            }) ],function(){Dialog.more(wid);}
        );
    }

    //修改
    var edit = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dg");
        var dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, baoCaiKuAddOrEditPageUrl
            + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
            filter();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    }


    function saveForm() {


        var cutTcBomMain = JQ.getFormAsJson("cutTcBomMainForm");

        var listCutTcBomDetail = [];

        var cutTcBomDetails = $('#cutTcBomDetail_dg').datagrid('getData').rows;

        if (cutTcBomDetails.length == 0) {
            Tip.warn("请添加裁剪bom明细");
            return;
        }
        for (var i = 0; i < cutTcBomDetails.length; i++) {
            $("#cutTcBomDetail_dg").datagrid("endEdit", i);
            var r = {};
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

        console.log(cutTcBomMain.listCutTcBomDetail);

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
    var selectConsumerWindowId;

    function selectConsumer() {
        selectConsumerWindowId = Dialog.open("选择客户", 900, 500, consumerUrl, [EasyUI.window.button("icon-ok", "选择", function () {
            var row = $("#_common_consumer_dg").datagrid("getChecked");
            if (row.length == 0) {
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
        var _row = {
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
        var rowIndex = $('#cutTcBomDetail_dg').datagrid('getRowIndex', $('#cutTcBomDetail_dg').datagrid('getSelected'));

        $("#cutTcBomDetail_dg").datagrid("deleteRow", rowIndex);

    }

    function onClickRow(index, row) {
        $("#cutTcBomDetail_dg").datagrid("beginEdit", index);
    }

    //作废
    var cancel = function () {
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
            JQ.ajax(cancelUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }
    var packing = function () {
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
            JQ.ajax(cancelUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    //生效
    var effect = function () {
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
            JQ.ajax(effectUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

</script>
