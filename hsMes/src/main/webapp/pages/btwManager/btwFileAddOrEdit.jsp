<!--
作者:徐波
日期:2016-11-14 15:40:51
页面:打印机信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">

</style>
<script type="text/javascript">
    //JS代码
</script>
<div>
    <!--打印机信息表单-->
    <form id="btwFileForm" method="post" ajax="true" action="<%=basePath %>printer/${empty btwFile.id ?'add':'edit'}"
          autocomplete="off">

        <input type="hidden" name="id" value="${btwFile.id}"/>

        <table width="100%">
            <tr>
                <td class="title">客户名称:</td>
                <!--客户名称-->
                <td>
                    <input type="hidden" name="consumerId"   id="consumerId"   value="${btwFile.consumerId}">
                    <input type="hidden" name="consumerCode" id="consumerCode" value="${btwFile.consumerCode}">
                    <input type="hidden" name="tagActName"   id="tagActName"   value="${btwFile.tagActName}">
                    <input type="hidden" name="State" id="State" value="1">
                    <input type="text" id="consumerName" name="consumerName" value="${btwFile.consumerName}"
                           class="easyui-searchbox" required="true" data-options="searcher:selectConsumer,icons:[]">
                </td>


            </tr>
            <tr>
                <td class="title">标签类型:</td>
                <td>
                    <input type="text" id="tagType" name="tagType"
                           class="easyui-combobox" value="${btwFile.tagType}"
                           required="true" style="width: 80px;"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=TagType'"
                    >
                </td>

                <td class="title">标签名称:</td>
                <td>
                    <input type="text" id="tagName" name="tagName" value="${btwFile.tagName}" required="true"></td>
                </td>

            </tr>
            <tr>
                <td class="title">客户条码前缀:</td>
                <td>
                    <input type="text" id="consumerBarCodePrefix" name="consumerBarCodePrefix"
                           value="${btwFile.consumerBarCodePrefix}" required="true"></td>
                </td>
            </tr>
            <tr>
                <td class="title">客户条码记录:</td>
                <td>
                    <input type="number" id="consumerBarCodeRecord" name="consumerBarCodeRecord" value="0"
                           required="true"></td>
                </td>
                <td class="title">客户条码位数:</td>
                <td>
                    <input type="number" id="consumerBarCodeDigit" name="consumerBarCodeDigit" min="1" value="6"
                           required="true"></td>
                </td>
            </tr>
            <tr>
                <td class="title">供销商条码前缀:</td>
                <td>
                    <input type="text" id="agentBarCodePrefix" name="agentBarCodePrefix"
                           value="${btwFile.agentBarCodePrefix}" required="true"></td>
                </td>
            </tr>

            <tr>
                <td class="title">供销商条码记录:</td>
                <td>
                    <input type="number" id="agentBarCodeRecord" name="agentBarCodeRecord" value="0" required="true">
                </td>
                </td>
                <td class="title">供销商条码位数:</td>
                <td>
                    <input type="number" id="agentBarCodeDigit" min="1" name="agentBarCodeDigit" value="6"
                           required="true"></td>
                </td>
            </tr>
        </table>
    </form>
</div>
