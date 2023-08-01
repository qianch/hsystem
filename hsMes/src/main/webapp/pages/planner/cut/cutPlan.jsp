<!-- 作者:宋黎明
日期:2016-10-18 13:35:17
页面:裁剪计划JSP文件 -->

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>裁剪计划</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="cutPlan.js.jsp" %>
    <style type="text/css">
        button {
            background: white;
            border: 1px solid #e2dbdb;
            font-size: 12px;
        }

        button:hover {
            background: #fff900;
            color: #9f00ff;
        }

        #createCutTask {
            display: none;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" resizable="false" title="裁剪计划" minWidth="200" width="200">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',split:false,border:false"
             style="/* background:red; */height:98px;text-align: center;">
            <form autoSearchFunction="loadProducePlans">
                <%--					<input id="searchInput0" name="searchInput0" type="text" class="easyui-combobox" prompt="请选择车间"  data-options="data: [--%>
                <%--		                        {value:'00107',text:'编织一车间'},--%>
                <%--		                        {value:'00108',text:'编织二车间'},--%>
                <%--		                        {value:'00109',text:'编织三车间'}],onchange:loadProducePlans()"><br/>--%>
                <input id="searchInput0" name="searchInput" type="text" class="easyui-searchbox" prompt="输入计划单号"
                       searcher="loadProducePlans" editable="true"
                       data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');loadProducePlans();}}]"><br/>
                <input id="searchInput" name="searchInput" type="text" class="easyui-searchbox" prompt="输入计划单号"
                       searcher="loadProducePlans" editable="true"
                       data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');loadProducePlans();}}]"><br/>
                <input id="searchInput1" name="searchInput1" type="text" class="easyui-searchbox" prompt="输入客户简称"
                       searcher="loadProducePlans" editable="true"
                       data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');loadProducePlans();}}]"><br/>
                <input id="searchInput2" name="searchInput2" type="text" class="easyui-searchbox" prompt="输入叶型"
                       searcher="loadProducePlans" editable="true"
                       data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');loadProducePlans();}}]"><br/>
                <input id="searchInput3" name="searchInput3" type="text" class="easyui-searchbox" prompt="输入批号"
                       searcher="loadProducePlans" editable="true"
                       data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');loadProducePlans();}}]"><br/>
                <input id="searchInput4" name="searchInput4" type="text" class="easyui-searchbox" prompt="输入订单号"
                       searcher="loadProducePlans" editable="true"
                       data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');loadProducePlans();}}]">
            </form>
        </div>
        <div data-options="region:'center',border:false" style="overflow-x:hidden;">
            <ul id="dl" class="easyui-datalist" lines="true" style="width:100%;" valueField="ID"
                textField="PRODUCEPLANCODE" groupField="CREATETIME" data-options="onSelect:loadCutPlan">
            </ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="finish" name="ids"/>
            <jsp:param value="doClose" name="ids"/>
            <jsp:param value="addOrder" name="ids"/>
            <jsp:param value="createCutTask" name="ids"/>

            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="platform-close" name="icons"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-cut" name="icons"/>

            <jsp:param value="完成" name="names"/>
            <jsp:param value="关闭" name="names"/>
            <jsp:param value="增加/修改胚布订单" name="names"/>
            <jsp:param value="生成裁剪任务单" name="names"/>

            <jsp:param value="finish()" name="funs"/>
            <jsp:param value="doClose()" name="funs"/>
            <jsp:param value="addOrder()" name="funs"/>
            <jsp:param value="createCutTask()" name="funs"/>

        </jsp:include>
    </div>
    <table id="dg" singleSelect="true" title="计划列表" class="easyui-datagrid" toolbar="#toolbar" pagination="false"
           rownumbers="true" fitColumns="false" fit="true" data-options="">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <!-- <th field="PLANCODE" sortable="true" width="180">生产计划单号</th> -->
            <th field="BATCHCODE" sortable="true" width="120">批次号</th>
            <th field="CONSUMERSIMPLENAME" width="100" sortable="true">客户简称</th>
            <th field="PRODUCTNAME" sortable="true" width="120">产品名称</th>
            <th field="CONSUMERPRODUCTNAME" width="120">客户产品名称</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="120">厂内产品名称</th>
            <th field="DELEVERYDATE" sortable="true" width="100" formatter="orderDateFormat">发货日期</th>
            <th field="ISCREATWEAVE" sortable="true" width="100" formatter="formatterIsCreateWeave">是否生成过胚布计划
            </th>
            <th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
            <th field="PROCESSBOMVERSION" width="80">工艺版本</th>
            <th field="BCBOMCODE" width="130" styler="bvStyler">包装代码</th>
            <th field="BCBOMVERSION" width="80">包装版本</th>
            <th field="SALESORDERCODE" sortable="true" width="160">订单号</th>
            <th field="ISFINISHED" sortable="true" width="100" formatter="formatterIsFinish">完成状态</th>
            <th field="AUDITSTATE" sortable="true" width="100" formatter="formatterState">审核状态</th>
            <!-- <th field="PRODUCTMODEL" sortable="true" width="120">产品规格</th> -->
            <th field="RC" sortable="true" width="80" data-options="formatter:rcFormatter">生产卷数</th>
            <th field="TC" sortable="true" width="80" data-options="formatter:tcFormatter">打包托数</th>
            <th field="PRODUCEDCOUNT" sortable="true" data-options="formatter:processFormatter3">生产进度</th>
            <th field="DELEVERYDATE" sortable="true" width="70">出货时间</th>
            <th field="CONSUMERNAME" sortable="true" width="100">客户名称</th>
            <th field="PACKREQ" width="100">包装要求</th>
            <th field="PROCREQ" width="100">工艺要求</th>
            <th field="COMMENT" width="80">备注</th>
        </tr>
        </thead>
    </table>
