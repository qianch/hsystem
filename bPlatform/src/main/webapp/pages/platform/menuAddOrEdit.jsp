<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : (":" + request.getServerPort())) + path + "/";
    application.setAttribute("path", basePath);
    long nocache = new Date().getTime();
%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/material/easyui.css?_=<%=nocache %>>">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/icon.css?_=<%=nocache %>">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/color.css?_=<%=nocache %>">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/platform.css?_=<%=nocache %>">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/layer/skin/layer.css?_=<%=nocache %>">
    <script type="text/javascript" src="<%=basePath%>resources/json2.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/jquery/jquery-1.8.0.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.ajax.ext.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.cookie.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/easyui/jquery.easyui.min.js?_=<%=nocache %>"></script>
    <script type="text/javascript"
            src="<%=basePath%>resources/easyui/locale/easyui-lang-zh_CN.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/layer/layer.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.form.js?_=<%=nocache %>"></script>
    <script type="text/javascript"
            src="<%=basePath%>resources/jquery/valid/jquery.validate.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/easyui.validation.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/easyui.validation.ext.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/platform.validation.js?_=<%=nocache %>"></script>
    <script type="text/javascript"
            src="<%=basePath%>resources/jquery/valid/validate-methods.js?_=<%=nocache %>"></script>
    <script type="text/javascript"
            src="<%=basePath%>resources/jquery/valid/localization/messages_zh.min.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/uipack/ui.tip.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/uipack/ui.dialog.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/uipack/ui.loading.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/jquery.ext.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/easyui.ext.js?_=<%=nocache %>"></script>
    <script type="text/javascript">
        /**
         * 设置ajax不缓存
         */
        $.ajaxSetup({cache: false});

        /**
         * 自动搜索
         */
        var autoSearch = null;
        var autoSearchFunction;
        $(function () {
            $("form .textbox-text").keyup(function () {
                //searchboxname,textbox
                if ($("input[name='" + $(this).parent().prev().attr("textboxname") + "']").length != 0) {
                    $("input[name='" + $(this).parent().prev().attr("textboxname") + "']").val($(this).val());
                } else {
                    $("input[name='" + $(this).parent().prev().attr("searchboxname") + "']").val($(this).val());
                }

                clearTimeout(autoSearch);
                autoSearchFunction = $(this).parent().parent().attr("autoSearchFunction");
                if (autoSearchFunction) {
                    autoSearch = setTimeout(window[autoSearchFunction], 500);
                } else {
                    if (typeof filter !== "undefined")
                        autoSearch = setTimeout(filter, 500);
                }
            });
        });

        /**
         * 设置Layer的全局配置
         */
        layer.config({
            extend: ['../../resources/layer/skin/layer.ext.css'], //加载新皮肤
            shift: 0,
            skin: 'layui-layer-molv',
            shade: [0.3, '#9D9A9A']
        });
        const loginPath = "<%=basePath%>login";
        const path = "<%=basePath%>";
        const tipLoading = "<spring:message code="Tip.Loading" />";
        const tipSubmiting = "<spring:message code="Tip.Submiting" />";
        const tipInfo = "<spring:message code="Tip.Info" />";
        const tipYes = "<spring:message code="Tip.Yes" />";
        const tipNo = "<spring:message code="Tip.No" />";
        const tipLock = "<spring:message code="Tip.Lock" />";
        const tipUnlock = "<spring:message code="Tip.Unlock" />";
        const tipSuccess = "<spring:message code="Tip.Success" />";
        const tipError = "<spring:message code="Tip.Error" />";
        const tipConfirm = "<spring:message code="Tip.Confirm" />";
        const tipSelectATreeNode = "<spring:message code="Tip.SelectATreeNode" />";
        const tipSelectAtLeastOne = "<spring:message code="Tip.SelectAtLeastOne" />";
        const tipSelectOnlyOne = "<spring:message code="Tip.SelectOnlyOne" />";
        const buttonAddMore = "<spring:message code="Button.AddMore" />";
    </script>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/dropdown/css/msdropdown/dd.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/dropdown/css/msdropdown/skin2.css">
    <script type="text/javascript" src="<%=basePath%>resources/dropdown/js/msdropdown/jquery.dd.js"></script>
    <script type="text/javascript">
        let valid;
        let form;
        const currentIcon = "${menu.icon}";
        $(document).ready(function () {
            valid = $("#menu").validate({
                debug: false,
                rules: {
                    name: {
                        required: true,
                        maxlength: 32,
                        minlength: 2,
                        stringCheck: true
                    },
                    url: {
                        required: true
                    },
                    icon: {
                        required: false,
                        minlength: 3,
                        maxlength: 64
                    }
                },
                onfocusout: function (element) {
                    $(element).valid();
                }
            });
            form = $("#menu");
            $.ajax({
                url: "<%=basePath %>resources/themes/icon.json",
                type: "get",
                dataType: "json",
                success: function (data) {
                    $.each(data, function (index, e) {
                        if (currentIcon == e.style) {
                            $("#icon_select").append("<option value='" + e.style + "' data-image='<%=basePath %>" + e.img + "' selected>" + e.style + "</option>");
                        } else {
                            $("#icon_select").append("<option value='" + e.style + "' data-image='<%=basePath %>" + e.img + "'>" + e.style + "</option>");
                        }
                    });
                    $("#icon_select").msDropdown({
                        roundedBorder: true
                    });
                    $("#icon_select").change(function () {
                        $("#icon").val(this.value);
                    })
                }
            });
        });

        function doSelect() {
            var isButton = $("#isButton").combobox("getValue");
            if (isButton == 1) {
                $("#buttonCodeTr").show();
            } else {
                $("#buttonCode").val("");
                $("#buttonCodeTr").hide();
            }
        }
    </script>
