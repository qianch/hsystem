<!--
作者:高飞
日期:2016-8-19 9:34:14
页面:调度实例增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--调度实例表单-->
    <form id="scheduleInstanceForm" method="post" ajax="true"
          action="<%=basePath %>scheduleInstance/${empty scheduleInstance.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${scheduleInstance.id}"/>
        <input type="hidden" name="status" value="${scheduleInstance.status}">
        <table width="100%">
            <tr>
                <td class="title">调度模板:</td>
                <!--调度任务模板ID-->
                <td>
                    <input type="text" id="templateId" name="templateId" value="${scheduleInstance.templateId}"
                           class="easyui-combobox" panelHeight="auto" editable="false" required="true"
                           url="${path }scheduleTemplate/combo" data-options="textField:'TEMPLATENAME',valueField:'ID'">
                </td>
            </tr>
            <tr>
                <td class="title">CRON表达式:</td>
                <!--CRON表达式-->
                <td>
                    <input type="text" id="cron" name="cron" value="${scheduleInstance.cron}" class="easyui-textbox"
                           required="true">
                    <a class="easyui-linkbutton" plain="true" iconCls="platform-time" data-options="onClick:buildCron">CRON构建</a>
                </td>
            </tr>
            <tr>
                <td class="title">描述:</td>
                <!--描述-->
                <td>
                    <input type="text" id="instanceDesc" name="instanceDesc" value="${scheduleInstance.instanceDesc}"
                           class="easyui-textbox" style="height:50px;width:420px;" validType="length[1,100]"
                           multiline="true">
                </td>
            </tr>
        </table>
    </form>
</div>