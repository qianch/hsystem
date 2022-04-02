//添加系统公告
const noticeAdd = path + "notice/add";
//编辑系统公告
const noticeEdit = path + "notice/edit";
//删除系统公告
const noticeDelete = path + "notice/delete";

function filter() {
    EasyUI.grid.search("dg", "noticeFilter");
}

function closeDialog() {
    UE.getEditor('content').destroy();
}

//添加系统公告
function add() {
    const wid = Dialog.open("新增", 800, 450, noticeAdd + "?userId=" + userId, [
            EasyUI.window.button("icon-save", "保存", function () {
                UE.getEditor('content').ready(function () {
                    const txt = ue.getContentTxt();
                    JQ.setValue("#contentTxt", txt);
                });
                EasyUI.form.submit("noticeForm", noticeAdd, function (data) {
                    filter();
                    closeDialog();
                    if (Dialog.isMore(wid)) {
                        Dialog.close(wid);
                        add();
                    } else {
                        Dialog.close(wid);
                    }
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                closeDialog();
                Dialog.close(wid);
            })], function () {
            Dialog.more(wid);
        }, closeDialog
    );
}

//编辑系统公告
const edit = function () {
    const r = EasyUI.grid.getOnlyOneSelected("dg");
    if (r.length == 0)
        return;
    const wid = Dialog.open("编辑", 800, 450, noticeEdit + "?id=" + r.ID + "&userId=" + r.USERID, [
        EasyUI.window.button("icon-save", "保存", function () {
            UE.getEditor('content').ready(function () {
                const txt = ue.getContentTxt();
                JQ.setValue("#contentTxt", txt);
            });
            EasyUI.form.submit("noticeForm", noticeEdit, function (data) {
                filter();
                closeDialog();
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            closeDialog();
            Dialog.close(wid);
        })], null, closeDialog
    );
};

//删除系统公告
function doDelete() {
    const r = EasyUI.grid.getSelections("dg");
    if (r.length == 0) {
        Tip.warn("至少选择一行!");
        return;
    }
    const ids = [];
    for (let i = 0; i < r.length; i++) {
        ids.push(r[i].ID);
    }
    Dialog.confirm(function () {
        JQ.ajax(noticeDelete, "post", {
            ids: ids.toString()
        }, function (data) {
            filter();
        });
    });
}
