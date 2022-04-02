<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script type="text/javascript">
    const tree = null;
    const treeListUrl = path + "dict/list";
    const dictAdd = path + "dict/add";
    const dictDelete = path + "dict/delete";
    const dictEdit = path + "dict/edit";
    let pid = null;
    const treeName = "NAME_ZH_CN";
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
                pIdKey: "PID"
            },
            key: {
                name: treeName
            }
        },
        callback: {
            onDrop: onDrop,
            onClick: onClick
        }
    };

    $(document).ready(function () {
        JQ.ajaxGet(treeListUrl + "?all=1", function (data) {
            ZTree.init("dictTree", setting, data.rows, true);
        });
    });

    function onDrop() {

    }

    function expandAll(action) {
        if (action == 1) {
            ZTree.expand("dictTree", true);
        } else {
            ZTree.expand("dictTree", false);
        }
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
        EasyUI.grid.search("dg", "dictFilter");
    }

    function onClick(event, treeId, treeNode) {
        pid = treeNode.ID;
        JQ.setValue("#pid", pid);
        EasyUI.grid.search("dg", "dictFilter");
    }

    //删除字典
    const doDelete = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.length == 0)
            return;
        if (r.FIXED === 0) {
            Tip.warn("<spring:message code="Dict.NoDelete" />");
            return;
        }
        Dialog.confirm(function () {
            const ids = [];
            const treeNode = ZTree.getNode("dictTree", r.ID);
            getChildren(ids, treeNode);
            JQ.ajax(dictDelete, "post", {
                ids: ids.toString()
            }, function (data) {
                EasyUI.grid.search("dg", "dictFilter");
                ZTree.removeNode("dictTree", r.ID);
            });
        });
    };

    const unselect = function () {
        $.fn.zTree.getZTreeObj("dictTree").cancelSelectedNode();
        pid = null;
        JQ.setValue("#pid", "");
        EasyUI.grid.search("dg", "dictFilter");
    };

    const isDeprecated = function (value, row, index) {
        if (value == 0) {
            return "<spring:message code="Dict.Enable" />";
        } else {
            return "<spring:message code="Dict.Deprecated" />";
        }
    };


    const add = function () {
        const node = ZTree.getNode("dictTree", pid);
        if (node != null && node.DEPRECATED === 1) {
            Tip.warn("[" + node[treeName] + "] <spring:message code="Dict.HasDeprecated" />");
            return;
        }
        const wid = Dialog.open("<spring:message code="Button.Add" />", 400, 241, dictAdd + (pid == null ? "" : ("?pid=" + pid)), [EasyUI.window.button("icon-save", "<spring:message code="Button.Save" />", function () {
            EasyUI.form.submit("dictForm", dictAdd, function (data) {
                if (Tip.hasError(data)) {
                    Tip.error(data.error);
                    return;
                }
                //向树添加节点
                ZTree.addNode("dictTree", pid, data);
                EasyUI.grid.search("dg", "dictFilter");
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
            Dialog.close(wid)
        })]);
    };

    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r.length == 0)
            return;

        const wid = Dialog.open("<spring:message code="Button.Edit" />", 400, 241, dictEdit + "?id=" + r.ID, [EasyUI.window.button("icon-save", "<spring:message code="Button.Save" />", function () {
            //遍历子节点，修改弃用/启用状态
            const ids = [];
            const treeNode = ZTree.getNode("dictTree", r.ID);
            getChildren(ids, treeNode);
            JQ.setValue("#children", ids.toString());
            EasyUI.form.submit("dictForm", dictEdit, function (data) {
                if (Tip.hasError(data)) {
                    Tip.error(data.error);
                    return;
                }
                if (treeNode.DEPRECATED !== data.DEPRECATED) {
                    location.reload();
                }
                ZTree.updateNode("dictTree", "ID", data.ID, [treeName, "DEPRECATED"], [data[treeName], data["DEPRECATED"]]);
                EasyUI.grid.search("dg", "dictFilter");
                Dialog.close(wid);
            });
        }), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function () {
            Dialog.close(wid)
        })]);
    };

    function deprecatedStyle(index, row) {
        if (row.DEPRECATED === 1) {
            return "background-color:#c7c7c7;";
        }
    }
</script>