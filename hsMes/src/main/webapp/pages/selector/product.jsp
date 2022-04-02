<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<title>选择产品信息</title>
<%@ include file="../base/jstl.jsp"%>

<script type="text/javascript">
	//添加成品信息
	var _common_product_addUrl=path+"finishProduct/add";
	//选择客户信息
	var chooseConsumer=path+"selector/consumer";
	//选择套材/非套材BOM信息
	var chooseBomUrl=path+"finishProduct/chooseBom";
	//选择包装BOM信息
	var choosePackingBomUrl=path+"selector/packingBom";

	var consumerWindow=null;
	var bomWindow=null;
	var packingBomWindow=null;
	var productIsTc=null;

	//查询
	function _common_product_filter() {
		EasyUI.grid.search("_common_product_dg", "_common_product_dg_form");
	}
	
	var _common_product_dgenableFilter=false;
	$(document).ready(function() {
		var autoSearch=null;
		var autoSearchFunction;
		$(function() {
			$(".textbox-text").keyup(function(){
				$("input[name='"+$(this).parent().prev().attr("textboxname")+"']").val($(this).val());
				clearTimeout(autoSearch);
				autoSearchFunction=$(this).parent().parent().attr("autoSearchFunction");
				if(autoSearchFunction){
					autoSearch=setTimeout(window[autoSearchFunction](), 500);
				}else{
					autoSearch=setTimeout(_common_product_filter, 500);
				}
			});
		});
		$('#_common_product_dg').datagrid({
			onDblClickRow : function(index, row) {
				if (typeof _common_product_dbClickRow === "function") {
					if (rows.CARRIERCODE.indexOf("04.03")!=-1 && rows.CARRIERWEIGHT == 0) {				
						Tip.warn("请维护纸管"+rows.CARRIERCODE+"重量");
						return;
					}
					_common_product_dbClickRow(index, row);
				} else {
					if (window.console) {
						console.log("没有为产品选择界面提供_common_product_dbClickRow方法，参数为index,row");
					}
				}
			},
			onLoadSuccess : function(data) {
				if(!_common_product_dgenableFilter){
					var cs=[{'field':'CONSUMERCODE','type':'textbox'}, {'field':'CONSUMERNAME','type':'textbox'}, {'field':'FACTORYPRODUCTNAME','type':'textbox'}, {'field':'ISCOMMON','type':'textbox'}, {'field':'AUDITSTATE','type':'textbox'}, {'field':'CONSUMERPRODUCTNAME','type':'textbox'}, {'field':'PRODUCTMODEL','type':'textbox'}, {'field':'PRODUCTWIDTH','type':'textbox'}, {'field':'PRODUCTROLLLENGTH','type':'textbox'}, {'field':'PRODUCTROLLWEIGHT','type':'textbox'}, {'field':'PRODUCTPROCESSCODE','type':'textbox'}, {'field':'PRODUCTPROCESSBOMVERSION','type':'textbox'}, {'field':'PRODUCTCONSUMERBOMVERSION','type':'textbox'},{'field':'PRODUCTPACKAGINGCODE','type':'textbox'}, {'field':'PRODUCTPACKAGEVERSION','type':'textbox'}, {'field':'PRODUCTMEMO','type':'textbox'},
					        {'field':'PRODUCTISTC','type':'combobox',options:{
					        	icons:[],
					        	panelHeight:'auto',
					        	data:[{value:"",text:"全部"},{value:"套材",text:"套材"},{value:"非套材",text:"非套材"}],
					        	onChange:function(nv,ov){
					        		if (isEmpty(nv)){
					        			$('#_common_product_dg').datagrid('removeFilterRule', 'PRODUCTISTC');
			                        }else{
			                        	$('#_common_product_dg').datagrid("addFilterRule",{field:'PRODUCTISTC',op:'equal',value:nv});
			                        }
					        		$('#_common_product_dg').datagrid("doFilter");
					        	}
					        }}
					   ];
					$(this).datagrid("enableFilter",cs);
					_common_product_dgenableFilter=true;
				}
				
				if (typeof _common_product_onLoadSuccess === "function") {
					_common_product_onLoadSuccess(data);
				} else {
					if (window.console) {
						console.log("未定义产品选择界面加载完成的方法：_common_product_onLoadSuccess(data)");
					}
				}
			}

		});
	});
	
	//添加成品信息
	var _common_product_add = function() {
		var wid = Dialog.open("添加", 800, 295,_common_product_addUrl, [
			EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("finishProductForm",_common_product_addUrl, function(data){
				_common_product_filter();
				if(Dialog.isMore(wid)){
					Dialog.close(wid);
					add();
				}else{
					Dialog.close(wid);
				}
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ],function(){Dialog.more(wid);}
		);
	};
	
	//选择客户
	function ChooseConsumer(){
		consumerWindow = Dialog.open("选择客户", 850, 450,chooseConsumer , [ 
	   	    EasyUI.window.button("icon-save", "确认", function() {
	   	    	var _common_product_r=EasyUI.grid.getOnlyOneSelected("_common_consumer_dg");
	   	    	$('#productConsumerCode').searchbox('setValue',_common_product_r.CONSUMERCODE);
	   	    	$('#productConsumerName').searchbox('setValue',_common_product_r.CONSUMERNAME);  	  
	   	    	JQ.setValue('#productConsumerId', _common_product_r.ID);
	   	    	Dialog.close(consumerWindow);
	   	}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
	   		Dialog.close(consumerWindow);
	   	}) ]);
	}

	//选择客户双击事件
	function _common_consumer_dbClickRow(index, row){
		$('#productConsumerCode').searchbox('setValue',row.CONSUMERCODE);
	   	$('#productConsumerName').searchbox('setValue',row.CONSUMERNAME);  	  
	   	JQ.setValue('#productConsumerId', row.ID);
	   	Dialog.close(consumerWindow);
	}

	//选择套材/非套材工艺BOM窗口
	function chooseBom(){
		var productIsTc1=$("#productIsTc1").attr("checked");
		var productIsTc2=$("#productIsTc2").attr("checked");
		if(productIsTc1=='checked'){
			productIsTc=1;
		}else{
			productIsTc=2;
		}
		bomWindow = Dialog.open("", 400, 450,chooseBomUrl+"?productIsTc="+productIsTc, [ 
	       	    EasyUI.window.button("icon-save", "确认", function() {
	       	    	var tcNode=$('#tcBomTree').tree('getSelected');
	       	    	var ftcNode=$('#ftcBomTree').tree('getSelected');
		      	 	if(tcNode==null){
		      	 		//非套材
		      	 		var parentFtcInfo=$('#ftcBomTree').tree('getParent',ftcNode.target);
		      	 		if(parentFtcInfo==null){
		       	    		Tip.warn("请选择对应的版本信息");
		       	    		return;
		       	    	}
		      	 		$('#productProcessBom').searchbox('setValue',parentFtcInfo.attributes.FTCPROCBOMCODE);
			      	 	$('#productProcessBomVersion').textbox('setValue',ftcNode.text);
		      	 	}else{
		      	 		//套材
		      	 		var parentTcInfo=$('#tcBomTree').tree('getParent',tcNode.target);
		      	 		if(parentTcInfo==null){
		       	    		Tip.warn("请选择对应的版本信息");
		       	    		return;
		       	    	}
		      	 		$('#productProcessBom').searchbox('setValue',parentTcInfo.attributes.TCPROCBOMCODE);
			      	 	$('#productProcessBomVersion').textbox('setValue',tcNode.text);
		      	 	}
		      	 	
	       	    	Dialog.close(bomWindow);
	       	}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
	       		Dialog.close(bomWindow);
	       	}) ]);
	}

	//选择包装工艺BOM窗口
	function choosePackingBom(){
		packingBomWindow = Dialog.open("",400, 450,choosePackingBomUrl , [ 
	       	    EasyUI.window.button("icon-save", "确认", function() {
		       		var node = $('#bcBomTree').tree('getSelected');
	       	    	var parentInfo=$('#bcBomTree').tree('getParent',node.target);
	       	    	if(parentInfo==null){
	       	    		Tip.warn("请选择对应的版本信息");
	       	    		return;
	       	    	}
		       	 	$('#productPackage').searchbox('setValue',parentInfo.attributes.PACKBOMCODE);
		       	 	$('#productPackageVersion').textbox('setValue',node.text);
	       	    	Dialog.close(packingBomWindow);
	       	}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
	       		Dialog.close(packingBomWindow);
	       	}) ]);
	}

	//双击选中套材BOM tree
	function dblClickTcVersion(node){
		var parentInfo=$('#tcBomTree').tree('getParent',node.target);
		$('#productProcessBom').searchbox('setValue',parentInfo.attributes.TCPROCBOMCODE);
		$('#productProcessBomVersion').textbox('setValue',node.text);
		Dialog.close(bomWindow);
	}

	//双击选中非套材BOM tree
	function dblClickFtcVersion(node){
		var parentInfo=$('#ftcBomTree').tree('getParent',node.target);
		$('#productProcessBom').searchbox('setValue',parentInfo.attributes.FTCPROCBOMCODE);
		$('#productProcessBomVersion').textbox('setValue',node.text);
		Dialog.close(bomWindow);
	}

	//双击选中包装BOM tree
	function dblClickBcVersion(node){
		var parentInfo=$('#bcBomTree').tree('getParent',node.target);
		$('#productPackage').searchbox('setValue',parentInfo.attributes.PACKBOMCODE);
		$('#productPackageVersion').textbox('setValue',node.text);
		Dialog.close(packingBomWindow);
	}

	function formatterIsTc(value, row){
		if(value=='1'){
			return "套材";
		}else if(value=='2'){
			return "非套材";
		}else{
			return "胚布";
		}
	}
	
	function _commons_product_dg_onBeforeLoad(){
		if (typeof _commons_product_dg_onBeforeLoad_callback == "function") {
			return _commons_product_dg_onBeforeLoad_callback();
		} else {
			if (window.console) {
				console.log("未定义产品选择界面加载完成的方法：_commons_product_dg_onBeforeLoad_callback()");
			}
			return true;
		}
	}
	
	//产品属性
	function formatterIscommon(value, row, index){
		if(value==0){
			return "试样";
		}else if(value==1){
			return "常规";
		}
	}
	//审核状态
	function formatterReviewState(val, row, index) {
		return auditStateFormatter(row.AUDITSTATE);
	}
	
