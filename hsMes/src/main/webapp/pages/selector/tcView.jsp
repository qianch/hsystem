<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../base/jstl.jsp"%>

<script>
	//查询版本下的部件
	var find = path + "bom/tc/findParts";
	var partsD = path + "bom/tcBomVersionPartsDetail/list";
	$(function(){
		var id=$("#id").val();
		$('#partsTree').tree({
					url : find+"?id="+id,
					method : 'get',
					animate : true,
					onClick : function(node) {
							$("#dddg").datagrid({
										url : partsD + "?all=1&filter[ids]=" + node.id
							});
					}
				});
	});
	
</script>
	<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',collapsible:false" title="" height="150px">
	<input type="hidden"  id="id" name="" value="${tcBomVersion.id}">
    	<table width="100%" title="版本列表">
				<tr>
					<td class="title">版本号:</td>
					<!--版本号-->
					<td>
						<input type="text" data-options="required:true,validType:'length[1,20]','icons':{}" editable=false id="tcProcBomVersionCode" name="tcProcBomVersionCode" value="${tcBomVersion.tcProcBomVersionCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">是否启用:</td>
					<!--是否启用-->
					<td>
						<input type="text" id="tcProcBomVersionEnabled" editable=false name="tcProcBomVersionEnabled" value="${tcBomVersion.tcProcBomVersionEnabled}" class="easyui-combobox" readOnly="true" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'启用'},
	                        {value:'-1',text:'未启用'}
                    	],'icons':{}" >
					</td>
				</tr>
				<tr>
					<td class="title">是否默认:</td>
					<!--是否默认-->
					<td>
						<input type="text" id="tcProcBomVersionDefault" disable=false readOnly="true" name="tcProcBomVersionDefault" value="${tcBomVersion.tcProcBomVersionDefault}" class="easyui-combobox"  panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'是'},
	                        {value:'-1',text:'否'}
                    	],'icons':{}"  >
					</td>
				</tr>
		</table>
    </div>
    <div data-options="region:'west',collapsible:false" title="部件信息" width=200px>
    	<ul id="partsTree" class="easyui-tree" ></ul>
    </div>
    <div data-options="region:'center',collapsible:false" title="部件明细信息" >
    	<table singleSelect="false" id="dddg"  style="width:100%;" class="easyui-datagrid" url=""   rownumbers="true"  >
			<thead>
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="PRODUCTPROCESSCODE" sortable="true" width="15" >工艺代号</th>
					<th field="PRODUCTPROCESSBOMVERSION" sortable="true" width="15" >工艺版本</th>
					<th field="PRODUCTPACKAGINGCODE" sortable="true" width="15" >包装代号</th>
					<th field="PRODUCTPACKAGEVERSION" sortable="true" width="15" >包装版本</th>
					<th field="PRODUCTMODEL" sortable="true" width="15" >胚布规格</th>
					<th field="PRODUCTWIDTH" sortable="true" width="15" >胚布门幅</th>
					<th field="PRODUCTROLLLENGTH" sortable="true" width="15" >胚布米长</th>
					<th field="TCPROCBOMFABRICCOUNT" sortable="true" width="15">数量(卷/套)</th>
				</tr>
			</thead>
		</table>
    </div>
</div>
		
