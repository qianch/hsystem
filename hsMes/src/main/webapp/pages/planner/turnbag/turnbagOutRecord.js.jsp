<!--
作者:sunli
日期:2018-4-11 14:10:25
页面:翻包领出JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    $(function () {
        $("#dg").datagrid({
            url: "${path}/planner/tbp/turnbagOut/list",
            onBeforeLoad: dgOnBeforeLoad
        })
    });

    //查询
    function filter() {
        EasyUI.grid.search("dg", "turnBagOutRecordSearchForm");
    }

    //翻包领料导出
    function exportExcel() {
        location.href = encodeURI(path + "planner/tbp/turnBagOutExport?" + JQ.getFormAsString("turnBagOutRecordSearchForm"));

    }

    function formatterWeight(value, row, index) {
        if (value != null) {
            return value.toFixed(1);
        }

    }
</script>