<!--
作者:季晓龙
日期:2016-9-29 15:45:32
页面:仓库管理增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">

    CSS 代码
</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--打印属性表单-->
    <form id="printTemplateForm" method="post" ajax="true"
          action="<%=basePath %>printTemplate/${empty printTemplate.id ?'add':'edit'}" autocomplete="off">

        <input type="hidden" name="id" value="${printTemplate.id}"/>

        <table width="100%">
            <tr>
                <td class="title">打印属性:</td>
                <!--打印属性-->
                <td>
                    <input type="text" id="printAttribute" data-options="required:true,validType:'length[1,200]'"
                           name="printAttribute" value="${printTemplate.printAttribute}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">打印属性名称:</td>
                <!--打印属性名称-->
                <td>
                    <input type="text" id="printAttributeName" name="printAttributeName"
                           value="${printTemplate.printAttributeName}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">打印类型:</td>
                <!--打印类型-->
                <td>
                    <input type="text" id="type" name="type"
                           value="${printTemplate.type}" class="easyui-combobox"
                           required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=printTemplatetype'"
                    >
                </td>
            </tr>
        </table>
    </form>
</div>
