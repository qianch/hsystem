<!--
作者:高飞
日期:2017-11-28 11:10:48
页面:包装版本信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
</style>
<script type="text/javascript">
</script>
<div>
    <!--包装版本信息表单-->
    <form id="ftcBcBomVersionForm" method="post" ajax="true"
          action="<%=basePath %>ftcBcBomVersion/${empty ftcBcBomVersion.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${ftcBcBomVersion.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>包材BOM ID:</td>
                <!--包材BOM ID-->
                <td>
                    <input type="text" id="bid" name="bid" value="${ftcBcBomVersion.bid}" class="easyui-textbox"
                           required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>版本:</td>
                <!--版本-->
                <td>
                    <input type="text" id="version" name="version" value="${ftcBcBomVersion.version}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">包材总重:</td>
                <!--包材总重-->
                <td>
                    <input type="text" id="bcTotalWeight" name="bcTotalWeight" value="${ftcBcBomVersion.bcTotalWeight}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>产品类别:</td>
                <!--产品类别-->
                <td>
                    <input type="text" id="productType" name="productType" value="${ftcBcBomVersion.productType}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>客户名称:</td>
                <!--客户名称-->
                <td>
                    <input type="text" id="consumerId" name="consumerId" value="${ftcBcBomVersion.consumerId}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">卷径:</td>
                <!--卷径-->
                <td>
                    <input type="text" id="rollDiameter" name="rollDiameter" value="${ftcBcBomVersion.rollDiameter}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">托长:</td>
                <!--托长-->
                <td>
                    <input type="text" id="palletLength" name="palletLength" value="${ftcBcBomVersion.palletLength}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">托宽:</td>
                <!--托宽-->
                <td>
                    <input type="text" id="palletWidth" name="palletWidth" value="${ftcBcBomVersion.palletWidth}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>每托卷数:</td>
                <!--每托卷数-->
                <td>
                    <input type="text" id="rollsPerPallet" name="rollsPerPallet"
                           value="${ftcBcBomVersion.rollsPerPallet}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">托高:</td>
                <!--托高-->
                <td>
                    <input type="text" id="palletHeight" name="palletHeight" value="${ftcBcBomVersion.palletHeight}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">塑料膜要求:</td>
                <!--塑料膜要求-->
                <td>
                    <input type="text" id="requirement_suliaomo" name="requirement_suliaomo"
                           value="${ftcBcBomVersion.requirement_suliaomo}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">摆放要求:</td>
                <!--摆放要求-->
                <td>
                    <input type="text" id="requirement_baifang" name="requirement_baifang"
                           value="${ftcBcBomVersion.requirement_baifang}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">打包带要求:</td>
                <!--打包带要求-->
                <td>
                    <input type="text" id="requirement_dabaodai" name="requirement_dabaodai"
                           value="${ftcBcBomVersion.requirement_dabaodai}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">标签要求:</td>
                <!--标签要求-->
                <td>
                    <input type="text" id="requirement_biaoqian" name="requirement_biaoqian"
                           value="${ftcBcBomVersion.requirement_biaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">小标签要求:</td>
                <!--小标签要求-->
                <td>
                    <input type="text" id="requirement_xiaobiaoqian" name="requirement_xiaobiaoqian"
                           value="${ftcBcBomVersion.requirement_xiaobiaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">卷标签要求:</td>
                <!--卷标签要求-->
                <td>
                    <input type="text" id="requirement_juanbiaoqian" name="requirement_juanbiaoqian"
                           value="${ftcBcBomVersion.requirement_juanbiaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">托标签要求:</td>
                <!--托标签要求-->
                <td>
                    <input type="text" id="requirement_tuobiaoqian" name="requirement_tuobiaoqian"
                           value="${ftcBcBomVersion.requirement_tuobiaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">缠绕要求:</td>
                <!--缠绕要求-->
                <td>
                    <input type="text" id="requirement_chanrao" name="requirement_chanrao"
                           value="${ftcBcBomVersion.requirement_chanrao}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">其他:</td>
                <!--其他-->
                <td>
                    <input type="text" id="requirement_other" name="requirement_other"
                           value="${ftcBcBomVersion.requirement_other}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>是否可用:</td>
                <!--是否可用-->
                <td>
                    <input type="text" id="enabled" name="enabled" value="${ftcBcBomVersion.enabled}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>