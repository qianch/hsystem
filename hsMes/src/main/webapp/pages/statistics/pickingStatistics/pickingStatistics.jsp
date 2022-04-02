<!--
	作者:肖文彬
	日期:2016-11-16 12:33:40
	页面:出库单明细JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>生产领料统计</title>
  	<%@ include file="/pages/base/meta.jsp" %>
	<%@ include file="pickingStatistics.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="materialOutOrderDetailSearchForm" autoSearchFunction="false">
					原料大类:<input type="text" name="filter[produceCategory]" like="true" class="easyui-textbox" />
					规格型号:<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox" />
					领用车间:<input type="text" name="filter[workShop]" like="true" class="easyui-combobox"
							data-options=" valueField: 'id',textField: 'text',data:[{'id':'','text':'全部车间'},{'id':'编织一车间','text':'编织一车间'},{'id':'编织二车间','text':'编织二车间'},{'id':'编织三车间','text':'编织三车间'},{'id':'裁剪车间','text':'裁剪车间'}],onSelect:filter">
					<br/>
					开始时间:<input type="text" id="start" name="filter[start]" class="easyui-datetimebox" />
					结束时间:<input type="text" name="filter[end]" class="easyui-datetimebox" />
					&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
					&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="platform-icon9" onclick="exportExcel()">
						导出
					</a>
				</form>
			</div>
		</div>
		<table id="dg" remotesort="false" singleSelect="true" title="生产领料统计汇总" class="easyui-datagrid"   toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true" data-options="onLoadSuccess:onLoadSuccess,showFooter:true">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PRODUCECATEGORY" sortable="true" width="20%">原料大类</th>
					<th field="MATERIALMODEL" sortable="true" width="20%">规格型号</th>
					<th field="OUTWEIGHT" sortable="true" width="20%">重量(Kg)</th>
					<th field="OUTTIME" sortable="true" width="18%" formatter="outTimeFormatter">出库时间</th>
					<th field="WORKSHOP" sortable="true" width="18%">领料车间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>