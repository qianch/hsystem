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

	var bomList = path + "siemens/bom/list";
	var suitList = path + "siemens/bom/suit/list";
	
	var fragmentMap={};

	$(function() {
		searcher();
	});

	//套材BOM搜索
	function searcher(value, name) {
		$(".tooltip").remove();
		$("#TcBomTree").tree("reload");
	}

	//BOM树加tooltip
	function treeFormatter(node) {
		return "<span title='"+node.text+"' class='easyui-tooltip'>" + node.text + "</span>";
	}
	
	var comboboxLoaded=false;
	function onComboboxLoadSuccess(){
		if(comboboxLoaded)return;
		comboboxLoaded=true;
		fragmentMap={};
		var data=$(this).combobox("getData");
		for(var i=0;i<data.length;i++){
			fragmentMap[data[i].fragmentCode]=data[i]
		}
	}
	
	function onTreeBeforeLoad(node, param){
		var siemens = $("#isSiemens:checked").is(':checked');
		param.siemens=siemens;
		try {
			var code = $("#searchInput").searchbox("getValue");
			param.code=code;
		} catch (e) {}
		if(node!=null){
			param.tcBomId=node.id;
		}
	}
	
	//树加载成功事件
	function onTreeLoadSuccess(node, data) {
		$.parser.parse($(this));
	}

	//树点击事件
	function onTreeClick(node) {
		if(!isPartNode(node))
			return;
		//var bomNode=$("#TcBomTree").tree("getParent",node.target);
		loadSuit(node.id);
		bomState(node.id,-1,false);
	}
	
	var saveSuitUrl=path+"siemens/bom/suit/add";

	//加载图纸BOM
	function loadSuit(partId){
		$("#suitDg").datagrid("uncheckAll");
		var rows=$("#suitDg").datagrid("getRows");
		
		for(var i=0;i<rows.length;i++){
			$("#suitDg").datagrid("endEdit",i);
		}
		
		var inserted = $("#suitDg").datagrid('getChanges', "inserted");
	    var deleted = $("#suitDg").datagrid('getChanges', "deleted");
	    var updated = $("#suitDg").datagrid('getChanges', "updated");
	    
	    if(inserted.length!=0||deleted.length!=0||updated.length!=0){
	    	$.messager.confirm("信息提示","是否保存当前更改？", function (data) {  
	    	       if(data){
	    	    	   if(saveSuit(partId)){
	    	    		   comboboxLoaded=false;
	    	    	   }
	    	       }else{
	    	    	  	var node=$("#TcBomTree").tree("getSelected");
	    	    	  	var title="组套BOM"+(node.attributes.needSort==0?"有序":"无序");
	    		    	$("#suitLayout").panel({title:title});
	    	    	   if(!partId)return;
	    	    	   Loading.show("正在加载");
	    	    	   comboboxLoaded=false;
	    	    	   JQ.ajaxGet(suitList+"?partId="+partId,function(data){
	    	    		   	Loading.hide();
	    	    			$("#suitDg").datagrid("loadData",data);
	    	    		});
	    	       }
	    	});
	    }else{
	    	var node=$("#TcBomTree").tree("getSelected");
	    	var title="组套BOM"+(node.attributes.needSort==0?"有序":"无序");
	    	$("#suitLayout").panel({title:title});
	    	if(!partId)return;
	    	Loading.show("正在加载");
	    	JQ.ajaxGet(suitList+"?partId="+partId,function(data){
	    		Loading.hide();
	    		comboboxLoaded=false;
				$("#suitDg").datagrid({data:data});
			});
	    }
	}
	
	function isPartNode(node){
		if(node==null)
			return false;
		if($("#TcBomTree").tree("getParent",node.target)==null)
			return false;
		if($("#TcBomTree").tree("getParent",$("#TcBomTree").tree("getParent",node.target).target)==null)
			return false;
		return true;
	}
	
	function onSuitComboboxBeforeLoad(param){
		var index=$(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
		var row=EasyUI.grid.getRowByIndex('suitDg',index);
		param.tcBomId=row.tcBomId;
	}

	function addSuit(){
		var node=$("#TcBomTree").tree("getSelected");
		if(!isPartNode(node)){
			Tip.warn("请选择部件节点");
			return;
		}
		var bomNode=$("#TcBomTree").tree("getParent",node.target);
		$("#suitDg").datagrid("appendRow",{tcBomId:bomNode.id,partId:node.id,partName:node.text,id:(new Date().getTime())});
	}

	function onClickSuitDgRow(index,row){
		$(this).datagrid("beginEdit",index);
	}

	function saveSuit(partId){
		Loading.show("正在校验");
		var rows=$("#suitDg").datagrid("getRows");
		var NO={};
		var needSort=$("#suitLayout").panel("options").title.indexOf("有序")!=-1?true:false;
		for(var i=0;i<rows.length;i++){
			//$("#suitDg").datagrid("beginEdit",i);
			if(!$("#suitDg").datagrid("validateRow",i)){
				Tip.warn("请修正红色输入框的内容");
				Loading.hide();
				return;
			}
			$("#suitDg").datagrid("endEdit",i);
			if(needSort){
				if(NO[rows[i].suitSort]!=null){
					Tip.warn("第"+(i+1)+"行 和 第"+NO[rows[i].suitSort]+"行 顺序号 一样，请修改");
					Loading.hide();
					return;
				}
				NO[rows[i].suitSort]=i+1;
			}
		}
		
		if(!partId&&rows.length>0)
			partId=rows[0].partId;
		
		var inserted = $("#suitDg").datagrid('getChanges', "inserted");
	    var deleted = $("#suitDg").datagrid('getChanges', "deleted");
	    var updated = $("#suitDg").datagrid('getChanges', "updated");
	    if(inserted.length==0&&deleted.length==0&&updated==0){
	    	Tip.warn("未作任何修改");
	    	Loading.hide();
	    	return false;
	    }
	    var suitGrid={inserted:inserted,deleted:deleted,updated:updated};
	    Loading.hide();
	    Loading.show("正在保存");
		$.ajax({
			url:saveSuitUrl,
			type:"post",
			data:JSON.stringify(suitGrid),
			contentType:"application/json",
			success:function(data){
				Loading.hide();
				Tip.success("保存成功");
				$("#suitDg").datagrid("acceptChanges");
				$("#suitDg").datagrid("uncheckAll");
				if(partId)
					loadSuit(partId);
				return true;
			}
		});
	}

	function deleteSuit(){
		EasyUI.grid.deleteSelected("suitDg");
	}
	
	function fragmentFormatter(v,r,i){
		return r.fragmentName;
	}
	
	
	function suitValidCode(){
		var _options = $(this).combobox('options');
	    var _data = $(this).combobox('getData');/* 下拉框所有选项 */  
	    var _value = $(this).combobox('getValue');/* 用户输入的值 */  
	    var _text = $(this).combobox('getText');
	    var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */  
	    log(_value,_text)
	    for (var i = 0; i < _data.length; i++) {  
	        if (_data[i][_options.valueField] == _value&&_data[i][_options.textField]==_text) {  
	            _b=true;
	            onSuitComboSelect({id:_value,fragmentCode:_text},this);
	            break;
	        }  
	    }
	    if(!_b){
	        $(this).combobox('setValue', '');
	        var index=$(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
			var row=EasyUI.grid.getRowByIndex('suitDg',index);
			
			row.fragmentId=undefined;
			row.fragmentName=undefined;
			
			var ed = $("#suitDg").datagrid('getEditor', {index:index,field:"fragmentCode"});
			$(ed.target).textbox("setValue","");
			
			ed = $("#suitDg").datagrid('getEditor', {index:index,field:"fragmentName"});
			$(ed.target).textbox("setValue","");
			
	        return;
	    }
	}
	
	function onSuitComboSelect(record,_this){
			var index=$(_this?_this:this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
			var row=EasyUI.grid.getRowByIndex('suitDg',index);
			
			var fragment=fragmentMap[record.fragmentCode];
			
			row.fragmentId=fragment.id;
			row.fragmentCode=fragment.fragmentCode;
			row.fragmentWeight=fragment.fragmentWeight;
			row.fragmentLength=fragment.fragmentLength;
			row.fragmentWidth=fragment.fragmentWidth;
			row.fragmentMemo=fragment.fragmentMemo;
			row.fragmentCountPerDrawings=fragment.fragmentCountPerDrawings;
			row.farbicModel=fragment.farbicModel;
			
			var ed = $("#suitDg").datagrid('getEditor', {index:index,field:"fragmentName"});
			$(ed.target).textbox("setValue",fragment.fragmentName);
			
	}

	function beginEdit(){
		var rows=$("#suitDg").datagrid("getRows");
		for(var i=0;i<rows.length;i++){
			$("#suitDg").datagrid("beginEdit",0);
			break;
		}
	}

	function cancelSuitEdit(){
		$("#suitDg").datagrid("uncheckAll");
		$("#suitDg").datagrid("rejectChanges");
	}
	
	function fragmentFormatter(row){
		return row.fragmentCode+" / "+row.fragmentName;
	}
	
	function comboFilter(q,row){
		return row.fragmentName.toUpperCase().indexOf(q.toUpperCase()) !=-1||row.fragmentCode.toUpperCase().indexOf(q.toUpperCase()) !=-1;
	}
	
	function disableBom(){
		var node=$("#TcBomTree").tree("getSelected");
		if(!isPartNode(node)){
			Tip.warn("请选择部件节点");
			return;
		}
		bomState(node.id, 0, false);
	}
	
	function enableBom(){
		var node=$("#TcBomTree").tree("getSelected");
		if(!isPartNode(node)){
			Tip.warn("请选择部件节点");
			return;
		}
		bomState(node.id, 1, false);
	}
	
	function enableBomButton(){
		$("#add").linkbutton("enable");
		$("#del").linkbutton("enable");
		$("#edit").linkbutton("enable");
		$("#cancelEdit").linkbutton("enable");
		$("#save").linkbutton("enable");
		$("#importFromDrawings").linkbutton("enable");
		$("#enable").linkbutton("enable");
		$("#disable").linkbutton("disable");
	}
	
	function disableBomButton(){
		$("#add").linkbutton("disable");
		$("#del").linkbutton("disable");
		$("#edit").linkbutton("disable");
		$("#cancelEdit").linkbutton("disable");
		$("#save").linkbutton("disable");
		$("#importFromDrawings").linkbutton("disable");
		$("#enable").linkbutton("disable");
		$("#disable").linkbutton("enable");
	}
	
	function bomState(partId,state,isDrawingsBom){
		JQ.ajaxPost(path+"siemens/bom/enable",{partId:partId,enable:state==-1?"":state,isDrawingsBom:isDrawingsBom},function(data){
			if(data.state==0){
				enableBomButton();
			}else{
				disableBomButton();
			}
		});
	}
	
	function importFromDrawings(){
		var node=$("#TcBomTree").tree("getSelected");
		if(!isPartNode(node)){
			Tip.warn("请选择部件节点");
			return;
		}
		
		Dialog.confirm(function(){
			JQ.ajaxGet(path+"siemens/bom/drawingsToSuit?partId="+node.id,function(data){
				$("#dlg").dialog("open");
				$("#tempSuitDg").datagrid("loadData",data);
			});
		},"如果从图纸BOM导入，会清空之前的组套BOM，是否继续？");
	}
	
	function getSuitBom(){
		Loading.show("正在导入");
		setTimeout(function(){
			$("#dlg").dialog("close");
			$("#suitDg").datagrid("loadData",[]);
			var rows=$("#tempSuitDg").datagrid("getRows");
			for(var i=0;i<rows.length;i++){
				$("#suitDg").datagrid("appendRow",rows[i]);			
			}
			Loading.hide();
		},0);
	}
	
	function onContextMenu(e,node){
		$(".tooltip").remove();
		e.preventDefault();
		$("#TcBomTree").tree("expand",node.target);
	}

</script>