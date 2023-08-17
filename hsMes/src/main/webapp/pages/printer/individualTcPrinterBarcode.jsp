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
    //JS代码
    const getCount = function (newValue, oldValue) {
        var r = EasyUI.grid.getOnlyOneSelected("dailyDetails");
        JQ.ajaxPost(path + "bom/plan/tc/ver/parts/" + +r.FROMSALESORDERDETAILID + "/" + r.ID + "/" + newValue, {}, function (data) {
            console.log(data.partCount);
            $("#count").numberbox('setValue', data.partCount);
        });
    };
</script>
<div>
    <!--打印机信息表单-->
    <form id="doPrintBarcodeForm" method="post" ajax="true"
          action="<%=basePath%>individualprinter/doIndividualPrintBarcode" autocomplete="off">
        <input type="hidden" name="cutPlanId" value="${cutPlanId}"/>
        <input type="hidden" name="cutPlanDailyPlanId" value="${cutPlanDailyPlanId}"/>
        <input type="hidden" name="departmentCode" value="${departmentCode}"/>
        <input type="hidden" name="type" value="${type}"/>
        <table width="100%">
            <tr>
                <td class="title">选择打印机:</td>
                <td><input id="pName" class="easyui-combobox" name="pName"
                           style="width:95%"
                           data-options="valueField:'value',textField:'text',url:'<%=basePath%>printer/getPrinterInfo'"
                           required="true"></td>
            </tr>
            <!-- 选择打印的部件，填写打印的数量 -->
            <tr>
                <td class="title">打印部件:</td>
                <td>
                    <input class="easyui-combobox"
                           style="width:95%;"
                           data-options="valueField:'value',url:'<%=basePath%>planner/cutPlan/findParts2?cutPlanId=${cutDailyPlanDetailId}&&cutDailyPlanId=${cutDailyPlanId}',onSelect: function(rec){
            $('#partId').val(rec.value);
            $('#partName').val(rec.text);
            console.log(  $('#partId').val());
         },
        panelHeight:150,
        onChange:getCount
        " required="true">
                </td>
                <input type="hidden" name="partId" id="partId"/>
                <input type="hidden" name="partName" id="partName"/>
            </tr>
            <tr>
                <input type="hidden" name="btwfileId" id="btwfileId">
                <td class="title">挑选版本:</td>
                <td>
                    <input id="btwfileSelect" class="easyui-combobox" style="width:95%"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>btwFile/queryBtwFilebyCustomerId?customerId=${customerId}&type=${type}',onLoadSuccess:function(data){
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
                    <input name="count" id="count" type="text" class="easyui-numberbox" value=${count==null?"1":count}
                            data-options="precision:0" style="width:265px;" required="true"/>
                </td>
            </tr>
            <tr>
                <td class="title">每条份数:</td>
                <td>
                    <input name="copies" id="copies" type="text" class="easyui-numberbox"
                           value=${copies==null?"4":copies}data-options="precision:0" style="width:265px;"
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