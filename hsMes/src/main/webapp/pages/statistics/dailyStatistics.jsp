<!--
作者:徐波
日期:2016-11-26 14:44:04
页面:综合统计JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>生产统计</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="dailyStatistics.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border:0px solid #DDDDDD">
            <form action="#" id="totalStatisticsSearchForm">
                　条码号:<input type="text" name="filter[rollBarcode]" like="true" class="easyui-textbox">
                　条码类型:<input type="text" name="searchType" class="easyui-combobox" value="" text="全部"
                                 data-options=" valueField: 'id',
       				 textField: 'text',data:[{'id':'','text':'全部'},{'id':'roll','text':'卷条码'},{'id':'part','text':'部件条码'},{'id':'box','text':'箱条码'},{'id':'tray','text':'托条码'}],onSelect:filter">
                客户名称:<input type="text" name="filter[CONSUMERNAME]" like="true" class="easyui-textbox">
                　　　车间:<input type="text" name="filter[workshopcode]" class="easyui-combobox"
                               data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave,cut'">
                　批次号:<input type="text" name="filter[batchCode]"
                               class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print"
                   onclick="exportDailyStatistics()">导出
                </a></br>
                产品规格:<input type="text" name="filter[productModel]" like="true"
                                class="easyui-textbox">
                　质量等级:<input type="text" class="easyui-combobox" name="filter[rollqualitygradecode]"
                                 data-options="valueField:'gradename',textField:'gradedesc',url:'<%=basePath%>qualityGrade/getQualityGrade'">
                　机台号:<input type="text" name="filter[deviceCode]" class="easyui-textbox">
                内部订单号:<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox">
                在库状态:<input type="easyui-combobox" name="filter[state]" value="" class="easyui-combobox"
                                data-options=" valueField: 'id',
                     textField: 'text' ,data:[{'id':'','text':'全部'},{'id':'1','text':'在库'},{'id':'2','text':'待入库'},{'id':'3','text':'在途'},{'id':'-1','text':'出库'},{'id':'0','text':'未入库'}],onSelect:filter"></br>
                产出时间:<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
                　　&nbsp;&nbsp;　至: <input type="text" id="end" name="filter[end]" class="easyui-datetimebox">
                　操作人:<input type="text" name="filter[loginName]" like="true" class="easyui-textbox">
                　计划单号:<input type="text" name="filter[producePlanCode]" like="true" class="easyui-textbox">
                是否作废:<input type="text" name="filter[isAbandon]" class="easyui-combobox"
                                data-options=" valueField: 'id',textField: 'text',data:[{'id':'','text':'全部'},{'id':'1','text':'已作废'},{'id':'0','text':'正常'}],onSelect:filter">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a> </br>
                是否拆包:<input type="text" name="filter[isOpened]" class="easyui-combobox"
                                data-options=" valueField: 'id',textField: 'text',data:[{'id':'','text':'全部'},{'id':'1','text':'已拆包'},{'id':'0','text':'正常'}],onSelect:filter">
                　是否打包:<input type="text" name="filter[isPacked]" class="easyui-combobox"
                                 data-options=" valueField: 'id',textField: 'text',data:[{'id':'','text':'全部'},{'id':'1','text':'已打包'},{'id':'0','text':'未打包'}],onSelect:filter">
                客户订单号:<input type="text" name="filter[salesOrderSubCodePrint]" like="true" class="easyui-textbox">
                客户代码:<input type="text" name="filter[CONSUMERCODE]" like="true" class="easyui-textbox">
                客户简称:<input type="text" name="filter[CONSUMERSIMPLENAME]" like="true" class="easyui-textbox">
            </form>
        </div>
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="changeInfo" name="ids"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="修改重量" name="names"/>
            <jsp:param value="changeInfo()" name="funs"/>

        </jsp:include>
    </div>
    <table id="dg" singleSelect="false" title=""
           class="easyui-datagrid"
           toolbar="#toolbar" pagination="true" rownumbers="true"
           fitColumns="false" fit="true" data-options="onBeforeLoad:setTotal,onLoadSuccess:onLoadSuccess">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="ROLLBARCODE" sortable="true" width="150" formatter="barcodeFormatter">条码号</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="BARCODETYPE" sortable="true" width="60" formatter="barcodeTypeFormatter">条码类型</th>
            <th field="PRODUCEPLANCODE" sortable="true" width="150">计划单号</th>
            <th field="SALESORDERCODE" sortable="true" width="150">内部订单号</th>
            <th field="SALESORDERSUBCODEPRINT" sortable="true" width="150">客户订单号</th>
            <th field="CONSUMERNAME" sortable="true" width="155">客户名称</th>
            <th field="CONSUMERSIMPLENAME" sortable="true" width="155">客户简称</th>
            <th field="CONSUMERCODE" sortable="true" width="155">客户代码</th>
            <th field="PRODUCTMODEL" sortable="true" width="130">产品规格</th>
            <th field="BLADEPROFILE" sortable="true" width="130">叶型</th>
            <th field="BATCHCODE" sortable="true" width="80">批次号</th>
            <th field="ROLLQUALITYGRADECODE" width="60">质量等级</th>
            <th field="DEVICECODE" sortable="true" width="80">机台号</th>
            <th field="DEPARTMENTNAME" sortable="true" width="80" formatter="getWorkShop">车间</th>
            <th field="ROLLWEIGHT" sortable="true" width="70" formatter="processNumberFormatter">卷重(kg)</th>
            <th field="PRODUCTWIDTH" sortable="true" width="70" formatter="processNumberFormatter">门幅(mm)</th>
            <th field="PRODUCTWEIGHT" sortable="true" width="70" formatter="processNumberFormatter">称重重量(kg)</th>
            <th field="PRODUCTLENGTH" sortable="true" width="70" formatter="processNumberFormatter">卷长(m)</th>
            <th field="ROLLREALLENGTH" sortable="true" width="70" formatter="processNumberFormatter">实际卷长(m)</th>
            <th field="ROLLCOUNT" width="70">卷数</th>
            <th field="ROLLOUTPUTTIME" sortable="true" width="140">生产时间</th>
            <th field="LOGINNAME" sortable="true" width="70">操作人</th>
            <th field="STATE" sortable="true" width="70" formatter="stockStateFormatter">库存状态</th>
            <th field="ISLOCKED" sortable="true" width="70" formatter="lockStateFormatter">状态</th>
            <th field="ISABANDON" sortable="true" width="70" formatter="isAbandonFormatter">是否作废</th>
            <th field="ISPACKED" sortable="true" width="70" formatter="isPackedFormatter">是否打包</th>
            <th field="ISOPENED" sortable="true" width="70" formatter="isOpenFormatter">是否拆包</th>
            <th field="MEMO" sortable="true" width="150">备注</th>
        </tr>
        </thead>
    </table>
</div>
</body>
