<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const dialogWidth = 700, dialogHeight = 350;

    function rowStyler(index, row) {
        let str = new Date(row.MATERIALSHELFLIFES);
        str = str.setTime(str.getTime() - 8000 * 60 * 60);
        let date1 = new Date();
        date1 = date1.setDate(date1.getDate() - 1);
        if (str < date1) {
            return 'background-color:red';
        }
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "comparisonDetailSearchForm");
    }

    function numberChange(value, row) {
        if (!value) {
            return "0";
        }
        return value;
    }

    function inDays(value, row) {
        console.log(value);
        if (isEmpty(value)) return "";
        if (value <= 30) return "1个月以内";
        if (value > 30 && value <= 90) return "1-3个月";
        if (value > 90 && value <= 180) return "3-6个月";
        if (value > 180 && value <= 360) return "6-12个月";
        if (value >= 361) return "12个月以上";
    }

    function exportDetail() {
        location.href = encodeURI(path + "stock/productStock/exportComparison?" + JQ.getFormAsString("comparisonDetailSearchForm"));
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}stock/productStock/comparisonDetail",
            onBeforeLoad: dgOnBeforeLoad,
        });
        const nowdays = new Date();
        let year = nowdays.getFullYear();
        let month = nowdays.getMonth();
        if (month === 0) {
            month = 12;
            year = year - 1;
        }
        if (month < 10) {
            month = "0" + month;
        }
        //var firstDay = year + "-" + month + "-" + "01";//上个月的第一天
        const myDate = new Date(year, month, 0);
        const lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
        $("#start").datetimebox("setValue",
            lastDay + " 08:00:00");
        /* $("#end").datetimebox("setValue",
                new Calendar().format("yyyy-MM-dd") + " 20:00:00"); */
        /* filter(); */
    });

    //合计
    function onLoadSuccess(data) {
        $("#dg").datagrid('appendRow', {
            CONSUMERPRODUCTNAME: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
            OLDWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("OLDWEIGHT") + '</span>',
            NOWWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("NOWWEIGHT") + '</span>',
            DIFFERENCE: '<span class="subtotal" style=" font-weight: bold;">' + compute("DIFFERENCE") + '</span>'
        });
    }

    //指定列求和
    function compute(colName) {
        const rows = $("#dg").datagrid("getRows");
        let total = 0;
        for (let i = 0; i < rows.length; i++) {
            total += parseFloat(rows[i][colName]);
        }
        return total.toFixed(1);
    }
</script>