<!--
作者:高飞
日期:2017-8-3 20:38:40
页面:裁片条码JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加裁片条码
    const addUrl = path + "fragmentBarcode/add";
    //编辑裁片条码
    const editUrl = path + "fragmentBarcode/edit";
    //删除裁片条码
    const deleteUrl = path + "fragmentBarcode/delete";
    const dialogWidth = 700, dialogHeight = 350;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "fragmentBarcodeSearchForm");
    }

    function resizeDg(data) {
        $('#dg').datagrid('resize', {height: 'auto'})
    }

    let printers =${empty printers?"[]":printers};

    function extraPrint() {
        const rows = $("#dg").datagrid("getSelections");
        if (rows.length === 0) {
            return;
        }
        const codes = [];
        for (let i = 0; i < rows.length; i++) {
            codes.push(rows[i].BARCODE);
        }
        $("#dlg").dialog("open");
        $("#barcodes").text(codes.join(","));
    }

    function doPrint() {
        const barcodes = $("#barcodes").text();
        const printer = $("#printer").combobox("getValue");
        if (printer === "") {
            Tip.warn("请选择打印机");
            return;
        }
        const reason = $("#reason").val();
        if (reason === "") {
            Tip.warn("请输入补打原因");
            return;
        }
        Loading.show("正在打印");
        JQ.ajaxPost(path + "siemens/fragmentBarcode/extraPrint", {
            barcodes: barcodes,
            printer: printer,
            reason: reason,
            user: "${userName}"
        }, function (data) {
            Tip.success("打印成功");
            $("#dlg").dialog("close");
            Loading.hide();
        });
    }

    function exportBarcode() {
        const d1 = $("#PACKEDTIME_S").datebox("getValue");
        const d2 = $("#PACKEDTIME_E").datebox("getValue");
        const d3 = $("#PRINTTIME_S").datebox("getValue");
        const d4 = $("#PRINTTIME_E").datebox("getValue");
        if (d1 === "" || d2 === "" || d3 === "" || d4 === "") {
            Tip.warn("必须输入打印和组套时间");
            return;
        }
        location.href = encodeURI(path + "excel/export/裁片条码导出/com.bluebirdme.mes.siemens.excel.FragmentBarcodeExportHandler/filter?" + JQ.getFormAsString("fragmentBarcodeSearchForm"));
    }

    function suitStateFormatter(v, r, i) {
        if (v === 1) {
            return "<label style='display:inline-block;width:100%;height:100%;background:green;color:white;text-align:center;'>已组套</font>";
        } else {
            return "<label style='display:inline-block;width:100%;height:100%;background:red;color:white;text-align:center;'>未组套</font>";
        }
    }
</script>