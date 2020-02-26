var Loading=Loading={};

/**
 * 显示等待效果
 * @param {Object} text 提示文字
 */
Loading.show=function(text){
	$(".ui_loading").show();
	$(".ui_loading_content").text(text==undefined?"加载中":text);
}
/**
 * 隐藏等待效果
 */
Loading.hide=function(){
	$(".ui_loading").hide();
}

//在页面中添加UI Loading元素
!function(){
   $(document).ready(function(){
		$("body").append("<div class=\"ui_loading\"> <div class=\"ui_loading_content\"> </div> </div>");
	});
}();

