<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>销售订单</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="salesOrder.js.jsp" %>
    <style type="text/css">
        @keyframes changed {
            from {
                color: white;
                background: #3669c1;
            }
            to {
                color: white;
                background: #82a8e8;
            }
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="forceEdit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="close" name="ids"/>
            <jsp:param value="cancelclose" name="ids"/>
            <jsp:param value="submit" name="ids"/>
            <jsp:param value="view" name="ids"/>
            <jsp:param value="excel" name="ids"/>
            <jsp:param value="copy" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="platform-edit2" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="platform-close" name="icons"/>
            <jsp:param value="platform-icon80" name="icons"/>
            <jsp:param value="platform-icon154" name="icons"/>
            <jsp:param value="icon-tip" name="icons"/>
            <jsp:param value="platform-icon9" name="icons"/>
            <jsp:param value="platform-copy" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="强制变更" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="关闭" name="names"/>
            <jsp:param value="取消关闭" name="names"/>
            <jsp:param value="提交审核" name="names"/>
            <jsp:param value="查看审核" name="names"/>
            <jsp:param value="导出" name="names"/>
            <jsp:param value="复制关闭订单" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="forceEdit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="closeOrder()" name="funs"/>
            <jsp:param value="cancelcloseOrder()" name="funs"/>
            <jsp:param value="doAudit()" name="funs"/>
            <jsp:param value="view()" name="funs"/>
            <jsp:param value="exportExcel()" name="funs"/>
            <jsp:param value="copy()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="salesOrderSearchForm" autoSearchFunction="false">
                订单编号:<input type="text" name="filter[orderCode]" like="true" class="easyui-textbox"
                                data-options="onChange:filter">
                订单号　:<input type="text" name="filter[salesOrderSubCode]" like="true" class="easyui-textbox"
                               data-options="onChange:filter">
                客户简称:<input type="text" name="filter[consumerSimpleName]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a><br>
                业务人员:<input type="text" name="filter[userName]" like="true" class="easyui-textbox">
                订单类型:<input type="text" name="filter[orderType]" class="easyui-combobox"
                                data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'常规'},{'v':'2','t':'试样'},{'v':'3','t':'新品'},{'v':'-1','t':'未知'}],onSelect:filter">
                订单属性:<input type="text" name="filter[export]" class="easyui-combobox"
                                data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'内销'},{'v':'0','t':'外销'}],onSelect:filter"><br>
                厂内名称:<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox"
                                data-options="onChange:filter">
                客户产品:<input type="text" name="filter[consumerProductName]" like="true" class="easyui-textbox"
                                data-options="onChange:filter">
                产品规格:<input type="text" name="filter[productModel]" like="true" class="easyui-textbox"
                                data-options="onChange:filter">
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="true" title="" url="${path}salesOrder/list" toolbar="#toolbar" pagination="true"
           data-options="onClickRow:orderRowClick,nowrap:false,onLoadSuccess:onDgLoadSuccess ">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="SALESORDERCODE" sortable="true" width="21">订单编号</th>
            <th field="SALESORDERTYPE" sortable="true" width="10" data-options="formatter:orderTypeFormat">订单类型</th>
            <th field="SALESORDERDATE" sortable="true" width="13" data-options="formatter:orderDateFormat">下单日期</th>
            <!-- <th field="CONSUMERNAME" sortable="true" width="35">客户名称</th> -->
            <th field="CONSUMERSIMPLENAME" width="20" sortable="true">客户简称</th>
            <th field="SALESORDERISEXPORT" sortable="true" width="10" data-options="formatter:exportFormat">
                内销/外销/胚布
            </th>
            <!-- <th field="SALESORDERTOTALMONEY" sortable="true" width="15" >订单总金额</th> -->
            <th field="USERNAME" sortable="true" width="13">业务员</th>
            <th field="AUDITSTATE" sortable="true" width="15" formatter="formatterReviewState">订单审核状态</th>
            <th field="ISCLOSED" sortable="true" width="13" formatter="stateFormatter">订单状态</th>
        </tr>
        </thead>
    </table>
</div>
<div id="salesOrderMemo" data-options="region:'east',border:true,split:true" title="销售订单备注"
     style="width:200px;padding:5px;">

</div>
<div id="dd" class="easyui-dialog" title="关闭订单" style="width:700px;height:300px;"
     data-options="resizable:true,closed:true,modal:true,onOpen:onOpen,maximizable:true,
        		buttons: [{
                    text:'关闭',
                    iconCls:'icon-ok',
                    handler:function(){
                        doCloseOrder();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                       $('#dd').dialog('close');
                    }
                }]">
    <table id="dgg"></table>
</div>
<div id="ddd" class="easyui-dialog" title="取消关闭订单" style="width:700px;height:300px;"
     data-options="resizable:true,closed:true,modal:true,onOpen:onOpen1,maximizable:true,
        		buttons: [{
                    text:'取消关闭',
                    iconCls:'icon-ok',
                    handler:function(){
                        doCancelCloseOrder();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                       $('#ddd').dialog('close');
                    }
                }]">
    <table id="dggg"></table>
</div>
<%@ include file="../packTask/pack_task_order.jsp" %>
</body>