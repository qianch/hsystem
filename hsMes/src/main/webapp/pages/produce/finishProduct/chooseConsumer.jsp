<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<title>选择客户信息</title>
<%@ include file="../../base/jstl.jsp" %>
<script type="text/javascript">

	//双击行事件
	$(document).ready(function(){
		$("#ddg").datagrid({
			onDblClickRow: function(rowIndex, rowData){
				_chooseConsumer(rowIndex, rowData);
			}
		});
	});
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar1">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="consumerSearchForm">
					<label class="panel-title">搜索：</label>
					客户代码：<input type="text" id="code" name="filter[code]" like="true" class="easyui-textbox">
					客户名称：<input type="text" name="filter[name]" like="true" class="easyui-textbox">
					</br>&nbsp;&nbsp;&nbsp;
					客户大类：<input type="text" name="filter[type]" like="true" class="easyui-combobox" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'国内'},
	                        {value:'2',text:'国外'}
                    	] ,icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]">
					客户等级：<input type="text" name="filter[level]" like="true" class="easyui-combobox" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'A'},
	                        {value:'2',text:'B'},
	                        {value:'3',text:'C'}
                    	],icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="ddg" singleSelect="true" title="" class="easyui-datagrid"  url="${path}consumer/list" toolbar="#toolbar1" pagination="true" rownumbers="true" fitColumns="true" fit="true">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CONSUMER_CODE" width="15">客户代码</th>
					<th field="CONSUMER_NAME" width="15">客户名称</th>
					<th field="CONSUMER_GRADE" width="15">客户等级</th>
					<th field="CONSUMER_CATEGORY" width="15">客户大类</th>
					<th field="CONSUMER_CONTACT" width="15">客户联系人</th>
					<th field="CONSUMER_ADDRESS" width="15">客户地址</th>
					<th field="CONSUMER_PHONE" width="15">联系电话</th>
					<th field="CONSUMER_EMAIL" width="15">邮件地址</th>
					<th field="CONSUMER_MEMO" width="15">备注</th>
					<th field="CONSUMER_CODE_ERP" width="15">客户ERP代码</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
