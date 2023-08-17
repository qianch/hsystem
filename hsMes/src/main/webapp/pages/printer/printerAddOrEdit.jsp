<!--
作者:徐波
日期:2016-11-14 15:40:51
页面:打印机信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--打印机信息表单-->
    <form id="printerForm" method="post" ajax="true" action="<%=basePath %>printer/${empty printer.id ?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${printer.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>打印机名称:</td>
                <td>
                    <input type="text" id="printerName" name="printerName" value="${printer.printerName}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>打印机ip:</td>
                <td>
                    <input type="text" id="printerIp" name="printerIp" value="${printer.printerIp}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>