<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>非套材BOM</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="ftcBom.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true">
<div region="west" split="true" title="非套材BOM列表" style="width:260px;">
    <div class="easyui-layout" fit=true>
        <div data-options="region:'north',split:false,border:false"
             class="datagrid-toolbar" style="height:75px;text-align: center;">
            <input id="searchInput" type="text" class="easyui-searchbox"
                   prompt="请输入内容" searcher="searchInfo" editable="true"
                   data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');findFtcBom();}}]">
            <input type="text" id="testPro" value="1" class="easyui-combobox" style="width: 90%" panelHeight="auto"
                   editable="false" data-options="data: [
	                        {value:'1',text:'常规产品'},
	                        {value:'-1',text:'变更试样'},
	                        {value:'2',text:'新品试样'}
                    	],onSelect:function(rec){
                    	findFtcBom();
        				},icons:[]">
            <div class="datagrid-toolbar" style="text-align: center;font-size:13px;font-weight:bolder;padding:5px;">
                <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="expandAll()">展开</a> &nbsp;
                <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="collapseAll()">收缩</a>&nbsp;
                <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="reload(true)">刷新</a>
            </div>
        </div>
        <div data-options="region:'center'">
            <ul id="FTcBomTree" class="easyui-tree"></ul>
        </div>
    </div>
</div>
<div data-options="region:'center',border:false"
     style="overflow: fasle;position: relative;">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="save" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-save" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="保存" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="doSave()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="fTc_BomSearchForm" autoSearchFunction="false">
                <input type="hidden" id="pid" in="true" name="filter[pid]">
                原料名称：<input type="text" name="filter[detailName]" value=""
                                like="true" class="easyui-textbox"> 原料规格：<input
                    type="text" name="filter[detailModel]" value="" like="true"
                    class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a>
            </form>
        </div>
    </div>
    <table singleSelect="false" fit="true" id="dg" title="非套材BOM明细列表"
           style="width:auto;" class="easyui-datagrid" url="" toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true"
           data-options="onDblClickRow:onDblClickCell,onEndEdit:onEndEdit,onClickRow:onClickCell,showFooter:true,fitColumns:true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="FTCBOMDETAILNAME" sortable="true" width="12"
                data-options="editor:{type:'textbox',options:{required:true,validType:'length[1,30]'}}">编织结构
            </th>
            <th field="FTCBOMDETAILMODEL" sortable="true" width="12"
                data-options="editor:{type:'combobox',options:{'required':true,editable:true,panelHeight:240,'icons':[],url:'${path}material/list1',valueField:'value',textField:'text'}}
					">原料规格
            </th>
            <th field="FTCBOMDETAILITEMNUMBER" sortable="true" width="12"
                data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">物料代码
            </th>
            <th field="FTCBOMDETAILWEIGHTPERSQUAREMETRE" sortable="true" width="10"
                data-options="editor:{type:'numberbox',options:{required:true,precision:2}}">单位面积质量(g/m²)
            </th>
            <th field="FTCBOMDETAILREED" sortable="true" width="12"
                data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">钢筘规格
            </th>
            <th field="FTCBOMDETAILGUIDENEEDLE" sortable="true" width="12"
                data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">导纱针规格
            </th>
            <th field="FTCBOMDETAILREMARK" sortable="true" width="12"
                data-options="editor:{type:'textbox',options:{validType:'length[1,30]'}}">备注
            </th>
            <!-- <th field="FTCBOMDETAILTOTALWEIGHT" sortable="true" width="10"
                data-options="editor:{type:'numberbox',options:{precision:2}}">总单位面积质量(g/m²)</th> -->
        </tr>
        </thead>
    </table>
</div>

<div id="mainMenu" class="easyui-menu" style="width:140px;">
    <div onclick="appendit()" data-options="iconCls:'icon-add'">增加</div>
    <div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
</div>
<div id="treeMenu" class="easyui-menu" style="width:140px;">
    <div onclick="appenditVersion()" data-options="iconCls:'icon-add'">增加</div>
    <div onclick="editit()" data-options="iconCls:'icon-edit'">修改/查看</div>
    <div onclick="removeit()" data-options="iconCls:'icon-remove'">删除</div>
    <div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
</div>
<div id="treeMenuVersion" class="easyui-menu" style="width:140px;">
    <div onclick="edititVersion()" data-options="iconCls:'icon-edit'">修改</div>
    <div onclick="removeitVersion()" data-options="iconCls:'icon-remove'">删除</div>
    <!-- <div onclick="copyVersion()" data-options="iconCls:'icon-cut'">复制</div> -->
    <div class="menu-sep"></div>
    <div onclick="doAudit()" data-options="iconCls:'icon-ok'">提交审核</div>
    <div onclick="view()" data-options="iconCls:'icon-tip'">查看审核</div>
    <div onclick="forceEdit()"
         data-options="iconCls:'platform-arrow_switch'">工艺变更
    </div>
    <!-- <div class="menu-sep"></div>
   <div onclick="setDefult('ftc',1)">默认</div>
   <div onclick="setDefult('ftc',-1)">非默认</div>
   <div class="menu-sep"></div>
   <div onclick="setEnableState('ftc',1)">启用</div>
   <div onclick="setEnableState('ftc',0)">改版</div>
   <div onclick="setEnableState('ftc',-1)">停用</div> -->
    <div class="menu-sep"></div>
    <div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
</div>
</body>
</html>