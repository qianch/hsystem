let pid = null;
let action = null;
let levelCount = null;
let ztree = null;

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
            pIdKey: "PARENTID"
        },
        key: {
            name: "NAME"
        }
    },
    callback: {
        onClick: onClick,
        beforeDrop: beforeDrop,
        onDrop: onDrop
    }
};

function onClick(event, treeId, treeNode) {
    pid = treeNode.ID;
    JQ.setValue("#pid", pid);
    levelCount = treeNode.level;// 菜单层级
    reload();
}

function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
    try {
        if (treeNodes[0].levelCount != targetNode.levelCount || moveType == "inner") {
            return false;
        }
    } catch (e) {
        console.log(e);
        return false;
    }
}

function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
    sort();
}

function sort() {
    const nodes = ztree.transformToArray(ztree.getNodes());
    const sort = [];
    $.each(nodes, function (i, node) {
        sort.push({
            "id": node.ID,
            "sortOrder": i
        });
    });
    $.ajax({
        url: path + "menu/sort",
        type: "post",
        data: JSON.stringify(sort),
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
    $.ajax({
        url: path + "menu/list?all=1",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (Tip.hasError(data)) {
                return;
            }
            ztree = ZTree.init("menuTree", setting, data.rows, true);
        }
    });
});

function filter() {
    reload();
}

function add() {
    const nodes = ZTree.getSelectedNode("menuTree");
    for (let i = 0; i < nodes.length; i++) {
        console.log(nodes);
        if (nodes[i].ISBUTTON == 1) {
            Tip.warn("按钮下面无法添加节点");
            return;
        }
    }
    if (pid == null && nodes.length > 0) {
        Tip.warn("请选择一个父节点");
        return;
    }
    action = "add";
    openDialog();
}

function edit() {
    action = "edit";
    const rows = $('#dg').datagrid('getSelections');
    if (rows.length > 1) {
        Tip.warn('只能编辑一个菜单信息');
        return;
    }
    if (rows.length == 0) {
        Tip.warn('请选择要编辑的菜单');
        return;
    }
    openDialog(rows[0].ID);
}

function deleteMenu() {
    const rows = $('#dg').datagrid('getSelections');

    if (rows.length == 0) {
        Tip.warn('请选择要删除的菜单');
        return;
    }
    const list = [];
    for (let i = 0; i < rows.length; i++) {
        list.push({
            id: rows[i].ID
        });
    }

    layer.confirm('确定删除?', {
        icon: 3,
        title: '警告',
        btn: ['删除', '取消'], // 按钮
    }, function (index) {// 第一个按钮
        const ids = [];
        const rows = $('#dg').datagrid('getSelections');
        for (let i = 0; i < rows.length; i++) {
            ids.push({
                id: rows[i].ID
            });
        }

        $.ajax({
            url: path + "menu/delete",
            type: "delete",
            data: JSON.stringify(list),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success('删除成功');
                for (let i = 0; i < rows.length; i++) {
                    ZTree.removeNode("menuTree", rows[i].ID);
                }
                reload();
            }
        });
        layer.close(index);
    });

}

function isButtonFormatter(value, row, index) {
    return value == 0 ? '否' : '是';
}

function iconFormatter(value, row, index) {
    return "<a class='icon-icon " + value + "'></a>";
}

function openDialog(id) {
    let url;
    let title;

    if (action == "add") {
        url = path + "menu/add?parentId=" + (pid == null ? '' : pid) + "&levelCount=" + (levelCount == null ? '' : (levelCount + 1));
        title = "新增";
    } else {
        url = path + "menu/edit?id=" + id;
        title = "编辑";
    }

    layer.open({
        type: 2,
        maxmin: true,
        area: ['450px', '400px'],
        title: title,
        btn: ['保存', '取消'],
        skin: 'layui-layer-molv', // 墨绿风格
        content: url,
        yes: function (index, layero) {
            const valid = window.frames["layui-layer-iframe" + index].valid;
            const form = window.frames["layui-layer-iframe" + index].form;
            if (valid.form()) {
                if (form != undefined) {
                    form.ajaxSubmit({
                        dataType: "json",
                        success: function (data) {
                            if (Tip.hasError(data)) {
                                return;
                            }
                            layer.close(index);
                            Tip.success("保存成功");

                            if (action == "add") {
                                ZTree.addNode("menuTree", pid, data);
                                sort(ZTree.getNode("menuTree", data.ID));
                            } else {
                                ZTree.updateNode("menuTree", "ID", data.ID, ["NAME", "CODE", "ISBUTTON"], [data.NAME, data.CODE, data.ISBUTTON]);
                            }
                            reload();
                        },
                        error: function (data) {
                            const rs = JSON.parse(data.responseText);
                            Tip.error(rs.error);
                        }
                    });
                }
            }
        }
    });
}

function reload() {
    EasyUI.grid.search("dg", "menuFilter");
}