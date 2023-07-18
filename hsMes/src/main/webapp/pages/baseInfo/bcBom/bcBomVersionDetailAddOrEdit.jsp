<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:包材bom明细增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
    .mui-input-row .mui-input-clear ~ .mui-icon-clear, .mui-input-row .mui-input-speech
    ~ .mui-icon-speech, .mui-input-row .mui-input-password ~ .mui-icon-eye {
        top: 2px;
    }

    .mui-input-group .mui-input-row {
        height: 25px;
    }

    .mui-input-row label ~ input, .mui-input-row label ~ select, .mui-input-row label
    ~ textarea {
        height: 25px;
    }

    .mui-input-row .mui-input-clear ~ .mui-icon-clear, .mui-input-row .mui-input-speech
    ~ .mui-icon-speech, .mui-input-row .mui-input-password ~ .mui-icon-eye {
        top: 2px;
    }

    .mui-input-row label {
        font-family: 'Helvetica Neue', Helvetica, sans-serif;
        padding: 0px 10px 0px 15px;
        line-height: 25px;
    }
</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--包材bom明细表单-->
    <form id="bcBomVersionDetailForm" method="post" ajax="true"
          action="<%=basePath %>bcBomVersionDetail/${empty bcBomVersionDetail.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${bcBomVersionDetail.id}"/>
        <table style="width:100%">
            <tr>
                <td class="title">包材名称:</td>
                <!--包材名称-->
                <td>
                    <input type="text" id="packMaterialName" name="packMaterialName"
                           value="${bcBomVersionDetail.packMaterialName}" class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">规格:</td>
                <!--规格-->
                <td>
                    <input type="text" id="packMaterialModel" name="packMaterialModel"
                           value="${bcBomVersionDetail.packMaterialModel}" class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">材质:</td>
                <!--材质-->
                <td>
                    <input type="text" id="packMaterialAttr" name="packMaterialAttr"
                           value="${bcBomVersionDetail.packMaterialAttr}" class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">数量:</td>
                <!--数量-->
                <td>
                    <input type="text" id="packMaterialCount" name="packMaterialCount"
                           value="${bcBomVersionDetail.packMaterialCount}" class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">物料单位:</td>
                <!--物料单位-->
                <td>
                    <input type="text" id="packMaterialUnit" name="packMaterialUnit"
                           value="${bcBomVersionDetail.packMaterialUnit}" class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">包装单位:</td>
                <!--包装单位-->
                <td>
                    <input type="text" id="packUnit" name="packUnit" value="${bcBomVersionDetail.packUnit}"
                           class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">包装要求:</td>
                <!--包装要求-->
                <td>
                    <input type="text" id="packRequire" name="packRequire" value="${bcBomVersionDetail.packRequire}"
                           class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">备注:</td>
                <!--备注-->
                <td>
                    <input type="text" id="packMemo" name="packMemo" value="${bcBomVersionDetail.packMemo}"
                           class="textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">包装版本信息:</td>
                <!--包装版本信息-->
                <td>
                    <input type="text" id="packVersionId" name="packVersionId"
                           value="${bcBomVersionDetail.packVersionId}" class="textbox" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>