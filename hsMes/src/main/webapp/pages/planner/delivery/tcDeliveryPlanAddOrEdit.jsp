<!--
作者:徐波
日期:2016-11-2 9:30:07
页面:出货计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
</style>
<script type="text/javascript">
    const orderDatas = ${orderDatas};
    const productDatas = ${productDatas};
</script>
<div>
    <!--出货计划表单-->
    <form id="deliveryPlanForm" style="margin-bottom: 0px;" method="post" ajax="true"
          action="<%=basePath %>planner/deliveryPlan/${empty deliveryPlan.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${deliveryPlan.id}"/>
        <input type="hidden" name="auditState" value="${deliveryPlan.auditState}"/>
        <input type="hidden" name="deliveryBizUserId" value="${userId }"/>
        <table width="100%">
            <tr>
                <td class="title">出货单编号:</td>
                <!--条码-->
                <td>
                    <select id="deliveryType" onchange="getSerial()">
                        <option value="NX">内销</option>
                        <option value="WX">外销</option>
                    </select>
                    <input type="text" id="deliveryCode" name="deliveryCode" value="${deliveryPlan.deliveryCode}"
                           class="easyui-textbox" required="true">
                </td>
                <td class="title">出货时间:</td>
                <!--出货时间-->
                <td>
                    <input type="text" id="deliveryDate" name="deliveryDate" value="${deliveryPlan.deliveryDate}"
                           class="easyui-datebox" required="true"></td>
                <td class="title">包装方式:</td>
                <!--出货人-->
                <td>
                    <input type="text" id="packagingType" name="packagingType" value="${deliveryPlan.packagingType}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">要货公司:</td>
                <!--物流公司-->
                <input type="hidden" name="consumerId" id=" consumerId" value="">
                <td>
                    <input type="text" id="deliveryTargetCompany" name="deliveryTargetCompany"
                           value="${deliveryPlan.deliveryTargetCompany}" required="true" class="easyui-searchbox"
                           data-options="searcher:selectConsumer"></td>
                <td class="title">条码:</td>
                <!--条码-->
                <td>
                    <input type="text" id="barcode" name="barcode" value="${deliveryPlan.barcode}"
                           class="easyui-textbox"></td>
                <td class="title">物流公司:</td>
                <!--物流公司-->
                <td>
                    <input type="text" id="logisticsCompany" name="logisticsCompany"
                           value="${deliveryPlan.logisticsCompany}" class="easyui-textbox"></td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <td class="title">注意事项:</td>
                <!--注意事项-->
                <td>
                    <textarea id="attention" name="attention" style="width:100%;height:50px;"
                              placeholder="1000字以内">${deliveryPlan.attention}
                    </textarea>
                </td>
                <!--注意事项-->
            </tr>
        </table>
    </form>
    <div style="height: 300px;">
        <div style="height: 50%">
            <div id="toolbar_order">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="ChooseOrder()">增加</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-delete" plain="true"
                   onclick="doDelete_order()">删除</a>
            </div>
            <table id="dg_order" singleSelect="false" title="订单" class="easyui-datagrid" url=""
                   toolbar="#toolbar_order" rownumbers="true" fitColumns="true" fit="true"
                   data-options="onClickRow:onClickRow">
                <thead>
                <tr>
                    <th field="ID" checkbox=true></th>
                    <th field="PACKINGNUMBER" width="15" editor="{type:'textbox',options:{'required':true}}">装箱号</th>
                    <th field="LADINGCODE" width="15" editor="{type:'textbox',options:{}}">提单号</th>
                    <th field="BOXNUMBER" width="15" editor="{type:'textbox',options:{}}">箱号</th>
                    <th field="SERIALNUMBER" width="15" editor="{type:'textbox',options:{}}">封号</th>
                    <th field="COUNT" width="15" editor="{type:'numberbox',options:{}}">件数</th>
                    <th field="WEIGHT" width="15" editor="{type:'numberbox',options:{}}">毛重</th>
                    <th field="SIZE" width="15" editor="{type:'numberbox',options:{}}">尺码</th>
                </tr>
                </thead>
            </table>
        </div>
        <div style="height: 90%">
            <div id="toolbar_product">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add_order_products_tc()">增加</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-delete" plain="true"
                   onclick="delete_order_products()">删除</a>
            </div>
            <table id="dg_product" singleSelect="false" title="产品" class="easyui-datagrid" toolbar="#toolbar_product"
                   url="" rownumbers="true" fitColumns="false" fit="true"
                   data-options="onClickRow:clickRow,rowStyler:product_rowStyle,onBeforeEdit:onBeforeEdit">
                <thead frozen="true">
                <tr>
                    <th field="PRODUCTID" checkbox=true></th>
                    <th field="PACKINGNUMBER" width="15"
                        editor="{type:'textbox',options:{'required':true,precision:0}}">装箱号
                    </th>
                    <!-- <th field="SALESORDERSUBCODE" width="100px">订单号</th> -->
                    <th field="BATCHCODE" width="100px"
                        editor="{type:'combobox',options:{'required':true,'icons':[],onBeforeLoad:loadOldBatchCode,url:'/hsmes/planner/tbp/batchCode',valueField:'v',textField:'t'}}">
                        批次号
                    </th>
                    <!-- editor="{type:'combobox',options:{'required':true,'icons':[],onBeforeLoad:loadOldBatchCode,onChange:onChange,onLoadSuccess:comboboxLoadSuccess,url:'planner/tbp/batchCode',valueField:'v',textField:'t'}}" -->
                    <!-- <th field="TRAYCOUNT"  width="100px" >当前库存数量</th> -->
                    <th field="DELIVERYCOUNT" width="100px"
                        editor="{type:'numberbox',options:{precision:0,'required':true}}">发货数量
                    </th>
                    <th field="MEMO" width="120px" editor="{type:'textbox',options:{precision:0}}">备注</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th field="FACTORYPRODUCTNAME" width="150px">产品名称</th>
                    <th field="CONSUMERPRODUCTNAME" width="150px">厂内名称</th>
                    <th field="PRODUCTMODEL" width="150px">产品型号</th>
                    <th field="PARTNAME" width="80px">部件名称</th>
                    <th field="PRODUCTROLLWEIGHT" width="80px">卷重(kg)</th>
                    <th field="PRODUCTROLLLENGTH" width="80px">卷长(m)</th>
                    <th field="PRODUCTWIDTH" width="80px">门幅(mm)</th>
                    <th field="PRODUCTSHELFLIFE" width="100px">保质期(天)</th>
                    <th field="PRODUCTPROCESSCODE" width="120px">工艺代码</th>
                    <th field="PRODUCTPROCESSBOMVERSION" width="100px">工艺版本</th>
                    <th field="PRODUCTPACKAGINGCODE" width="120px">包装代码</th>
                    <th field="PRODUCTPACKAGEVERSION" width="100px">包装版本</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>