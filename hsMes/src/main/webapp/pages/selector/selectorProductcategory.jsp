<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
    function filter2() {
        EasyUI.grid.search("category_dg", "product_category_searchForm");
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
        <div id="category_toolbar">
            <div style="border-top:1px solid #DDDDDD">
                <form action="#" id="product_category_searchForm" autoSearchFunction="false">
                    <label class="panel-title">类别代码：</label>
                    <input type="text" name="filter[categoryCode]" like="true" class="easyui-textbox">
                    <label class="panel-title">类别名称：</label>
                    <input type="text" name="filter[categoryName]" like="true" class="easyui-textbox">
                    <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                       onclick="filter2()">搜索
                    </a>
                </form>
            </div>
        </div>
        <table id="category_dg" singleSelect="true" title="" class="easyui-datagrid" url="${path}product/category/list"
               toolbar="#category_toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="CATEGORYCODE" sortable="true" width="15">类别编号</th>
                <th field="CATEGORYNAME" sortable="true" width="15">类别名称</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
