<!--
作者:宋黎明
日期:2016-9-30 10:49:34
页面:成品信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<style type="text/css">
    textarea {
        border-radius: 5px;
    }
</style>
<script type="text/javascript">
    var x = -99;

    //JS代码
    function doChange(act) {
        if (x === act) return;
        x = act;

        $("#productProcessBom").searchbox("setValue", "");
        $("#productProcessBomVersion").textbox("setValue", "");

        $("#productPackagingCode").searchbox("setValue", "");
        $("#productPackageVersion").textbox("setValue", "");

        $("#packBomId").val("");
        $("#procBomId").val("");

        if (act === -1) {
            $("#packBomId").val("-1");

            $("#productPackagingCode").searchbox({"readonly": true});

            $("#productPackagingCode").searchbox("setValue", "无包装");
            $("#productPackageVersion").textbox("setValue", "无包装");
        } else {
            $("#productPackagingCode").searchbox({"readonly": false});
            $("#productPackagingCode").searchbox("setValue", "");
            $("#productPackageVersion").textbox("setValue", "");
        }
    }
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
        <table width="100%">
            <tr>
                <td class="title">客户名称:</td>
                <!--客户名称-->
                <td><input id="productConsumerName" class="easyui-searchbox"
                           value="${consumer.consumerName}" editable="false" required="true"
                           readonly="true"></td>

                <td class="title">客户代码:</td>
                <!--客户代码-->
                <td><input id="productConsumerCode" class="easyui-textbox" readonly="true" reqiured="true"
                           value="${consumer.consumerCode}"></td>
            </tr>
            <tr>
                <td class="title">客户产品名称:</td>
                <!--客户产品名称-->
                <td><input type="text" id="consumerProductName"
                           name="consumerProductName"
                           value="${finishProduct.consumerProductName}" class="easyui-textbox"
                           required="true" readonly="true"></td>

                <td class="title">厂内名称:</td>
                <!--厂内名称-->
                <td><input type="text" id="factoryProductName"
                           name="factoryProductName"
                           value="${finishProduct.factoryProductName}" class="easyui-textbox" required="true"
                           readonly="true"></td>
            </tr>
            <tr>
                <td class="title">门幅:</td>
                <!--门幅-->
                <td><input type="text" id="productWidth" name="productWidth"
                           value="${finishProduct.productWidth}" class="easyui-numberbox"
                           suffix="mm" precision="2" required="true" readonly="true"></td>

                <td class="title">卷长:</td>
                <!--卷长-->
                <td><input type="text" id="productRollLength"
                           name="productRollLength" value="${finishProduct.productRollLength}"
                           class="easyui-numberbox" suffix="m" required="true" precision="2"></td>
            </tr>
            <tr>
                <td class="title">卷重:</td>
                <!--卷重-->
                <!-- suffix="Kg" -->
                <td><input type="text" id="productRollWeight"
                           name="productRollWeight" value="${finishProduct.productRollWeight}"
                           class="easyui-numberbox" precision="1" readonly="true"></td>

                <td class="title">托唛头:</td>
                <!--托唛头代码-->
                <td><input type="text" id="productTrayCode"
                           name="productTrayCode" value="${finishProduct.productTrayCode}"
                           class="easyui-textbox" readonly="true"></td>
            </tr>

            <tr>
                <td class="title">卷标签:</td>
                <!--卷标签代码-->
                <td><input type="text" id="productRollCode"
                           name="productRollCode" value="${finishProduct.productRollCode}"
                           class="easyui-textbox" readonly="true"></td>

                <td class="title">箱唛头:</td>
                <!--箱唛头代码-->
                <td><input type="text" id="productBoxCode"
                           name="productBoxCode" value="${finishProduct.productBoxCode}"
                           class="easyui-textbox" readonly="true"></td>
            </tr>
            <tr>
                <td class="title">产品规格:</td>
                <td><input type="text" id="productModel" name="productModel"
                           value="${finishProduct.productModel}" class="easyui-textbox"
                           required="true" readonly="true"></td>
                </td>
                <td class="title">产品类型:</td>
                <td>
                    <label for="productIsTc3"><input id="productIsTc3" type="radio" value="-1" name="productIsTc"
                                                     checked="checked">
                        胚布</label>
                </td>
            </tr>
            <tr>
                <td class="title">工艺代码:</td>
                <!--工艺标准代码-->
                <td><input id="productProcessBom" name="productProcessCode"
                           value="${finishProduct.productProcessCode}"
                           class="easyui-searchbox" editable="false" required="true"
                           readonly="true"></td>

                <td class="title">工艺版本:</td>
                <!--工艺标准版本-->
                <input type="hidden" id="procBomId" name="procBomId"
                       value="${finishProduct.procBomId} ">
                <td><input type="text" id="productProcessBomVersion"
                           name="productProcessBomVersion" readonly="true"
                           value="${finishProduct.productProcessBomVersion}"
                           class="easyui-textbox" editable="false"></td>
            </tr>
            <tr>
                <td class="title">保质期:</td>
                <!--备注-->
                <td><input type="text" id="productShelfLife"
                           name="productShelfLife" value="${finishProduct.productShelfLife}"
                           class="easyui-numberbox" required="true" min="1" precision="0" readonly="true"></td>
                <td class="title">备注:</td>
                <!--备注-->
                <td style="padding-left:10px"><textarea id="productMemo"
                                                        name="productMemo" readonly="true"
                                                        style="width:90%;height:100px;">${finishProduct.productMemo}</textarea>
                </td>
            </tr>
            <tr>
                <td class="title">包装要求:</td>
                <!--备注-->
                <td style="padding-left:10px"><textarea id="packReq"
                                                        name="packReq" style="width:90%;height:100px;" maxlength="500"
                                                        readonly="true"
                                                        placeholder="限500字以内">${finishProduct.packReq}</textarea>
                </td>
                <td class="title">工艺要求:</td>
                <!--备注-->
                <td style="padding-left:10px"><textarea id="procReq"
                                                        name="procReq" style="width:90%;height:100px;" maxlength="500"
                                                        readonly="true"
                                                        placeholder="限500字以内">${finishProduct.procReq}</textarea>
                </td>
            </tr>
        </table>
    </form>
</div>