<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .title {
        width: 150px;
    }
</style>
<div>
    <form action="#" id="dictForm">
        <!-- 字典ID -->
        <input type="hidden" name="id" value="${dict.id }">
        <!-- 父级ID -->
        <input type="hidden" name="pid" value="${dict.pid }">
        <input type="hidden" name="children" id="children">
        <table style="width:100%;">
            <c:if test="${!empty parentName }">
                <tr>
                    <td class="title">父级节点</td>
                    <td>
                        <input class="easyui-textbox" required="true" value="${parentName }" editable="false"
                               validType="length[0,30]">
                    </td>
                </tr>
            </c:if>
            <tr>
                <td class="title">字典代码</td>
                <td>
                    <!-- 字典编码 -->
                    <input name="code" class="easyui-validatebox easyui-textbox"
                           data-options="validType:['CharAndNumber','length[0,30]']" required="true"
                           value="${dict.code }" ${dict.code==null?'':'readonly' }>
                </td>
            </tr>
            <tr>
                <td class="title">字典名称</td>
                <td><input name="name_zh_CN" class="easyui-textbox" required="true" value="${dict.name_zh_CN}"
                           validType="length[0,30]"></td>
            </tr>
            <tr>
                <td class="title">是否生效</td>
                <td>
                    <select id="deprecated" name="deprecated"
                            style="width: 174px;border-radius: 5px;height: 23px;margin-left: 6px;" panelHeight="auto">
                        <option value="0" ${dict.deprecated==0?'selected':'' }>启用</option>
                        <option value="1" ${dict.deprecated==1?'selected':'' }>弃用</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
