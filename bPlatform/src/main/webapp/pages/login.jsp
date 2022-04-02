<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="base/meta.jsp" %>
<%@ include file="base/jstl.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>企业信息化平台管理系统</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/css/style.css">
    <style type="text/css">
        body {
            background: url("<%=basePath %>resources/images/login.jpg") center -100px;
        }

        .login-box {
            width: 1003px;
            margin: 203px auto 0 auto;
            padding: 120px 0 0 0
        }

        .login-input {
            width: 330px;
            height: 200px;
            margin-left: 624px;
        }

        .login-input ul {

        }

        .inputstyle {
            width: 272px;
            height: 34px;
            border: 0;
            margin-left: 38px;
            margin-top: 12px;
            outline: none
        }

        .subbtn {
            border: 0;
            background: #7ec6de;
            height: 31px;
            border-radius: 5px;
            margin-top: 24px;
            margin-right: 10px;
            font-family: "微软雅黑";
            color: #fff;
            outline: none;
            cursor: pointer;
        }
    </style>
    <script type="text/javascript">
        if (self != top) {
            top.location.href = "<%=basePath%>";
        }
    </script>
</head>
<body>
<div class="login-box">
    <form action="<%=basePath%>login" method="post" autocomplete="off" style="min-width:400px;">
        <div class="login-input">
            <ul>
                <li><input autocomplete="off" type="text" class="inputstyle" name="loginName" value="administrator"/>
                </li>
                <li><input autocomplete="off" type="password" class="inputstyle" name="password" value="123456"
                           style="margin-top:39px"/></li>
                <li>
                    <div style="text-align: left;vertical-align: middle;height:50px;line-height:50px;margin-top:20px">
                        <label style="vertical-align:middle; font-size:14px;">语言</label>
                        <select name="locale">
                            <option value="zh-CN">中文</option>
                            <option value="en-US">English</option>
                            <option value="ar-EG"> اللغة العربية</option>
                            <option value="tr-TR">Türkçe</option>
                        </select>
                    </div>
                </li>
                <li><input type="submit" class="subbtn fl" style="width:315px;" value="登录"/>
                    <div style="padding: 20px; text-align: center; color: red;">${error }</div>
                </li>
            </ul>
        </div>
    </form>
</div>
</body>
</html>