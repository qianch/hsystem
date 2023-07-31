<!--
作者:高飞
日期:2019-4-15 18:44:42
页面:西门子裁剪车间机台打印任务JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加西门子裁剪车间机台打印任务
    const addUrl = path + "cutTaskOrderPrintTask/add";
    //编辑西门子裁剪车间机台打印任务
    const editUrl = path + "cutTaskOrderPrintTask/edit";
    //删除西门子裁剪车间机台打印任务
    const deleteUrl = path + "cutTaskOrderPrintTask/delete";
    const dialogWidth = 700, dialogHeight = 350;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "cutTaskOrderPrintTaskSearchForm");
    }

    //添加西门子裁剪车间机台打印任务
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("cutTaskOrderPrintTaskForm", addUrl, function (data) {
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

    //编辑西门子裁剪车间机台打印任务
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("cutTaskOrderPrintTaskForm", editUrl, function (data) {
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
                EasyUI.form.submit("cutTaskOrderPrintTaskForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除西门子裁剪车间机台打印任务
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