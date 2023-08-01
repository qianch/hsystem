<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/jstl.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<style>
</style>
<script type="text/javascript">
    /* JS代码 */
    const details =${details};
</script>
<div class="easyui-layout" style="height:100%;width:100%;">
    <div data-options="region:'north',height:112">
        <!--销售订单表单-->
        <input type="hidden" id="code" name="code" value="${code}"/>
        <form id="salesOrderForm" style="padding:0px;margin:0px;">
            <input type="hidden" name="id" value="${salesOrder.id}"/>
            <input type="hidden" name="auditState" value="${salesOrder.auditState}"/>
            <input type="hidden" name="isClosed" value="0"/>
            <input type="hidden" id="cutPlanId" name="cutPlanId" value="${cutPlanId}"/>
            <input type="hidden" id="finalConsumerId" name="finalConsumerId" value="${finalConsumerId}"/>
            <input type="hidden" id="fromProducePlancode" name="fromProducePlancode"
                   value="${empty fromProducePlancode?salesOrder.fromProducePlancode:fromProducePlancode}">
            <table style="width: 100%">
                <tr>
                    <td class="title">客户名称:</td>
                    <!--客户名称-->
                    <td>
                        <input type="hidden" name="salesOrderConsumerId" id="salesOrderConsumerId"
                               value="${salesOrder.salesOrderConsumerId }">
                        <input type="hidden" id="consumerCode" value="${consumerCode }">
                        <input type="text" id="salesOrderConsumerName" value="${consumerName}" class="easyui-searchbox"
                               required="true"></td>
                    <td class="title">内销/外销:</td>
                    <!--内销/外销-->
                    <td><input type="text" id="salesOrderIsExport" name="salesOrderIsExport"
                               value="${salesOrder.salesOrderIsExport}" class="easyui-combobox"
                               data-options="valueField:'v',textField:'t',data:[{'v':'-1','t':'胚布'}],onSelect:changeSerial">
                    </td>
                </tr>
                <tr>
                    <td class="title">订单编号:</td>
                    <!--订单号-->
                    <td><input type="text" id="salesOrderCode" name="salesOrderCode"
                               value="${salesOrder.salesOrderCode}" class="easyui-textbox" required="true"></td>
                    <td class="title">下单日期:</td>
                    <!--下单日期-->
                    <td><input type="text" id="salesOrderDate" name="salesOrderDate"
                               value="${salesOrder.salesOrderDate}" class="easyui-datebox" required="true"
                               readonly="true"></td>
                </tr>
                <tr>
                    <td class="title">业务员:</td>
                    <!--业务员-->
                    <td>
                        <input type="hidden" name="salesOrderBizUserId" id="salesOrderBizUserId"
                               value="${empty salesOrder.salesOrderBizUserId?userId:salesOrder.salesOrderBizUserId }">
                        <input type="text" id="salesOrderBizUserName" value="${empty bizUserName?userName:bizUserName}"
                               class="easyui-searchbox" required="true"
                        ></td>
                    <td class="title">订单类型:</td>
                    <!--订单类型 （3新品，2试样，1常规产品，-1未知）-->
                    <td><input type="text" id="salesOrderType" required="true" name="salesOrderType"
                               value="${salesOrder.salesOrderType}" class="easyui-combobox"
                               data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'常规'},{'v':'2','t':'试样'},{'v':'3','t':'新品'},{'v':'-1','t':'未知'}],value:1">
                    </td>
                </tr>
                <tr>
                    <td class="title">订单备注:</td>
                    <!--订单备注-->
                    <td colspan="3">
                        <textarea style="width:100%;height:25px;border-radius:5px;" name="salesOrderMemo"
                                  maxlength="2000">${salesOrder.salesOrderMemo}</textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center'">
        <div id="toolbar_product">
            <%--<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon- add" onclick="selectProduct()">增加</a>--%>
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-remove"
               onclick="removeProduct()">删除</a>
            <input type="text" class="easyui-datebox" id="sss"><a href="javascript:void(0)" class="easyui-linkbutton"
                                                                  plain="true" iconcls="icon-edit"
                                                                  onclick="resetDate()">批量修改时间</a>
            <input type="text" class="easyui-textbox" id="ssss"><a href="javascript:void(0)" class="easyui-linkbutton"
                                                                   plain="true" iconcls="icon-edit"
                                                                   onclick="resetSales()">批量修改订单号</a>
        </div>
        <table id="product_dg" title="订单产品" fit="true" class="easyui-datagrid" singleSelect="false"
               fitColumns="false" style="width:100%;" toolbar="#toolbar_product" data-options="onClickRow:clickRow">
            <thead frozen="true">
            <tr>
                <th field="productId" checkbox=true></th>
                <th width="120" field="salesOrderSubCode" editor="{type:'textbox',options:{required:true,'icons':[]}}">
                    订单号
                </th>
                <th width="120" field="productBatchCode" editor="{type:'textbox',options:{required:false,'icons':[]}}">
                    批次号
                </th>
                <th width="100" field="consumerProductName">客户名称</th>
                <th width=120 field="factoryProductName">厂内名称</th>
                <th width="80" field="productModel">产品型号</th>
                <!-- <th width="55px" field="productBatchCode" editor="{type:'textbox',options:{'icons':[]}}" >批次号</th>
                <th width="55px" field="salesOrderSubCode" editor="{type:'textbox',options:{'icons':[]}}"  >子订单号</th> -->
                <th width="80" field="productCount" editor="{type:'numberbox',options:{'required':true,'icons':[]}}"
                    formatter="processNumberFormatter">数量(kg/套)
                </th>
                <th width="100" field="deliveryTime" editor="{type:'datebox',options:{'required':true}}">发货时间</th>
            </tr>
            </thead>
            <thead>
            <tr>
                <%-- <th width="65" field="productWidth" editor="{type:'numberbox',options:{'icons':[]}}">门幅(mm)</th> --%>
                <th width="65" field="productWidth">门幅(mm)</th>
                <%-- <th width="65" field="productRollLength" editor="{type:'numberbox',options:{'icons':[]}}" >卷长(m)</th> --%>
                <th width="65" field="productRollLength">卷长(m)</th>
                <th width="65" field="productRollWeight">卷重(Kg)</th>
                <th width="65" field="partName">部件名称</th>
                <th width="80" field="drawNo" editor="{type:'textbox',options:{'icons':[]}}">图号</th>
                <th width="65" field="rollNo" editor="{type:'textbox',options:{'icons':[]}}">卷号</th>
                <th width="65" field="levelNo" editor="{type:'textbox',options:{'icons':[]}}">层数</th>
                <th width="80" field="productProcessCode">工艺代码</th>
                <th width="80" field="productProcessBomVersion">工艺版本</th>
                <th width="80" field="productPackagingCode">包装代码</th>
                <th width="80" field="productPackageVersion">包装版本</th>
                <%-- 					<th width="80" field="productRollCode" editor="{type:'textbox',options:{'icons':[]}}">卷标签</th>
                                    <th width="80" field="productBoxCode" editor="{type:'textbox',options:{'icons':[]}}">箱唛头</th>
                                    <th width="80" field="productTrayCode" editor="{type:'textbox',options:{'icons':[]}}">托唛头</th> --%>
                <th width="80" field="productMemo" editor="{type:'textbox',options:{'icons':[]}}">备注</th>
            </tr>
            </thead>
        </table>
    </div>
</div>