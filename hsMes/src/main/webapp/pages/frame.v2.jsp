<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="resources/fav.ico"/>
<link rel="bookmark" href="resources/fav.ico"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="frame_common.jsp" %>
<style type="text/css">
	.logo{
		background: url(${path}/resources/logo/logo.png) no-repeat;
    	background-size: 100% 100%;
	}
</style>
</head>
<body class="easyui-layout" style="min-width:1029px;">
	<div data-options="region:'north',border:false" class="top">
		<div class="shousuo uparrow"></div>
		<div class="logo" style="width:290px;"></div>
		
		<div class="clear"></div>
		<div class="top-info">
			<span class="fl">&nbsp;&nbsp;&nbsp;&nbsp;欢迎 [ <font style='color:red;font-size:13px;font-weight:bolder;'>${userName }</font> ] 登陆系统&nbsp;&nbsp;</span>
			<span class="fr">
				<span id="ctime" style="margin-right:20px;"></span>
				<select onchange="doChange()" id="style">
					<option value="v1">V1风格</option>
					<option value="v2" selected="selected">V2风格</option>
				</select>
				<span class="pwd-icon"><a href="javascript:void(0)" id="modifyPassword">修改密码</a></span>
				<span class="exit-icon"><a href="javascript:void(0)" id="loginOut">退出系统</a></span>
			</span>
		</div>
	</div>
	<div id="leftTitle" data-options="region:'west',split:true,title:'功能菜单'" style="width:200px;">
		<ul id="mt"></ul>
	</div>
	<div data-options="region:'center',border:false" style="overflow:hidden">
		<div id="tt" class="easyui-tabs" fit="true">
			<div title="首页" style="padding:5px;" iconCls="platform-home">
				<div class="main-left">
					<div class="panel-header">系统信息</div>
					<ul class="main-infor">
						<li>项目名称：<font color="red" id="projectName"></font></li>
						<li>系统用户数：<font color="red">${applicationScope.user_count }</font></li>
						<li>在线人数：<font color="red" >${applicationScope.online_count }</font></li>
						<li>系统版本：<font color="red">${applicationScope.sys_version }</font></li>
					</ul>
				</div>
				<div class="main-right">
					<div class="panel-header">系统公告</div>
					<ul class="main-infor" id="notice_info"  style="width:535px;">
					</ul>
				</div>

				<div class="clear"></div>
					<div id="tab" class="easyui-tabs" style="height:200px;margin:5px">
						<div title="审批消息提醒" style="padding:5px;">
							<table id="auditDg" singleSelect="true" class="easyui-datagrid" pagination="false" fitColumns="true" fit="true" style="width:100%;" data-options="onClickRow:auditRowClick">
								<thead>
									<tr>
										<th field="AUDITTITLE" width="10">流程标题</th>
										<th field="USERNAME" width="10">提审人员</th>
										<th field="CREATETIME" width="10">创建时间</th>
										<th field="CURRENTAUDITPROCESSNODE" width="10" formatter="formatNode">当前节点</th>
									</tr>
								</thead>
							</table>
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
