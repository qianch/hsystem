<!--
作者:高飞
日期:2017-2-9 19:53:49
页面:翻包计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>翻包计划</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="turnBagPlan.js.jsp" %>
    <base href="<%=basePath%>">
    <style type="text/css">
        .title {
            height: 35px;
            width: 115px;
        }

        #CONTENT td {
            padding-left: 10px;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:true">
<div data-options="region:'west',split:true,border:true,collapsible:true" title="翻包计划列表" style="width:600px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="complete" name="ids"/>
            <jsp:param value="commit" name="ids"/>
            <jsp:param value="view" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="platform-ok1" name="icons"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="icon-tip" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="完成" name="names"/>
            <jsp:param value="提交审核" name="names"/>
            <jsp:param value="查看审核" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="complete()" name="funs"/>
            <jsp:param value="doAudit()" name="funs"/>
            <jsp:param value="view()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="turnBagPlanSearchForm" autoSearch="" autoSearchFunction="false">
                翻包单号:<input type="text" name="filter[trunBagCode]" like="true" class="easyui-textbox"
                                style="width:100px">
                下单人:<input type="text" name="filter[userName]" like="true" class="easyui-textbox"
                              style="width:100px">
                翻包部门:<input type="text" name="filter[departmentName]" like="true" class="easyui-textbox"
                                style="width:100px"><br/>
                订单号:<input type="text" name="filter[orderCode]" like="true" class="easyui-textbox"
                              style="width:100px">
                <input type="text" id="start" name="filter[start]" value="" prompt="开始时间" class="easyui-datetimebox"
                       data-options="onChange:filter">
                <input type="text" id="end" name="filter[end]" value="" prompt="截止时间" class="easyui-datetimebox"
                       data-options="onChange:filter">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="true" class="easyui-datagrid" url="${path}planner/tbp/list" toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="onDblClickRow:dbClickEdit,onClickRow:doClick,onLoadSuccess:dgLoadSuccess,rowStyler:completeStyler">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="TRUNBAGCODE" sortable="true" width="20">翻包单号</th>
            <th field="CREATETIME" sortable="true" width="16">下单时间</th>
            <th field="FINISHTIME" sortable="true" width="16">完成时间</th>
            <th field="USERNAME" sortable="true" width="15">下单人</th>
            <th field="DEPARTMENTNAME" sortable="true" width="15">执行部门</th>
            <th field="ISCOMPLETED" sortable="true" width="15" formatter="isCompleteFormatter">完成状态</th>
            <th field="AUDITSTATE" sortable="true" width="15" formatter="autoAuditStateFormatter">审核状态</th>
            <!-- <th field="MEMO" sortable="true" width="15">说明</th> -->
        </tr>
        </thead>
    </table>
</div>
<div data-options="region:'center'" title="翻包任务详情">
    <table width="100%" id="CONTENT">
        <tr>
            <td class="title">新订单号</td>
            <!--新批次号-->
            <td id="NEWSALESORDERCODE"></td>
            <td class="title">新批次号</td>
            <!--新批次号-->
            <td id="NEWBATCHCODE"></td>
        </tr>
        <tr>
            <td class="title">交货日期</td>
            <!--翻包任务单号-->
            <td id="DELIVERYDATE"></td>
            <td class="title">翻包数量</td>
            <!--翻包任务单号-->
            <td id="TRUNBAGCOUNT"></td>
        </tr>
        <tr>
            <td class="title">下单时间</td>
            <!--下单时间-->
            <td id="CREATETIME"></td>
            <td class="title">完成时间</td>
            <!--完成截止时间-->
            <td id="FINISHTIME"></td>
        </tr>
        <tr>
            <td class="title">下单人员</td>
            <!--下单人-->
            <td id="USERNAME"></td>
            <td class="title">翻包执行部门</td>
            <!--翻包执行部门-->
            <td id="DEPARTMENTNAME"></td>
        </tr>
        <tr>
            <td class="title">说明</td>
            <td colspan="3" id="MEMO"></td>
        </tr>
    </table>
    <table id="details" title="待翻包订单" singleSelect="true" class="easyui-datagrid" fitColumns="false"
           style="width:100%;" toolbar="#toolbar_product">
        <thead frozen="true">
        <tr>
            <th width="120" field="SALESORDERSUBCODE">订单号</th>
            <th width="120" field="BATCHCODE">翻包批次</th>
            <th width="80" field="TURNBAGCOUNT" formatter="processFormatter" styler="processStyler">翻包数量</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <!-- <th width="100" field="SALESORDERSUBCODEPRINT">客户订单号</th> -->
            <th width=150 field="FACTORYPRODUCTNAME">厂内名称</th>
            <th width="100" field="CONSUMERPRODUCTNAME">客户产品名称</th>
            <th width="150" field="PRODUCTMODEL">产品型号</th>
            <th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
            <th field="PRODUCTROLLLENGTH" width="80">卷长(m)</th>
            <th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
            <th field="PRODUCTPROCESSCODE" width="140" styler="vStyler">工艺代码</th>
            <th field="PRODUCTPROCESSBOMVERSION" width="80">工艺版本</th>
            <th field="PRODUCTPACKAGINGCODE" width="140" styler="bvStyler">包装代码</th>
            <th field="PRODUCTPACKAGEVERSION" width="80">包装版本</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>