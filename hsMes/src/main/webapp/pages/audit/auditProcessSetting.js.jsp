<!--
作者:高飞
日期:2016-10-24 14:51:44
页面:流程设置JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加流程设置
    const addUrl = path + "audit/setting/add";
    //编辑流程设置
    const editUrl = path + "audit/setting/edit";
    //删除流程设置
    const deleteUrl = path + "audit/setting/delete";
    const dialogWidth = 700, dialogHeight = 350;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "auditProcessSettingSearchForm");
    }

    //添加流程设置
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("auditProcessSettingForm", addUrl, function (data) {
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

    //编辑流程设置
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("auditProcessSettingForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })], function () {
            if ($("#auditLevel").textbox("getValue") === 1) {
                $("#auditSecondLevelUsers").searchbox("readonly");
            }
        });
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("auditProcessSettingForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })], function () {
            if ($("#auditLevel").textbox("getValue") === 1) {
                $("#auditSecondLevelUsers").searchbox("readonly");
            }
        });
    };

    //几级审核人员
    let level = 0;
    //选择业务员信息
    let selectUserWindowId;

    function selectLevelOneUser() {
        level = 1;
        selectUser();
    }

    function selectLevelTwoUser() {
        level = 2;
        selectUser();
    }

    function selectUser() {
        selectUserWindowId = Dialog.open("选择用户", 900, 500, path + "selector/user?singleSelect=false", [EasyUI.window.button("icon-ok", "选择", function () {
            var rows = $("#_common_user_dg").datagrid("getChecked");
            if (rows.length === 0) {
                Tip.warn("至少选择一个用户");
                return;
            }
            var uids = "";
            var unames = "";
            for (var i = 0; i < rows.length; i++) {
                uids += (i === 0 ? "" : ",") + rows[i].ID;
                unames += (i === 0 ? "" : ",") + rows[i].USERNAME;
            }
            if (level === 1) {
                $("#firstLevelUsers").val(uids);
                $("#auditFirstLevelUsers").searchbox("setValue", unames);
            } else {
                $("#secondLevelUsers").val(uids);
                $("#auditSecondLevelUsers").searchbox("setValue", unames);
            }
            Dialog.close(selectUserWindowId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectUserWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectUserWindowId)
        });
    }

    function _common_user_dbClickRow(index, row) {
        if (level === 1) {
            $("#firstLevelUsers").val(row.ID);
            $("#auditFirstLevelUsers").searchbox("setValue", row.USERNAME);
        } else {
            $("#secondLevelUsers").val(row.ID);
            $("#auditSecondLevelUsers").searchbox("setValue", row.USERNAME);
        }
        Dialog.close(selectUserWindowId);
    }

    function _common_user_onLoadSuccess(data) {
        const rows = $("#_common_user_dg").datagrid("getRows");
        let uids;
        if (level === 1) {
            uids = $("#firstLevelUsers").val().split(",");
        } else {
            uids = $("#secondLevelUsers").val().split(",");
        }
        for (var i = 0; i < uids.length; i++) {
            $("#_common_user_dg").datagrid("selectRecord", uids[i]);
        }
    }

    function levelFormatter(value, row, index) {
        if (value === 1) {
            return "<font color=red>一级审核</font>";
        } else {
            return "<font color=green>二级审核</font>";
        }
    }

    function userFormatter2(value, row, index) {
        if (row.AUDITLEVEL === 1) {
            return "无";
        } else {
            if (!value) {
                return "<font color=red>未设置</font>";
            }
            return value;
        }
    }

    function userFormatter1(value, row, index) {
        if (value === "" || value === undefined) {
            return "<font color=red>未设置</font>";
        } else {
            return value;
        }
    }
</script>