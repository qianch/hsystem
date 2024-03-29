<!--
作者:肖文彬
日期:2016-11-16 11:15:02
页面:原料库存状态表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //编辑原料库存状态表
    const editUrl = path + "stock/materialStockState/edit";
    //删除原料库存状态表
    const deleteUrl = path + "stock/materialStockState/delete";
    const addUrl = path + "stock/materialStock/add";
    const dialogWidth = 700, dialogHeight = 350;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "materialStockStateSearchForm");
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

    function formatterStockState(value, row) {
        if (value === 1) {
            return "入库";
        } else {
            return "出库";
        }
    }

    function rowStyler(index, row) {
        let style = "";
        if (row.STATE === 0) {
            style += "background: #8edd9b;";
        }
        if (row.STATE === 2) {
            style += "background: #FF6347;";
        }
        return style;
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
        if (value === 5) {
            return "退货";
        }
    }


    //编辑原料库存状态表
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialStockStateForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialStockStateForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除原料库存状态表
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

    function formatterIslock(value, row, index) {
        console.log(value);
        if (value === 1) {
            return "冻结";
        } else {
            return "正常";
        }
    }

    function formatterIspass(value, row, index) {
        if (value === 1) {
            return "是";
        } else {
            return "否";
        }
    }

    const lockUrl = path + "stock/materialStockState/lock";
    const unlockUrl = path + "stock/materialStockState/unlock";

    function lock() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].ISLOCKED === 1) {
                Tip.warn("有些原料已经被冻结，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(lockUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    function unlock() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            console.log(r[i]);
            if (r[i].ISLOCKED === -1 || r[i].ISLOCKED === 0
                || r[i].ISLOCKED === undefined) {
                Tip.warn("有些原料为正常状态，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }

        Dialog.confirm(function () {
            JQ.ajax(unlockUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    const passUrl = path + "stock/materialStockState/pass";
    const unpassUrl = path + "stock/materialStockState/unpass";

    function ispass() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].ISPASS === 1) {
                Tip.warn("有些原料已经被放行，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(passUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    function unpass() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            console.log(r[i]);
            if (r[i].ISPASS === -1 || r[i].ISPASS === 0
                || r[i].ISPASS === undefined) {
                Tip.warn("有些原料不是放行状态，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(unpassUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }
</script>