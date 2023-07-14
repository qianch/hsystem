<!--
作者:高飞
日期:2016-8-18 15:02:50
页面:任务调度模板JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加任务调度模板
    const addUrl = path + "scheduleTemplate/add";
    //编辑任务调度模板
    const editUrl = path + "scheduleTemplate/edit";
    //删除任务调度模板
    const deleteUrl = path + "scheduleTemplate/delete";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "scheduleTemplateSearchForm");
    }

    //添加任务调度模板
    function add() {
        const wid = Dialog.open("添加", 650, 250, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("scheduleTemplateForm", addUrl, function (data) {
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
    }

    //编辑任务调度模板
    function edit() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", 650, 250, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("scheduleTemplateForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    }

    //删除任务调度模板
    function doDelete() {
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

    function getTaskName() {
        if ($("#clazz").textbox("getValue") !== "") {
            $.ajax({
                url: path + "scheduleTemplate/check",
                type: "post",
                data: {clazz: $("#clazz").textbox("getValue")},
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    $("#templateName").textbox("setValue", data.name);
                }
            });
        }
    }
</script>