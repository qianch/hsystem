<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:物料需求计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    $(function () {
        loadProducePlans();
    });
    const exportUrl = path + "planner/mrp/export";

    function _export() {
        const r = $("#dl").datalist("getSelections");
        //获取所有选中的行，计算需要的物料
        if (r.length === 0) {
            $("#dg_ftc").datagrid("loadData", []);
            $("#dg_bc").datagrid("loadData", []);
            Loading.hide();
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        window.open(exportUrl + "?ids=" + ids.toString());
    }

    /**
     * 加载左侧 DataList数据，如果无查询条件，默认最新100条数据
     */
    function loadProducePlans() {
        Loading.show("加载中");
        var order = JQ.getFormAsJson("salesOrderSearchForm");
        if (Object.keys(order).length === 0) {
            order.all = 0;
            order.rows = 100;
        } else {
            order.all = 1;
        }
        $.ajax({
            url: path + "planner/produce/datalist",
            type: 'post',
            dataType: 'json',
            data: order,
            success: function (data) {
                loadDataList(data);
                Loading.hide();
                loadMrp();
            }
        });

        //内部函数
        function loadDataList(data) {
            const rs = data.rows;
            for (let i = 0; i < rs.length; i++) {
                rs[i]["CREATETIME"] = rs[i]["CREATETIME"].substring(0, 10);
            }
            $("#dl").datalist({
                "data": rs
            });
            //默认选中第一行的生产计划
            /* if (rs.length != 0) {
                $("#dl").datalist("selectRow", 0);
                //var r=$("#dl").datalist("getSelections");
                //console.log(r);
                //loadMrp( r);
            } */
            Loading.hide();
        }
    }

    function allSelect() {
        $("#dl").datalist("selectAll");
        loadMrp();
    }

    function unAllSelect() {
        $("#dl").datalist("unselectAll");
        loadMrp();
    }

    function loadMrp() {
        Loading.show("加载中");
        const r = $("#dl").datalist("getSelections");
        //获取所有选中的行，计算需要的物料
        if (r.length === 0) {
            $("#dg_ftc").datagrid("loadData", []);
            $("#dg_bc").datagrid("loadData", []);
            Loading.hide();
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        const str = {"ids": ids.toString()};
        $.ajax({
            url: path + "planner/mrp/list",
            type: "post",
            data: str,
            dataType: "json",
            success: function (data) {
                Loading.hide();
                $("#dg_ftc").datagrid("loadData", data.yuanliao);
                $("#dg_bc").datagrid("loadData", data.baocai);
            }
        });
    }

    function numberFormatter(value, row, index) {
        return value + "（KG）";
    }

    function numberFormatter2(value, row, index) {
        let unit = row.PACKMATERIALUNIT;
        if (unit === undefined || unit === "" || unit == null)
            unit = "";
        else
            unit = "（" + unit + "）";
        return value + unit;
    }

    function onLoadSuccess(data) {
        $("#dg_ftc").datagrid('appendRow', {
            MATERIALMODEL: '<span class="subtotal" style=" font-weight: bold;">小计</span>',
            MATERIALTOTALWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("MATERIALTOTALWEIGHT") + '</span>',
            MATERIALSTOCKWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("MATERIALSTOCKWEIGHT") + '</span>'
        });
    }

    function compute(colName) {
        const rows = $("#dg_ftc").datagrid("getRows");
        let total = 0;
        for (let i = 0; i < rows.length; i++) {
            total += parseFloat(rows[i][colName]);
        }
        return total.toFixed(2);
    }
</script>