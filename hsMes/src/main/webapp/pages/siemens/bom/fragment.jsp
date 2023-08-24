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
    <%@ include file="fragment.js.jsp" %>
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
<body class="easyui-layout" data-options="fit:true" oncontextmenu="window.event.returnValue=false">
<div region="west" split="true" title="BOM列表" style="width:260px;" data-options="minWidth:250">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',split:false,border:false" class="datagrid-toolbar"
             style="height:27px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searcher"
                   editable="true"
                   style="width:120px">
            <label for="isSiemens"
                   style="display: inline-block; border: 1px solid #e0e0e0; background: rgb(116, 217, 26); border-radius: 4px; height: 22px; color: white;">
                <input id="isSiemens" type="checkbox" checked="checked" onchange="searcher()"
                       style="display: inline-block; vertical-align: middle;margin-bottom: 5px; ">仅西门子
            </label>
        </div>
        <div data-options="region:'center'">
            <ul id="TcBomTree" class="easyui-tree"
                data-options="animate:true,formatter:treeFormatter,onLoadSuccess:onTreeLoadSuccess,onClick:onTreeClick,onContextMenu:function(e,n){
				e.preventDefault();
				$('#import').menu('show',{
						left: e.pageX,
						top: e.pageY
					});}">
            </ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false,title:'裁片管理',iconCls:'platform-icon65'">
    <div id="drawings-toolbar">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true"
           onclick="addFragment()">添加</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true"
           onclick="deleteFragment()">删除</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" plain="true"
           onclick="beginEdit()">编辑</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'platform-icon8'" plain="true"
           onclick="cancelFragmentEdit()">取消编辑</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" plain="true" onclick="saveFragment()">保存</a>
        <label class="save_tip">删除后保存生效</label>
    </div>
    <table id="fragmentDg" idField="id" singleSelect="false" class="easyui-datagrid" pagination="false"
           rownumbers="true" fitColumns="true" remoteSort="false" remoteFilter="local" toolbar="#drawings-toolbar"
           fit="true" data-options="onLoadSuccess:onFragmentDgLoadSuccess,onClickRow:onClickFragmentDgRow">
        <thead>
        <tr>
            <th field="id" checkbox="true"></th>
            <th field="fragmentCode" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{validType:'length[1,100]'}}">小部件编码
            </th>
            <th field="fragmentName" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{required:true,validType:'length[1,100]'}}">小部件名称
            </th>
            <th field="fragmentWeight" width="150" sortable="true"
                data-options="editor:{type:'numberbox',options:{required:true,precision:2,min:0,max:999}}">小部件重量(KG)
            </th>
            <th field="fragmentLength" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{validType:'length[1,100]'}}">长度(M)
            </th>
            <th field="fragmentWidth" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{validType:'length[1,100]'}}">宽度(MM)
            </th>
            <th field="fragmentCountPerDrawings" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{required:true,validType:'length[1,100]'}}">每套卷数
            </th>
            <th field="farbicModel" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{validType:'length[1,100]'}}">胚布规格
            </th>
            <th field="fragmentMemo" width="150" sortable="true"
                data-options="editor:{type:'textbox',options:{validType:'length[1,100]'}}">备注
            </th>
        </tr>
        </thead>
    </table>
</div>
<div id="import" class="easyui-menu" style="width:120px;">
    <div onclick="importFragment()" data-options="iconCls:'icon-excel'">导入</div>
</div>
</body>
</html>