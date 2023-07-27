<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>新增或者编辑包材</title>
    <%@ include file="../../base/meta.jsp" %>
    <script type="text/javascript">
        const path = "<%=basePath%>";
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
    <input type="hidden" name="id" value="${baocaiku.id}"/>
    <table width="100%">
        <tr>
            <td class="title"><span style="color:red;">*</span>类别</td>
            <td><input id="type" name="type" class="easyui-combobox" required="true"
                       data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=baoCaiType'"
                       value="${baocaiku.type}"></td>
        </tr>
        <tr>
            <td class="title">包装材料</td>
            <td><input id="specs" name="specs" class="easyui-textbox"
                       data-options="validType:['noSpecialChar','length[1,30]']" value="${department.specs}"></td>
        </tr>
        <tr>
            <td class="title">尺寸</td>
            <td><input id="size" name="specs" class="easyui-textbox"
                       data-options="validType:['noSpecialChar','length[1,30]']" value="${department.size}"></td>
        </tr>
        <tr>
            <td class="title">库存量</td>
            <td><input id="stock" name="stock" class="easyui-textbox"
                       data-options="validType:['noSpecialChar','length[1,30]']" value="${department.stock}"></td>
        </tr>
        <tr>
            <td class="title">预警值</td>
            <td><input id="warning" name="stock" class="easyui-textbox"
                       data-options="validType:['noSpecialChar','length[1,30]']" value="${department.warning}"></td>
        </tr>
        <tr>
            <td class="title">备注</td>
            <td><input id="remarks" name="remarks" class="easyui-textbox"
                       data-options="validType:['noSpecialChar','length[1,50]']" value="${department.remarks}"></td>
        </tr>
    </table>
</form>
</body>
</html>
