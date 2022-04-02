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

<div id='cutTcBomMain_form_layout' class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
    <div data-options="region:'north',height:120,title:'导出裁剪图纸套材bom信息',split:true">
        <!--销售订单表单-->
        <form id="cutTcBomMainForm" method="post" ajax="true" autocomplete="off">
            <input type="hidden" name="id" value="${cutTcBomMain.id}"/>
            <table width="100%">
                <tr>
                    <td class="title">BOM代码版本:</td>
                    <!--BOM代码版本-->
                    <td>
                        <input type="text" id="tcProcBomCodeVersion" name="tcProcBomCodeVersion"
                               value="${cutTcBomMain.tcProcBomCodeVersion}" class="easyui-textbox"
                               required="true">
                    </td>

                    <td class="title">叶型名称:</td>
                    <!--叶型名称-->
                    <td>
                        <input type="text" id="bladeTypeName" name="bladeTypeName" value="${cutTcBomMain.bladeTypeName}"
                               class="easyui-textbox" required="true">
                    </td>
                </tr>

                <tr>
                    <td class="title">客户名称:</td>
                    <!--客户名称-->
                    <td>
                        <input type="text"
                               id="customerName" name="customerName"
                               value="${cutTcBomMain.customerName}" class="easyui-searchbox"
                               required="true" data-options="searcher:selectConsumer,icons:[]">

                        <input type="hidden" name="customerCode" id="customerCode" value="${cutTcBomMain.customerCode}">

                    </td>
                </tr>
            </table>
        </form>
    </div>


    <div data-options="region:'center',title:'裁剪图纸套材明细'">
        <div id="toolbar_cutTcBomDetail">
            <c:if test="${empty force }">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-add"
                   onclick="addDetail()">增加</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-remove"
                   onclick="deleteDetail()">删除</a>
            </c:if>
        </div>

        <table id="cutTcBomDetail_dg" singleSelect="true" title="裁剪套材bom明细" width="100%"
               class="easyui-datagrid" toolbar="#toolbar_cutTcBomDetail"
               rownumbers="true" fitColumns="true" fit="true"
               url="${path}bom/cutTcBom/findCutTcBomDetailByMainId?mainId=${cutTcBomMain.id}"
               data-options="onClickRow:cutTcBomDetail_dg_Click">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PARTNAME" sortable="true" width="50" editor="{type:'textbox',options:{precision:0}}">部件名称
                </th>
                <th field="DRAWNAME" sortable="true" width="100" editor="{type:'textbox',options:{precision:0}}">图纸名称
                </th>
                <th field="ORIENTATION" sortable="true" width="20" editor="{type:'textbox',options:{precision:0}}">朝向
                </th>
                <th field="PRODUCTMODEL" sortable="true" width="15" editor="{type:'textbox',options:{precision:0}}">规格
                </th>
                <th field="LENGTH" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">米长
                </th>
                <th field="GRAMWEIGHT" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">克重
                </th>
                <th field="PRODUCTIONRATE" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">制成率
                </th>
                <th field="UNITPRICE" hidden="true" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">单价
                </th>
                <th field="UPPERSIZELIMIT" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">尺寸上限
                </th>
                <th field="LOWERSIZELIMIT" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">尺寸下限
                </th>
                <th field="SIZEPERCENTAGE" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">尺寸百分比
                </th>
                <th field="SIZEABSOLUTEVALUE" sortable="true" width="15"
                    editor="{type:'numberbox',options:{precision:0}}">尺寸绝对值
                </th>
            </tr>

            </thead>
        </table>


    </div>


</div>

