<!--
作者:宋黎明
日期:2016-11-28 11:02:50
页面:文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="../produce/producePlanDetailPrint/producePlanDetailPrint.js.jsp" %>
    <%@ include file="cutDailyPlan.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'east',border:false,split:true" style="width:750px;">
    <div class="easyui-layout" data-options="fit:true,border:false">
        <div data-options="region:'center',border:true,split:true" style="">
            <div id="toolbarCutPrint">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-edit"
                   onclick="print()">打印</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit"
                   onclick="editPlanDetailPrints()">修改打印属性</a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print"
                   onclick="doPrintPart()"> 打印部件条码(个性化) </a>
            </div>
            <!-- <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onclick="check_details()">查看 工艺/包装 需求</a> -->
            <table id="dailyDetails" toolbar="#toolbarCutPrint" width="100%" singleSelect="true" title="裁剪排产明细"
                   class="easyui-datagrid" pagination="false" rownumbers="true" fitColumns="false" fit="true"
                   data-options="
  	 					onClickRow:dailyRowClick,
  	 					onLoadSuccess:dailyLoadSuccess,
  	 					view: detailview,
			                detailFormatter:function(index,row){
			                    return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
			                },
			                onExpandRow: function(index,row){
			                    var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
			                    ddv.datagrid({
			                        url:path+'bom/plan/tc/ver/parts/' + row.FROMSALESORDERDETAILID + '/'+row.PRODUCEPLANDETAILID,
			                        fitColumns:true,
			                        singleSelect:true,
			                        rownumbers:true,
			                        loadMsg:'',
			                        height:'auto',
			                        rowStyler:danxiangbu,
			                        width:450,
			                        columns:[ [ {
										field : 'partName',
										title : '部件名称',
										width : 20
									}, {
										field : 'planPartCount',
										title : '计划数量',
										width : 15
									},{
										field : 'partCount',
										title : '订单数量',
										width : 15
									},  {
										field : 'partBomCount',
										title : 'BOM数量',
										width : 15
									} ] ],
			                        onResize:function(){
			                            setTimeout(function(){
			                                $('#dailyDetails').datagrid('fixDetailRowHeight',index);
			                                $('#dailyDetails').datagrid('fixRowHeight',index);
			                            },0);
			                        },
			                        onLoadSuccess:function(){
			                            setTimeout(function(){
			                                $('#dailyDetails').datagrid('fixDetailRowHeight',index);
			                                $('#dailyDetails').datagrid('fixRowHeight',index);
			                            },0);
			                        }
			                    });
			                    $('#dailyDetails').datagrid('fixDetailRowHeight',index);
			                     $('#dailyDetails').datagrid('fixRowHeight',index);
			                }">
                <thead>
                <tr>
                    <th field="PLANCODE" sortable="true" width="120">生产计划单号</th>
                    <th field="SALESORDERCODE" sortable="true" width="120">销售订单号</th>
                    <th field="PRODUCTNAME" sortable="true" width="120">产品名称</th>
                    <th field="CONSUMERPRODUCTNAME" width="120">客户产品名称</th>
                    <th field="PRODUCTMODEL" sortable="true" width="100">产品规格</th>
                    <th field="BATCHCODE" sortable="true" width="80">批次号</th>
                    <th field="DELEVERYDATE" sortable="true" width="70">出货时间</th>
                    <th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
                    <th field="PROCESSBOMVERSION" width="80">工艺版本</th>
                    <th field="BCBOMCODE" width="130" styler="bvStyler">包装代码</th>
                    <th field="BCBOMVERSION" width="80">包装版本</th>
                </tr>
                </thead>
            </table>
        </div>
        <div data-options="region:'south',border:false,split:true" style="height:300px;">
            <div class="easyui-layout" data-options="region:'layout',border:false,fit:true">
                <div id="commentPanel" title="备注" data-options="region:'south',border:true"
                     style="height:100px;padding:10px;">

                </div>
                <div id="userCountPanel" title="人员分配信息" data-options="region:'center',border:true"
                     style="padding:10px;">
                    <table id="partCounts" class="easyui-datagrid" style="width:100%;" fitColumns="true">
                        <thead>
                        <tr>
                            <th field="part" width="80">部件名称</th>
                            <th field="user" width="80" formatter="cutDailyUsersCounts">人员数量</th>
                            <!-- <th field="partBomCount"  width="80">BOM数量</th> -->
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div data-options="region:'center',border:true,split:true" style="">
    <div id="toolbar">
        <form id="cutDailyPlanSearchForm" autoSearchFunction="false">
            日期:<input type="text" class="easyui-datebox" name="filter[produceDate]" style="width:120px;"
                      data-options="onSelect:filter">
            批号：<input type="text" class="easyui-textbox" name="filter[batchCode]" like="true" style="width:120px;"
                      data-options="onSelect:filter">
            产品名称：<input type="text" class="easyui-textbox" name="filter[productName]" like="true" style="width:120px;"
                        data-options="onSelect:filter">
            部件名称：<input type="text" class="easyui-textbox" name="filter[partNames]" like="true" style="width:120px;"
                        data-options="onSelect:filter">
            车间:<input type="text" id="workshop" class="easyui-combobox" style="width:130px;" name="filter[workShopCode]"
                      value="裁剪一车间"
                      data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=cut'">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" readOnly="true"
               onclick="filter()"> 搜索 </a>
        </form>
        <hr>
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="audit" name="ids"/>
            <jsp:param value="view" name="ids"/>
            <jsp:param value="reload1" name="ids"/>
            <jsp:param value="export" name="ids"/>

            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="icon-tip" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-print" name="icons"/>

            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="提交审核" name="names"/>
            <jsp:param value="查看审核" name="names"/>
            <jsp:param value="关闭计划" name="names"/>
            <jsp:param value="导出" name="names"/>

            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="doAudit()" name="funs"/>
            <jsp:param value="view()" name="funs"/>
            <jsp:param value="reloadAudit()" name="funs"/>
            <jsp:param value="_export()" name="funs"/>
        </jsp:include>
    </div>

    <table id="dg" singleSelect="true" title="裁剪排产列表" class="easyui-datagrid" url="<%=basePath %>cutDailyPlan/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="onClickRow:dgRowClick,onLoadSuccess:dgLoadSuccess,rowStyler:formatterIsClosed">
        <thead>
        <tr>
            <th field="ID" checkbox="true" formatter="formatterIsClosed"></th>
            <th field="DATE" sortable="true" width="15">时间</th>
            <th field="USERNAME" sortable="true" width="15">操作人</th>
            <th field="BATCHCODE" sortable="true" width="15">批号</th>
            <th field="PRODUCTNAME" sortable="true" width="15">产品名称</th>
            <th field="PARTNAMES" sortable="true" width="15">部件名称</th>
            <!-- <th field="AUDITSTATE" sortable="true" width="15"  formatter="formatterState">审核状态</th> -->
        </tr>
        </thead>
    </table>
</div>
</body>
