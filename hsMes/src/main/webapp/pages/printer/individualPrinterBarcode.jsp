<!--
作者:徐波
日期:2016-11-14 15:40:51
页面:打印机信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--打印机信息表单-->
    <form id="doPrintBarcodeForm" method="post" ajax="true"
          action="<%=basePath%>individualprinter/doIndividualPrintBarcode" autocomplete="off">
        <input type="hidden" name="weavePlanId" value="${weavePlanId}"/>
        <input type="hidden" name="cutPlanId" value="${cutPlanId}"/>
        <input type="hidden" name="departmentCode" value="${departmentCode}"/>
        <input type="hidden" name="devCode" value="${devCode}"/>
        <input type="hidden" name="type" value="${type}"/>
        <table width="100%">
            <tr>
                <td class="title">选择打印机:</td>
                <td>
                    <input id="pName" class="easyui-combobox" name="pName" style="width:95%"
                           data-options="valueField:'value',textField:'text',url:'<%=basePath %>printer/getPrinterInfo'"
                           required="true">
                </td>
            </tr>
            <tr>
                <input type="hidden" name="partId" id="partId">
                <input type="hidden" name="fabricId" id="fabricId">
                <input type="hidden" name="partName" id="partName">
                <td class="title">选择部件:</td>
                <td>
                    <input id="partNameSelect" class="easyui-combobox" style="width:95%"
                           data-options="valueField:'value',textField:'text',url:'<%=basePath %>printer/getPrinterPart?weaveId=${weavePlanId}',onLoadSuccess:function(data){
					    if(data.length==1){
					    	$('#partNameSelect').combobox('select',data[0].value);
					    }
					    }, onSelect: function(rec){
					            $('#partId').val(rec.value);
					             $('#partName').val(rec.text);
					        }" required="true">
                </td>
            </tr>
            <tr>
                <input type="hidden" name="btwfileId" id="btwfileId">
                <td class="title">挑选版本:</td>
                <td>
                    <input id="btwfileSelect" class="easyui-combobox" style="width:95%"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>btwFile/queryBtwFile?weavePlanId=${weavePlanId}&type=${type}',onLoadSuccess:function(data){
					      if(data.length>0){
					    	$('#btwfileSelect').combobox('select',data[0].value);
					       }
					    }, onSelect: function(rec){
					             $('#btwfileId').val(rec.v);
					        }" required="true">
                </td>
            </tr>
            <tr>
                <td class="title">打印数量:</td>
                <td>
                    <input name="count" id="count" type="text" class="easyui-numberbox"
                           value=${count==null?"1":count}  data-options="min:1,precision:0" style="width:265px;"
                           required="true"></input>
                </td>
            </tr>
            <tr>
                <td class="title">下载地址:</td>
                <td id="downLoad"></td>
            </tr>
        </table>
    </form>
</div>