<!--
	作者:徐秦冬
	日期:2017-12-22 11:14:24
	页面:非套材包材bom版本信息审查查看页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
  	<%@ include file="../../base/meta.jsp" %>

<style type="text/css">
.mui-input-row .mui-input-clear ~ .mui-icon-clear,.mui-input-row .mui-input-speech 
	~ .mui-icon-speech,.mui-input-row .mui-input-password ~ .mui-icon-eye {
	top: 2px;
}

.mui-input-group .mui-input-row {
	height: 25px;
}

.mui-input-row label ~ input,.mui-input-row label ~ select,.mui-input-row label
	~ textarea {
	height: 25px;
}

.mui-input-row .mui-input-clear ~ .mui-icon-clear,.mui-input-row .mui-input-speech 
	~ .mui-icon-speech,.mui-input-row .mui-input-password ~ .mui-icon-eye {
	top: 2px;
}

.mui-input-row label {
	font-family: 'Helvetica Neue', Helvetica, sans-serif;
	padding: 0px 10px 0px 15px;
	line-height: 25px;
}
 
</style>
<script type="text/javascript">
	//JS代码
	var details = ${details};
	
	$(document).ready(function(){
		//$('.easyui-textbox').textbox('textbox').attr('readonly',true);
		for(var a=0;a<details.length;a++){
			add_bomDetail_data(details[a]);
		}
	});
	
	function add_bomDetail_data(r) {
		var _row = {
			"ID" : r.ID,
			"PACKMATERIALCODE" : r.PACKMATERIALCODE,
			"PACKSTANDARDCODE" : r.PACKSTANDARDCODE,
			"PACKMATERIALNAME" : r.PACKMATERIALNAME,
			"PACKMATERIALMODEL" : r.PACKMATERIALMODEL,
			"PACKUNIT" : r.PACKUNIT,
			"PACKMATERIALCOUNT" : r.PACKMATERIALCOUNT,
			"PACKMEMO" : r.PACKMEMO
		};
		$("#dg_bom_detail").datagrid("appendRow", _row);
}
	
	function formatValue(value, row, index){
		if(value==null){
			return "";
		}else{
			//return "<div class='easyui-tooltip' title='"+value+"'>"+value+"</div>";
			return '<div class="easyui-panel easyui-tooltip" title="'+value+'" style="width:100px;padding:5px">'+value+'</div>';
		}
	}
</script>
<div>
	<!--非套材包材bom版本信息表单-->
<table >
			<tr>
				<td class="title">版本号:</td>
				<!--版本号-->
				<td><input type="text" id="packVersion" name="packVersion"
					value="${ftcBcBomVersion.version}" class="easyui-textbox"
					readonly="true"></td>
			</tr>
			<%-- <tr>
				<td class="title">是否启用:</td>
				<!--是否启用，-1不启用，1启用-->
				<td><input type="text" id="packEnabled" name="packEnabled"
					value="${bCBomVersion.packEnabled==-1?'不启用':'启用'}" class="easyui-textbox"
					readonly="true"></td>
			</tr>
			<tr>
				<td class="title">是否默认:</td>
				<!--是否默认，-1不是默认，1默认-->
				<td><input type="text" id="packIsDefault" name="packIsDefault"
					value="${bCBomVersion.packIsDefault==-1?'非默认':'默认'}" class="easyui-textbox"
					readonly="true"></td>
			</tr> --%>
		</table>






<table title="非套材包材bom信息">
			<tr>
				<td class="title" style="text-align: left;">包装标准代码:</td><td id="PACKBOMGENERICNAME" >${ftcBcBom.code}</td>
				<td style="text-align: left;" class="title">适用客户:</td><td id="PACKBOMWIDTH">${consumer.consumerName}</td>
				<td style="text-align: left;" class="title">卷径/cm:</td><td id="PACKBOMTYPE">${ftcBcBomVersion.rollDiameter}</td>
				<td style="text-align: left;" class="title">每托卷数:</td><td id="PACKBOMCODE">${ftcBcBomVersion.rollsPerPallet}</td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">托长/cm:</td><td id="PACKBOMMODEL">${ftcBcBomVersion.palletLength}</td>
				<td style="text-align: left;" class="title">托宽/cm:</td><td id="PACKBOMWEIGHT">${ftcBcBomVersion.palletWidth}</td>
				<td style="text-align: left;" class="title">托高/cm:</td><td id="PACKBOMLENGTH">${ftcBcBomVersion.palletHeight}</td>
				<td style="text-align: left;" class="title">包材重量/kg:</td><td id="PACKBOMRADIUS">${ftcBcBomVersion.bcTotalWeight}</td>
			</tr>
			<tr>
				<th colspan="8">包装要求</th>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">塑料膜要求:</td><td colspan="3" id="PACKBOMCONSUMERID">${ftcBcBomVersion.requirement_suliaomo}</td>
				<td style="text-align: left;" class="title">摆放要求:</td><td colspan="3" id="PACKBOMGENERICNAME">${ftcBcBomVersion.requirement_baifang}</td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">打包带要求:</td><td colspan="3" id="PACKBOMCONSUMERID">${ftcBcBomVersion.requirement_dabaodai}</td>
				<td style="text-align: left;" class="title">标签要求:</td><td colspan="3" id="PACKBOMGENERICNAME">${ftcBcBomVersion.requirement_biaoqian}</td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">小标签要求:</td><td colspan="3" id="PACKBOMCONSUMERID">${ftcBcBomVersion.requirement_xiaobiaoqian}</td>
				<td style="text-align: left;" class="title">卷（箱）标签要求:</td><td colspan="3" id="PACKBOMGENERICNAME">${ftcBcBomVersion.requirement_juanbiaoqian}</td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">托标签要求:</td><td colspan="3" id="PACKBOMCONSUMERID">${ftcBcBomVersion.requirement_tuobiaoqian}</td>
				<td style="text-align: left;" class="title">缠绕要求:</td><td colspan="3" id="PACKBOMGENERICNAME">${ftcBcBomVersion.requirement_chanrao}</td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">其他要求:</td><td colspan="7" id="PACKBOMCONSUMERID">${ftcBcBomVersion.requirement_other}</td>
			</tr>
			
		</table>






<table id="dg_bom_detail" singleSelect="false" title="非套材包材bom明细"
		class="easyui-datagrid" url="" 
		rownumbers="true" fitColumns="true" fit="true" pagination="true">
		<thead>
			<tr>
				<th field="ID" checkbox=true></th>
				<th field="PACKMATERIALCODE" width="15">物料代码</th>
				<th field="PACKSTANDARDCODE" width="15">标准码</th>
				<th field="PACKMATERIALNAME" width="15">材料名称</th>
				<th field="PACKMATERIALMODEL" width="15">规格</th>
				<th field="PACKUNIT" width="15">单位</th>
				<th field="PACKMATERIALCOUNT" width="15">数量</th>
				<th field="PACKMEMO" width="15" formatter="formatValue">备注</th>
			</tr>
		</thead>
		</table>
</div>