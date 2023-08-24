<!--
作者:高飞
日期:2017-7-25 10:39:09
页面:组别管理增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>
    <!--组别管理表单-->
    <form id="cutGroupForm" method="post" ajax="true" action="<%=basePath %>cutGroup/${empty cutGroup.id ?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${cutGroup.id}"/>
        <input type="hidden" name="createUser" value="${cutGroup.createUser}"/>
        <input type="hidden" name="createTime" value="${cutGroup.createTime}"/>
        <jsp:useBean id="now" class="java.util.Date"/>
        <input type="hidden" name="modifyUser" value="${userName}"/>
        <input type="hidden" name="modifyTime"
               value="<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" />"/>
        <table width="100%">
            <tr>
                <td class="title">组别:</td>
                <td>
                    <input type="text" id="groupName" name="groupName" value="${cutGroup.groupName}" required="true"
                           class="easyui-textbox" data-options="validType:'length[1,30]'">
                </td>
            </tr>
            <tr>
                <td class="title">机长:</td>
                <td>
                    <input type="text" id="groupLeader" name="groupLeader" editable="true"
                           value="${cutGroup.groupLeader}" required="true" class="easyui-combobox"
                           data-options="url:'<%=basePath %>cutGroup/workshop/cut/users',valueField:'USERNAME',textField:'USERNAME',panelMaxHeight:200,filter:comboFilter,onHidePanel:validCode">
                </td>
            </tr>
            <tr>
                <td class="title">班别:</td>
                <td>
                    <input type="text" id="groupType" name="groupType" value="${cutGroup.groupType}"
                           class="easyui-textbox" data-options="validType:'length[1,30]'">
                </td>
            </tr>

        </table>
    </form>
</div>