<!--
作者:高飞
日期:2017-07-18 14:19:51
页面:西门子BOM文件
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="suit.js.jsp" %>
    <style type="text/css">
        body {
            -moz-user-select: none; /*火狐*/
            -webkit-user-select: none; /*webkit浏览器*/
            -ms-user-select: none; /*IE10*/
            -khtml-user-select: none; /*早期浏览器*/
            user-select: none;
        }

        .platform-icon75 {
            margin-right: -2px;
        }

        .platform-icon65 {
            margin-right: -2px;
        }

        .tooltip {
            background-color: #ffffff;
            border-color: #ddd;
            color: #01502c;
            border: 1px solid #036313;
            font-weight: bold;
            font-family: monospace;
        }

        .save_tip {
            display: inline-block;
            height: 20px;
            line-height: 20px;
            color: #b9b9b9;
            font-family: serif;
            font-size: smaller;
            padding-left: 10px;
            padding-right: 10px;
        }

        .textbox {
            border: 1px solid #d4d4d4;
        }

        .searchbox {
            border: 1px solid #e0e0e0;
        }
    </style>
    <title></title>
</head>
<body class="easyui-layout" data-options="fit:true">
<div region="west" split="true" title="BOM列表" style="width:260px;" data-options="minWidth:250">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',split:false,border:false" class="datagrid-toolbar"
             style="height:27px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searcher"
                   editable="true" style="width:120px">
            <label for="isSiemens"
                   style="display: inline-block; border: 1px solid #e0e0e0; background: rgb(116, 217, 26); border-radius: 4px; height: 22px; color: white;padding-right:5px;">
                <input id="isSiemens" type="checkbox" checked="checked" onchange="searcher()"
                       style="display: inline-block; vertical-align: middle;margin-bottom: 5px; ">
                仅西门子
            </label>
        </div>
        <div data-options="region:'center'">
            <ul id="TcBomTree" class="easyui-tree"
                data-options="animate:true,url:'${path}siemens/bom/list',method:'get',onBeforeLoad:onTreeBeforeLoad,formatter:treeFormatter,onLoadSuccess:onTreeLoadSuccess,onClick:onTreeClick,onContextMenu:onContextMenu"></ul>
        </div>
    </div>
</div>
<div id="suitLayout" data-options="region:'center',border:false,title:'组套BOM',iconCls:'platform-icon68'">
    <div id="suit-toolbar">
        <a href="#" id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true"
           onclick="addSuit()">添加</a>
        <a href="#" id="del" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true"
           onclick="deleteSuit()">删除</a>
        <a href="#" id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true"
           onclick="beginEdit()">编辑</a>
        <a href="#" id="cancelEdit" class="easyui-linkbutton" data-options="iconCls:'platform-icon8'" plain="true"
           onclick="cancelSuitEdit()">取消编辑</a>
        <a href="#" id="save" class="easyui-linkbutton" data-options="iconCls:'icon-save'" plain="true"
           onclick="saveSuit()">保存</a>
        <a href="#" id="importFromDrawings" class="easyui-linkbutton" data-options="iconCls:'platform-icon78'"
           plain="true" onclick="importFromDrawings()">从图纸BOM导入</a>
        <a href="#" id="disable" class="easyui-linkbutton" data-options="iconCls:'platform-cancel_select'" plain="true"
           onclick="disableBom()">BOM不可用</a>
        <a href="#" id="enable" class="easyui-linkbutton" data-options="iconCls:'platform-icon88'" plain="true"
           onclick="enableBom()">BOM可用</a>
        <label class="save_tip">删除后保存生效</label>
    </div>
    <table id="suitDg"
           idField="id"
           singleSelect="false"
           class="easyui-datagrid"
           pagination="false"
           rownumbers="true"
           fitColumns="false"
           remoteSort="false"
           remoteFilter="local"
           toolbar="#suit-toolbar"
           fit="true"
           data-options="onClickRow:onClickSuitDgRow">
        <thead>
        <tr>
            <th field="id" checkbox="true"></th>
            <!-- <th field="fragmentCode" width="15" sortable="true" data-options="editor:{type:'textbox'}">小部件编码</th> -->
            <th field="fragmentCode" width="150" sortable="true"
                data-options="editor:{type:'combobox',options:{url:'${path }siemens/bom/fragment/list',method:'get',formatter:fragmentFormatter,panelWidth:300,onBeforeLoad:onSuitComboboxBeforeLoad,filter:comboFilter,onHidePanel:suitValidCode,onSelect:onSuitComboSelect,onLoadSuccess:onComboboxLoadSuccess,required:true,editable:true,valueField:'fragmentCode',textField:'fragmentCode',panelHeight:'auto',panelMaxHeight:200}}">
                小部件编码
            </th>
            <th field="fragmentName" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{editable:true,required:true}}">名称
            </th>
            <th field="suitCount" width="150" sortable="true"
                data-options="editor:{type:'numberspinner',options:{required:true,min:0,max:9999,precision:0}}">数量
            </th>
            <th field="suitSort" width="150" sortable="true"
                data-options="editor:{type:'numberspinner',options:{required:true,min:0,max:9999,precision:0}}">顺序号
            </th>
        </tr>
        </thead>
    </table>
</div>
</div>
<div id="dlg" class="easyui-dialog" title="从图纸BOM导入" style="width:690px;height:400px;"
     data-options="
            	maximizable:true,
            	resizable:true,
            	closed:true,
                iconCls: 'platform-icon78',
                buttons: '#dlg-buttons',
                modal:true">
    <table id="tempSuitDg"
           idField="id"
           singleSelect="false"
           class="easyui-datagrid"
           pagination="false"
           rownumbers="true"
           fitColumns="false"
           remoteSort="false"
           remoteFilter="local"
           fit="true"
           data-options="
					onClickRow:onClickSuitDgRow,
					onLoadSuccess:function(){
						$(this).datagrid('enableDnd');
					}">
        <thead>
        <tr>
            <!-- <th field="id" checkbox="true">小部件编码</th> -->
            <th field="fragmentCode" width="150" sortable="true">小部件编码</th>
            <th field="fragmentName" width="150" sortable="true">名称</th>
            <th field="suitCount" width="150" sortable="true">数量</th>
            <th field="suitSort" width="150" sortable="true">顺序号</th>
        </tr>
        </thead>
    </table>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" iconCls="icon-save" class="easyui-linkbutton" onclick="getSuitBom()">保存</a>
    <a href="javascript:void(0)" iconCls="icon-cancel" class="easyui-linkbutton"
       onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>
</body>
</html>