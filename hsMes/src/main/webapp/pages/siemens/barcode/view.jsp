<!--
作者:徐波
日期:2017-02-07 10:23:00
页面:产品信息查询JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>产品信息查询</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="view.js.jsp" %>
    <style type="text/css">
        td {
            padding-left: 10px;
            height: 35px;
        }

        .title {
            padding-right: 5px;
        }

        table {
            width: 100%;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" title="条码结构" style="width:400px;">
    <div class="easyui-layout" fit=true>
        <div data-options="region:'north',split:false,border:false" class="datagrid-toolbar"
             style="height:30px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchInfo"
                   editable="true"
                   data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');}}]">
        </div>
        <div data-options="region:'center'">
            <ul id="tree" class="easyui-tree"></ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false" style="position: relative;" title="详细信息">
    <table id="p">
        <tr>
            <td class="title">订单号</td>
            <td id="p_order"></td>
        </tr>
        <tr>
            <td class="title">批次号</td>
            <td id="p_batch"></td>
        </tr>
        <tr>
            <td class="title">部件</td>
            <td id="p_part"></td>
        </tr>
        <tr>
            <td class="title">打印日期</td>
            <td id="p_printDate"></td>
        </tr>
    </table>
    <table id="c" style="display:none;">
        <tr>
            <td class="title">订单号</td>
            <td id="c_order"></td>
        </tr>
        <tr>
            <td class="title">批次号</td>
            <td id="c_batch"></td>
        </tr>
        <tr>
            <td class="title">部件</td>
            <td id="c_part"></td>
        </tr>
        <tr>
            <td class="title">裁片</td>
            <td id="c_fragmentName"></td>
        </tr>
        <tr>
            <td class="title">图号</td>
            <td id="c_drawingNo"></td>
        </tr>
        <tr>
            <td class="title">组别</td>
            <td id="c_group"></td>
        </tr>
        <tr>
            <td class="title">机长</td>
            <td id="c_groupLeader"></td>
        </tr>
        <tr>
            <td class="title">组套人</td>
            <td id="c_suitUser"></td>
        </tr>
        <tr>
            <td class="title">组套时间</td>
            <td id="c_suitTime"></td>
        </tr>
        <tr>
            <td class="title">设备</td>
            <td id="c_device"></td>
        </tr>
        <tr>
            <td class="title">打印日期</td>
            <td id="c_printDate"></td>
        </tr>
    </table>
</div>
</body>