/**
 * 该JS主要目的
 * 1. 在每个页面增加 注销登录、退出应用 ,检查更新按钮
 * 2. 在每个页面的头部，显示当前登录的用户
 * 3. 软件长时间不操作，要退出，或者跳转到登录页面，待客户需求而定，先采用注销登录方式
 * 4. 检查更新，如果有更新，自动下载更新，下载完进入安装界面
 * 5. 如果header上有nouser的样式名称，那么就不显示用户名，如果有nomenu样式，那么不显示菜单图标
 */

/**
 * 页面标题加上用户名 以及 初始化菜单
 */
$(document).ready(function () {
    if (!isEmpty(LS.get(KEY.USER_NAME))) {
        if (!$("header").hasClass("nouser")) {
            $(".mui-title").append("<font style='font-size:10px;padding-left:2px;'>" + Config.project.version + "</font>");
        }
        $(".mui-title").append("<font color='red'>（" + LS.get(KEY.USER_NAME) + "）</font>");
    }
    if ($("header").hasClass("nomenu"))
        return;
    //<li class=\"app-menu-item\">系统设置</li>
    $("header").append("<div class='app-menu-mask'></div><a class=\"mui-icon mui-icon-bars mui-pull-right \"></a> <div class=\"app-menu\"> <ul class=\"app-menu-items\"><li class=\"app-menu-item\">回到主页</li> <li style=\"display:none;\" class=\"app-menu-item\" href=\"#popover\">打开菜单</li> <li class=\"app-menu-item\">注销登录</li> <li class=\"app-menu-item\">退出应用</li><li class=\"app-menu-item\">检查更新</li> </ul> </div>");
    $("header").append("<div class=\"app-menu2\"> <ul class=\"app-menu-items\"> <li class=\"app-menu-item\">生产</li> <li class=\"app-menu-item\">库存</li> <li class=\"app-menu-item\">打包/翻包</li><li class=\"app-menu-item\">信息查询</li> </ul> </div>")
    $("header").append("<div class=\"app-menu3\" id=\"subMenu1\"> <ul class=\"app-menu-items\"> <li class=\"app-menu-item\" href=\"bzIn.html\">编织投料</li><li class=\"app-menu-item\" href=\"bzOut.html\">编织产出</li><li class=\"app-menu-item\" href=\"cjIn.html\">裁剪投料</li><li class=\"app-menu-item\" href=\"cjOut.html\">裁剪产出</li></ul> </div>");
    $("header").append("<div class=\"app-menu3\" id=\"subMenu2\"> <ul class=\"app-menu-items\"> <li class=\"app-menu-item\" href=\"mIn.html\">巨石原料入库</li><li class=\"app-menu-item\" href=\"mForceOut.html\">巨石原料退回</li><li class=\"app-menu-item\" href=\"mIn2.html\">车间退料</li><li class=\"app-menu-item\" href=\"mOut.html\">原料出库</li><li class=\"app-menu-item\" href=\"mMove.html\">原料移库</li><li class=\"app-menu-item\" href=\"mCheck.html\">原料盘库</li><li class=\"app-menu-item\" href=\"pIn2.html\">成品入库</li><li class=\"app-menu-item\" href=\"pForceOut.html\">异常退回车间</li><li class=\"app-menu-item\" href=\"pOut.html\">成品出库</li><li class=\"app-menu-item\" href=\"pMove.html\">成品移库</li><li class=\"app-menu-item\" href=\"pCheck.html\">成品盘库</li><li class=\"app-menu-item\" href=\"stockInfos.html\">库位查询</li><li class=\"app-menu-item\" href=\"stockInfos2.html\">成品仓库</li></ul> </div>");
    $("header").append("<div class=\"app-menu3\" id=\"subMenu3\"> <ul class=\"app-menu-items\"> <li class=\"app-menu-item\" href=\"pBPackage.html\">盒打包</li><li class=\"app-menu-item\" href=\"pTPackage.html\">托打包</li><li class=\"app-menu-item\" href=\"pOpenPack.html\">拆　包</li><li class=\"app-menu-item\" href=\"pPackageCheck.html\">条码校验</li><li class=\"app-menu-item\" href=\"tTakeOut.html\">翻包领出</li><li class=\"app-menu-item\" href=\"tTurnBagPerTray.html\">整托翻包</li><li class=\"app-menu-item\" href=\"tTurnBagPackBox.html\">翻包盒打包</li><li class=\"app-menu-item\" href=\"tTurnBagPackTray.html\">翻包托打包</li></ul> </div>");
    $("header").append("<div class=\"app-menu3\" id=\"subMenu4\"> <ul class=\"app-menu-items\"> <li class=\"app-menu-item\" href=\"mInfo.html\">原料信息查询</li><li class=\"app-menu-item\" href=\"pInfo.html\">成品信息查询</li></ul> </div>");
    mui("header").on("tap", ".mui-icon-bars", function () {
        if ($(".app-menu").is(":hidden")) {
            $(".app-menu").show();
            $(".app-menu-mask").show();
        } else {
            $(".app-menu").hide();
            $(".app-menu-mask").hide();
            $(".app-menu2").hide();
            $("#subMenu1").hide();
            $("#subMenu2").hide();
            $("#subMenu3").hide();
            $("#subMenu4").hide();
        }
    });

    mui("body").on("tap", ".app-menu-mask", function () {
        $(".app-menu").hide();
        $(".app-menu2").hide();
        $(".app-menu3").hide();
        $(".app-menu-mask").hide();
    });

    mui(".app-menu-items").on("tap", ".app-menu-item", function () {
        if ($(this).text() == "回到主页") {
            App.go("main.html");
        }
        else if ($(this).text() == "打开菜单") {
            if ($(".app-menu2").is(":hidden")) {
                $(".app-menu2").show();
                $(".app-menu-mask").show();
            } else {
                $(".app-menu2").hide();
                $(".app-menu-mask").hide();
                $("#subMenu1").hide();
                $("#subMenu2").hide();
                $("#subMenu3").hide();
                $("#subMenu4").hide();
            }
        }
        else if ($(this).text() == "生产") {
            if ($("#subMenu1").is(":hidden")) {
                $("#subMenu2").hide();
                $("#subMenu3").hide();
                $("#subMenu4").hide();
                $("#subMenu1").show();
                $(".app-menu-mask").show();
            } else {
                $("#subMenu1").hide();
                $(".app-menu-mask").hide();
            }
        }
        else if ($(this).text() == "库存") {
            if ($("#subMenu2").is(":hidden")) {
                $("#subMenu1").hide();
                $("#subMenu3").hide();
                $("#subMenu4").hide();
                $("#subMenu2").show();
                $(".app-menu-mask").show();
            } else {
                $("#subMenu2").hide();
                $(".app-menu-mask").hide();
            }
        }
        else if ($(this).text() == "打包/翻包") {
            if ($("#subMenu3").is(":hidden")) {
                $("#subMenu1").hide();
                $("#subMenu2").hide();
                $("#subMenu4").hide();
                $("#subMenu3").show();
                $(".app-menu-mask").show();
            } else {
                $("#subMenu3").hide();
                $(".app-menu-mask").hide();
            }
        }
        else if ($(this).text() == "信息查询") {
            if ($("#subMenu4").is(":hidden")) {
                $("#subMenu1").hide();
                $("#subMenu2").hide();
                $("#subMenu3").hide();
                $("#subMenu4").show();
                $(".app-menu-mask").show();
            } else {
                $("#subMenu4").hide();
                $(".app-menu-mask").hide();
            }
        }
        else if ($(this).text() == "注销登录") {
            $(".app-menu").hide();
            logout();
        } else if ($(this).text() == "退出应用") {
            $(".app-menu").hide();
            exitApp();
        } else if ($(this).text() == "系统设置") {
            $(".app-menu").hide();
            App.go("settings.html");
        } else if ($(this).text() == "检查更新") {
            $(".app-menu").hide();
            checkForUpdate();
        }
        else {
            App.go($(this).attr("href"));
        }
    });

});
mui.plusReady(function () {
    mui.init({
        gestureConfig: {
            tap: true, //默认为true
            doubletap: true, //默认为false
            longtap: true, //默认为false
            swipe: true, //默认为true
            drag: true, //默认为true
            hold: true, //默认为false，不监听
            release: true //默认为false，不监听
        }
    });
    //网络检测
    if (plus.networkinfo.getCurrentType() == plus.networkinfo.CONNECTION_NONE) {
        toast("当前未连接网络");
        setTimeout(function () {
            var main = plus.android.runtimeMainActivity();
            var Intent = plus.android.importClass("android.content.Intent");
            var mIntent = new Intent('android.settings.WIFI_SETTINGS');
            main.startActivity(mIntent);
        }, 1000);
    }
    //将音量设置最大
    plus.device.setVolume(1);
    /*******************操作超时监控开始************************/
	/*var currentWebview = plus.webview.currentWebview();
	var seconds = 0;

	setInterval(function() {
		try {
			//如果当前的webview为最顶层的webview，那么计数累加，否则清零
			//这里有可能会有异常，当返回的时候，执行这个操作，获取的topwebview可能为空，所有用try catch来捕获
			if(currentWebview.id == plus.webview.getTopWebview().id) {
				seconds++;
				//设置一定的误差，达到事件的过期时间了，进行跳转的计数器正好还没有自行就清零了
				if(seconds > Config.EXPIRE_TIME + 5) {
					seconds = 0;
				}
			} else {
				seconds = 0;
			}
		} catch(e) {
			seconds = 0;
		}

	}, 1000);

	setInterval(function() {
		if(seconds == Config.EXPIRE_TIME) {
			logout();
		}
	}, 1000);

	mui.init({
		gestureConfig: {
			tap: true, //默认为true
			doubletap: true, //默认为false
			longtap: true, //默认为false
			swipe: true, //默认为true
			drag: true, //默认为true
			hold: true, //默认为false，不监听
			release: true //默认为false，不监听
		}
	});

	//监听所有事件
	document.addEventListener("tap", function() {
		seconds = 0;
	});
	document.addEventListener("doubletap", function() {
		seconds = 0;
	});
	document.addEventListener("longtap", function() {
		seconds = 0;
	});

	document.addEventListener("hold", function() {
		seconds = 0;
	});
	document.addEventListener("release", function() {
		seconds = 0;
	});
	document.addEventListener("swipeleft", function() {
		seconds = 0;
	});
	document.addEventListener("swiperight", function() {
		seconds = 0;
	});
	document.addEventListener("swipeup", function() {
		seconds = 0;
	});
	document.addEventListener("swipedown", function() {
		seconds = 0;
	});
	document.addEventListener("dragstart", function() {
		seconds = 0;
	});
	document.addEventListener("drag", function() {
		seconds = 0;
	});
	document.addEventListener("dragend", function() {
		seconds = 0;
	});

	$("body").keydown(function() {
		seconds = 0;
	});*/
    /*******************操作超时监控结束************************/
});

