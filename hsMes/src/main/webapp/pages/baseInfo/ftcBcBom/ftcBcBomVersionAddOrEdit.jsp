<!--
作者:徐秦冬
日期:2017-12-5 17:00:00
页面:非套材包材bom版本信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>

</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--非套材包材bom版本信息表单-->
    <form id="ftcBcBomVersionForm" method="post" ajax="true"
          action="<%=basePath %>ftcBcBomVersion/${empty ftcBcBomVersion.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${ftcBcBomVersion.id}"/>
        <input id="bid" type="hidden" name="bid" value="${ftcBcBomVersion.bid}"/>
        <input id="init_version" type="hidden" value="${ftcBcBomVersion.version}"/>
        <input type="hidden" id="consumerId" name="consumerId" value="${ftcBcBomVersion.consumerId}"/>
        <table width="100%">
            <tr>
                <td class="title">版本号:</td>
                <!--版本号-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="version" name="version"
                           value="${ftcBcBomVersion.version}" class="easyui-textbox" required="true"
                           data-options="validType:['length[1,100]','onlyOneVersion']">
                </td>
            </tr>
            <tr>
                <td class="title">包材重量(kg):</td>
                <!--包材重量-->
                <td>
                    <input type="text" style="width:360px" id="bcTotalWeight" name="bcTotalWeight"
                           value="${ftcBcBomVersion.bcTotalWeight}" class="easyui-numberbox"
                           data-options="validType:['length[1,100]']" precision="2">
                </td>
                <td class="title">产品类别:</td>
                <!--产品类别-->
                <td>
                    <%-- <input type="text" id="ftcProcBomConsumerId" name="ftcProcBomConsumerId" value="${fTc_Bom.ftcProcBomConsumerId}" class="textbox" required="true" > --%>
                    <input type="text" style="width:360px" id="productType" name="productType"
                           value="${ftcBcBomVersion.productType}" class="easyui-combobox" panelHeight="auto"
                           required="true" editable="false" data-options="data: [
	                        {value:'0',text:'常规产品'},
	                        {value:'1',text:'变更试用'},
	                        {value:'2',text:'新品试样'}
                    	]">
                </td>
            </tr>
            <tr>
                <td class="title">适用客户:</td>
                <!--适用客户-->
                <td>
                    <input id="bcBomConsumerId" style="width:360px" class="easyui-searchbox" value="${consumerName}"
                           editable="false" required="true" data-options="searcher:ChooseConsumer">
                </td>
                <td class="title">卷经(cm):</td>
                <!--卷经-->
                <td>
                    <input type="text" style="width:360px" id="rollDiameter" name="rollDiameter"
                           value="${ftcBcBomVersion.rollDiameter}" class="easyui-numberbox"
                           data-options="validType:['length[1,30]']" precision="0">
                </td>
            </tr>
            <tr>
                <td class="title">托长(cm):</td>
                <!--托长-->
                <td>
                    <input type="text" style="width:360px" id="palletLength" name="palletLength"
                           value="${ftcBcBomVersion.palletLength}" class="easyui-numberbox"
                           data-options="validType:['length[1,30]']" precision="0">
                </td>
                <td class="title">托宽(cm):</td>
                <!--托宽-->
                <td>
                    <input type="text" style="width:360px" id="palletWidth" name="palletWidth"
                           value="${ftcBcBomVersion.palletWidth}" class="easyui-numberbox"
                           data-options="validType:['length[1,30]']" precision="0">
                </td>
            </tr>
            <tr>
                <td class="title">每托卷数:</td>
                <!--每托卷数-->
                <td>
                    <input type="text" style="width:360px" id="rollsPerPallet" name="rollsPerPallet"
                           value="${ftcBcBomVersion.rollsPerPallet}" class="easyui-numberbox" required="true"
                           data-options="validType:['length[1,100]']" precision="0">
                </td>
                <td class="title">托高(cm):</td>
                <!--托高-->
                <td>
                    <input type="text" style="width:360px" id="palletHeight" name="palletHeight"
                           value="${ftcBcBomVersion.palletHeight}" class="easyui-numberbox"
                           data-options="validType:['length[1,30]']" precision="0">
                </td>
            </tr>
            <tr>
                <td class="title" colspan="4" style="text-align:left;">包装要求</td>
            </tr>
            <tr>
                <td class="title">塑料膜要求:</td>
                <!--塑料膜要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_suliaomo" name="requirement_suliaomo"
                           value="${ftcBcBomVersion.requirement_suliaomo}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">摆放要求:</td>
                <!--摆放要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_baifang" name="requirement_baifang"
                           value="${ftcBcBomVersion.requirement_baifang}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">打包带要求:</td>
                <!--打包带要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_dabaodai" name="requirement_dabaodai"
                           value="${ftcBcBomVersion.requirement_dabaodai}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">标签要求:</td>
                <!--标签要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_biaoqian" name="requirement_biaoqian"
                           value="${ftcBcBomVersion.requirement_biaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">小标签要求:</td>
                <!--小标签要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_xiaobiaoqian" name="requirement_xiaobiaoqian"
                           value="${ftcBcBomVersion.requirement_xiaobiaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">卷标签要求:</td>
                <!--卷标签要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_juanbiaoqian" name="requirement_juanbiaoqian"
                           value="${ftcBcBomVersion.requirement_juanbiaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">托标签要求:</td>
                <!--托标签要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_tuobiaoqian" name="requirement_tuobiaoqian"
                           value="${ftcBcBomVersion.requirement_tuobiaoqian}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">缠绕要求:</td>
                <!--缠绕要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_chanrao" name="requirement_chanrao"
                           value="${ftcBcBomVersion.requirement_chanrao}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">其他要求:</td>
                <!--其他要求-->
                <td colspan="3">
                    <input type="text" style="width:360px" id="requirement_other" name="requirement_other"
                           value="${ftcBcBomVersion.requirement_other}" class="easyui-textbox">
                </td>
            </tr>
        </table>
    </form>
</div>