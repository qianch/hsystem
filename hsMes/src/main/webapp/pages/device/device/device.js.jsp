<!--
作者:宋黎明
日期:2016-9-29 11:46:46
页面:设备信息JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //树
    const treeListUrl = path + "deviceType/list";
    //添加设备信息
    const addUrl = path + "device/add";
    //编辑设备信息
    const editUrl = path + "device/edit";
    //删除设备信息
    const deleteUrl = path + "device/delete";
    const tree = null;
    let pid = null;
    let id = null;
    const setting = {
        edit: {
            enable: true,
            showRemoveBtn: false,
            showRenameBtn: false
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "ID",
                pIdKey: "CATEGORYPARENTID"
            },
            key: {
                name: "CATEGORYNAME"
            }
        },
        callback: {
            onClick: onClick
        }
    };

    //初始化tree
    $(document).ready(function () {
        initTree();
    });

    function initTree() {
        $.ajax({
            url: treeListUrl + "?all=1",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                ZTree.init("deviceTree", setting, data.rows, true);
            }
        });
    }

    const getChildren = function (ids, treeNode) {
        ids.push(treeNode.ID);
        if (treeNode.isParent) {
            for (const obj in treeNode.children) {
                getChildren(ids, treeNode.children[obj]);
            }
        }
        return ids;
    };

    function filter() {
        EasyUI.grid.search("dg", "deviceSearchForm");
    }

    //树列表单击事件
    function onClick(event, treeId, treeNode) {
        pid = treeNode.PID;
        const ids = [];
        id = getChildren(ids, treeNode);
        JQ.setValue("#id", id);
        filter();
    }

    //添加设备信息
    const add = function () {
        const treeObj = $.fn.zTree.getZTreeObj("deviceTree");
        const sNodes = treeObj.getSelectedNodes();
        if (sNodes.length === 0) {
            Tip.warn("请先选择一个节点");
            return;
        }
        const wid = Dialog.open("添加", 380, 400, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    var did = $("#departmentSelect").combotree('getValue');
                    JQ.setValue("#deviceDepartmentId", did);
                    EasyUI.form.submit("deviceForm", addUrl + ("?deviceCatetoryId=" + sNodes[0].ID), function (data) {
                        if (Tip.hasError(data)) {
                            return;
                        }
                        filter();
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
                $.ajax({
                    url: path + "department/list?all=1",
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        if (Tip.hasError(data)) {
                            return;
                        }
                        $("#departmentSelect").combotree({data: EasyUI.tree.getTreeData(data.rows, "ID", "NAME", "PID")});
                    }
                });
            }
        );
    };

    //编辑设备信息
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", 380, 400, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                var did = $("#departmentSelect").combotree('getValue');
                JQ.setValue("#deviceDepartmentId", did);
                EasyUI.form.submit("deviceForm", editUrl, function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    filter();
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $.ajax({
                url: path + "department/list?all=1",
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    $("#departmentSelect").combotree({data: EasyUI.tree.getTreeData(data.rows, "ID", "NAME", "PID")});
                }
            });
        });
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", 380, 400, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                const did = $("#departmentSelect").combotree('getValue');
                JQ.setValue("#deviceDepartmentId", did);
                EasyUI.form.submit("deviceForm", editUrl, function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    filter();
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $.ajax({
                url: path + "department/list?all=1",
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    $("#departmentSelect").combotree({data: EasyUI.tree.getTreeData(data.rows, "ID", "NAME", "PID")});
                }
            });
        });
    };

    //删除设备信息
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
        }, '确认删除该条设备信息记录？');
    };
</script>