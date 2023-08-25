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
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="produceInfo.js.jsp" %>
    <style type="text/css">
        td {
            padding-left: 10px;
            height: 35px;
        }

        .title {
            padding-right: 5px;
        }

        .modifybtn {
            border-radius: 5px;
            width: 50px;
            border: none;
            color: white;
            background: #2a5469;
            padding: 3px;
        }

        .savebtn {
            border-radius: 5px;
            width: 50px;
            border: none;
            color: white;
            background: red;
            padding: 3px;
        }

        .abandonbtn {
            border-radius: 5px;
            width: 80px;
            border: none;
            color: white;
            background: red;
            padding: 3px;
        }

        .w {
            width: 100px;
            border: none;
            padding: 5px;
            border-bottom: 2px solid black;
        }

        .memo {
            width: 300px;
            border: none;
            padding: 5px;
            border-bottom: 2px solid black;
        }

        .w:read-only {
            background-color: #eee;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" title="产品层级结构" style="width:260px;">
    <div class="easyui-layout" fit=true>
        <div data-options="region:'north',split:false,border:false" class="datagrid-toolbar"
             style="height:52px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchInfo"
                   editable="true"
                   data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');}}]">
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconCls="platform-icon9"
               onclick="Export()"><spring:message code="Excel导出"/></a>
        </div>
        <div data-options="region:'center'">
            <ul id="produceTree" class="easyui-tree"></ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false" style="position: relative;" id="infos" title="产品信息">
</div>
</body>