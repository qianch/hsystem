<!--
	作者:高飞
	日期:2017-8-3 20:38:40
	页面:裁片条码JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>裁片条码</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="fragmentBarcode.js.jsp" %>
	<style type="text/css">
		#fragmentBarcodeSearchForm .textbox{
			width:145px!important;
		}
		.title{
			min-width:100px;
		}
		#barcode{
			word-wrap: break-word;
   			word-break: break-word;
		}
	</style>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="extraPrint" name="ids"/>
				<jsp:param value="export" name="ids"/>
				<jsp:param value="icon-print" name="icons"/>
				<jsp:param value="icon-excel" name="icons"/>
				<jsp:param value="补打" name="names"/>
				<jsp:param value="导出" name="names"/>
				<jsp:param value="extraPrint()" name="funs"/>
				<jsp:param value="exportBarcode()" name="funs"/>
			</jsp:include>
			<div id="p" class="easyui-panel" title="查询" style="width:100%;height:210px; padding:5px;background:rgb(250, 250, 250);" data-options="iconCls:'icon-search',collapsible:true,onExpand:resizeDg,onCollapse:resizeDg">
	            <form action="#" id="fragmentBarcodeSearchForm" autoSearch="true" autoSearchFunction="false">
						　条码号:<input type="text" class="easyui-textbox" like="true" name="filter[BARCODE]">
						任务单号:<input type="text" class="easyui-textbox" like="true" name="filter[CTCODE]">
						派工单号:<input type="text" class="easyui-textbox" like="true" name="filter[CTOCODE]">
						　订单号:<input type="text" class="easyui-textbox" like="true" name="filter[ORDERCODE]"><br>
						　批次号:<input type="text" class="easyui-textbox" like="true" name="filter[BATCHCODE]">
						客户名称:<input type="text" class="easyui-textbox" like="true" name="filter[CONSUMERNAME]">
						客户大类:<input type="text" class="easyui-textbox" like="true" name="filter[CONSUMERCATEGORY]">
						部件名称:<input type="text" class="easyui-textbox" like="true" name="filter[PARTNAME]"><br>
						裁片名称:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTNAME]">
						　　图号:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTDRAWINGNO]">
						图纸版本:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTDRAWINGVER]">
						出图顺序:<input type="text" class="easyui-textbox" like="true" name="filter[PRINTSORT]"><br>
						　　备注:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTMEMO]">
						　长度M:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTLENGTH]">
						宽度MM:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTWIDTH]">
						　　重量:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTWEIGHT]"><br>
						组套状态:<input type="text" class="easyui-combobox" name="filter[ISPACKED]" data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'0',t:'未组套'},{'v':'1',t:'已组套'}],onChange:filter">
						打印人员:<input type="text" class="easyui-textbox" like="true" name="filter[PRINTUSER]">
						打印类型:<input type="text" class="easyui-textbox" like="true" name="filter[PRINTTYPE]">
						　组套人:<input type="text" class="easyui-textbox" like="true" name="filter[PACKUSERNAME]"><br>
						　　机长:<input type="text" class="easyui-textbox" like="true" name="filter[GROUPLEADER]">
						　　机台:<input type="text" class="easyui-textbox" like="true" name="filter[DEVICECODE]">
						胚布规格:<input type="text" class="easyui-textbox" like="true" name="filter[FARBICMODEL]">
						　　组别:<input type="text" class="easyui-textbox" like="true" name="filter[GROUPNAME]"><br>
						组套时间:<input type="text" id="PACKEDTIME_S" class="easyui-datetimebox" name="filter[PACKEDTIME_S]" data-options="onSelect:filter"> 
							　　至　 <input type="text" id="PACKEDTIME_E" class="easyui-datetimebox" name="filter[PACKEDTIME_E]" data-options="onSelect:filter">
						打印时间:<input type="text" id="PRINTTIME_S" class="easyui-datetimebox" name="filter[PRINTTIME_S]" data-options="onSelect:filter"> 
							　　至　 <input type="text" id="PRINTTIME_E" class="easyui-datetimebox" name="filter[PRINTTIME_E]" data-options="onSelect:filter">
						
						　　　　　<a class="easyui-linkbutton" iconcls="icon-search" onclick="filter()"> 搜索 </a>
				</form>
      	  </div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}siemens/fragmentBarcode/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true">
			<thead data-options="frozen:true">
	            <tr>
	                <th field="ID" checkbox=true ></th>
					<th field="BARCODE" sortable="true" width="150">条码号</th>
					<th field="CTCODE" sortable="true" width="150">任务单号</th>
					<th field="CTOCODE" sortable="true" width="150">派工单号</th>
	            </tr>
	        </thead>
			<thead>
				<tr>
					<th field="ORDERCODE" sortable="true" width="150">订单号</th>
					<th field="BATCHCODE" sortable="true" width="120">批次号</th>
					<th field="CONSUMERNAME" sortable="true" width="100">客户名称</th>
					<th field="CONSUMERCATEGORY" sortable="true" width="60">客户大类</th>
					<th field="PARTNAME" sortable="true" width="100">部件名称</th>
					<th field="FRAGMENTCODE" sortable="true" width="150">小部件编码</th>
					<th field="FRAGMENTNAME" sortable="true" width="120">小部件名称</th>
					<th field="FRAGMENTDRAWINGNO" sortable="true" width="100">图号</th>
					<th field="FRAGMENTDRAWINGVER" sortable="true" width="80">图纸版本</th>
					<th field="PRINTSORT" sortable="true" width="80">出图顺序</th>
					<th field="FRAGMENTMEMO" sortable="true" width="80">备注</th>
					<th field="FRAGMENTLENGTH" sortable="true" width="60">长度M</th>
					<th field="FRAGMENTWIDTH" sortable="true" width="60">宽度MM</th>
					<th field="FRAGMENTWEIGHT" sortable="true" width="60">重量</th>
					<th field="ISPACKED" sortable="true" width="60" formatter="suitStateFormatter">组套状态</th>
					<th field="PACKEDTIME" sortable="true" width="125">组套时间</th>
					<th field="PACKUSERNAME" sortable="true" width="80">组套操作人</th>
					<th field="GROUPNAME" sortable="true" width="60">组别</th>
					<th field="GROUPLEADER" sortable="true" width="60">机长</th>
					<th field="DEVICECODE" sortable="true" width="60">机台</th>
					<th field="WORKSHOP" sortable="true" width="80">车间</th>
					<th field="FARBICMODEL" sortable="true" width="120">胚布规格</th>
					<th field="PRINTTIME" sortable="true" width="125">打印时间</th>
					<th field="PRINTUSER" sortable="true" width="60">打印人员</th>
					<th field="PRINTTYPE" sortable="true" width="60">打印类型</th>
					<th field="REPRINTREASON" sortable="true" width="60">重打原因</th>
					<th field="EXTRAPRINTTIME" sortable="true" width="125">补打时间</th>
					<th field="EXTRAPRINTUSER" sortable="true" width="60">补打人员</th>
					<th field="EXTRAPRINTREASON" sortable="true" width="60">补打原因</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div id="dlg" class="easyui-dialog" title="小部件条码补打" style="width:600px;height:350px;"
		data-options="
	            	maximizable:true,
	            	border:'none',
	            	resizable:true,
	            	closed:true,
	                iconCls: 'icon-print',
	                buttons: '#dlg-buttons',
	                modal:true
	            ">
	    <table style="width:100%;">
	    	<tr>
				<td class="title">条码号</td>
				<td style="padding:7px;" id="barcodes">
				</td>
		  	</tr>
			  <tr>
			    <td class="title">打印机</td>
			    <td style="padding:5px;padding-left:0px;">
			    	<input type="text" id="printer" class="easyui-combobox" style="width:80%!important;" data-options="icons:[],data:printers,valueField:'PRINTERNAME',textField:'PRINTERTXTNAME'">
			    </td>
			  </tr>
			  <tr>
			    <td class="title">补打原因</td>
			    <td style="padding:5px;">
			    	<textarea id="reason" style="width:80%;height:100px;padding:5px;border-radius:5px;resize:none;" maxLength="80"></textarea>
			    </td>
			  </tr>
		</table>
	</div>
	<div id="dlg-buttons" style="display:none;">
		<a href="javascript:void(0)" iconCls="icon-print" class="easyui-linkbutton" onclick="doPrint()">打印</a>
		<a href="javascript:void(0)" iconCls="icon-cancel" class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
	</div>
</body>
</html>