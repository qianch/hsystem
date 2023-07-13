<!--
作者:高飞
日期:2016-10-25 13:52:50
页面:流程实例JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //查询
    function filter() {
        EasyUI.grid.search("dg", "auditInstanceSearchForm");
    }

    function formatNode(value, row, index) {
        if (value === 1) {
            return "一级审核";
        }
        if (value === 2) {
            return "二级审核";
        }
    }

    function resultFormatter(value, row, index) {
        if (value === -1)
            return "<font color=red>不通过</font>";
        if (value === 2)
            return "<font color=green>通过</font>";
        return "通过";
    }

    function loadSuccess(data) {
        $("#dg").datagrid("autoMergeCells", ["AUDITTITLE"]);
    }

    const dbClickEdit = function (index, row) {
        dialogId = Dialog.open(row.AUDITTITLE, 400, 400, path + "audit/"
            + row.ID, [EasyUI.window.button("icon-cancel", "关闭",
            function () {
                Dialog.close(dialogId);
            })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    };
</script>