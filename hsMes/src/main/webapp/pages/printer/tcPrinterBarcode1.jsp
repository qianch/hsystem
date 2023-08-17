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
<script type="text/javascript">
    const getCount = function (newValue, oldValue) {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        JQ.ajaxPost(path + "bom/plan/tc/ver/parts/" + +r.FROMSALESORDERDETAILID + "/" + r.ID + "/" + newValue, {}, function (data) {
            console.log(data.partCount);
            $("#count").numberbox('setValue', data.partCount);
        });
    };
</script>
<div>
    <!--打印机信息表单-->
    <form id="doPrintBarcodeForm" method="post" ajax="true"
          action="<%=basePath%>printer/doPrintBarcode" autocomplete="off">
        <input type="hidden" name="cutPlanId" value="${cutPlanId}"/>
        <input type="hidden" name="departmentName" value="${departmentName}"/>
        <input type="hidden" name="trugPlanId" value="${trugPlanId}"/>
        <table width="100%">
            <tr>
                <td class="title">选择打印机:</td>
                <td><input id="pName" class="easyui-combobox" name="pName"
                           style="width:95%"
                           data-options="valueField:'value',textField:'text',url:'<%=basePath%>printer/getPrinterInfo'"
                           required="true"></td>
            </tr>
            <tr>
                <td class="title">打印部件:</td>
                <td><input class="easyui-combobox"
                           style="width:95%;"
                           data-options="valueField:'value',url:'<%=basePath%>planner/cutPlan/findParts3?orderId=${fromSalesOrderDetailId}&&planId=${producePlanDetailId}',onSelect: function(rec){
    $('#partId').val(rec.value);
            $('#partName').val(rec.text);
            console.log(  $('#partId').val());
                    },
                    panelHeight:150,
                    onChange:getCount
                    " required="true">
                    <input type="hidden" name="partId" id="partId"/>
                    <input type="hidden" name="partName" id="partName"/>
                </td>
            </tr>
            <tr>
                <td class="title">打印数量:</td>
                <td>
                    <!-- <input name="count" id="count" class="easyui-numberspinner" style="width:265px;"
                        required="required" data-options="min:1,max:100,editable:false,value:1">   -->
                    <input name="count" id="count" type="text" class="easyui-numberbox" value=${count==null?"1":count}
                            data-options="precision:0" style="width:265px;" required="true"/>
                </td>
            </tr>
            <tr>
                <td class="title">下载地址:</td>
                <td id="downLoad"></td>
            </tr>
        </table>
    </form>
</div>