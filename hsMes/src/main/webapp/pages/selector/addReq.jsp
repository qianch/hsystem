<!--
作者:宋黎明
日期:2016-9-30 10:49:34
页面:成品信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
    textarea {
        outline: none;
        resize: none;
        border: none;
        padding: 10px;
    }
</style>
<div>
    <!--成品信息表单-->
    <form id="reqFrom" method="post" ajax="true" autocomplete="off">
        <table width="100%" height="95%">
            <tr style="height: 35px;">
                <td class="title" style="text-align: left;font-size:20px;">包装要求</td>
                <td class="title" style="text-align: left;font-size:20px;">工艺要求</td>
            </tr>
            <tr>
                <td>
                    <textarea id="packReq" name="packReq" style="width:100%;height:100%;"
                              placeholder="请输入包装要求(2000字以内)" maxlength="2000">${pack}</textarea>
                </td>
                <td>
                    <textarea id="procReq" name="procReq" style="width:100%;height:100%;"
                              placeholder="请输入工艺要求(2000字以内)" maxlength="2000">${proc}</textarea>
                </td>
            </tr>
        </table>
    </form>
</div>