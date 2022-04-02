let url;
let treeNode = null;
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
        onClick: zTreeOnClick
    }
};

let did = null;

function zTreeOnClick(event, treeId, treeNode) {
    did = treeNode.ID;
    const ids = [];
    id = getChildren(ids, treeNode);
    JQ.setValue("#did", id);
    reload();
}

function getChildren(ids, treeNode) {
    if (treeNode.ID != undefined) {
        ids.push(treeNode.ID);
    }
    if (treeNode.isParent) {
        for (let obj in treeNode.children) {
            getChildren(ids, treeNode.children[obj]);
        }
    }
    return ids;
}

$(document).ready(function () {
    $.ajax({
        url: path + "department/list?all=1",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (Tip.hasError(data)) {
                return;
            }
            ZTree.init("zTreeUser", setting, data.rows, true);
            treeNode = ZTree.getNode("zTreeUser", did);
        }
    });

});

//添加用户人员信息
function addUser() {
    const wid = Dialog.open("新增", 422, 350, path + 'user/add?did=' + (did == null ? '' : did), [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("user", path + "user/add", function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    if (Dialog.isMore(wid)) {
                        Dialog.close(wid);
                        addUser();
                    } else {
                        Dialog.close(wid);
                    }
                    reload();
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
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
}

//编辑用户人员信息
function editUser() {
    const r = EasyUI.grid.getOnlyOneSelected("dg");
    if (r.length == 0)
        return;
    const wid = Dialog.open("编辑", 430, 355, path + 'user/edit?id=' + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("user", path + "user/edit", function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Dialog.close(wid);
                    reload();
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })
        ], function () {
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
}


//删除用户人员信息
function deleteUser() {
    const rows = $('#dg').datagrid('getSelections');
    const ids = [];
    for (let i = 0; i < rows.length; i++) {
        ids.push({
            id: rows[i].ID
        });
    }
    if (rows.length == 0)
        return;
    Dialog.confirm(function () {
        $.ajax({
            url: path + "user/delete",
            type: "delete",
            data: JSON.stringify(ids),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success("删除成功");
                reload()
            }
        });
    }, '确定删除?');
}

function filter() {
    reload();
}

function sexFormatter(value, row, index) {
    if (value === null || value === undefined || value === "") {
        return "";
    }
    return value === 0 ? '男' : '女';
}

function statusFormatter(value, row, index) {
    return value === 0 ? '启用' : '禁用';
}


//角色分配
function setRoles() {
    const rows = $('#dg').datagrid('getSelections');
    let id = "";
    if (rows.length == 1) {
        id = rows[0].ID;
    }

    if (rows.length == 0) {
        Tip.warn('请选择要编辑的人员');
        return;
    }

    const wid = Dialog.open("角色分配", 400, 400, path + 'user/role?id=' + id, [
        EasyUI.window.button("icon-save", "保存", function () {
            Loading.show();
            const uids = [];
            const rids = getSelected();
            const rs = $("#dg").datagrid("getSelections");
            for (let i = 0; i < rs.length; i++) {
                uids.push(rs[i].ID);
            }
            $.ajax({
                url: path + "user/role",
                type: "post",
                data: {
                    uids: uids.join(","),
                    rids: rids.join(",")
                },
                dataType: "json",
                success: function (data) {
                    Loading.hide();
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("保存成功");
                },
                error: function (data) {
                    Loading.hide();
                    var json = JSON.parse(data.responseText);
                    Tip.error(json.error);
                }
            });
            Dialog.close(wid)
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })
    ]);

}

//重置密码
function reset() {
    const rows = $('#dg').datagrid('getSelections');
    if (rows.length > 1) {
        Tip.warn('只能编辑一个人员信息');
        return;
    }
    if (rows.length == 0) {
        Tip.warn('请选择要重置密码的人员');
        return;
    }
    Dialog.confirm(function () {
        $.ajax({
            url: path + "user/reset",
            type: "post",
            data: {
                id: rows[0].ID
            },
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success("重置成功");
            }
        });
    }, '确定要重置密码吗?');

}

//启用禁用
function enable() {
    const rows = $('#dg').datagrid('getSelections');
    if (rows.length > 1) {
        Tip.warn('只能同时启用/禁用一个人员信息');
        return;
    }
    if (rows.length == 0) {
        Tip.warn('请选择要启用/禁用的人员');
        return;
    }

    let status = rows[0].STATUS;
    let title;
    const btn = null;

    if (status == 0) {
        status = -1;
        title = "确认禁用？";
    } else {
        status = 0;
        title = "确认启用？";
    }
    Dialog.confirm(function () {
        $.ajax({
            url: path + "user/enable",
            type: "post",
            data: {
                id: rows[0].ID,
                status: status
            },
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                reload();
                Tip.success("操作成功");
            }
        });
    }, title);

}

function reload() {
    let ids = [];
    if (treeNode == null) {
        EasyUI.grid.search("dg", "userFilter");
    } else {
        ids = getChildren(ids, treeNode);
        JQ.setValue("#did", ids.toString());
        EasyUI.grid.search("dg", "userFilter");
    }
}