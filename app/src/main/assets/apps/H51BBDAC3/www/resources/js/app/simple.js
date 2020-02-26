/**
 * 安卓Toast提示框
 * @param {Object} text
 */
function toast(text) {
	plus.nativeUI.toast(text,{duration:"short",verticalAlign:"bottom",icon:"../resources/images/logo_mini.png",style:"inline"});
}

/**
 * 改进Console.log()方法
 */
function log() {
	for(var i = 0; i < arguments.length; i++) {
		try {
			console.log(JSON.stringify(arguments[i]));
		} catch(e) {
			console.log(arguments[i]);
		}
	}
}

/**
 * 遍历对象中的KEY-VALUE
 * @param {Object} obj
 */
function forEach(obj) {
	for(var k in obj) {
		console.log(k + "\t" + obj[k]);
	}
}

/**
 * 判断对象是否为空
 * @param {Object} obj
 */
function isEmpty(obj) {
	if(obj == null || obj == "" || obj == undefined) return true;
	return false;
}

/**
 * 确认对话框
 * @param {Object} title 标题
 * @param {Object} text 提示内容
 * @param {Object} okCB 确认回调
 * @param {Object} noCB 取消回调
 */
function mconfirm(title, text, okCB, noCB) {
	plus.nativeUI.confirm(text, function(e) {
		if(e.index == 0) {
			if(typeof okCB === "function") {
				okCB();
			}
		} else {
			if(typeof noCB === "function") {
				noCB();
			}
		}
	}, title, ["确认", "取消"]);
}

/**
 * 显示等待Loading效果
 * @param {Object} text 提示文字
 * @param {Object} closeAble 是否返回键可关闭，默认不可以关闭
 */
function waiting(text, closeAble) {
	plus.nativeUI.showWaiting(text, {
		width: "50%",
		height: "100px",
		modal: true,
		back: (closeAble == undefined || closeAble == false) ? "none" : "close"
	});
}

/**
 * 关闭等待效果
 */
function closeWaiting() {
	plus.nativeUI.closeWaiting();
}

/**
 * 系统弹出框
 * @param {Object} title 标题
 * @param {Object} msg 提示内容
 * @param {Object} buttonText 按钮文字
 * @param {Object} alertCB 确认后回调方法
 */
function malert(title, msg, buttonText, alertCB) {
	title == undefined ? "" : title;
	msg == undefined ? "" : msg;
	buttonText == undefined ? "确认" : buttonText;
	plus.nativeUI.alert(msg, alertCB, title, buttonText);
}

/**
 * 设备发出蜂鸣声
 */
function beep() {
	setTimeout(function(){plus.device.beep();},0);
}

/**
 * 设备震动
 */
function vibrate() {
	setTimeout(function(){plus.device.vibrate();},0);
}

/**
 * 设备震动和蜂鸣声
 */
function beepAndVibrate() {
	vibrate();
	beep();
}

/**
 * 表单校验
 * @param {Object} formId 表单ID
 */
function validForm(formId) {

	var inputs = $("#" + formId + " input");

	var v;
	var required;
	var validFn;

	var valid = true;

	$.each(inputs, function(i, e) {
		v = $(this).val();
		required = $(this).attr("required");
		validFn = $(this).attr("validFn");

		if(required && required == "required") {
			if(isEmpty(v)) {
				valid = false;
				$(this).addClass("mui-input-invalid");
			} else {
				$(this).removeClass("mui-input-invalid");
			}
		}

		if(!isEmpty(validFn)) {

			var fnName = validFn.substring(0, validFn.indexOf("("));
			var fnParam = validFn.substring(validFn.indexOf("(") + 1, validFn.length - 1);
			var fn = window[fnName];
			var param = fnParam.split(",");
			param.unshift(v);
			if(typeof fn == "function") {
				if(!fn.apply(null, param)) {
					valid = false;
					if(!$(this).hasClass("mui-input-invalid")) {
						$(this).addClass("mui-input-invalid");
					}
					toast(ValidMsg[fnName]);
				}
			}
		}
	});

	return valid;
}

/**
 * 下载文件
 * @param {Object} url 文件地址
 * @param {Object} okCallback 成功回调，传回 下载对象
 * @param {Object} errorCallback 失败回调，传回 下载对象
 */
function download(url, okCallback, errorCallback) {
	var options = {
		method: "GET",
		timeout: 10,
		retry: 0
	};
	plus.downloader.createDownload(url, options, function(d, status) {
		if(status == 200) {
			if(typeof okCallback === "function") {
				okCallback(d);
			}
		} else {
			if(typeof okCallback === "function") {
				errorCallback();
			}
		}
	}).start();
	/*down.addEventListener("statechanged", function(task, status) {
		if(!down) {
			return;
		}
		switch(task.state) {
			case 1: // 开始
				break;
			case 2: // 已连接到服务器
				break;
			case 3: // 已接收到数据
				log(task.downloadedSize + "/" + task.totalSize);
				break;
			case 4: // 下载完成
				log(task.filename)
				plus.runtime.install(task.filename);
				break;
		}
	});*/
}
/**
 * 是否IP
 * @param {Object} ipStr
 */
function isIp(ipStr){
	return /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/.test(ipStr);  
}

/**
 * 提醒，加震动和声音
 * @param {Object} msg
 */
function tip(msg){
	toast(msg);
	beepAndVibrate();
}

/**
 * 通知
 * @param {Object} title 标题
 * @param {Object} content 内容
 */
function notify(title,content){
	/*plus.device.setVolume(1);
	plus.audio.createPlayer("notice.mp3").play();
	plus.push.createMessage(content, null, {title:title,sound:"none"});*/
}

function sucAlert(content){
	plus.device.setVolume(1);
	plus.audio.createPlayer("notice.mp3").play();
	malert("操作提示",content);
}

function sucTip(content){
	plus.device.setVolume(1);
	plus.audio.createPlayer("notice.mp3").play();
	toast(content);
}
