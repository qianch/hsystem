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
    var codes = ${codes};
    var gz = $('input[name="productWeigh"]:checked').val();//称重规则的value值
    var x = $('input[name="productIsTc"]:checked').val();//是否是套材的value值

    function doChange(act) {
        gz = $('input[name="productWeigh"]:checked').val();
        x = $('input[name="productIsTc"]:checked').val();
        x = act;
        $("#productProcessBom").searchbox("setValue", "");
        $("#productProcessBomVersion").textbox("setValue", "");
        $("#packBomId").val("");
        $("#procBomId").val("");

        $("#productWeigh0").attr("disabled", true);
        $("#productWeigh1").attr("disabled", true);
        $("#productWeigh2").attr("disabled", true);

        switch (act) {
            case -1://成品胚布
                $("#productWeigh2").removeAttr("disabled");
                $("#productWeigh2").attr("checked", true);
                $("#packBomId").val("-1");

                if (gz == 2) {
                    $("#productRollWeight").textbox({"required": false});
                }

                if ($("#productPackagingCode").val() == "") {
                    $("#productPackagingCode").searchbox({"readonly": true});
                }

                $("#carrierCode").searchbox({"required": false});
                $("#productPackagingCode").searchbox("setValue", "无包装");
                $("#productPackageVersion").textbox("setValue", "无包装");
                break;
            case 1://套才
                $("#productWeigh2").removeAttr("disabled");
                $("#productWeigh2").attr("checked", true);
                $("#productPackagingCode").searchbox("setValue", "");
                $("#productPackagingCode").searchbox({"required": true, "readonly": false});
                $("#productPackageVersion").textbox("setValue", "");
                $("#productPackageVersion").searchbox({"readonly": true, "required": true});
                $("#productRollWeight").textbox({"required": false});
                $("#carrierCode").searchbox({"required": false});
                break;
            case 2://非套才
                $("#productWeigh0").removeAttr("disabled");
                $("#productWeigh1").removeAttr("disabled");
                $("#productWeigh2").removeAttr("disabled");
                $("#productPackagingCode").searchbox("setValue", "");
                $("#productPackagingCode").searchbox({"required": true, "readonly": false});
                $("#productPackageVersion").textbox("setValue", "");
                $("#productPackageVersion").searchbox({"readonly": false, "required": false, "editable": false});

                $("#carrierCode").searchbox({"required": true});
                if (gz == 2) {
                    $("#productRollWeight").textbox({"required": true});
                    $("#carrierCode").searchbox({"required": false});
                }
                break;
            default:
                break;
        }
    }

    //选择不称规则时 卷重为必选项
    function Change(cz) {
        if (cz == 2) {
            if ($("#carrierCode").val() == "") {
                $("#carrierCode").textbox({
                    "required": false
                });
            }

            if (x == 1 || x == -1) {//选择套材及不称规则是卷重不填
                $("#productRollWeight").textbox({
                    "required": false
                });
            } else {
                $("#productRollWeight").textbox({
                    "required": true
                });

            }

        } else {
            if ((cz == 0 || cz == 1 || cz == 3) && x == 2) {
                if ($("#carrierCode").val() == "") {
                    $("#carrierCode").searchbox({
                        "required": true
                    });
                }
            } else {
                $("#carrierCode").searchbox({
                    "required": false
                });
            }

            $("#productRollWeight").textbox({
                "required": false
            });
        }

    }

    /**
     * 选择成品类别
     */
    var selectorProductCategoryUrl = path
        + "finishProduct/selectorProductCategory";
    var selectPcType = function () {
        selectorDialog = Dialog.open("选择成品类别", 600, 400,
            selectorProductCategoryUrl, [
                EasyUI.window.button("icon-save", "选择", function () {
                    var r = EasyUI.grid
                        .getOnlyOneSelected("category_dg");
                    //成品类别
                    $("#pcCode").searchbox("setValue", r.CATEGORYNAME);
                    //成品编号
                    $("#pcType").textbox("setValue", r.CATEGORYCODE);
                    //成品类别ID
                    $("#product_category_ID").attr("value", r.ID);
                    Dialog.close(selectorDialog);
                }),
                EasyUI.window.button("icon-cancel", "取消", function () {
                    Dialog.close(selectorDialog)
                })]);
    };

    /**
     * 选择衬管编码
     */
    var selectorCarrierCodeUrl = path + "finishProduct/selectorCarrierCode";
    var selectorCarrierCode = function () {
        selectorDialog = Dialog.open("选择衬管编码", 600, 400,
            selectorCarrierCodeUrl, [
                EasyUI.window.button("icon-save", "选择",
                    function () {
                        var r = EasyUI.grid
                            .getOnlyOneSelected("weight_dg");
                        //衬管编码
                        $("#carrierCode").searchbox("setValue",
                            r.CARRIERCODE);
                        //衬管名称
                        $("#pcType1").textbox("setValue", r.CARRIERNAME);
                        //重量
                        $("#carrierWeight").attr("value", r.CARRIERWEIGHT);
                        //衬管id
                        $("#carrier_ID").attr("value", r.ID);
                        Dialog.close(selectorDialog);
                    }),
                EasyUI.window.button("icon-cancel", "取消", function () {
                    Dialog.close(selectorDialog)
                })]);
    };

    //厂内名称为：产品规格-门幅（mm）-卷长（m）/卷重（kg）
    $("#productWidth")
        .numberbox(
            {
                "onChange": function () {
                    var productModel = $("#productModel").textbox(
                        'getValue');
                    var productWidth = $("#productWidth").numberbox(
                        'getValue');
                    var productRollLength = $("#productRollLength")
                        .numberbox('getValue');
                    var productRollWeight = $("#productRollWeight")
                        .numberbox('getValue');
                    var factoryProductName = "";
                    if (productWidth != "" && productWidth != undefined
                        && productWidth != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productWidth + "mm";
                        } else {
                            factoryProductName += "-" + productWidth
                                + "mm";
                        }

                    }
                    if (productRollLength != ""
                        && productRollLength != undefined
                        && productRollLength != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productRollLength
                                + "mm";
                        } else {
                            factoryProductName += "-"
                                + productRollLength + "m";
                        }
                    }
                    if (productRollWeight != ""
                        && productRollWeight != undefined
                        && productRollWeight != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productRollWeight
                                + "kg";
                        } else if (factoryProductName != ""
                            && productRollLength != ""
                            && productRollLength != undefined
                            && productRollLength != null) {
                            factoryProductName += "/"
                                + productRollWeight + "kg";
                        } else if (factoryProductName != ""
                            && (productRollLength == ""
                                || productRollLength == undefined || productRollLength == null)) {
                            factoryProductName += "-"
                                + productRollWeight + "kg";
                        }
                    }
                    if (productModel != "" && productModel != undefined
                        && productModel != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productModel;
                        } else {
                            factoryProductName = productModel + "-"
                                + factoryProductName;
                        }
                    }
                    $("#factoryProductName").textbox('setValue',
                        factoryProductName);

                    if (productWidth != "" && productWidth != undefined
                        && productWidth != null && productRollLength != ""
                        && productRollLength != undefined
                        && productRollLength != null) {
                        var maxpersent = $("#maxWeightSpinner").numberbox('getValue');
                        changeMaxWeight(maxpersent);
                        var minpersent = $("#minWeightSpinner").numberbox('getValue');
                        changeMinWeight(minpersent);
                    }

                }
            });

    $("#productRollLength")
        .numberbox(
            {
                "onChange": function () {
                    var productModel = $("#productModel").textbox(
                        'getValue');
                    var productWidth = $("#productWidth").numberbox(
                        'getValue');
                    var productRollLength = $("#productRollLength")
                        .numberbox('getValue');
                    var productRollWeight = $("#productRollWeight")
                        .numberbox('getValue');
                    var factoryProductName = "";
                    if (productWidth != "" && productWidth != undefined
                        && productWidth != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productWidth + "mm";
                        } else {
                            factoryProductName += "-" + productWidth
                                + "mm";
                        }

                    }
                    if (productRollLength != ""
                        && productRollLength != undefined
                        && productRollLength != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productRollLength
                                + "mm";
                        } else {
                            factoryProductName += "-"
                                + productRollLength + "m";
                        }
                    }

                    if (productRollWeight != ""
                        && productRollWeight != undefined
                        && productRollWeight != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productRollWeight
                                + "kg";
                        } else if (factoryProductName != ""
                            && productRollLength != ""
                            && productRollLength != undefined
                            && productRollLength != null) {
                            factoryProductName += "/"
                                + productRollWeight + "kg";
                        } else if (factoryProductName != ""
                            && (productRollLength == ""
                                || productRollLength == undefined || productRollLength == null)) {
                            factoryProductName += "-"
                                + productRollWeight + "kg";
                        }
                    }

                    if (productModel != "" && productModel != undefined
                        && productModel != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productModel;
                        } else {
                            factoryProductName = productModel + "-"
                                + factoryProductName;
                        }
                    }
                    $("#factoryProductName").textbox('setValue',
                        factoryProductName);

                    if (productWidth != "" && productWidth != undefined
                        && productWidth != null && productRollLength != ""
                        && productRollLength != undefined
                        && productRollLength != null) {
                        var maxpersent = $("#maxWeightSpinner").numberbox('getValue');
                        changeMaxWeight(maxpersent);
                        var minpersent = $("#minWeightSpinner").numberbox('getValue');
                        changeMinWeight(minpersent);
                    }


                }
            });

    $("#productRollWeight")
        .numberbox(
            {
                "onChange": function () {
                    var productModel = $("#productModel").textbox(
                        'getValue');
                    var productWidth = $("#productWidth").numberbox(
                        'getValue');
                    var productRollLength = $("#productRollLength")
                        .numberbox('getValue');
                    var productRollWeight = $("#productRollWeight")
                        .numberbox('getValue');
                    var factoryProductName = "";

                    if (productWidth != "" && productWidth != undefined
                        && productWidth != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productWidth + "mm";
                        } else {
                            factoryProductName += "-"
                                + productRollWeight + "mm";
                        }
                        if (productRollLength != ""
                            && productRollLength != undefined
                            && productRollLength != null) {
                            if (factoryProductName == "") {
                                factoryProductName += productRollLength
                                    + "m ";
                            } else {
                                factoryProductName += "-"
                                    + productRollLength + "m";
                            }
                        }
                    }
                    if (productRollWeight != ""
                        && productRollWeight != undefined
                        && productRollWeight != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productRollWeight
                                + "kg";
                        } else if (factoryProductName != ""
                            && productRollLength != ""
                            && productRollLength != undefined
                            && productRollLength != null) {
                            factoryProductName += "/"
                                + productRollWeight + "kg";
                        } else if (factoryProductName != ""
                            && (productRollLength == ""
                                || productRollLength == undefined || productRollLength == null)) {
                            factoryProductName += "-"
                                + productRollWeight + "kg";
                        }

                        var maxpersent = $("#maxWeightSpinner").numberbox('getValue');
                        changeMaxWeight(maxpersent);
                        var minpersent = $("#minWeightSpinner").numberbox('getValue');
                        changeMinWeight(minpersent);
                    }

                    if (productRollWeight == null
                        || productRollWeight == "") {
                        $("#maxWeight").numberbox('clear');
                        $("#maxWeight").numberbox('disableValidation');
                        $("#minWeight").numberbox('clear');
                        $("#minWeight").numberbox('disableValidation');
                    } else if (productRollWeight != null
                        || productRollWeight != "") {
                        $("#maxWeight").numberbox({
                            required: true
                        });
                        $("#minWeight").numberbox({
                            required: true
                        });
                    }

                    if (productModel != "" && productModel != undefined
                        && productModel != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productModel;
                        } else {
                            factoryProductName = productModel + "-"
                                + factoryProductName;
                        }
                    }
                    $("#factoryProductName").textbox('setValue',
                        factoryProductName);
                }
            });

    $("#productModel")
        .textbox(
            {
                "onChange": function () {
                    var productModel = $("#productModel").textbox(
                        'getValue');
                    var productWidth = $("#productWidth").numberbox(
                        'getValue');
                    var productRollLength = $("#productRollLength")
                        .numberbox('getValue');
                    var productRollWeight = $("#productRollWeight")
                        .numberbox('getValue');
                    var factoryProductName = "";
                    if (productWidth != "" && productWidth != undefined
                        && productWidth != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productWidth + "mm";
                        } else {
                            factoryProductName += "-" + productWidth
                                + "mm";
                        }

                    }
                    if (productRollLength != ""
                        && productRollLength != undefined
                        && productRollLength != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productRollLength
                                + "mm";
                        } else {
                            factoryProductName += "-"
                                + productRollLength + "m";
                        }
                    }
                    if (productRollWeight != ""
                        && productRollWeight != undefined
                        && productRollWeight != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productRollWeight
                                + "kg";
                        } else if (factoryProductName != ""
                            && productRollLength != ""
                            && productRollLength != undefined
                            && productRollLength != null) {
                            factoryProductName += "/"
                                + productRollWeight + "kg";
                        } else if (factoryProductName != ""
                            && (productRollLength == ""
                                || productRollLength == undefined || productRollLength == null)) {
                            factoryProductName += "-"
                                + productRollWeight + "kg";
                        }
                    }
                    if (productModel != "" && productModel != undefined
                        && productModel != null) {
                        if (factoryProductName == "") {
                            factoryProductName += productModel;
                        } else {
                            factoryProductName = productModel + "-"
                                + factoryProductName;
                        }
                    }
                    $("#factoryProductName").textbox('setValue',
                        factoryProductName);
                }
            });

    $(function () {
        $("#maxWeight").numberbox({
            required: false
        });
        $("#minWeight").numberbox({
            required: false
        });
    })


    function hqwlbm() {
        //产品类别
        var lbval = $("#pcType").val();
        //var gyval = $("#productProcessBom").val();

        //产品规格
        var cpgg = $("#productModel").val();

        //门幅
        var mf = $("#productWidth").val();

        //卷长
        var jc = $("#productRollLength").val();

        //卷重
        var jz = $("#productRollWeight").val();


        if (lbval == undefined || lbval == "" || lbval == null) {
            Tip.warn("未选择成品类别!");
            return;
        }
        ;

        if (cpgg == undefined || cpgg == "" || cpgg == null) {
            Tip.warn("产品规格为空!");
            return;
        }
        ;


        if (mf == undefined || mf == "" || mf == null) {
            Tip.warn("门幅为空!");
            return;
        }
        ;

        if ((jc == undefined || jc == "" || jc == null) && (jz == undefined || jz == "" || jz == null)) {
            Tip.warn("卷重和卷长不能都为空!");
            return;
        }
        ;


        $.ajax({
            url: path + "finishProduct/hqwlbm?lbval=" + lbval + "&cpgg=" + cpgg + "&mf=" + mf + "&jc=" + jc + "&jz=" + jz,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                $("#materielCode").textbox("setValue", data);
            },
            error: function () {
            }
        });


    }

    function changeMaxWeight(value) {
        var productRollWeight = $("#productRollWeight").numberbox('getValue');
        var productWeight = $("#productWeight").val();
        var bomWeight = $("#bomWeight").val();
        if (productRollWeight != "") {
            var maxWeight = productRollWeight * (100 + Math.abs(value)) / 100;
            $("#maxWeight").numberbox('setValue', maxWeight);
        } else if (bomWeight != "" && bomWeight != "0") {
            var productWidth = $("#productWidth").numberbox('getValue');
            var productRollLength = $("#productRollLength").numberbox('getValue');
            var maxWeight = (bomWeight * productWidth * productRollLength) / 1000000 * (100 + Math.abs(value)) / 100;
            maxWeight = maxWeight.toFixed(2);
            $("#maxWeight").numberbox('setValue', maxWeight);
        } else if (productWeight != "" && productWeight != "0") {
            var productWeight = $("#productWeight").val();
            var maxWeight = productWeight * (100 + Math.abs(value)) / 100;
            $("#maxWeight").numberbox('setValue', maxWeight);
        }
    }

    function changeMinWeight(value) {
        var productRollWeight = $("#productRollWeight").numberbox('getValue');
        var productWeight = $("#productWeight").val();
        var bomWeight = $("#bomWeight").val();
        if (productRollWeight != "") {
            var minWeight = productRollWeight * (100 - Math.abs(value)) / 100;
            $("#minWeight").numberbox('setValue', minWeight);
        } else if (bomWeight != "" && bomWeight != "0") {
            var productWidth = $("#productWidth").numberbox('getValue');
            var productRollLength = $("#productRollLength").numberbox('getValue');
            var minWeight = (bomWeight * productWidth * productRollLength) / 1000000 * (100 - Math.abs(value)) / 100;
            minWeight = minWeight.toFixed(2);
            $("#minWeight").numberbox('setValue', minWeight);
        } else if (productWeight != "" && productWeight != "0") {
            var productWeight = $("#productWeight").val();
            var minWeight = productWeight * (100 - Math.abs(value)) / 100;
            $("#minWeight").numberbox('setValue', minWeight);
        }
    }

    function calcWeight() {
        var productWidth = $("#productWidth").numberbox('getValue');
        var productRollLength = $("#productRollLength").numberbox('getValue');
        var productProcessBom = $("#productProcessBom").val();
        var procBomId = $("#procBomId").val();
        if (productWidth == undefined || productWidth == "" || productWidth == null) {
            Tip.warn("请填写门幅!");
            return;
        }
        if (productRollLength == undefined || productRollLength == "" || productRollLength == null) {
            Tip.warn("请填写卷长!");
            return;
        }
        if (productProcessBom == undefined || productProcessBom == "" || productProcessBom == null) {
            Tip.warn("请填写工艺代码!");
            return;
        }

        $.ajax({
            url: path + "finishProduct/calcWeight?procBomId=" + procBomId,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                var weight = (data * productWidth * productRollLength) / 1000000;
                weight = weight.toFixed(1);
                $("#productRollWeight").numberbox('setValue', weight);
                var maxpersent = $("#maxWeightSpinner").numberbox('getValue');
                changeMaxWeight(maxpersent);
                var minpersent = $("#minWeightSpinner").numberbox('getValue');
                changeMinWeight(minpersent);
            },
            error: function () {
            }
        });
    }
    function calcGrossWeight() {

        var productRollWeight = $("#productRollWeight").numberbox('getValue');
        var carrierWeight = $("#carrierWeight").val();
        if (productRollWeight == undefined || productRollWeight == "" || productRollWeight == null) {
            Tip.warn("请先计算卷重!");
            return;
        }
        if (carrierWeight == undefined || carrierWeight == "" || carrierWeight == null) {
            Tip.warn("请先选择载具!");
            return;
        }
        var weight = parseFloat(productRollWeight)+parseFloat(carrierWeight);
        $("#rollGrossWeight").numberbox('setValue', weight);
    }
