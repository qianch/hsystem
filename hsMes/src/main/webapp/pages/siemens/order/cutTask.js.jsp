<!--
作者:高飞
日期:2017-7-26 10:56:16
页面:西门子裁剪任务单JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    $.extend($.fn.textbox.defaults, {
        "icons": []
    });
    $.extend($.fn.combobox.defaults, {
        "icons": []
    });
    $.extend($.fn.combotree.defaults, {
        "icons": []
    });
    $.extend($.fn.numberbox.defaults, {
        "icons": []
    });
    $.extend($.fn.searchbox.defaults, {
        "icons": []
    });
    //添加西门子裁剪任务单
    const addUrl = path + "cutTask/add";
    //编辑西门子裁剪任务单
    const editUrl = path + "cutTask/edit";
    //删除西门子裁剪任务单
    const deleteUrl = path + "cutTask/delete";
    const dialogWidth = 700, dialogHeight = 350;

    //查询
    function filter() {
        EasyUI.grid.search("dg", "cutTaskSearchForm");
    }

    //添加西门子裁剪任务单
    const enableTask = function () {
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        if (row == null) {
            return;
        }
        enableOrCloseTask(row.ID, 0);
    };

    //编辑西门子裁剪任务单
    const closeTask = function () {
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        if (row == null) {
            return;
        }
        enableOrCloseTask(row.ID, 1);
    };

    function enableOrCloseTask(id, action) {
        Dialog.confirm(function () {
            JQ.ajaxGet(path + "siemens/cutTask/close?id=" + id + "&closed=" + action, function (data) {
                filter();
            });
        }, "确认" + (action === 0 ? "启用" : "关闭") + "选中的任务单?");
    }

    function exportTask() {
        location.href = path + "excel/export/裁剪任务单/com.bluebirdme.mes.siemens.excel.CutTaskExportHandler/filter?" + JQ.getFormAsString("cutTaskSearchForm");
    }

    function ccFormat(v, r, i) {
        if (v === "1") {
            return "国内";
        }
        if (v === "2") {
            return "国外";
        }
    }

    function dateFormatter(v, r, i) {
        if (!v) return '';
        return v.substring(0, 10);
    }

    function completeFormatter(v, r, i) {
        if (v === undefined) return '';
        if (v === 0)
            return "<font color=red>未完成</font>";
        else
            return "<font color=red>已完成</font>";
    }

    const enableFilter = true;

    function onLoadSuccess(data) {

    }

    function styler(i, r) {
        if (r && r.ISCLOSED === "1")
            return "color:red";
        else
            return "color:#0012ff";
    }

    function closedFormatter(v, r, i) {
        if (v === undefined) return;
        if (v === 0)
            return "<font color=green>✔</font>";
        if (v === 1)
            return "<font color=red>✘</font>";
    }

    function resizeDg(data) {
        $('#dg').datagrid('resize', {height: 'auto'})
    }

    let groups =${empty groups?"[]":groups};

    function validCode() {
        const _options = $(this).combobox('options');
        const _data = $(this).combobox('getData');/* 下拉框所有选项 */
        const _value = $(this).combobox('getValue');/* 用户输入的值 */
        const _text = $(this).combobox('getText');
        let _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
        for (let i = 0; i < _data.length; i++) {
            if (_data[i][_options.valueField] === _value && _data[i][_options.textField] === _text) {
                _b = true;
                onGroupComboSelect({GROUPLEADER: _text}, this);
                break;
            }
        }
        if (!_b) {
            $(this).combobox('setValue', '');
            $("#ctoGroupName").textbox("setValue", "");
        }
    }

    function onGroupComboSelect(record) {
        for (let i = 0; i < groups.length; i++) {
            if (record.GROUPLEADER === groups[i].GROUPLEADER) {
                $("#ctoGroupName").textbox("setValue", groups[i].GROUPNAME);
                break;
            }
        }
    }

    function genTaskCard() {
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        if (row == null) {
            return;
        }
        Loading.show("正在获取单号，图纸信息");
        //获取单号
        $.ajax({
            url: path + "siemens/cutTaskOrder/info?ctId=" + row.ID,
            type: "get",
            dataType: "json",
            success: function (data) {
                Loading.hide();
                $("#dlg").dialog("open");
                $("input[name='ctId']").val(row.ID);
                $("input[name='taskCode']").val(row.TASKCODE);
                $("input[name='orderCode']").val(row.ORDERCODE);
                $("input[name='partName']").val(row.PARTNAME);
                $("input[name='partId']").val(row.PARTID);
                $("input[name='batchCode']").val(row.BATCHCODE);
                $("input[name='consumerSimpleName']").val(row.CONSUMERSIMPLENAME);
                $("input[name='consumerId']").val(row.CONSUMERID);
                $("input[name='consumerCategory']").val(row.CONSUMERCATEGORY);
                $("input[name='suitCount']").val(row.SUITCOUNT);
                $("input[name='deliveryDate']").val(row.DELIVERYDATE.substring(0, 10));
                $("input[name='packedSuitCount']").val(row.PACKEDSUITCOUNT);
                $("input[name='cutPlanId']").val(row.CUTPLANID);
                if (row.CONSUMERCATEGORY === "1") {
                    $("#taskConsumerCategoryX").textbox("setValue", "国内");
                }
                if (row.CONSUMERCATEGORY === "2") {
                    $("#taskConsumerCategoryX").textbox("setValue", "国外");
                }
                $("#deliveryDate").textbox("setValue", row.DELIVERYDATE.substring(0, 10));
                $("#orderCode").textbox("setValue", row.ORDERCODE);
                $("#batchCode").textbox("setValue", row.BATCHCODE);
                $("#consumerSimpleName").textbox("setValue", row.CONSUMERSIMPLENAME);
                $("#partName").textbox("setValue", row.PARTNAME);
                $("#suitCount").textbox("setValue", row.SUITCOUNT);
                $("#assignCount").textbox("setValue", row.ASSIGNCOUNT);
                $("#taskCode").textbox("setValue", row.TASKCODE);
                $("#ctoCode").textbox("setValue", data.serial);
                $("#drawingsDg").datagrid("loadData", data.drawings);
                $('#assignCount').numberspinner({
                    min: 1,
                    max: data.assigned,
                    value: data.assigend,
                    onChange: setCount,
                    inputEvents: $.extend({}, $.fn.numberbox.defaults.inputEvents, {
                        keyup: function (e) {
                            setCount($("#assignCount").numberspinner("getText"));
                        }
                    })
                });
                $('#assignCount').numberbox("setValue", data.assigned);
                setCount(data.assigned);
            }
        });
    }

    let inValidSuitCount = true;

    function setCount(nv, ov) {
        let v = nv;
        if (v > $("#assignCount").numberspinner("options").max || v === "0") {
            Tip.warn("超出可派工数量");
            $("#assignCount").numberspinner("setValue", $("#assignCount").numberspinner("options").max);
            setCount($("#assignCount").numberspinner("options").max);
            return;
        }
        if (v === "") {
            setWrongCount();
            return;
        }
        v = parseInt(v);
        const rows = $("#drawingsDg").datagrid("getRows");
        const c = undefined;
        for (let i = 0; i < rows.length; i++) {
            /*  if(v%rows[i].suitCountPerDrawings!=0){
                 setWrongCount();
                 return;
             } */
            rows[i].farbicRollCount = Calc.div(v, rows[i].suitCountPerDrawings, 1);
            $("#drawingsDg").datagrid("updateRow", {index: i, row: rows[i]});
        }
        inValidSuitCount = false;
    }

    function setWrongCount() {
        inValidSuitCount = true;
        const rows = $("#drawingsDg").datagrid("getRows");
        for (let i = 0; i < rows.length; i++) {
            rows[i].farbicRollCount = "<font color='red'>无效派工套数</font>";
            $("#drawingsDg").datagrid("updateRow", {index: i, row: rows[i]});
        }
    }

    function saveCutTaskOrder() {
        if (inValidSuitCount) {
            Tip.warn("派工套数不正确");
            return;
        }
        if (!$("#cutTaskForm").form("validate")) {
            return;
        }
        const formData = JQ.getFormAsJson("cutTaskForm");
        /* var list=$("#drawingsDg").datagrid("getRows");
        formData["list"]=list; */
        $.ajax({
            url: path + "siemens/cutTaskOrder/save",
            type: "post",
            data: JSON.stringify(formData),
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                Tip.success("派工成功");
                $("#ctoGroupLeader").combobox("setValue", "");
                $("#ctoGroupName").textbox("setValue", "");
                $("#dlg").dialog("close");
                filter();
            }
        });
    }

    function exportCheckBarcode() {
        const r = EasyUI.grid.getOnlyOneSelected("dg");
        if (r == null) return;
        location.href = path + "siemens/cutTask/checkBarcode?ctId=" + r.ID;
    }

    function viewDrawings() {
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        if (row == null) {
            return;
        }
        viewDrawingsBom(row.ID);
    }

    function viewSuit() {
        const row = EasyUI.grid.getOnlyOneSelected("dg");
        if (row == null) {
            return;
        }
        viewSuitBom(row.ID);
    }
</script>