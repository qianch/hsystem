var EasyUI = EasyUI = EasyUI || {};

//表格
EasyUI.grid = {
	/**
	 * 搜索
	 * @param gridId 表格ID
	 * @param searchFormId 搜索表单的ID
	 */
	search : function(gridId, searchFormId) {
		$('#' + gridId).datagrid("reload", JQ.getFormAsJson(searchFormId));
	},
	/**
	 * 搜索2 自定义搜索
	 * @param gridId 表格ID
	 * @param jsonFilter JSON对象
	 */
	search2 : function(gridId, jsonFilter) {
		$('#' + gridId).datagrid("reload", filter);
	},
	//获取选中行
	getSelections : function(gridId) {
		return $("#" + gridId).datagrid("getSelections");
	},
	//获取唯一的选中的行
	getOnlyOneSelected : function(gridId) {
		var rows = EasyUI.grid.getSelections(gridId);
		if (rows.length > 1) {
			Tip.warn(tipSelectOnlyOne);
			return null;
		}
		if (rows.length == 0) {
			Tip.warn(tipSelectAtLeastOne);
			return null;
		}
		return rows[0];
	},
	/**
	 * 检查表格是否包含了新增的行数据
	 * @param grid
	 * @param rowData
	 * @returns {Boolean}
	 */
	contains:function(grid,rowData){
		var data=$("#"+grid).datagrid("getData");
		if(data.total==0){
			return false;
		}else{
			for(var i=0;i<data.rows.length;i++){
				if(data.rows[i]["ID"]==rowData["ID"]){
					return true
				}
			}
			return false;
		}
	},
	contains2:function(grid,rowData,idField){
		var data=$("#"+grid).datagrid("getData");
		if(data.total==0){
			return false;
		}else{
			for(var i=0;i<data.rows.length;i++){
				if(data.rows[i][idField]==rowData[idField]){
					return true
				}
			}
			return false;
		}
	},
	/**
	 * 获取表格的数据数
	 * @param gridId
	 * @returns
	 */
	size:function(gridId){
		var data=$("#"+gridId).datagrid("getData");
		return data.total;
	},
	/**
	 * 通过Index获取行
	 * @param gridId
	 * @param index
	 * @returns
	 */
	getRowByIndex:function(gridId,index){
		var datas=$("#"+gridId).datagrid("getData");
		for(var i=0;i<datas.rows.length;i++){
			if(i==index){
				return datas.rows[i];
			}
		}
	},
	/**
	 * 获取行索引号
	 * @param gridId
	 * @param rowData
	 * @returns
	 */
	getRowIndex:function(gridId,rowData){
		return $("#"+gridId).datagrid("getRowIndex",rowData);
	},
	/**
	 * 删除行
	 * @param gridId
	 * @param index
	 */
	removeRow:function(gridId,index){
		$("#"+gridId).datagrid("deleteRow",index);
	},
	/**
	 * 新增行
	 * @param gridId
	 * @param row
	 */
	appendRow:function(gridId,row){
		if(!EasyUI.grid.contains(gridId, row))
			$("#"+gridId).datagrid("appendRow",row);
	},
	/**
	 * 清空DataGrid
	 * @param gridId
	 */
	empty:function(gridId){
		var rows=$("#"+gridId).datagrid("getRows");
		for(var i=rows.length-1;i>=0;i--){
			$("#"+gridId).datagrid("deleteRow",$("#"+gridId).datagrid("getRowIndex",rows[i]));
		}
	}
};
/**
 * EasyUI表单操作
 */
EasyUI.form={
		/**
		 * 表单验证
		 */
		valid:function(id){
			var isValid = $("#"+id).form('validate');
			return isValid;
		},
		/**
		 * 表单提交
		 * @param id 表单ID
		 * @param url 表单提交地址
		 * @param success 成功回调函数
		 * @param error 失败回调函数
		 */
		submit:function(id,url,success,error){
			if(!EasyUI.form.valid(id))return;
			Loading.show(tipSubmiting);
			$.ajax({
				url:url,
				type:"post",
				data:$("#"+id).serialize(),
				dataType:"json",
				success:function(data){
					Loading.hide();
					if(!Utils.isNull(success)&&success instanceof Function){
						success(data);
					}
				},error:function(data){
					Loading.hide();
					if(!Utils.isNull(error)&&error instanceof Function){
						error(data);
					}
				}
			});
		},
		clear:function(id){
			$("#"+id).form("clear");
		}
};

