<!--
作者:肖文彬
日期:2016-11-16 12:48:44
页面:原料出库表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加原料出库表
    const addUrl = path + "stock/materialStockOut/add";
    //编辑原料出库表
    const editUrl = path + "stock/materialStockOut/edit";
    //删除原料出库表
    const deleteUrl = path + "stock/materialStockOut/delete";
    const dialogWidth = 700, dialogHeight = 350;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "materialStockOutSearchForm");
    }

    $(function () {
        $('#dg').datagrid({
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                const ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                ddv.datagrid({
                    url: path + "stock/materialStockOut/findOut?id=" + row.ID,
                    fitColumns: true,
                    singleSelect: true,
                    rownumbers: true,
                    loadMsg: '',
                    height: 'auto',
                    columns: [[
                        {field: 'MATERIALCODE', title: '物料条码', width: 15},
                        {field: 'WAREHOUSECODE', title: '仓库编码', width: 15},
                        {field: 'WAREHOUSEPOSCODE', title: '库位编码', width: 15},
                        {field: 'WORKSHOP', title: '领料仓库', width: 15},
                        {field: 'OUTWEIGHT', title: '重量', width: 15},
                        {field: 'OUTTIME', title: '出库时间', width: 15},
                        {field: 'NUMBERDEVIATION', title: '号数偏差', width: 15},
                        {field: 'SUBWAY', title: '接头方式', width: 15},
                        {field: 'MATERIALMODEL', title: '规格型号', width: 15},
                        {field: 'PRODUCECATEGORY', title: '产品大类', width: 15},
                        {field: 'PRODUCEDATE', title: '生产日期', width: 15},
                        {field: 'NAME', title: '操作人', width: 15}
                    ]],
                    onResize: function () {
                        $('#dg').datagrid('fixDetailRowHeight', index);
                    },
                    onLoadSuccess: function () {
                        setTimeout(function () {
                            $('#dg').datagrid('fixDetailRowHeight', index);
                        }, 100);
                    }
                });
                $('#dg').datagrid('fixDetailRowHeight', index);
            }
        });
    });

    //添加原料出库表
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("materialStockOutForm", addUrl, function (data) {
                        filter();
                        if (Dialog.isMore(wid)) {
                            Dialog.close(wid);
                            add();
                        } else {
                            Dialog.close(wid);
                        }
                    })
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid)
                })], function () {
                Dialog.more(wid);
            }
        );
    };

    //编辑原料出库表
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialStockOutForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialStockOutForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除原料出库表
    const doDelete = function () {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    };
</script>