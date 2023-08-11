<!--
作者:宋黎明
日期:2016-10-18 13:35:17
页面:裁剪计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
</style>
<script type="text/javascript">
    //var userList=${userList};
</script>
<div>
    <!--裁剪计划表单-->
    <form id="cutPlanForm" method="post" ajax="true"
          <%-- action="<%=basePath %>cutPlan/${empty cutPlan.id ?'add':'edit'}"  --%>autocomplete="off">
        <%-- <input type="hidden" name="id" value="${cutPlan.id}" />
        <input type="hidden" name="tcBomPartId" value="${tcVersionParts.id}" />
        <input type="hidden" name="producePlanDetailId" value="${cutPlan.producePlanDetailId}" />
        <input type="hidden" id="userIds" name="userIds" value="${userIds}"/> --%>
        <table style="width: 100%">
            <tr>
                <td class="title">人员姓名:</td>
                <!--人员姓名-->
                <td>
                    <input type="text" id="userName" class="easyui-searchbox"
                           data-options="'icons':[],searcher:ChooseUser,width:'90%'" value="${userName}"></td>
            </tr>
            <tr>
                <td class="title">数量:</td>
                <!--数量-->
                <td><input type="text" id="num" class="easyui-numberbox"></td>
            </tr>
        </table>
    </form>
</div>