</script>

<div id="_common_product_toolbar">
<%-- 	<jsp:include page="../base/toolbar.jsp">
		<jsp:param value="_common_product_add" name="ids"/>
		<jsp:param value="icon-add" name="icons"/>
		<jsp:param value="增加" name="names"/>
		<jsp:param value="_common_product_add()" name="funs"/>
	</jsp:include> --%>
	<div style="border-top:1px solid #DDDDDD">
		<form action="#" id="_common_product_dg_form" style="display:none" autoSearchFunction="false">
			<input type="hidden" id="commonProductIds" in="true" name="filter[salesCode]">
			<input type="hidden" id="consumerId" name="filter[consumerId]">
			<input type="hidden" id="auditState" name="filter[auditState]">
			<input type="hidden" id="isCommon" name="filter[isCommon]">
			<input type="hidden" id="productIsTc" name="filter[productIsTc]" in="true" value="${empty isShow ?'-1':'1,2'}"><!-- 标识是否需要显示裁剪添加的胚布 -->
		<!-- 	客户代码：<input type="text" name="filter[consumerCode]" like="true" class="easyui-textbox">
			客户名称：<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox"> -->
			厂内名称：<input type="text" name="filter[factoryName]" like="true" class="easyui-textbox">
			产品型号：<input id="_common_productModel" type="text" name="filter[productModel]" like="true" class="easyui-textbox">&nbsp;&nbsp;&nbsp;
			<a id="_common_product_searchButton" href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="_common_product_filter()">
				搜索
			</a><br>
			显示作废：<input type="text" name="filter[old]"  class="easyui-combobox"
					 data-options="data: [
		                        {value:'1',text:'已作废'}],onSelect:_common_product_filter">
		        门&nbsp;&nbsp;幅：<input type="text" name="filter[productWidth]" like="true" class="easyui-textbox">
		</form>
	</div>
