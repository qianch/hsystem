<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:包材bom明细JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>裁片图纸bom明细</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="cutTcBomMain.js.jsp" %>
    <style type="text/css">
        td {
            min-width: 30px;
            padding: 3px;
        }

        .title {
            padding-right: 5px;
        }

    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" title="裁剪BOM列表" style="width:180px;">
    <div class="easyui-layout" fit=true>
        <div data-options="region:'north',split:false,border:false" class="datagrid-toolbar"
             style="height:75px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchInfo"
                   editable="true"
                   data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');findBcBom();}}]">

            <div class="datagrid-toolbar" style="text-align: center;font-size:13px;font-weight:bolder;padding:5px;">
                <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="expandAll()">展开</a> &nbsp;
                <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="collapseAll()">收缩</a> &nbsp;
                <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="reload(true)">刷新</a>
            </div>
        </div>
        <div data-options="region:'center'">
            <ul id="cutTcBomMainTree" class="easyui-tree"></ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false" style="position: relative;">
    <div id="toolbarCutTcBomPartMain">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="addPartMain" name="ids"/>
            <jsp:param value="doDeletePartMain" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="addPartMain()" name="funs"/>
            <jsp:param value="doDeletePartMain()" name="funs"/>
        </jsp:include>
    </div>
    <div id="toolbarCutTcBomPartDetail">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="addPartDetail" name="ids"/>
            <jsp:param value="doDeletePartDetail" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="addPartDetail()" name="funs"/>
            <jsp:param value="doDeletePartDetail()" name="funs"/>
        </jsp:include>
    </div>
    <div style="height:50%" data-options="region:'center',title:'裁剪图纸套材明细'">
        <table singleSelect="true" fit="true" id="cutTcBomDetailDg" pagination="true" title="裁剪套材bom明细"
               class="easyui-datagrid" url="" rownumbers="true" fitColumns="false">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PARTNAME" sortable="true" width="100" editor="{type:'textbox',options:{precision:0}}">
                    部件名称
                </th>
                <th field="DRAWNAME" sortable="true" editor="{type:'textbox',options:{precision:0}}">
                    图纸名称
                </th>
                <th field="ORIENTATION" sortable="true" width="100" editor="{type:'textbox',options:{precision:0}}">
                    朝向
                </th>
                <th field="PRODUCTMODEL" sortable="true" width="100"
                    editor="{type:'textbox',options:{precision:0}}">
                    规格
                </th>
                <th field="LENGTH" sortable="true" width="100"
                    editor="{type:'numberbox',options:{precision:0}}">米长
                </th>
                <th field="GRAMWEIGHT" sortable="true" width="100"
                    editor="{type:'numberbox',options:{precision:0}}">克重
                </th>
                <th field="PRODUCTIONRATE" sortable="true" width="100"
                    editor="{type:'numberbox',options:{precision:0}}">制成率
                </th>
                <th field="UNITPRICE" hidden="true" sortable="true" width="100"
                    editor="{type:'numberbox',options:{precision:0}}">单价
                </th>
            </tr>
            </thead>
        </table>
    </div>
    <div style="height:50%">
        <div style="float: left;height:100%;width: 55%">
            <table singleSelect="true" fit="true" id="cutTcBomPartMainDg" pagination="true" title="裁片列表"
                   class="easyui-datagrid" url=""
                   toolbar="#toolbarCutTcBomPartMain" rownumbers="true" fitColumns="false"
                   data-options="onDblClickRow:cutTcBomPartMainDg_DbClick,onClickRow:cutTcBomPartMainDg_Click,onEndEdit:cutTcBomPartMainDg_onEndEdit">
                <thead>
                <tr>
                    <th field="ID" checkbox=true></th>
                    <th field="TCBOMMAINID" hidden='true'></th>
                    <th field="PARTNAME" sortable="true" width="100" data-options="editor:{type:'textbox'}">部件名称
                    </th>
                    <th field="PRODUCTMODEL" sortable="true" data-options="editor:{type:'textbox'}">胚布规格
                    </th>
                    <th field="CUTNAME" sortable="true" width="100" data-options="editor:{type:'textbox'}">裁片名称
                    </th>
                    <th field="LAYERNUM" sortable="true" width="50"
                        data-options="editor:{type:'numberbox',options:{precision:0,required:true}}">层数
                    </th>
                    <th field="REMARK" sortable="true" width="100" data-options="editor:{type:'textbox'}">备注
                    </th>
                </tr>
                </thead>
            </table>
        </div>
        <div style="float: left;height:100%;width: 45%">
            <table singleSelect="true" fit="true" id="cutTcBomPartDetailDg" pagination="true" title="裁片详情列表"
                   class="easyui-datagrid"
                   url=""
                   toolbar="#toolbarCutTcBomPartDetail" rownumbers="true" fitColumns="false"
                   data-options="onDblClickRow:cutTcBomPartDetailDg_DbClick,onClickRow:cutTcBomPartDetailDg_Click,onEndEdit:cutTcBomPartDetailDg_onEndEdit">
                <thead>
                <tr>
                    <th field="ID" checkbox=true></th>
                    <th field="MAINID" hidden='true'></th>
                    <th field="TCBOMMAINID" hidden='true'></th>
                    <th field="CUTNAMELAYNO" sortable="true" width="100"
                        data-options="editor:{type:'textbox',options:{required:true}}">裁片明细/层
                    </th>
                    <th field="LAYNO" sortable="true" width="100"
                        data-options="editor:{type:'numberbox',options:{precision:0,required:true}}">裁片层号
                    </th>
                    <th field="AMOUNT" sortable="true" width="100"
                        data-options="editor:{type:'numberbox',options:{precision:0,required:true}}">卷/套
                    </th>

                    <th field="PACKSEQUENCE" sortable="true" width="100"
                        data-options="editor:{type:'numberbox',options:{precision:0,required:true}}">包装顺序
                    </th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<div data-options="region:'east',border:true,split:true" title="图纸bom基本信息" style="height:100px;width:200px">
    <table style="width: 99%;">
        <tr>
            <td class="title" style="text-align: left;">BOM代码版本</td>
        </tr>
        <tr>
            <td id="tcProcBomCodeVersion"></td>
        </tr>
        <tr>
            <td style="text-align: left;" class="title">叶型名称</td>
        </tr>
        <tr>
            <td id="bladeTypeName"></td>
        </tr>
        <tr>
            <td style="text-align: left;" class="title">客户名称</td>
        </tr>
        <tr>
            <td id="customerName"></td>
        </tr>
        <tr>
            <td style="text-align: left;" class="title">客户编码</td>
        </tr>
        <tr>
            <td id="customerCode"></td>
        </tr>
    </table>
</div>
<div id="mainMenu" class="easyui-menu" style="width:130px;">
    <div onclick="addCutTcBomMain()" data-options="iconCls:'icon-add'">添加</div>
    <div onclick="importDetail()" data-options="iconCls:'icon-add'">导入裁剪bom</div>
    <div onclick="importCutTcBomPartMain()" data-options="iconCls:'icon-add'">导入裁片</div>
    <div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
</div>
<div id="treeMenu" class="easyui-menu" style="width:130px;">
    <div onclick="editCutTcBomMain()" data-options="iconCls:'icon-edit'">修改/查看</div>
    <div onclick="cancelCutTcBomMain()" data-options="iconCls:'icon-remove'">作废</div>
    <div onclick="effectCutTcBomMain()" data-options="iconCls:'icon-add'">生效</div>
    <div onclick="exportDetail()" data-options="iconCls:'icon-add'">导出裁剪bom图纸</div>
    <div onclick="exportCutTcBomPart()" data-options="iconCls:'icon-add'">导出质检确认表1</div>
</div>
</body>
