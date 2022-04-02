<!--
	作者:孙利
	日期:2017-7-5 9:30:51
	页面:称重介质JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <html>
    <head>
        <meta charset="UTF-8">
        <title>Basic DataGrid - jQuery EasyUI Demo</title>
        <link rel="stylesheet" type="text/css" href="../../themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="../../themes/icon.css">
        <link rel="stylesheet" type="text/css" href="../demo.css">
        <script type="text/javascript" src="../../jquery.min.js"></script>
        <script type="text/javascript" src="../../jquery.easyui.min.js"></script>
    </head>
    <body>
        <h2>Basic DataGrid</h2>
        <p>The DataGrid is created from markup, no JavaScript code needed.</p>
        <div style="margin:20px 0;"></div>
        
        <table class="easyui-datagrid" title="Basic DataGrid" style="width:700px;height:250px"
                data-options="singleSelect:true,collapsible:true,url:'datagrid_data1.json',method:'get'">
            <thead>
                <tr>
                    <th data-options="field:'itemid',width:80">Item ID</th>
                    <th data-options="field:'productid',width:100">Product</th>
                    <th data-options="field:'listprice',width:80,align:'right'">List Price</th>
                    <th data-options="field:'unitcost',width:80,align:'right'">Unit Cost</th>
                    <th data-options="field:'attr1',width:250">Attribute</th>
                    <th data-options="field:'status',width:60,align:'center'">Status</th>
                </tr>
            </thead>
        </table>
     
    </body>
    </html>

