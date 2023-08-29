<!--
作者:肖文彬
日期:2016-11-16 11:15:02
页面:原料库存状态表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>原料库存状态表</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="materialStockState.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="materialStockStateSearchForm" autoSearchFunction="false">
                仓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库：
                <input type="text" class="easyui-combobox" name="filter[warehouseCode]" like="true"
                       data-options="valueField:'warehouseName',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=ycl',onSelect:filter">
                库&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位：
                <input type="text" like="true" name="filter[warehousePosCode]" class="easyui-textbox">
                状 　 &nbsp;态：<input type="text" name="filter[state]" value="" class="easyui-combobox"
                                     data-options="data: [
	                        {value:'0',text:'待检'},
	                        {value:'1',text:'合格'},
	                        {value:'2',text:'不合格'}
                    	] ,icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}],onSelect:filter">
                冻结<input type="text" name="filter[isLocked]" class="easyui-combobox"
                           data-options="data:[{value:'',text:'全部'},{value:'1',text:'冻结'},{value:'0',text:'正常'}],onSelect:filter"></br>
                规格：&nbsp;<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox">
                托盘编号：&nbsp;<input type="text" name="filter[materialCode]" like="true" class="easyui-textbox">
                入库日期：<input type="text" name="filter[intimea]" class="easyui-datebox">&nbsp;
                至：<input type="text" name="filter[intimeb]" class="easyui-datebox">&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)" onclick="filter()"> 搜索 </a></br>
            </form>
        </div>
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="sentenceLevel" name="ids"/>
            <jsp:param value="lock" name="ids"/>
            <jsp:param value="unlock" name="ids"/>
            <jsp:param value="ispass" name="ids"/>
            <jsp:param value="unpass" name="ids"/>
            <jsp:param value="platform-ok3" name="icons"/>
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
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}stock/materialStockState/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="rowStyler:rowStyler,pageList:[10,20,30,50,100,500,1000]">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="MATERIALCODE" sortable="true" width="15">托盘编号</th>
            <th field="PRODUCECATEGORY" sortable="true" width="15">产品大类</th>
            <th field="MATERIALMODEL" sortable="true" width="15">产品规格</th>
            <th field="WAREHOUSENAME" sortable="true" width="15">仓库</th>
            <th field="WAREHOUSEPOSCODE" sortable="true" width="15">库位</th>
            <th field="INWEIGHT" sortable="true" width="15">拖重量（kg）</th>
            <th field="STATE" sortable="true" width="15" formatter="formatterState">状态
            </th>
            <th field="ISLOCKED" sortable="true" width="15" formatter="formatterIslock">冻结状态
            </th>
            <th field="ISPASS" sortable="true" width="15" formatter="formatterIspass">是否紧急放行
            </th>
            <th field="INTIME" sortable="true" width="15">入库日期</th>
            <th field="PRODUCEDATE" sortable="true" width="15">生产日期</th>
            <th field="MATERIALSHELFLIFE" sortable="true" width="15">保质天数</th>
        </tr>
        </thead>
    </table>
</div>
</body>