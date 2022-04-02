<%-- 工具栏按钮，包括权限控制--%>
<%@page import="com.bluebirdme.mes.core.constant.Constant"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String[] ids = request.getParameterValues("ids");
	String[] icons = request.getParameterValues("icons");
	String[] names = request.getParameterValues("names");
	String[] funs = request.getParameterValues("funs");

	//是否是超级管理员
	boolean isAdministrator = (session.getAttribute(Constant.CURRENT_USER_NAME)==null?"":session.getAttribute(Constant.CURRENT_USER_NAME).toString()).equals("administrator") ? true : false;
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	List<String> btns = (List<String>) request.getAttribute(Constant.CURRENT_PAGE_BUTTONS);
	if(isAdministrator){
		for (int i = 0; i < ids.length; i++) {
			%>
				<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="<%=funs[i]%>" id="<%=ids[i]%>" iconCls="<%=icons[i]%>"><%=names[i]%></a>
			<%
		}
	}
	else{
		if (btns != null) {
			for (int i = 0; i < ids.length; i++) {
				if (isAdministrator) {
						%>
						<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="<%=funs[i]%>" id="<%=ids[i]%>" iconCls="<%=icons[i]%>"><%=names[i]%></a>
						<%
					} else {
						if (btns.contains(ids[i])) {
							%>
							<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="<%=funs[i]%>" id="<%=ids[i]%>" iconCls="<%=icons[i]%>"><%=names[i]%></a>
							<%
						}
				}
			}
		}
	}
	
%>

