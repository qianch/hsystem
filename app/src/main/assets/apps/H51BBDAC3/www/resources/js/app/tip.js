
var Tip = Tip = {};

var tipTimeout;
/**
 * 成功提示
 * @param {Object} text 提示文本
 * @param {Object} timeout 超时时间
 */
Tip.suc=function(text,timeout){
	clearTimeout(tipTimeout);
	$(".ui_tip").text(text);
	$(".ui_tip").removeClass("ui_tip_error");
	$(".ui_tip").removeClass("ui_tip_warn");
	$(".ui_tip").addClass("ui_tip_success");
	$(".ui_tip").slideDown("normal",function(){
		tipTimeout=setTimeout(function(){
			$(".ui_tip").slideUp("normal");
		},timeout==undefined?3000:timeout);
	});
}

/**
 * 警告提示
 * @param {Object} text 提示文本
 * @param {Object} timeout 超时时间
 */
Tip.warn=function(text,timeout){
	clearTimeout(tipTimeout);
	$(".ui_tip").text(text);
	$(".ui_tip").removeClass("ui_tip_error");
	$(".ui_tip").removeClass("ui_tip_success");
	$(".ui_tip").addClass("ui_tip_warn");
	$(".ui_tip").slideDown("normal",function(){
		tipTimeout=setTimeout(function(){
			$(".ui_tip").slideUp("normal");
		},timeout==undefined?3000:timeout);
	});
}


/**
 * 错误提示
 * @param {Object} text 提示文本
 * @param {Object} timeout 超时时间
 */
Tip.error=function(text,timeout){
	clearTimeout(tipTimeout);
	$(".ui_tip").text(text);
	$(".ui_tip").removeClass("ui_tip_success");
	$(".ui_tip").removeClass("ui_tip_warn");
	$(".ui_tip").addClass("ui_tip_error");
	$(".ui_tip").slideDown("normal",function(){
		tipTimeout=setTimeout(function(){
			$(".ui_tip").slideUp("normal");
		},timeout==undefined?3000:timeout);
	});
}

//在页面中添加UI TIP元素
!function(){
   $(document).ready(function(){
		$("body").append("<div class=\"ui_tip\"> </div>");
	});
}();
