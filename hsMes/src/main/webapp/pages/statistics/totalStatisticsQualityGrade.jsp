<!--
作者:徐波
日期:2017-03-09 12:44:04
页面:质量判级页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--综合统计表单-->
    <form id="totalStatisticsForm" method="post" ajax="true" autocomplete="off">
        <input type="hidden" name="ids" value="${ids}"/>
        <table width="100%">
            <tr>
                <td class="title">质量等级:</td>
                <td>
                    <input type="text" class="easyui-combobox" id="qualityGrade" name="qualityGrade"
                           data-options="valueField:'gradename',textField:'gradedesc',url:'<%=basePath%>qualityGrade/getQualityGrade'">
                </td>
            </tr>
        </table>
    </form>
</div>