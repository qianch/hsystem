<!--
作者:肖文彬
日期:2016-11-16 11:25:41
页面:原料入库记录表增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--原料入库记录表表单-->
    <form id="materialInRecordForm" method="post" ajax="true"
          action="<%=basePath %>materialInRecord/${empty materialInRecord.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${materialInRecord.id}"/>
        <table width="100%">
            <tr>
                <td class="title">库位编码:</td>
                <td>
                    <input type="text" id="warehousePosCode" name="warehousePosCode"
                           value="${materialInRecord.warehousePosCode}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">仓库编码:</td>
                <td>
                    <input type="text" id="warehouseCode" name="warehouseCode" value="${materialInRecord.warehouseCode}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">物料条码:</td>
                <td>
                    <input type="text" id="materialCode" name="materialCode" value="${materialInRecord.materialCode}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">重量:</td>
                <td>
                    <input type="text" id="inWeight" name="inWeight" value="${materialInRecord.inWeight}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">入库时间:</td>
                <td>
                    <input type="text" id="inTime" name="inTime" value="${materialInRecord.inTime}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">号数偏差:</td>
                <td>
                    <input type="text" id="numberDeviation" name="numberDeviation"
                           value="${materialInRecord.numberDeviation}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">接头方式:</td>
                <td>
                    <input type="text" id="subWay" name="subWay" value="${materialInRecord.subWay}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">产品大类:</td>
                <td>
                    <input type="text" id="produceCategory" name="produceCategory"
                           value="${materialInRecord.produceCategory}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">规格型号:</td>
                <td>
                    <input type="text" id="materialModel" name="materialModel" value="${materialInRecord.materialModel}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">生产日期:</td>
                <td>
                    <input type="text" id="produceDate" name="produceDate" value="${materialInRecord.produceDate}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">操作人:</td>
                <td>
                    <input type="text" id="inUserId" name="inUserId" value="${materialInRecord.inUserId}"
                           class="easyui-textbox">
                </td>
            </tr>
        </table>
    </form>
</div>