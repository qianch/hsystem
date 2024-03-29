<!--
作者:徐波
日期:2017-2-11 8:53:07
页面:胚布移库表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加胚布移库表
    const addUrl = path + "stockFabricMove/add";
    //编辑胚布移库表
    const editUrl = path + "stockFabricMove/edit";
    //删除胚布移库表
    const deleteUrl = path + "stockFabricMove/delete";
    //排布Excel导出Url
    const exportUrl = path + "stockFabricMove/export";
    const dialogWidth = 700, dialogHeight = 350;
    //获取上月最后一天或第一天
    const nowdays = new Date();
    let year = nowdays.getFullYear();
    let month = nowdays.getMonth();
    if (month === 0) {
        month = 12;
        year = year - 1;
    }
    if (month < 10) {
        month = "0" + month;
    }
    const myDate = new Date(year, month, 0);
    $(document).ready(function () {
        const firstDay = year + "-" + month + "-" + "01";//上个月的第一天
        const lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
        const time2 = lastDay + " " + "12:00:00";
        $("#start").datetimebox("setValue", time2);
        setTimeout(function () {
            filter();
        }, 1500);
        $("#dg").datagrid({
            url: "${path}stockFabricMove/list",
            onBeforeLoad: dgOnBeforeLoad
        });
    });

    //查询
    function filter() {
        EasyUI.grid.search("dg", "stockFabricMoveSearchForm");
    }

    //添加胚布移库表
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("stockFabricMoveForm", addUrl, function (data) {
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
            }
        );
    };

    //编辑胚布移库表
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("stockFabricMoveForm", editUrl, function (data) {
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
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("stockFabricMoveForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除胚布移库表
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

    function warhouseName(value, row, index) {
        if (value === "bz1pbk") {
            return "编织一胚布库";
        }
        if (value === "bz2pbk") {
            return "编织二胚布库";
        }
        if (value === "bz3pbk") {
            return "编织三胚布库";
        }
        if (value === "cjpbk") {
            return "裁剪胚布库";
        }
        return value;
    }

    /**
     * 行统计
     */
    function onLoadSuccess(data) {
        appendRow();
    }

    /**
     * 表格末尾追加统计行
     */
    function appendRow() {
        $("#dg").datagrid('appendRow', {
            FACTORYPRODUCTNAME: '<span class="subtotal" style=" font-weight: bold;">小计</span>',
            ROLLWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("ROLLWEIGHT") + '</span>'//重量
        });
    }

    /**
     * 指定列求和
     */
    function compute(colName) {
        const rows = $("#dg").datagrid("getRows");
        let total = 0;
        for (let i = 0; i < rows.length; i++) {
            total += parseFloat(rows[i][colName]);
        }
        total = total.toFixed(2);
        return total;
    }

    /**
     * Excel 导出
     */
    function exportExcel() {
        location.href = encodeURI(exportUrl + "?" + JQ.getFormAsString("stockFabricMoveSearchForm"));
    }
</script>