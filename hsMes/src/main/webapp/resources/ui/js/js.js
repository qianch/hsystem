var menus;
$(function() {
	initNotice();
	var a = 0
	// 收缩菜单
	$(".shousuo").click(function() {
		if (a == 0) {
			a = 1;
			$('.easyui-layout').layout('panel', 'north').panel('resize', {
				height : 8
			});
			$('.easyui-layout').layout('resize');
			$(this).css("bottom", "0px");
			$(this).removeClass("uparrow").addClass("downarrow");
		} else {
			a = 0;
			$('.easyui-layout').layout('panel', 'north').panel('resize', {
				height : 107
			});
			$('.easyui-layout').layout('resize');
			$(this).css("bottom", "24px");
			$(this).removeClass("downarrow").addClass("uparrow");
		}
	});

	// 加载菜单数据
	$.ajax({
		"type" : "get",
		"url" : "menu/mymenu",
		"dataType" : "json",
		success : function(data) {
			menus = data;
			initTop();
		}
	});

	$('#tt').tabs({
		onContextMenu : function(e, title, index) {
			e.preventDefault();
			if (index > 0) {
				$('#mm').menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data("tabTitle", title);
			}
		}
	});

	tabCloseEven();
	// <div class="top-menu" id="menu-top"><span class="home-icon"
	// id="index">系统首页</span><span index="0" id="625" class="top-icon
	// icon-setup">系统设置</span></div>
	// 点击首页按钮事件
	$("#menu-top").delegate("span", "click", function() {
		if ($(this).attr("id") == "index") {
			addTab("首页",null);
		} else {
			$("#leftTitle").panel({title:$(this).text()});
			initLeft(leftMenu($(this).attr("index")));
		}
	});

	$("#loginOut").click(function() {
		$.messager.confirm('警告', '确定退出系统?', function(r) {
			if (r) {
				top.location.href = path + "/login/logout";
			}
		});
	});
	$("#modifyPassword").click(function() {
		modifyPassword();
	});
});

// 系统公告模块
function initNotice() {
$(document).ready(function() {
		$.ajax({
			type : "get",
			url : "notice/findnotice",
			dataType : 'json',
			timeout : 8 * 1000,
			success : function(data) {
				InitNoticeInfo(data);
			},
			error : function(data) {
			}
		});
	});
	function InitNoticeInfo(data) {
		$.each(data, function(i, sm) {
			$("#notice_info").append("<li><a href='javascript:void(0)' id=notice" + sm.ID + "><span class='fl'>" + sm.TITLE + "</span><span class='fr'>" + sm.INPUTTIME + "</span></a></li>");
			$("#notice" + sm.ID).click(function() {
				var wid = Dialog.open("系统公告", 1000, 520, path + "notice/openview?id=" + sm.ID + "&userId=" + userId);
			});
		});

	}
}

// 绑定右键菜单事件
function tabCloseEven() {
	// 刷新
	$('#mm-tabupdate').click(function() {
		var currTab = $('#tt').tabs('getSelected');
		var title = currTab.panel('options').tab.text();
		if ("欢迎页面" != title) {
			var url = $(currTab.panel('options').content).attr('src');
			$('#tt').tabs('update', {
				tab : currTab,
				options : {
					content : '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>'
				}
			});
		}
	});
	// 关闭当前
	$('#mm-tabclose').click(function() {
		var currtab_title = $('#mm').data("tabTitle");
		$('#tt').tabs('close', currtab_title);
	});
	// 全部关闭
	$('#mm-tabcloseall').click(function() {
		$('.tabs-inner span').each(function(i, n) {
			if (i != 0) {
				$('#tt').tabs('close', $(n).text());
			}
		});
	});
	// 关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function() {
		$('#mm-tabcloseright').click();
		$('#mm-tabcloseleft').click();
	});

	// 关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function() {
		var nextall = $('.tabs-selected').nextAll();
		if (nextall.length == 0) {
			return false;
		}
		nextall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tt').tabs('close', t);
		});
		return;
	});
	// 关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function() {
		var prevall = $('.tabs-selected').prevAll();
		if (prevall.length == 0) {
			return;
		}
		for (var i = 0; i < prevall.length - 1; i++) {
			$('#tt').tabs('close', $('a:eq(0) span', $(prevall[i])).text());
		}
		return;
	});

	// 退出
	$("#mm-exit").click(function() {
		top.location.href = path + "/login/logout";
	});
}

