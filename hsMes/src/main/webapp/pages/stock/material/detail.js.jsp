<!--
作者:高飞
日期:2016-10-12 11:06:09
页面:原料JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    function resizeDg(data) {
        $('#dg').datagrid('resize', {height: 'auto'})
    }

    //查询
    function filter() {
        EasyUI.grid.search("dg", "materialSearchForm");
    }

    $(function () {
        $("#dg").datagrid({
            url: "${path}material/detail/list",
            onBeforeLoad: dgOnBeforeLoad,
        })
    })
    /**
     * 行统计
     */
    var flg = true;

    function onLoadSuccess(data) {
        if (flg) {
            appendRow();
        }
        flg = true;
    }

    /**
     * 表格末尾追加统计行
     */
    var flg = true;

    function appendRow(data) {
        if (flg) {
            appendRow();
        }
        flg = true;
    }

    function appendRow() {
        $("#dg").datagrid('appendRow', {
            PRODUCECATEGORY: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
            WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + computes("WEIGHT") + '</span>'	//月末累计数量统计
        });
        flg = false;
    }

    /**
     * 指定列求和
     */
    function computes(colName) {
        var rows = $("#dg").datagrid("getRows");
        var totals = 0;
        for (var i = 0; i < rows.length; i++) {
            totals += parseFloat(rows[i][colName]);
        }
        return totals.toFixed(2);
    }

    //是否放行
    function isPassFormatter(value, row, index) {
        if (value === 0) {
            return "正常";
        } else if (value === 1) {
            return "放行";
        }
    }

    //库存状态
    function stockStateFormatter(value, row, index) {
        if (value === 0) {
            return "在库";
        } else if (value === 1) {
            return "不在库";
        }
    }

    //k3同步状态
    function syncStateFormatter(value, row, index) {
        if (value === 0) {
            return "未同步";
        } else {
            return "已同步";
        }
    }

    //是否冻结
    function isLockFormatter(value, row, index) {
        if (value === 0) {
            return "正常";
        } else if (value === 1) {
            return "冻结";
        }
    }

    //状态
    function stateFormatter(value, row, index) {
        if (value === 0) {
            return "<font color='#b1610c'>待检</font>";
        } else if (value === 1) {
            return "<font color=green>合格</font>";
        } else if (value === 2) {
            return "<font color=red>不合格</font>";
        }
    }

    function TimeFormatter(value, row, index) {
        if (!value) return "";
        return new Calendar(value).format("yyyy-MM-dd HH:mm:ss");
    }

    //仓库
    function warehousecodeFormatter(value, row, index) {
        if (value === "yclk1") {
            return "原材料库1";
        } else if (value === "yclk2") {
            return "原材料库2";
        } else if (value === "yclk3") {
            return "原材料库3";
        } else if (value === "yclk4") {
            return "原材料库4";
        }
    }

    const actionUrl = path + "material/action";
    const gradeUrl = path + "material/grade";

    //物料判级
    function sentenceLevel() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        const wid = Dialog.open("设置", 400, 200, gradeUrl, [
            EasyUI.window.button("icon-save", "保存", function () {
                var state = $("#state").combobox("getValue");
                if (state === "undefined" || state == null || state === "") {
                    Tip.warn("请选择物料等级");
                    return;
                }
                JQ.ajax(actionUrl, "post", {
                    mssId: ids.toString(),
                    action: "state",
                    actionValue: state
                }, function (data) {
                    filter();
                    Dialog.close(wid)
                })
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid)
            })]);
    }

    //冻结
    function lock() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].ISLOCKED === 1) {
                Tip.warn("有些原料已经被冻结，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(actionUrl, "post", {
                mssId: ids.toString(),
                action: "isLocked",
                actionValue: 1
            }, function (data) {
                filter();
            });
        });
    }

    //解冻
    function unlock() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            console.log(r[i]);
            if (r[i].ISLOCKED === -1 || r[i].ISLOCKED === 0
                || r[i].ISLOCKED === undefined) {
                Tip.warn("有些原料为正常状态，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }

        Dialog.confirm(function () {
            JQ.ajax(actionUrl, "post", {
                mssId: ids.toString(),
                action: "isLocked",
                actionValue: 0
            }, function (data) {
                filter();
            });
        });
    }

    //紧急放行
    function ispass() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            if (r[i].ISPASS === 1) {
                Tip.warn("有些原料已经被放行，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(actionUrl, "post", {
                mssId: ids.toString(),
                action: "isPass",
                actionValue: 1
            }, function (data) {
                filter();
            });
        });
    }

    //取消放行
    function unpass() {
        const r = EasyUI.grid.getSelections("dg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            console.log(r[i]);
            if (r[i].ISPASS === -1 || r[i].ISPASS === 0 || r[i].ISPASS === undefined) {
                Tip.warn("有些原料不是放行状态，请重新筛选");
                return;
            } else
                ids.push(r[i].ID);
        }

        Dialog.confirm(function () {
            JQ.ajax(actionUrl, "post", {
                mssId: ids.toString(),
                action: "isPass",
                actionValue: 0
            }, function (data) {
                filter();
            });
        });
    }

    function devationFormatter(v, r, i) {
        if (!v) return "";
        return r.LOWERDEVIATION + "~" + r.UPPERDEVIATION;
    }

    function realDevationFormatter(v, r, i) {
        if (!v) return "";
        return r.REALLOWERDEVIATION + "~" + r.REALUPPERDEVIATION;
    }

    function TimeFormatter2(v, r, i) {
        if (v === undefined) return '';
        return new Calendar(v).format("yyyy-MM-dd");
    }

    //原料库存导出
    function detailExport() {
        location.href = encodeURI(path + "material/detailExport?" + JQ.getFormAsString("materialSearchForm"));
    }
</script>