/**
 * APP初始化
 */
function appInit() {
    // 添加监听从系统消息中心点击某条消息启动应用事件
	/*plus.push.addEventListener("click", function(msg) {
		// 分析msg.payload处理业务逻辑 
		malert(msg.title, msg.content, "我已知晓", undefined);
	}, false);*/

    //打开软件后，清空用户信息，防止APP被非正常终止掉，清空用户信息
    LS.remove(KEY.USER_ID);
    LS.remove(KEY.USER_NAME);

    //检查是否屏幕常亮
    if (!isEmpty(LS.get(KEY.WAKE_UP)) && LS.get(KEY.WAKE_UP)) {
        plus.device.setWakelock(true);
    } else {
        plus.device.setWakelock(false);
    }
    //如果版本不一致，那么清除IP设定，否则新版可能设置了新的IP，那么这里就会出问题
    if (Config.project.version != LS.get(KEY.VERSION)) {
        LS.remove(KEY.SERVER_IP);
        LS.set(KEY.VERSION, Config.project.version)
    }
    //检查更新
    checkForUpdate();
}

/**
 * 登出，清空用户信息，调用登录页面，关闭已经预加载的页面
 */
function logout() {
    waiting("正在注销");
    LS.remove(KEY.USER_ID);
    LS.remove(KEY.USER_NAME);
    closeAllWebview();
    App.show(plus.webview.getLaunchWebview());
    closeWaiting();
    toast("已注销登陆");
}
/**
 * 退出APP
 */
