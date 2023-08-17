<!--
作者:king
日期:2017-8-2 10:39:01
页面:成品类别管理JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script type="text/javascript">
    const exportUrl = path + "productsReport/export3";
    //获取上月最后一天或第一天
    const nowdays = new Date();
    const year = nowdays.getFullYear();
    const month = nowdays.getMonth();
    const day = nowdays.getDate();
    const hours = nowdays.getHours(); //获取系统时，
    const minutes = nowdays.getMinutes(); //分
    const seconds = nowdays.getSeconds(); //秒
    const myDate = new Date(year, month, 0);
    $(document).ready(function () {
        const time2 = year + month + day + " " + hours + ":" + minutes + ":" + seconds;
        $("#end").datetimebox("setValue", time2);
    });
    $(function () {
        $('#dg').datagrid({
            url: "${path}productsReport/pcsslist",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });
    /**
     * 行统计
     */
    let flg = true;

    function onLoadSuccess(data) {
        if (flg) {
            appendRow();
        }
        flg = true;
    }

    /**
     * 表格末尾追加统计行
     */
    function appendRow() {
        $("#dg").datagrid('appendRow', {
            FACTORYPRODUCTNAME: '<span class="subtotal" style=" font-weight: bold;">小计</span>',
            WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("WEIGHT") + '</span>'//重量
        });
        flg = false;
    }

    /**
     * 指定列求和
     */
    function compute(colName) {
        const rows = $("#dg").datagrid("getRows");
        let total = 0;
        for (let i = 0; i < rows.length; i++) {
            total += parseFloat(rows[i][colName]);
        }
        console.log(total);
        return total;
    }

    /**
     * Excel 导出
     */
    function exportExcel() {
        location.href = encodeURI(exportUrl + "?" + JQ.getFormAsString("productStockSearchForm"));
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "productStockSearchForm");
    }

    function formatterStockState(value, row) {
        if (value === 1) {
            return "入库";
        } else {
            return "出库";
        }
    }

    function formatterState(value, row) {
        if (value === 1) {
            return "在库";
        } else if (value === -1) {
            return "不在库";
        } else {
            return "";
        }
    }

    //超期
    function rowStyler(index, row) {
        let style = "";
        if (row.PRODUCTSHELFLIFE < inDays(null, row)) {
            style += 'color:red';
        }
        if (!isEmpty(row.NEWBATCHCODE)) {
            style += 'background:#f9ff00;';
        }
        return style;
    }

    function inDays(value, row) {
        if (isEmpty(row.INTIME)) return "";
        const intimes = row.INTIME + "";
        const begintime_ms = new Date(intimes.replace(/-/g, "/"));
        const nowtimes = new Date();
        const millions = nowtimes.getTime() - begintime_ms.getTime();
        const days = Math.floor(millions / (24 * 3600 * 1000));
        return days;
    }

    function deliveryFormatter(value, row, index) {
        if (!isEmpty(row.PLANDELIVERYDATE)) {
            return row.PLANDELIVERYDATE.substring(0, 10);
        }
        return row.DELIVERYDATE;
    }

    function orderFormatter(value, row, index) {
        if (!isEmpty(row.NEWSALESORDERCODE)) {
            return row.NEWSALESORDERCODE;
        }
        return value;
    }

    function warehouseFormatter(value, row, index) {
        if (!isEmpty(value)) {
            return value.toUpperCase();
        }
    }

    function batchFormatter(value, row, index) {
        if (!isEmpty(row.NEWBATCHCODE)) {
            return row.NEWBATCHCODE;
        }
        return value;
    }

    function modelFormatter(value, row, index) {
        if (!isEmpty(row.NEWPRODUCTMODEL)) {
            return row.NEWPRODUCTMODEL;
        }
        return value;
    }

    function consumerFormatter(value, row, index) {
        if (!isEmpty(row.NEWCONSUMER)) {
            return row.NEWCONSUMER;
        }
        return value;
    }
</script>
