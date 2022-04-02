<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">

var packTaskAddOrEdit=path+"packtask";
var producePackTask=path+"packtask/produce/";
var orderPackTask=path+"packtask/order/";

var _sodId=null;
var _ppdId=null;

//var producePackTask=path+"packtask/produce/";

function loadTask(ppdId,sodId){
    _sodId=sodId;
    _ppdId=ppdId;
    
    JQ.ajaxGet(producePackTask+ppdId,function(data){
        if(data.length==0){
            loadOrderTask();
        }else{
            showTask(data);
        }
    });
}

function loadOrderTask(){
    JQ.ajaxGet(orderPackTask+_sodId,function(data){
        if(data.length==0){
            Tip.warn("找不到该产品的包装方式");
        }else{
        	for(var i=0;i<data.length;i++){
        		data[i].PRODUCETOTALCOUNT=data[i].LEFTCOUNT;
        	}
            showTask(data);
        }
    });
}

function showTask(data,change){
    $("#pack_task_window").dialog("open");
    $("#pack_task_dg").datagrid({data:data});
}

function pack_task_delete(){    
    EasyUI.grid.deleteSelected("pack_task_dg");
}

function savePackTask(){
    var rows=$("#pack_task_dg").datagrid("getRows");
    var data=[];
    
    for(var i=0;i<rows.length;i++){
        if(!doValid(i)){
            Tip.warn("请输入第"+(i+1)+"行的总托数");
            return;
        }
        
        data.push({
            ppdId:_ppdId,
            ptId:rows[i].ID,
            produceTotalCount:rows[i].PRODUCETOTALCOUNT,
            printedTrayBarcodeCount:0,
            memo:rows[i].MEMO
        });
    }
    Dialog.confirm(function(){
        $.ajax({
            url:packTaskAddOrEdit,
            type:"post",
            dataType:"json",
            contentType:"application/json",
            data:JSON.stringify(data),
            success:function(){
            	if(data.error){
                    return;
                }
                $('#pack_task_window').dialog('close');
                Tip.warn("保存成功");
            }
        });
    },"确认保存？");
}

function doValid(i){
    $("#pack_task_dg").datagrid("beginEdit",i);
    var valid=$("#pack_task_dg").datagrid("validateRow",i);
    if(valid)
           $("#pack_task_dg").datagrid("endEdit",i);
    return valid;
}



</script>
<div id="pack_task_window" class="easyui-dialog" title="包装任务" style="width:750px;height:300px;" 
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
        <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="loadOrderTask()" iconCls="icon-edit">重置</a>
        <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="pack_task_delete()" iconCls="icon-remove">删除</a>
    </div>
    <table id="pack_task_dg" singleSelect="false" fit="true" fitColumns="true" 
           singleSelect="true" pagination="false" width="100%"
           data-options="{
               onClickRow:function(i,r){
                   $(this).datagrid('beginEdit',i);
               }
           }"
           >
        <thead>
            <tr>
                <th field="VID" checkbox="true"></th>
                <th field="CODE" width="250">包装代码</th>
                <th field="VERSION" width="30">版本</th>
                <th field="MEMO" width="60" editor="{type:'textbox',options:{validType:'length[0,100]'}}">备注</th>
                <th field="TOTALCOUNT" width="60">订单总托数</th>
                <th field="LEFTCOUNT" width="60" >可排产托数</th>
                <th field="PRODUCETOTALCOUNT" width="60" editor="{type:'numberbox',options:{precision:0,min:1,max:9999,required:true}}">计划总托数</th>
            </tr>
        </thead>
    </table>
</div>