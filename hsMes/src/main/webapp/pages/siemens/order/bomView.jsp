<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script type="text/javascript">
    function viewSuitBom(ctId) {
        JQ.ajaxGet(path + "siemens/cutTask/view/suit?ctId=" + ctId, function (data) {
            $("#suitDlg").dialog("open");
            $("#viewSuitDg").datagrid("loadData", data);
        });
    }

    function viewDrawingsBom(ctId) {
        JQ.ajaxGet(path + "siemens/cutTask/view/drawings?ctId=" + ctId, function (data) {
            $("#drawingsDlg").dialog("open");
            $("#viewDrawingsDg").datagrid("loadData", data);
        });
    }
</script>
<div id="suitDlg" class="easyui-dialog" title="组套BOM" style="width:600px;height:100%;overflow: hidden;"
     data-options="
            	maximizable:true,
            	border:'none',
            	resizable:true,
            	closed:true,
            	maximized:true,
                iconCls: 'platform-icon68',
                modal:true">
    <table id="viewSuitDg" singleSelect="false" class="easyui-datagrid" pagination="false" rownumbers="true"
           fitColumns="false" remoteSort="false" fit="true">
        <thead>
        <tr>
            <th field="id" checkbox="true"></th>
            <th field="fragmentCode" width="150" sortable="true">小部件编码</th>
            <th field="fragmentName" width="150" sortable="true">名称</th>
            <th field="suitCount" width="150" sortable="true">数量</th>
            <th field="suitSort" width="150" sortable="true">顺序号</th>
        </tr>
        </thead>
    </table>
</div>
<div id="drawingsDlg" class="easyui-dialog" title="图纸BOM" style="width:600px;height:100%;overflow: hidden;"
     data-options="
            	maximizable:true,
            	border:'none',
            	resizable:true,
            	closed:true,
            	maximized:true,
                iconCls: 'platform-icon78',
                modal:true">
    <table id="viewDrawingsDg" idField="id" singleSelect="false" class="easyui-datagrid" pagination="false"
           rownumbers="true" fitColumns="true" remoteSort="false" fit="true">
        <thead>
        <tr>
            <th field="id" checkbox="true"></th>
            <th field="fragmentCode" width="23" sortable="true">小部件编码</th>
            <th field="fragmentName" width="23" sortable="true">名称</th>
            <th field="fragmentWeight" width="15" sortable="true">重量</th>
            <th field="fragmentLength" width="15" sortable="true">长度(M)</th>
            <th field="fragmentWidth" width="15" sortable="true">宽度(MM)</th>
            <th field="farbicModel" width="15" sortable="true">胚布规格</th>
            <th field="fragmentCountPerDrawings" width="10" sortable="true">数量</th>
            <th field="fragmentDrawingNo" width="15" sortable="true">图号</th>
            <th field="fragmentDrawingVer" width="12" sortable="true">图纸版本</th>
            <%-- <th field="partId" formatter="partNameFormatter" width="20" sortable="true" data-options="editor:{type:'combobox',options:{url:'${path }siemens/bom/parts',onBeforeLoad:onDrawingsComboboxBeforeLoad,filter:comboFilter,onHidePanel:validCode,onSelect:onDrawingsComboSelect,required:true,editable:true,valueField:'ID',textField:'NAME',panelHeight:'auto'}}">部件名称</th> --%>
            <th field="suitCountPerDrawings" width="15" sortable="true">图内套数</th>
            <th field="printSort" width="15" sortable="true">出图顺序</th>
            <th field="fragmentMemo" width="20" sortable="true">备注</th>
        </tr>
        </thead>
    </table>
</div>