$(document).ready(function () {
    laydate({
        elem: '#endcreateTime',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        istime: true
    });
    laydate({
        elem: '#startcreateTime',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        istime: true
    });
});

//添加调度任务
function add() {
    const wid = Dialog.open("新增", 650, 310, path + "schedule/add", [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("schedule", path + "schedule/add", function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    EasyUI.grid.search("dg", "scheduleFilter");
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

//编辑调度任务
const edit = function () {
    const r = EasyUI.grid.getOnlyOneSelected("dg");
    if (r == null) {
        Tip.warn("请选择要编辑的任务");
        return;
    }
    const wid = Dialog.open("编辑", 650, 310, path + "schedule/edit?id=" + r.ID, [
        EasyUI.window.button("icon-save", "保存", function () {
            EasyUI.form.submit("schedule", path + "schedule/edit", function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                EasyUI.grid.search("dg", "scheduleFilter");
                Dialog.close(wid);
                /*$('#dg').datagrid('reload', {
                    "filter[pid]" : (pid == null ? "" : pid)
                });
                var node = $.fn.zTree.getZTreeObj("deptTree").getNodeByParam("id", data.id, null);
                node.name = data.name;
                node.code = data.code;
                $.fn.zTree.getZTreeObj("deptTree").updateNode(node);*/
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid)
        })
    ]);
};

//删除调度任务
function deleteSchedule() {
    const rows = $('#dg').datagrid('getSelected');
    if (rows == null) {
        Tip.warn("请选择要删除的任务");
        return;
    }

    if (rows.STATUS != 3) {
        Tip.warn("请先停止该任务");
        return;
    }
    Dialog.confirm(function () {
        $.ajax({
            url: path + "schedule/delete",
            type: "post",
            data: {id: rows.ID},
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                filter();
                Tip.success("删除成功");
            }
        });
    }, '确定删除?');
}

function pause() {
    const rows = $('#dg').datagrid('getSelected');
    if (rows == null) {
        Tip.warn("请选择要暂停的任务");
        return;
    }

    if (rows.STATUS === 3) {
        Tip.warn("该任务已停止，无法暂停");
        return;
    }

    Dialog.confirm(function () {
        $.ajax({
            url: path + "schedule/pause",
            type: "post",
            data: {id: rows.ID},
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                filter();
                Tip.success("暂停成功");
            }

        });
    }, '确定暂停?');
}

function stop() {
    const rows = $('#dg').datagrid('getSelected');
    if (rows == null) {
        Tip.warn("请选择要暂停的任务");
        return;
    }

    if (rows.STATUS == 3) {
        Tip.warn("该任务已经停止");
        return;
    }

    if (rows.STATUS == 1) {
        Tip.warn("该任务已经执行完毕");
        return;
    }

    Dialog.confirm(function () {
        $.ajax({
            url: path + "schedule/stop",
            type: "post",
            data: {id: rows.ID},
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                filter();
                Tip.success("任务已停止");
            }
        });
    }, '确定停止?');
}

function start() {
    const rows = $('#dg').datagrid('getSelected');
    if (rows == null) {
        Tip.warn("请选择要恢复的任务");
        return;
    }

    if (rows.STATUS == 0) {
        Tip.warn("任务已启动");
        return;
    }

    Dialog.confirm(function () {
        $.ajax({
            url: path + "schedule/start",
            type: "post",
            data: {id: rows.ID},
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                filter();
                Tip.success("启动成功");
            }
        });
    }, '确定启动?');
}

function openDialog(id) {
    let url;
    let title;

    if (action == "add") {
        url = path + "schedule/add";
        title = "新增";
    } else {
        url = path + "schedule/edit?id=" + id;
        title = "编辑";
    }

    layer.open({
        type: 2,
        maxmin: true,
        area: ['650px', '480px'],
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
                            filter();
                            layer.close(index);
                            Tip.success("保存成功");
                            $('#dg').datagrid('reload', {
                                "filter[pid]": (pid == null ? "" : pid)
                            });
                            if (action == "add") {
                                $.fn.zTree.getZTreeObj("deptTree").addNodes(pid == null ? null : $.fn.zTree.getZTreeObj("deptTree").getNodeByParam("id", pid, null), data, true);
                            } else {
                                var node = $.fn.zTree.getZTreeObj("deptTree").getNodeByParam("id", data.id, null);
                                node.name = data.name;
                                node.code = data.code;
                                $.fn.zTree.getZTreeObj("deptTree").updateNode(node);
                            }
                        }
                    });
                }
            }
        }
    });
}

function statusFormatter(value, row, index) {
    let status = "";
    value = value == undefined ? 3 : value;
    switch (value) {
        case 0:
            status = "执行中";
            break;
        case 1:
            status = "执行完毕";
            break;
        case 2:
            status = "暂停";
            break;
        case 3:
            status = "停止";
            break;
        default:
            status = "未知";
            break;
    }
    return status;
}

function filter() {
    EasyUI.grid.search("dg", "scheduleFilter");
}
