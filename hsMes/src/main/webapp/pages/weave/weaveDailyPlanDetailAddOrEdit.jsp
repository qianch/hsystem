<!--
作者:肖文彬
日期:2016-11-24 14:11:43
页面:日计划和编织计划表增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--日计划和编织计划表表单-->
    <form id="weaveDailyPlanDetailForm" method="post" ajax="true"
          action="<%=basePath %>weaveDailyPlanDetail/${empty weaveDailyPlanDetail.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${weaveDailyPlanDetail.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>编织计划id:</td>
                <td>
                    <input type="text" id="weavePlanId" name="weavePlanId" value="${weaveDailyPlanDetail.weavePlanId}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>日计划id:</td>
                <td>
                    <input type="text" id="DailyId" name="DailyId" value="${weaveDailyPlanDetail.DailyId}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>