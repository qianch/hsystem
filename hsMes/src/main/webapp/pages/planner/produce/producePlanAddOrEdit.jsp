<!--
作者:高飞
日期:2016-11-28 21:25:48
页面:生产任务单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/jstl.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<script type="text/javascript">
    const details = ${empty details?"[]":details};
    let indexData = {};
    $(function () {
        $("#produceProducts").datagrid({
            data: details,
            view: detailview,
            onLoadSuccess: function (data) {
                const rows = data.rows;
                for (let i = 0; i < rows.length; i++) {
                    if (rows[i].productIsTc === 1) {
                        $(this).datagrid("expandRow", i);
                    }
                }
            },
            detailFormatter: function (index, row) {
                return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
            },
            onCollapseRow: function (index, row) {
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                try {
                    const rows = ddv.datagrid("getRows");
                    for (let i = 0; i < rows.length; i++) {
                        ddv.datagrid("endEdit", i);
                    }
                    indexData[index] = rows;
                } catch (e) {
                    indexData[index] = [];
                }
            },
            onExpandRow: function (index, row) {
                if (row.productIsTc !== 1) {
                    Tip.warn("该产品是非套材，无部件列表");
                    return;
                }
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                if (indexData[index] === undefined) {
                    Loading.show();
                    let planId = row.id;
                    if (!row.id) {
                        planId = -1;
                    }
                    let ws = $("#ws").val();
                    if (isEmpty(ws)) {
                        ws = $("#ws").combobox("getValue");
                    }
                    JQ.ajaxPost(path + "bom/plan/tc/ver/parts/" + row.fromSalesOrderDetailId + "/" + planId, {workShopCode: ws}, function (data) {
                        Loading.hide();
                        indexData[index] = data;
                        initSubGrid(ddv, index);
                    }, function (data) {
                        Loading.hide();
                    });
                } else {
                    initSubGrid(ddv, index);
                }
            }
        });
    });

    function initSubGrid(ddv, index) {
        ddv.datagrid({
            data: indexData[index],
            fitColumns: true,
            singleSelect: true,
            rownumbers: true,
            loadMsg: '',
            width: '450',
            autoUpdateDetail: false,
            height: 'auto',
            onClickRow: detailRowClick,
            rowStyler: danxiangbu,
            columns: [[{
                field: 'partName',
                title: '部件名称',
                width: 20
            }, {
                field: 'planPartCount',
                title: '计划数量',
                width: 15,
                editor: {
                    type: 'numberbox',
                    options: {
                        required: true,
                        precision: 0,
                        onChange: doChange,
                        max: 99999
                    }
                }
            }, {
                field: 'partCount',
                title: '订单数量',
                width: 15
            }, {
                field: 'partBomCount',
                title: 'BOM数量',
                width: 15
            }]],
            onResize: function () {
                $('#produceProducts').datagrid('fixDetailRowHeight', index);
            },
            onLoadSuccess: function () {
                setTimeout(function () {
                    $('#produceProducts').datagrid('fixDetailRowHeight', index);
                }, 0);
            }
        });
        indexData = {};
        $('#produceProducts').datagrid('fixDetailRowHeight', index);
    }

    function doChange(nv, ov) {
        /* var parent = $(this).datagrid("getParentGrid");
         var sub = $(this).datagrid("getSelfGrid");

         var rows = sub.datagrid("getRows");
         var minInt = 999999;
         var tempInt = -1;

         var editorValue;
         var rowValue;
         var editor;
         var value;

         for (var i = 0; i < rows.length; i++) {

         editor=sub.datagrid("getEditor",{index:i,field:"planPartCount"});
         if(editor){
         value=$(editor.target).numberbox("getValue");
         }else{
         value=rows[i].partCount;
         }
         tempInt = Calc.div(value, rows[i].partBomCount);
         if (tempInt < minInt)
         minInt = tempInt;
         }
         parent.datagrid("endEdit",sub.datagrid("getParentRowIndex")); */
        /* editor=parent.datagrid("getEditor",{index:sub.datagrid("getParentRowIndex"),field:"productCount"});
         if(editor!=null){
         parent.datagrid("endEdit",sub.datagrid("getParentRowIndex"));
         } */
        /* $("#produceProducts").datagrid("updateRow",{index:sub.datagrid("getParentRowIndex"),row:{requirementCount:minInt}}); */
        //parent.datagrid("beginEdit",sub.datagrid("getParentRowIndex"));
        /* editor=parent.datagrid("getEditor",{index:sub.datagrid("getParentRowIndex"),field:"productCount"});
         $(editor.target).numberbox("setValue",minInt); */
    }

    const valid = false;

    function detailRowClick(index, row) {
        $("#produceProducts").datagrid("endEdit", $(this).datagrid("getSelfGrid").datagrid("getParentRowIndex"));
        $(this).datagrid("beginEdit", index);
    }

    function doOrderChange(nv, ov) {
        let index = $(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
        const row = EasyUI.grid.getRowByIndex('produceProducts', index);
        if (!isEmpty(row)) {
            if (row.productIsTc === 1) {
                $(this).numberbox({suffix: '套'});
            } else {
                $(this).numberbox({suffix: '卷'});
            }
        }
        if (isEmpty(ov)) {
            return;
        }
        //获取当前编辑行的行号
        index = $(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
        const ddv = $("#produceProducts").datagrid('getRowDetail', index).find('table.ddv');
        try {
            const rows = ddv.datagrid("getRows");
            for (let i = 0; i < rows.length; i++) {
                ddv.datagrid("endEdit", i);
                ddv.datagrid("updateRow", {index: i, row: {planPartCount: Calc.mul(nv, rows[i].partBomCount, 0)}});
            }
        } catch (e) {
        }
    }

    function produceProductsClickRow(index, row) {
        $("#produceProducts").datagrid("beginEdit", index);
        if (row.packReq === "" && row.procReq === "") {
            JQ.ajaxGet(path + "planner/produce/requirements?productId=" + row.productId, function (data) {
                $("#addOrEditPackReq").html(data.PACKREQ);
                $("#addOrEditProcReq").html(data.PROCREQ);
            })
        } else {
            $("#addOrEditPackReq").html(row.packReq);
            $("#addOrEditProcReq").html(row.procReq);
        }
    }

    //查看库存
    function viewStock() {
        r = EasyUI.grid.getOnlyOneSelected("produceProducts");
        w = $("#ws").combobox("getValue");
        if (r == null)
            return;
        r.view = 1;
        const wid = Dialog.open("厂内名称:[<font color=red>" + r.factoryProductName + "</font>]　　工艺代码:[<font color=red>" + r.processBomCode + "</font>]　　工艺版本:[<font color=red>" + r.processBomVersion + "</font>]", 1500, 650, viewStockUrl + "?factoryProductName=" + r.factoryProductName + "&" + "productProcessCode=" + r.processBomCode + "&" + "productProcessBomVersion=" + r.processBomVersion + "&" + "workShopCode=" + w,
            [EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
                $("#" + wid).dialog("maximize");
            });
    }
</script>
<div style="height:98%;width:98%">
    <!--生产任务单表单-->
    <form id="producePlanForm" method="post" ajax="true"
          action="<%=basePath %>planner/produce/${empty producePlan.id ?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${producePlan.id}"/>
        <table width="100%">
            <tr>
                <td class="title">车间:</td>
                <!--生产任务单号-->
                <td><input type="text" id="ws" name="workShopCode"
                           value="${producePlan.workShopCode}"${empty producePlan.workShopCode?"":"readonly"}
                           class="easyui-combobox" required="true"
                           data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=cut,weave',onSelect:onSelect,icons:[]">
                </td>
                <td class="title">生产任务单号:</td>
                <!--生产任务单号-->
                <td>
                    <input type="text" editable="true"
                    <c:if test="${!empty force }"> readonly='true' </c:if> name="producePlanCode"
                           value="${producePlan.producePlanCode}" class="easyui-textbox" required="true"
                           data-options="icons:[]">
                </td>
                <td class="title">创建人:</td>
                <!--创建人-->
                <td>
                    <input type="text" editable="true" readonly name="producePlanCode" value="${userName}"
                           class="easyui-textbox" required="true" data-options="icons:[]">
                </td>
            </tr>
        </table>
    </form>
    <div id="produceSalesOrderToolbar">
        <c:if test="${empty force }">
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
               iconcls="icon-add" onclick="orderProductSelect()">添加产品
            </a>
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
               iconcls="icon-remove" onclick="orderProductRemove()">删除产品
            </a>
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"
           iconcls="icon-edit" onclick="editReqDetails()">修改 工艺/包装 需求
        </a>
        <a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'"
           onClick="viewStock()">查看库存
        </a>
    </div>
    <table id="produceProducts" toolbar="#produceSalesOrderToolbar"
           singleSelect="true" fitColumns="false" class="easyui-datagrid"
           height="55%" width="100%"
           data-options="rowStyler:rowStyler1,onClickRow:produceProductsClickRow">
        <!-- <thead frozen="true">
                <tr>
                    这是生产计划明细ID
                    <th field="fromSalesOrderDetailId" checkbox="true"></th>
                    <th field="salesOrderCode" width="100" >订单号</th>
                    <th field="consumerName" width="200" >客户名称</th>
                    <th field="batchCode" width="90" editor="{type:'textbox',options:{required:true}}" >批次号</th>
                    <th field="orderCount" width="50" >订单数量(kg/套)</th>
                    <th field="requirementCount" width="50" data-options="editor:{type:'numberbox',options:{required:true,precision:4,min:1,'icons':[]}}" >生产数量(kg/套)</th>
                    <th field="totalRollCount" width="50" data-options="editor:{type:'numberbox',options:{precision:0,min:1,'icons':[]}}" >总卷数</th>
                    <th field="totalTrayCount" width="50" data-options="editor:{type:'numberbox',options:{precision:0,min:1,'icons':[]}}" >总托数</th>
                    <th field="productName" width="80" >产品名称</th>
                </tr>
            </thead> -->
        <thead>
        <tr>
            <th field="fromSalesOrderDetailId" checkbox="true"></th>
            <th field="isTurnBagPlan" width="60"
                editor="{type:'combobox',options:{'valueField':'v','textField':'t','required':true,'icons':[],'data':[{'v':'生产','t':'生产'},{'v':'翻包','t':'翻包'}]}}">
                计划类型
            </th>
            <th field="salesOrderCode" width="100">订单号</th>
            <th field="salesOrderSubcodePrint" width="100" hidden="true">客户订单号</th>
            <th field="consumerName" width="200">客户名称</th>
            <th field="batchCode" width="90"
                    <c:if test="${empty force }"> editor="{type:'textbox',options:{required:true}}" </c:if>>批次号
            </th>
            <th field="orderCount" data-options="formatter:countFormatter">订单数量</th>
            <th width="80" field="orderTotalWeight"
                formatter="totalWeightFormatter2">总重量
            </th>
            <th width="80" field="orderTotalMetres"
                formatter="totalMetresFormatter">总米长
            </th>
            <th field="requirementCount" width="100"
                data-options="formatter:planCountFormatter,editor:{type:'numberbox',options:{required:true,precision:0,min:0,'icons':[],onChange:doOrderChange} }">
                排产数量
            </th>
            <th field="productLength" width="50">卷长(m)</th>
            <th field="productWidth" width="50">门幅(mm)</th>
            <th field="productWeight" width="50">卷重(kg)</th>
            <th width="80" field="planAssistCount"
                editor="{type:'numberbox',options:{'required':false,'icons':[],min:0,precision:2}}">排产辅助数量
            </th>
            <th width="80" field="planAssistUnit"
                editor="{type:'combobox',options:{'required':false,'icons':[],valueField:'v',textField:'t',data:[{'v':'','t':'　'},{'v':'KG','t':'KG'},{'v':'套','t':'套'}]}}">
                计划辅助单位
            </th>
            <!-- <th field="totalRollCount" width="100" data-options="editor:{type:'numberbox',options:{precision:0,min:1,'icons':[]}}" >总卷数</th>-->
            <th field="totalTrayCount" width="100"
                data-options="formatter:totalTrayCountFormatter,editor:{type:'numberbox',options:{'required':false,precision:0,min:1,'icons':[],suffix:'托'}}">
                总托数
            </th>
            <th field="deleveryDate" width="100"
                data-options="editor:{type:'datebox',options:{required:true,editable:false}}">出货日期
            </th>
            <th field="consumerProductName" width="300">客户产品名称</th>
            <th field="factoryProductName" width="300">厂内名称</th>
            <th field="productModel" width="300">产品规格</th>
            <!-- <th field="packReq" width="1" hidden="true"></th>
                <th field="procReq" width="1" hidden="true"></th> -->
            <th field="productLength" width="50">卷长(m)</th>
            <th field="productWidth" width="50">门幅(mm)</th>
            <th field="productWeight" width="50">卷重(kg)</th>
            <th field="drawNo" width="50">图号</th>
            <th field="rollNo" width="50">卷号</th>
            <th field="levelNo" width="50">层号</th>
            <th field="processBomCode" width="150">工艺代码</th>
            <th field="processBomVersion" width="80">工艺版本</th>
            <th field="bcBomCode" width="150">包装代码</th>
            <th field="bcBomVersion" width="80">包装版本</th>
            <th field="deviceCode" width="100"
                data-options="editor:{type:'textbox'}">机台号
            </th>
            <th field="comment" width="120"
                data-options="editor:{type:'textbox'}">备注
            </th>
            <!-- <th field="id"></th> -->
            <!-- <th field="productIsTc" hidden='true' width="80" >是否套材</th> -->
        </tr>
        </thead>
    </table>
    <table style="width:100%;height:35%;table-layout: fixed;">
        <tr style="height: 10%;">
            <td class="title" style="text-align: left;font-size:20px;">包装要求</td>
            <td class="title" style="text-align: left;font-size:20px;">工艺要求</td>
        </tr>
        <tr style="height: 90%;">
            <td style="padding: 5px;">
                <pre id="addOrEditPackReq" name="packReq" style="width: 100%; height:100%"></pre>
            </td>
            <td style="padding: 5px;">
                <pre id="addOrEditProcReq" name="procReq" style="width: 100%; height:100%"></pre>
            </td>
        </tr>
    </table>
</div>

