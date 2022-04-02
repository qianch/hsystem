<!--
	作者:肖文彬
	日期:2016-9-28 11:24:47
	页面:客户管理JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加客户管理
var addUrl=path+"consumer/add";
//编辑客户管理
var editUrl=path+"consumer/edit";
//删除客户管理
var deleteUrl=path+"consumer/delete";
//作废客户管理
var oldUrl=path+"consumer/old"
var width=650,height=450;

//查询
function filter() {
	EasyUI.grid.search("dg","consumerSearchForm");
}

function formatterLevel(value,row){
	if(value=="1"){
		return "A";
	}
	if(value=="2"){
		return "B";
	}
	if(value=="3"){
		return "C";
	}
}

function formatterType(value,row){
	if(value=="1"){
		return "国内";
	}
	if(value=="2"){
		return "国外";
	}
}
//添加客户管理
var add = function() {
	var wid = Dialog.open("添加", width, height,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("consumerForm",addUrl, function(data){
			filter();
			if(Dialog.isMore(wid)){
				Dialog.close(wid);
				add();
			}else{
				Dialog.close(wid);
			}
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ],function(){Dialog.more(wid);}
	);
}

//编辑客户管理
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	var wid = Dialog.open("编辑", width, height, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("consumerForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除客户管理
var doDelete = function(){
	var r=EasyUI.grid.getSelections("dg");
	if(r.length == 0){
		Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
		return;
	}
		
	var ids=[];
	for(var i=0;i<r.length;i++){
		ids.push(r[i].ID);
	}
	Dialog.confirm(function(){
		JQ.ajax(deleteUrl, "post", {
			ids : ids.toString()
		}, function(data){
			filter();
		});
	});
}

//作废客户管理
var old = function(){
    var r=EasyUI.grid.getSelections("dg");
    if(r.length == 0){
        Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
        return;
    }

    var ids=[];
    for(var i=0;i<r.length;i++){
        ids.push(r[i].ID);
    }
    Dialog.confirm(function(){
        JQ.ajax(oldUrl, "post", {
            ids : ids.toString()
        }, function(data){
            filter();
        });
    });
}
/**
 * 双击行，弹出编辑
 */
var dbClickEdit=function(index,row){
	var wid = Dialog.open("编辑",width, height, editUrl+"?id="+row.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("consumerForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

var enableFilter=false;
function dgLoadSuccess(data){
	if(enableFilter)return;
	$(this).datagrid("enableFilter");
	$(".datagrid-filter[name='CONSUMERCATEGORY']").parent().remove();
	
}

</script>