var webroot = window.location.pathname.substring(window.location.pathname.indexOf("\/") + 1).substring(0, window.location.pathname.substring(window.location.pathname.indexOf("\/") + 1).indexOf("\/"));
var MES_BASE = window.location.protocol + "\/\/" + window.location.host + "\/" + webroot;
$(function() {
	tabClose();
	tabCloseEven();
	InitTopMenu();
	InitLeftMenu();
	$("#loginOut").click(function() {
		layer.confirm('确定退出系统?', {
			icon : 3,
			title : '警告',
			shift : 4,
			shade : 0.5,
			btn : [ '退出', '取消' ], // 按钮
		}, function(index) {// 第一个按钮
			console.log(path);
			top.location.href = path + "/login/logout";
			layer.close(index);
		});
	});

	// 修改密码
	$("#modifyPwd").click(function() {
		$("#modify_account_form :input").removeClass('validatebox-invalid');
		$('#modify_account_form input').val('');
		$("#modify_account").dialog("open");
	})

	// 确认保存或修改
	$("#account_edit").click(function() {

		$('#modify_account_form').form('submit', {
			onSubmit : function() {
				// 对数据校验，通过返回 false 来阻止提交
				return $(this).form('validate');
			},
			success : function(result) {
				$("#modify_account").dialog("close");
				$.messager.show({
					title : "提示",
					msg : "修改成功",
					timeout : 5000,
					showType : 'slide'
				});
			}
		});
	});

	$("#topslide").toggle(function() {
		$("#toplogobar").slideUp("fast");
		$("#toplogobar").hide("slow");
		$("#topslide").addClass("down");
		$("#mainall").layout('panel', 'north').panel('resize', {
			height : 30
		});
		$("#mainall").layout('resize');
	}, function() {
		$("#toplogobar").slideDown("fast");
		$("#toplogobar").show();
		$("#topslide").removeClass().addClass("toparrow up");
		$("#mainall").layout('panel', 'north').panel('resize', {
			height : 50
		});
		$("#mainall").layout('resize');

	});

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

	$("#selfsection").click(function() {
		$("#sectionpage").show();
		$("#sectionpage").window('open');
	});
	$("#homebut").click()

});

function Clearnav() {
	var pp = $("#wnav").accordion("panels");
	if (pp) {
		$.each(pp, function(i, n) {
			var t = pp[0].panel("options").title;
			$("#wnav").accordion("remove", t);
		});
	}
}

// 一级菜单
function InitTopMenu() {
	InitsubMenu(menu);
}

function InitsubMenu(data) {
	$.each(data, function(i, sm) {
		var menulist = "";
		var submenu = "";
		menulist += '<ul>';
		if (sm.children) {
			$.each(sm.children, function(k, o) {// 二级菜单
				menulist += '<li class="subtitle"><div><a ref="' + o.id + '" href="javascript:void(0)" rel="' + o.url + '" ><span class="icon ' + o.icon + '" >&nbsp;</span><span  class="nav">' + o.text + '</span</a></div>';
				if (o.children) {
					submenu += '<ul>'
					$.each(o.children, function(m, n) {// 三级菜单
						submenu += '<li><div><a ref="' + n.id + '" href="javascript:void(0)" rel="' + n.url + '" ><span class="icon ' + n.icon + '" >&nbsp;</span><span class="nav">' + n.text + '</span></a></div></li>'
					});
					submenu += '</ul>'

				}
				menulist += submenu;
				menulist += '</li>';
				submenu = "";
			});
		} else {
			menulist += '<li><div><a ref="' + sm.id + '" href="javascript:void(0)" rel="' + sm.url + '" ><span class="icon' + sm.icon + '" >&nbsp;</span><span class="nav">' + sm.text + '</span></a></div></li> ';
		}
		menulist += '</ul>';

		$("#wnav").accordion('add', {
			title : sm.text,
			content : menulist,
			iconCls : 'icon ' + sm.icon
		});
	});

	var pp = $("#wnav").accordion('panels');
	var t = pp[0].panel('options').title;
	$("#wnav").accordion('select', t);
}

// 初始化左侧
function InitLeftMenu() {
	hoverMenuItem();

	$("#wnav li a").live('click', function() {
		var tabTitle = $(this).children('.nav').text();
		var url = $(this).parent().find('a').attr("rel");
		var menuid = $(this).parent().attr("ref");
		var listicon = $(this).children('.icon').attr("class")
		addTab(tabTitle, url, listicon, menuid);
		$("#wnav li div").removeClass("selected");
		$(this).parent().addClass("selected");
	});

}

