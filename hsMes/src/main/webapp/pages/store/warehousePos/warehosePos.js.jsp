<!--
作者:肖文彬
日期:2016-9-29 16:26:04
页面:库位管理JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加库位管理
    const addUrl = path + "warehosePos/add";
    //编辑库位管理
    const editUrl = path + "warehosePos/edit";
    //删除库位管理
    const deleteUrl = path + "warehosePos/delete";
    //作废库位管理
    const update = path + "warehosePos/updateS";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "warehosePosSearchForm");
    }

    //添加库位管理
    const add = function () {
        const wid = Dialog.open("添加", 380, 260, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("warehosePosForm", addUrl, function (data) {
                        filter();
                        if (Dialog.isMore(wid)) {
                            if (data === "保存成功") {
                                Tip.success(data);
                            }
                            Dialog.close(wid);
                            add();
                        } else {
                            if (data === "保存成功") {
                                Tip.success(data);
                            }
                            Dialog.close(wid);
                        }
                    })
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid)
                })], function () {
                Dialog.more(wid);
            }
        );
    };

    //编辑库位管理
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", 380, 260, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("warehosePosForm", editUrl, function (data) {
                    if (data === "保存成功") {
                        Tip.success(data);
                    }
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除库位管理
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

    //作废库位管理
    const old = function () {
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
            JQ.ajax(update, "post", {
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
        const wid = Dialog.open("编辑", 380, 260, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("warehosePosForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };
</script>