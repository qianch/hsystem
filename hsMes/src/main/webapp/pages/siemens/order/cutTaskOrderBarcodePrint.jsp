<!--
作者:高飞
日期:2017-7-31 17:04:13
页面:裁剪派工单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="dlg3" class="easyui-dialog" title="小部件条码打印" style="width:600px;height:80%;"
     data-options="
            	maximizable:true,
            	border:'none',
            	resizable:true,
            	closed:true,
                iconCls: 'icon-print',
                buttons: '#dlg-buttons3',
                modal:true">
    <div id="barcodePrintToolbar" style="font-weight:bold;padding:5px;">
        打印机:<input type="text" id="printer" class="easyui-combobox"
                      data-options="icons:[],data:printers,valueField:'PRINTERNAME',textField:'PRINTERTXTNAME'">
        打印套数:<input type="text" id="printSuitCount" class="easyui-numberspinner"
                        data-options="icons:[],min:1,max:999">
    </div>
    <table id="drawingsDgPrint" toolbar="#barcodePrintToolbar" fit="true" class="easyui-datagrid" width="100%"
           pagination="false" rownumbers="true" fitColumns="true">
        <thead>
        <tr>
            <th field="ID" checkbox="true"></th>
            <th field="FRAGMENTDRAWINGNO" width="100">图纸号</th>
            <th field="FRAGMENTDRAWINGVER" width="100">图纸版本</th>
        </tr>
        </thead>
    </table>
</div>
<div id="dlg-buttons3" style="display:none;">
    <a href="javascript:void(0)" iconCls="icon-print" class="easyui-linkbutton" onclick="doPrintBarcode()">打印</a>
    <a href="javascript:void(0)" iconCls="icon-cancel" class="easyui-linkbutton"
       onclick="javascript:$('#dlg3').dialog('close')">关闭</a>
</div>    