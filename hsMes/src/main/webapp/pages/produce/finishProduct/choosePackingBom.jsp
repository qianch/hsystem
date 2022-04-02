<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<title>选择包装BOM</title>
<%@ include file="../../base/jstl.jsp" %>
<script type="text/javascript">
var bcBomtreeUrl = path + "finishProduct/listBcBom";
	 $(function(){
		$('#bcBomTree').tree({
			url:bcBomtreeUrl,
			onDblClick:function(node){
				dblClickBzVersion(node);
				//console.log($('#tcBomTree').tree('getParent'));
			},
			onSelect : function(node) {
			}
		});
	});
	 
	//树列表收缩
	function collapse() {
		var bcNode = $('#bcBomTree').tree('getSelected');
		$('#bcBomTree').tree('collapse', bzNode.target);
	}

	//树列表展开
	function expand() {
		var bcNode = $('#bcBomTree').tree('getSelected');
		$('#bcBomTree').tree('expand', bcNode.target);
	}
	
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'east',collapsible:false" title="包材BOM">
   		<form id="bc_BomForm"  autoSearchFunction="false">
			<label>搜索：</label>
			<input id="searchBc" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchBcInfo" editable="true" data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');}}]">
		</form>
    	<ul id="bcBomTree" class="easyui-tree" ></ul>
    </div>
</div>

