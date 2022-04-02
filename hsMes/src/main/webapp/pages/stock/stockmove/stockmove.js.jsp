<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    var findproducePlanDetailUrl = path + "planner/producePlanDetail/getProducePlanDetailPageInfo";
    var moveList2Url = path + "stock/productStock/moveList2";

    var dialogWidth = 700, dialogHeight = 500;


    $(function () {
        $('#dg').datagrid({
            url: findproducePlanDetailUrl,
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                var url11=moveList2Url + "?producePlanDetailId=" +row.ID+"&"+ JQ.getFormAsString("stockmoveSearchForm");


                ddv.datagrid({
                        url: moveList2Url + "?producePlanDetailId=" +row.ID+"&start="+ $("input[name='filter[start]']").val()+"&end="+ $("input[name='filter[end]']").val() ,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        height: 'auto',
                        columns: [[
                            {
                                field: 'BARCODE',
                                title: '条码号',
                                width: 50,
                            }, {
                                field: 'MOVEUSER',
                                title: '操作人',
                                width: 20,
                            }, {
                                field: 'ORIGINWAREHOUSENAME',
                                title: '起始仓库名称',
                                width: 20,
                            }, {
                                field: 'ORIGINWAREHOUSECODE',
                                title: '起始仓库编码',
                                width: 20,
                            }, {
                                field: 'ORIGINWAREHOUSEPOSCODE',
                                title: '起点库位',
                                width: 20,
                            }, {
                                field: 'NEWWAREHOUSENAME',
                                title: '终点仓库名称',
                                width: 20,
                            }, {
                                field: 'NEWWAREHOUSECODE',
                                title: '终点仓库编码',
                                width: 20,
                            }, {
                                field: 'NEWWAREHOUSEPOSCODE',
                                title: '终点库位',
                                width: 20,
                            }, {
                                field: 'MOVETIME',
                                title: '移库时间',
                                width: 20,
                            }, {
                                field: 'WEIGHT',
                                title: '重量',
                                width: 20,
                            }

                        ]],
                        onResize: function () {
                            $('#dg').datagrid('fixDetailRowHeight', index);
                        },
                        onLoadSuccess: function () {
                            Loading.hide();
                            setTimeout(function () {
                                $('#dg').datagrid('fixDetailRowHeight', index);
                            }, 0);
                        }
                        , rowStyler: function (index, row) {
                            if (row.length == 0) {
                                return 'background-color:yellow;color:blue;font-weight:bold;';
                            }
                        }
                    }
                );
            }
            , rowStyler: function (index, row) {
                if (row.ONTHEWAYCOUNT > 0) {
                    return 'background-color:pink;color:blue;font-weight:bold;';
                }
            }

        });
    });


    //查询
    function filter() {
        EasyUI.grid.search("dg", "stockmoveSearchForm");
    }


</script>
