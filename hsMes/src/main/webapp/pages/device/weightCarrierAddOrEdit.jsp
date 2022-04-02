<!--
作者:孙利
日期:2017-7-10 8:44:34
页面:称重载具增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
    /
    /
    CSS 代码
</style>
<script type="text/javascript">
    //JS代码
    $('#carrierWeight').numberbox({
        min: 0,
        max: 9999,
        precision: 2
    });
</script>
<div>
    <!--称重载具表单-->
    <form id="weightCarrierForm" method="post" ajax="true"
          action="<%=basePath %>weightCarrier/${empty weightCarrier.id ?'add':'edit'}" autocomplete="off">

        <input type="hidden" name="id" value="${weightCarrier.id}"/>

        <table width="95%">
            <tr>
                <td class="title" style="width:200px;"><span style="color:red;">*</span>载具编号:</td>
                <!--载具编号-->
                <td>
                    <input type="text" id="carrierCode" name="carrierCode" value="${weightCarrier.carrierCode}"
                           class="easyui-textbox" required="true" data-options="validType:['length[0,120]']"
                           style="width:200px;">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>载具名称:</td>
                <!--载具名称-->
                <td>
                    <input type="text" id="carrierName" name="carrierName" value="${weightCarrier.carrierName}"
                           class="easyui-textbox" required="true" data-options="validType:['length[0,120]']"
                           style="width:200px;">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>重量(kg):</td>
                <!--重量(kg)-->
                <td>
                    <input type="text" id="carrierWeight" name="carrierWeight" value="${weightCarrier.carrierWeight}"
                           class="easyui-textbox" required="true" style="width:200px;">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>车间:</td>
                <!--所属车间-->
                <td>

                    <input type="text" id="workShopCode" class="easyui-combobox" name="workShopCode"
                           value="${weightCarrier.workShopCode}" panelHeight="200"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=cut,weave,company' ,onSelect: function(rec){
      	$('#workSpace').val(rec.t);
        }">

                    <input type="hidden" id="workSpace" name="workSpace" value="${weightCarrier.workSpace}">
                </td>
            </tr>

        </table>
    </form>
</div>
