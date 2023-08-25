<!--
作者:孙利
日期:2018-03-01
页面:库存(明细)JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>原料库存状态表</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="detail.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false">
    <div id="toolbar">
        <div id="p" class="easyui-panel" title="查询"
             style="width:100%;height:135px; padding:5px;background:rgb(250, 250, 250);"
             data-options="iconCls:'icon-search',collapsible:true,onExpand:resizeDg,onCollapse:resizeDg">
            <form action="#" id="materialSearchForm" autoSearchFunction="false">
                托盘编码：<input type="text" name="filter[palletCode]" class="easyui-textbox">
                原料编码：<input type="text" name="filter[materialCode]" like="true" class="easyui-textbox">
                产品大类：<input type="text" name="filter[produceCategory]" like="true" class="easyui-textbox">
                规格型号：<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox"><br>
                仓　　库：<input type="text" name="filter[warehouseCode]" class="easyui-combobox"
                              data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=ycl',onSelect:filter">
                库　　位：<input type="text" name="filter[warehousePosCode]" class="easyui-textbox">
                是否放行：<input type="text" name="filter[isPass]" class="easyui-combobox"
                                data-options="data: [
                                            {value:'0',text:'正常'},
                                            {value:'1',text:'放行'},
                                        ] ,icons: [{
                                    iconCls:'icon-clear',
                                    handler: function(e){
                                        $(e.data.target).combobox('clear');
                                    }
                                }],onSelect:filter">
                是否冻结：<input type="text" name="filter[isLocked]" class="easyui-combobox"
                                data-options="data: [
                                            {value:'0',text:'正常'},
                                            {value:'1',text:'冻结'},
                                        ] ,icons: [{
                                    iconCls:'icon-clear',
                                    handler: function(e){
                                        $(e.data.target).combobox('clear');
                                    }
                                }],onSelect:filter"><br>
                生产日期：<input type="text" name="filter[start]" class="easyui-datebox">
                　　　至：<input type="text" name="filter[end]" class="easyui-datebox">
                入库时间：<input type="text" name="filter[inTimeStart]" class="easyui-datebox">
                　　　至：<input type="text" name="filter[inTimeEnd]" class="easyui-datebox"><br>
                在库状态：<input type="text" name="filter[stockState]" value="0" class="easyui-combobox"
                                data-options="data: [
                                            {value:'0',text:'在库'},
                                            {value:'1',text:'不在库'},
                                        ] ,icons: [{
                                    iconCls:'icon-clear',
                                    handler: function(e){
                                        $(e.data.target).combobox('clear');
                                    }
                                }],onSelect:filter">
                质量等级：<input type="text" name="filter[state]" value="1" class="easyui-combobox"
                                data-options="data: [
                                            {value:'1',text:'合格'},
                                            {value:'2',text:'不合格'}
                                        ] ,icons: [{
                                    iconCls:'icon-clear',
                                    handler: function(e){
                                        $(e.data.target).combobox('clear');
                                    }
                                }],onSelect:filter">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="detailExport()">导出</a>
            </form>
        </div>
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="sentenceLevel" name="ids"/>
            <jsp:param value="lock" name="ids"/>
            <jsp:param value="unlock" name="ids"/>
            <jsp:param value="ispass" name="ids"/>
            <jsp:param value="unpass" name="ids"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="platform-lock" name="icons"/>
            <jsp:param value="platform-key" name="icons"/>
            <jsp:param value="platform-send_receive" name="icons"/>
            <jsp:param value="platform-del" name="icons"/>
            <jsp:param value="物料判级" name="names"/>
            <jsp:param value="冻结" name="names"/>
            <jsp:param value="解冻" name="names"/>
            <jsp:param value="紧急放行" name="names"/>
            <jsp:param value="取消放行" name="names"/>
            <jsp:param value="sentenceLevel()" name="funs"/>
            <jsp:param value="lock()" name="funs"/>
            <jsp:param value="unlock()" name="funs"/>
            <jsp:param value="ispass()" name="funs"/>
            <jsp:param value="unpass()" name="funs"/>
        </jsp:include>
        <!-- <button type="button" onclick="lock()">冻结</button>
        <button type="button" onclick="unlock()">解冻</button>
        <button type="button" onclick="ispass()">放行</button>
        <button type="button" onclick="unpass()">取消放行</button>
        <button type="button" onclick="sentenceLevel()">物料判级</button>  -->
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="false" fit="true" data-options="onLoadSuccess:onLoadSuccess">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="MATERIALCODE" sortable="true" width="100">原料编码</th>
            <th field="PRODUCECATEGORY" sortable="true" width="120">产品大类</th>
            <th field="MATERIALMODEL" sortable="true" width="130">规格型号</th>
            <th field="PALLETCODE" width="120">托盘编码</th>
            <th field="INTIME" sortable="true" width="80" formatter="TimeFormatter2">入库时间</th>
            <th field="WAREHOUSECODE" width="60">仓库</th>
            <th field="WAREHOUSEPOSCODE" width="60">库位</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="STATE" width="60" sortable="true" formatter="stateFormatter">质量等级</th>
            <th field="ISPASS" width="70" sortable="true" formatter="isPassFormatter">是否放行</th>
            <th field="ISLOCKED" width="70" sortable="true" formatter="isLockFormatter">是否冻结</th>
            <th field="STOCKSTATE" width="70" sortable="true" formatter="stockStateFormatter">库存状态</th>
            <th field="PRODUCTIONDATE" width="80" sortable="true" formatter="TimeFormatter2">生产日期</th>
            <th field="EXPIREDATE" width="80" sortable="true" formatter="TimeFormatter2">有效期</th>
            <th field="WEIGHT" width="80" sortable="true">重量(KG)</th>
            <!-- <th field="MATERIALMEMO" width="80" sortable="true">备注</th> -->
            <!-- <th field="MATERIALMINSTOCK" sortable="true" width="80">最低库存</th>
            <th field="MATERIALMAXSTOCK" sortable="true" width="80">最大库存</th> -->
            <th field="REALUPPERDEVIATION" width="80" formatter="realDevationFormatter">实际偏差</th>
            <th field="UPPERDEVIATION" width="80" formatter="devationFormatter">理论偏差</th>
            <th field="REALSUBWAY" width="80">接头方式</th>
            <th field="MATERIALSHELFLIFE" sortable="true" width="80">保质期</th>
            <!-- <th field="MADERATE" sortable="true" width="80">制成率</th> -->
        </tr>
        </thead>
    </table>
</div>
</body>