function exitApp() {
    LS.remove("uid");
    LS.remove("uname");
    plus.runtime.quit();
}

/**
 * 关闭出应用入口以外的所有已经加载的webview
 */
function closeAllWebview() {
    //关闭已预加载的webview
    var views = plus.webview.all();
    for (var i = 0; i < views.length; i++) {
        try {
            if (views[i].id == plus.webview.getLaunchWebview().id) continue;
            views[i].close();
        } catch (e) {
            tip("系统错误，请退出应用重新打开");
        }
    }
}

/**
 * 检查更新，如果有新版本，则下载，并直接进入安装界面
 */
function checkForUpdate() {
    App.ajaxGet(App.getServerUrl() + "update?version=" + Config.project.version,
        function (data) {
            closeWaiting();
            if (data.hasNew) {
                waiting("检测到新版本，正在下载!");
                setTimeout(function () {
                    download(
                        data.url,
                        function (d) {
                            closeWaiting();
                            toast("下载成功，请安装更新");
                            plus.runtime.install(d.filename);
                        },
                        function (d) {
                            closeWaiting();
                            toast("下载失败");
                        }
                    );
                }, 1000);
            } else {
                toast("已是最新版本了！")
            }
        },
        function (xhr, error, errorThrown) {
            toast("服务器错误，请联系系统管理员！")
            closeWaiting();
        }
    );
}