<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script>
    function filter1() {
        EasyUI.grid.search("weight_dg", "carrier_searchForm");
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
        <div id="category_toolbar">
            <div style="border-top:1px solid #DDDDDD">
                <form action="#" id="carrier_searchForm" autoSearchFunction="false">
                    <label class="panel-title">衬管编码：</label>
                    <input type="text" id="carrierCode" name="filter[carrierCode]" like="true" class="easyui-textbox">
                    <label class="panel-title">衬管名称：</label>
                    <input type="text" name="filter[carrierName]" like="true" class="easyui-textbox">
                    <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                       onclick="filter1()">
                        搜索
                    </a>
                </form>
            </div>
        </div>
        <table id="weight_dg" singleSelect="true" title="" class="easyui-datagrid" url="${path}weightCarrier/list"
               toolbar="#category_toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="CARRIERCODE" sortable="true" width="15">衬管编码</th>
                <th field="CARRIERNAME" sortable="true" width="15">衬管名称</th>
                <th field="CARRIERWEIGHT" hidden="true" sortable="true" width="15">重量</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
