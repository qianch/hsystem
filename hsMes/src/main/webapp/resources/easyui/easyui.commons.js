var EasyUI = EasyUI = EasyUI || {};
EasyUI.grid = {
	search : function(a, b) {
		$("#" + a).datagrid("reload", JQ.getFormAsJson(b))
	},
	search2 : function(a, b) {
		$("#" + a).datagrid("reload", filter)
	},
	getSelections : function(a) {
		return $("#" + a).datagrid("getSelections")
	},
	getOnlyOneSelected : function(a) {
		var b = EasyUI.grid.getSelections(a);
		if (b.length > 1) {
			Tip.warn(tipSelectOnlyOne);
			return null
		}
		if (b.length == 0) {
			Tip.warn(tipSelectAtLeastOne);
			return null
		}
		return b[0]
	},
	contains : function(b, d) {
		var c = $("#" + b).datagrid("getData");
		if (c.total == 0) {
			return false
		} else {
			for (var a = 0; a < c.rows.length; a++) {
				if (c.rows[a]["ID"] == d.ID) {
					return true
				}
			}
			return false
		}
	},
	contains2 : function(c, e, a) {
		var d = $("#" + c).datagrid("getData");
		if (d.total == 0) {
			return false
		} else {
			for (var b = 0; b < d.rows.length; b++) {
				if (d.rows[b][a] == e[a]) {
					return true
				}
			}
			return false
		}
	},
	size : function(a) {
		var b = $("#" + a).datagrid("getData");
		return b.total
	},
	getRowByIndex : function(b, c) {
		var a = $("#" + b).datagrid("getData");
		for (var d = 0; d < a.rows.length; d++) {
			if (d == c) {
				return a.rows[d]
			}
		}
	},
	getRowIndex : function(a, b) {
		return $("#" + a).datagrid("getRowIndex", b)
	},
	removeRow : function(a, b) {
		$("#" + a).datagrid("deleteRow", b)
	},
	appendRow : function(a, b) {
		if (!EasyUI.grid.contains(a, b)) {
			$("#" + a).datagrid("appendRow", b)
		}
	},
	empty : function(a) {
		var c = $("#" + a).datagrid("getRows");
		for (var b = c.length - 1; b >= 0; b--) {
			$("#" + a).datagrid("deleteRow", $("#" + a).datagrid("getRowIndex", c[b]))
		}
	},
	deleteSelected:function(gridId){
		var rows=$("#" + gridId).datagrid("getSelections");
		var index=-1;
		for(var i=rows.length-1;i>=0;i--){
			index=$("#"+gridId).datagrid("getRowIndex",rows[i]);
			$("#"+gridId).datagrid("deleteRow",index);
		}
	}
};
EasyUI.form = {
	valid : function(b) {
		var a = $("#" + b).form("validate");
		return a
	},
	submit : function(d, b, c, a) {
		if (!EasyUI.form.valid(d)) {
			return
		}
		Loading.show(tipSubmiting);
		$.ajax({
			url : b,
			type : "post",
			data : $("#" + d).serialize(),
			dataType : "json",
			success : function(e) {
				Loading.hide();
				if (!Utils.isNull(c) && c instanceof Function) {
					c(e)
				}
			},
			error : function(e) {
				Loading.hide();
				if (!Utils.isNull(a) && a instanceof Function) {
					a(e)
				}
			}
		})
	},
	clear : function(a) {
		$("#" + a).form("clear")
	}
};
EasyUI.window = {
	button : function(d, e, c) {
		var a = {};
		a.iconCls = d == undefined ? null : d;
		a.text = e == undefined ? "&nbsp;" : e;
		a.handler = c == undefined ? null : c;
		return a
	},
	open : function(h, d, a, c, e, g, b) {
		var f = new Date().getTime();
		$("body").append("<div id='" + f + "'></div>");
		$("#" + f).dialog({
			id : f,
			title : h,
			width : d,
			height : a,
			href : c,
			modal : true,
			maximizable : true,
			minimizable : false,
			resizable : true,
			buttons : e,
			onClose : function() {
				$("#" + f).dialog("destroy");
				$("#" + f).remove();
				if (typeof b === "function") {
					b()
				}
			},
			onLoad : function() {
				if (typeof g === "function") {
					g()
				}
			}
		});
		return f
	},
	close : function(a) {
		$("#" + a).dialog("destroy");
		$("#" + a).remove()
	}
};
EasyUI.combobox = function(d, b, c, a) {
};
EasyUI.datebox = {
	formatter : function(a) {
		return a.format("yyyy-MM-dd")
	}
};
EasyUI.datetimebox = {
	formatter : function(a) {
		return a.format("yyyy-MM-dd HH:mm:ss")
	}
};
EasyUI.tree = {
	getTreeData : function(b, d, a, c) {
		function g(k) {
			var j = [];
			for (var h = 0; h < b.length; h++) {
				if (b[h][c] == k.id) {
					j.push({
						id : b[h][d],
						text : b[h][a]
					})
				}
			}
			for (var h = 0; h < j.length; h++) {
				j[h].children = g(j[h])
			}
			return j
		}
		var f = [];
		for (var e = 0; e < b.length; e++) {
			if (b[e][c] == null) {
				f.push({
					id : b[e][d],
					text : b[e][a]
				})
			}
		}
		for (var e = 0; e < f.length; e++) {
			f[e].children = g(f[e])
		}
		return f
	},
	select : function(b, c) {
		var a = $("#" + b).tree("find", c);
		$("#" + b).tree("expandTo", a.target).tree("select", a.target)
	}
};
function auditStyler(a, b) {
	return ""
}
function auditStyler2(a, b) {
	return ""
}
function auditStateFormatter(a) {
	if (a == 1) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF8C00;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">审核中</span><span class="l-btn-icon icon-question">&nbsp;</span></span>'
	} else {
		if (a == 2) {
			return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:green;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">已通过</span><span class="l-btn-icon icon-ok3">&nbsp;</span></span>'
		} else {
			if (a == 0) {
				return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#5a080f;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">未提交</span><span class="l-btn-icon icon-commit">&nbsp;</span></span>'
			} else {
				if (a == -1) {
					return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF0000;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">不通过</span><span class="l-btn-icon icon-del">&nbsp;</span></span>'
				}
			}
		}
	}
}
function autoAuditStateFormatter(b, c, a) {
	if (b == 1) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF8C00;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">审核中</span><span class="l-btn-icon icon-question">&nbsp;</span></span>'
	} else {
		if (b == 2) {
			return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:green;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">已通过</span><span class="l-btn-icon icon-ok3">&nbsp;</span></span>'
		} else {
			if (b == 0) {
				return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#5a080f;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">未提交</span><span class="l-btn-icon icon-commit">&nbsp;</span></span>'
			} else {
				if (b == -1) {
					return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF0000;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">不通过</span><span class="l-btn-icon icon-del">&nbsp;</span></span>'
				}
			}
		}
	}
}
function bomAuditStateFormatter(a) {
	if (a == 1) {
		return '<span style="color:#FF8C00;font-weight:bold;">[审核中]</span>'
	} else {
		if (a == 2) {
			return '<span style="color:green;font-weight:bold;">[已通过]</span>'
		} else {
			if (a == 0) {
				return '<span style="color:#5a080f;font-weight:bold;">[未提交]</span>'
			} else {
				if (a == -1) {
					return '<span style="color:#FF0000;font-weight:bold;">[不通过]</span>'
				}
			}
		}
	}
}
function auditTreeStyler(a) {
	if (a.attributes.nodeType == "version") {
		return a.text + bomAuditStateFormatter(a.attributes.AUDITSTATE)
	} else {
		return a.text
	}
};