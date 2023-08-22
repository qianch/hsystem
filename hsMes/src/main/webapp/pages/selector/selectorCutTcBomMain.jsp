<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择库位信息</title>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    //查询
    function _common_cutTcBomMain_filter() {
        EasyUI.grid.search("_common_cutTcBomMain_dg", "_common_cutTcBomMain_dg_form");
    }

    $(document).ready(function () {
        //设置数据表格的DetailFormatter内容
        $('#_common_cutTcBomMain_dg').datagrid({
            onDblClickRow: function (index, row) {
                if (typeof _common_cutTcBomMain_dbClickRow === "function") {
                    _common_cutTcBomMain_dbClickRow(index, row);
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供_common_cutTcBomMain_dbClickRow方法，参数为index,row");
                    }
                }
            },
            onLoadSuccess: function (data) {
                if (typeof _common_cutTcBomMain_onLoadSuccess === "function") {
                    _common_cutTcBomMain_onLoadSuccess(data);
                } else {
                    if (window.console) {
                        console.log("未定义用户选择界面加载完成的方法：_common_cutTcBomMain_onLoadSuccess(data)");
                    }
                }
            }
        });
    });
</script>
<div id="_common_cutTcBomMain_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <form action="#" id="_common_cutTcBomMain_dg_form" autoSearchFunction="false">
            BOM代码版本:<input type="text" name="filter[tcProcBomCodeVersion]" like="true" class="easyui-textbox">
            叶型名称：<input type="text" name="filter[bladeTypeName]" like="true" class="easyui-textbox">
            创建时间:<input type="text" name="filter[start]" class="easyui-datetimebox">
            至:<input type="text" name="filter[end]" class="easyui-datetimebox">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
               onclick="filter()">搜索
            </a>
        </form>
    </div>
</div>
<table id="_common_cutTcBomMain_dg" singleSelect="${empty singleSelect?'true':singleSelect }" class="easyui-datagrid"
       url="${path}bom/cutTcBom/list" toolbar="#_common_cutTcBomMain_toolbar" pagination="true" rownumbers="true"
       fitColumns="true" fit="true">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="TCPROCBOMCODEVERSION" sortable="true" width="80">BOM代码版本</th>
        <th field="CUSTOMERNAME" sortable="true" width="80">客户名称</th>
        <th field="BLADETYPENAME" sortable="true" width="80">叶型名称</th>
        <th field="CREATETIME" sortable="true" width="100">创建时间</th>
        <th field="STATE" width="15"
            data-options="formatter:function(value,row,index){return dictFormatter('State',value)}">状态
        </th>
    </tr>
    </thead>
</table>
