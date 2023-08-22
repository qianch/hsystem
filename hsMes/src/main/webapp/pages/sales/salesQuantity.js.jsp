<!--
作者:谢辉
日期:2017-06-08 16:54:20
页面:销售出库明细表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //查询
    function filter() {
        EasyUI.grid.search("dg", "salesQuantityFrom");
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}/salesOrder/Quantitylist",
            onBeforeLoad: dgOnBeforeLoad,
        });
        const cal = new Calendar();
        cal.add(Calendar.field.MONTH, -1);
        cal.set(Calendar.field.DAY_OF_MONTH, 1);
        $("#start").datebox("setValue", cal.format("yyyy-MM-dd"));
        cal.add(Calendar.field.MONTH, 1);
        cal.add(Calendar.field.DAY_OF_MONTH, -1);
        $("#end").datebox("setValue", cal.format("yyyy-MM-dd"));
        //filter();
    });

    function export1() {
        //var order = JQ.getFormAsJson("salesQuantityFrom");
        //location.href=path+"/salesOrder/export2?jss="+JSON.stringify(order);
        //alert(JQ.getFormAsString("salesQuantityFrom"));
        location.href = encodeURI(path + "salesOrder/export2?" + JQ.getFormAsString("salesQuantityFrom"));
    }

    function weightFormatter(value, row, inde) {
        if (value === undefined)
            return null;
        return value.toFixed(2);
    }

    function PRFormatter(value, row, inde) {
        value = row.RW + row.PW;
        return value.toFixed(2);
    }

    function prWeightFormatter(value, row, inde) {
        value = row.TWEIGHT - (row.RW + row.PW);
        if (value < 0)
            return 0;
        return value.toFixed(2);
    }

    function prwWeightFormatter(value, row, inde) {
        value = row.WRW + row.WPW;
        return value.toFixed(2);
    }

    function rowStyler(index, row) {
        return "";
    }

    function formatterIsTc(value, row, inde) {
        if (value === 1) {
            return "套材";
        } else if (value === 2) {
            return "非套材";
        } else if (value === -1) {
            return "胚布";
        }
    }
</script>