// 获取顶部菜单数据
function topMenu() {
	return menus.children;
}
// 获取左侧菜单数据
function leftMenu(index) {
	return menus.children[index];
}

// 获取左侧菜单内部数据
function leftInnerMenu(index) {
	return menus.children[index].children;
}

// 情况左侧菜单
function EmptyPanel() {
	var panels = $("#menu").accordion("panels");
	for (var i = panels.length - 1; i >= 0; i--) {
		$("#menu").accordion("remove", i);
	}
}
// 初始化顶部菜单
function initTop() {

	$("#menu-top").append('<span class="home-icon" id="index">系统首页</span>');
	if (menus.children.length > 8) {
		$(".icon-arrow-left").show();
		$(".icon-arrow-right").show();
	}
	$.each(topMenu(), function(index, item) {
		$("#menu-top").append("<span index='" + index + "' id='" + item.id + "' class='top-icon "+item.iconCls+"'>" + item.text + "</span>");
	});
	// 绑定鼠标事件
	$("#menu-top span").mouseenter(function() {
		hover = true;
	});
	$("#menu-top span").mouseout(function() {
		hover = false;
	});

	initLeft(leftMenu(0));
}
// 初始化左侧菜单
function initLeft(data) {
	EmptyPanel();// 清空Panel
	$.each(data.children, function(index, e) {
		$('#menu').accordion('add', {
			title : e.text,
			iconCls:e.iconCls,
			content : '<ul style="margin: 1px;margin-top: 2px;" id="' + e.id + '"></ul>'
		});

	});
	// 选中第一个节点
	$('#menu').accordion('select', 0);
}
// 左侧菜单选择展开事件
function doSelect(title, index) {
	var data = getDataByTitle(title);
	// 初始化内部菜单树
	if (!$("#" + data.id).hasClass("tree")) {
		$("#" + data.id).tree({
			data : data.children,
			animate : true,
			onClick : function(node) {
				if (node.attributes.url != undefined) {
					addTab(node.text,Assert.isEmpty(node.iconCls)?"tree-file":node.iconCls, node.attributes.url);
				}
			}
		});
	}
}
// 添加选项卡
function addTab(title,iconCls, url) {
	if ($('#tt').tabs('exists', title)) {
		$('#tt').tabs('select', title);
	} else {
		var content = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
		$('#tt').tabs('add', {
			title : title,
			iconCls:iconCls,
			content : content,
			closable : true,
			tools:"#tabTools"
		});
	}
}

// 根据标题获取节点的子节点数据
function getDataByTitle(title) {
	var data = {};
	for (var i = 0; i < menus.children.length; i++) {
		for (var j = 0; j < menus.children[i].children.length; j++) {
			if (title == menus.children[i].children[j].text) {
				data.id = menus.children[i].children[j].id;
				data.children = menus.children[i].children[j].children;
				return data;
			}
		}
	}
}

// -----------------------
// 下面主要是顶部菜单的效果
var hover = false;

var scrollFunc = function(e) {
	var direct = 0;
	e = e || window.event;
	if (e.wheelDelta) { // 判断浏览器IE，谷歌滑轮事件
		if (e.wheelDelta > 0 && hover) { // 当滑轮向上滚动时
			moveToRight(e);
		}
		if (e.wheelDelta < 0 && hover) { // 当滑轮向下滚动时
			moveToLeft(e);
		}
	} else if (e.detail) { // Firefox滑轮事件
		if (e.detail > 0 && hover) { // 当滑轮向上滚动时
			moveToRight(e);
		}
		if (e.detail < 0 && hover) { // 当滑轮向下滚动时
			moveToLeft(e);
		}
	}
}
// 给页面绑定滑轮滚动事件
if (document.addEventListener) {
	document.addEventListener('DOMMouseScroll', scrollFunc, false);
}
// 滚动滑轮触发scrollFunc方法
window.onmousewheel = document.onmousewheel = scrollFunc;

