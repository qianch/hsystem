/**
 * 本JS方法只适用于 上海竹研电子科技有限公司 的型号为ZY-F07的PDA
 * 采用的是系统广播的方式，接受扫描到的条码
 */
(function (target) {
	/**
	 * ZY-F07 PDA扫码器的广播名称
	 */
    ACTION_SCANNER_BROADCAST = "action_barcode_broadcast";

	/**
	 * ZY-F07 PDA扫码器的广播内容
	 */
    KEY_SCANNER_STRING = "key_barcode_string";

    var mainActivity;
    var receiver;

    var IntentFilter;
    var filter;

    var Scanner = function (scanCallback) {
        if (typeof scanCallback != "function") {
            malert("代码异常", "Exception:new Scanner()必须传入一个回调方法，参数为扫描到的条码号");
        }

        var devices = ["SAMSUNG", "HUAWEI", "XIAOMI", "OPPO", "VIVO", "MEIZU", "ONEPLUS", "SUNMI"];
        if (devices.indexOf(plus.device.vendor.toUpperCase()) != -1) {
            $("body").append("<div id=\"scan_qrcode\" style=\"z-index:999;position: fixed;bottom:50px;left: 10px;height: 100px;width:100px;border-radius:10px;background: url(../resources/images/scan.png);background-size: 100% 100%;\"></div>");
            $("#scan_qrcode").draggable();
            mui("body").on("tap", "#scan_qrcode", function () {
                App.go("barcode_scan.html", {
                    scanCallback: scanCallback.name
                });
            });
        }
        this.mainActivity = plus.android.runtimeMainActivity();
        //广播接收器
        this.receiver = plus.android.implements('io.dcloud.android.content.BroadcastReceiver', {
            onReceive: function (context, intent) { //实现onReceiver回调函数
                //通过intent对象，引入android中的类的实例，可以通过"."调用其方法
                plus.android.importClass(intent);
                //获取条码
                var code = intent.getSerializableExtra(KEY_SCANNER_STRING);
                scanCallback(code);
                //扫码完成之后，取消监听，下次调用时候，再启动监听
                //mainActivity.unregisterReceiver(receiver);
            }
        });
        //获取IntentFilter对象
        this.IntentFilter = plus.android.importClass('android.content.IntentFilter');
        this.filter = new this.IntentFilter(ACTION_SCANNER_BROADCAST);
        return this;
    }

    Scanner.prototype = {
        scan: function () {
            if (this.mainActivity != null)
                //注册广播接收器
                this.mainActivity.registerReceiver(this.receiver, this.filter);
        },
        stop: function () {
            if (this.mainActivity != null)
                this.mainActivity.unregisterReceiver(this.receiver);
        }
    };
    target.Scanner = Scanner;
})(window);