<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <link rel="shortcut icon" href="resources/fav.ico"/>
    <link rel="bookmark" href="resources/fav.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@ include file="frame_common.jsp" %>
    <style type="text/css">
        .logo {
            background: url(resources/logo/logo.png) no-repeat;
            background-size: 100% 100%;
            width: 450px !important;
        }

        *::-webkit-input-placeholder {
            color: yellow;
        }

        input:-webkit-autofill {
            -webkit-box-shadow: 0 0 0px 1000px rgb(46, 195, 190) inset;
            -webkit-text-fill-color: yellow;
        }
    </style>
</head>
<body class="easyui-layout" style="min-width:1029px;" ondragstart="return false">
<div id="top" data-options="region:'north',border:false,collapsible:true" class="top">
    <div class="shousuo uparrow"></div>
    <div class="logo" style="width:290px;"></div>
    <div class="menu-block">
        <div class="top-menu" id="menu-top"></div>
    </div>
    <div class="clear"></div>
    <div class="top-info">
			<span class="fl">&nbsp;&nbsp;&nbsp;&nbsp;欢迎 [ <font
                    style='color:red;font-size:13px;font-weight:bolder;'>${userName }</font> ] 登陆系统&nbsp;&nbsp;
			</span> <span class="fr"> <span id="ctime" style="margin-right:20px;"></span>
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
<div id="leftTitle" data-options="region:'west',split:true,title:'恒石MES'" style="width:200px;">
    <div id="menu" class="easyui-accordion" data-options="fit:true,border:false,onSelect:doSelect"></div>
</div>
<div data-options="region:'center',border:false" style="overflow:hidden">
    <div id="tt" class="easyui-tabs" fit="true">
        <div title="首页" style="padding:5px;" iconCls="platform-home">
            <div class="main-left">
                <div class="panel-header">系统信息</div>
                <ul class="main-infor">
                    <li>项目名称：<font color="red" id="projectName"></font></li>
                    <li>系统用户数：<font color="red">${applicationScope.user_count }</font></li>
                    <li>服务器：<font color="red">Windows Server 2008</font></li>
                    <li>数据库：<font color="red">MYSQL 集群</font></li>
                    <li>WEB服务器：<font color="red">Tomcat 集群</font></li>
                    <li>反向代理：<font color="red">NGINX</font></li>
                </ul>
            </div>
            <div class="main-right">
                <div class="panel-header">系统公告</div>
                <ul class="main-infor" id="notice_info" style="width:535px;">
                </ul>
            </div>

            <div class="main-left">
                <div class="panel-header">我的审核</div>
                <table id="auditDg" singleSelect="true" emptyMsg="无数据" class="easyui-datagrid" showHeader="false"
                       pagination="false" fitColumns="true" fit="true" style="width:100%;"
                       data-options="onClickRow:auditRowClick">
                    <thead>
                    <tr>
                        <th field="AUDITTITLE" width="10">流程标题</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="main-right">
                <div class="panel-header">我的消息</div>
                <table id="dg" fit="true" width="100%" url="<%=site %>/msg/unread?rows=10"
                       class="easyui-datagrid" pagination="false"
                       rownumbers="true" fitColumns="true" singleSelect="true" showHeader="false">
                    <thead>
                    <tr>
                        <th field="CONTENT" width="30" formatter="contentFormatter">消息内容</th>
                    </tr>
                    </thead>
                </table>
                <script type="text/javascript">
                    function contentFormatter(v, r, i) {
                        return '<a title="' + v + '">' + v + '</a>';
                    }
                </script>
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
