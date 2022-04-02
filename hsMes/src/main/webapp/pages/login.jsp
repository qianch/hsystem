<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="base/meta.jsp"%>
<%@ include file="base/jstl.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="resources/fav.ico"/>
<link rel="bookmark" href="resources/fav.ico"/>
<title>恒石纤维基业MES系统</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/style.css">

<style type="text/css">

body {
	background-image: url(resources/images/bgg.jpg);
	background-repeat: no-repeat;
	background-size: 100% 100%;
}


.subbtn {
	border: 0;
	background:#d84f4b;
	height: 45px;
	border-radius: 5px;
	font-family: "微软雅黑";
	color: #fff;
	outline: none;
	cursor: pointer;
	width:180px;
	margin-top:10px;
	margin-bottom:15px;
}

.subbtn:active{
	background:#fb6560;
}
.login-box {
	position: fixed;
	top: 50%;
	width: 100%;
	height: 350px;
	margin-top: -175px;
	padding: 0 0 0 0;
	text-align: center;
}

.login-form {
	position:fixed;
	margin:0 auto;
	top:50%;
	height: 444px;
	margin-top:-275px;
	left:50%;
	width: 600px;
	margin-left:-450px;
	border-radius: 8px;
	background:rgba(255, 255, 255, 0.23);
	box-shadow:1px 2px 10px 5px rgba(255, 255, 255, 0.54);
}

*::-webkit-input-placeholder { 
color: yellow; 
} 
input:-webkit-autofill {
 -webkit-box-shadow:0 0 0px 1000px rgb(46, 195, 190) inset;
 -webkit-text-fill-color: yellow;
}

input {
	height: 30px;
	background: rgba(128, 128, 128, 0);
    border: none;
    font-size:18px;
    padding-left:5px;
    color:yellow;
    margin-left:5px;
    outline:none;
}

*::-webkit-input-placeholder { 
color: yellow; 
} 
input:focus
{ 
	border-bottom:1px solid;
}

ul {
	padding:0;
	margin:0;
	text-align: center;
}
li{
	text-align: center;
	 border-bottom:1px solid white;
	 height: 60px;
	 line-height: 50px;
}
.out{
	background: #2ec3be;
    display: inline-block;
    font-size: 18px;
    color: white;
    width:100%;
    vertical-align: bottom;
}
.login-form-div {
	position:fixed;
	margin:0 auto;
	height: 100%;
	width: 100%;
	background: url("./resources/images/bgg.jpg");
	background-size:100% 100%;
            filter: blur(8px);
}
.logo-text{
	font-weight: bold;
    height: 120px;
    font-size: 30px;
    margin-top: 80px;
    font-family: serif;
    color: rgb(209, 202, 201);
    text-shadow: -1px -1px 0 #fff, 1px 1px 0 #333, 1px 1px 0 #444;
}
</style>
<script type="text/javascript">
		if(self!=top){
			top.location.href="<%=basePath%>";
	}
		
		function doBgAnimate(event){
			var h=$(document).height();
			var w=$(document).width();
			var x=event.clientX;
			var y=event.clientY;
			//$(".login-form-div").css("padding-left",Random.int(1,10));
		}
</script>
</head>
<body onmousemove="doBgAnimate(event)">
	<div class="login-form-div"></div>
	<div class="login-box">
		<form class="login-form" action="<%=basePath%>login" method="post" autocomplete="off">
			<ul>
				<li style="border:none;">
					<img  src="resources/logo/logo.png" height="55px" style="margin-top:45px;">
				</li>
				<li class="logo-text">MES信息管理系统<br>用户登陆</li>
				<li class="out"><label >用户名<input autocomplete="off" autofocus="autofocus" type="text"  name="loginName" value="" placeholder="请输入用户名" /></label></li>
				<li class="out"><label >密&nbsp;&nbsp;&nbsp;&nbsp;码<input  autocomplete="off" type="password" name="password" value="" placeholder="请输入密码" /></label></li>
				<li class="out">
					<label>语&nbsp;&nbsp;&nbsp;&nbsp;言</label>
					<select name="locale" style="width: 220px;font-size: 18px;color: yellow;background: #2ec3be">
						<option value="zh-CN">中文</option>
						<option value="en-US">English</option>
						<option value="ar-EG"> اللغة العربية</option>
						<option value="tr-TR">Türkçe</option>
					</select>
				</li>
				<li class="out" style="border-radius:0px 0px 10px 10px">
					<input type="submit" class="subbtn"  value="登&nbsp;&nbsp;录" />
					<br>
					<font style="color:red;font-weight: bold;">${error }</font>
				</li>
			</ul>
		</form>
	</div>
</body>
</html>