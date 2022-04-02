<!--
作者:徐波
日期:2016-11-26 14:44:04
页面:综合统计JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>

    var dialogWidth = 700, dialogHeight = 400;
    //定义总行数
    var total = 0;

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

    $(function () {
        $('#dg').datagrid({
            url: "${path}totalStatistics/list1",
            onBeforeLoad: dgOnBeforeLoad,
        });
    })

    function getWorkShop(value, row, index) {

        if (value == '00107') {
            return "编织一车间";
        }
        if (value == '00108') {
            return "编织二车间";
        }
        if (value == '00109') {
            return "编织三车间";
        }
        if (value == '00116') {
            return "裁剪一车间";
        }
        if (value == '00117') {
            return "裁剪二车间";
        }
        return value;
    }

    function barcodeFormatter(value, row, index) {
        if (row.ROLLBARCODE == null || row.ROLLBARCODE == "") {
            return row.PARTBARCODE;
        }
        return value;
    }

    function isPackedFormatter(value, row, index) {
        if (value == 1) {
            return "已打包";
        } else if (value == 0) {
            return "未打包";
        } else {
            return "";
        }
    }

    function isOpenFormatter(value, row, index) {
        if (value != null) {

            if (value == 1) {
                return "已拆包";
            } else if (value == 0) {
                return "正常";
            } else {
                return "";
            }

        } else {
            return "";
        }


        return value;
    }

    function endPackFormatter(value, row, index) {
        if (row.ROLLBARCODE.indexOf("T") == 0) {
            if (value == null || value == 0) {
                return "未结束";
            } else {
                return "已结束";
            }
        }
        return value;
    }

    function barcodeTypeFormatter(value, row, index) {
        if (row.ROLLBARCODE == null || row.ROLLBARCODE == "") {
            return "部件条码";
        } else if (row.ROLLBARCODE.indexOf("T") == 0) {
            return "托条码";
        } else if (row.ROLLBARCODE.indexOf("B") == 0) {
            return "箱条码";
        } else if (row.ROLLBARCODE.indexOf("P") == 0) {
            return "部件条码";
        } else {
            return "卷条码";
        }
        return value;
    }

    function isAbandonFormatter(value, row, index) {
        if (value == 1) {
            return "已作废";
        } else {
            return "正常";
        }
    }

    function stockStateFormatter(value, row, index) {
        /* var firstString=row.ROLLBARCODE+"";
        firstString=firstString.substring(0, 1);
        if(firstString!="T"){
            return "";
        } */
        if (row.BARCODETYPE == "tray") {
            if (value == 1) {
                return "在库";
            } else if (value == -1) {
                return "不在库";
            } else if (value == 2) {
                return "待入库";
            } else if (value == 3) {
                return "在途";
            } else {
                return "";
            }
        }

    }

    function lockStateFormatter(value, row, index) {
        if (value == 1) {
            return "冻结";
        } else {
            return "正常";
        }
    }

    var exportUrl = path + "totalStatistics/exportDailyStatistics";

    function exportDailyStatistics() {
        location.href= encodeURI(exportUrl+"?"+JQ.getFormAsString("totalStatisticsSearchForm")) ;
    }

    function export1() {
        /*   var order = JQ.getFormAsJson("totalStatisticsSearchForm");

        location.href=path+"/totalStatistics/export1?jss="+JSON.stringify(order);
        $.ajax({
            url : path + "/totalStatistics/export1?jss="+JSON.stringify(order),
            type : 'get',
            success : function(data) {


            }
        });   */
        var exportUrl = path + "totalStatistics/export1?";
        var condition = "";
        x = $("form").serializeArray();
        $.each(x, function (i, field) {
            if (field.value != '' && field.value != null) {
                if (field.name == "searchType") {

                    condition += (field.name + "=" + field.value + "&");
                } else {
                    condition += (field.name.substring(7, field.name.length - 1) + "=" + field.value + "&");

                }
            }
        });
        window.open(exportUrl + condition.substring(0, condition.length - 1));
        console.log(exportUrl + condition.substring(0, condition.length - 1));
    }


</script>
