<!--
作者:肖文彬
日期:2016-9-29 15:45:32
页面:订单管理JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加订单管理
    const addUrl = path + "cutSalesOrder/add";
    //编辑订单管理
    const editUrl = path + "cutSalesOrder/edit";
    //删除订单管理
    const deleteUrl = path + "cutSalesOrder/doDeleteCutSalesOrder";
    const chooseproducePlanDetailUrl = path + "selector/producePlanDetail";
    const chooseCutTcBomMainUrl = path + "selector/cutTcBomMain";
    const savecutSalesOrderUrl = path + "cutSalesOrder/saveCutSalesOrder";
    const dialogWidth = 700, dialogHeight = 500;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "cutSalesOrderSearchForm");
    }

    let chooseproducePlanDetailWindowId;

    function chooseproducePlanDetail() {
        chooseproducePlanDetailWindowId = Dialog.open("选择生产批次号", 1200, 450, chooseproducePlanDetailUrl, [EasyUI.window.button("icon-save", "确认", function () {
            const r = EasyUI.grid.getOnlyOneSelected("_common_producePlanDetail_dg");
            $("#batchCode").searchbox("setValue", r.BATCHCODE);
            $("#producePlanDetailId").val(r.ID);
            $("#salesOrderCode").searchbox("setValue", r.SALESORDERCODE);
            Dialog.close(chooseproducePlanDetailWindowId);
        }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
            Dialog.close(chooseproducePlanDetailWindowId);
        })]);
    }

    let chooseCutTcBomMainWindowId;

    function chooseCutTcBomMain() {
        chooseCutTcBomMainWindowId = Dialog.open("选择裁剪bom", 1200, 450, chooseCutTcBomMainUrl, [EasyUI.window.button("icon-save", "确认", function () {
            const r = EasyUI.grid.getOnlyOneSelected("_common_cutTcBomMain_dg");
            if (r.STATE === 2) {
                Tip.warn("已作废裁剪套材不能挑选");
                return;
            }
            $('#bladeTypeName').searchbox('setValue', r.BLADETYPENAME);
            $("#tcBomMainId").val(r.ID);
            Dialog.close(chooseCutTcBomMainWindowId);
        }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
            Dialog.close(chooseCutTcBomMainWindowId);
        })]);
    }

    //添加订单管理
    const add = function () {
        dialogId = Dialog.open("新增", dialogWidth, dialogHeight, addUrl, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
            filter();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };

    //编辑订单管理
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl
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
        const cutSalesOrder = JQ.getFormAsJson("cutSalesOrderForm");
        Loading.show('保存中');
        $.ajax({
            url: savecutSalesOrderUrl,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(cutSalesOrder),
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

    //删除订单管理
    const doDelete = function () {
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
            JQ.ajax(deleteUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        dialogId = Dialog.open("编辑", 380, 160, editUrl + "?id=" + row.ID, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("cutSalesOrderForm", editUrl, function (data) {
                        filter();
                        Dialog.close(dialogId);
                    })
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(dialogId)
                })], function () {
                $("#" + dialogId).dialog("maximize");
                Dialog.more(dialogId);
            }
        );
    };
</script>