/**
 * 菜单项鼠标Hover
 */
function hoverMenuItem() {
	$(".easyui-accordion").find('a').hover(function() {
		$(this).parent().addClass("hover");
	}, function() {
		$(this).parent().removeClass("hover");
	});
}

function addTab(subtitle, url, icon, menuid) {
	$("#rightmask").show();
	$("#progressBar_right").show();
	$.ajax({
		type : "get", // 以post方式与后台沟通
		url : url,// 与此页面沟通
		timeout : 8 * 1000,
		dataType : 'text',
		beforeSend : function() {
			$("#rightmask").show();
			$("#progressBar_right").show();
		},
		success : function(html) {
			$("#rightmask").hide()
			$("#progressBar_right").hide();
			if (html) {
				if (!$("#tabs").tabs("exists", subtitle)) {
					$("#tabs").tabs('add', {
						title : subtitle,
						content : createFrame(url),
						closable : true,
						iconCls : icon
					});
				} else {
					$('#tabs').tabs('select', subtitle);
					// $('#mm-tabupdate').click();
				}
			} else {
			}
			tabClose();
			$("#tabs").tabs('select', subtitle);

		}
	});
}

function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose() {
	/* 双击关闭TAB选项卡 */
	$(".tabs-inner").dblclick(function() {
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close', subtitle);
	});
	/* 为选项卡绑定右键 */
	$(".tabs-inner").bind('contextmenu', function(e) {
		$("#mm").menu("show", {
			left : e.pageX,
			top : e.pageY
		});

		var subtitle = $(this).children(".tabs-closable").text();

		$("#mm").data("currtab", subtitle);
		$("#tabs").tabs("select", subtitle);
		return false;
	});
}
// 绑定右键菜单事件
function tabCloseEven() {
	// 刷新
	$('#mm-tabupdate').click(function() {
		var currTab = $('#tabs').tabs('getSelected');
		var title = currTab.panel('options').tab.text();
		if ("欢迎页面" != title) {
			var url = $(currTab.panel('options').content).attr('src');
			$('#tabs').tabs('update', {
				tab : currTab,
				options : {
					content : createFrame(url)
				}
			});
		}
	});
	// 关闭当前
	$('#mm-tabclose').click(function() {
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close', currtab_title);
	});
	// 全部关闭
	$('#mm-tabcloseall').click(function() {
		$('.tabs-inner span').each(function(i, n) {
			var t = $(n).text();
			$('#tabs').tabs('close', t);
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
			// msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});
	// 关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function() {
		var prevall = $('.tabs-selected').prevAll();
		if (prevall.length == 0) {
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});

	// 退出
	$("#mm-exit").click(function() {
		$('#mm').menu('hide');
	});
}

// 弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}
// 框架换肤
function changeTheme(themeName) {
	var $easyuiTheme = $('#easyuiTheme');
	var url = $easyuiTheme.attr('href');
	var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/easyui.css';
	$easyuiTheme.attr('href', href);
	var hrefindex = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/index.css';
	$('#easyuiThemeindex').attr('href', hrefindex)
	// alert(href);
	var $iframe = $('iframe');
	if ($iframe.length > 0) {
		for (var i = 0; i < $iframe.length; i++) {
			var ifr = $iframe[i];
			$(ifr).contents().find('#easyuiTheme').attr('href', '../' + href);
		}
	}

	$.cookie('easyuiThemeName', themeName, {
		expires : 7
	});

};

$.extend($.fn.validatebox.defaults.rules, {
	safepass : {
		validator : function(value, param) {
			return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(value));
		},
		message : '密码由字母和数字组成，至少6位'
	},
	passwordOk : {
		validator : function(value, param) {

			return value == $('#loginPwd').val();
		},
		message : '两次输入的密码不一致！'
	},
	passwordOldOk : {
		validator : function(value, param) {
			var flag = false;
			var loginPwdOld = $('#loginPwdOld').val();
			$.ajax({
				url : "pf/account/accountPwdOld_isOk.action?form.oldPwd=" + loginPwdOld,
				type : "post",
				async : false,
				success : function(item) {
					if (item) {
						if ('true' === item) {
							flag = true;
						}
					} else {
						flag = false;
					}
				}
			});
			return flag;
		},
		message : '旧密码不正确！'
	}
});