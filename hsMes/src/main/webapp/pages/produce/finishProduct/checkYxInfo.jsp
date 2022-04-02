<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>

var yxdates = ${yxdates};


</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<table id="weight_dg" singleSelect="true" title="" class="easyui-datagrid"   toolbar="#category_toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  >
			<thead>
				<tr>
					<th field="TCPROCBOMNAME" sortable="true" width="15">BOM名称</th>
					<th field="TCPROCBOMCODE" sortable="true" width="15">BOM代码</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
