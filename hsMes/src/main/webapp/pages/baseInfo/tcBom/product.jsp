<!--
作者:宋黎明
日期:2016-9-30 10:49:34
页面:成品信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
    textarea {
        border-radius: 5px;
    }
</style>
<script type="text/javascript">
</script>
<div>
    <!--成品信息表单-->
    <form id="finishProductForm" method="post" ajax="true"
          action="<%=basePath %>finishProduct/${empty finishProduct.id ?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${finishProduct.id}"/>
        <!--成品客户信息ID-->
        <input type="hidden" id="productConsumerId" name="productConsumerId"
               value="${finishProduct.productConsumerId}"/>
        <table style="width: 100%">
            <tr>
                <td class="title">客户代码:</td>
                <!--客户代码-->
                <td><input id="productConsumerCode" class="easyui-searchbox"
                           value="${consumer.consumerCode}" editable="false" required="true"
                           data-options="searcher:ChooseConsumer1"></td>
                <td class="title">客户名称:</td>
                <!--客户名称-->
                <td><input id="productConsumerName" class="easyui-textbox"
                           value="${consumer.consumerName}" editable="false" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">客户产品名称:</td>
                <!--客户产品名称-->
                <td><input type="text" id="consumerProductName"
                           name="consumerProductName"
                           value="${finishProduct.consumerProductName}" class="easyui-textbox"
                           required="true" data-options="validType:['length[0,150]']"></td>
                <td class="title">厂内名称:</td>
                <!--厂内名称-->
                <td><input type="text" id="factoryProductName"
                           name="factoryProductName"
                           value="${finishProduct.factoryProductName}" class="easyui-textbox"
                           required="true" data-options="validType:['length[0,25]']">
                </td>
            </tr>
            <tr>
                <td class="title">卷重:</td>
                <td>
                    <input id="productIsTc2" type="hidden" value="2" name="productIsTc" checked="checked">
                    <input id="productRollWeight" name="productRollWeight" value="${finishProduct.productRollWeight }"
                           class="easyui-numberbox" suffix="kg" precision="2">
                </td>
                <td class="title">米长:</td>
                <!--卷长-->
                <td><input type="text" id="productRollLength"
                           name="productRollLength" value="${finishProduct.productRollLength}"
                           class="easyui-numberbox" suffix="m" precision="2">
                </td>
            </tr>
            <tr>
                <td class="title">门幅:</td>
                <!--门幅-->
                <td><input type="text" id="productWidth" name="productWidth"
                           value="${finishProduct.productWidth}" class="easyui-numberbox" suffix="mm"></td>
                <td class="title">产品规格:</td>
                <td>
                    <input type="text" id="productModel"
                           name="productModel" value="${finishProduct.productModel}"
                           class="easyui-textbox" required="true" data-options="validType:['length[0,25]']">
                </td>
                </td>
            </tr>
            <tr>
                <td class="title">工艺标准代码:</td>
                <!--工艺标准代码-->
                <td><input id="productProcessBom"
                           name="productProcessCode" value="${finishProduct.productProcessCode}"
                           class="easyui-searchbox" editable="false"
                           data-options="searcher:chooseBom" required="true">
                </td>
                <td class="title">工艺标准版本:</td>
                <!--工艺标准版本-->
                <td><input type="text" id="productProcessBomVersion"
                           name="productProcessBomVersion" value="${finishProduct.productProcessBomVersion}"
                           class="easyui-textbox" editable="false" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>