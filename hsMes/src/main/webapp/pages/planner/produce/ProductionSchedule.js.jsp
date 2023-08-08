<!--
作者:谢辉
日期:2017-06-11 16:54:20
页面:生产进度跟踪报表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //定义总行数
    let total = 0;

    //在加载前，设置totalRows，默认0，后台如果获取到0，那么就会计算总数，否则不再计算总数
    function setTotal(param) {
        param.totalRows = total;
    }

    //加载完成后，也要重新设置total，这样就能保证当前翻页时，不再重新计算总数
    function onLoadSuccess(data) {
        total = data.total;
    }

    //查询
    function filter() {
        total = 0;
        EasyUI.grid.search("dg", "ProductscheFrom");
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}planner/produce/schelist",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

    function resizeDg(data) {
        $('#dg').datagrid('resize', {height: 'auto'})
    }

    /* $(document).ready(function(){
        setTimeout(function(){
            $("#p").panel("collapse");
        },500);
    }); */

    var new_window = null;

    function export1() {
        const start = $("#start").datebox("getValue");
        const end = $("#end").datebox("getValue");
        if (start === "" || end === "") {
            Tip.warn("请选择计调下单时间段");
            new_window.close();
            return;
        }
        Loading.show("正在导出...");
        new_window.location.href = encodeURI(path + "planner/produce/export2?" + JQ.getFormAsString("ProductscheFrom"));
        Loading.hide("");
    }

    function weightFormatter(value, row, inde) {
        if ("总计" !== row.CATEGORYNAME) {
            if (value === undefined)
                return 0;
            return value.toFixed(2);
        }
    }

    function countFormatter(value, row, inde) {
        if (value === undefined)
            return 0;
        return value.toFixed(0);
    }

    function ProductCompleteFormatter(value, row, inde) {
        if ("总计" !== row.CATEGORYNAME) {
            if (row.UNCOMPLETEROLLCOUNT <= 0) {
                return '已完成';
            }
            return '未完成';
        }
    }

    function closedFormatter(value, row, index) {
        if ("总计" !== row.CATEGORYNAME) {
            if (value === 1) {
                return "已关闭";
            } else {
                return "正常";
            }
        }
    }

    function deliverywarningFormatter(value, row, inde) {
        if (value === -1)
            return "";
        const intimes = row.DELEVERYDATE + "";
        const begintime_ms = new Date(intimes.replace(/-/g, "/"));
        const nowtimes = new Date();
        const millions = begintime_ms.getTime() - nowtimes.getTime();
        const days = Math.floor(millions / (24 * 3600 * 1000));
        if (days <= 5) {
            return "是";
        } else
            return "否";
    }

    function rowStyler(index, row) {
        if (row._CLOSED === '已关闭') {
            return "text-decoration:line-through;";
        }
        if (row._COMPLETED === '已完成') {
            return "";
        }
        if (row._YUJING === '是') {
            return "color:red;";
        }
        return "";
    }

    function dataFormatter(value, row, index) {
        if (value === undefined)
            return null;
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function wWeight(value, row, inde) {
        if ((row.REQUIREMENTCOUNT - row.RC) > 0) {
            if (row.PLANTOTALWEIGHT > 0) {
                value = (row.REQUIREMENTCOUNT - row.RC) / row.REQUIREMENTCOUNT * row.PLANTOTALWEIGHT;
                return value.toFixed(2);
            }
        } else
            return 0;
    }

    function exportFormat(value, row, index) {
        //（1胚布订单,0外销,-1内销）
        switch (value) {
            case 1:
                return "内销";
            case 0:
                return "外销";
            case -1:
                return "胚布订单";
        }
    }

    function StockinFormat(value, row, inde) {
        if ("总计" !== row.CATEGORYNAME) {
            if (value === undefined)
                return 0;
            return value;
        }
    }
</script>