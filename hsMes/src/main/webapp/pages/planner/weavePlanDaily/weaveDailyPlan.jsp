<!--
作者:肖文彬
日期:2016-11-24 11:02:50
页面:日计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>日计划</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="weaveDailyPlan.js.jsp" %>
    <style type="text/css">
        pre {
            white-space: pre-wrap;
            word-wrap: break-word;
        }
    </style>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true,border:false">
        <div data-options="region:'center',border:true">
            <div id="toolbar1">
                <jsp:include page="../../base/toolbar.jsp">
                    <jsp:param value="print" name="ids"/>
                    <jsp:param value="icon-print" name="icons"/>
                    <jsp:param value="打印" name="names"/>
                    <jsp:param value="print()" name="funs"/>
                </jsp:include>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-edit"
                   onclick="check_details()">查看 工艺/包装 需求</a>
            </div>

            <table id="dailyDetails" width="100%" singleSelect="true" title="日计划明细" class="easyui-datagrid"
                   pagination="false" rownumbers="true" fitColumns="false" fit="true" toolbar="#toolbar1"
                   data-options="onClickRow:dailyRowClick,onLoadSuccess:dailyLoadSuccess, singleSelect:true,
                collapsible:true,
                rownumbers:true,
                fitColumns:true,
                view:groupview,
                groupField:'PRODUCTTYPE',
                groupFormatter:function(value,rows){
                if(value==1){
                	 return '大卷产品';
                }
                if(value==2){
               		 return '中卷产品';
                }
                if(value==3){
               		 return '小卷产品';
                }
                if(value==4){
                	return '其他产品';
                }
                }">
                <thead frozen="true">
                <tr>
                    <th field="ID" checkbox=true></th>
                    <th field="DELEVERYDATE" sortable="true" width="60" formatter="orderDateFormat">出货日期</th>
                    <th field="PLANCODE" sortable="true" width="150">生产计划单号</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th field="SALESORDERSUBCODE" sortable="true" width="100">销售订单号</th>
                    <th field="PRODUCTMODEL" sortable="true" width="130">产品规格</th>
                    <th field="BATCHCODE" sortable="true" width="100">批次号</th>
                    <th field="PRODUCTWIDTH" sortable="true" width="50">门幅</th>
                    <th field="PRODUCTLENGTH" sortable="true" width="50">米长</th>
                    <th field="PRODUCEDTOTALWEIGHT" sortable="true" width="80" formatter="processFormatter"
                        styler="processStyler">生产进度
                    </th>
                    <th field="TOTALTRAYCOUNT" sortable="true" width="80">总托数</th>
                    <th field="REQUIREMENTCOUNT" sortable="true" width="80">总重量</th>
                    <th field="TOTALROLLCOUNT" sortable="true" width="80">总卷数</th>
                    <th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
                    <th field="PROCESSBOMVERSION" width="80">工艺版本</th>
                    <th field="BCBOMCODE" width="130" styler="bvStyler">包装代码</th>
                    <th field="BCBOMVERSION" width="80">包装版本</th>
                    <th field="PACKREQ" sortable="true" width="80">包装需求</th>
                    <th field="PROCREQ" sortable="true" width="80">工艺需求</th>
                </tr>
                </thead>
            </table>
        </div>
        <div data-options="region:'south',border:false,split:true" style="height:300px;">
            <div class="easyui-layout" fit="true">
                <div class="easyui-layout" data-options="region:'layout',border:false,fit:true">
                    <div data-options="region:'center',border:false" style="">
                        <table id="dailyDeviceDg" singleSelect="true" idField="DEVICEID" class="easyui-datagrid"
                               title="生产机台" style="width:100%height:250px" fit="true" fitColumns="true">
                            <thead>
                            <tr>
                                <!-- <th data-options="field:'DEVICEID',checkbox:true"></th> -->
                                <th data-options="field:'DEVICENAME',width:80">机台名称</th>
                                <th data-options="field:'DEVICECODE',width:100">机台代码</th>
                                <th data-options="field:'PRODUCECOUNT',width:80">生产数量</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div data-options="region:'east',border:false" title="备注" style="width:50%;">
                        <div class="easyui-panel" fit="true" id="comment_panel" style="width:100%;padding:10px;">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div data-options="region:'west',border:true,width:420,collapsible:true">
    <div id="toolbar">
        <form id="deviceSearchForm" autoSearchFunction="false">
            日期:<input type="text" class="easyui-datebox" name="filter[produceDate]" style="width:120px;"
                        data-options="onSelect:filter">
            车间:<input type="text" id="workshop" name="filter[workshop]" class="easyui-combobox" style="width:130px;"
                        data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave'">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search"
               onclick="filter()"> 搜索 </a>
        </form>
        <hr>
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="audit" name="ids"/>
            <jsp:param value="view" name="ids"/>
            <jsp:param value="reload" name="ids"/>
            <jsp:param value="export" name="ids"/>

            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="icon-tip" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-print" name="icons"/>

            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="提交审核" name="names"/>
            <jsp:param value="查看审核" name="names"/>
            <jsp:param value="关闭计划" name="names"/>
            <jsp:param value="导出" name="names"/>

            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="doAudit()" name="funs"/>
            <jsp:param value="view()" name="funs"/>
            <jsp:param value="reloadAudit()" name="funs"/>
            <jsp:param value="_export()" name="funs"/>
        </jsp:include>
    </div>
    <table id="dg" singleSelect="true" title="日计划列表" class="easyui-datagrid" url="<%=basePath%>weaveDailyPlan/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="onClickRow:dgRowClick,onLoadSuccess:dgLoadSuccess,rowStyler:formatterIsClosed">
        <thead>
        <tr>
            <th field="ID" checkbox=true formatter="formatterIsClosed"></th>
            <th field="DATE" sortable="true" width="15">时间</th>
            <th field="WORKSHOP" sortable="true" width="15">车间</th>
            <th field="USERNAME" sortable="true" width="13">操作人</th>
            <th field="AUDITSTATE" sortable="true" width="15" formatter="formatterState">审核状态</th>
        </tr>
        </thead>
    </table>
</div>
</body>