<!--
作者:肖文彬
日期:2016-9-29 16:26:04
页面:库位管理增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
    textarea {
        border-radius: 5px;
        margin: 0px;
        height: 112px;
        width: 238px;
        resize: none;
    }
</style>
<div>
    <!--库位管理表单-->
    <form id="warehosePosForm" method="post" ajax="true"
          action="<%=basePath %>warehosePos/${empty warehosePos.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${warehosePos.id}"/>
        <table width="100%">
            <tr>
                <td class="title">库位代码:</td>
                <td>
                    <input type="text" style="width:95%" data-options="required:true,validType:'length[1,20]'"
                           id="warehousePosCode" name="warehousePosCode" value="${warehosePos.warehousePosCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">库位名称:</td>
                <td>
                    <input type="text" style="width:95%" data-options="required:true,validType:'length[1,20]'"
                           id="warehousePosName" name="warehousePosName" value="${warehosePos.warehousePosName}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">所属仓库:</td>
                <td>
                    <input type="text" style="width:95%" id="warehouseId" panelHeight="200" name="warehouseId"
                           required="true" value="${warehosePos.warehouseId}"
                           class="easyui-combobox" data-options="url:'<%=basePath %>warehouse/combobox'">
                </td>
            </tr>
            <tr>
                <td class="title">备注:</td>
                <td>
                    <textarea maxlength=200 placeholder="请输入1-200之间的文本" name="warehousePosMemo" rows="2"
                              cols="22">${warehosePos.warehousePosMemo}</textarea>
                </td>
            </tr>
        </table>
    </form>
</div>