<!--
作者:高飞
日期:2017-07-18 14:19:51
页面:西门子BOM文件
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="drawings.js.jsp" %>

    <style type="text/css">
        .datagrid-footer table {
            color: red;
            font-weight: bold;
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
<div region="west" split="true" title="BOM列表" style="width:260px;"
     data-options="minWidth:250">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',split:false,border:false" class="datagrid-toolbar"
             style="height:27px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searcher"
                   editable="true" style="width:120px">
            <label for="isSiemens"
                   style="display: inline-block; border: 1px solid #e0e0e0; background: rgb(116, 217, 26); border-radius: 4px; height: 22px; color: white;padding-right:5px;">
                <input id="isSiemens" type="checkbox" checked="checked" onchange="searcher()"
                       style="display: inline-block; vertical-align: middle;margin-bottom: 5px; ">仅西门子
            </label>
        </div>
        <div data-options="region:'center'">
            <ul id="TcBomTree" class="easyui-tree"
                data-options="animate:true,url:'${path}siemens/bom/list',method:'get',onBeforeLoad:onTreeBeforeLoad,formatter:treeFormatter,onLoadSuccess:onTreeLoadSuccess,onClick:onTreeClick,onContextMenu:
				function(e,n){
				e.preventDefault();
				$('#import').menu('show',{
						left: e.pageX,
						top: e.pageY
					});}">
            </ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false,title:'图纸BOM',iconCls:'platform-icon78'">
    <div id="drawings-toolbar">
        <a href="#" id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true"
           onclick="addDrawings()">添加</a>
        <a href="#" id="del" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true"
           onclick="deleteDrawings()">删除</a>
        <a href="#" id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true"
           onclick="beginEdit()">编辑</a>
        <a href="#" id="cancelEdit" class="easyui-linkbutton" data-options="iconCls:'platform-icon8'" plain="true"
           onclick="cancelDrawingsEdit()">取消编辑</a>
        <a href="#" id="save" class="easyui-linkbutton" data-options="iconCls:'icon-save'" plain="true"
           onclick="saveDrawings()">保存</a>
        <a href="#" id="disable" class="easyui-linkbutton" data-options="iconCls:'platform-cancel_select'" plain="true"
           onclick="disableBom()">禁用</a>
        <a href="#" id="enable" class="easyui-linkbutton" data-options="iconCls:'platform-icon88'" plain="true"
           onclick="enableBom()">启用</a> <label class="save_tip">删除后保存生效</label>
    </div>
    <table id="drawingsDg" idField="id" singleSelect="false"
           class="easyui-datagrid" pagination="false" rownumbers="true"
           fitColumns="true" remoteSort="false" remoteFilter="local"
           toolbar="#drawings-toolbar" fit="true"
           data-options="onLoadSuccess:onDrawingsDgLoadSuccess,onClickRow:onClickDrawingsDgRow">
        <thead>
        <tr>
            <th field="id" checkbox="true"></th>
            <!-- <th field="fragmentCode" width="15" sortable="true" data-options="editor:{type:'textbox'}">小部件编码</th> -->
            <%-- <th field="fragmentName" formatter="fragmentFormatter" width="20" sortable="true" data-options="editor:{type:'combobox',options:{url:'${path }siemens/bom/fragment/list',method:'get',onBeforeLoad:onDrawingsComboboxBeforeLoad,filter:comboFilter,onHidePanel:drawingsValidCode,onSelect:onDrawingsComboSelect,onLoadSuccess:onComboboxLoadSuccess,required:true,editable:true,valueField:'fragmentName',textField:'fragmentName',panelHeight:'auto',panelMaxHeight:200}}">小部件名称</th> --%>
            <th field="fragmentCode" width="28" sortable="true"
                data-options="editor:{type:'combobox',options:{url:'${path }siemens/bom/fragment/list',method:'get',onBeforeLoad:onDrawingsComboboxBeforeLoad,formatter:fragmentFormatter,panelWidth:300,filter:comboFilter,onHidePanel:drawingsValidCode,onSelect:onDrawingsComboSelect,onLoadSuccess:onComboboxLoadSuccess,required:true,editable:true,valueField:'fragmentCode',textField:'fragmentCode',panelHeight:'auto',panelMaxHeight:200}}">
                小部件编码
            </th>
            <th field="fragmentName" width="23" sortable="true"
                data-options="editor:{type:'textbox',options:{required:false,readonly:true}}">名称
            </th>
            <th field="fragmentWeight" width="15" sortable="true"
                data-options="editor:{type:'textbox',options:{required:false,readonly:true}}">重量
            </th>
            <th field="fragmentLength" width="15" sortable="true"
                data-options="editor:{type:'textbox',options:{required:false,readonly:true}}">长度(M)
            </th>
            <th field="fragmentWidth" width="15" sortable="true"
                data-options="editor:{type:'textbox',options:{required:false,readonly:true}}">宽度(MM)
            </th>
            <th field="farbicModel" width="15" sortable="true"
                data-options="editor:{type:'textbox',options:{editable:false,required:true,validType:'length[1,100]'}}">
                胚布规格
            </th>
            <th field="fragmentCountPerDrawings" width="10" sortable="true"
                data-options="editor:{type:'numberbox',options:{editable:true,required:true,min:0,max:999,precision:0}}">
                数量
            </th>
            <th field="fragmentDrawingNo" width="15" sortable="true"
                data-options="editor:{type:'textbox',options:{required:true,validType:'length[1,100]'}}">图号
            </th>
            <th field="fragmentDrawingVer" width="12" sortable="true"
                data-options="editor:{type:'textbox',options:{required:true,validType:'length[1,100]'}}">图纸版本
            </th>
            <%-- <th field="partId" formatter="partNameFormatter" width="20" sortable="true" data-options="editor:{type:'combobox',options:{url:'${path }siemens/bom/parts',onBeforeLoad:onDrawingsComboboxBeforeLoad,filter:comboFilter,onHidePanel:validCode,onSelect:onDrawingsComboSelect,required:true,editable:true,valueField:'ID',textField:'NAME',panelHeight:'auto'}}">部件名称</th> --%>
            <th field="suitCountPerDrawings" width="15" sortable="true"
                data-options="editor:{type:'numberbox',options:{required:true,min:1,max:999,precision:0}}">图内套数
            </th>
            <th field="printSort" width="15" sortable="true"
                data-options="editor:{type:'numberbox',options:{required:true,min:1,max:9999,precision:0}}">出图顺序
            </th>
            <th field="fragmentMemo" width="20" sortable="true"
                data-options="editor:{type:'textbox',options:{editable:true,validType:{length:[0,100]}}}">备注
            </th>
        </tr>
        </thead>
    </table>
</div>
</div>
<div id="dlg" class="easyui-dialog" title="图号和小部件数量确认"
     style="width:400px;height:300px;"
     data-options="maximizable:true,resizable:true,closed:true, iconCls: 'platform-paste', buttons: '#dlg-buttons',modal:true">
    <table id="summaryDg" class="easyui-datagrid" width="100%" pagination="false" rownumbers="true" fitColumns="true"
           fit="true" showFooter="true">
        <thead>
        <tr>
            <th field="NO" width="100">图号</th>
            <th field="COUNT" width="100">小部件数量</th>
        </tr>
        </thead>
    </table>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" iconCls="icon-ok"
       class="easyui-linkbutton" onclick="doEnable()">确认启用</a> <a
        href="javascript:void(0)" iconCls="icon-cancel"
        class="easyui-linkbutton"
        onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>
<div id="import" class="easyui-menu" style="width:120px;">
    <div onclick="importDrawings()" data-options="iconCls:'icon-excel'">导入</div>
</div>
</body>
</html>