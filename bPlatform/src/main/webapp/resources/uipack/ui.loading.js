var Loading = Loading = Loading || {};

Loading.show = function(text) {
	$("<div id='_uiloading' class=\"window-mask\" style='z-index:9998'></div>").css({
		display : "block",
		width : "100%",
		height : $(window).height()
	}).appendTo("body");
	
	$("<div id='_uiloading_text' class=\"datagrid-mask-msg\" style='z-index:9999;height:10px;line-height:10px;font-size:10px;'></div>").html(text==undefined?"请稍等(Loading)...":text).appendTo("body").css({
		display : "block",
		left : ($(document.body).outerWidth(true) - 190) / 2,
		top : ($(window).height() - 45) / 2
	});
}

Loading.hide = function() {
	$("#_uiloading").remove();
	$("#_uiloading_text").remove();
}