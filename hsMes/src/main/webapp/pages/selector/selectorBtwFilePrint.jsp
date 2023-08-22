<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
    function filter2() {
        EasyUI.grid.search("selectorBtwFilePrint_dg", "selectorBtwFilePrint_searchForm");
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
        <div id="category_toolbar">
            <div style="border-top:1px solid #DDDDDD">
                <form action="#" id="selectorBtwFilePrint_searchForm" autoSearchFunction="false">
                    <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                       onclick="filter2()">搜索
                    </a>
                </form>
            </div>
        </div>
        <table id="selectorBtwFilePrint_dg" singleSelect="false" title="" class="easyui-datagrid"
               url="${path}btwManager/BtwFilePrint/findBtwFilePrints?btwFileId=${btwFileId}" toolbar="#toolbar1"
               pagination="false" rownumbers="true" fitColumns="true" fit="true">
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
