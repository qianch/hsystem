<!--
作者:高飞
日期:2016-10-12 11:06:09
页面:原料JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //查询
    function filter() {
        EasyUI.grid.search("dg", "materialSearchForm");
    }

    $(function () {
        $("#dg").datagrid({
            url: "${path}material/out/list",
            onBeforeLoad: dgOnBeforeLoad,
        })
    })
    /**
     * 行统计
     */
    let flg = true;

    function onLoadSuccess(data) {
        if (flg) {
            appendRow1();
        }
        flg = true;
    }

    flg = true;

    function appendRow(data) {
        if (flg) {
            appendRow1();
        }
        flg = true;
    }

    function appendRow1() {
        $("#dg").datagrid('appendRow', {
            PRODUCECATEGORY: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
            WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + computes("WEIGHT") + '</span>'	//月末累计数量统计
        });
        flg = false;
    }

    /**
     * 指定列求和
     */
    function computes(colName) {
        const rows = $("#dg").datagrid("getRows");
        let totals = 0;
        for (let i = 0; i < rows.length; i++) {
            totals += parseFloat(rows[i][colName]);
        }
        return totals.toFixed(2);
    }

    //k3同步状态
    function syncStateFormatter(value, row, index) {
        if (value === 0) {
            return "未同步";
        } else if (value === 1) {
            return "已同步";
        }
    }

    //库存状态
    function stockStateFormatter(value, row, index) {
        if (value === 0) {
            return "在库";
        } else if (value === 1) {
            return "不在库";
        }
    }

    //状态
    function stateFormatter(value, row, index) {
        if (value === 0) {
            return "<font color='#b1610c'>待检</font>";
        } else if (value === 1) {
            return "<font color=green>合格</font>";
        } else if (value === 2) {
            return "<font color=red>不合格</font>";
        }
    }

    //是否放行
    function isPassFormatter(value, row, index) {
        if (value === 0) {
            return "正常";
        } else if (value === 1) {
            return "放行";
        }
    }

    //是否冻结
    function isLockFormatter(value, row, index) {
        if (value === 0) {
            return "正常";
        } else if (value === 1) {
            return "冻结";
        }
    }

    function TimeFormatter(value, row, index) {
        if (!value) return "";
        return new Calendar(value).format("yyyy-MM-dd HH:mm:ss");
    }

    //原料出库明细导出
    function exportDetail() {
        location.href = encodeURI(path + "material/outExport?" + JQ.getFormAsString("materialSearchForm"));
    }

    function exportDetail2() {
        location.href = encodeURI(path + "material/outExport2?" + JQ.getFormAsString("materialSearchForm"));
    }

    function inTimeFormatter(value, row, index) {
        if (!value) return "";
        return new Calendar(value).format("yyyy-MM-dd");
    }

    function devationFormatter(v, r, i) {
        if (!v) return "";
        return r.LOWERDEVIATION + "~" + r.UPPERDEVIATION;
    }

    function realDevationFormatter(v, r, i) {
        if (!v) return "";
        return r.REALLOWERDEVIATION + "~" + r.REALUPPERDEVIATION;
    }
</script>