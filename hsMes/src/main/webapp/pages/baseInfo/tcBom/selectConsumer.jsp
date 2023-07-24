<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择客户信息</title>
<%@ include file="../../base/jstl.jsp" %>
<script type="text/javascript">
    //添加客户信息
    const findC = path + "bom/tc/findConsumer";
    //双击行事件
    $(document).ready(function () {
        const node = $('#TcBomTree').tree('getSelected');
        JQ.ajaxPost(findC, {
            id: node.id
        }, function (data) {
            $("#dddg").datagrid("loadData", data);
        })
        $("#dddg").datagrid({
            onDblClickRow: function (rowIndex, rowData) {
                _chooseConsumer11(rowIndex, rowData);
            }
        });

        //选择客户双击事件
        function _chooseConsumer11(index, row) {
            $('#productConsumerCode').searchbox('setValue', row.CONSUMERCODE);
            $('#productConsumerName').searchbox('setValue', row.CONSUMERNAME);
            JQ.setValue('#productConsumerId', row.ID);
            Dialog.close(consumerWindow);
        }
    });

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
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
        <table id="dddg" singleSelect="true" title="" class="easyui-datagrid" url="" toolbar="#toolbar1"
               pagination="true" rownumbers="true" fitColumns="true" fit="true">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="CONSUMERCODE" width="15">客户代码</th>
                <th field="CONSUMERNAME" width="15">客户名称</th>
                <th field="CONSUMERGRADE" width="15" formatter="formatterLevel">客户等级</th>
                <th field="CONSUMERCATEGORY" width="15" formatter="formatterType">客户大类</th>
                <th field="CONSUMERCONTACT" width="15">客户联系人</th>
                <th field="CONSUMERADDRESS" width="15">客户地址</th>
                <th field="CONSUMERPHONE" width="15">联系电话</th>
                <th field="CONSUMEREMAIL" width="15">邮件地址</th>
                <th field="CONSUMERMEMO" width="15">备注</th>
                <th field="CONSUMERCODEERP" width="15">客户ERP代码</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
