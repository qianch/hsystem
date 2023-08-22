<!--
作者:徐波
日期:2017-02-07 14:49:00
页面:产品信息查询JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    let produceTreeUrl = path + "salesOrder/listsalesOrderDetail";
    const findInfoUrl = path + "salesOrder/salesOrderInfos";
    //查询部件信息
    const partsList = path + "bom/tcBomVersionParts/mirrorList";
    //datagrid数据
    const dgList = path + "bom/ftc/mirrorList";
    //查询部件明细
    const partsD = path + "bom/tcBomVersionPartsDetail/mirrorList";
    //查询部件成品重量胚布信息
    const partsFW = path + "bom/tcBomVersionPartsFinishedWeightEmbryoCloth/mirrorList";
    //查询成品信息
    const productUrl = path + "finishProduct/productMirrorList";
    const data = [{
        "text": "镜像bom数据",
        "state": "closed",
        "attributes": {
            "status": "0",
            "vId": "0"
        }
    }];
    const searchInfo = function () {
        $("#searchInput").searchbox("setText", $("#searchInput").searchbox("getText").toUpperCase());
        const t = $("#searchInput").searchbox("getText");
        produceTreeUrl = path + "salesOrder/listsalesOrderDetail" + "?status=0&data=" + t.toString().replace(/\s+/g, "") + "&isNeedTop=1";
        if (t !== "") {
            $('#produceTree').tree({
                url: produceTreeUrl,
                data: data,
                method: 'get',
                animate: true,
                onBeforeExpand: function (node, param) {
                    $('#produceTree').tree('options').url = produceTreeUrl + "&status=" + node.attributes.status + "&vId=" + node.attributes.vId + "&productIsTc=" + node.attributes.productIsTc;
                },
                onClick: function (node) {
                    if (node.attributes.status === "4" && node.attributes.productIsTc === "1") {
                        $("#parts").datagrid({
                            url: encodeURI(partsList + "?filter[id]=" + node.attributes.ID)
                        });
                        $("#product").datagrid({
                            url: encodeURI(productUrl + "?filter[id]=" + node.attributes.SALESORDERID)
                        });
                        $('#partsDetails').datagrid('loadData', {
                            total: 0,
                            rows: []
                        });
                        $('#partsFinishedWeightEmbryoCloth').datagrid('loadData', {
                            total: 0,
                            rows: []
                        });
                    }
                    if (node.attributes.productIsTc === "2" && node.attributes.nodeType === "version") {
                        $("#dg").datagrid({
                            url: encodeURI(dgList + "?filter[id]=" + node.attributes.ID)
                        });
                        $('#partsDetails').datagrid('loadData', {
                            total: 0,
                            rows: []
                        });
                        $('#partsFinishedWeightEmbryoCloth').datagrid('loadData', {
                            total: 0,
                            rows: []
                        });
                        $("#parts").datagrid('loadData', {
                            total: 0,
                            rows: []
                        });
                    } else {
                        $("#dg").datagrid('loadData', {
                            total: 0,
                            rows: []
                        });
                    }
                    if (node.attributes.status === "5") {
                        const node1 = $('#produceTree').tree('find', node.attributes.TCPROCBOMVERSIONPARENTPARTS);
                        $("#parts").datagrid({
                            url: encodeURI(partsList + "?filter[ids]=" + node.attributes.ID)
                        });
                        $("#partsDetails").datagrid({
                            url: encodeURI(partsD + "?filter[ids]=" + node.attributes.ID)
                        });
                        $("#partsFinishedWeightEmbryoCloth").datagrid({
                            url: encodeURI(partsFW + "?filter[ids]=" + node.attributes.ID)
                        });
                    }
                }
            });
        }
    };

    function formatterS(value, row, index) {
        if (value == null) {
            return "正常";
        } else {
            return "已作废";
        }
    }

    function formatterWeigh(value, row, index) {
        if (value === 0) {
            return "全称	";
        }
        if (value === 1) {
            return "抽称";
        }
        if (value === 2) {
            return "不称";
        }
        if (value === 3) {
            return "首卷必称";
        }
    }

    //包装代码
    function formatterPackagingCode(value, row, index) {
        if (value == null) {
            return "无包装";
        } else {
            return value;
        }
    }

    //产品属性
    function formatterIscommon(value, row, index) {
        if (value === 0) {
            return "试样";
        } else if (value === 1) {
            return "常规";
        }
    }

    //审核状态
    function formatterReviewState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    //查看产品信息
    const lookProduct = path + "finishProduct/checkProductMirror";
    const checkProduct = function () {
        const r = EasyUI.grid.getOnlyOneSelected("product");
        const wid = Dialog.open("查看产品信息", 1017, 550, lookProduct + "?id=" + r.ID, [
            EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
        });
    };
</script>