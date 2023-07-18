<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:包材bomJS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加包材bom
    const addUrl = path + "bcBom/add";
    //编辑包材bom
    const editUrl = path + "bcBom/edit";
    //删除包材bom
    const deleteUrl = path + "bcBom/delete";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "bcBomSearchForm");
    }

    //添加包材bom
    const add = function () {
        const wid = Dialog.open("添加", 500, 300, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("bcBomForm", addUrl, function (data) {
                        filter();
                        if (Dialog.isMore(wid)) {
                            Dialog.close(wid);
                            add();
                        } else {
                            Dialog.close(wid);
                        }
                    });
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid);
                })], function () {
                Dialog.more(wid);
                $("#" + wid).dialog("maximize");
            }, function () {
                Dialog.more(wid);
            }
        );
    };

    function formatterU(value, row, index) {
        if (value === -1) {
            return "未启用";
        } else {
            return "启用";
        }
    }

    function formatterDefult(value, row, index) {
        if (value === -1) {
            return "非默认";
        } else {
            return "默认";
        }
    }

    //编辑包材bom
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", 380, 250, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("bcBomForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            Dialog.more(wid);
            $("#" + wid).dialog("maximize");
        });
    };

    //删除包材bom
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
</script>