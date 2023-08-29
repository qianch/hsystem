<!--
作者:肖文彬
日期:2016-11-8 15:25:19
页面:盘库记录表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加盘库记录表
    const addUrl = path + "stockCheck/add";
    //编辑盘库记录表
    const editUrl = path + "stockCheck/edit";
    //删除盘库记录表
    const deleteUrl = path + "stockCheck/delete";
    const dialogWidth = 700, dialogHeight = 350;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "stockCheckSearchForm");
    }

    function stateStyler(index, row) {
        const value = row.CHECKRESULT;
        if (value === undefined) return "";
        if (value === 0) {
            return "background:#86f186;";
        } else if (value === 1) {
            return "background:yellow;";
        } else if (value === 2) {
            return "background: #ff9393;";
        } else if (value === 3) {
            return "background: #ff79e8;";
        }
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
                    url: path + "stock/productStock/findCheck?id=" + row.ID,
                    fitColumns: true,
                    singleSelect: true,
                    rownumbers: true,
                    loadMsg: '',
                    height: 'auto',
                    rowStyler: stateStyler,
                    columns: [[
                        {
                            field: 'BARCODE',
                            title: '条码号(产品或原料)',
                            width: 15,
                            formatter: function (value, row, index) {
                                return row.PRODUCTPALLETCODE ? row.PRODUCTPALLETCODE : row.MATERIALPALLETCODE;
                            }
                        },
                        {
                            field: 'CHECKRESULT', title: '盘点状态', width: 8, formatter: function (value, row, index) {
                                if (value === 0) {
                                    return "正常";
                                } else if (value === 1) {
                                    return "错库";
                                } else if (value === 2) {
                                    return "非法入库";
                                } else if (value === 3) {
                                    return "非法出库";
                                } else if (value === 4) {
                                    return "错库或非法入库";
                                }
                            }
                        }
                    ]],
                    onResize: function () {
                        $('#dg').datagrid('fixDetailRowHeight', index);
                    },
                    onLoadSuccess: function (data) {
                        setTimeout(function () {
                            $('#dg').datagrid('fixDetailRowHeight', index);
                        }, 100);
                        if (data.total === 0) {
                            $(this).datagrid("appendRow", {"CHECKRESULT": -1});
                        }
                    }
                });
                $('#dg').datagrid('fixDetailRowHeight', index);
            }
        });
    });

    //添加盘库记录表
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("stockCheckForm", addUrl, function (data) {
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

    //编辑盘库记录表
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("stockCheckForm", editUrl, function (data) {
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
                EasyUI.form.submit("stockCheckForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除盘库记录表
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

    function formatterIsCheck(value, row) {
        if (value === '1') {
            return "盘过"
        }
        if (value === '2') {
            return "未盘过"
        }
    }
</script>