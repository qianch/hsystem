<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const exportUrl = path + "shopStatistics/export1";

    //查询
    function filter() {
        EasyUI.grid.search("dg", "totalStatisticsSearchForm");
    }

    Date.prototype.Format = function (fmt) { //author: meizz
        const o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}shopStatistics/gfylist",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

    //获取上月最后一天或第一天
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
    const myDate = new Date(year, month, 0);
    $(document).ready(function () {
        const firstDay = year + "-" + month + "-" + "01";//上个月的第一天
        const lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
        const time2 = lastDay + " " + "12:00:00";
        $("#start").datetimebox("setValue", time2);
    });

    /**
     * 行统计
     */
    function onLoadSuccess(data) {
        const obj = eval(data);
        const row = obj.rows;
        for (let i = 0; i < row.length; i++) {
            const name = row[i].FACTORYPRODUCTNAME;
            if (name == null || name === "") {//删除厂内名称为空的行
                const rowIndex = $("#dg").datagrid("getRowIndex", row[i]);
                $("#dg").datagrid("deleteRow", rowIndex);
            }
        }
        appendRow();
    }

    /**
     * 表格末尾追加统计行
     */
    function appendRow() {
        $("#dg").datagrid('appendRow', {
            CATEGORYNAME: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
            PRODUCTWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("PRODUCTWEIGHT") + '</span>',//托
            TNUM: '<span class="subtotal" style=" font-weight: bold;">' + compute("TNUM") + '</span>'//重量
        });
    }

    /**
     * 指定列求和
     */
    function compute(colName) {
        var rows = $("#dg").datagrid("getRows");
        var total = 0;
        for (var i = 0; i < rows.length; i++) {
            total += parseFloat(rows[i][colName]);
        }
        total = total.toFixed(2);
        return total;
    }

    /**
     * Excel导出
     */
    function exportExcel() {
        location.href = encodeURI(exportUrl + "?" + JQ.getFormAsString("totalStatisticsSearchForm"));
    }
</script>