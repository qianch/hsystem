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
<style type="text/css">


</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--套材Bom版本表单-->
    <form id="cutTcBomPartMainUploadForm" method="post" ajax="true" autocomplete="off">


        <table width="100%">
                <tr>
                    <td class="title">裁片图纸文件上传:</td>
                    <!--工艺文件上传-->
                    <td>
                        <input type="file" id="cutTcBomPartMainUploadFile">
                    </td>
                </tr>
                <tr>
                    <td class="title"></td>
                    <td style="color:red;">
                        注意事项：裁片图纸上传会覆盖(删除并新增)套材BOM的裁片图纸信息。
                    </td>
                </tr>


        </table>
    </form>
</div>
