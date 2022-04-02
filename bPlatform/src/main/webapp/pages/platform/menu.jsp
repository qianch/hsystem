<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>菜单管理</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : (":" + request.getServerPort())) + path + "/";
        application.setAttribute("path", basePath);
        long nocache = new Date().getTime();
    %>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <script src="<%=basePath %>/resources/ext/jquery.js?_=<%=nocache %>"></script>
    <script src="<%=basePath %>/resources/ext/main.js?_=<%=nocache %>"></script>
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
            src="<%=basePath%>resources/easyui/jquery.easyui.extentions.js?_=<%=nocache %>"></script>
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
    <script type="text/javascript" src="<%=basePath%>resources/ext/prototype.ext.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/utiljs/utils.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/utiljs/Assert.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/utiljs/Calc.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/utiljs/Calendar.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/utiljs/Random.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/utiljs/Regex.js?_=<%=nocache %>"></script>
    <script type="text/javascript" src="<%=basePath%>resources/constant/Formatter.js?_=<%=nocache %>"></script>
    <script>
        /**
         * 设置ajax不缓存
         */
        $.ajaxSetup({cache: false});
        /**
         * 自动搜索
         */
        let autoSearch = null;
        let autoSearchFunction;
        $(function () {
            $("form .textbox-text").keyup(function () {
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
        const tipLoading = '<spring:message code="Tip.Loading"/>';
        const tipSubmiting = '<spring:message code="Tip.Submiting"/>';
        const tipInfo = '<spring:message code="Tip.Info"/>';
        const tipYes = '<spring:message code="Tip.Yes"/>';
        const tipNo = '<spring:message code="Tip.No"/>';
        const tipLock = '<spring:message code="Tip.Lock"/>';
        const tipUnlock = '<spring:message code="Tip.Unlock"/>';
        const tipSuccess = '<spring:message code="Tip.Success"/>';
        const tipError = '<spring:message code="Tip.Error"/>';
        const tipConfirm = '<spring:message code="Tip.Confirm"/>';
        const tipSelectATreeNode = '<spring:message code="Tip.SelectATreeNode"/>';
        const tipSelectAtLeastOne = '<spring:message code="Tip.SelectAtLeastOne"/>';
        const tipSelectOnlyOne = '<spring:message code="Tip.SelectOnlyOne"/>';
        const buttonAddMore = '<spring:message code="Button.AddMore"/>';

    </script>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.exedit-3.5.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/zTree.ext.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/platform/menu.js"></script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" title="菜单树形列表" style="width:200px;">
    <ul id="menuTree" class="ztree"></ul>
</div>
<div data-options="region:'center',border:false" style="overflow: auto;position: relative;">
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add()">添加</a> <a
            href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton"
           iconCls="icon-remove" plain="true" onclick="deleteMenu()">删除</a>
        <div>
            <form action="#" id="menuFilter">
                <input type="hidden" id="pid" name="filter[m1.parentId]">
                <label class="panel-title">菜单搜索：</label>
                菜单名称：<input type="text" name="filter[m1.name]" id="name" like="true" value="" class="easyui-textbox">
                菜单地址：<input type="text" name="filter[m1.url]" id="url" like="true" value="" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a>
            </form>
        </div>
    </div>
    <table id="dg" title="菜单信息列表" class="easyui-datagrid" fit="true" url="<%=basePath%>menu/list" toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true" singleSelect="false">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="ICON_" width="50" data-options="field:'_icon',formatter:iconFormatter">图标</th>
            <th field="NAME" width="50">菜单名称</th>
            <th field="URL_" width="50">菜单地址</th>
            <th field="CODE" width="50">菜单代码</th>
            <th field="PNAME" width="50">上级菜单</th>
            <th field="ISBUTTON" width="50" data-options="field:'ISBUTTON',formatter:isButtonFormatter">是否按钮</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>
