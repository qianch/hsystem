<!--
作者:徐波
日期:2016-11-26 14:44:04
页面:综合统计JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const dialogWidth = 700, dialogHeight = 400;
    //定义总行数
    let total = 0;

    //在加载前，设置totalRows，默认是0.后台如果获取到0，那么就会计算总数，或者不在计算总数
    function setTotal(param) {
        param.totalRows = total;
    }

    //加载完成后，也要重新设置total，这样就能保证当前翻页时，不在重新计算总数
    function onLoadSuccess(data) {
        total = data.total;
    }

    //查询,将total重设，就会重新加载记录数量
    function filter() {
        total = 0;
        EasyUI.grid.search("dg", "totalStatisticsSearchForm");
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}tbStatistics/list",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

    //生产统计查询默认时间
    /* $(function() {
        $("#start").datetimebox("setValue",
                new Calendar().format("yyyy-MM-dd") + " 08:00:00");
        $("#end").datetimebox("setValue",
                new Calendar().format("yyyy-MM-dd") + " 20:00:00");
        filter();
    }); */

    window.onload = function () {
        $("#start").datetimebox("setValue",
            new Calendar().format("yyyy-MM-dd") + " 08:00:00");
        $("#end").datetimebox("setValue",
            new Calendar().format("yyyy-MM-dd") + " 20:00:00");
        filter();
    };

    function barcodeFormatter(value, row, index) {
        if (row.ROLLBARCODE == null || row.ROLLBARCODE === "") {
            return row.PARTBARCODE;
        }
        return value;
    }

    function isPackedFormatter(value, row, index) {
        if (value === 1) {
            return "已打包";
        } else if (value === 0) {
            return "未打包";
        } else {
            return "";
        }
    }

    function isOpenFormatter(value, row, index) {
        if (value != null) {
            if (row.ROLLBARCODE.indexOf("T") === 0) {
                if (value === 1) {
                    return "已拆包";
                } else if (value === 0) {
                    return "正常";
                } else {
                    return "";
                }
            }
        } else {
            return "";
        }
        return value;
    }

    function endPackFormatter(value, row, index) {
        if (row.ROLLBARCODE.indexOf("T") === 0) {
            if (value == null || value === 0) {
                return "未结束";
            } else {
                return "已结束";
            }
        }
        return value;
    }

    function barcodeTypeFormatter(value, row, index) {
        if (row.ROLLBARCODE == null || row.ROLLBARCODE === "") {
            return "部件条码";
        } else if (row.ROLLBARCODE.indexOf("T") === 0) {
            return "托条码";
        } else if (row.ROLLBARCODE.indexOf("B") === 0) {
            return "箱条码";
        } else {
            return "卷条码";
        }
    }

    function isAbandonFormatter(value, row, index) {
        if (value === 1) {
            return "已作废";
        } else {
            return "正常";
        }
    }

    function stockStateFormatter(value, row, index) {
        if (row.BARCODETYPE === "tray") {
            if (value === 1) {
                return "在库";
            } else if (value === -1) {
                return "出库";
            } else if (value === 0 || value == null) {
                return "未入库";
            }
        }
    }

    function lockStateFormatter(value, row, index) {
        if (value === 1) {
            return "冻结";
        } else {
            return "正常";
        }
    }

    function export1() {
        const exportUrl = path + "totalStatistics/export1?";
        let condition = "";
        x = $("form").serializeArray();
        $.each(x, function (i, field) {
            if (field.value !== '' && field.value != null) {
                if (field.name === "searchType") {
                    condition += (field.name + "=" + field.value + "&");
                } else {
                    condition += (field.name.substring(7, field.name.length - 1) + "=" + field.value + "&");
                }
            }
        });
        window.open(exportUrl + condition.substring(0, condition.length - 1));
    }
</script>