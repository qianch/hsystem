/**
 * APP主要功能封装，注意，所有涉及到H5 plus部分的代码，必须包含在App.ready方法中
 */
var App = App = {};
App = {
	/**
	 * APP ready方法，一般立即执行的方法，都要写到这里，类似JQuery
	 * @param {Object} fn
	 */
    ready: function (fn) {
        mui.init();
        mui.plusReady(function () {
            if (fn) {
                fn();
            }
			
			try
			{
				initControl();
			}
			catch(ex)
			{
				
			}
			 
        });
    },
    preload: function (url, data, styles) {
        return mui.preload({
            url: url,
            id: url,
            extras: data,
            styles: styles
        });
    },
    show: function (webview) {
        webview.show(Config.animation.type, Config.animation.speed);
    },
	/**
	 * 页面跳转
	 */
    go: function (url, param, createNew) {
        waiting("加载中", true);
        var view;
        view = plus.webview.create(url, url, null, param);
        view.addEventListener("loaded", function () { //注册新webview的载入完成事件
            closeWaiting();
            view.show(Config.animation.type, Config.animation.speed); //把新webview窗体显示出来，显示动画效果为速度200毫秒的右侧移入动画
        }, false);
        return view;
    },
	/**
	 * 取参数值,需要在App.ready中才可以取到
	 */
    param: function (key) {
        return plus.webview.currentWebview()[key];
    },
	/**
	 * AJAX GET方法
	 * @param {Object} url
	 * @param {Object} successCB
	 * @param {Object} errorCB
	 */
    ajaxGet: function (url, successCB, errorCB) {
        waiting("请稍后", true);
        mui.ajax(url, {
            beforeSend: function (request) {
                request.setRequestHeader("AppVersion", Config.project.version);
                request.setRequestHeader("UserID",LS.get(KEY.USER_ID));
            },
            type: "get",
            dataType: "json",
            timeout: 30000,
            success: function (data) {
                closeWaiting();
                if (successCB) {
                    successCB(data);
                }
            },
            error: function (xhr, type, errorThrown) {
                closeWaiting();
                try {
                    var ret = JSON.parse(xhr.responseText);
                    tip(ret.error);
                } catch (e) {
                    console.log(e);
                }

                if (errorCB) {
                    errorCB(xhr, type, errorThrown);
                }
            }
        });
    },
	/**
	 * 获取服务器地址
	 */
    getServerUrl: function () {
        if (isEmpty(LS.get(KEY.SERVER_IP))) {
            return Config.serverUrl;
        } else {
            return Config.serverUrl.replace(SERVER_IP, LS.get(KEY.SERVER_IP));
        }
    },
	/**
	 * AJAX GET方法
	 * @param {Object} url 请求地址
	 * @param {Object} data JSON格式
	 * @param {Object} successCB 成功回调
	 * @param {Object} errorCB 失败回调
	 */
    ajaxPost: function (url, data, successCB, errorCB) {
        waiting("请稍后", true);
        mui.ajax(url, {
            beforeSend: function (request) {
                request.setRequestHeader("AppVersion", Config.project.version);
                request.setRequestHeader("UserID",LS.get(KEY.USER_ID));
            },
            data: data,
            type: "post",
            timeout: 30000,
            dataType: "json",
            success: function (data) {
                closeWaiting();
                if(data && data.error){
                    tip(data.error);
                    return;
                }
                if (successCB) {
                    successCB(data);
                }
            },
            error: function (xhr, type, errorThrown) {
                closeWaiting();
                try {
                    var ret = JSON.parse(xhr.responseText);
                    tip(ret.error);
                } catch (e) {
                    console.log(e);
                }

                if (errorCB) {
                    errorCB(xhr, type, errorThrown);
                }
            }
        });
    },
	/**
	 * 获取当前登陆用户ID
	 */
    getLoginUserId: function () {
        return LS.get(KEY.USER_ID);
    },
	/**
	 * 获取当前登陆用户名
	 */
    getLoginUserName: function () {
        return LS.get(KEY.USER_NAME);
    },
	/**
	 * 获取用户部门
	 */
    getUserDepartment: function () {
        return LS.get(KEY.USER_DEPT);
    }
};