</div>
<table id="_common_product_dg" width="100%" height="98%"  idField="ID" data-options="onBeforeLoad:_commons_product_dg_onBeforeLoad,pageSize:10000,nowrap:false,rowStyler:productStyler"  class="easyui-datagrid" url="<%=basePath %>finishProduct/listAll" toolbar="#_common_product_toolbar" pagination="false" rownumbers="true" fitColumns="false" fit="true">
	<thead frozen="true">
		<tr>
			<th field="I" checkbox=true ></th>
			<th field="ID" width="90" >ID</th>
			<th field="CONSUMERCODE" width="60" sortable="true" ${empty showCode?'':'hidden'}>客户代码</th>
			<th field="CONSUMERNAME" width="200" sortable="true">客户名称</th>
			<th field="PRODUCTISTC" width="60" sortable="true" formatter="formatterIsTc">类型</th>
			<th field="FACTORYPRODUCTNAME" width="140" sortable="true">厂内名称</th>
			<th field="ISCOMMON" sortable="true" width="50" formatter="formatterIscommon">产品属性</th>
			<th field="AUDITSTATE" sortable="true" width="100" formatter="formatterReviewState">审核状态</th>
		</tr>
	</thead>
	<thead>
		<tr>
			<th field="CONSUMERPRODUCTNAME" width="150" sortable="true">客户产品名称</th>
			<th field="PRODUCTMODEL" width="130" sortable="true">产品型号</th>
			<th field="PRODUCTWIDTH" width="70" sortable="true">门幅(mm)</th>
			<th field="PRODUCTROLLLENGTH" width="70" sortable="true">定长(m)</th>
			<th field="PRODUCTROLLWEIGHT" width="70" sortable="true">定重(kg)</th>
			<th field="PRODUCTPROCESSCODE" sortable="true" width="200" formatter="bomVersionView" styler="vStyler">工艺标准代码</th>
			<th field="PRODUCTPROCESSBOMVERSION" sortable="true" width="100">工艺标准版本</th>
			<th field="PRODUCTCONSUMERBOMVERSION" sortable="true" width="100">客户版本号</th>
			<th field="PRODUCTPACKAGINGCODE" sortable="true" width="200">包装标准代码</th>
			<th field="PRODUCTPACKAGEVERSION" sortable="true" width="100" >包装标准版本</th>
			<th field="PRODUCTMEMO" width="300">备注</th>
		</tr>
	</thead>
</table>
