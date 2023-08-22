<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../base/jstl.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
    form {
        margin: 0px;
    }

    .datagrid-row-selected {
        background: #d4d8f7 !important;
        color: #000000;
    }
</style>
<div id='salesorder_form_layout' class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
    <div data-options="region:'north',height:160,title:'订单信息',split:true">
        <!--销售订单表单-->
        <form id="salesOrderForm" method="post" ajax="true"
              action="<%=basePath %>salesOrder/${empty salesOrder.id ?'add':'edit'}" autocomplete="off">
            <input type="hidden" name="id" value="${salesOrder.id}"/>
            <input type="hidden" name="auditState" value="${salesOrder.auditState}"/>
            <input type="hidden" name="isClosed" value="0"/>
            <input type="hidden" id="forceEdit" value="${forceEdit }">
            <table width="100%">
                <tr>
                    <td class="title">客户名称:</td>
                    <!--客户名称-->
                    <td><input type="hidden" name="salesOrderConsumerId" id="salesOrderConsumerId"
                               value="${salesOrder.salesOrderConsumerId }">
                        <input type="hidden" id="consumerCode" value="${consumerCode }">
                        <input type="text" id="salesOrderConsumerName" value="${consumerName}" class="easyui-searchbox"
                               required="true" data-options="searcher:selectConsumer,icons:[]">
                    </td>
                    <td class="title">内销/外销:</td>
                    <!--内销/外销-->
                    <td><input type="text" id="salesOrderIsExport" name="salesOrderIsExport"
                               value="${salesOrder.salesOrderIsExport}" class="easyui-combobox"
                               data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'内销'},{'v':'0','t':'外销'},{'v':'-1','t':'胚布'}],onSelect:changeSerial">
                    </td>
                </tr>
                <tr>
                    <td class="title">订单编号:</td>
                    <!--订单号-->
                    <td>
                        <input type="text" id="salesOrderCode" name="salesOrderCode"
                               value="${salesOrder.salesOrderCode}" class="easyui-textbox" required="true">
                    </td>
                    <td class="title">下单日期:</td>
                    <!--下单日期-->
                    <td>
                        <input type="text" id="salesOrderDate" name="salesOrderDate"
                               value="${salesOrder.salesOrderDate}" class="easyui-datebox" required="true">
                    </td>
                </tr>
                <tr>
                    <td class="title">业务员:</td>
                    <!--业务员-->
                    <td>
                        <input type="hidden" name="salesOrderBizUserId" id="salesOrderBizUserId"
                               value="${empty salesOrder.salesOrderBizUserId?userId:salesOrder.salesOrderBizUserId }">
                        <input type="text" id="salesOrderBizUserName" value="${empty bizUserName?userName:bizUserName}"
                               class="easyui-searchbox" required="true" data-options="searcher:selectUser,icons:[]">
                    </td>
                    <td class="title">订单类型:</td>
                    <!--订单类型 （3新品，2试样，1常规产品，-1未知）-->
                    <td><input type="text" id="salesOrderType" required="true" name="salesOrderType"
                               value="${salesOrder.salesOrderType}" class="easyui-combobox"
                               data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'常规'},{'v':'2','t':'试样'},{'v':'3','t':'新品'},{'v':'-1','t':'未知'}]">
                    </td>
                </tr>
                <tr>
                    <td class="title">订单备注:</td>
                    <!--订单备注-->
                    <td colspan="3"><textarea style="width:100%;height:40px;"
                            <c:if test="${!empty force }"> readonly="true" </c:if> name="salesOrderMemo"
                                              maxlength="2000">${salesOrder.salesOrderMemo}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',title:'订单产品'">
        <div id="toolbar_product">
            <c:if test="${empty force }">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-add"
                   onclick="selectProduct()">增加</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-remove"
                   onclick="removeProduct()">删除</a>
            </c:if>
        </div>
        <table id="product_dg" singleSelect="true" class="easyui-datagrid" fitColumns="true"
               style="width:100%;height:100%;" toolbar="#toolbar_product">
            <thead>
            <tr>
                <th field="productId" checkbox=true></th>
                <th width="100" field="consumerProductName">客户产品名称</th>
                <th width=120 field="factoryProductName">厂内名称</th>
                <th width="120" field="salesOrderSubCode" styler="orderStyler"
                    <c:if test="${empty force }">editor="{type:'textbox',options:{required:true,'icons':[]}}" </c:if>>
                    订单号
                </th>
                <th width="120" field="salesOrderSubCodePrint" styler="printOrderStyler"
                    editor="{type:'textbox',options:{'icons':[],required:true}}">打印订单号
                </th>
                <th width="80" field="productCount"
                    editor="{type:'numberbox',options:{'required':true,'icons':[],onChange:doOrderChange,min:0,precision:0,suffix:'',onChange:onOrderCountChanged}}"
                    data-options="formatter:countFormatter">数量
                </th>
                <th width="80" field="totalMetres"
                    editor="{type:'numberbox',options:{'required':false,'icons':[],min:0,precision:2,suffix:'米'}}"
                    data-options="formatter:function(value,row,index){if(isEmpty(value))return '';return value+'米';}">
                    总米长
                </th>
                <th width="100" field="deliveryTime" editor="{type:'datebox',options:{'required':true}}"
                    data-options="formatter:function(value,row,index){if(value)return value.substring(0,10);}">发货时间
                </th>
                <th width="80" field="productModel">产品型号</th>
                <th width="65" field="productWidth">门幅(mm)</th>
                <th width="65" field="productRollLength">卷长(m)</th>
                <th width="65" field="productRollWeight">卷重(Kg)</th>
                <th width="80" field="productProcessCode">工艺代码</th>
                <th width="80" field="productProcessBomVersion">工艺版本</th>
                <th width="80" field="productConsumerBomVersion">客户版本</th>
                <th width="80" field="productPackagingCode">包装代码</th>
                <th width="80" field="productPackageVersion">包装版本</th>
                <%-- <th width="80" field="productMemo" <c:if test="${empty force }">editor="{type:'textbox',options:{'icons':[]}}"</c:if>>备注</th> --%>
                <th width="80" field="productMemo" editor="{type:'textbox',options:{'icons':[]}}">备注</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<script type="text/javascript">
    const details = ${details};
    let indexData = {};
    $(function () {
        $("#product_dg").datagrid({
            data: details,
            onClickRow: clickRow,
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
                const rows = ddv.datagrid("getRows");
                for (let i = 0; i < rows.length; i++) {
                    ddv.datagrid("endEdit", i);
                }
                indexData[index] = rows;
            },
            onExpandRow: function (index, row) {
                if (row.productIsTc !== 1) {
                    Tip.warn("该产品是非套材，无部件列表");
                    return;
                }
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                if (indexData[index] === undefined) {
                    Loading.show();
                    let orderId = row.id;
                    if (!row.id) {
                        orderId = -1;
                    }
                    JQ.ajaxPost(path + "bom/tc/ver/parts/" + row.procBomId + "/" + row.productCount + "/" + orderId, {}, function (data) {
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
            width: '300',
            autoUpdateDetail: false,
            height: 'auto',
            onClickRow: detailRowClick,
            rowStyler: danxiangbu,
            columns: [[{
                field: 'partName',
                title: '部件名称',
                width: 15
            }, {
                field: 'partCount',
                title: '订单数量',
                width: 15,
                editor: {
                    type: 'numberbox',
                    options: {
                        precision: 0,
                        required: true,
                        onChange: doChange,
                        max: 99999
                    }
                }
            }, {
                field: 'partBomCount',
                title: 'BOM数量',
                width: 15
            }]],
            onResize: function () {
                $('#product_dg').datagrid('fixDetailRowHeight', index);
            },
            onLoadSuccess: function () {
                setTimeout(function () {
                    $('#product_dg').datagrid('fixDetailRowHeight', index);
                }, 0);
            }
        });
        indexData = {};
        $('#product_dg').datagrid('fixDetailRowHeight', index);
    }

    function onOrderCountChanged(nv, ov) {
        const index = $(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
        const row = EasyUI.grid.getRowByIndex('product_dg', index);
        if (!isEmpty(row)) {
            if (row.productIsTc === 1) {
                $(this).numberbox({suffix: '套'});
            } else {
                $(this).numberbox({suffix: '卷'});
            }
        }
    }

    function doChange(nv, ov) {
        const parent = $(this).datagrid("getParentGrid");
        const sub = $(this).datagrid("getSelfGrid");
        const rows = sub.datagrid("getRows");
        let minInt = 999999;
        let tempInt = -1;
        let editorValue;
        let rowValue;
        let editor;
        let value;
        for (let i = 0; i < rows.length; i++) {
            editor = sub.datagrid("getEditor", {index: i, field: "partCount"});
            if (editor) {
                value = $(editor.target).numberbox("getValue");
            } else {
                value = rows[i].partCount;
            }
            tempInt = Math.floor(Calc.div(value, rows[i].partBomCount, 2));
            if (tempInt < minInt)
                minInt = tempInt;
        }
        editor = parent.datagrid("getEditor", {index: sub.datagrid("getParentRowIndex"), field: "productCount"});
        if (editor != null) {
            parent.datagrid("endEdit", sub.datagrid("getParentRowIndex"));
        }
        $("#product_dg").datagrid("updateRow", {index: sub.datagrid("getParentRowIndex"), row: {productCount: minInt}});
        //parent.datagrid("beginEdit",sub.datagrid("getParentRowIndex"));
        /* editor=parent.datagrid("getEditor",{index:sub.datagrid("getParentRowIndex"),field:"productCount"});
        $(editor.target).numberbox("setValue",minInt); */
    }

    function clickRow(index, row) {
        valid = false;
        $("#product_dg").datagrid("beginEdit", index);
    }

    function detailRowClick(index, row) {
        $("#product_dg").datagrid("endEdit", $(this).datagrid("getSelfGrid").datagrid("getParentRowIndex"));
        /* try {
            $("#product_dg").datagrid("beginEdit", $(this).datagrid("getSelfGrid").datagrid("getParentRowIndex"));
        } catch (e) {
            $("#product_dg").datagrid("beginEdit", $(this).datagrid("getSelfGrid").datagrid("getParentRowIndex"));
        } */
        $(this).datagrid("beginEdit", index);
    }

    function doOrderChange(nv, ov) {
        if (isEmpty(ov)) {
            return;
        }
        //获取当前编辑行的行号
        const index = $(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
        const ddv = $("#product_dg").datagrid('getRowDetail', index).find('table.ddv');
        try {
            const rows = ddv.datagrid("getRows");
            for (let i = 0; i < rows.length; i++) {
                ddv.datagrid("endEdit", i);
                ddv.datagrid("updateRow", {index: i, row: {partCount: Calc.mul(nv, rows[i].partBomCount, 0)}});
            }
        } catch (e) {
        }
    }
</script>