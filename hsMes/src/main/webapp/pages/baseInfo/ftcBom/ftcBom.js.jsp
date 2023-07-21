<!--
作者:宋黎明
日期:2016-10-8 13:36:52
页面:非套材工艺BOMJS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //datagrid数据
    const dgList = path + "bom/ftc/list";
    //添加非套材工艺BOM
    const addUrl = path + "bom/ftc/add";
    //编辑非套材工艺BOM
    const editUrl = path + "bom/ftc/edit";
    //删除非套材工艺BOM
    const deleteUrl = path + "bom/ftc/delete";
    //添加非套材BOM版本
    const addVersionUrl = path + "bom/ftc/addBomVersion";
    //添加非套材BOM版本
    const editVersionUrl = path + "bom/ftc/editBomVersion";
    //添加非套材BOM版本
    const deleteVersionUrl = path + "bom/ftc/deleteBomVersion";
    //删除非套材BOM明细
    const deleteDetailUrl = path + "bom/ftc/deleteDetail";
    //保存非套材BOM明细
    const saveDetailUrl = path + "bom/ftc/saveDetail";
    //选择客户信息
    const chooseConsumer = path + "selector/consumer";
    //树加载
    let ftcBomtreeUrl = path + "bom/ftc/listBom";

    const ftcFilterUrl = path + "/bom/ftc/bomList";
    //版本拷贝
    const copyftcBomVersion = path + "bom/ftc/copyftcBomVersion";
    //打开提交审核页面
    const _auditCommitUrl = path + "selector/commitAudit";
    //发送提交审核数据
    const auditCommitUrl = path + "bom/ftc/commitAudit";
    //查看bom下有没有版本
    const findV = path + "bom/ftc/findV";
    let consumerWindow = null;
    let editIndex = undefined;
    let isFirst = true;
    let addindex = 0;
    const data = [{
        "id": 1,
        "text": "非套材BOM",
        "state": "closed",
        "attributes": {
            'nodeType': 'root'
        }
    }];

    //根据选项框选择的是试样还是正式的bom，改变获取bom的url
    function isgetTestPro() {
        const value = $("#testPro").combobox('getValue');
        if (value === 1) {
            ftcBomtreeUrl = path + "bom/ftc/listBom";
        } else if (value === -1) {
            ftcBomtreeUrl = path + "bom/ftc/listBomTest";
        } else {
            ftcBomtreeUrl = path + "bom/ftc/listBomTest1";
        }
    }

    //初始化树
    $(function () {
        $('#FTcBomTree').tree({
            url: ftcBomtreeUrl,
            data: data,
            method: 'get',
            animate: true,
            onBeforeLoad: function (node, param) {
                if (isFirst) {
                    isFirst = false;
                    return false;
                } else {
                    return true;
                }
            },
            formatter: auditTreeStyler,
            onContextMenu: function (e, node) {
                e.preventDefault();
                $(this).tree('select', node.target);
                if (node.attributes.nodeType === "root") {
                    $('#mainMenu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                } else if (node.attributes.nodeType === "bom") {
                    $('#treeMenu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                } else if (node.attributes.nodeType === "version") {
                    var item = $('#treeMenuVersion').menu('findItem', '提交审核');
                    var item1 = $('#treeMenuVersion').menu('findItem', '修改');
                    var item2 = $('#treeMenuVersion').menu('findItem', '删除');
                    var item3 = $('#treeMenuVersion').menu('findItem', '工艺变更');
                    if (node.attributes.AUDITSTATE === 1) {
                        $('#treeMenu').menu('hideItem', item.target);
                        $('#treeMenu').menu('hideItem', item1.target);
                        $('#treeMenu').menu('hideItem', item2.target);
                        $('#treeMenu').menu('hideItem', item3.target);
                    } else if (node.attributes.AUDITSTATE === 2) {
                        $('#treeMenu').menu('hideItem', item.target);
                        $('#treeMenu').menu('hideItem', item1.target);
                        $('#treeMenu').menu('hideItem', item2.target);
                        $('#treeMenu').menu('showItem', item3.target);
                    } else {
                        $('#treeMenu').menu('showItem', item.target);
                        $('#treeMenu').menu('showItem', item1.target);
                        $('#treeMenu').menu('showItem', item2.target);
                        $('#treeMenu').menu('hideItem', item3.target);
                        //item.hidden="true";
                    }
                    $('#treeMenuVersion').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            },
            onBeforeExpand: function (node, param) {
                $('#FTcBomTree').tree('options').url = ftcBomtreeUrl + "?nodeType=" + node.attributes.nodeType;
            },
            onClick: function (node) {
                if (node.attributes.nodeType === "version") {
                    $("#dg").datagrid({
                        url: encodeURI(dgList + "?filter[id]=" + node.attributes.ID)
                    });
                } else {
                    $("#dg").datagrid('loadData', {
                        total: 0,
                        rows: []
                    });
                }
                addindex = 0;
                //树列表展开/收缩
                if (node.state === "open") {
                    collapse();
                } else {
                    expand();
                }
            }
        });
        $('#FTcBomTree').tree('expand', $('#FTcBomTree').tree('getRoot').target);
    });

    function findFtcBom() {
        isgetTestPro();
        $('#FTcBomTree').tree({
            url: ftcBomtreeUrl,
            data: data,
            method: 'get',
            animate: true,
            formatter: auditTreeStyler,
            onContextMenu: function (e, node) {
                e.preventDefault();
                $(this).tree('select', node.target);
                if (node.attributes.nodeType === "root") {
                    $('#mainMenu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                } else if (node.attributes.nodeType === "bom") {
                    $('#treeMenu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                } else if (node.attributes.nodeType === "version") {
                    var item = $('#treeMenuVersion').menu('findItem', '提交审核');
                    var item1 = $('#treeMenuVersion').menu('findItem', '修改');
                    var item2 = $('#treeMenuVersion').menu('findItem', '删除');
                    var item3 = $('#treeMenuVersion').menu('findItem', '工艺变更');
                    if (node.attributes.AUDITSTATE == 1) {
                        $('#treeMenu').menu('hideItem', item.target);
                        $('#treeMenu').menu('hideItem', item1.target);
                        $('#treeMenu').menu('hideItem', item2.target);
                        $('#treeMenu').menu('hideItem', item3.target);
                    } else if (node.attributes.AUDITSTATE === 2) {
                        $('#treeMenu').menu('hideItem', item.target);
                        $('#treeMenu').menu('hideItem', item1.target);
                        $('#treeMenu').menu('hideItem', item2.target);
                        $('#treeMenu').menu('showItem', item3.target);
                    } else {
                        $('#treeMenu').menu('showItem', item.target);
                        $('#treeMenu').menu('showItem', item1.target);
                        $('#treeMenu').menu('showItem', item2.target);
                        $('#treeMenu').menu('hideItem', item3.target);
                        //item.hidden="true";
                    }
                    $('#treeMenuVersion').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            },
            onBeforeExpand: function (node, param) {
                $('#FTcBomTree').tree('options').url = ftcBomtreeUrl + "?nodeType=" + node.attributes.nodeType;
            },
            onClick: function (node) {
                if (node.attributes.nodeType === "version") {
                    $("#dg").datagrid({
                        url: encodeURI(dgList + "?filter[id]=" + node.attributes.ID)
                    });
                } else {
                    $("#dg").datagrid('loadData', {
                        total: 0,
                        rows: []
                    });
                }
                addindex = 0;
                //树列表展开/收缩
                if (node.state === "open") {
                    collapse();
                } else {
                    expand();
                }
            }
        });
    }

    /**
     *工艺变更
     */
    function forceEdit() {
        const node = $("#FTcBomTree").tree('getSelected');
        Dialog.confirm(function () {
            Loading.show();
            JQ.ajaxPost(path + "bom/modify", {id: node.id, type: "FTC"}, function () {
                Loading.hide();
                Tip.success("工艺变更中!");
                reload(false);
            }, function () {
                Loading.hide();
            });
        }, "确认变更?变更期间，销售将无法下达相关产品的订单！");
    }

    let cc = null;//导入Excel错误提示框
    //添加工艺BOM列表
    function appendit() {
        const wid = Dialog.open("添加非套材工艺BOM", 600, 300, addUrl, [EasyUI.window.button("icon-save", "保存", function () {
            if (cc != null) {
                try {
                    cc.window('close');
                    cc = null;
                } catch (e) {
                    cc = null;
                }
            }
            EasyUI.form.submit("fTc_BomForm", addUrl, function (data) {
                if (data.excelErrorMsg) {
                    const c = '<div style="max-height:400px;">' + data.excelErrorMsg + '</div>';
                    cc = $.messager.show({
                        title: '导入Excel错误',
                        msg: c,
                        timeout: 0,
                        showType: 'fade'
                    });
                    return;
                } else if (!data.error) {
                    Tip.success("添加成功");
                }
                filter();
                reload(true);
                if (Dialog.isMore(wid)) {
                    Dialog.close(wid);
                    add();
                } else {
                    Dialog.close(wid);
                }
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            Dialog.more(wid);
        });
    }

    //编辑工艺BOM列表
    function editit() {
        const t = $('#FTcBomTree');
        const node = t.tree('getSelected');
        const wid = Dialog.open("编辑", 600, 200, editUrl + "?id=" + node.id, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("fTc_BomForm", editUrl, function (data) {
                if (Tip.hasError(data)) {
                    Loading.hide();
                    return;
                }
                Dialog.close(wid);
                //reload(false);
                reload1();
                searchInfo();
                Dialog.close(wid);
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })]);
    }

    //移除工艺BOM列表
    function removeit() {
        const node = $('#FTcBomTree').tree('getSelected');
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: node.id
            }, function (data) {
                console.log(data.error);
                if (data.error != null) {
                    return;
                }
                $('#FTcBomTree').tree('remove', node.target);
            });
        }, '移除该工艺，将会删除该工艺下的所有版本及明细，确认删除？');
    }

    //添加BOM版本
    function appenditVersion() {
        const t = $('#FTcBomTree');
        const node = t.tree('getSelected');
        JQ.ajax(findV, "post", {
            id: node.id
        }, function (data) {
            if (data === 1) {
                Tip.warn("此bom下存在版本无法添加");
                return;
            }
            const wid = Dialog.open("添加", 600, 100, addVersionUrl, [EasyUI.window.button("icon-save", "保存", function () {
                JQ.setValue("#ftcProcBomId", node.id);
                EasyUI.form.submit("ftcBomVersionForm", addVersionUrl, function (data) {
                    filter();
                    reload(false);
                    if (Dialog.isMore(wid)) {
                        Dialog.close(wid);
                        add();
                    } else {
                        Dialog.close(wid);
                    }
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
                Dialog.more(wid);
            });
        });
    }

    //编辑BOM版本
    function edititVersion() {
        const t = $('#FTcBomTree');
        const node = t.tree('getSelected');
        if (node.attributes.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录不能编辑！");
            return;
        }
        const wid = Dialog.open("编辑", 600, 200, editVersionUrl + "?id=" + node.id, [EasyUI.window.button("icon-save", "保存", function () {
            if (cc != null) {
                try {
                    cc.window('close');
                    cc = null;
                } catch (e) {
                    cc = null;
                }
            }
            EasyUI.form.submit("ftcBomVersionForm", editVersionUrl, function (data) {
                if (data.excelErrorMsg) {
                    const c = '<div style="max-height:400px;">' + data.excelErrorMsg + '</div>';
                    cc = $.messager.show({
                        title: '导入Excel错误',
                        msg: c,
                        timeout: 0,
                        showType: 'fade'
                    });
                    return;
                } else if (!data.error) {
                    Tip.success("修改成功");
                }
                filter();
                Dialog.close(wid);
                reload(false);
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })]);
    }

    //删除BOM版本
    function removeitVersion() {
        const node = $('#FTcBomTree').tree('getSelected');
        if (node.attributes.AUDITSTATE > 0) {
            Tip.warn("审核中或审核通过的记录不能编辑！");
            return;
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteVersionUrl, "post", {
                ids: node.id
            }, function (data) {
                if (data.error != null) {
                    return;
                }
                $('#FTcBomTree').tree('remove', node.target);
            });
        }, '移除该版本，将会删除该版本下的所有明细，确认删除？');
    }

    //树列表收缩
    function collapse() {
        const node = $('#FTcBomTree').tree('getSelected');
        $('#FTcBomTree').tree('collapse', node.target);
    }

    //树列表展开
    function expand() {
        const node = $('#FTcBomTree').tree('getSelected');
        $('#FTcBomTree').tree('expand', node.target);
    }

    function expandAll() {
        $('#FTcBomTree').tree('expandAll');
    }

    function collapseAll() {
        $('#FTcBomTree').tree('collapseAll');
    }

    //数节点刷新
    function reload(isSelf) {
        isgetTestPro();
        const node = $('#FTcBomTree').tree('getSelected');
        if (isSelf) {
            $('#FTcBomTree').tree('reload', node.target);
        } else {
            const pNode = $('#FTcBomTree').tree('getParent', node.target);
            $('#FTcBomTree').tree('reload', pNode.target);
        }
    }

    function reload1() {
        isgetTestPro();
        const node = $("#FTcBomTree").tree("getParent", $('#FTcBomTree').tree('getSelected').target);
        if (node) {
            $('#FTcBomTree').tree('reload', node.target);
        } else {
            $('#FTcBomTree').tree('reload');
        }
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "fTc_BomSearchForm");
    }

    //结束datagrid行编辑
    function endEditing() {
        if (editIndex === undefined) {
            return true;
        }
        if ($('#dg').datagrid('validateRow', editIndex)) {
            $('#dg').datagrid('endEdit', editIndex);
            editIndex = undefined;
            addindex = 0;
            return true;
        } else {
            return false;
        }
    }

    //结束编辑
    function onEndEdit(index, row) {
        addindex = 0;
        const node = $('#FTcBomTree').tree('getSelected');
        const rowstr = {
            "id": row.ID,
            "ftcBomDetailName": row.FTCBOMDETAILNAME,
            "ftcBomDetailModel": row.FTCBOMDETAILMODEL,
            "ftcBomDetailItemNumber": row.FTCBOMDETAILITEMNUMBER,
            "ftcBomDetailWeightPerSquareMetre": row.FTCBOMDETAILWEIGHTPERSQUAREMETRE,
            "ftcBomDetailTotalWeight": row.FTCBOMDETAILTOTALWEIGHT,
            "ftcBomDetailReed": row.FTCBOMDETAILREED,
            "ftcBomDetailGuideNeedle": row.FTCBOMDETAILGUIDENEEDLE,
            "ftcBomDetailRemark": row.FTCBOMDETAILREMARK,
            "ftcBomVersionId": node.id
        };
        $.ajax({
            url: saveDetailUrl,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(rowstr),
            success: function (data) {
                $('#dg').datagrid('reload');
                filter();
                /*editIndex=undefined; */
            }
        });
    }

    //datagrid双击编辑事件
    function onDblClickCell(index, field) {
        const node = $('#FTcBomTree').tree('getSelected');
        if (node.attributes.AUDITSTATE > 0) {
            Tip.warn("该BOM版本为已审核或审核中，禁止编辑该版本下的明细！");
            return;
        }
        if (editIndex !== index) {
            if (endEditing()) {
                $('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
                var ed = $('#dg').datagrid('getEditor', {
                    index: index,
                    field: field
                });
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                editIndex = index;
            } else {
                setTimeout(function () {
                    $('#dg').datagrid('selectRow', editIndex);
                }, 0);
            }
        }
    }

    //datagrid单击结束编辑
    function onClickCell(index, field) {
        endEditing();
    }

    //datagrid行添加事件 添加明细
    function add() {
        const node = $('#FTcBomTree').tree('getSelected');
        if (node == null) {
            Tip.warn("请先选择BOM版本！");
        } else if (node.attributes.nodeType === "version") {
            const rowInfo = $("#dg").datagrid('getSelected');
            if (node.attributes.AUDITSTATE > 0) {
                Tip.warn("该BOM版本为已审核或审核中，禁止添加明细！");
                return;
            }
            if (addindex === 0) {
                $("#dg").datagrid('insertRow', {
                    index: addindex,
                    row: {}
                });
                $("#dg").datagrid('beginEdit', 0);
                //$("#dg").datagrid('selectRow',0);
                editIndex = 0;
                addindex = 1;
            }
        } else {
            Tip.warn("请先选择BOM版本！");
        }
    }

    //保存datagrid数据
    const doSave = function () {
        /* $("#dg").datagrid('endEdit',editIndex);
        addindex=0; */
        endEditing();
    };

    //删除datagrid数据
    const doDelete = function () {
        const node = $('#FTcBomTree').tree('getSelected');
        if (node.attributes.AUDITSTATE > 0) {
            Tip.warn("该BOM版本为已审核或审核中，禁止删除该版本下的明细！");
            return;
        }
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
            JQ.ajax(deleteDetailUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                addindex = 0;
                filter();
            });
        }, '确认删除该条明细？');
    };

    //BOM版本复制
    function copyVersion() {
        let node = $('#FTcBomTree').tree('getSelected');
        const t = $('#FTcBomTree');
        node = t.tree('getSelected');
        Dialog.confirm(function () {
            JQ.ajax(copyftcBomVersion, "post", {
                ids: node.id,
                name: node.attributes.FTCPROCBOMVERSIONCODE + "(复制)"
            }, function (data) {
                reload(false);
            });
        }, '确认复制？');
    }

    //选择客户
    function ChooseConsumer() {
        consumerWindow = Dialog.open("选择客户", 850, 450, chooseConsumer, [EasyUI.window.button("icon-save", "确认", function () {
            const r = EasyUI.grid.getOnlyOneSelected("_common_consumer_dg");
            $('#ftcProcBomConsumer').searchbox('setValue', r.CONSUMERNAME);
            JQ.setValue('#ftcProcBomConsumerId', r.ID);
            Dialog.close(consumerWindow);
        }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
            Dialog.close(consumerWindow);
        })]);
    }

    //选择客户双击事件
    function _common_consumer_dbClickRow(index, row) {
        $('#ftcProcBomConsumer').searchbox('setValue', row.CONSUMERNAME);
        JQ.setValue('#ftcProcBomConsumerId', row.ID);
        Dialog.close(consumerWindow);
    }

    //审核非套材BOM版本
    const doAudit = function () {
        const node = $('#FTcBomTree').tree('getSelected');
        const wid = Dialog.open("审核", 700, 120, _auditCommitUrl + "?id=" + node.id, [
            EasyUI.window.button("icon-ok", "提交审核", function () {
                EasyUI.form.submit("editAuditProduce", auditCommitUrl, function (data) {
                    filter();
                    reload(false);
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            const pNode = $('#FTcBomTree').tree('getParent', node.target);
            if (pNode.attributes.ISTESTPRO === 1) {
                $("#editAuditProduce #name").textbox("setValue", "常规非套材BOM版本审核，编号：" + pNode.attributes.FTCPROCBOMNAME + "/" + pNode.attributes.FTCPROCBOMCODE + "，版本: " + node.attributes.FTCPROCBOMVERSIONCODE);
            } else if (pNode.attributes.ISTESTPRO === -1) {
                $("#editAuditProduce #name").textbox("setValue", "变更试样非套材BOM版本审核，编号：" + pNode.attributes.FTCPROCBOMNAME + "/" + pNode.attributes.FTCPROCBOMCODE + "，版本: " + node.attributes.FTCPROCBOMVERSIONCODE);
            } else {
                $("#editAuditProduce #name").textbox("setValue", "新品试样非套材BOM版本审核，编号：" + pNode.attributes.FTCPROCBOMNAME + "/" + pNode.attributes.FTCPROCBOMCODE + "，版本: " + node.attributes.FTCPROCBOMVERSIONCODE);
            }
        });
    };

    let dialogId;
    //查看审核
    //常规产品
    const viewUrl = path + "audit/FTC/{id}/state";
    //变更试样
    const viewUrl1 = path + "audit/FTC1/{id}/state";
    //新品试样
    const viewUrl2 = path + "audit/FTC2/{id}/state";

    function view() {
        const node = $('#FTcBomTree').tree('getSelected');
        if (node == null)
            return;
        const pNode = $('#FTcBomTree').tree('getParent', node.target);
        if (pNode.attributes.ISTESTPRO === 1) {
            dialogId = Dialog.open("查看审核状态", 700, 400, viewUrl.replace("{id}", node.id), [EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
                $("#" + dialogId).dialog("maximize");
            });
        } else if (pNode.attributes.ISTESTPRO === -1) {
            dialogId = Dialog.open("查看审核状态", 700, 400, viewUrl1.replace("{id}", node.id), [EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
                $("#" + dialogId).dialog("maximize");
            });
        } else {
            dialogId = Dialog.open("查看审核状态", 700, 400, viewUrl2.replace("{id}", node.id), [EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(dialogId);
            })], function () {
                $("#" + dialogId).dialog("maximize");
            });
        }
    }

    function searchInfo() {
        isgetTestPro();
        const t = $("#searchInput").searchbox("getText");
        if (t !== "") {
            $('#FTcBomTree').tree({
                url: ftcBomtreeUrl + "?nodeType=root&data=" + t.toString(),
                data: data,
                animate: true,
                onBeforeLoad: function (node, param) {
                    if (isFirst) {
                        isFirst = false;
                        return false;
                    } else {
                        return true;
                    }
                },
                formatter: auditTreeStyler,
                onContextMenu: function (e, node) {
                    e.preventDefault();
                    $(this).tree('select', node.target);
                    if (node.attributes.nodeType === "root") {
                        $('#mainMenu').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    } else if (node.attributes.nodeType === "bom") {
                        $('#treeMenu').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    } else if (node.attributes.nodeType === "version") {
                        const item = $('#treeMenuVersion').menu('findItem', '提交审核');
                        const item1 = $('#treeMenuVersion').menu('findItem', '修改');
                        const item2 = $('#treeMenuVersion').menu('findItem', '删除');
                        const item3 = $('#treeMenuVersion').menu('findItem', '工艺变更');
                        if (node.attributes.AUDITSTATE === 1) {
                            $('#treeMenu').menu('hideItem', item.target);
                            $('#treeMenu').menu('hideItem', item1.target);
                            $('#treeMenu').menu('hideItem', item2.target);
                            $('#treeMenu').menu('hideItem', item3.target);
                        } else if (node.attributes.AUDITSTATE === 2) {
                            $('#treeMenu').menu('hideItem', item.target);
                            $('#treeMenu').menu('hideItem', item1.target);
                            $('#treeMenu').menu('hideItem', item2.target);
                            $('#treeMenu').menu('showItem', item3.target);
                        } else {
                            $('#treeMenu').menu('showItem', item.target);
                            $('#treeMenu').menu('showItem', item1.target);
                            $('#treeMenu').menu('showItem', item2.target);
                            $('#treeMenu').menu('hideItem', item3.target);
                            //item.hidden="true";
                        }
                        $('#treeMenuVersion').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    }
                },
                onBeforeExpand: function (node, param) {
                    $('#FTcBomTree').tree('options').url = ftcBomtreeUrl + "?nodeType=" + node.attributes.nodeType;
                },
                onClick: function (node) {
                    if (node.attributes.nodeType === "version") {
                        $("#dg").datagrid({
                            url: encodeURI(dgList + "?filter[id]=" + node.attributes.ID)
                        });
                    } else {
                        $("#dg").datagrid('loadData', {
                            total: 0,
                            rows: []
                        });
                    }
                    addindex = 0;
                    //树列表展开/收缩
                    if (node.state === "open") {
                        collapse();
                    } else {
                        expand();
                    }
                }
            });
        }
        //$('#FTcBomTree').tree('expand',$('#FTcBomTree').tree('getRoot').target);
    }

    function setDefult(type, state) {
        const node = $('#FTcBomTree').tree('getSelected');
        JQ.ajax(path + "bom/setDefult", "post", {
            'type': type,
            'defultType': state,
            'id': node.id
        }, function (data) {
            Tip.success("设置成功");
            reload(false);
        });
    }

    function setEnableState(type, state) {
        const node = $('#FTcBomTree').tree('getSelected');
        JQ.ajax(path + "bom/state", "post", {
            'type': type,
            'state': state,
            'id': node.id
        }, function (data) {
            Tip.success("设置成功");
            reload(false);
        });
    }
</script>