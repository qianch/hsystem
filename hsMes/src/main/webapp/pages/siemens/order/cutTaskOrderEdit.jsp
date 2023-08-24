<!--
作者:高飞
日期:2017-7-31 17:04:13
页面:裁剪派工单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="dlg" class="easyui-dialog" title="编辑派工单" style="width:600px;height:400px;"
     data-options="
            	maximizable:true,
            	resizable:true,
            	closed:true,
                iconCls: 'icon-cut',
                buttons: '#dlg-buttons',
                modal:true">
    <div class="easyui-layout" style="width:100%;height:100%;">
        <div data-options="region:'north',border:'none'" style="height:181px">
            <form style="margin: 0;" id="cutTaskForm" autoSearchFunction="null" autoSearchFunction="false">
                <input type="hidden" id="id" name="id">
                <input type="hidden" id="ctId" name="ctId">
                <table width="100%">
                    <tr>
                        <td class="title">派工单号:</td>
                        <td>
                            <input type="text" id="ctoCode" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                        <td class="title">任务单号:</td>
                        <td>
                            <input type="text" id="taskCode" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                    </tr>
                    <tr>
                        <td class="title">订单号:</td>
                        <td>
                            <input type="text" id="orderCode" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                        <td class="title">批次号:</td>
                        <td>
                            <input type="text" id="batchCode" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                    </tr>
                    <tr>
                        <td class="title">客户简称:</td>
                        <td>
                            <input type="text" id="consumerSimpleName" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                        <td class="title">客户大类:</td>
                        <td>
                            <input type="text" id="taskConsumerCategoryX" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                    </tr>
                    <tr>
                        <td class="title">部件名称:</td>
                        <td>
                            <input type="text" id="partName" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                        <td class="title">交货日期:</td>
                        <td>
                            <input type="text" id="deliveryDate" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                    </tr>
                    <tr>
                        <td class="title">机长:</td>
                        <td>
                            <input type="text" id="ctoGroupLeader" name="ctoGroupLeader" class="easyui-combobox"
                                   data-options="icons:[],filter:comboFilter,data:groups,textField:'GROUPLEADER',valueField:'GROUPLEADER',onHidePanel:validCode,maxPanelHeight:150,panelHeight:'auto'"
                                   editable="true" required="true">
                        </td>
                        <td class="title">班组:</td>
                        <td>
                            <input type="text" id="ctoGroupName" name="ctoGroupName" class="easyui-textbox"
                                   data-options="icons:[]" editable="false" required="true">
                        </td>
                    </tr>
                    <tr>
                        <td class="title">总套数:</td>
                        <td>
                            <input type="text" id="suitCount" class="easyui-textbox" data-options="icons:[]"
                                   editable="false" required="true">
                        </td>
                        <td class="title">派工套数:</td>
                        <td>
                            <input type="text" id="assignCount" name="assignSuitCount" class="easyui-numberspinner"
                                   precision="0" data-options="icons:[],min:1" editable="true" required="true">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center',border:'none'">
            <table id="drawingsDg" class="easyui-datagrid"
                   width="100%"
                   pagination="false"
                   rownumbers="true"
                   fitColumns="true"
                   fit="true">
                <thead>
                <tr>
                    <th field="partName" width="100">部件名称</th>
                    <th field="fragmentDrawingNo" width="100">图纸号</th>
                    <th field="fragmentDrawingVer" width="100">图值版本</th>
                    <th field="farbicRollCount" width="100">胚布卷数</th>
                    <th field="X" width="100">质检确认</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" iconCls="icon-save" class="easyui-linkbutton" onclick="saveCutTaskOrder()">保存</a>
    <a href="javascript:void(0)" iconCls="icon-cancel" class="easyui-linkbutton"
       onclick="javascript:$('#dlg').dialog('close')">关闭</a>
</div>
