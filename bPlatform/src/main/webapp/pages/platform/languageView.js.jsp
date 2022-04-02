<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加客户管理
    const addUrl = path + "language/add";
    //编辑客户管理
    const editUrl = path + "language/edit";
    //删除客户管理
    const deleteUrl = path + "language/delete";

    const width = 650, height = 450;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "consumerSearchForm");
    }

    function formatterLevel(value, row) {
        console.log(value);
        const s = "< spring" + ":message code=\"" + value + "\" />";
        return s;
    }


    //添加客户管理
    const add = function () {
        const wid = Dialog.open("<spring:message code="add" />", width, height, addUrl, [
                EasyUI.window.button("icon-save", "<spring:message code="save" />", function () {
                    EasyUI.form.submit("consumerForm", addUrl, function (data) {
                        filter();
                        if (Dialog.isMore(wid)) {
                            Dialog.close(wid);
                            add();
                        } else {
                            Dialog.close(wid);
                        }
                    })
                }), EasyUI.window.button("icon-cancel", "<spring:message code="Cancel" />", function () {
                    Dialog.close(wid)
                })], function () {
                Dialog.more(wid);
            }
        );
    };

    //编辑客户管理
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("<spring:message code="edit" />", width, height, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "<spring:message code="save" />", function () {
                EasyUI.form.submit("consumerForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "<spring:message code="Cancel" />", function () {
                Dialog.close(wid)
            })]);
    };

    //删除客户管理
    const doDelete = function () {
        let r = EasyUI.grid.getSelections("dg");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }

        let ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(deleteUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("<spring:message code="edit" />", width, height, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "<spring:message code="save" />", function () {
                EasyUI.form.submit("consumerForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "<spring:message code="Cancel" />", function () {
                Dialog.close(wid)
            })]);
    };

    const enableFilter = false;

    function dgLoadSuccess(data) {
        if (enableFilter) return;
        $(this).datagrid("enableFilter");
        $(".datagrid-filter[name='CONSUMERCATEGORY']").parent().remove();
    }
</script>