EasyUI.window={
		//获取按钮
		button:function(icon,text,handler){
			var b={};
			b["iconCls"]=icon==undefined?null:icon;
			b["text"]=text==undefined?"&nbsp;":text;
			b["handler"]=handler==undefined?null:handler;
			return b;
		},
		/**
		 * 打开一个窗口,无需绑定到具体元素上，这里的元素都是临时生产的
		 * @param title 窗口标题
		 * @param width 宽度
		 * @param height 高度
		 * @param url 内容地址
		 * @param buttons 按钮，数组通过EasyUI.window.button("图标","按钮文字","回调函数");
		 * @param onLoadCallback 加载时回调
		 * @param onCloseCallback 关闭时执行的回调方法
		 * @returns 返回当前窗口的ID，调用close方法的时候要用
		 */
		open:function(title,width,height,url,buttons,onLoadCallback,onCloseCallback){
			//当前的时间戳作为新增的div的ID，这个ID要返回，关闭的时候要用
			var wid=new Date().getTime();
			$("body").append("<div id='"+wid+"'></div>");
			$("#"+wid).dialog({
				id:wid,
				title:title,
				width:width,
				height:height,
				href:url,
				modal:true,
				maximizable:true,
				minimizable:false,
				resizable:true,
				buttons:buttons,
				onClose:function(){
					$("#"+wid).dialog("destroy");
					$("#"+wid).remove();
					if( typeof onCloseCallback === 'function' ){
						onCloseCallback();
					}
				},onLoad:function(){
					if( typeof onLoadCallback === 'function' ){
						onLoadCallback();
					}
				}
			});
			return wid;
		},
		close:function(wid){
			$("#"+wid).dialog("destroy");
			$("#"+wid).remove();
		}
};

EasyUI.combobox=function(id,url,idFiled,textFiled){
	
}

EasyUI.datebox={
			formatter:function(date){
				return date.format("yyyy-MM-dd");
			}
}

EasyUI.datetimebox={
		formatter:function(date){
			return date.format("yyyy-MM-dd HH:mm:ss");
		}
}

EasyUI.tree={
			getTreeData:function(arrayData,valueField,textField,pid){
				function getChildren(parent){
					var children=[];
					for(var i=0;i<arrayData.length;i++){
						if(arrayData[i][pid]==parent.id){
							children.push({id:arrayData[i][valueField],text:arrayData[i][textField]});
						}
					}
					for(var i=0;i<children.length;i++){
						children[i].children=getChildren(children[i]);
					}
					return children;
				}
				
				var top=[];
				for(var i=0;i<arrayData.length;i++){
					if(arrayData[i][pid]==null){
						top.push({id:arrayData[i][valueField],text:arrayData[i][textField]});
					}
				}
				for(var i=0;i<top.length;i++){
					top[i].children=getChildren(top[i]);
				}
			return top;
		},
		select:function(treeId,nodeId){
			var node = $('#'+treeId).tree('find',nodeId);
            $('#'+treeId).tree("expandTo",node.target).tree('select', node.target);
		}
}
/**
 * 审核状态字体颜色
 */
function auditStyler(index,row){
	return "";
}
function auditStyler2(index,row){
	return "";
}

/**
 * 格式化状态显示
 * @param row
 * @returns {String}
 */
function auditStateFormatter(auditState){
	if (auditState == 1) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF8C00;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">审核中</span><span class="l-btn-icon icon-question">&nbsp;</span></span>';
	} else if (auditState == 2) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:green;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">已通过</span><span class="l-btn-icon icon-ok3">&nbsp;</span></span>';
	} else if (auditState == 0) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#5a080f;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">未提交</span><span class="l-btn-icon icon-commit">&nbsp;</span></span>';
	} else if (auditState == -1) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF0000;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">不通过</span><span class="l-btn-icon icon-del">&nbsp;</span></span>';
	}
}

function autoAuditStateFormatter(value,row,index){
	if (value == 1) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF8C00;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">审核中</span><span class="l-btn-icon icon-question">&nbsp;</span></span>';
	} else if (value == 2) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:green;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">已通过</span><span class="l-btn-icon icon-ok3">&nbsp;</span></span>';
	} else if (value == 0) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#5a080f;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">未提交</span><span class="l-btn-icon icon-commit">&nbsp;</span></span>';
	} else if (value == -1) {
		return '<span style="background:#dedddd;border: 1px solid #a9a4a4;color:#FF0000;font-weight:bold;" class="l-btn-left l-btn-icon-left"><span class="l-btn-text">不通过</span><span class="l-btn-icon icon-del">&nbsp;</span></span>';
	}
}

function bomAuditStateFormatter(auditState){
	if (auditState == 1) {
		return '<span style="color:#FF8C00;font-weight:bold;">[审核中]</span>';
	} else if (auditState == 2) {
		return '<span style="color:green;font-weight:bold;">[已通过]</span>';
	} else if (auditState == 0) {
		return '<span style="color:#5a080f;font-weight:bold;">[未提交]</span>';
	} else if (auditState == -1) {
		return '<span style="color:#FF0000;font-weight:bold;">[不通过]</span>';
	}
}

function auditTreeStyler(node) {
		if(node.attributes.nodeType=="version")
			return node.text+bomAuditStateFormatter(node.attributes.AUDITSTATE);
		else
			return node.text;

}
