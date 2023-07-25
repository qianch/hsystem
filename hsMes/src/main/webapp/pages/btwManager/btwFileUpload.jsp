<!--
作者:肖文彬
日期:2016-10-9 16:10:05
页面:套材Bom版本增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
</style>
<script type="text/javascript">
</script>
<div>
    <!--套材Bom版本表单-->
    <form id="btwFileUploadForm" method="post" ajax="true" autocomplete="off">
        <input type="hidden" name="btwFileId" id="btwFileId" value="${btwFile.id}"/>
        <table style="width: 100%">
            <tr>
                <td class="title">标签名称:</td>
                <td>
                    <input type="text" id="tagName" name="tagName" value="${btwFile.tagName}" readonly="true"></td>
                </td>
            </tr>
            <tr>
                <td class="title">标签文件上传:</td>
                <!--工艺文件上传-->
                <td>
                    <input type="file" id="btwFileUploadFile">
                </td>
            </tr>
        </table>
    </form>
</div>
