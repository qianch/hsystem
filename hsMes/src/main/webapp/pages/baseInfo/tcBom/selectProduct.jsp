<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<title>选择产品信息</title>
<%@ include file="../../base/jstl.jsp"%>
<script type="text/javascript">
	//--------------------------------------------------------------------------------------

	//添加成品信息
	var find = path + "bom/tc/findFtc";
	//添加成品信息
	var _common_product_addUrl = path + "finishProduct/add";
	var _common_product_editUrl = path + "finishProduct/edit";
	//选择客户信息
	var chooseConsumer = path + "selector/consumer";
	//选择套材/非套材BOM信息
	var chooseBomUrl = path + "finishProduct/chooseBom";
	//选择包装BOM信息
	var choosePackingBomUrl = path + "selector/packingBom";
	var findConsumer = path + "bom/tc/addConsum11er";
	var findProduct = path + "bom/tc/findProduct";
	var consumerWindow = null;
	var bomWindow = null;
	var packingBomWindow = null;
	var productIsTc = null;
	var consumerName = null;
	var consumerCode = null;


	//查询
	function filter() {
		EasyUI.grid.search("dg","partProductSearchForm");
	}

	function ChooseConsumer1() {
		consumerWindow = Dialog.open("选择客户", 850, 450, findConsumer, [ EasyUI.window.button("icon-save", "确认", function() {
			var _product_r = EasyUI.grid.getOnlyOneSelected("dddg");
			$('#productConsumerCode').searchbox('setValue', _product_r.CONSUMERCODE);
			$('#productConsumerName').searchbox('setValue', _product_r.CONSUMERNAME);
			JQ.setValue('#productConsumerId', _product_r.ID);
			Dialog.close(consumerWindow);
		}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
			Dialog.close(consumerWindow);
		}) ]);
	}

	//选择套材/非套材工艺BOM窗口
	function chooseBom() {
		var productIsTc1 = $("#productIsTc1").attr("checked");
		var productIsTc2 = $("#productIsTc2").attr("checked");
		if (productIsTc1 == 'checked') {
			productIsTc = 1;
		} else {
			productIsTc = 2;
		}
		bomWindow = Dialog.open("选择套材/非套材BOM", 300, 400, chooseBomUrl + "?productIsTc=" + productIsTc, [ EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
			Dialog.close(bomWindow);
		}) ]);
	}
	//选择包装工艺BOM窗口
	function choosePackingBom() {
		packingBomWindow = Dialog.open("选择包装BOM", 300, 400, choosePackingBomUrl, [ EasyUI.window.button("icon-save", "确认", function() {
			var node = $('#bzBomTree').tree('getSelected');
			var parentInfo = $('#bzBomTree').tree('getParent', node.target);
			$('#productPackage').searchbox('setValue', parentInfo.attributes.PACKBOMCODE);
			$('#productPackageVersion').textbox('setValue', node.text);
			Dialog.close(packingBomWindow);
		}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
			Dialog.close(packingBomWindow);
		}) ]);
	}

	function _common_product_filter() {
		var node = $('#TcBomTree').tree('getSelected');
		/* JQ.ajaxPost(find, {
			id : node.id
		}, function(data) {
			$("#dg").datagrid("loadData", data);
		}) */
	}

	//双击选中套材BOM tree
	function dblClickTcVersion(node) {
		var parentInfo = $('#tcBomTree').tree('getParent', node.target);
		$('#productProcessBom').searchbox('setValue', parentInfo.attributes.TCPROCBOMCODE);
		$('#productProcessBomVersion').textbox('setValue', node.text);
		Dialog.close(bomWindow);
	}

	//双击选中非套材BOM tree
	function dblClickFtcVersion(node) {
		var parentInfo = $('#ftcBomTree').tree('getParent', node.target);
		$('#productProcessBom').searchbox('setValue', parentInfo.attributes.FTCPROCBOMCODE);
		$('#productProcessBomVersion').textbox('setValue', node.text);
		Dialog.close(bomWindow);
	}

	//双击选中包装BOM tree
	function dblClickBcVersion(node) {
		var parentInfo = $('#bcBomTree').tree('getParent', node.target);
		$('#productPackage').searchbox('setValue', parentInfo.attributes.PACKBOMCODE);
		$('#productPackageVersion').textbox('setValue', node.text);
		Dialog.close(packingBomWindow);
	}
	function selectProduct() {
		var wid = Dialog.open("添加", 600, 260, findProduct, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("finishProductForm", _common_product_addUrl, function(data) {
				_common_product_filter();
				if (Dialog.isMore(wid)) {
					Dialog.close(wid);
					add();
				} else {
					Dialog.close(wid);
				}
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ], function() {
			Dialog.more(wid);
		});
	}
	$(function() {
		var node = $('#TcBomTree').tree('getSelected');
		log($('#TcBomTree').tree('getSelected').id);
		$('#partProductId').hide();
		// JQ.ajaxPost(find, {
		// 	filterRules: [node.id],
		// 	id : node.id
		// }, function(data) {
		// 	log(data);
		// 	$("#dg").datagrid("loadData", data);
		// });
		$('#partProductId').val(node.id);

		// $('#partProductId').textbox({
		// 	value:node.id
		// });
		// $('#partProductId').textbox('setValue',node.id);
		var dg = $('#dg').datagrid({
			queryParams:{id:node.id},
			onDblClickRow : function(index, row) {
				_common_product_dbClickRow1(index, row);
			},
			onLoadSuccess : function(data) {
				// enableDgFilter();
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
	var boolean = false;
	function enableDgFilter() {
		if (!boolean) {
			$("#dg").datagrid('enableFilter');
		}
		boolean = true;
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

	function _commons_product_dg_onBeforeLoad() {
		if (typeof _commons_product_dg_onBeforeLoad_callback === "function") {
			return _commons_product_dg_onBeforeLoad_callback();
		} else {
			if (window.console) {
				console.log("未定义产品选择界面加载完成的方法：_commons_product_dg_onBeforeLoad_callback()");
			}
			return true;
		}
	}

	function copy() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		var wid = Dialog.open("复制胚布信息", 700, 295, _common_product_editUrl + "?id=" + r.ID+"&copy=true", [
				EasyUI.window.button("icon-save", "保存复制", function() {
					$("input[name='id']").val("");
					EasyUI.form.submit("finishProductForm", _common_product_addUrl, function(data) {
						_common_product_filter();
						Dialog.close(wid);
					});
				}), EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(wid);
				}) ],function(){
					Dialog.max(wid);
			}
		);
	}
	
	function idFormatter(v,r,i){
		return r.ID;
	}

</script>

<div id="_common_product_toolbar1">
	<form action="#" id="partProductSearchForm" autoSearch="true" autoSearchFunction="true">
		<div style="display: none">
			<input type="text" id="partProductId" name="filter[id]" class="easyui-textbox" style="width:250px;">
		</div>
		产品ID：<input type="text" name="filter[XID]" like="true" class="easyui-textbox" style="width:250px;">
		物料代码：<input type="text" name="filter[materielCode]" like="true" class="easyui-textbox" style="width:150px;">
		客户代码：<input type="text" name="filter[consumerCode]" like="true" class="easyui-textbox" style="width:150px;">
		客户名称：<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox" style="width:230px;"></br>
		客户产品名称：<input type="text" name="filter[consumerProductName]" like="true" class="easyui-textbox" style="width:230px;">
		工艺名称：<input type="text" name="filter[productProcessName]" like="true" class="easyui-textbox" style="width:230px;">
		工艺代码：<input type="text" name="filter[productProcessCode]" like="true" class="easyui-textbox" style="width:230px;"></br>
		工艺版本：<input type="text" name="filter[productProcessbomVersion]" like="true" class="easyui-textbox" style="width:230px;">
		产品型号：<input type="text" name="filter[productModel]" like="true" class="easyui-textbox" style="width:230px;">
		<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
			搜索
		</a>
	</form>
	<div style="border-top:1px solid #DDDDDD">
		<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="platform-copy" onclick="copy()">复制</a>
	</div>
</div>

<table id="dg" idField="ID" style="width:100%" singleSelect="false" class="easyui-datagrid"
	   url="${path}bom/tc/findFtc" toolbar="#_common_product_toolbar1" pagination="true" remoteSort="false" rownumbers="true" fitColumns="true" fit="true" remoteFilter="true">
	<thead>
		<tr>
			<th field="ID" checkbox=true></th>
			<th field="XID" formatter="idFormatter">产品ID</th>
			<th field="MATERIELCODE" sortable="true">物料代码</th>
			<th field="CONSUMERCODE" sortable="true">客户代码</th>
			<th field="CONSUMERNAME" sortable="true">客户名称</th>
			<th field="CONSUMERPRODUCTNAME" sortable="true">客户产品名称</th>
			<th field="PRODUCTPROCESSNAME" sortable="true">工艺名称</th>
			<th field="PRODUCTWIDTH" sortable="true">门幅(mm)</th>
			<th field="PRODUCTROLLLENGTH" sortable="true">米长(m)</th>
			<th field="PRODUCTROLLWEIGHT" sortable="true">定重(kg)</th>
			<th field="PRODUCTPROCESSCODE" sortable="true">工艺代码</th>
			<th field="PRODUCTPROCESSBOMVERSION" sortable="true">工艺版本</th>
			<th field="PRODUCTMODEL" sortable="true">产品型号</th>
			<th field="PRODUCTISTC" sortable="true" formatter="formatterIsTc">套材</th>
		</tr>
	</thead>
</table>
