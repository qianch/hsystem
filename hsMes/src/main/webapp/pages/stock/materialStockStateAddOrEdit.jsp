<!--
作者:肖文彬
日期:2016-11-16 11:15:02
页面:原料库存状态表增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--原料库存状态表表单-->
    <form id="materialStockStateForm" method="post" ajax="true"
          action="<%=basePath %>materialStockState/${empty materialStockState.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${materialStockState.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>库位编码:</td>
                <td>
                    <input type="text" id="warehousePosCode" name="warehousePosCode"
                           value="${materialStockState.warehousePosCode}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>仓库编码:</td>
                <td>
                    <input type="text" id="warehouseCode" name="warehouseCode"
                           value="${materialStockState.warehouseCode}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>物料条码:</td>
                <td>
                    <input type="text" id="materialCode" name="materialCode" value="${materialStockState.materialCode}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>状态:</td>
                <td>
                    <input type="text" id="state" name="state" value="${materialStockState.state}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>库存状态:</td>
                <td>
                    <input type="text" id="stockState" name="stockState" value="${materialStockState.stockState}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>