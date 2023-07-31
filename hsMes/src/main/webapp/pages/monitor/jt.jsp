<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>机台显示屏</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="resources/jquery/toast/jquery.toast.min.css">
    <script type="text/javascript" src="resources/jquery/jquery-1.8.0.js"></script>
    <script type="text/javascript" src="resources/jquery/toast/jquery.toast.min.js"></script>
    <script type="text/javascript" src="resources/jquery/jquery-1.8.0.js"></script>
    <script type="text/javascript" src="resources/js/template.js"></script>
    <script type="text/javascript" src="resources/js/clipboard.min.js"></script>
    <style type="text/css">
        ::-webkit-scrollbar {
            width: 0;
        }

        html, body {
            margin: 0;
            font-family: 微软雅黑;
            background: #c7c7c7;
            font-weight: bolder;
        }

        table {
            width: 100%;
            table-layout: fixed;
        }

        th, td {
            font-weight: bolder;
            text-align: center;
            word-wrap: break-word;
        }

        hr {
            padding: 0;
            margin: 0;
        }

        th {
            background: #c7c7c7;
            font-size: 35px;
            color: rgb(179, 39, 91);
        }

        td {
            font-size: 40px;
        }

        .jt {
            text-align: center;
            font-size: 120px;
            font-weight: bold;
            color: rgb(179, 39, 39);
            width: 200px;
            left: 50%;
            margin-left: -100px;
            position: absolute;
            top: -20px;
            font-weight: bold;
            font-family: sans-serif;
        }

        .producing {
            background: rgba(1, 45, 0, 0.95) !important;
            color: rgba(255, 214, 0, 0.95);
        }

        .prior {
            color: rgb(255, 133, 0) !important;
        }

        .odd {
            background: rgb(239, 239, 239);
        }

        .tb_header {
            top: 120px;
            position: absolute;
        }

        .logo {
            background: url("resources/logo/logo_jt.png");
            background-repeat: no-repeat;
            background-position: 50%;
            height: 150px;
            width: 300px;
        }

        button {
            background: none;
            font-size: 40px;
            border: none;
            font-weight: bold;
            color: #ea0057;
        }

        .mes {
            text-align: center;
            font-size: 40px;
            font-weight: bold;
            color: rgb(179, 39, 39);
            width: 300px;
            position: absolute;
            right: 3px;
            top: 47px;
            background: #c7c7c7;
            font-family: sans-serif;
        }

        .tip {
            width: 300px;
            text-align: center;
            height: 45px;
            line-height: 45px;
            background: green;
            color: white;
            z-index: 9999;
            position: absolute;
            top: 300px;
            left: 50%;
            margin-left: -100px;
            display: none;
        }
    </style>
</head>
<body>
<!-- 后加的退出/进入全屏 -->
<div onclick="fullScreen()"
     style="z-index:999;position: fixed;bottom:0px;right:0px;height:40px;line-height:40px;text-align:center;font-weight:bold;font-size:18px;font-family:'微软雅黑';width:200px;background:#80808094;">
    进入/退出 全屏
</div>
<div id="closeButton" onclick="closeWin()"
     style="position: fixed; top: 5px; right: 5px; background: red; color: white; font-weight: bold; width: 60px; text-align: center; height: 30px; line-height: 30px; border-radius: 5px;">
    关闭
