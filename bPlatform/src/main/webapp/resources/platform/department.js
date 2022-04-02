let url;
let pid = null;
const action = null;
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
            pIdKey: "PID"
        },
        key: {
            name: "NAME"
        }
    },
    callback: {
        onDrop: onDrop,
        onClick: onClick
    }
};

function onClick(event, treeId, treeNode) {
    pid = treeNode.ID;
    JQ.setValue("#pid", pid);
    EasyUI.grid.search("dg", "departmentFilter");
}

function onDrop(event, treeId, treeNodes, targetNode, moveType) {
    const pid = targetNode == null ? null : targetNode.ID;
    const list = [];
    for (let i = 0; i < treeNodes.length; i++) {
        list.push({
            id: treeNodes[i].ID,
            pid: pid
        });
    }
    $.ajax({
        url: path + "department/batchUpdateDepartmentLevel",
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
};

$(document).ready(function () {
    $.ajax({
        url: path + "department/list?all=1",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (Tip.hasError(data)) {
                return;
            }
            ZTree.init("deptTree", setting, data.rows, true);
        }
    });
});

// 添加部门
const add = function () {
    const wid = Dialog.open("新增", 380, 300, path + "department/add?pid=" + (pid == null ? "" : pid), [EasyUI.window.button("icon-save", "保存", function () {
        EasyUI.form.submit("department", path + "department/add", function (data) {
            ZTree.addNode("deptTree", pid, data);
            EasyUI.grid.search("dg", "departmentFilter");
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
    });
};

// 编辑部门
function edit() {
    const r = EasyUI.grid.getOnlyOneSelected("dg");
    if (r.length == 0)
        return;
    const wid = Dialog.open("编辑", 380, 300, path + "department/edit?id=" + r.ID, [EasyUI.window.button("icon-save", "保存", function () {
        EasyUI.form.submit("department", path + "department/edit", function (data) {
            ZTree.updateNode("deptTree", "ID", data.ID, ["NAME", "CODE"], [data.NAME, data.CODE]);
            EasyUI.grid.search("dg", "departmentFilter");
            Dialog.close(wid);
        })
    }), EasyUI.window.button("icon-cancel", "关闭", function () {
        Dialog.close(wid)
    })]);
};

// 删除部门
function deleteDepartment() {
    const rows = $('#dg').datagrid('getSelections');
    if (rows.length == 0) {
        Tip.warn("请选择要删除的部门");
        return;
    }

    if (rows.length > 1) {
        Tip.warn("部门不提供批量删除");
        return;
    }

    const ids = [];
    const treeNode = $.fn.zTree.getZTreeObj("deptTree").getNodeByParam("ID", rows[0].ID, null);
    getChildren(ids, treeNode);
    Dialog.confirm(function () {
        $.ajax({
            url: path + "department/delete",
            type: "delete",
            data: JSON.stringify(ids),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success("删除成功");
                EasyUI.grid.search("dg", "departmentFilter");
                for (var i = 0; i < ids.length; i++) {
                    $.fn.zTree.getZTreeObj("deptTree").removeNode($.fn.zTree.getZTreeObj("deptTree").getNodeByParam("id", ids[i].id, null), false);
                    ZTree.removeNode("deptTree", ids[i].id);
                }
            }
        });
    }, '确定删除?<br>删除的时候子部门也会一并删除!<br>请确保部门下没有人员信息。');
}

function getChildren(ids, treeNode) {
    ids.push({
        "id": treeNode.ID
    });
    if (treeNode.isParent) {
        for (var i = 0; i < treeNode.children.length; i++) {
            getChildren(ids, treeNode.children[i]);
        }
    }
    return ids;
}

function filter() {
    $('#dg').datagrid('reload', JQ.getFormAsJson("departmentFilter"));
}

function importXlsx() {
    const wid = Dialog.open("导入Excel", 380, 300, path + "/excel/upload", [EasyUI.window.button("icon-save", "保存", function () {
    }), EasyUI.window.button("icon-cancel", "关闭", function () {
        Dialog.close(wid)
    })]);
}