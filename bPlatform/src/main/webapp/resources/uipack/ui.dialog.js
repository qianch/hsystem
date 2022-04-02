var Dialog=Dialog=Dialog||{};

/**
 * 打开一个窗口,无需绑定到具体元素上，这里的元素都是临时生产的
 * @param title 窗口标题
 * @param width 宽度
 * @param height 高度
 * @param url 内容地址
 * @param buttons 按钮，数组通过EasyUI.window.button("图标","按钮文字",回调函数)返回一button，需要多个，调用多次即可
 * @returns 返回当前窗口的ID，调用close方法的时候要用
 */
Dialog.open=function(title,width,height,url,buttons,onLoadCallback,onCloseCallback){
	var wid=EasyUI.window.open(title, width, height, url, buttons,onLoadCallback,onCloseCallback);
	return wid;
}
/**
 * 向Dialog按钮一览左侧添加 连续添加的选择框
 * @param wid 窗口ID
 */
Dialog.more=function(wid){
	$("#"+wid).parent().find("div.dialog-button").prepend("<div style=\"float:left;font-size:12px;\"><input style=\"vertical-align:middle;\" type=\"checkbox\" id=\"addMore\"><label style=\"vertical-align:middle;\" for=\"addMore\">"+buttonAddMore+"</label></div>");
}
/**
 * 获取Dialog是否是连续添加
 * @param wid 窗口ID
 */
Dialog.isMore=function(wid){
	if($("#"+wid).parent().find(".dialog-button").find("input[type=checkbox]").attr("checked")=="checked"){
		return true;
	}
	return false;
}

/**
 * 最大化窗口
 */
Dialog.max=function(wid){
	$("#" + wid).dialog("maximize");
}

/**
 * 最小化窗口
 */
Dialog.min=function(wid){
	$("#" + wid).dialog("minimize");
}

/**
 * 关闭窗口
 */
Dialog.close=function(wid){
	EasyUI.window.close(wid);
}

/**
 * 确认对话框
 */
Dialog.confirm=function(fn,text){
	$.messager.confirm(tipInfo, Utils.isEmpty(text)?tipConfirm:text, function (data) {  
       if(data){
    	   fn();
       }
    });
}