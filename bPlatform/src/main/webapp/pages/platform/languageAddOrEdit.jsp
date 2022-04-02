<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style type="text/css">
    textarea {
        border-radius: 5px;
        margin: 0px;
        height: 112px;
        width: 238px;
        resize: none;
    }
</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--客户管理表单-->
    <form id="consumerForm" method="post" ajax="true"
          action="<%=basePath %>consumer/${empty languageCode.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${languageCode.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><spring:message code="identification"/></td>
                <td>
                    <input type="text" style="width:100%" data-options="required:true" validType="length[1,100]"
                           id="consumerCode" name="languageCode" value="${languageCode.languageCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title"><spring:message code="chinese"/></td>
                <td>
                    <input type="text" style="width:100%" data-options="required:true" validType="length[1,100]"
                           id="chinese" name="chinese" value="${languageCode.chinese}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title"><spring:message code="english"/></td>
                <td>
                    <input type="text" style="width:100%" validType="length[1,100]" id="english" name="english"
                           value="${languageCode.english}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title"><spring:message code="arabic"/></td>
                <td>
                    <input type="text" style="width:100%" validType="length[1,100]" id="arabic" name="arabic"
                           value="${languageCode.arabic}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title"><spring:message code="turkey"/></td>
                <td>
                    <input type="text" style="width:100%" validType="length[1,100]" id="turkey" name="turkey"
                           value="${languageCode.turkey}" class="easyui-textbox">
                </td>
            </tr>
        </table>
    </form>
</div>
