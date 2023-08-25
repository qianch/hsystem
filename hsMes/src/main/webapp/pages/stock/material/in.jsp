<!--
作者:孙利
日期:2018-03-01
页面:原料入库/退库(明细)JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>原料入库/退库(明细)</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="in.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="materialSearchForm" autoSearchFunction="false">
                原料编码：<input type="text" name="filter[materialCode]" like="true" class="easyui-textbox">
                产品大类：<input type="text" name="filter[produceCategory]" like="true" class="easyui-textbox">
                规格型号：<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox">
                托盘编号：<input type="text" name="filter[palletCode]" like="true" class="easyui-textbox"></br>
                仓　　库：<input type="text" name="filter[warehouseCode]" class="easyui-combobox"
                              data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=ycl',onSelect:filter">
                库　　位：<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox">
                入库来源：<input type="text" name="filter[inBankSource]" like="true" class="easyui-textbox">
                入库类型：<input type="text" name="filter[inBankType]" class="easyui-combobox"
                                data-options="data: [
                            {value:'0',text:'入库'},
                            {value:'-1',text:'退库'},
                        ] ,icons: [{
                    iconCls:'icon-clear',
                    handler: function(e){
                        $(e.data.target).combobox('clear');
                    }
                }],onSelect:filter"> </br>
                操作人：<input type="text" name="filter[optUser]" like="true" class="easyui-textbox">
                库存状态：<input type="text" name="filter[stockState]" class="easyui-combobox"
                                data-options="data: [
                            {value:'0',text:'在库'},
                            {value:'1',text:'不在库'},
                        ] ,icons: [{
                    iconCls:'icon-clear',
                    handler: function(e){
                        $(e.data.target).combobox('clear');
                    }   
                }],onSelect:filter">
                入库时间：<input type="text" name="filter[start]" class="easyui-datetimebox">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="text" name="filter[end]" class="easyui-datetimebox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="exportDetail()">
                    导出
                </a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="exportDetail2()">导出2
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="false" fit="true" data-options="onLoadSuccess:onLoadSuccess">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="MATERIALCODE" width="80">原料编码</th>
            <th field="PRODUCECATEGORY" width="150">产品大类</th>
            <th field="MATERIALMODEL" width="150">规格型号</th>
            <th field="PALLETCODE" width="120">托盘编码</th>
            <th field="INTIME" width="80" formatter="inTimeFormatter">入库时间</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="WAREHOUSECODE" width="80">仓库</th>
            <th field="WAREHOUSEPOSCODE" width="80">库位</th>
            <th field="WEIGHT" width="80">重量(KG)</th>
            <th field="INBANKSOURCE" width="80">入库来源</th>
            <!-- <th field="MATERIALMINSTOCK" width="80">最低库存</th>
            <th field="MATERIALMAXSTOCK" width="80">最大库存</th>
            <th field="REALUPPERDEVIATION" width="80">上偏差</th>
            <th field="REALLOWERDEVIATION" width="80">下偏差</th>
            <th field="REALSUBWAY" width="80">接头方式</th>
            <th field="MADERATE" width="80">制成率</th>
            <th field="WEIGHT" width="80">总重量</th> -->
            <th field="REALUPPERDEVIATION" width="80" formatter="realDevationFormatter">实际偏差</th>
            <th field="UPPERDEVIATION" width="80" formatter="devationFormatter">理论偏差</th>
            <th field="REALSUBWAY" width="80">接头方式</th>
            <th field="OPTUSER" width="80">操作人</th>
            <th field="SYNCSTATE" width="80" formatter="syncStateFormatter">k3同步状态</th>
            <th field="INBANKTYPE" width="80" formatter="inBankTypeFormatter">入库类型</th>
            <th field="STATE" width="80" formatter="stateFormatter">状态</th>
            <!-- <th field="ISPASS" width="150">是否放行</th> -->
            <!-- <th field="ISLOCKED" width="150">是否冻结</th> -->
            <th field="STOCKSTATE" width="80" formatter="stockStateFormatter">库存状态</th>
            <th field="PRODUCTIONDATE" width="150" formatter="inTimeFormatter">生产日期</th>
            <!-- <th field="MATERIALMEMO" width="150">备注</th>
            <th field="MATERIALSHELFLIFE" width="80">保质期</th> -->
        </thead>
    </table>
</div>
</body>