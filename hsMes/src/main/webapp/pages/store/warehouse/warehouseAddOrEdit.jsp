<!--
作者:肖文彬
日期:2016-9-29 15:45:32
页面:仓库管理增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--仓库管理表单-->
    <form id="warehouseForm" method="post" ajax="true"
          action="<%=basePath %>warehouse/${empty warehouse.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${warehouse.id}"/>
        <table width="100%">
            <tr>
                <td class="title">仓库名称:</td>
                <!--仓库名称-->
                <td>
                    <input type="text" id="warehouseName" data-options="required:true,validType:'length[1,20]'"
                           name="warehouseName" value="${warehouse.warehouseName}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">仓库代码:</td>
                <td>
                    <input type="text" id="warehouseCode" name="warehouseCode" value="${warehouse.warehouseCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">仓库类型:</td>
                <td>
                    <input type="text" id="wareType" name="wareType" value="${warehouse.wareType}"
                           class="easyui-combobox" required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=WareType'">
                </td>
            </tr>
            <tr>
                <td class="title"></td>
                <td>(如果不填仓库代码，自动用仓库名称首字母缩写)</td>
            </tr>
        </table>
    </form>
</div>
