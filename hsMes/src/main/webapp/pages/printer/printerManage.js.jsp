<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加打印机信息
    const addUrl = path + "printerManage/add";
    //编辑打印机信息
    const editUrl = path + "printerManage/edit";
    //删除打印机信息
    const deleteUrl = path + "printerManage/delete";
    const dialogWidth = 700, dialogHeight = 360;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "printerManageSearchForm");
    }

    //添加打印机信息
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("printerManageForm", addUrl, function (data) {
                    filter();
                    if (Dialog.isMore(wid)) {
                        Dialog.close(wid);
                        add();
                    } else {
                        Dialog.close(wid);
                    }
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })], function () {
            Dialog.more(wid);
        });
    };

    //编辑打印机信息
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("printerManageForm", editUrl, function (data) {
                filter();
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })]);
    };

    //双击行，弹出编辑
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("printerManageForm", editUrl, function (data) {
                filter();
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })]);
    };

    //删除打印机信息
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

    //隐藏IP地址
    function formatter(value, row) {
        value = Math.random() * 10000;
        return value.toFixed(0);
    }
</script>