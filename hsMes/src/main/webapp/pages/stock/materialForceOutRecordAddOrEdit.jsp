<!--
作者:徐波
日期:2017-2-13 14:10:25
页面:原料强制出库增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--原料强制出库表单-->
    <form id="materialForceOutRecordForm" method="post" ajax="true"
          action="<%=basePath %>materialForceOutRecord/${empty materialForceOutRecord.id ?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${materialForceOutRecord.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>出库时间:</td>
                <!--出库时间-->
                <td>
                    <input type="text" id="outTime" name="outTime" value="${materialForceOutRecord.outTime}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>