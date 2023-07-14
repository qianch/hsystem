<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<head>
    <title>浏览文件</title>
</head>
<div style="padding:10px;">
    <h1 style="text-align:center;font-size:30px;">${notice.title}</h1>
    <p style="font-size:13px;text-align:center">发布者：${user.userName}&nbsp&nbsp&nbsp时间：${notice.inputTime}</p>
    ${notice.content}
</div>