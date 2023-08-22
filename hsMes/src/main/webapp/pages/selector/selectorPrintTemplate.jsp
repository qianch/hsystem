<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--<%@ include file="../base/meta.jsp" %>--%>
<script>
    function filterSelectorPrintTemplate_dg() {
        const opts = $("#SelectorPrintTemplate_dg").datagrid("options");
        opts.url = path + "printTemplate/printTemplateList";
        EasyUI.grid.search("SelectorPrintTemplate_dg", "PrintTemplate_searchForm");
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
        <div id="category_toolbar">
            <div style="border-top:1px solid #DDDDDD">
                <form action="#" id="PrintTemplate_searchForm" autoSearchFunction="false">
                    <input type="hidden" id="type" name="filter[type]" value="${type}"/>
                    打印模版显示名称:<input type="text" name="filter[printAttributeName]" like="true"
                                            class="easyui-textbox">
                    打印模版属性:<input type="text" name="filter[printAttribute]" like="true" class="easyui-textbox">
                    <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                       onclick="filterSelectorPrintTemplate_dg()">搜索
                    </a>
                </form>
            </div>
        </div>
        <table id="SelectorPrintTemplate_dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar1"
               pagination="true" rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PRINTATTRIBUTE" width="100">打印属性</th>
                <th field="PRINTATTRIBUTENAME" width="100">打印属性名称</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
