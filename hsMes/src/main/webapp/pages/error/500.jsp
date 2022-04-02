<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <%@ include file="../base/meta.jsp" %>
    <title>500:服务器内部错误</title>
    <style type="text/css">
    	body{
	    	background: #EDF5FA;
	    	font-size: 12px;
    	}
    	hr{
    		border: none;
    		border-top: 1px solid #E0E0E0;
    	}
    	font{
    		color:red;
    		font-weight:bold;
    		font-size:18px;
    	}
    </style>
  </head>
  <body>
		<font >[500 Error] 服务器内部可能发生错误，请等待解决。</font>
		<hr>
		${error }
  </body>
</html>
