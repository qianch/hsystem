<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String site = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>企业信息管理系统</title>

    <link rel="stylesheet" type="text/css" href="<%=site%>/resources/ui/themes/cupertino/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=site%>/resources/ui/css/style.css">
    <link rel="stylesheet" type="text/css" href="<%=site%>/resources/themes/platform.css">
    <link rel="stylesheet" type="text/css" href="<%=site%>/resources/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=site%>/resources/ui/themes/addition/addition-icon.css">

    <script type="text/javascript" src="<%=site%>/resources/ui/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=site%>/resources/ui/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=site%>/resources/ui/js/jquery.portal.js"></script>
    <script type="text/javascript" src="<%=site%>/resources/ui/js/jquery.cookie.js"></script>
    <script type="text/javascript" src="<%=site%>/resources/ui/js/js.js"></script>
    <script type="text/javascript" src="<%=site%>resources/uipack/ui.dialog.js"></script>
    <script type="text/javascript" src="<%=site%>resources/ext/easyui.ext.js"></script>
    <script type="text/javascript" src="<%=site%>resources/ext/jquery.ext.js"></script>
    <script type="text/javascript" src="<%=site%>resources/uipack/ui.tip.js"></script>
    <script type="text/javascript" src="<%=site%>resources/uipack/ui.loading.js"></script>
    <script type="text/javascript" src="<%=site%>resources/utiljs/Assert.js"></script>
    <script type="text/javascript" src="<%=site%>resources/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript">
        const path = "<%=site%>";
        const userId =${userId};
        const locale = "${locale}";
        //获取项目名称
        let projectName = "${pageContext.request.contextPath}";
        $(document).ready(function () {
            projectName = projectName.substring(1);
            $("#projectName").html(projectName);
            $("#locale").val(locale);
        });
        const loginPath = "<%=site%>login";
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

        function doChange() {
            $.cookie("ui", $("#style").val());
            if ($("#style").val() == "v1") {
                location.href = path;
            } else {
                location.href = path + "?ui=v2";
            }
        }

        function localeChange() {
            location.href = path + "language/localeChange?locale=" + $("#locale").val();
        }
    </script>
</head>
<body class="easyui-layout" style="min-width:1029px;">
<div data-options="region:'north',border:false" class="top">
    <div class="shousuo uparrow"></div>
    <div class="logo" style="width:290px;"></div>
    <div class="menu-block">
        <div class="top-menu" id="menu-top"></div>
    </div>
    <div class="clear"></div>
    <div class="top-info">
        <span class="fl">&nbsp;&nbsp;&nbsp;&nbsp;欢迎 [ <font
                style='color:red;font-size:13px;font-weight:bolder;'>${userName }</font> ] 登陆系统&nbsp;&nbsp;</span>
        <span class="fr">
				<span id="ctime" style="margin-right:20px;"></span>
				<select onchange="doChange()" id="style">
					<option value="v1">V1风格</option>
					<option value="v2">V2风格</option>
				</select>
				<select id="locale" onchange="localeChange()">
                    <option value="zh-CN">中文</option>
                    <option value="en-US">English</option>
                    <option value="ar-EG"> اللغة العربية</option>
                    <option value="tr-TR">Türkçe</option>
                </select>
				<span class="pwd-icon"><a href="javascript:void(0)" id="modifyPassword">修改密码</a></span>
				<span class="exit-icon"><a href="javascript:void(0)" id="loginOut">退出系统</a></span>
			</span>
    </div>
</div>
<div id="leftTitle" data-options="region:'west',split:true,title:''" style="width:200px;">
    <div id="menu" class="easyui-accordion" data-options="fit:true,border:false,onSelect:doSelect"></div>
</div>
<div data-options="region:'center',border:false" style="overflow:hidden">
    <div id="tt" class="easyui-tabs" fit="true">
        <div title="首页" style="padding:5px;">
            <div class="main-left">
                <div class="panel-header">系统信息</div>
                <ul class="main-infor">
                    <li>项目名称：<font color="red" id="projectName"></font></li>
                    <li>合作单位：<font color="red">科技</font></li>
                    <li>系统用户数：<font color="red">${applicationScope.user_count }</font></li>
                    <li>在线人数：<font color="red">${applicationScope.online_count }</font></li>
                    <li>系统版本：<font color="red">${applicationScope.sys_version }</font></li>
                    <li>当前版本号:<font color="red">${applicationScope.version }</font></li>
                </ul>
            </div>
            <div class="main-right">
                <div class="panel-header">系统公告</div>
                <ul class="main-infor" id="notice_info" style="width:535px;">
                </ul>
            </div>

            <div class="clear"></div>
            <div class="main-bottom">
                <div class="panel-header">系统消息提醒</div>
                <div id="tab" class="easyui-tabs" style="height:200px;margin:5px">
                    <div title="内部消息" style="padding:5px;width:100%;">
                        <table class="tablelist">
                            <thead>
                            <tr>
                                <th width="80" height="30" align="center" class="datagrid-header">序号</th>
                                <th width="80" align="center" class="datagrid-header">消息类型</th>
                                <th width="80" align="center" class="datagrid-header">提醒时间</th>
                                <th width="60" align="center" class="datagrid-header">消息内容</th>
                                <th width="60" align="center" class="datagrid-header">消息状态</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td align="center" colspan="6">暂无数据显示</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div title="审批消息提醒" style="padding:5px;">
                        <table class="tablelist">
                            <thead>
                            <tr>
                                <th width="80" align="center" class="datagrid-header">审批类型</th>
                                <th width="80" align="center" class="datagrid-header">审批事物</th>
                                <th width="60" align="center" class="datagrid-header">提请时间</th>
                                <th width="60" align="center" class="datagrid-header">审批状态</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td align="center" colspan="6">暂无数据显示</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div title="设备异常提醒" style="padding:5px;">
                        <table class="tablelist">
                            <thead>
                            <tr>
                                <th width="70" height="30" align="center" class="datagrid-header">提醒标题</th>
                                <th width="50" align="center" class="datagrid-header">异常设备</th>
                                <th width="50" align="center" class="datagrid-header">提醒时间</th>
                                <th width="50" align="center" class="datagrid-header">提醒详情</th>
                                <th width="50" align="center" class="datagrid-header">处理节点</th>
                                <th width="60" align="center" class="datagrid-header">计划处理时间</th>
                                <th width="60" align="center" class="datagrid-header">处理负责人</th>
                            </tr>
                            </thead>
                            <tr>
                                <td align="center" colspan="7">暂无数据显示</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- TAB右键点击菜单 -->
<div id="mm" class="easyui-menu" style="width:150px;">
    <div id="mm-tabupdate" iconCls="icon-reload">刷新</div>
    <div class="menu-sep"></div>
    <div id="mm-tabclose" iconCls="icon-no">关闭</div>
    <div id="mm-tabcloseall" iconCls="platform-del1">全部关闭</div>
    <div id="mm-tabcloseother" iconCls="platform-del">除此之外全部关闭</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright">当前页右侧全部关闭</div>
    <div id="mm-tabcloseleft">当前页左侧全部关闭</div>
    <div class="menu-sep"></div>
    <div id="mm-exit">退出</div>
</div>
</body>
</html>
