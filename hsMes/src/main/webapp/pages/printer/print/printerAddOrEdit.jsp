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
<style type="text/css">
    /
    /
    CSS 代码
</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--仓库管理表单-->
    <form id="printerForm" method="post" ajax="true" action="<%=basePath %>print/${empty printer.id ?'add':'edit'}"
          autocomplete="off">

        <input type="hidden" name="id" value="${printer.id}"/>

        <table width="100%">
            <tr>
                <td class="title">打印机名称:</td>
                <!--仓库名称-->
                <td>
                    <input type="text" id="printerName" data-options="required:true,validType:'length[1,200]'"
                           name="printerName" value="${printer.printerName}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">打印机显示名称:</td>
                <!--仓库代码-->
                <td>
                    <input type="text" id="printerTxtName" name="printerTxtName" value="${printer.printerTxtName}"
                           class="easyui-textbox">
                </td>
            </tr>

            <tr>
                <td class="title">打印机ip:</td>
                <!--仓库代码-->
                <td>
                    <input type="text" id="printerIp" name="printerIp" value="${printer.printerIp}"
                           class="easyui-textbox">
                </td>
            </tr>

            <tr>
                <td class="title">打印机状态:</td>
                <!--仓库类型-->
                <td>
                    <input type="text" id="printerState" name="printerState"
                           value="${printer.printerState}" class="easyui-combobox"
                           required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=printerState'"
                    >
                </td>
            </tr>

            <tr>
                <td class="title">部门:</td>
                <!--仓库类型-->
                <td>
                    <input type="text" id="departmentId" name="departmentId"
                           value="${printer.departmentId}" class="easyui-combobox"
                           required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryAllDepartmentID'"
                    >
                </td>
            </tr>

            <tr>
                <td class="title">打印port:</td>
                <!--仓库代码-->
                <td>
                    <input type="text" id="port" name="port" value="${printer.port}"
                           class="easyui-textbox">
                </td>
            </tr>

        </table>
    </form>
</div>
