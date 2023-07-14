<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<style type="text/css">
    .deptTree {
        position: absolute;
        z-index: 9px;
        width: 190px;
        overflow-y: auto;
        background-color: #FFFFFF;
        border: 1px solid rgb(169, 157, 157);
        top: 207px;
        left: 131px;
        display: none;
    }

    #selectDiv {
        border: 1px solid rgb(169, 157, 157);
        margin-left: 6px;
        width: 190;
    }

</style>
<div>
    <form id="user" method="post" ajax="true" action="<%=basePath %>user/${action eq 'add'?'add':'edit'}"
          autocomplete="off" data-options="ajax:true">
        <input type="hidden" name="id" value="${user.id}"/>
        <input type="hidden" name="createTime" value="${user.createTime}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>姓名:</td>
                <td><input type="text" id="userName" required="true" name="userName" value="${user.userName}"
                           required="true" class="easyui-textbox"></td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>登录帐号:</td>
                <td><input type="text" id="loginName" required="true" validType="code" name="loginName"
                           value="${user.loginName}" required="true" class="easyui-textbox"></td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>编号:</td>
                <td><input type="text" id="userCode" required="true" validType="code" name="userCode"
                           value="${user.userCode}" required="true" class="easyui-textbox"></td>
            </tr>
            <!-- <tr>
                <td class="title">登录密码:</td>
                <td><input type="text" id="password" name="password" value=""></td>
            </tr> -->
            <tr>
                <td class="title">电话:</td>
                <td><input type="text" id="phone" name="phone" value="${user.phone}" class="easyui-textbox"></td>
            </tr>
            <tr>
                <td class="title">邮箱:</td>
                <td><input type="text" id="email" name="email" value="${user.email}" validType="email"
                           class="easyui-textbox"></td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>部门:</td><!-- value="" -->
                <td>
                    <input type="text" panelHeight="200px" name="did" id="departmentSelect" required="true"
                           required="true" value="${action eq 'add'?dept.id:user.did}" class="easyui-tree">
                </td>
            </tr>
            <tr>
                <td class="title">性别:</td>
                <td><select name="sex" class="easyui-combobox" editable="false" style="width:175px" panelHeight="auto">
                    <option value="0" ${user.sex eq 0||user.sex eq null?'selected':''}>男</option>
                    <option value="1" ${user.sex eq 1?'selected':''}>女</option>
                </select></td>
            </tr>
            <tr>
                <td class="title">状态:</td>
                <td>
                    <select name="status" class="easyui-combobox" editable="false" style="width:175px"
                            panelHeight="auto">
                        <option value="0" ${user.status eq 0||user.status eq null?'selected':''}>启用</option>
                        <option value="-1" ${user.status eq -1?'selected':''}>禁用</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
