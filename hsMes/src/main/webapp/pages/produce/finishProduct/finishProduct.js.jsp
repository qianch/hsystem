<!--
作者:宋黎明
日期:2016-9-30 10:49:34
页面:成品信息JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //添加成品信息
    var addUrl = path + "finishProduct/add";
    //编辑成品信息
    var editUrl = path + "finishProduct/edit";
    //删除成品信息
    var deleteUrl = path + "finishProduct/delete";
    //选择客户信息
    var chooseConsumer = path + "selector/consumer";
    //选择套材/非套材BOM信息
    var chooseBomUrl = path + "finishProduct/chooseBom";
    //选择包装BOM信息
    var choosePackingBomUrl = path + "selector/packingBom";
    var addReqDetails = path + "finishProduct/addReq";
    //更新废弃按钮
    var update = path + "finishProduct/updateS";

    //恢复产品
    var resumeFinishProductUrl = path + "finishProduct/resumeFinishProduct";

    //查看产品信息
    var lookProduct = path + "finishProduct/checkProduct";


    //提交审核
    var _auditCommitUrl = path + "selector/commitAudit";
    var auditCommitUrl = path + "finishProduct/commitAudit";

    var dialogWidth = 700, dialogHeight = 500;
    var consumerWindow = null;
    var bomWindow = null;
    var packingBomWindow = null;
    var productIsTc = null;

    var finishProductUrl = path + "finishProduct/list";
    var findFinishedProductPrintRecordsUrl = path + "finishProduct/finishedProductPrintRecord/findFinishedProductPrintRecords";

    $(function () {
        $('#dgfinishProduct').datagrid({
            url: finishProductUrl,
            onBeforeLoad: dgOnBeforeLoad,
            view: detailview,
            detailFormatter: function (index, row) {
                return '<div style="padding:2px;"><table class="ddv"></table></div>';
            },
            onExpandRow: function (index, row) {
                var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');

                ddv.datagrid({
                        url: findFinishedProductPrintRecordsUrl + "?productId=" + row.ID,
                        fitColumns: true,
                        singleSelect: true,
                        rownumbers: true,
                        loadMsg: '',
                        height: 'auto',
                        columns: [[
                            {
                                field: 'PRINTATTRIBUTE',
                                title: '打印属性',
                                width: 15,
                            }, {
                                field: 'PRINTATTRIBUTENAME',
                                title: '打印属性名称',
                                width: 15,
                            }, {
                                field: 'PRINTATTRIBUTECONTENT',
                                title: '打印属性内容',
                                width: 15,
                            }
                        ]],
                        onResize: function () {
                            $('#dgfinishProduct').datagrid('fixDetailRowHeight', index);
                        },
                        onLoadSuccess: function () {
                            Loading.hide();
                            setTimeout(function () {
                                $('#dgfinishProduct').datagrid('fixDetailRowHeight', index);
                            }, 0);
                        }
                        , rowStyler: function (index, row) {
                            if (row.length == 0) {
                                return 'background-color:yellow;color:blue;font-weight:bold;';
                            }
                        }
                    }
                );
            }
        });
        filter();
    });


    function add_details() {
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");

        var wid = Dialog.open("添加", 700, 295, addReqDetails + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                EasyUI.form.submit("finishProductForm", addReqDetails, function (
                    data) {
                    filter();
                    if (Dialog.isMore(wid)) {
                        Dialog.close(wid);
                    } else {
                        Dialog.close(wid);
                    }
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            Dialog.more(wid);
        });
    }

    //查询
    function filter() {
        EasyUI.grid.search("dgfinishProduct", "finishProductSearchForm");
    }

    //提交审核
    var doAudit = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");
        if (r.AUDITSTATE != 0) {
            Tip.warn("只有未提交审核的产品才能提交审核");
            return;
        }
        var wid = Dialog.open("审核", 500, 120, _auditCommitUrl + "?id=" + r.ID, [EasyUI.window.button("icon-ok", "提交审核", function () {
            EasyUI.form.submit("editAuditProduce", auditCommitUrl, function (data) {
                filter();
                Dialog.close(wid);
            })
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(wid);
        })], function () {
            var type = null;
            if (r.PRODUCTISTC == 1) {
                type = "套材";
            } else if (r.PRODUCTISTC == 2) {
                type = "非套材";
            } else if (r.PRODUCTISTC == -1) {
                type = "胚布";
            }
            var isCommon = null;
            if (r.ISCOMMON == 1) {
                isCommon = "常规";
            } else if (r.ISCOMMON == 0) {
                isCommon = "试样";
            }

            $("#editAuditProduce #name").textbox("setValue", type + isCommon + "产品信息审核，产品规格：" + r.PRODUCTMODEL);
        });
    }

    //查看审核
    function view() {
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");
        if (r == null)
            return;

        var type = null;
        if (r.ISCOMMON == 1) {
            if (r.PRODUCTISTC == 1) {
                type = "CPTC";
            } else if (r.PRODUCTISTC == 2) {
                type = "CPFC";
            } else if (r.PRODUCTISTC == -1) {
                type = "CPPC";
            }
        } else if (r.ISCOMMON == 0) {
            if (r.PRODUCTISTC == 1) {
                type = "CPTS";
            } else if (r.PRODUCTISTC == 2) {
                type = "CPFS";
            } else if (r.PRODUCTISTC == -1) {
                type = "CPPS";
            }
        }
        console.log(type);
        var viewUrl = path + "audit/" + type + "/{id}/state";
        dialogId = Dialog.open("查看审核状态", dialogWidth, dialogHeight, viewUrl.replace("{id}", r.ID), [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            //$("#" + dialogId).dialog("maximize");
        });
    }

    //添加成品信息
    var add = function () {
        var wid = Dialog.open("添加", 1006, 550, addUrl, [
            EasyUI.window.button("icon-save", "保存", function () {
                Loading.show();
                EasyUI.form.submit("finishProductForm", addUrl, function (
                    data) {
                    Loading.hide();
                    filter();
                    if (Dialog.isMore(wid)) {
                        Dialog.close(wid);
                        add();
                    } else {
                        Dialog.close(wid);
                    }
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            Dialog.more(wid);
            Dialog.max(wid);
        });
    };

    function validCode() {
        var _options = $(this).combobox('options');
        var _data = $(this).combobox('getData');/* 下拉框所有选项 */
        var _value = $(this).combobox('getValue');/* 用户输入的值 */
        var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
        for (var i = 0; i < _data.length; i++) {
            if (_data[i][_options.valueField] == _value) {
                _b = true;
                break;
            }
        }
        if (!_b) {
            $(this).combobox('setValue', '');
            return;
        }
    }

    function comboFilter(q, row) {
        var opts = $(this).combobox('options');
        return row[opts.textField].toUpperCase().indexOf(q.toUpperCase()) != -1;
    }

    //编辑成品信息
    var edit = function (type) {

        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");
        if (type == null) {
            if (r.AUDITSTATE == 1 || r.AUDITSTATE == 2) {
                Tip.warn("已提交审核或已通过审核的产品不能编辑");
                return;
            }
        } else {
            if (r.AUDITSTATE != 2) {
                Tip.warn("只有已通过审核的产品才能变更");
                return;
            }
        }
        var wid = Dialog.open("编辑", 1006, 550.4, editUrl + "?id=" + r.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                Loading.show();
                EasyUI.form.submit("finishProductForm", editUrl, function (data) {
                    Loading.hide();
                    filter();
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            Dialog.max(wid);
        });
    };

    //查看产品信息窗口
    var checkProduct = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");
        var wid = Dialog.open("查看产品信息", 1017, 550, lookProduct + "?id=" + r.ID, [
            EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");
        });
    }


    //查看产品信息窗口
    var checkYxInfo = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");
        var wid = Dialog.open("查看叶型信息", 550, 550, path + "finishProduct/queryYxInfo" + "?id=" + r.ID, [
            EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            $("#" + wid).dialog("maximize");

            $("#weight_dg").datagrid("loadData", yxdates);
        });
    }

    /**
     * 双击行，弹出编辑
     */
    var dbClickEdit = function (index, row) {
        if (row.AUDITSTATE == 1 || row.AUDITSTATE == 2) {
            Tip.warn("已提交审核或已通过审核的产品不能编辑");
            return;
        }
        var wid = Dialog.open("编辑", 1005, 543.4, editUrl + "?id=" + row.ID, [
            EasyUI.window.button("icon-save", "保存", function () {
                Loading.show();
                EasyUI.form.submit("finishProductForm", editUrl, function (
                    data) {
                    Loading.hide();
                    filter();
                    Dialog.close(wid);
                });
            }), EasyUI.window.button("icon-cancel", "关闭", function () {
                Dialog.close(wid);
            })], function () {
            Dialog.max(wid);
        });
    }

    //删除成品信息
    var doDelete = function () {
        var r = EasyUI.grid.getSelections("dgfinishProduct");
        if (r.length == 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }

        var ids = [];
        for (var i = 0; i < r.length; i++) {
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

    //产品复制
    function copy() {
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");
        var wid = Dialog.open("复制产品信息", 1015, 533, editUrl + "?id=" + r.ID + "&copy=true", [
                EasyUI.window.button("icon-save", "保存复制", function () {
                    $("input[name='id']").val("");
                    Loading.show();
                    EasyUI.form.submit("finishProductForm", addUrl, function (data) {
                        Loading.hide();
                        filter();
                        if (data.error != "已存在相同物料编号，请重新获取物料编号！") {
                            Dialog.close(wid);
                        }

                    });
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid);
                })], function () {
                Dialog.max(wid);
            }
        );
    }


    //废弃成品
    var old = function () {
        var r = EasyUI.grid.getOnlyOneSelected("dgfinishProduct");
        Dialog.confirm(function () {
            Loading.show();
            $.ajax({
                url: update + "?id=" + r.ID,
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("作废成功");
                    Loading.hide();
                    filter();
                }
            });
        }, "确认作废");
    }

    //恢复产品
    var resumeFinishProduct = function () {

        var r = EasyUI.grid.getSelections("dgfinishProduct");
        if (r.length != 1) {
            Tip.warn("请选择唯一成品信息");
            return;
        }

        Dialog.confirm(function () {
            Loading.show();
            $.ajax({
                url: resumeFinishProductUrl + "?id=" + r[0].ID,
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    Tip.success("恢复成功");
                    Loading.hide();
                    filter();
                }
            });
        }, "确认恢复");
    }

    //选择客户
    function ChooseConsumer() {
        consumerWindow = Dialog.open("选择客户", 900, 450, chooseConsumer, [
            EasyUI.window.button("icon-save", "确认", function () {
                var r = EasyUI.grid
                    .getOnlyOneSelected("_common_consumer_dg");
                $('#productConsumerCode').searchbox('setValue',
                    r.CONSUMERCODE);
                $('#productConsumerName').searchbox('setValue',
                    r.CONSUMERNAME);
                JQ.setValue('#productConsumerId', r.ID);
                Dialog.close(consumerWindow);
            }),
            EasyUI.window.button("icon-cancel",
                "<spring:message code="Button.Cancel" />", function () {
                    Dialog.close(consumerWindow);
                })]);
    }

    //选择客户双击事件
    function _common_consumer_dbClickRow(index, row) {
        $('#productConsumerCode').searchbox('setValue', row.CONSUMERCODE);
        $('#productConsumerName').searchbox('setValue', row.CONSUMERNAME);
        JQ.setValue('#productConsumerId', row.ID);
        Dialog.close(consumerWindow);
    }

    //选择套材/非套材工艺BOM窗口
    function chooseBom() {
        var productIsTc1 = $("#productIsTc1").attr("checked");
        var productIsTc2 = $("#productIsTc2").attr("checked");
        if (productIsTc1 == 'checked') {
            productIsTc = 1;
        } else {
            productIsTc = 2;
        }
        bomWindow = Dialog.open("", 400, 450, chooseBomUrl + "?productIsTc="
            + productIsTc, [
            EasyUI.window.button("icon-save", "确认", function () {
                var tcNode = $('#tcBomTree').tree('getSelected');
                var ftcNode = $('#ftcBomTree').tree('getSelected');
                if (tcNode == null) {
                    //非套材
                    var parentFtcInfo = $('#ftcBomTree').tree('getParent',
                        ftcNode.target);
                    $('#productProcessBom').searchbox('setValue',
                        parentFtcInfo.attributes.FTCPROCBOMCODE);
                    $('#productProcessName').textbox('setValue',
                        parentFtcInfo.attributes.FTCPROCBOMNAME);
                    //$('#productConsumerBomVersion').textbox('setValue', parentFtcInfo.attributes.FTCPROCBOMNAME);
                    $('#productConsumerBomVersion').textbox('setValue', ftcNode.text);
                    $('#productProcessBomVersion').textbox('setValue',
                        ftcNode.text);
                    $('#procBomId').val(ftcNode.id);
                } else {
                    //套材
                    var parentTcInfo = $('#tcBomTree').tree('getParent',
                        tcNode.target);
                    $('#productProcessBom').searchbox('setValue',
                        parentTcInfo.attributes.TCPROCBOMCODE);
                    $('#productProcessName').textbox('setValue', parentTcInfo.attributes.TCPROCBOMNAME);
                   // $('#productConsumerBomVersion').textbox('setValue',parentTcInfo.attributes.TCPROCBOMNAME);
                    $('#productConsumerBomVersion').textbox('setValue', tcNode.text);
                    $('#productProcessBomVersion').textbox('setValue',
                        tcNode.text);
                    $('#procBomId').val(tcNode.id);
                }

                Dialog.close(bomWindow);
            }),
            EasyUI.window.button("icon-cancel",
                "<spring:message code="Button.Cancel" />", function () {
                    Dialog.close(bomWindow);
                })]);
    }

    //选择包装工艺BOM窗口
    function choosePackingBom() {
        var productIsTc = $("input[name='productIsTc']:checked").val();
        if (productIsTc == 2) {
            chooseFtcBcBom();
            return;
        }
        packingBomWindow = Dialog.open("", 400, 450, choosePackingBomUrl, [
            EasyUI.window.button("icon-save", "确认", function () {
                var node = $('#bcBomTree').tree('getSelected');
                var parentInfo = $('#bcBomTree').tree('getParent',
                    node.target);
                $('#productPackagingCode').searchbox('setValue',
                    parentInfo.attributes.PACKBOMGENERICNAME + "/" + parentInfo.attributes.PACKBOMCODE);
                $('#productPackageVersion').textbox('setValue', node.text);
                $('#packBomId').val(node.id);
                Dialog.close(packingBomWindow);
            }),
            EasyUI.window.button("icon-cancel",
                "<spring:message code="Button.Cancel" />", function () {
                    Dialog.close(packingBomWindow);
                })]);
    }


    /**
     * 选择非套材包材BOM
     */
    function chooseFtcBcBom() {
        //加载非套材BOM的二级代码
        $("#packageType").dialog("open", true);
    }

    //双击选中套材BOM tree
    function dblClickTcVersion(node) {
        var parentInfo = $('#tcBomTree').tree('getParent', node.target);
        $('#productProcessBom').searchbox('setValue',parentInfo.attributes.TCPROCBOMCODE);
        $('#productProcessName').textbox('setValue',       parentInfo.attributes.TCPROCBOMNAME);
       // $('#productConsumerBomVersion').textbox('setValue',parentInfo.attributes.TCPROCBOMNAME);
        $('#productConsumerBomVersion').textbox('setValue',node.text);
        $('#productProcessBomVersion').textbox('setValue', node.text);
        $('#procBomId').val(node.id);
        Dialog.close(bomWindow);
    }

    //双击选中非套材BOM tree
    function dblClickFtcVersion(node) {
        var parentInfo = $('#ftcBomTree').tree('getParent', node.target);
        $('#productProcessBom').searchbox('setValue',
            parentInfo.attributes.FTCPROCBOMCODE);
        $('#productProcessName').textbox('setValue',
            parentInfo.attributes.FTCPROCBOMNAME);
       // $('#productConsumerBomVersion').textbox('setValue',parentInfo.attributes.FTCPROCBOMNAME);
        $('#productConsumerBomVersion').textbox('setValue',node.text);
        $('#productProcessBomVersion').textbox('setValue', node.text);
        $('#procBomId').val(node.id);
        Dialog.close(bomWindow);
    }

    //双击选中包装BOM tree
    function dblClickBcVersion(node) {
        var parentInfo = $('#bcBomTree').tree('getParent',
            node.target);
        $('#productPackagingCode').searchbox('setValue',
            parentInfo.attributes.PACKBOMGENERICNAME + "/" + parentInfo.attributes.PACKBOMCODE);
        $('#productPackageVersion').textbox('setValue', node.text);
        $('#packBomId').val(node.id);
        Dialog.close(packingBomWindow);
    }

    //查看工艺bom明细
    function bomVersionView(value, row, index) {
        if (value == null) {
            return "";
        } else if (row.PRODUCTPROCESSBOMVERSION == null || row.PRODUCTPROCESSBOMVERSION == "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_bomVersionView(" + row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value + "</a>"
        }
    }

    var dialogId;

    function _bomVersionView(procBomId, isTc) {
        if (procBomId == null) {
            Tip.error("工艺版本错误，请重新编辑产品");
            return;
        }
        var viewUrl = "";
        if (isTc == 1) {
            viewUrl = path + "selector/view/tc?procBomId=" + procBomId;
        } else {
            viewUrl = path + "selector/view/ftc?procBomId=" + procBomId;
        }
        dialogId = Dialog.open("查看工艺bom明细", 700, 400, viewUrl, [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            $("#" + dialogId).dialog("maximize");
            if (isTc != 1) {
                for (var a = 0; a < details.length; a++) {
                    _common_bomDetail_data(details[a]);
                }
            }
        });
    };

    //查看包装bom明细
    function packBomView(value, row, index) {
        if (value == null) {
            return "";
        } else if (value == "无包装") {
            return "";
        } else if (row.PRODUCTPACKAGEVERSION == null || row.PRODUCTPACKAGEVERSION == "") {
            return "";
        } else {
            return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_packBomView(" + row.PACKBOMID + ")'>" + value + "</a>"
        }
    }

    var dialogId;

    function _packBomView(packBomId) {
        if (packBomId == null || packBomId == "") {
            Tip.error("包装工艺错误，请重新编辑产品");
            return;
        }
        var viewUrl = path + "selector/view/bc?packBomId=" + packBomId;
        dialogId = Dialog.open("查看包装bom明细", 700, 400, viewUrl, [EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId);
        })], function () {
            $("#" + dialogId).dialog("maximize");
            for (var a = 0; a < details.length; a++) {
                _common_bcBomDetail_data(details[a]);
            }
        });

    };


    function formatterS(value, row, index) {
        if (value == null) {
            return "正常";
        } else {
            return "已作废";
        }
    }

    function formatterWeigh(value, row, index) {
        if (value == 0) {
            return "全称	";
        }
        if (value == 1) {
            return "抽称";
        }
        if (value == 2) {
            return "不称";
        }
        if (value == 3) {
            return "首卷必称";
        }
    }

    //包装代码
    function formatterPackagingCode(value, row, index) {
        if (value == null) {
            return "无包装";
        } else {
            return value;
        }
    }

    //产品属性
    function formatterIscommon(value, row, index) {
        if (value == 0) {
            return "试样";
        } else if (value == 1) {
            return "常规";
        }
    }

    //审核状态
    function formatterReviewState(val, row, index) {
        return auditStateFormatter(row.AUDITSTATE);
    }

    /**
     * 成品信息模板导出
     */
    function downloadTemplate() {
        var downloadTemplateURL = path + "finishProduct/tempExport";
        location.href = downloadTemplateURL;
    }

    /**
     * 成品信息导入
     */
    var Import = function () {
        /*		var wid = Dialog.open("成品信息导入",200,200,path+"excel/upload/com.bluebirdme.mes.produce.excelImport.FinishedProductImportHander/",
                    [EasyUI.window.button("icon-cancel","关闭",function(){
                        filter();
                        Dialog.close(wid);
                    })
                ]);*/
        var wid = Dialog.open("成品信息导入", 300, 200, path + "excel/upload/com.bluebirdme.mes.produce.excelImport.FinishedProductImportHander/", [
                EasyUI.window.button("icon-save", "导入", function () {
                    $('#file_upload').uploadify('upload', '*');
                    filter();
//        			Dialog.close(wid);
                }), EasyUI.window.button("icon-cancel", "关闭", function () {
                    Dialog.close(wid)
                })]
        );
    }


</script>
