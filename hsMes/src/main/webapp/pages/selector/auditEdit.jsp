<!--
作者:徐波
日期:2016-10-18 12:56:44
页面:生产计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--生产计划表单-->
    <form id="editAuditProduce" method="post" ajax="true" action="<%=basePath %>planner/produce/commit"
          autocomplete="off">
        <input type="hidden" name="id" value="${id}"/>
        <table width="100%">
            <tr>
                <td class="title">审核名称:</td>
                <!--生产计划单号-->
                <td>
                    <input type="text" id="name" name="name" value="" class="easyui-textbox" required="true"
                           style="width:100%;">
                </td>
            </tr>
        </table>
    </form>
</div>