</script>
<div>


    <!--成品信息表单-->
    <form id="finishProductForm" method="post" ajax="true"
          action="<%=basePath %>finishProduct/${empty finishProduct.id ?'add':'edit'}"
          autocomplete="off">

        <input type="hidden" name="id" value="${finishProduct.id}"/> <input
            type="hidden" id="packBomId" name="packBomId"
            value="${finishProduct.packBomId} "> <input type="hidden"
                                                        id="obsolete" name="obsolete"
                                                        value="${finishProduct.obsolete} ">
        <!--成品客户信息ID-->
        <input type="hidden" id="productConsumerId" name="productConsumerId"
               value="${finishProduct.productConsumerId}"/>
        <input type="hidden" name="productWeight" id="productWeight" value="${productWeight}"/>
        <input type="hidden" name="bomWeight" id="bomWeight" value="${bomWeight}"/>
        <table width="100%">
            <tr>
                <td class="title">客户名称:</td>
                <!--客户名称-->
                <td><input id="productConsumerName" class="easyui-searchbox"
                           value="${consumer.consumerName}" editable="false" required="true"
                           data-options="searcher:ChooseConsumer" style="width:360px"></td>

                <td class="title">客户代码:</td>
                <!--客户代码-->
                <td><input id="productConsumerCode" class="easyui-textbox"
                           reqiured="true" value="${consumer.consumerCode}"
                           style="width:360px"></td>
            </tr>
            <tr>
                <td class="title">客户产品名称:</td>
                <!--客户产品名称-->
                <td><input type="text" id="consumerProductName"
                           name="consumerProductName"
                           value="${finishProduct.consumerProductName}" class="easyui-textbox"
                           required="true" validType:['length[0,150]']" style="width:360px">
                </td>

                <td class="title">厂内名称:</td>
                <!--厂内名称-->
                <td><input type="text" id="factoryProductName"
                           name="factoryProductName"
                           value="${finishProduct.factoryProductName}" class="easyui-textbox"
                           data-options="validType:['length[0,150]']" style="width:360px"
                           readlonly="true" editable="false"></td>
            </tr>
            <tr>
                <td class="title">门幅(mm):</td>
                <!--门幅-->
                <td><input type="text" id="productWidth" name="productWidth"
                           value="${finishProduct.productWidth}" class="easyui-numberbox"
                           precision="1" style="width:360px"></td>

                <td class="title">卷长(m):</td>
                <!--卷长-->
                <td><input type="text" id="productRollLength"
                           name="productRollLength" value="${finishProduct.productRollLength}"
                           class="easyui-numberbox" precision="2" style="width:360px"></td>
            </tr>
            <tr>
                <td class="title">预留长度(m):</td>
                <!--预留长度-->
                <td><input type="text" id="reserveLength" required="true"
                           name="reserveLength" value="${finishProduct.reserveLength}"
                           class="easyui-numberbox" precision="2" style="width:360px"></td>
                <td class="title">客户物料号:</td>
                <!--客户物料号-->
                <td><input type="text" id="customerMaterialCodeOfFP"
                           name="customerMaterialCodeOfFP" value="${finishProduct.customerMaterialCodeOfFP}"
                           class="easyui-textbox" precision="2" style="width:360px"></td>
            </tr>

            <tr>
                <td class="title">卷重(kg):</td>
                <!--卷重-->
                <!-- suffix="Kg" -->
                <td><input type="text" id="productRollWeight"
                           name="productRollWeight" value="${finishProduct.productRollWeight}"
                           class="easyui-numberbox" onchange="fillfactoryProductName()"
                           precision="1" style="width:300px">
                    <a
                            href="javascript:void(0)"
                            class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                            onclick="calcWeight()"> 计算</a></td>

                <td class="title">托唛头:</td>
                <!--托唛头代码-->
                <td><input type="text" id="productTrayCode"
                           name="productTrayCode" value="${finishProduct.productTrayCode}"
                           class="easyui-textbox" style="width:360px"></td>
            </tr>
            <tr id="isShowWeight">
                <td class="title">最大卷重(kg):</td>
                <!--最大卷重-->
                <td><input type="text" id="maxWeight" name="maxWeight"
                           value="${finishProduct.maxWeight}" class="easyui-numberbox"
                           required="true" precision="2" style="width:300px">
                    <input id="maxWeightSpinner" class="easyui-numberspinner" value="3"
                           data-options="increment:1,min:0,max:5,onChange:changeMaxWeight" style="width:45px;"></input>%
                </td>

                <td class="title">最小卷重(kg):</td>
                <!--最小卷重-->
                <td><input type="text" id="minWeight" name="minWeight"
                           value="${finishProduct.minWeight}" class="easyui-numberbox"
                           required="true" precision="2" style="width:300px">
                    <input id="minWeightSpinner" class="easyui-numberspinner" value="-3"
                           data-options="increment:1,min:-5,max:0,onChange:changeMinWeight" style="width:45px;"></input>%
                </td>
            </tr>
            <!-- onkeyup="fillfactoryProductName()" -->
            <tr>
                <td class="title">卷标签:</td>
                <!--卷标签代码-->
                <td><input type="text" id="productRollCode"
                           name="productRollCode" value="${finishProduct.productRollCode}"
                           class="easyui-textbox" style="width:360px"></td>

                <td class="title">箱唛头:</td>
                <!--箱唛头代码-->
                <td><input type="text" id="productBoxCode"
                           name="productBoxCode" value="${finishProduct.productBoxCode}"
                           class="easyui-textbox" style="width:360px"></td>
            </tr>
            <tr>

                <td class="title">产品类型:</td>
                <td><label for="productIsTc1"><input id="productIsTc1"
                                                     type="radio" value="1" onclick="doChange(1)" name="productIsTc"
                ${finishProduct.productIsTc eq 1 ?'checked':''}> 套材</label>&nbsp <label
                        for="productIsTc2"><input id="productIsTc2" type="radio"
                                                  value="2" onclick="doChange(2)" name="productIsTc"
                ${finishProduct.productIsTc eq 2 ?'checked':''}> 非套材</label> <label
                        for="productIsTc3"><input id="productIsTc3" type="radio"
                                                  value="-1" onclick="doChange(-1)" name="productIsTc"
                ${finishProduct.productIsTc eq -1|| finishProduct.productIsTc eq null  ?'checked':''}>
                    胚布</label></td>
                <td class="title">产品属性:</td>
                <td><label for="productIsComm1"><input
                        id="productIsComm1" type="radio" value="1" name="isCommon"
                ${finishProduct.isCommon eq 1 || finishProduct.isCommon eq null  ?'checked':''}>
                    常规</label>&nbsp <label for="productIsComm0"><input
                        id="productIsComm0" type="radio" value="0" name="isCommon"
                ${finishProduct.isCommon eq 0 ?'checked':''}> 试样</label></td>
            </tr>
            <tr>
                <td class="title">工艺名称:</td>
                <td><input type="text" id="productProcessName"
                           name="productProcessName"
                           value="${finishProduct.productProcessName}" class="easyui-textbox"
                           data-options="validType:['length[0,150]']" style="width:360px"
                           readonly="true"></td>
                <td class="title">保质期(天):</td>
                <!--备注-->
                <td><input type="text" id="productShelfLife"
                           name="productShelfLife" value="${finishProduct.productShelfLife}"
                           class="easyui-numberbox" required="true" validType="length[1,5]"
                           min="1" precision="0" style="width:360px"></td>

            </tr>
            <tr>
                <td class="title">客户版本号:</td>
                <td><input type="text" id="productConsumerBomVersion"
                           name="productConsumerBomVersion"
                           value="${finishProduct.productConsumerBomVersion}" class="easyui-textbox"
                           data-options="validType:['length[0,150]']" style="width:360px"
                           readonly="true"></td>
                <td class="title">卷毛重(kg):</td>
                <!--卷重-->
                <!-- suffix="Kg" -->
                <td><input type="text" id="rollGrossWeight"
                           name="rollGrossWeight"
                           value="${finishProduct.rollGrossWeight eq null?0:finishProduct.rollGrossWeight}"
                           class="easyui-numberbox"
                           precision="1" style="width:300px">
                    <a
                            href="javascript:void(0)"
                            class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                            onclick="calcGrossWeight()"> 计算</a>
                </td>

            </tr>
            <%-- <tr>
                <td class="title">是否为胚布</td>
                <td><input id="productIsFab2"
                    type="radio" value="0" name="isShow"
                    ${finishProduct.isShow eq 0 || finishProduct.isShow eq null ?'checked':''}>
                    <label for="productIsFab2">产品</label>&nbsp
                <input id="productIsFab1" type="radio" name="isShow"
                    value="1" ${finishProduct.isShow eq 1  ?'checked':''}> <label
                    for="productIsFab1">胚布</label></td>
            </tr> --%>
            <tr>
                <td class="title">工艺代码:</td>
                <!--工艺标准代码-->
                <td><input id="productProcessBom" name="productProcessCode"
                           value="${finishProduct.productProcessCode}"
                           class="easyui-searchbox" editable="false" required="true"
                           data-options="searcher:chooseBom" style="width:360px"></td>
                <td class="title">工艺版本:</td>
                <!--工艺标准版本-->
                <input type="hidden" id="procBomId" name="procBomId"
                       value="${finishProduct.procBomId} " style="width:360px">
                <td><input type="text" id="productProcessBomVersion"
                           name="productProcessBomVersion" readonly="true" required="true"
                           value="${finishProduct.productProcessBomVersion}"
                           class="easyui-textbox" style="width:360px"></td>

            </tr>
            <tr>
                <td class="title">包装代码:</td>
                <!--包装标准代码-->
                <td><input type="text" id="productPackagingCode"
                           name="productPackagingCode"
                           value="${empty finishProduct.productPackagingCode?'无包装':finishProduct.productPackagingCode}"
                ${finishProduct.productIsTc eq -1||finishProduct.productIsTc eq null ?'readonly':''}
                           class="easyui-searchbox" editable="false"
                ${finishProduct.productIsTc eq -1 ?'':'required=\"true\"'}
                           data-options="searcher:choosePackingBom" style="width:360px">
                </td>
                <td class="title">包装版本:</td>
                <!--包装标砖版本-->
                <td><input type="text" id="productPackageVersion"
                           name="productPackageVersion" readonly="true"
                           value="${empty finishProduct.productPackageVersion?'无包装':finishProduct.productPackageVersion}"
                           class="easyui-textbox" editable="false" style="width:360px"></td>

            </tr>
            <tr>
                <td class="title">衬管编码:</td>
                <!--衬管编码-->
                <%-- <td>
                    <input type="text" id="carrierCode" name="carrierCode" value="${finishProduct.carrierCode}"  class="easyui-combobox"   }
                    data-options="icons:[],filter:comboFilter,required:false,data:codes,valueField:'v',textField:'t',editable:true,onHidePanel:validCode,panelHeight:'150'"  >
                </td> --%>
                <td><input type="text" id="carrierCode" name="carrierCode"
                           value="${finishProduct.carrierCode}" class="easyui-searchbox"
                           editable="false" data-options="searcher:selectorCarrierCode"
                           style="width:360px"/> <input type="hidden" id="carrier_ID"
                                                        name="carrierID" value="${finishProduct.id}"/></td>
                <td class="title">成品类别代码：</td>
                <td><input type="text" id="pcType" name="categoryName"
                           value="${productCategory.categoryCode}" class="easyui-searchbox"
                           editable="false" required="true"
                           data-options="searcher:selectPcType" style="width:360px"/> <input
                        type="hidden" id="product_category_ID" name="productCategoryID"
                        value="${productCategory.id}"/></td>
            </tr>
            <tr>
                <td class="title">成品类别名称:</td>
                <td><input type="text" id="pcCode" name="categoryCode"
                           value="${productCategory.categoryName}" class="easyui-textbox"
                           style="width:360px" readonly="readonly"></td>

                <td class="title">物料编码:</td>
                <td><input type="text" id="materielCode" name="materielCode"
                           value="${finishProduct.materielCode}" class="easyui-textbox"
                           style="width:250px" required="true">
                    <a
                            href="javascript:void(0)"
                            class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                            onclick="hqwlbm()"> 获取物料编码</a>

                </td>
            </tr>
            <tr>
                <td class="title">衬管规格:</td>
                <td><input type="text" id="pcType1" name="zgmc"
                           value="${zgmc}" class="easyui-textbox"
                           style="width:360px" readonly="readonly">
                    <input type="hidden" id="carrierWeight" name="carrierWeight"
                           value="${carrierWeight}">
                </td>

                <td class="title">产品规格:</td>
                <td><input type="text" id="productModel" name="productModel"
                           value="${finishProduct.productModel}" class="easyui-textbox"
                           required="true" data-options="validType:['length[0,150]']"
                           style="width:360px"></td>

            </tr>
            <!-- onkeyup="fillfactoryProductName()" -->
            <tr>
                <td class="title">包装要求:</td>
                <!--备注-->
                <td style="padding-left:10px"><textarea id="packReq"
                                                        name="packReq" style="width:90%;height:100px;" maxlength="500"
                                                        placeholder="限500字以内">${finishProduct.packReq}</textarea></td>
                <td class="title">称重规则:</td>
                <td>
                    <label for="productWeigh0">
                        <input id="productWeigh0" type="radio" value="0"
                               name="productWeigh" ${finishProduct.productIsTc eq 2?'':'disabled'}
                               onclick="Change(0)" ${finishProduct.productWeigh eq 0  ?'checked':''}>
                        全称 (单卷重量300KG以上)
                    </label>

                    <br>
                    <label for="productWeigh1">
                        <input  ${finishProduct.productIsTc eq 2?'':'disabled'} id="productWeigh1" type="radio"
                                                                                value="1" name="productWeigh"
                                                                                onclick="Change(1)" ${finishProduct.productWeigh eq 1 ?'checked':''}>
                        抽称(单卷重量300KG以下)
                    </label>


                    <br>
                    <label for="productWeigh2">
                        <input id="productWeigh2" type="radio" value="2" name="productWeigh"
                               onclick="Change(2)"  ${finishProduct.productWeigh eq 2 || finishProduct.productWeigh eq null ?'checked':''}>
                        不称 (套裁、胚布)
                    </label>

                    <br>


                    <%-- <label for="productWeigh3"><input id="productWeigh3"
                        type="radio" value="3" name="productWeigh" onclick="Change(3)"
                        ${finishProduct.productWeigh eq 3 ?'checked':''}> 首卷称重
                        (胚布)</label> --%></td>
            </tr>


            <tr>
                <td class="title" style="width:360px">备注:</td>
                <!--备注-->
                <td style="padding-left:10px"><textarea id="productMemo"
                                                        name="productMemo"
                                                        style="width:90%;height:100px;">${finishProduct.productMemo}</textarea>
                </td>
                <td class="title">工艺要求:</td>
                <!--备注-->
                <td style="padding-left:10px"><textarea id="procReq"
                                                        name="procReq" style="width:90%;height:100px;" maxlength="500"
                                                        placeholder="限500字以内">${finishProduct.procReq}</textarea></td>
            </tr>
        </table>
    </form>
</div>

<div id="packageType" class="easyui-dialog" title="选择包装方式"
     style="width:350px;height:120px;"
     data-options="
                closed:true,
                resizable:false,modal:true,buttons: [{
                    text:'确定',
                    iconCls:'icon-ok',
                    handler:function(){
                        var code=$('#packageTypeCombo').combobox('getValue');
                        if(isEmpty(code)){
                            Tip.warn('请选择包装方式');
                            return;
                        }
                        $('#productPackagingCode').searchbox('setValue',code);
                        $('#packageType').dialog('close');
                    }
                },{
                    text:'取消',
                    handler:function(){
                        $('#packageType').dialog('close');
                    }
                }]">

    <input id="packageTypeCombo"
           value="${finishProduct.productProcessCode}" class="easyui-combobox"
           name="packageTypeCombo" style="width:100%;"
           data-options="
                    editable:true,
                    data:codes,
	                valueField: 'CODE',
	                textField: 'CODE',
	                filter:comboFilter,
	                onHidePanel:validCode,
	                formatter:function(row){
                       return row.CODE+'/'+row.NAME;
	                }
                    ">
</div>