</div>
<script type="application/javascript">
    function fullScreen() {
        /*判断是否全屏*/
        const isFullscreen = document.fullScreenElement //W3C
            ||
            document.msFullscreenElement //IE11
            ||
            document.mozFullScreenElement //火狐
            ||
            document.webkitFullscreenElement //谷歌
            ||
            false;
        if (!isFullscreen) {
            const el = document.documentElement;
            if (el.requestFullscreen) {
                el.requestFullscreen();
            } else if (el.mozRequestFullScreen) {
                el.mozRequestFullScreen();
            } else if (el.webkitRequestFullscreen) {
                el.webkitRequestFullscreen();
            } else if (el.msRequestFullscreen) {
                el.msRequestFullscreen();
            }
        } else {
            if (document.exitFullscreen) {
                document.exitFullscreen();
            } else if (document.msExitFullscreen) {
                document.msExitFullscreen();
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if (document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();
            }
        }
    }
</script>
<!-- 后加部分结束 -->
<div class="logo"></div>
<div class="jt">${code }</div>
<div class="mes">MES机台显示屏</div>
<div class="tip">复制成功</div>
<table class="tb_header" border="1" cellspacing="0" cellpadding="0" bordercolor="gray"
       style="BORDER-COLLAPSE: collapse;">
    <tr>
        <th style="width:30%;">客户产品
            <hr>
            产品型号
        </th>
        <th style="width:20%;">门幅
            <hr>
            米长
            <hr>
            定重
        </th>
        <th style="width:15%;">卷号
            <hr>
            层号
            <hr>
            图号
        </th>
        <th style="width:30%;">生产工艺
            <hr>
            包装工艺
        </th>
        <th style="width:20%;">订单号
            <hr>
            批次号
        </th>
        <th style="width:15%;">未完成
            <hr>
            已完成
            <hr>
            订单量
        </th>
    </tr>
</table>
<script id="orders" type="text/template">
    {{each data as value i}}
    <tr style="border-top:3px solid #6f6f6f;border-bottom:3px solid #6f6f6f;" data-row="true"
        class="{{colorful(i)}} {{producing(value.ISPRODUCING)}} {{prior(value.SORT)}}">
        <td>{{value.CONSUMERPRODUCTNAME}}
            <hr>
            {{value.PRODUCTMODEL}}
        </td>
        <td>{{nullcontent(value.PRODUCTWIDTH)}}毫米
            <hr>
            {{nullcontent(value.PRODUCTLENGTH)}}米
            <hr>
            {{nullcontent(value.PRODUCTWEIGHT)}}千克
        </td>
        <td>{{nullcontent(value.ROLLNO)}}
            <hr>
            {{nullcontent(value.LEVELNO)}}
            <hr>
            {{nullcontent(value.DRAWINGNO)}}
        </td>
        <td>
            <button data-clipboard-text="{{code(value.PROCESSBOMCODE)}} {{value.PROCESSBOMVERSION}}">
                {{code(value.PROCESSBOMCODE)}}({{value.PROCESSBOMVERSION}})
            </button>
            <hr>
            <button data-clipboard-text="{{code(value.BCBOMCODE)}} {{nullcontent2(value.BCBOMVERSION)}}">
                {{code(value.BCBOMCODE)}}({{nullcontent(value.BCBOMVERSION)}})
            </button>
        </td>
        <td>{{value.SALESORDERCODE}}
            <hr>
            {{value.BATCHCODE}}
        </td>
        <td>{{moreCount(value)}}
            <hr>
            {{value.RC}}
            <hr>
            {{value.REQUIREMENTCOUNT}}
        </td>
    </tr>
    {{/each}}
</script>
</body>
<script type="text/javascript">
    const url = "jt/view/" +;
    const pageNo = 0;
    const pageSize = 5;
    let data = [];

    function closeWin() {
        window.location.href = "about:blank";
        window.close();
    }

    template.helper('code', function (content) {
        if (content) {
            return content.substring(content.lastIndexOf("/") + 1);
        }
    });
    template.helper('prior', function (PRIOR) {
        if (PRIOR) {
            return "prior";
        }
    });
    template.helper('colorful', function (i) {
        if (i % 2 === 0) {
            return "odd";
        }
    });
    template.helper('producing', function (ISPRODUCING) {
        if (ISPRODUCING === 1) {
            return "producing";
        }
    });
    template.helper('nullcontent', function (content) {
        if (content) {
            return content;
        } else {
            return "-";
        }
    });
    template.helper('nullcontent2', function (content) {
        if (content) {
            return content;
        } else {
            return "";
        }
    });
    template.helper('moreCount', function (row) {
        const c = row.REQUIREMENTCOUNT - row.RC;
        return c < 0 ? 0 : c;
    });

    function page() {
        /* pageNo++;
        var pages=Math.ceil(data.length/pageSize);
        
        if(data.length==0)return "";
        
        if(pageNo>pages){
            pageNo=1;
        } */
        //console.log("第"+pageNo+"/"+pages+"页");
        return template('orders', {
            "data": data
        });
    }

    function loadPage() {
        $(".tb_header tr[data-row='true']").remove();
        $(".tb_header").append(page());
    }

    let timeout = null;

    +function getData() {
        $.ajax({
            url: url,
            type: "get",
            dataType: "json",
            success: function (ret) {
                data = ret;
                loadPage();

                //新增复制功能
                //2018-10-24
                var btns = document.querySelectorAll('button');
                var clipboard = new ClipboardJS(btns);

                clipboard.on('success', function (e) {
                    clearTimeout(timeout);
                    $(".tip").show();
                    timeout = setTimeout(function () {
                        $(".tip").hide();
                    }, 1000);
                });

                clipboard.on('error', function (e) {
                    console.log(e);
                });

            }
        });
    }();
</script>
</html>