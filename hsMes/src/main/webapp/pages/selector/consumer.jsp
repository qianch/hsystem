<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择客户信息</title>
<%@ include file="../base/jstl.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
    //查询
    function _common_consumer_filter() {
        EasyUI.grid.search("_common_consumer_dg", "_common_consumer_dg_form");
    }

    //客户等级
    function formatterLevel(value, row) {
        if (value === "1") {
            return "A";
        }
        if (value === "2") {
            return "B";
        }
        if (value === "3") {
            return "C";
        }
    }

    //客户大类
    function formatterType(value, row) {
        if (value === "1") {
            return "国内";
        }
        if (value === "2") {
            return "国外";
        }
    }

    $(document).ready(function () {
        //设置数据表格的DetailFormatter内容
        $('#_common_consumer_dg')
            .datagrid({
                remoteFilter: true,
                onDblClickRow: function (index, row) {
                    if (typeof _common_consumer_dbClickRow === "function") {
                        _common_consumer_dbClickRow(index, row);
                    } else {
                        if (window.console) {
                            console.log("没有为用户选择界面提供_common_consumer_dbClickRow方法，参数为index,row");
                        }
                    }
                },
                onLoadSuccess: function (data) {
                    $(this).datagrid("enableFilter");
                    $(".datagrid-filter[name='CONSUMERCATEGORY']").parent().remove();
                    if (typeof _common_consumer_onLoadSuccess === "function") {
                        _common_consumer_onLoadSuccess(data);
                    } else {
                        if (window.console) {
                            console.log("未定义用户选择界面加载完成的方法：_common_consumer_onLoadSuccess(data)");
                        }
                    }
                }
            });
    });
</script>
<div id="_common_consumer_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <!-- <form action="#" id="_common_consumer_dg_form" autoSearch="true" autoSearchFunction="_common_consumer_filter">
            客户代码：<input type="text"
                id="code" name="filter[code]" like="true" class="easyui-textbox">
            客户名称：<input type="text" name="filter[name]" like="true"
                class="easyui-textbox"> </br>
            客户大类：<input
                type="text" name="filter[type]" like="true" class="easyui-combobox"
                panelHeight="auto" editable="false"
                data-options="data: [
                            {value:'1',text:'国内'},
                            {value:'2',text:'国外'}
                        ] ,icons: [{
                    iconCls:'icon-clear',
                    handler: function(e){
                        $(e.data.target).combobox('clear');
                    }
                }]">
            客户等级：<input type="text" name="filter[level]" like="true"
                class="easyui-combobox" panelHeight="auto" editable="false"
                data-options="data: [
                            {value:'1',text:'A'},
                            {value:'2',text:'B'},
                            {value:'3',text:'C'}
                        ],icons: [{
                    iconCls:'icon-clear',
                    handler: function(e){
                        $(e.data.target).combobox('clear');
                    }
                }]">
            备注：<input type="text" name="filter[memo]" like="true"
        class="easyui-textbox">
            <a href="javascript:void(0)"
                class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                onclick="_common_consumer_filter()"> 搜索 </a>
        </form> -->
    </div>
</div>
<table id="_common_consumer_dg"
       singleSelect="${empty singleSelect?'true':singleSelect }" style="width:100%;" class="easyui-datagrid"
       url="<%=basePath %>consumer/list"
       toolbar="#_common_consumer_toolbar" pagination="true" rownumbers="true"
       fitColumns="false" fit="true">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="CONSUMERCODE" width="120">客户代码</th>
        <th field="CONSUMERNAME" width="400">客户名称</th>
        <th field="CONSUMERGRADE" width="80" formatter="formatterLevel">客户等级</th>
        <th field="CONSUMERCATEGORY" width="80" formatter="formatterType">客户大类</th>
        <!-- <th field="CONSUMERCONTACT" width="15">客户联系人</th> -->
        <!-- <th field="CONSUMERADDRESS" width="15">客户地址</th> -->
        <!-- <th field="CONSUMERPHONE" width="15">联系电话</th> -->
        <!-- <th field="CONSUMEREMAIL" width="15">邮件地址</th> -->
        <th field="CONSUMERMEMO" width="140">备注</th>
        <!-- <th field="CONSUMERCODEERP" width="15">客户ERP代码</th> -->
    </tr>
    </thead>
</table>
