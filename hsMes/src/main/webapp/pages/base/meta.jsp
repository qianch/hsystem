<%@page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : (":" + request.getServerPort())) + path + "/";
    application.setAttribute("path", basePath);
    long nocache = new Date().getTime();
%>
<link rel="shortcut icon" href="resources/fav.ico"/>
<link rel="bookmark" href="resources/fav.ico"/>
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/material/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/layer/skin/layer.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/themes/all.min.css">
<script type="text/javascript" src="<%=basePath%>resources/js/a.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/easyui/jquery.easyui.extentions.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/layer/layer.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/constant/Formatter.js"></script>
<script>
    document.title = "恒石纤维基业MES系统";
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
        $("form").delegate(".textbox-text", "keyup", function () {
            //searchboxname,textbox
            if ($("input[name='" + $(this).parent().prev().attr("textboxname") + "']").length !== 0) {
                $("input[name='" + $(this).parent().prev().attr("textboxname") + "']").val($(this).val());
            } else {
                $("input[name='" + $(this).parent().prev().attr("searchboxname") + "']").val($(this).val());
            }
            clearTimeout(autoSearch);
            let ele = null;
            while (true) {
                ele = $(ele || this).parent();
                if ($(ele).is("form")) {
                    break;
                }
            }
            autoSearchFunction = $(ele).attr("autoSearchFunction");
            if (autoSearchFunction) {
                autoSearch = setTimeout(window[autoSearchFunction], 500);
            } else {
                if (typeof filter !== "undefined")
                    autoSearch = setTimeout(filter, 500);
            }
        });
    });

    function dictFormatter(type, value) {
        if (value === "undefined") {
            return "";
        }
        let result = value;
        $.ajax({
            url: "<%=basePath %>dict/queryDictText?type=" + type + "&value=" + value,
            type: "get",
            async: false,
            dataType: "text",
            success: function (ajaxdata) {
                result = ajaxdata;
            }
        });
        return result;
    }

    /**
     * 设置 EasyUI datagrid 首次不加载
     * */
    const dgOnBeforeLoad = function (param) {
        var firstLoad = $(this).attr("firstLoad");
        if (firstLoad === "false" || typeof (firstLoad) == "undefined") {
            $(this).attr("firstLoad", "true");
            return false;
        }
        return true;
    };

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
    const tipLoading = "数据加载，请稍等片刻......";
    const tipSubmiting = "数据正在提交中......";
    const tipInfo = "提示信息";
    const tipYes = "是";
    const tipNo = "否";
    const tipLock = "锁定";
    const tipUnlock = "未锁定";
    const tipSuccess = "操作成功";
    const tipError = "操作失败";
    const tipConfirm = "确认操作?";
    const tipSelectATreeNode = "请选择一个树节点";
    const tipSelectAtLeastOne = "请至少选择一行记录";
    const tipSelectOnlyOne = "请选择唯一一行记录";
    const buttonAddMore = "连续添加";
</script>