<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>调度任务管理</title>
    <%@ include file="../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/layerDate/need/laydate.css">
    <script type="text/javascript" src="<%=basePath %>/resources/layerDate/laydate.js"></script>
    <script type="text/javascript" src="<%=basePath %>/resources/platform/schedule.js"></script>
</head>

<body style="margin:0;">
<div id="toolbar">
    <jsp:include page="../base/toolbar.jsp">
        <jsp:param value="add" name="ids"/>
        <jsp:param value="edit" name="ids"/>
        <jsp:param value="delete" name="ids"/>
        <jsp:param value="start" name="ids"/>
        <jsp:param value="stop" name="ids"/>
        <jsp:param value="enable" name="ids"/>
        <jsp:param value="icon-add" name="icons"/>
        <jsp:param value="icon-edit" name="icons"/>
        <jsp:param value="icon-remove" name="icons"/>
        <jsp:param value="icon-start" name="icons"/>
        <jsp:param value="icon-stop" name="icons"/>
        <jsp:param value="icon-pause" name="icons"/>
        <jsp:param value="icon-disable" name="icons"/>
        <jsp:param value="增加" name="names"/>
        <jsp:param value="编辑" name="names"/>
        <jsp:param value="删除" name="names"/>
        <jsp:param value="启动" name="names"/>
        <jsp:param value="停止" name="names"/>
        <jsp:param value="暂停" name="names"/>
        <jsp:param value="add()" name="funs"/>
        <jsp:param value="edit()" name="funs"/>
        <jsp:param value="deleteSchedule()" name="funs"/>
        <jsp:param value="start()" name="funs"/>
        <jsp:param value="stop()" name="funs"/>
        <jsp:param value="pause()" name="funs"/>
    </jsp:include>
    <div>
        <form id="searchbox" style="margin: 0;" id="scheduleFilter">
            类名：<input type="text" name="filter[clazz]" id="clazzz" like="true" value="" class="textbox"
                        style="height:22px;">
            方法名：<input type="text" name="filter[method]" id="method" like="true" value="" class="textbox"
                          style="height:22px;">
            起始时间：<input type="text" name="filter[startcreateTime]" id="startcreateTime" value=""
                            class="textbox laydate-icon" readonly="readonly">
            结束时间：<input type="text" name="filter[endcreateTime]" id="endcreateTime" value=""
                            class="textbox laydate-icon" readonly="readonly">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
               onclick="filter()">
                搜索
            </a>
        </form>
    </div>
</div>
<table id="dg" fit="true" title="" class="easyui-datagrid" url="<%=basePath %>schedule/list" toolbar="#toolbar"
       pagination="true" rownumbers="true" fitColumns="true" singleSelect="true">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="NAME" width="20">任务名称</th>
        <!-- <th field="type" width="20">任务类型</th> -->
        <th field="CRON" width="20">CRON表达式</th>
        <th field="CREATETIME" width="20">创建时间</th>
        <th field="STARTTIME" width="20">启动时间</th>
        <th field="INTERVALS" width="20">执行间隔</th>
        <th field="TIMES" width="20">执行次数</th>
        <th field="EXECUTETIMES" width="20">已执行次数</th>
        <th field="RUNDESC" width="20">运行描述</th>
        <th field="STATUS" width="20" data-options="formatter:statusFormatter">状态</th>
    </tr>
    </thead>
</table>
</body>
</html>
