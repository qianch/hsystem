<!--
作者:高飞
日期:2016-8-19 9:34:14
页面:调度实例JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加调度实例
    const addUrl = path + "scheduleInstance/add";
    //编辑调度实例
    const editUrl = path + "scheduleInstance/edit";
    //删除调度实例
    const deleteUrl = path + "scheduleInstance/delete";
    //构建CRON表达式页面
    const cronUrl = path + "scheduleInstance/cron";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "scheduleInstanceSearchForm");
    }

    //添加调度实例
    function add() {
        const wid = Dialog.open("添加", 650, 240, addUrl, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("scheduleInstanceForm", addUrl, function (data) {
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

    //编辑调度实例
    function edit() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.STATUS == "RUN") {
            Tip.warn("实例正在运行，请先停止后在编辑!");
            return;
        }
        if (r.EDITABLE == 1) {
            Tip.warn("该实例系自动生成，不可编辑");
            return;
        }
        const wid = Dialog.open("编辑", 650, 240, editUrl + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("scheduleInstanceForm", editUrl, function (data) {
                filter();
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })]);
    }

    //删除调度实例
    function doDelete() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }

        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].EDITABLE == 1) {
                let index = EasyUI.grid.getRowIndex("dg", r[i]);
                Tip.warn("第[" + (index + 1) + "]行实例系自动生成，不可删除");
                return;
            }
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

    function start() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        if (r.length > 1) {
            Tip.warn("只能同时启动一个实例");
            return;
        }
        if (r[0].STATUS == 'RUN') {
            Tip.warn("实例已在运行中");
            return;
        }
        if (r[0].EDITABLE == 1) {
            Tip.warn("该实例系自动生成，不可手动启动");
            return;
        }
        Dialog.confirm(function () {
            JQ.ajaxPost(path + "/scheduleInstance/start", {
                id: r[0].ID
            }, function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                filter();
                Tip.success("启动成功");
            }, function () {
                Tip.error("启动失败");
            });
        }, "确定启动?");

    }

    function stop() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        if (r.length > 1) {
            Tip.warn("只能同时启动一个实例");
            return;
        }
        if (r[0].STATUS == 'STOP') {
            Tip.warn("实例已经停止");
            return;
        }
        if (r[0].EDITABLE == 1) {
            Tip.warn("该实例系自动生成，不可手动停止");
            return;
        }
        Dialog.confirm(function () {
            JQ.ajaxPost(path + "/scheduleInstance/stop", {
                id: r[0].ID
            }, function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                filter();
                Tip.success("成功停止");
            }, function () {
                Tip.error("停止失败");
            });
        }, "确定停止?");

    }

    function statusFromatter(value, row, index) {
        if (value == "STOP") {
            return "<font color='red'>停止</font>";
        } else {
            return "<font color='green'>运行中</font>";
        }
    }

    function editableFormatter(value, row, index) {
        if (value == 1) {
            return "不可编辑";
        } else {
            return "可编辑";
        }
    }

    function buildCron() {
        const wid = Dialog.open("构建CRON", 800, 550, cronUrl, [EasyUI.window.button("icon-save", "确定", function () {
            $("#cron").textbox("setText", $("#cron_target").val());
            Dialog.close(wid);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })], function () {
        });
    }
</script>