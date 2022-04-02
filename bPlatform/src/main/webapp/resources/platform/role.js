const action = null;

function filter() {
    EasyUI.grid.search("dg", "roleFilter");
}

//添加角色管理
function add() {
    const wid = Dialog.open("新增", 370, 200, path + "role/add", [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("role", path + "role/add", function (data) {
                    EasyUI.grid.search("dg", "roleFilter");
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
}

//编辑角色管理
function edit() {
    const r = EasyUI.grid.getOnlyOneSelected("dg");
    if (r.length == 0)
        return;
    const wid = Dialog.open("编辑", 370, 200, path + 'role/edit?id=' + r.ID, [
        EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("role", path + "role/edit", function (data) {
                EasyUI.grid.search("dg", "roleFilter");
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })]);
}

//删除角色管理
const deleteRole = function () {
    const rows = $('#dg').datagrid('getSelections');
    if (rows.length == 0) {
        Tip.warn("请选择要删除的角色");
        return;
    }

    const list = [];
    for (let i = 0; i < rows.length; i++) {
        list.push({id: rows[i].ID});
    }

    Dialog.confirm(function () {
        $.ajax({
            url: path + "role/delete",
            type: "delete",
            data: JSON.stringify(list),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success("删除成功");
                filter();
            }
        });
    }, '确定删除?');
};

//权限分配
const permission = function () {
    const rows = $('#dg').datagrid('getSelections');
    if (rows.length == 0) {
        Tip.warn("请选择一个角色");
        return;
    }
    const wid = Dialog.open("权限分配", 350, 430, path + "role/permission?id=" + rows[0].ID, [
        EasyUI.window.button("icon-save", "保存", function () {
            const tree = $.fn.zTree.getZTreeObj("role_permission");
            const nodes = tree.getCheckedNodes(true);
            const ids = [];
            for (let i = 0; i < nodes.length; i++) {
                ids.push(nodes[i].ID);
            }
            Loading.show();
            $.ajax({
                url: path + "role/permission",
                type: "post",
                data: {id: rows[0].ID, ids: ids.toString()},
                dataType: "json",
                success: function (data) {
                    Loading.hide();
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("保存成功");
                }, error: function () {
                    Loading.hide();
                }
            });
            Dialog.close(wid)
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })
    ]);
};
