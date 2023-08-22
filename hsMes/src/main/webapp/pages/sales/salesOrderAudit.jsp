<!--
作者:宋黎明
日期:2016-10-27 10:06:42
页面:查看审核后页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../base/meta.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
    @keyframes changed {
        from {
            color: white;
            background: rgb(214, 9, 9);
        }
        to {
            color: white;
            background: rgba(255, 0, 0, 0.55);
        }
    }
</style>
<script type="text/javascript">
    function editTimesFormatter(value, index, row) {
        return (isEmpty(value) ? 0 : value) + "次";
    }

    function detailRowStyler(index, row) {
        let style = "";
        if (isEmpty(row.closed) || row.closed === 0) {
        } else {
            style += "text-decoration:line-through;";
        }
        if (row.editTimes != null && row.editTimes > 0) {
            style += "animation: changed 0.5s infinite ease-in-out;animation-direction: alternate;";
        }
        return style;
    }

    const details = ${details};
    const indexData = {};
    $(function () {
        $("#audit_product_dg").datagrid({
            data: details,
            view: detailview,
            onLoadSuccess: function (data) {
                const rows = data.rows;
                for (let i = 0; i < rows.length; i++) {
                    if (rows[i].PRODUCTISTC === 1) {
                        $(this).datagrid("expandRow", i);
                    }
                }
            },
            detailFormatter: function (index, row) {
                return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
            },
            onExpandRow: function (index, row) {
                if (row.PRODUCTISTC !== 1) {
                    Tip.warn("该产品是非套材，无部件列表");
                    return;
                }
                let orderId = row.ID;
                if (!row.ID) {
                    orderId = -1;
                }
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                JQ.ajaxPost(path + "bom/tc/ver/parts/" + row.PROCBOMID + "/" + row.PRODUCTCOUNT + "/" + orderId, {}, function (data) {
                    Loading.hide();
                    ddv.datagrid({
                        data: data,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        width: '300',
                        rowStyler: danxiangbu,
                        columns: [[{
                            field: 'partName',
                            title: '部件名称',
                            width: 15
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
                            $('#audit_product_dg').datagrid('fixDetailRowHeight', index);
                            $('#audit_product_dg').datagrid('fixRowHeight', index);
                        },
                        onLoadSuccess: function () {
                            setTimeout(function () {
                                $('#audit_product_dg').datagrid('fixDetailRowHeight', index);
                                $('#audit_product_dg').datagrid('fixRowHeight', index);
                            }, 0);
                        }
                    });
                    $('#audit_product_dg').datagrid('fixDetailRowHeight', index);
                }, function (data) {
                    Loading.hide();
                });
            }
        });
    });

    //查看工艺bom明细
    function common_bomVersionView(value, row, index) {
        if (value == null) {
            return "";
        } else if (row.PRODUCTPROCESSCODE == null || row.PRODUCTPROCESSCODE === "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_common_bomVersionView(" + row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value + "</a>"
        }
    }

    let dialogId;

    function _common_bomVersionView(procBomId, isTc) {
        if (procBomId == null) {
            Tip.error("工艺版本错误，请重新编辑产品");
            return;
        }
        let viewUrl = "";
        if (isTc === 1) {
            viewUrl = path + "selector/view/tc?procBomId=" + procBomId;
        } else {
            viewUrl = path + "selector/view/ftc?procBomId=" + procBomId;
        }
        dialogId = Dialog.open("查看工艺bom明细", 700, 400, viewUrl, [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            $("#" + dialogId).dialog("maximize");
            if (isTc !== 1) {
                for (var a = 0; a < details.length; a++) {
                    _common_bomDetail_data(details[a]);
                }
            }
        });
    }

    //查看包装bom明细
    function common_packBomView(value, row, index) {
        if (value == null) {
            return "";
        } else if (value === "无包装") {
            return "";
        } else if (row.PRODUCTPACKAGINGCODE == null || row.PRODUCTPACKAGINGCODE === "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_common_packBomView(" + row.PACKBOMID + ")'>" + value + "</a>"
        }
    }

    function _common_packBomView(packBomId) {
        if (packBomId == null || packBomId === "") {
            Tip.error("包装工艺错误，请重新编辑产品");
            return;
        }
        const viewUrl = path + "selector/view/bc?packBomId=" + packBomId;
        dialogId = Dialog.open("查看包装bom明细", 700, 400, viewUrl, [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            $("#" + dialogId).dialog("maximize");
            for (var a = 0; a < details.length; a++) {
                _common_bcBomDetail_data(details[a]);
            }
        });
    }

    function packTask(v, r, i) {
        if (r.PRODUCTISTC === 2) {
            return "<a style='color:red' href='javascript:void(0)' onclick='loadTask(" + r.ID + "," + r.PRODUCTID + ")'>包装任务</a>";
        }
    }
</script>
<div>
    <!--销售订单表单-->
    <form id="salesOrderAuditForm" method="post" ajax="true"
          action="<%=basePath %>salesOrder/${empty salesOrder.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${salesOrder.id}"/>
        <table width="100%">
            <tr>
                <td class="title">订单号:</td>
                <td>
                    <input type="text" id="salesOrderCode" name="salesOrderCode" value="${salesOrder.salesOrderCode}"
                           class="easyui-textbox" required="true" editable="false" data-options="'icons':[]">
                </td>
                <td class="title">下单日期:</td>
                <td>
                    <input type="text" id="salesOrderDate" name="salesOrderDate"
                           value="${fn:substring(salesOrder.salesOrderDate, 0, 19)}" class="easyui-textbox"
                           required="true" editable="false" data-options="'icons':[]">
                </td>
            </tr>
            <tr>
                <td class="title">客户名称:</td>
                <td>
                    <input type="hidden" name="salesOrderConsumerId" id="salesOrderConsumerId"
                           value="${salesOrder.salesOrderConsumerId }">
                    <input type="hidden" id="consumerCode" value="${consumerCode }">
                    <input type="text" id="salesOrderConsumerName" value="${consumerName}" class="easyui-textbox"
                           required="true" editable="false" data-options="'icons':[]"></td>
                <td class="title">业务员:</td>
                <td>
                    <input type="hidden" name="salesOrderBizUserId" id="salesOrderBizUserId"
                           value="${salesOrder.salesOrderBizUserId }">
                    <input type="text" id="salesOrderBizUserName" value="${userName}" class="easyui-textbox"
                           required="true" editable="false" data-options="'icons':[]"></td>
            </tr>
            <tr>
                <td class="title">内销/外销/胚布:</td>
                <!--内销/外销-->
                <td>
                    <input type="text" id="salesOrderIsExport" name="salesOrderIsExport"
                           value="${salesOrder.salesOrderIsExport}" class="easyui-combobox" required="true"
                           editable="false" readonly="true"
                           data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'内销'},{'v':'-1','t':'胚布'},{'v':'0','t':'外销'}],'icons':[]">
                </td>
                <td class="title">订单类型:</td>
                <!--订单类型 （3新品，2试样，1常规产品，-1未知）-->
                <td>
                    <input type="text" id="salesOrderType" required="true" name="salesOrderType"
                           value="${salesOrder.salesOrderType}" class="easyui-combobox" required="true" readonly="true"
                           editable="false"
                           data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'常规'},{'v':'2','t':'试样'},{'v':'3','t':'新品'},{'v':'-1','t':'未知'}],'icons':[]">
                </td>
                <%-- <td class="title">订单总金额:</td>
                <!--订单总金额-->
                <td><input type="text" id="salesOrderTotalMoney" name="salesOrderTotalMoney" value="${salesOrder.salesOrderTotalMoney}" class="easyui-textbox" editable="false" readonly="true" data-options="'icons':[]"></td> --%>
            </tr>
            <tr>
                <td class="title">订单备注:</td>
                <td colspan="3">
					<textarea style="width:100%;height:50px;border-radius:5px;background-color:white;"
                              name="salesOrderMemo"
                              disabled="disabled" maxlength="2000">${salesOrder.salesOrderMemo}</textarea>
                </td>
            </tr>
        </table>
    </form>
    <div style="widht:100%;">
        <table id="audit_product_dg" title="订单产品" class="easyui-datagrid" fitColumns="false" style="width:100%;"
               data-options="rowStyler:detailRowStyler">
            <thead>
            <tr>
                <th field="PACK_TASK" formatter="packTask" width="70">查看包装</th>
                <th field="EDITTIMES" formatter="editTimesFormatter" width="70">变更次数</th>
                <th field="SALESORDERSUBCODE" width="150">订单号</th>
                <th field="CONSUMERPRODUCTNAME" width="200">客户产品名称</th>
                <th field="FACTORYPRODUCTNAME" width="200">厂内名称</th>
                <th field="PRODUCTMODEL" width="200">产品型号</th>
                <!-- <th field="productBatchCode" width="80">批次号</th> -->
                <th field="PRODUCTCOUNT" formatter="countFormatter">数量</th>
                <th width="80" field="TOTALWEIGHT"
                    data-options="formatter:function(value,row,index){if(isEmpty(value))return '';return value+'KG';}">
                    总重量
                </th>
                <th width="80" field="TOTALMETRES"
                    data-options="formatter:function(value,row,index){if(isEmpty(value))return '';return value+'米';}">
                    总米长
                </th>
                <th field="DELIVERYTIME">发货时间</th>
                <th field="PRODUCTWIDTH">门幅(mm)</th>
                <th field="PRODUCTROLLLENGTH">卷长(m)</th>
                <th field="PRODUCTROLLWEIGHT">卷重(Kg)</th>
                <th field="PRODUCTPROCESSCODE" formatter="common_bomVersionView" styler="vStyler">工艺代码</th>
                <th field="PRODUCTPROCESSBOMVERSION">工艺版本</th>
                <th field="PRODUCTCONSUMERBOMVERSION">客户版本</th>
                <th field="PRODUCTPACKAGINGCODE">包装代码</th>
                <th field="PRODUCTPACKAGEVERSION">包装版本</th>
                <th field="PRODUCTMEMO">备注</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<%@ include file="../packTask/pack_task_order.jsp" %>