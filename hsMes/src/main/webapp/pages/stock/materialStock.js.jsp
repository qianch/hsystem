<!--
作者:徐波
日期:2016-10-24 15:08:19
页面:原料库存表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加原料库存表
    const addUrl = path + "stock/materialStock/add";
    //编辑原料库存表
    const editUrl = path + "stock/materialStock/edit";
    //删除原料库存表
    const deleteUrl = path + "stock/materialStock/delete";
    const dialogWidth = 700, dialogHeight = 350;

    function formatterStockState(value, row) {
        if (value === 1) {
            return "入库";
        } else {
            return "出库";
        }
    }

    function rowStyler(index, row) {
        let str = new Date(row.MATERIALSHELFLIFES);
        str = str.setTime(str.getTime() - 8000 * 60 * 60);
        let date1 = new Date();
        date1 = date1.setDate(date1.getDate() - 1);
        if (str < date1) {
            return 'background-color:red';
        }
    }

    function formatterState(value, row) {
        if (value === 0) {
            return "待检";
        }
        if (value === 1) {
            return "合格";
        }
        if (value === 2) {
            return "不合格";
        }
        if (value === 3) {
            return "冻结";
        }
        if (value === 4) {
            return "放行";
        }
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "materialStockSearchForm");
    }

    function sentenceLevel() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        const wid = Dialog.open("设置", 400, 200, addUrl, [
            EasyUI.window.button("icon-save", "保存", function () {
                var state = $("#state").combobox("getValue");
                JQ.ajax(addUrl, "post", {
                    ids: ids.toString(),
                    state: state
                }, function (data) {
                    filter();
                    Dialog.close(wid)
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    }

    //添加原料库存表
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialStockForm", addUrl, function (data) {
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
    }

    //编辑原料库存表
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID,
            [EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialStockForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    }

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID,
            [EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    }

    //删除原料库存表
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
    }
</script>