<!--
作者:宋黎明
日期:2016-11-16 13:44:47
页面:成品出库记录表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加成品出库记录表
    const addUrl = path + "productOutRecord/add";
    //编辑成品出库记录表
    const editUrl = path + "productOutRecord/edit";
    //删除成品出库记录表
    const deleteUrl = path + "productOutRecord/delete";
    const dialogWidth = 700, dialogHeight = 350;

    Date.prototype.Format = function (fmt) { //author: meizz
        const o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (const k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    $(function () {
        const time1 = new Date().Format("dd/MM/yyyy");
        const time2 = time1 + " " + "0:00:00";
        $("#start").datetimebox("setValue", time2);
        const time4 = new Date().Format("yyyy-MM-dd");
        const time3 = time4 + " " + "23:59:59";
        $("#end").datetimebox("setValue", time3);
        filter();
        $('#dg').datagrid({
            url: "${path}stock/productOutRecord/list",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

    //查询
    function filter() {
        EasyUI.grid.search("dg", "productOutRecordSearchForm");
    }

    //添加成品出库记录表
    const add = function () {
        const wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
                EasyUI.window.button("icon-save", "保存", function () {
                    EasyUI.form.submit("productOutRecordForm", addUrl, function (data) {
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

    //编辑成品出库记录表
    const edit = function () {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("productOutRecordForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    /**
     * 双击行，弹出编辑
     */
    const dbClickEdit = function (index, row) {
        const wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("productOutRecordForm", editUrl, function (data) {
                    filter();
                    Dialog.close(wid);
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    };

    //删除成品出库记录表
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

    //导出
    function productExport() {
        location.href = encodeURI(path + "stock/productOutRecord/export?" + JQ.getFormAsString("productOutRecordSearchForm"));
    }
</script>