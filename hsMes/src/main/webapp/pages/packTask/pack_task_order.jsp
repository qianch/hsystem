<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script type="text/javascript">
    const packTaskAddOrEdit = path + "packtask";
    const orderPackTask = path + "packtask/order/";
    const productPackTask = path + "packtask/product/";
    let _productId = null;
    let _sodId = null;

    //var producePackTask=path+"packtask/produce/";
    function loadTask(sodId, productId) {
        _productId = productId;
        _sodId = sodId;
        JQ.ajaxGet(orderPackTask + sodId, function (data) {
            if (data.length === 0) {
                loadProductTask();
            } else {
                showTask(data);
            }
        });
    }

    function loadProductTask() {
        JQ.ajaxGet(productPackTask + _productId, function (data) {
            if (data.length === 0) {
                Tip.warn("找不到该产品的包装方式");
            } else {
                showTask(data);
            }
        });
    }

    function showTask(data) {
        console.log(123)
        $("#pack_task_window").dialog("open");
        $("#pack_task_dg").datagrid({data: data});
    }

    function pack_task_delete() {
        EasyUI.grid.deleteSelected("pack_task_dg");
    }

    function savePackTask() {
        const rows = $("#pack_task_dg").datagrid("getRows");
        const data = [];
        for (let i = 0; i < rows.length; i++) {
            if (!validPackTask(i)) {
                Tip.warn("请输入第" + (i + 1) + "行的总托数");
                return;
            }
            if (parseInt(rows[i].TOTALCOUNT) < 1) {
                Tip.warn("第" + (i + 1) + "行的总托数不可小于1");
                return;
            }
            data.push({
                sodId: _sodId,
                vid: rows[i].VID,
                version: rows[i].VERSION,
                code: rows[i].CODE,
                totalCount: rows[i].TOTALCOUNT,
                rollsPerTray: rows[i].ROLLSPERTRAY,
                leftCount: rows[i].TOTALCOUNT,
                memo: rows[i].MEMO
            });
        }
        Dialog.confirm(function () {
            $.ajax({
                url: packTaskAddOrEdit,
                type: "post",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (data) {
                    if (data.error) {
                        return;
                    }
                    $('#pack_task_window').dialog('close');
                    Tip.warn("保存成功");
                }
            });
        }, "确认保存？");
    }

    function validPackTask(i) {
        $("#pack_task_dg").datagrid("beginEdit", i);
        const valid = $("#pack_task_dg").datagrid("validateRow", i);
        if (valid)
            $("#pack_task_dg").datagrid("endEdit", i);
        return valid;
    }
</script>
<div id="pack_task_window" class="easyui-dialog" title="包装任务" style="width:750px;height:400px;"
     data-options="
        closed:true,
        resizable:true,
        modal:true,
        buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        savePackTask();
                    }
                },{
                    text:'取消',
                    handler:function(){
                        $('#pack_task_window').dialog('close');
                    }
                }]
        ">
    <div id="pack_task_dg_toolbar">
        <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="loadProductTask()"
           iconCls="icon-edit">重置</a>
        <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="pack_task_delete()"
           iconCls="icon-remove">删除</a>
    </div>
    <table id="pack_task_dg" singleSelect="false" fit="true" fitColumns="true"
           singleSelect="true" pagination="false" width="100%"
           data-options="{
               onClickRow:function(i,r){
                   $(this).datagrid('beginEdit',i);
               }
           }">
        <thead>
        <tr>
            <th field="VID" checkbox="true"></th>
            <th field="CODE" width="250">包装代码</th>
            <th field="VERSION" width="50">版本</th>
            <th field="TOTALCOUNT" width="80"
                editor="{type:'numberbox',options:{precision:0,max:999999,required:true}}">订单总托数
            </th>
            <th field="MEMO" width="80" editor="{type:'textbox',options:{validType:'length[0,100]'}}">备注</th>
        </tr>
        </thead>
    </table>
</div>
