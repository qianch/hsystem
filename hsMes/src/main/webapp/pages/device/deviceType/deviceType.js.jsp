<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script type="text/javascript">
    const tree = null;
    const treeListUrl = path + "deviceType/list";
    const deviceAdd = path + "deviceType/add";
    const deviceDelete = path + "deviceType/delete";
    const deviceEdit = path + "deviceType/edit";
    let categoryParentId = null;
    const setting = {
        edit: {
            enable: false,
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
            onDrop: onDrop,
            onClick: onClick
        }
    };

    function onDrop(event, treeId, treeNodes, targetNode, moveType) {
        const categoryParentId = targetNode == null ? null : targetNode.ID;
        const list = [];
        for (let i = 0; i < treeNodes.length; i++) {
            list.push({
                id: treeNodes[i].ID,
                categoryParentId: categoryParentId
            });
        }
        $.ajax({
            url: path + "deviceType/batchUpdateDeviceTypeLevel",
            type: "post",
            data: JSON.stringify(list),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
            }
        });
    }

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
        EasyUI.grid.search("dg", "deviceFilter");
    }

    function onClick(event, treeId, treeNode) {
        categoryParentId = treeNode.ID;
        JQ.setValue("#pid", categoryParentId);
        EasyUI.grid.search("dg", "deviceFilter");
    }

    const doDelete = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.length === 0)
            return;
        Dialog.confirm(function () {
            const ids = [];
            const treeNode = ZTree.getNode("deviceTree", r.ID);
            getChildren(ids, treeNode);
            JQ.ajax(deviceDelete, "post", {
                ids: ids.toString()
            }, function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                EasyUI.grid.search("dg", "deviceFilter");
                ZTree.removeNode("deviceTree", r.ID);
            });
        }, '确定删除?<br>删除的时候子类别也会一并删除!');
    };

    const add = function () {
        const node = ZTree.getNode("deviceTree", categoryParentId);
        if (node != null && node.DEPRECATED === 1) {
            Tip.warn("");
            return;
        }
        const wid = Dialog.open("增加", 400, 99, deviceAdd + (categoryParentId == null ? "" : ("?categoryParentId=" + categoryParentId)), [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("deviceForm", deviceAdd, function (data) {
                //向树添加节点
                ZTree.addNode("deviceTree", categoryParentId, data);
                EasyUI.grid.search("dg", "deviceFilter");
                initTree();
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
    };

    // 编辑设备类别
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.length === 0)
            return;
        const wid = Dialog.open("编辑", 400, 99, deviceEdit + "?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("deviceForm", deviceEdit, function (data) {
                console.log(data);
                ZTree.updateNode("deviceTree", "ID", data.ID, ["CATEGORY_NAME"], [data.CATEGORYNAME]);
                EasyUI.grid.search("dg", "deviceFilter");
                initTree();
                Dialog.close(wid);
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })]);
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", 400, 99, deviceEdit + "?id=" + row.ID, [EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("deviceForm", deviceEdit, function (data) {
                console.log(data);
                ZTree.updateNode("deviceTree", "ID", data.ID, ["CATEGORY_NAME"], [data.CATEGORYNAME]);
                EasyUI.grid.search("dg", "deviceFilter");
                initTree();
                Dialog.close(wid);
            });
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })]);
    };
</script>