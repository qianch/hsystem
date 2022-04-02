<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<title>选择投诉信息</title>
<%@ include file="../base/jstl.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
function dateFormatter(value,row,index){
	return Assert.isEmpty(value)?"":value.substring(0,10);
}
function _common_complaint_filter() {
	EasyUI.grid.search("_common_complaint_dg","_common_complaintSearchForm");
}
$(document).ready(function() {
	//设置数据表格的DetailFormatter内容
	$('#_common_complaint_dg')
			.datagrid({
						onDblClickRow : function(index, row) {
							if (typeof _common_complaint_dbClickRow === "function") {
								_common_complaint_dbClickRow(index, row);
							} else {
								if (window.console) {
									console.log("没有为用户选择界面提供_common_complaint_dbClickRow方法，参数为index,row");
								}
							}
						},
						onLoadSuccess : function(data) {
							if (typeof _common_complaint_onLoadSuccess === "function") {
								_common_complaint_onLoadSuccess(data);
							} else {
								if (window.console) {
									console.log("未定义用户选择界面加载完成的方法：_common_complaint_onLoadSuccess(data)");
								}
							}
						}

					});

});
function _common_complaint_dgLoadSuccess(data){
	$("#_common_complaint_dg").datagrid("selectRow",0);
}
</script>


	<div data-options="region:'center',border:false" style="position: relative; ">
		
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="_common_complaintSearchForm" autoSearchFunction="false">
					<label class="panel-title">搜索：</label>
					<input type="text" name="_common_complaint_filter[data]" like="true" class="easyui-textbox" placeholder="请输入查询条件">
					<input type="text" name="_common_complaint_filter[time]" like="true" class="easyui-datebox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="_common_complaint_filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="_common_complaint_dg" singleSelect="${empty singleSelect?'true':singleSelect }"  title="投诉列表" class="easyui-datagrid"  url="${path}complaint/list"
 pagination="true" rownumbers="true" fitColumns="false" fit="true"  >
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="COMPLAINTCODE" sortable="true" width="100">投诉代码</th>
					<th field="CONSUMERNAME" sortable="true" width="80">客户名称</th>
					<th field="PRODUCTMODEL" sortable="true" width="80">供货规格</th>
					<th field="LASTUPDATEUSER" sortable="true" width="80">最后操作人</th>
					<th field="LASTUPDATEDATE" sortable="true" width="80" formatter="dateFormatter">最后操作时间</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="D8REPORT" sortable="true" width="80">8D报告</th>
					<th field="CONSUMERFROM" sortable="true" width="80">客户来源</th>
					<th field="BASEPLACE" sortable="true" width="80">基地</th>
					<th field="COMPLAINTDATE" sortable="true" width="100" formatter="dateFormatter">投诉日期</th>
					<th field="INFOFROM" sortable="true" width="80">信息来源</th>
					<th field="SUPPLYSTATE" sortable="true" width="80">供货状态</th>
					<th field="SUPPLYCOUNT" sortable="true" width="80">出货数量</th>
					<th field="DELIVERYDATE" sortable="true" width="100" formatter="dateFormatter">发货日期</th>
					<th field="PRODUCEDATE" sortable="true" width="100" formatter="dateFormatter">生成日期</th>
					<!-- <th field="QUESTIONDESC" sortable="true" width="80">问题描述</th> -->
					<th field="BUSINESSUNIT" sortable="true" width="80">责任单位</th>
					<th field="COMPLAINTTYPE" sortable="true" width="80">投诉类型</th>
					<th field="CHECKMESURES" sortable="true" width="80">措施验收</th>
					<th field="COMPENSATION" sortable="true" width="80">赔偿金额</th>
					<th field="HANDLINGPROGRESS" sortable="true" width="80">处理进度</th>
				</tr>
			</thead>
		</table>
	</div>
