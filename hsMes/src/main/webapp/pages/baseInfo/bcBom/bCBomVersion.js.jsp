<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:包材bom版本信息JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加包材bom版本信息
    const addUrl = path + "bCBomVersion/add";
    //编辑包材bom版本信息
    const editUrl = path + "bCBomVersion/edit";
    //删除包材bom版本信息
    const deleteUrl = path + "bCBomVersion/delete";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "bCBomVersionSearchForm");
    }

    //添加包材bom版本信息
    const add = function () {
        const wid = Dialog.open("添加", 380, 250, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("bCBomVersionForm", addUrl, function (data) {
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
            }
        );
    };

    //编辑包材bom版本信息
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", 380, 250, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("bCBomVersionForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })]);
    };
    //删除包材bom版本信息
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