</head>
<body>
<form id="menu" method="post" action="<%=basePath %>/menu/${action eq 'add'?'add':'edit'}" autocomplete="off">
    <input type="hidden" name="id" value="${menu.id}"/>
    <input type="hidden" name="parentId" value="${menu.parentId}"/>
    <input type="hidden" name="levelCount" value="${menu.levelCount }">
    <table width="100%">
        <tr>
            <td class="title"><span style="color:red;">*</span>名称:</td>
            <td><input type="text" id="name" name="name" value="${menu.name}" class="textbox"></td>
        </tr>
        <tr style="display:${(menu.levelCount eq 0||menu.levelCount eq 1||menu.levelCount eq 2)?'none':''}">
            <td class="title"><span style="color:red;">*</span>地址:</td>
            <td><input type="text" id="url" name="url" value="${menu.url}" class="textbox"></td>
        </tr>
        <tr>
            <td class="title">图标:</td>
            <td><input type="hidden" value="${menu.icon }" name="icon" id="icon">
                <select style="width: 176px;margin-left: 6;" class="" name="icon_select" id="icon_select">
                </select>
            </td>
        </tr>
        <tr>
            <td class="title">代码:</td>
            <td><input type="text" id="code" name="code" value="${menu.code}" readonly="readonly" class="textbox"></td>
        </tr>
        <tr style="display:${(menu.levelCount eq 0||menu.levelCount eq 1||menu.levelCount eq 2)?'none':''}">
            <td class="title">是否按钮:</td>
            <td>
                <select id="isButton" name="isButton" class="easyui-combobox"
                        data-options="panelHeight:'auto',editable:false,panelWidth:200,onSelect:doSelect"
                        style="width:176px;">
                    <option value="0" ${menu.isButton eq 0?'selected':''}>否</option>
                    <option value="1" ${menu.isButton eq 1?'selected':''}>是</option>
                </select>
            </td>
        </tr>
        <tr id="buttonCodeTr" style="display:${menu.isButton eq 0||empty menu.isButton?'none':''}">
            <td class="title">按钮ID:</td>
            <td>
                <input type="text" id="buttonCode" name="buttonCode" value="${menu.buttonCode}" class="textbox">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
