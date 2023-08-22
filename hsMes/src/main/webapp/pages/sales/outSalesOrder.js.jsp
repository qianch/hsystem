<!--
作者:谢辉
日期:2017-06-08 16:54:20
页面:销售出库明细表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //查询
    function filter() {
        EasyUI.grid.search("dg", "OutSaloderFrom");
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}/salesOrder/outlist",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

    function dataFormatter(value, row, index) {
        if (value == undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function export1() {
        //var order = JQ.getFormAsJson("OutSaloderFrom");
        location.href = encodeURI(path + "salesOrder/export1?" + JQ.getFormAsString("OutSaloderFrom"));
    }

    function weightFormatter(value, row, inde) {
        if (value == undefined)
            return null;
        return value.toFixed(2);
    }

    function unitFormatter(value, row, inde) {
        if (row.COUNT == undefined)
            return null;
        return "托";
    }

    function rowStyler(index, row) {
        return "";
    }
</script>