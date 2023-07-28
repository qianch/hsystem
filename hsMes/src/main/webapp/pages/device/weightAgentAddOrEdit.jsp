<!--
作者:孙利
日期:2017-7-6 10:20:58
页面:称重介质增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>

</style>
<script type="text/javascript">
    $('#agentWeight').numberbox({
        min: 0,
        max: 100,
        precision: 2
    });

</script>
<div>
    <!--称重载具表单-->
    <form id="weightAgentForm" method="post" ajax="true"
          action="<%=basePath %>weightAgent/${empty weightAgent.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${weightAgent.id}"/>
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>载具编号:</td>
                <!--载具编号-->
                <td>
                    <input type="text" id="agentCode" name="agentCode" value="${weightAgent.agentCode}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>载具名称:</td>
                <!--载具名称-->
                <td>
                    <input type="text" id="agentName" name="agentName" value="${weightAgent.agentName}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>规格型号:</td>
                <!--规格型号-->
                <td>
                    <input type="text" id="specificationsModels" name="specificationsModels"
                           value="${weightAgent.specificationsModels}" class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>重量(kg):</td>
                <!--重量(kg)-->
                <td>
                    <input type="text" id="agentWeight" name="agentWeight" value="${weightAgent.agentWeight}"
                           class="easyui-textbox" required="true">
                </td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>车间:</td>
                <!--所属车间-->
                <td>
                    <select id="workSpace" name="workSpace">
                        <option value="编织一车间" ${weightAgent.workSpace eq '编织一车间'?'selected':''} >编织一车间
                        </option>
                        <option value="编织二车间" ${weightAgent.workSpace eq '编织二车间'?'selected':''}>编织二车间
                        </option>
                        <option value="编织三车间" ${weightAgent.workSpace eq '编织三车间'?'selected':''}>编织三车间
                        </option>
                        <option value="裁剪车间" ${weightAgent.workSpace eq '裁剪车间'?'selected':''}>裁剪车间</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>