<!--
作者:徐波
日期:2016-11-30 14:03:19
页面:btw文件JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>

    var btwFileUrl = path + "btwFile/list";

    var findBtwFilePrintUrl = path + "btwManager/BtwFilePrint/findBtwFilePrints";
    var selectorPrintTemplateUrl = path + "selector/selectorPrintTemplate?type=" + 2;
    var btwFilePrintAddOrEditUrl = path + "btwManager/BtwFilePrint/btwFilePrintAddOrEdit";

    var saveBtwFilePrintsUrl = path + "btwManager/BtwFilePrint/saveBtwFilePrints";

    var btwFileUploadUrl = path + "btwFile/btwFileUpload";

    var importbtwFileUploadUrl = path + "btwFile/importbtwFileUpload";

    //添加btw文件
    var addUrl = path + "btwFile/add";
    //编辑btw文件
    var editUrl = path + "btwFile/edit";
    //删除btw文件
    var deleteUrl = path + "btwFile/delete";

    var dialogWidth = 700, dialogHeight = 500;


    $(function () {
        $('#dg').datagrid({
            url: btwFileUrl,
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');

                ddv.datagrid({
                        url: findBtwFilePrintUrl + "?btwFileId=" + row.ID,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        height: 'auto',
                        columns: [[
                            {
                                field: 'PRINTATTRIBUTE',
                                title: '打印属性',
                                width: 15,
                            }, {
                                field: 'PRINTATTRIBUTENAME',
                                title: '打印属性名称',
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
                        , rowStyler: function (index, row) {
                            if (row.length == 0) {
                                return 'background-color:yellow;color:blue;font-weight:bold;';
                            }
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


    //查询
    function filter() {
        EasyUI.grid.search("dg", "btwFileSearchForm");
    }


    function addbtwFile() {
        dialogId = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("btwFileForm", addUrl, function (data) {
                        Tip.warn("保存成功");
                        filter();
                        Dialog.close(dialogId);
                    })
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(dialogId);
                })], function () {
                Dialog.more(dialogId);
            }
        );
    }


    /**
     * 双击行，弹出编辑
     */
    function dbClickEdit(index, row) {
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("btwFileForm", editUrl, function (data) {
                    Tip.warn(data);
                    Dialog.close(dialogId);
                    filter();
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId)
            })]);
    }


    //删除btw文件
    function doDelete() {
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
            JQ.ajax(deleteUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    //打印属性维护
    function editBtwFilePrint() {
        action = "edit";
        var r = EasyUI.grid.getOnlyOneSelected("dg");

        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, btwFilePrintAddOrEditUrl + "?btwFileId=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {

            $("#" + dialogId).dialog("maximize");
        });
    }


    function saveForm() {

        var btwFile = JQ.getFormAsJson("btwFilePrintForm");

        var rows = $("#dg_BtwFilePrint").datagrid('getRows');//获得所有行
        var listBtwFilePrint = [];

        for (var i = 0; i < rows.length; i++) {
            $("#dg_BtwFilePrint").datagrid("endEdit", i);
            var r = {};
            r.btwFileId = btwFile.id;
            r.printAttribute = rows[i].PRINTATTRIBUTE;
            r.printAttributeName = rows[i].PRINTATTRIBUTENAME;
            listBtwFilePrint.push(r);
        }

        btwFile.listBtwFilePrint = listBtwFilePrint;

        Dialog.confirm(function () {
            $.ajax({
                url: saveBtwFilePrintsUrl,
                type: 'post',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(btwFile),
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


    var selectPrintTemplateId;

    function selectPrintTemplate() {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        var ids = r[0].ID;
        selectPrintTemplateId = Dialog.open("选择打印模版", 900, 600, selectorPrintTemplateUrl, [EasyUI.window.button("icon-ok", "选择", function () {

            var rows = $("#SelectorPrintTemplate_dg").datagrid("getChecked");

            var message = "";
            for (var i = 0; i < rows.length; i++) {

                addToPmovePrintTemplate_dg(rows[i]);
            }

            if (message != "") {
                Tip.warn(message);
            }
            Dialog.close(selectPrintTemplateId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectPrintTemplateId);
        })], function () {
            filterSelectorPrintTemplate_dg();
        }, function () {
            Dialog.close(selectPrintTemplateId)
        });
    }

    function addToPmovePrintTemplate_dg(r) {
        var rows = $("#dg_BtwFilePrint").datagrid('getRows');//获得所有行
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
        $("#dg_BtwFilePrint").datagrid("appendRow", _row);
    }

    function removePrintTemplate() {
        // 获取选中行的Index的值
        var rowIndex = $('#dg_BtwFilePrint').datagrid('getRowIndex', $('#dg_BtwFilePrint').datagrid('getSelected'));

        $("#dg_BtwFilePrint").datagrid("deleteRow", rowIndex);

    }

    function onClickBtwFilePrintAddOrEditRow(index, row) {
        $("#dg_BtwFilePrint").datagrid("beginEdit", index);
    }

    var selectConsumerWindowId;

    function selectConsumer() {
        selectConsumerWindowId = Dialog.open("选择客户", 900, 500, path + "selector/consumer?singleSelect=false", [EasyUI.window.button("icon-ok", "选择", function () {
            var row = $("#_common_consumer_dg").datagrid("getChecked");
            if (row.length == 0) {
                Tip.warn("至少选择一个客户");
                return;
            }

            $("#consumerName").searchbox("setValue", row[0].CONSUMERNAME);
            $("#consumerId").val(row[0].ID);
            $("#consumerCode").val(row[0].CONSUMERCODE);

            Dialog.close(selectConsumerWindowId);

        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectConsumerWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectConsumerWindowId);
        });
    }

    //右击添加工艺bom
    function importbtwFile() {
        var r = EasyUI.grid.getSelections("dg");
        if (r.length != 1) {
            Tip.warn("请选择一条记录");
            return;
        }

        dialogId = Dialog.open("导入", 500, 150, btwFileUploadUrl + "?id=" + r[0].ID, [EasyUI.window.button("icon-save", "导入", function () {
            importbtwFileUpload();

            Dialog.close(dialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {

        });
    }

    function importbtwFileUpload() {
        var btwFileUploadFile = $("#btwFileUploadFile");
        if (btwFileUploadFile.val().length <= 0) {
            Tip.warn("请选择文件");
            return false;
        }
        var filepath = btwFileUploadFile.val();
        var extStart = filepath.lastIndexOf(".");
        var ext = filepath.substring(extStart, filepath.length).toUpperCase();
        if (ext != ".BTW") {
            Tip.warn("请上传btw格式文档");
            return false;
        }
        //获取到上传的文件信息
        var data = document.getElementById("btwFileUploadFile").files[0];

        var fromData = new FormData();

        if (data != null) {
            fromData.append("file", data);
            fromData.append("btwFileId", $('#btwFileId').val());
            $.ajax({
                type: "post",
                url: importbtwFileUploadUrl,
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
                    filter();
                    Tip.success("导入成功!");
                }
            });
        }
    }

</script>
