<%-- 资源文件页面 --%>
<%@page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    Locale local = new Locale("zh_cn");
    if (session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME) != null) {
        local = ((Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
    }
    String language = local.getLanguage();
    String country = local.getCountry();
    pageContext.setAttribute("language", language);
    pageContext.setAttribute("path", basePath);
%>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/color.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/platform.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/layer/skin/layer.css">
<script type="text/javascript" src="<%=basePath%>resources/json2.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery-1.8.0.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.ajax.ext.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/easyui/jquery.easyui.extentions.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/layer/layer.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/valid/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/ext/easyui.validation.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/ext/easyui.validation.ext.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/ext/platform.validation.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/valid/validate-methods.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/valid/localization/messages_zh.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/uipack/ui.tip.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/uipack/ui.dialog.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/uipack/ui.loading.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/ext/jquery.ext.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/ext/easyui.ext.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/ext/prototype.ext.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/utiljs/utils.js"></script>
<script>
    /**
     * 设置ajax不缓存
     */
    $.ajaxSetup({cache: false});

    /**
     * 设置Layer的全局配置
     */
    layer.config({
        extend: ['../../resources/layer/skin/layer.ext.css'], //加载新皮肤
        shift: 0,
        skin: 'layui-layer-molv',
        shade: [0.3, '#9D9A9A']
    });
    var loginPath = "<%=basePath%>login";
    var path = "<%=basePath%>";
    var language = "<%=language%>";
    var country = "<%=country%>";
    var tipLoading = "<spring:message code="Tip.Loading" />";
    var tipSubmiting = "<spring:message code="Tip.Submiting" />";
    var tipInfo = "<spring:message code="Tip.Info" />";
    var tipYes = "<spring:message code="Tip.Yes" />";
    var tipNo = "<spring:message code="Tip.No" />";
    var tipLock = "<spring:message code="Tip.Lock" />";
    var tipUnlock = "<spring:message code="Tip.Unlock" />";
    var tipSuccess = "<spring:message code="Tip.Success" />";
    var tipError = "<spring:message code="Tip.Error" />";
    var tipConfirm = "<spring:message code="Tip.Confirm" />";
    var tipSelectATreeNode = "<spring:message code="Tip.SelectATreeNode" />";
    var tipSelectAtLeastOne = "<spring:message code="Tip.SelectAtLeastOne" />";
    var tipSelectOnlyOne = "<spring:message code="Tip.SelectOnlyOne" />";
    var buttonAddMore = "<spring:message code="Button.AddMore" />";
    console.log("当前国家代码:" + country + ";语言:" + language);
</script>