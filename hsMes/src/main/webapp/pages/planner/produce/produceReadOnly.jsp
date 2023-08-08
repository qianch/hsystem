<!--
作者:肖文彬
日期:2017-1-18 15:25:48
页面:生产任务查看页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
    const details =${empty details?"[]":details};
</script>
<div style="height:98%">
    <!--生产任务单表单-->
    <form id="producePlanForm" method="post" ajax="true"
          action="<%=basePath %>planner/produce/${empty producePlan.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${producePlan.id}"/>
        <table width="100%">
            <tr>
                <td class="title">车间:</td>
                <!--生产任务单号-->
                <td>
                    <input type="text" id="workshop" name="workshop"
                           value="${producePlan.workshop}" ${empty producePlan.workshop?"":"readonly"}
                           class="easyui-combobox" required="true" readonly="true"
                           data-options="data: [
		                        {value:'编织一车间',text:'编织一车间'},
		                        {value:'编织二车间',text:'编织二车间'},
		                        {value:'编织三车间',text:'编织三车间'},
		                        {value:'裁剪车间',text:'裁剪车间'}],
		                        onSelect:onSelect,icons:[]">
                </td>
                <td class="title">生产任务单号:</td>
                <!--生产任务单号-->
                <td>
                    <input type="text" editable="true" id="producePlanCode" name="producePlanCode"
                           value="${producePlan.producePlanCode}" class="easyui-textbox" required="true" readonly="true"
                           data-options="icons:[]">
                </td>
            </tr>
        </table>
    </form>
    <table id="produceProducts"
           toolbar="#produceSalesOrderToolbar"
           singleSelect="true"
           class="easyui-datagrid"
           fitColumns="false"
           height="300px"
           width="100%"
           data-options="">
        <thead frozen="true">
        <tr>
            <!-- 这是生产计划明细ID -->
            <th field="fromSalesOrderDetailId" checkbox="true"></th>
            <th field="salesOrderCode" width="150">订单号</th>
            <th field="consumerName" width="210">客户名称</th>
            <th field="batchCode" width="110" editor="{type:'textbox',options:{required:true}}">批次号</th>
            <th field="consumerProductName" width="180">客户产品名称</th>
            <th field="factoryProductName" width="150">厂内名称</th>
            <th field="productModel" width="80">产品规格</th>
            <th field="packReq" width="1" hidden="true"></th>
            <th field="procReq" width="1" hidden="true"></th>
            <!-- <th field="productName" width="80" >产品名称</th> -->
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="productWidth" width="80"
                data-options="editor:{type:'numberbox',options:{precision:4,min:1,'icons':[]}}">门幅(mm)
            </th>
            <th field="productWeight" width="80"
                data-options="editor:{type:'numberbox',options:{precision:4,min:1,'icons':[]}}">卷重(kg)
            </th>
            <th field="productLength" width="80"
                data-options="editor:{type:'numberbox',options:{precision:4,min:1,'icons':[]}}">卷长(m)
            </th>
            <th field="processBomCode" width="120">工艺代码</th>
            <th field="processBomVersion" width="120">工艺版本</th>
            <th field="bcBomCode" width="120">包装代码</th>
            <th field="bcBomVersion" width="120">包装版本</th>
            <th field="orderCount" width="110">订单数量(kg/套)</th>
            <th field="requirementCount" width="110"
                data-options="editor:{type:'numberbox',options:{required:true,precision:4,min:1,'icons':[]}}">生产数量(kg/套)
            </th>
            <th field="totalRollCount" width="80"
                data-options="editor:{type:'numberbox',options:{precision:0,min:1,'icons':[]}}">总卷数
            </th>
            <th field="totalTrayCount" width="80"
                data-options="editor:{type:'numberbox',options:{precision:0,min:1,'icons':[]}}">总托数
            </th>
            <th field="deleveryDate" width="100"
                data-options="editor:{type:'datebox',options:{required:true,editable:false}}">出货日期
            </th>
            <th field="deviceCode" width="100" data-options="editor:{type:'textbox'}">机台号</th>
            <th field="comment" width="120" data-options="editor:{type:'textbox'}">备注</th>
        </tr>
        </thead>
    </table>
</div>
