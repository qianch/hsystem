<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/jstl.jsp" %>
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

<div id='finishedProductPrintRecord_form_layout' class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
    <div data-options="region:'north',height:160,title:'产品打印信息',split:true">
        <!--销售订单表单-->
        <form id="finishedProductPrintRecordForm" method="post" ajax="true" autocomplete="off">

            <input type="hidden" id="id" name="id" value="${finishedProduct.id}"/>
            <table width="100%">


                <tr>
                    <td class="title">厂内名称:</td>
                    <!--出库调拨单号-->
                    <td>
                        <input type="text" id="factoryProductName" name="factoryProductName"
                               value="${finishedProduct.factoryProductName}" class="easyui-textbox"
                               readonly="readonly" required="true">
                    </td>

                    <td class="title">产品名称:</td>
                    <!--终点仓库-->
                    <td>
                        <input type="text" id="consumerProductName" name="consumerProductName"
                               value="${finishedProduct.consumerProductName}" class="easyui-textbox"
                               readonly="readonly" required="true">
                    </td>
                </tr>
                <tr>
                    <td class="title">物料编码:</td>
                    <!--物料编码-->
                    <td>
                        <input type="text" id="materielCode" name="factoryProductName"
                               value="${finishedProduct.materielCode}" class="easyui-textbox"
                               readonly="readonly" required="true">
                    </td>


                </tr>

            </table>
        </form>
    </div>


    <div data-options="region:'center',title:'打印明细'">
        <div id="toolbar_product">
            <c:if test="${empty force }">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-add"
                   onclick="createFinishedProductPrintRecord()">增加</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-remove"
                   onclick="removeFinishedProductPrintRecord()">删除</a>
            </c:if>
        </div>

        <table id="dg_finishedProductPrintRecord" singleSelect="true" title="打印信息"
               class="easyui-datagrid"
               rownumbers="true" fitColumns="true" fit="true"
               url="${path}finishProduct/finishedProductPrintRecord/findFinishedProductPrintRecords?productId=${finishedProduct.id}"
               data-options="onClickRow:onClickFinishedProductPrintRecordRow">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PRINTATTRIBUTE" width="100">打印属性</th>
                <th field="PRINTATTRIBUTENAME" width="100">打印属性名称</th>
                <th field="PRINTATTRIBUTECONTENT" width="150" editor="{type:'textbox',options:{}}">打印内容</th>
            </tr>
            </thead>
        </table>


    </div>


</div>

