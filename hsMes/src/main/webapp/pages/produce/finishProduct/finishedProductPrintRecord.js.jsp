<!--
作者:宋黎明
日期:2016-9-30 10:49:34
页面:成品信息JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>

    var productPrintRecordAddOrEditUrl = path + "finishProduct/finishedProductPrintRecord/finishedProductPrintRecordAddOrEdit";
    var saveFinishedProductPrintRecordsUrl = path + "finishProduct/finishedProductPrintRecord/saveFinishedProductPrintRecords";
    var selectorproductPrintRecordUrl = path + "selector/selectorPrintTemplate?type=" + 1;

    //打印属性维护
    function editfinishedProductPrintRecord() {
        action = "edit";
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");

        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, productPrintRecordAddOrEditUrl + "?productId=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveFormFinishedProductPrintRecord();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {

            $("#" + dialogId).dialog("maximize");

        });
    }

    function saveFormFinishedProductPrintRecord() {

        var finishProduct = JQ.getFormAsJson("finishedProductPrintRecordForm");

        var rows = $("#dg_finishedProductPrintRecord").datagrid('getRows');//获得所有行
        var listFinishedProductPrintRecord = [];

        for (var i = 0; i < rows.length; i++) {
            $("#dg_finishedProductPrintRecord").datagrid("endEdit", i);
            var r = {};
            r.productId = finishProduct.id;
            r.printAttribute = rows[i].PRINTATTRIBUTE;
            r.printAttributeName = rows[i].PRINTATTRIBUTENAME;
            r.printAttributeContent = rows[i].PRINTATTRIBUTECONTENT;
            listFinishedProductPrintRecord.push(r);
        }

        finishProduct.listFinishedProductPrintRecord = listFinishedProductPrintRecord;

        Dialog.confirm(function () {
            $.ajax({
                url: saveFinishedProductPrintRecordsUrl,
                type: 'post',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(finishProduct),
                success: function (data) {
                    Tip.success("保存", 3000);
                    Dialog.close(dialogId);

                },
                error: function (data) {
                    Loading.hide();
                }

            });

        }, "请核对打印信息，确认保存打印信息?");
    }

    var createFinishedProductPrintRecordId;

    function createFinishedProductPrintRecord() {
      
        createFinishedProductPrintRecordId = Dialog.open("选择打印模版", 900, 600, selectorproductPrintRecordUrl, [EasyUI.window.button("icon-ok", "选择", function () {

            var rows = $("#SelectorPrintTemplate_dg").datagrid("getChecked");

            var message = "";
            for (var i = 0; i < rows.length; i++) {

                addFinishedProductPrintRecord(rows[i]);
            }

            if (message != "") {
                Tip.warn(message);
            }
            Dialog.close(createFinishedProductPrintRecordId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(createFinishedProductPrintRecordId);
        })], function () {
            filterSelectorPrintTemplate_dg();
        }, function () {
            Dialog.close(createFinishedProductPrintRecordId)
        });
    }

    function addFinishedProductPrintRecord(r) {
        var rows = $("#dg_finishedProductPrintRecord").datagrid('getRows');//获得所有行
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].PRINTATTRIBUTE == r.PRINTATTRIBUTE) {
                Tip.warn(r.PRINTATTRIBUTE + "已经存在!");
                return;
            }
        }

        var _row = {
            "ID": r.ID,
            "PRINTATTRIBUTE": r.PRINTATTRIBUTE,
            "PRINTATTRIBUTENAME": r.PRINTATTRIBUTENAME
        };
        $("#dg_finishedProductPrintRecord").datagrid("appendRow", _row);
    }

    function removeFinishedProductPrintRecord() {
        // 获取选中行的Index的值
        var rowIndex = $('#dg_finishedProductPrintRecord').datagrid('getRowIndex', $('#dg_finishedProductPrintRecord').datagrid('getSelected'));

        $("#dg_finishedProductPrintRecord").datagrid("deleteRow", rowIndex);

    }

    function onClickFinishedProductPrintRecordRow(index, row) {
        $("#dg_finishedProductPrintRecord").datagrid("beginEdit", index);
    }


</script>
