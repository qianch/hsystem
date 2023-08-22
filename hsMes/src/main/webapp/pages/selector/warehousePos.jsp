<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择库位信息</title>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    //查询
    function _common_warehousePos_filter() {
        EasyUI.grid.search("_common_warehousePos_dg", "_common_warehousePos_dg_form");
    }

    $(document).ready(function () {
        //设置数据表格的DetailFormatter内容
        $('#_common_warehousePos_dg').datagrid({
            onDblClickRow: function (index, row) {
                if (typeof _common_warehousePos_dbClickRow === "function") {
                    _common_warehousePos_dbClickRow(index, row);
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供_common_warehousePos_dbClickRow方法，参数为index,row");
                    }
                }
            },
            onLoadSuccess: function (data) {
                if (typeof _common_warehousePos_onLoadSuccess === "function") {
                    _common_warehousePos_onLoadSuccess(data);
                } else {
                    if (window.console) {
                        console.log("未定义用户选择界面加载完成的方法：_common_warehousePos_onLoadSuccess(data)");
                    }
                }
            }
        });
    });
</script>
<div id="_common_warehousePos_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <form action="#" id="_common_warehousePos_dg_form" autoSearchFunction="false">
            <label class="panel-title">搜索：</label>
            库位代码:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
            库位名称:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small"
               iconcls="icon-search" onclick="_common_warehousePos_filter()"> 搜索 </a>
        </form>
    </div>
</div>
<table id="_common_warehousePos_dg" singleSelect="${empty singleSelect?'true':singleSelect }" class="easyui-datagrid"
       url="${path}warehosePos/list" toolbar="#_common_warehousePos_toolbar" pagination="true" rownumbers="true"
       fitColumns="true" fit="true">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="WAREHOUSEPOSCODE" width="15">库位代码</th>
        <th field="WAREHOUSEPOSNAME" width="15">库位名称</th>
        <th field="WAREHOUSEPOSMEMO" width="15">备注</th>
        <th field="WAREHOUSENAME" width="15">所属仓库</th>
        <th field="WAREHOUSECODE" width="15">所属仓库编码</th>
    </tr>
    </thead>
</table>
