<!--
	作者:高飞
	日期:2017-7-31 17:04:13
	页面:裁剪派工单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
    <div id="dlg2" class="easyui-dialog" title="" style="width:600px;height:100%;overflow: hidden;"
            data-options="
            	maximizable:true,
            	border:'none',
            	resizable:true,
            	closed:true,
                iconCls: 'icon-cut',
                buttons: '#dlg-buttons2',
                modal:true
            ">
            <div class="easyui-layout" id="pp" style="width:100%;height:100%;margin:0 auto;" >
		        <div data-options="region:'north',border:false" style="height:238px">
		        	<style type="text/css">
						#cutTaskForm2 .textbox{
							border:none;
						}
						#cutTaskForm2 .textbox-addon{
							display:none;
						}
						#pp .layout-panel{
							border:none;
						}
						#pp .datagrid-header .datagrid-cell, .datagrid-header .datagrid-cell-group{
							color:#1d1d1d;
						}
					</style>
		        	<form style="margin: 0;" id="cutTaskForm2"  autoSearchFunction="false">
								<table width="100%">
										<tr>
											<td colspan="4" style="height:70px;text-align: center;font-size: 20px;font-weight: bold;vertical-align: middle;">
												裁剪派工单
												<div style="position:absolute;top:6px;right:30px;">
													<div id="qr_code"></div>
												</div>
											</td>
										</tr>
										<tr>
											<td class="title">派工单号:</td>
											<!--任务单编号-->
											<td>
												<input type="text" id="ctoCode2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">任务单号:</td>
											<!--发货日期-->
											<td>
												<input type="text" id="taskCode2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">订单号:</td>
											<!--订单号-->
											<td>
												<input type="text" id="orderCode2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">批次号:</td>
											<!--批次号-->
											<td>
												<input type="text" id="batchCode2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">客户简称:</td>
											<!--客户简称-->
											<td>
												<input type="text" id="consumerSimpleName2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">客户大类:</td>
											<!--客户大类-->
											<td>
												<input type="text" id="taskConsumerCategoryX2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>	
										<tr>
											<td class="title">部件名称:</td>
											<!--部件名称-->
											<td>
												<input type="text" id="partName2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">交货日期:</td>
											<!--任务单套数-->
											<td>
												<input type="text" id="deliveryDate2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">机长:</td>
											<!--部件名称-->
											<td>
												<input type="text" id="ctoGroupLeader2" class="easyui-combobox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">班组:</td>
											<!--任务单套数-->
											<td>
												<input type="text" id="ctoGroupName2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">总套数:</td>
											<!--部件名称-->
											<td>
												<input type="text" id="suitCount2" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">派工套数:</td>
											<!--任务单套数-->
											<td>
												<input type="text" id="assignCount2" name="assignSuitCount2" class="easyui-textbox" precision="0" data-options="icons:[],min:1" editable="false" required="true" >
											</td>
										</tr>
							</table>
					</form>
		        	
		        </div>
		        <div data-options="region:'center',border:false,fit:false">
		            <table id="drawingsDg2" class="easyui-datagrid"
						width="100%"
						pagination="false" 
						rownumbers="true" 
						fitColumns="false" 
						fit="false" 
					 >
					 	<thead>
							<tr>
								<th field="partName" width="120" >部件名称</th>
								<th field="fragmentDrawingNo" width="180">图纸号</th>
								<th field="fragmentDrawingVer" width="70">图值版本</th>
								<th field="farbicRollCount" width="60">胚布卷数</th>
								<th field="X" width="113">质检确认</th>
							</tr>
						</thead>
					 </table>
		        </div>
		    </div>
    </div>
    <div id="dlg-buttons2" style="display:none;">
        <a href="javascript:void(0)" iconCls="icon-print" class="easyui-linkbutton" onclick="doPrint()">打印</a>
        <a href="javascript:void(0)" iconCls="icon-cancel"  class="easyui-linkbutton" onclick="javascript:$('#dlg2').dialog('close')">关闭</a>
    </div>

    