</div>
<div id="dlg" class="easyui-dialog" title="创建裁剪任务单" style="width:600px;height:200px;"
     data-options="
            	closed:true,
                iconCls: 'icon-cut',
                buttons: '#dlg-buttons',
                modal:true
            ">
    <jsp:useBean id="now" class="java.util.Date"/>
    <form id="cutTaskForm" method="post" ajax="true" action="<%=basePath %>cutTask/${empty cutTask.id ?'add':'edit'}"
          autocomplete="off" autoSearchFunction="false">
        <input type="hidden" id="pcId" name="pcId">
        <input type="hidden" id="cutPlanId" name="cutPlanId">
        <input type="hidden" id="partId" name="partId">
        <input type="hidden" id="consumerId" name="consumerId">
        <input type="hidden" id="assignSuitCount" name="assignSuitCount" value="0">
        <input type="hidden" id="isComplete" name="isComplete" value="0">
        <input type="hidden" id="packedSuitCount" name="packedSuitCount" value="0">
        <input type="hidden" id="createTime" name="createTime"
               value="<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" />">
        <input type="hidden" id="createUserName" name="createUserName" value="${userName }">
        <input type="hidden" id="consumerCategory" name="consumerCategory">
        <input type="hidden" id="isClosed" name="isClosed" value="0">
        <table width="100%">
            <tr>
                <td class="title">任务单编号:</td>
                <!--任务单编号-->
                <td>
                    <input type="text" id="taskCode" name="taskCode" class="easyui-textbox" data-options="icons:[]"
                           editable="false" required="true">
                </td>
                <td class="title">交货日期:</td>
                <!--发货日期-->
                <td>
                    <input type="text" id="deliveryDate" name="deliveryDate" class="easyui-textbox"
                           data-options="icons:[]" editable="false" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">订单号:</td>
                <!--订单号-->
                <td>
                    <input type="text" id="orderCode" name="orderCode" class="easyui-textbox" data-options="icons:[]"
                           editable="false" required="true">
                </td>
                <td class="title">批次号:</td>
                <!--批次号-->
                <td>
                    <input type="text" id="batchCode" name="batchCode" class="easyui-textbox" data-options="icons:[]"
                           editable="false" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">客户简称:</td>
                <!--客户简称-->
                <td>
                    <input type="text" id="consumerSimpleName" name="consumerSimpleName" class="easyui-textbox"
                           data-options="icons:[]" editable="false" required="true">
                </td>
                <td class="title">客户大类:</td>
                <!--客户大类-->
                <td>
                    <input type="text" id="taskConsumerCategoryX" class="easyui-textbox" data-options="icons:[]"
                           editable="false" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">部件名称:</td>
                <!--部件名称-->
                <td>
                    <input type="text" id="partName" name="partName" class="easyui-textbox" data-options="icons:[]"
                           editable="false" required="true">
                </td>
                <td class="title">任务单套数:</td>
                <!--任务单套数-->
                <td>
                    <input type="text" id="suitCount" name="suitCount" class="easyui-textbox" data-options="icons:[]"
                           editable="false" required="true">
                </td>
            </tr>

        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" iconCls="icon-save" class="easyui-linkbutton" onclick="saveCutTaskForm()">保存</a>
    <a href="javascript:void(0)" iconCls="icon-cancel" class="easyui-linkbutton"
       onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>
</body>