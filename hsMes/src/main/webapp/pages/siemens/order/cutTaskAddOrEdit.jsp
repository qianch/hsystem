<!--
作者:高飞
日期:2017-7-26 10:56:16
页面:西门子裁剪任务单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--西门子裁剪任务单表单-->
    <form id="cutTaskForm" method="post" ajax="true" action="<%=basePath %>cutTask/${empty cutTask.id ?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${cutTask.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>任务单编号:</td>
                <td>
                    <input type="text" id="taskCode" name="taskCode" value="${cutTask.taskCode}" class="easyui-textbox"
                           required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>订单号:</td>
                <td>
                    <input type="text" id="taskOrderCode" name="taskOrderCode" value="${cutTask.taskOrderCode}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>部件名称:</td>
                <td>
                    <input type="text" id="taskPartName" name="taskPartName" value="${cutTask.taskPartName}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>部件ID:</td>
                <td>
                    <input type="text" id="taskPartId" name="taskPartId" value="${cutTask.taskPartId}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>批次号:</td>
                <td>
                    <input type="text" id="taskOrderBatchCode" name="taskOrderBatchCode"
                           value="${cutTask.taskOrderBatchCode}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>客户简称:</td>
                <td>
                    <input type="text" id="taskConsumerSimpleName" name="taskConsumerSimpleName"
                           value="${cutTask.taskConsumerSimpleName}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>客户ID:</td>
                <td>
                    <input type="text" id="taskConsumerId" name="taskConsumerId" value="${cutTask.taskConsumerId}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>客户大类:</td>
                <td>
                    <input type="text" id="taskConsumerCategory" name="taskConsumerCategory"
                           value="${cutTask.taskConsumerCategory}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>任务单套数:</td>
                <td>
                    <input type="text" id="taskSuitCount" name="taskSuitCount" value="${cutTask.taskSuitCount}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>任务单已派工套数:</td>
                <td>
                    <input type="text" id="taskAssignSuitCount" name="taskAssignSuitCount"
                           value="${cutTask.taskAssignSuitCount}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>发货日期:</td>
                <td>
                    <input type="text" id="taskDeliveryDate" name="taskDeliveryDate" value="${cutTask.taskDeliveryDate}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>完成情况:</td>
                <td>
                    <input type="text" id="isComplete" name="isComplete" value="${cutTask.isComplete}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>任务已打包套数:</td>
                <td>
                    <input type="text" id="taskPackedSuitCount" name="taskPackedSuitCount"
                           value="${cutTask.taskPackedSuitCount}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>任务单创建时间:</td>
                <td>
                    <input type="text" id="taskCreateTime" name="taskCreateTime" value="${cutTask.taskCreateTime}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>创建人:</td>
                <td>
                    <input type="text" id="taskCreateUserName" name="taskCreateUserName"
                           value="${cutTask.taskCreateUserName}" class="easyui-textbox" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>