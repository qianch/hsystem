<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>新增或者编辑部门</title>
    <%@ include file="../base/meta.jsp" %>
    <script type="text/javascript">
        let valid;
        let form;
        $(document).ready(function () {
            valid = $("#department").validate({
                debug: true,
                rules: {
                    name: {
                        required: true,
                        maxlength: 32,
                        minlength: 2,
                        stringCheck: true
                    }
                },
                onfocusout: function (element) {
                    $(element).valid();
                }
            });
            form = $("#department");
        });
    </script>
</head>

<body>
<form id="department" method="post" action="<%=basePath %>department/${action eq 'add'?'add':'edit'}"
      autocomplete="off">
    <input type="hidden" name="id" value="${department.id}"/>
    <input type="hidden" name="pid" value="${department.pid eq ''?'1':department.pid}"/>
    <table width="100%">
        <tr>
            <td class="title"><span style="color:red;">*</span>部门名称</td>
            <td><input id="name" name="name" class="easyui-textbox" required="true"
                       data-options="validType:['noSpecialChar','length[1,30]']" value="${department.name}"></td>
        </tr>
        <tr>
            <td class="title">部门编码</td>
            <td><input id="code" name="code" class="easyui-textbox"
                       data-options="validType:['noSpecialChar','length[1,30]']" value="${department.code}"></td>
        </tr>
        <tr>
            <td class="title">部门类型:</td>
            <!--仓库类型-->
            <td>
                <input type="text" id="type" name="type" value="${department.type}" class="easyui-combobox"
                       data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=DepartmentType'">
            </td>
        </tr>
        <tr>
            <td class="title">部门前缀</td>
            <td><input id="prefix" name="prefix" class="easyui-textbox"
                       data-options="validType:['noSpecialChar','length[1,30]']" value="${department.prefix}"></td>
        </tr>
        <tr>
            <td class="title"></td>
            <td>(如果不填，自动用部门名称首字母缩写)</td>
        </tr>
    </table>
</form>
</body>
</html>