// 按钮向左移动
function moveToLeft(mouse) {
	if (mouse != undefined) {
		$(".icon-arrow-left").css("background", "url(resources/ui/themes/addition/icons/arrow_left_active.png) top center no-repeat");
		setTimeout(function() {
			$(".icon-arrow-left").css("background", "url(resources/ui/themes/addition/icons/arrow_left.png) top center no-repeat");
		}, 100);
	}
	doAnimate(1);
}
// 按钮向右移动
function moveToRight(mouse) {
	if (mouse != undefined) {
		$(".icon-arrow-right").css("background", "url(resources/ui/themes/addition/icons/arrow_right_active.png) top center no-repeat");
		setTimeout(function() {
			$(".icon-arrow-right").css("background", "url(resources/ui/themes/addition/icons/arrow_right.png) top center no-repeat");
		}, 100);
	}
	doAnimate(-1);
}

// 动画执行，使用队列的方式
function doAnimate(leftOrRight) {
	$("#menu-top").queue(function() {
		// 队列执行动画效果，否则在div移动的时候，不等到上一次动画效果；执行完毕，会获取到不正确的div的偏移量
		$("#menu-top").animate({
			marginLeft : getOffset(leftOrRight) + "px"
		}, 50);
		$(this).dequeue();
	});
}

/**
 * 1表示向左移动，-1表示向右移动
 */
function getOffset(leftOrRight) {
	var offset = parseInt($("#menu-top").css("margin-left").replace("px", ""));
	var width = parseInt($("#menu-top span:eq(1)").css("width"));
	if (leftOrRight == -1 && offset >= 0)
		return 0;
	if (offset <= (0 - (($("#menu-top span").length) * width) + 550) && leftOrRight == 1) {
		return offset;
	}
	return offset - leftOrRight * width;
}

// 获取当前时间
setInterval(showTime, 1000);
function showTime() {
	var today = new Date();
	var weekday = new Array(7);
	weekday[1] = "星期一";
	weekday[2] = "星期二";
	weekday[3] = "星期三";
	weekday[4] = "星期四";
	weekday[5] = "星期五";
	weekday[6] = "星期六";
	weekday[0] = "星期日";
	var y = today.getFullYear() + "年";
	var month = today.getMonth() + 1 + "月";
	var td = today.getDate() + "日";
	var d = weekday[today.getDay()];
	var h = today.getHours();
	var m = today.getMinutes();
	var s = today.getSeconds();
	var systime = y + (month < 10 ? "0" + month : month) + (td < 10 ? "0" + td : td) + ' ' + (d < 10 ? "0" + d : d) + ' ' + (h < 10 ?

	"0" + h : h) + ':' + (m < 10 ? "0" + m : m) + ':' + (s < 10 ? "0" + s : s);
	$("#ctime").text(systime);
}

function modifyPassword() {
	var wid=Dialog.open("修改密码", 300, 174, path + "/user/modifyPassword", [ EasyUI.window.button("", "保存", function() {
		if(JQ.getValue("#p1")==""||JQ.getValue("#p2")==""||JQ.getValue("#p3")==""){
			Tip.error("必须填写新旧密码");
			return;
		}
		if(JQ.getValue("#p2")!=JQ.getValue("#p3")){
			Tip.error("新密码两次输入不一致，请纠正");
			return;
		}
		JQ.ajaxPost(path+"/user/modifyPassword", {p1:JQ.getValue("#p1"),p2:JQ.getValue("#p2")}, function(data){
			if(!Tip.hasError(data)){
				Tip.success("修改成功");
				Dialog.close(wid);
			}else{
				Tip.dealError(data);
			}
		}, function(data){
			Tip.error("修改失败");
		});
	}), EasyUI.window.button("", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}