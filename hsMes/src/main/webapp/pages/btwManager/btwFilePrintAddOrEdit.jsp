<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../base/jstl.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
    form {
        margin: 0px;
    }

    .datagrid-row-selected {
        background: #d4d8f7 !important;
        color: #000000;
    }
</style>
<script type="text/javascript">

</script>

<div id='salesorder_form_layout' class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
    <div data-options="region:'north',height:160,title:'打印条码信息',split:true">
        <!--销售订单表单-->
        <form id="btwFilePrintForm" method="post" ajax="true" autocomplete="off">

            <input type="hidden" id="id" name="id" value="${btwFile.id}"/>
            <table width="100%">
                <tr>
                    <td class="title">客户名称:</td>
                    <!--客户名称-->
                    <td>
                        <input type="hidden" name="consumerId" id="consumerId" value="${btwFile.consumerId}">
                        <input type="hidden" name="consumerCode" id="consumerCode" value="${btwFile.consumerId}">
                        <input type="text" readonly="readonly" id="consumerName" name="consumerName" value="${btwFile.consumerName}">
                    </td>


                </tr>
                <tr>
                    <td class="title">客户条码前缀:</td>
                    <td>
                        <input type="text" id="consumerBarCodePrefix" name="consumerBarCodePrefix"
                               value="${btwFile.consumerBarCodePrefix}"  readonly="readonly"></td>
                    </td>
                </tr>
                <tr>
                    <td class="title">客户条码记录:</td>
                    <td>
                        <input type="number" id="consumerBarCodeRecord" name="consumerBarCodeRecord"
                               value="${btwFile.consumerBarCodeRecord}" value="0"
                                readonly="readonly"></td>
                    </td>
                    <td class="title">客户条码位数:</td>
                    <td>
                        <input type="number" id="consumerBarCodeDigit" name="consumerBarCodeDigit" min="1" value="6"
                               readonly="readonly"></td>
                    </td>
                </tr>
                <tr>
                    <td class="title">供销商条码前缀:</td>
                    <td>
                        <input type="text" id="agentBarCodePrefix" name="agentBarCodePrefix"
                               value="${btwFile.agentBarCodePrefix}" readonly="readonly"></td>
                    </td>
                </tr>

                <tr>
                    <td class="title">供销商条码记录:</td>
                    <td>
                        <input type="number" id="agentBarCodeRecord" name="agentBarCodeRecord"
                               value="${btwFile.agentBarCodeRecord}" value="0" readonly="readonly">
                    </td>
                    </td>
                    <td class="title">供销商条码位数:</td>
                    <td>
                        <input type="number" id="agentBarCodeDigit" min="1" name="agentBarCodeDigit" value="6"
                               readonly="readonly"></td>
                    </td>
                </tr>
            </table>
        </form>
    </div>


    <div data-options="region:'center',title:'打印明细'">
        <div id="toolbar_product">
            <c:if test="${empty force }">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-add"
                   onclick="selectPrintTemplate()">增加</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-remove"
                   onclick="removePrintTemplate()">删除</a>
            </c:if>
        </div>


        <table id="dg_BtwFilePrint" singleSelect="true" title="打印信息"
               class="easyui-datagrid"
               rownumbers="true" fitColumns="true" fit="true"
               url="${path}btwManager/BtwFilePrint/findBtwFilePrints?btwFileId=${btwFile.id}"
               data-options="onClickRow:onClickBtwFilePrintAddOrEditRow">

            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PRINTATTRIBUTE">打印属性</th>
                <th field="PRINTATTRIBUTENAME">打印属性名称</th>
            </tr>
            </thead>
        </table>


    </div>


</div>

