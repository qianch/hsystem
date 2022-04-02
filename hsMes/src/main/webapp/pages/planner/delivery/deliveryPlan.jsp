<!--
作者:徐波
日期:2016-11-2 9:30:07
页面:出货计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>出货计划</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="deliveryPlan.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="commit" name="ids"/>
            <jsp:param value="view" name="ids"/>
            <jsp:param value="cannel" name="ids"/>
            <jsp:param value="complete" name="ids"/>
            <jsp:param value="editOrder" name="ids"/>
            <jsp:param value="downLoadImage" name="ids"/>
            <jsp:param value="exportDeliveryExcel" name="ids"/>
            <jsp:param value="doReleaseDeliveryPlan" name="ids"/>

            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="platform-icon154" name="icons"/>
            <jsp:param value="icon-tip" name="icons"/>
            <jsp:param value="platform-icon8" name="icons"/>
            <jsp:param value="platform-close" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="platform-icon150" name="icons"/>
            <jsp:param value="platform-icon9" name="icons"/>
            <jsp:param value="platform-icon8" name="icons"/>

            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="提交" name="names"/>
            <jsp:param value="查看审核" name="names"/>
            <jsp:param value="撤回审核" name="names"/>
            <jsp:param value="关闭" name="names"/>
            <jsp:param value="填写车号" name="names"/>
            <jsp:param value="下载二维码" name="names"/>
            <jsp:param value="导出出货计划" name="names"/>
            <jsp:param value="修改出库状态为未出库" name="names"/>

            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="commit()" name="funs"/>
            <jsp:param value="view()" name="funs"/>
            <jsp:param value="cannel()" name="funs"/>
            <jsp:param value="doClose()" name="funs"/>
            <jsp:param value="editOrder()" name="funs"/>
            <jsp:param value="downLoadImage()" name="funs"/>
            <jsp:param value="exportDeliveryExcel()" name="funs"/>
            <jsp:param value="doReleaseDeliveryPlan()" name="funs"/>

        </jsp:include>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
           iconcls="platform-icon9" onclick="exportExcel()">导出</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
           iconcls="platform-copy" onclick="copy()">复制出库计划</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
           iconcls="icon-remove" onclick="unbinding()">PDA解绑</a>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="deliveryPlanSearchForm" 　 autoSearchFunction="false">
                发货单号:<input type="text" name="filter[deliveryCode]" like="true"
                            class="easyui-textbox">
                要货单位:<input type="text" name="filter[deliveryTargetCompany]"
                            like="true" class="easyui-textbox">
                　　　订单号:<input type="text"
                              name="filter[salesOrderSubCode]" like="true"
                              class="easyui-textbox">
                　　　批次号:<input type="text"
                              name="filter[batchCode]" like="true" class="easyui-textbox"></br>
                　提单号:<input type="text" name="filter[ladingCode]" like="true"
                            class="easyui-textbox">
                　　箱号:<input type="text"
                            name="filter[boxNumber]" like="true" class="easyui-textbox">
                客户产品名称:<input type="text" name="filter[consumerProductName]"
                              like="true" class="easyui-textbox">
                厂内产品名称:<input
                    type="text" name="filter[consumerProductName]" like="true"
                    class="easyui-textbox"></br>
                发货日期:<input type="text" id="start"
                            name="filter[start]" class="easyui-datebox">
                　　至:　<input type="text" id="end" name="filter[end]"
                            class="easyui-datebox">
                　　客户基地:<input
                    type="text" name="filter[basePlace]" like="true"
                    class="easyui-textbox">
                　<a href="javascript:void(0)"
                    class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                    onclick="filter()"> 搜索 </a>

            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid"
           toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="onDblClickRow:dbClickEdit,rowStyler:rowStyler,showFooter:true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="DELIVERYCODE" sortable="true" width="23">发货单编号</th>
            <th field="DELIVERYTARGETCOMPANY" sortable="true" width="45">要货单位</th>
            <th field="CONSUMERSIMPLENAME" sortable="true" width="20">单位简称</th>
            <th field="BASEPLACE" sortable="true" width="20">客户基地</th>
            <th field="CUSTOMERNOTES" sortable="true" width="20">客户备注信息</th>
            <th field="DELIVERYDATE" sortable="true" width="18" formatter="formatterDate">发货时间</th>
            <th field="USERNAME" sortable="true" width="15">出货人</th>
            <th field="LOGISTICSCOMPANY" sortable="true" width="15">物流公司</th>
            <th field="ATTENTION" sortable="true" width="15">注意事项</th>
            <th field="SAMPLEINFORMATION" sortable="true" width="15">样布信息</th>
            <th field="PACKAGINGTYPE" sortable="true" width="15">包装方式</th>
            <th field="BARCODE" sortable="true" width="15">条码</th>
            <th field="ISCOMPLETES" sortable="true" width="15">是否完成</th>
            <th field="AUDITSTATE" sortable="true" width="15" formatter="formatterState" styler="auditStyler2">审核状态</th>
            <th field="caozuo" width="15" formatter="formatterDetail">操作</th>
        </tr>
        </thead>
    </table>
</div>


</body>
