<!--
	作者:高飞
	日期:2017-07-18 14:19:51
	页面:西门子BOM文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	$.extend($.fn.textbox.defaults, {
		"icons" : []
	});
	$.extend($.fn.combobox.defaults, {
		"icons" : []
	});
	$.extend($.fn.combotree.defaults, {
		"icons" : []
	});
	$.extend($.fn.numberbox.defaults, {
		"icons" : []
	});

	$.extend($.fn.searchbox.defaults, {
		"icons" : []
	});
	
	var bomList = path + "siemens/bom/list?showChildren=false";
	var fragmentList = path + "siemens/bom/fragment/list";
	var importUrl=path+"siemens/bom/import";
	$(function() {
		searcher();
	});

	//套材BOM搜索
	function searcher(value, name) {
		var siemens = $("#isSiemens:checked").is(':checked');
		var code = $("#searchInput").searchbox("getValue");
		JQ.ajaxGet(bomList + "&siemens=" + siemens + "&code=" + code, function(data) {
			/* var tree = [];
			tree.push({
				id : -1,
				text : (siemens ? "西门子BOM" : "所有套材BOM"),
				iconCls : 'platform-icon76',
				children : data
			}); */
			$("#TcBomTree").tree("loadData", data);
		});
	}

	//BOM树加tooltip
	function treeFormatter(node) {
		return "<span title='"+node.text+"' class='easyui-tooltip'>" + node.text + "</span>";
	}

	//树加载成功事件
	function onTreeLoadSuccess(node, data) {
		$.parser.parse($(this));
	}

	//树点击事件
	function onTreeClick(node) {
		loadFragment(node.id);
	}
	
	var saveFragmentUrl=path+"siemens/bom/fragment/add";

	//加载图纸BOM
	function loadFragment(bomId){
		$("#fragmentDg").datagrid("uncheckAll");
		var rows=$("#fragmentDg").datagrid("getRows");
		
		for(var i=0;i<rows.length;i++){
			$("#fragmentDg").datagrid("endEdit",i);
		}
		
		var inserted = $("#fragmentDg").datagrid('getChanges', "inserted");
	    var deleted = $("#fragmentDg").datagrid('getChanges', "deleted");
	    var updated = $("#fragmentDg").datagrid('getChanges', "updated");
	    
	    if(inserted.length!=0||deleted.length!=0||updated.length!=0){
	    	$.messager.confirm("信息提示","是否保存当前更改？", function (data) {  
	    	       if(data){
	    	    	   saveFragment(bomId);
	    	       }else{
	    	    	   if(!bomId)return;
	    	    	   Loading.show("正在加载");
	    	    	   JQ.ajaxGet(fragmentList+"?tcBomId="+bomId,function(data){
	    	    		   	Loading.hide();
	    	    			$("#fragmentDg").datagrid("loadData",data);
	    	    		});
	    	       }
	    	});
	    }else{
	    	 if(!bomId)return;
	    	Loading.show("正在加载");
	    	JQ.ajaxGet(fragmentList+"?tcBomId="+bomId,function(data){
	    		Loading.hide();
				$("#fragmentDg").datagrid({data:data});
			});
	    }
	}
	//图纸BOM列表加载完毕
	function onFragmentDgLoadSuccess(data){
		//$("#fragmentDg").datagrid("enableFilter");
	}

	function addFragment(){
		var node=$("#TcBomTree").tree("getSelected");
		if(node==null){
			Tip.warn("请选择工艺BOM");
			return;
		}
		$("#fragmentDg").datagrid("appendRow",{tcBomId:node.id,isDeleted:0,id:(new Date().getTime())});
	}

	function onClickFragmentDgRow(index,row){
		$(this).datagrid("beginEdit",index);
	}

	function saveFragment(tcBomId){
		
		Loading.show("正在校验");
		var rows=$("#fragmentDg").datagrid("getRows");
		
		for(var i=0;i<rows.length;i++){
			//$("#fragmentDg").datagrid("beginEdit",i);
			if(!$("#fragmentDg").datagrid("validateRow",i)){
				Tip.warn("请修正红色输入框的内容");
				Loading.hide();
				return;
			}
			$("#fragmentDg").datagrid("endEdit",i);
		}
		
		if(!tcBomId&&rows.length>0)
			tcBomId=rows[0].tcBomId;
			
		var inserted = $("#fragmentDg").datagrid('getChanges', "inserted");
	    var deleted = $("#fragmentDg").datagrid('getChanges', "deleted");
	    var updated = $("#fragmentDg").datagrid('getChanges', "updated");
	    if(inserted.length==0&&deleted.length==0&&updated==0){
	    	Tip.warn("未作任何修改");
	    	Loading.hide();
	    	return;
	    }
	    var fragmentGrid={inserted:inserted,deleted:deleted,updated:updated};
	    Loading.hide();
	    Loading.show("正在保存");
		$.ajax({
			url:saveFragmentUrl,
			type:"post",
			data:JSON.stringify(fragmentGrid),
			contentType:"application/json",
			success:function(data){
				Loading.hide();
				Tip.success("保存成功");
				$("#fragmentDg").datagrid("acceptChanges");
				$("#fragmentDg").datagrid("uncheckAll");
				loadFragment(tcBomId);
			}
		});
	}

	function deleteFragment(){
		EasyUI.grid.deleteSelected("fragmentDg");
	}

	function onFragmentComboboxBeforeLoad(param){
		var node=$("#TcBomTree").tree("getSelected");
		param.tcBomId=node.id;
	}

	function onFragmentComboSelect(record){
		var index=$(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
		var row=EasyUI.grid.getRowByIndex('fragmentDg',index);
		row.partId=record.ID;
		row.partName=record.NAME;
	}

	function beginEdit(){
		var rows=$("#fragmentDg").datagrid("getRows");
		for(var i=0;i<rows.length;i++){
			$("#fragmentDg").datagrid("beginEdit",i);
		}
	}

	function partNameFormatter(v,r,i){
		return r.partName;
	}

	function cancelFragmentEdit(){
		$("#fragmentDg").datagrid("uncheckAll");
		$("#fragmentDg").datagrid("rejectChanges");
	}
	/**
	 * 西门子裁片导入
	 */
	
	var importFragment = function() {
		var node=$("#TcBomTree").tree("getSelected");
		var tcBomId="";
		if(node==null){
			Tip.warn("请选择工艺BOM");
			return;
		}else{
			tcBomId=node.id;
		}
		var wid = Dialog.open("导入", 280, 280,importUrl, [EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("form", importUrl+"?tcBomId="+tcBomId, function(data) {
				if(data.excelErrorMsg){//如果有导入 Excel错误信息，就显示出来
					//$.messager.alert("导入Excel错误",data.excelErrorMsg,""); 
					var c='<div style="max-height:400px;">'+data.excelErrorMsg+'</div>';
					$.messager.show({
						title:'导入Excel错误',
						msg:c,
						timeout:0,
						showType:'fade'
					});
					return;
				}else if(!data.error){
					Tip.success("保存成功");
					 Loading.show("正在加载");
	    	    	   JQ.ajaxGet(fragmentList+"?tcBomId="+tcBomId,function(data){
	    	    		   	Loading.hide();
	    	    			$("#fragmentDg").datagrid("loadData",data);
	    	    		});
	    	    	   Dialog.close(wid);
			}
				filter();
				reload(true);
				if (Dialog.isMore(wid)) {
					Dialog.close(wid);
					add();
				} else {
					Dialog.close(wid);
				}
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ]
		);
	}
</script>