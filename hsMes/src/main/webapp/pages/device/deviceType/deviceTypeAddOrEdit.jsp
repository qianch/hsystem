<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style>
    .title {
        width: 150px;
    }
</style>
<div>
    <form id="deviceForm">
        <!-- 字典ID -->
        <input type="hidden" name="id" value="${deviceType.id}">
        <!-- 父级ID -->
        <input type="hidden" name="categoryParentId" value="${deviceType.categoryParentId}">
        <table style="width:100%;">
            <tr>
                <td class="title">类别名称：</td>
                <td><input name="categoryName" class="easyui-textbox" required="true" value="${deviceType.categoryName}"
                           data-options="validType:['length[0,25]']"></td>
            </tr>
        </table>
    </form>
</div>
