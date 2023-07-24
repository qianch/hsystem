<!--
作者:高飞
日期:2016-10-12 11:06:09
页面:原料JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加原料
    const addUrl = path + "material/add";
    //编辑原料
    const editUrl = path + "material/edit";
    //删除原料
    const deleteUrl = path + "material/delete";
    const height = 400;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "materialSearchForm");
    }

    //添加原料
    const add = function () {
        const wid = Dialog.open("添加", 700, height, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("materialForm", addUrl, function (data) {
                        filter();
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
    };

    const stockFormatter = function (value, row, index) {
        if (value < row.MATERIALMINSTOCK || value > row.MATERIALMAXSTOCK) {
            return "<font color=red>" + value + "</font>";
        }
        return value;
    };

    //编辑原料
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", 700, height, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //验证最大库存
    const changeMax = function (newValue, oldValue) {
        const min = $("#materialMinStock").val();
        if (newValue < parseFloat(min)) {
            Tip.warn("最大库存不能小于最小库存");
            $("#materialMaxStock").numberbox('setValue', '');
        }
    };
    //验证最小库存
    const changeMin = function (newValue, oldValue) {
        const max = $("#materialMaxStock").val();
        if (newValue > parseFloat(max)) {
            Tip.warn("最小库存不能大于最大库存");
            $("#materialMinStock").numberbox('setValue', '');
        }
    };
    //验证上偏差
    const deviation = function (newValue, oldValue) {
        const low = $("#lowerDeviation").val();
        const up = $("#deviation").val();
        if (parseInt(up) < parseInt(low)) {
            Tip.warn("上偏差不能小于下偏差");
            $("#deviation").numberspinner('setValue', '');
        }
    };
    //验证下偏差
    const lowerDeviation = function (newValue, oldValue) {
        const low = $("#lowerDeviation").val();
        const up = $("#deviation").val();
        if (parseInt(low) > parseInt(up)) {
            Tip.warn("下偏差不能大于上偏差");
            $("#lowerDeviation").numberspinner('setValue', '');
        }
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", 700, height, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("materialForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除原料
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
        });
    };
</script>