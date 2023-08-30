<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String site = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script type="text/javascript" src="<%=site%>resources/jquery/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="<%=site%>resources/jquery/jquery.qrcode.min.js"></script>
    <script type="text/javascript" src="<%=site%>resources/jquery/download2.js"></script>
    <style>
        body {
            text-align: center;
            background: gray;
        }

        input {
            border: none;
            outline: none;
            width: 590px;
            height: 60px;
            font-size: 22px;
            font-weight: bold;
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid gray;
        }

        #qr_img {
            margin: 10px;
        }

        .area {
            background: white;
            width: 600px;
            margin: 0 auto;
            height: 400px;
        }
    </style>
    <script type="text/javascript">
        function makeQrCode() {
            $("#qr_img").empty();
            const t = $("#qr_text").val();
            if (t === "") return;
            $("#qr_img").qrcode({
                text: t,
                width: 200,
                height: 200,
                render: "img"
            });
        }

        function downloadImg() {
            const url = $("img").attr("src");
            download(url, $("#qr_text").val(), "img/png");
        }
    </script>
    <title></title>
</head>
<body>
<div class="area">
    <input type="text" name="qr_text" id="qr_text" value="" onkeyup="makeQrCode()"
           placeholder="输入条码号(仅限英文和数字)"/>
    <div id="qr_img"></div>
    <button onclick="downloadImg()">下载</button>
</div>
</body>
</html>