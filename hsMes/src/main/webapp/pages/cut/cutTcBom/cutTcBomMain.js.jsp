<!--
作者:徐波
日期:2016-10-8 16:53:24
页面:裁剪套才bom明细JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    let isFirst = true;
    const cutTcBomMainTreeUrl = path + "bom/cutTcBom/listBomTree";
    const findCutTcBomDetailByMainIdUrl = path + "bom/cutTcBom/findCutTcBomDetailByMainId";
    const listUrl = path + "bom/cutTcBom/list";
    const cutTcBomMainAddOrEditPageUrl = path + "bom/cutTcBom/cutTcBomMainAddOrEditPage";
    const saveCutTcBomMainUrl = path + "bom/cutTcBom/saveCutTcBomMain";
    const consumerUrl = path + "selector/consumer?singleSelect=false";
    const cancelUrl = path + "bom/cutTcBom/cancel";
    const effectUrl = path + "bom/cutTcBom/effect";
    //导入裁剪bom图纸地址
    const cutTcBomMainUploadUrl = path + "bom/cutTcBom/cutTcBomMainUpload";
    //提交导入裁剪bom图纸内容
    const cutTcBomMainUploadFileUrl = path + "bom/cutTcBom/importCutTcBomMainUploadFile";
    //导出
    const exportCutTcBomMainUrl = path + "bom/cutTcBom/exportcutTcBomMain";
    //导入裁片地址
    const cutTcBomPartMainUploadUrl = path + "cut/cutTcBomPartMain/cutTcBomPartMainUpload";
    //提交导入裁片内容
    const cutTcBomPartMainUploadFileUrl = path + "cut/cutTcBomPartMain/importCutTcBomPartMainUploadFile";
    const findCutTcBomPartMainByTcBomMainIdUrl = path + "cut/cutTcBomPartMain/findCutTcBomPartMainByTcBomMainId";
    const findCutTcBomPartDetailByMainIdUrl = path + "cut/cutTcBomPartMain/findCutTcBomPartDetailByMainId";
    const saveCutTcBomPartMainUrl = path + "cut/cutTcBomPartMain/saveCutTcBomPartMain";
    const saveCutTcBomPartDetailUrl = path + "cut/cutTcBomPartMain/saveCutTcBomPartDetail";
    const doDeletePartMainUrl = path + "cut/cutTcBomPartMain/doDeletePartMain";
    const doDeletePartDetailUrl = path + "cut/cutTcBomPartMain/doDeletePartDetail";
    //导出
    const exportCutTcBomPartUrl = path + "cut/cutTcBomPartMain/exportCutTcBomPart";
    const dialogWidth = 700, dialogHeight = 500;
    const data = [{
        "id": 1,
        "text": "裁剪套才BOM",
        "state": "closed",
        "attributes": {
            'nodeType': 'root'
        }
    }];

    $(function () {
        searchInfo();
        $('#cutTcBomMainTree').tree('expand', $('#cutTcBomMainTree').tree('getRoot').target);
    });

    function searchInfo() {
        var t = $("#searchInput").searchbox("getText");
        $('#cutTcBomMainTree').tree({
            url: cutTcBomMainTreeUrl + "?nodeType=root&data=" + t.toString(),
            data: data,
            animate: true,
            onBeforeLoad: function (node, param) {
                if (isFirst) {
                    isFirst = false;
                    return false;
                } else {
                    return true;
                }
            },
            onContextMenu: function (e, node) {
                e.preventDefault();
                $(this).tree('select', node.target);
                if (node.attributes.nodeType === "root") {
                    $('#mainMenu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                } else if (node.attributes.nodeType === "bom") {
                    $('#treeMenu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            },
            onBeforeExpand: function (node, param) {
                $('#cutTcBomMainTree').tree('options').url = cutTcBomMainTreeUrl + "?nodeType=" + node.attributes.nodeType;
            },
            onClick: function (node) {
                const detail = node.attributes;
                if (detail.nodeType === "bom") {
                    $("#cutTcBomDetailDg").datagrid({
                        url: encodeURI(findCutTcBomDetailByMainIdUrl + "?mainId=" + node.attributes.ID)
                    });
                    $("#cutTcBomPartMainDg").datagrid({
                        url: encodeURI(findCutTcBomPartMainByTcBomMainIdUrl + "?tcBomMainId=" + node.attributes.ID)
                    });
                    $('#cutTcBomPartDetailDg').datagrid('loadData', {total: 0, rows: []});
                    $('#tcProcBomCodeVersion').html(detail.TCPROCBOMCODEVERSION);
                    $('#bladeTypeName').html(detail.BLADETYPENAME);
                    $('#customerName').html(detail.CUSTOMERNAME);
                    $('#customerCode').html(detail.CUSTOMERCODE);
                } else {
                    $("#cutTcBomDetail_dg").datagrid('loadData', {
                        total: 0,
                        rows: []
                    });
                }
                addindex = 0;
                //树列表展开/收缩
                if (node.state === "open") {
                    collapse();
                } else {
                    expand();
                }
            }
        });
    }

    //右击添加工艺bom
    function importDetail() {
        dialogId = Dialog.open("导入", 500, 200, cutTcBomMainUploadUrl, [EasyUI.window.button("icon-save", "导入", function () {
            importCutTcBom();
            searchInfo();
            Dialog.close(dialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {

        });
    }

    function importCutTcBom() {
        const cutTcBomUploadFile = $("#cutTcBomUploadFile");
        if (cutTcBomUploadFile.val().length <= 0) {
            alert("请选择文件");
            return false;
        }
        const filepath = cutTcBomUploadFile.val();
        const extStart = filepath.lastIndexOf(".");
        const ext = filepath.substring(extStart, filepath.length).toUpperCase();
        if (ext !== ".XLSX" && ext !== ".XLS" && ext !== ".XLSM") {
            alert("请上传excel格式文档");
            return false;
        }
        //获取到上传的文件信息
        const data = document.getElementById("cutTcBomUploadFile").files[0];
        const fromData = new FormData();
        if (data != null) {
            fromData.append("file", data);
            $.ajax({
                type: "post",
                url: cutTcBomMainUploadFileUrl,
                data: fromData,
                dataType: "json",
                contentType: false,
                processData: false,
                beforeSend: function () {
                    //dss.load(true);
                },
                complete: function () {
                    //dss.load(false);
                },
                success: function (data) {
                    alert("导入成功！");
                }
            });
        }
    }

    function exportDetail() {
        const t = $('#cutTcBomMainTree');
        const node = t.tree('getSelected');
        window.open(exportCutTcBomMainUrl + "?id=" + node.id);
    }

    function importCutTcBomPartMain() {
        dialogId = Dialog.open("导入", 500, 200, cutTcBomPartMainUploadUrl, [EasyUI.window.button("icon-save", "导入", function () {
            importCutTcBomPartMainBom();
            filter();
            Dialog.close(dialogId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {

        });
    }

    function importCutTcBomPartMainBom() {
        const cutTcBomPartMainUploadFile = $("#cutTcBomPartMainUploadFile");
        if (cutTcBomPartMainUploadFile.val().length <= 0) {
            alert("请选择文件");
            return false;
        }
        const filepath = cutTcBomPartMainUploadFile.val();
        const extStart = filepath.lastIndexOf(".");
        const ext = filepath.substring(extStart, filepath.length).toUpperCase();
        if (ext !== ".XLSX" && ext !== ".XLS" && ext !== ".XLSM") {
            alert("请上传excel格式文档");
            return false;
        }
        //获取到上传的文件信息
        const data = document.getElementById("cutTcBomPartMainUploadFile").files[0];
        const fromData = new FormData();
        if (data != null) {
            fromData.append("file", data);
            $.ajax({
                type: "post",
                url: cutTcBomPartMainUploadFileUrl,
                data: fromData,
                dataType: "json",
                contentType: false,
                processData: false,
                beforeSend: function () {
                    //dss.load(true);
                },
                complete: function () {
                    //dss.load(false);
                },
                success: function (data) {
                    alert("导入成功！");
                }
            });
        }
    }

    //新增
    function addCutTcBomMain() {
        dialogId = Dialog.open("新增", dialogWidth, dialogHeight, cutTcBomMainAddOrEditPageUrl + "?id=", [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
            searchInfo();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    }

    //修改
    function editCutTcBomMain() {
        const t = $('#cutTcBomMainTree');
        const node = t.tree('getSelected');
        if (node.STATE === 2) {
            Tip.warn("已作废裁剪套材不能修改");
            return;
        }
        dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, cutTcBomMainAddOrEditPageUrl
            + "?id=" + node.id, [EasyUI.window.button("icon-save", "保存", function () {
            saveForm();
            searchInfo();
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(dialogId)
        })], function () {
            $("#" + dialogId).dialog("maximize");
        });
    }

    function saveForm() {
        const cutTcBomMain = JQ.getFormAsJson("cutTcBomMainForm");
        const listCutTcBomDetail = [];
        const cutTcBomDetails = $('#cutTcBomDetail_dg').datagrid('getData').rows;
        if (cutTcBomDetails.length === 0) {
            Tip.warn("请添加裁剪bom明细");
            return;
        }
        for (let i = 0; i < cutTcBomDetails.length; i++) {
            $("#cutTcBomDetail_dg").datagrid("endEdit", i);
            const r = {};
            r.id = cutTcBomDetails[i].ID;
            r.partName = cutTcBomDetails[i].PARTNAME;
            r.drawName = cutTcBomDetails[i].DRAWNAME;
            r.orientation = cutTcBomDetails[i].ORIENTATION;
            r.productModel = cutTcBomDetails[i].PRODUCTMODEL;
            r.length = cutTcBomDetails[i].LENGTH;
            r.gramWeight = cutTcBomDetails[i].GRAMWEIGHT;
            r.productionRate = cutTcBomDetails[i].PRODUCTIONRATE;
            r.unitPrice = cutTcBomDetails[i].UNITPRICE;
            r.upperSizeLimit = cutTcBomDetails[i].UPPERSIZELIMIT;
            r.lowerSizeLimit = cutTcBomDetails[i].LOWERSIZELIMIT;
            r.sizePercentage = cutTcBomDetails[i].SIZEPERCENTAGE;
            r.sizeAbsoluteValue = cutTcBomDetails[i].SIZEABSOLUTEVALUE;
            listCutTcBomDetail.push(r);
        }
        cutTcBomMain.listCutTcBomDetail = listCutTcBomDetail;
        Loading.show('保存中');
        $.ajax({
            url: saveCutTcBomMainUrl,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(cutTcBomMain),
            success: function (data) {
                searchInfo();
                Loading.hide();
                Dialog.close(dialogId);
            },
            error: function (data) {
                Loading.hide();
            }
        });
    }

    //选择客户信息
    let selectConsumerWindowId;

    function selectConsumer() {
        selectConsumerWindowId = Dialog.open("选择客户", 900, 500, consumerUrl, [EasyUI.window.button("icon-ok", "选择", function () {
            const row = $("#_common_consumer_dg").datagrid("getChecked");
            if (row.length === 0) {
                Tip.warn("至少选择一个客户");
                return;
            }
            $("#customerName").searchbox("setValue", row[0].CONSUMERNAME);
            $("#customerCode").val(row[0].CONSUMERCODE);
            Dialog.close(selectConsumerWindowId);
        }), EasyUI.window.button("icon-cancel", "关闭", function () {
            Dialog.close(selectConsumerWindowId);
        })], function () {
        }, function () {
            Dialog.close(selectConsumerWindowId);
        });
    }

    function addDetail() {
        const _row = {
            "ID": "",
            "PARTNAME": "",
            "DRAWNAME": "",
            "ORIENTATION": "",
            "PRODUCTMODEL": "",
            "LENGTH": 0,
            "GRAMWEIGHT": 0,
            "PRODUCTIONRATE": 0,
            "UNITPRICE": 0,
            "UPPERSIZELIMIT": 0,
            "LOWERSIZELIMIT": 0,
            "SIZEPERCENTAGE": 0,
            "SIZEABSOLUTEVALUE": 0
        };
        $("#cutTcBomDetail_dg").datagrid("appendRow", _row);
        $("#cutTcBomDetail_dg").datagrid("beginEdit", EasyUI.grid.getRowIndex("cutTcBomDetail_dg", _row));
    }

    function deleteDetail() {
        // 获取选中行的Index的值
        var rowIndex = $('#cutTcBomDetail_dg').datagrid('getRowIndex', $('#cutTcBomDetail_dg').datagrid('getSelected'));
        $("#cutTcBomDetail_dg").datagrid("deleteRow", rowIndex);
    }

    function cutTcBomDetail_dg_Click(index, row) {
        $("#cutTcBomDetail_dg").datagrid("beginEdit", index);
    }

    //作废
    function cancelCutTcBomMain() {
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
            JQ.ajax(cancelUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    //生效
    function effectCutTcBomMain() {
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
            JQ.ajax(effectUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                filter();
            });
        });
    }

    function reload(isSelf) {
        const node = $('#cutTcBomMainTree').tree('getSelected');
        if (isSelf) {
            $('#cutTcBomMainTree').tree('reload', node.target);
        } else {
            const pNode = $('#cutTcBomMainTree').tree('getParent', node.target);
            $('#cutTcBomMainTree').tree('reload', pNode.target);
        }
    }

    //树列表收缩
    function collapse() {
        const node = $('#cutTcBomMainTree').tree('getSelected');
        $('#cutTcBomMainTree').tree('collapse', node.target);
    }

    //树列表展开
    function expand() {
        const node = $('#cutTcBomMainTree').tree('getSelected');
        $('#cutTcBomMainTree').tree('expand', node.target);
    }

    function expandAll() {
        $('#cutTcBomMainTree').tree('expandAll');
    }

    function collapseAll() {
        $('#cutTcBomMainTree').tree('collapseAll');
    }

    let editIndex_cutTcBomPartMainDg = undefined;
    let addindex_cutTcBomPartMainDg = 0;

    //裁片信息修改
    function cutTcBomPartMainDg_DbClick(index, field) {
        addindex = 0;
        if (editIndex_cutTcBomPartMainDg !== index) {
            if (cutTcBomPartMainDg_endEditing()) {
                $('#cutTcBomPartMainDg').datagrid('selectRow', index).datagrid('beginEdit', index);
                const ed = $('#cutTcBomPartMainDg').datagrid('getEditor', {
                    index: index,
                    field: field
                });
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                editIndex_cutTcBomPartMainDg = index;
            } else {
                setTimeout(function () {
                    $('#cutTcBomPartMainDg').datagrid('selectRow', editIndex_cutTcBomPartMainDg);
                }, 0);
            }
        }
    }

    function cutTcBomPartMainDg_Click(index, field) {
        if (editIndex_cutTcBomPartMainDg !== undefined && index !== editIndex_cutTcBomPartMainDg) {
            $('#cutTcBomPartMainDg').datagrid('endEdit', editIndex_cutTcBomPartMainDg);
        }
        const r = EasyUI.grid.getSelections("cutTcBomPartMainDg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        $("#cutTcBomPartDetailDg").datagrid({
            url: encodeURI(findCutTcBomPartDetailByMainIdUrl + "?mainId=" + r[0].ID)
        });
    }

    function cutTcBomPartMainDg_endEditing() {
        if (editIndex_cutTcBomPartMainDg === undefined) {
            return true
        }
        if ($('#cutTcBomPartMainDg').datagrid('validateRow', editIndex_cutTcBomPartMainDg)) {
            $('#cutTcBomPartMainDg').datagrid('endEdit', editIndex_cutTcBomPartMainDg);
            editIndex_cutTcBomPartMainDg = undefined;
            return true;
        } else {
            return false;
        }
    }

    function cutTcBomPartMainDg_onEndEdit(index, row, changes) {
        addindex_cutTcBomPartMainDg = 0;
        const node = $('#cutTcBomMainTree').tree('getSelected');
        const rowstr = {
            "id": row.ID,
            "tcBomMainId": node.id,
            "partName": row.PARTNAME,
            "productModel": row.PRODUCTMODEL,
            "cutName": row.CUTNAME,
            "layerNum": row.LAYERNUM,
            "remark": row.REMARK
        };

        $.ajax({
            url: saveCutTcBomPartMainUrl,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(rowstr),
            success: function (data) {
                if (data !== "") {
                    Tip.error(data);
                } else {
                    Tip.success("保存裁片成功!");
                }
                $('#cutTcBomPartMainDg').datagrid('reload');
            }
        });
    }

    function addPartMain() {
        const node = $('#cutTcBomMainTree').tree('getSelected');
        if (node == null) {
            Tip.warn("请选中裁剪图纸bom后在点击增加");
            return;
        }
        const _row = {
            "ID": "",
            "TCBOMMAINID": node.id,//tcBomMainId
            "PARTNAME": "",
            "PRODUCTMODEL": "",
            "CUTNAME": "",
            "LAYERNUM": "",
            "REMARK": ""
        };

        if (addindex_cutTcBomPartMainDg === 0) {
            $("#cutTcBomPartMainDg").datagrid('insertRow', {
                index: 0,
                row: _row
            });
            $("#cutTcBomPartMainDg").datagrid('beginEdit', 0);
            editIndex_cutTcBomPartMainDg = 0;
            addindex_cutTcBomPartMainDg = 1;
        }
    }

    function doDeletePartMain() {
        const r = EasyUI.grid.getSelections("cutTcBomPartMainDg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(doDeletePartMainUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                $('#cutTcBomPartMainDg').datagrid('reload');
            });
        });
    }

    //裁片详情修改
    let editIndex_cutTcBomPartDetailDg = undefined;
    let addindex_cutTcBomPartDetailDg = 0;

    //裁片信息修改
    function cutTcBomPartDetailDg_DbClick(index, field) {
        addindex = 0;
        if (editIndex_cutTcBomPartDetailDg !== index) {
            if (cutTcBomPartDetailDg_endEditing()) {
                $('#cutTcBomPartDetailDg').datagrid('selectRow', index).datagrid('beginEdit', index);
                var ed = $('#cutTcBomPartDetailDg').datagrid('getEditor', {
                    index: index,
                    field: field
                });
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                editIndex_cutTcBomPartDetailDg = index;
            } else {
                setTimeout(function () {
                    $('#cutTcBomPartDetailDg').datagrid('selectRow', editIndex_cutTcBomPartDetailDg);
                }, 0);
            }
        }
    }

    function cutTcBomPartDetailDg_Click(index, field) {
        if (editIndex_cutTcBomPartDetailDg !== undefined && index !== editIndex_cutTcBomPartDetailDg) {
            $('#cutTcBomPartDetailDg').datagrid('endEdit', editIndex_cutTcBomPartDetailDg);
        }
    }


    function cutTcBomPartDetailDg_endEditing() {
        if (editIndex_cutTcBomPartDetailDg === undefined) {
            return true
        }
        if ($('#cutTcBomPartDetailDg').datagrid('validateRow', editIndex_cutTcBomPartDetailDg)) {
            $('#cutTcBomPartDetailDg').datagrid('endEdit', editIndex_cutTcBomPartDetailDg);
            editIndex_cutTcBomPartDetailDg = undefined;
            return true;
        } else {
            return false;
        }
    }

    function cutTcBomPartDetailDg_onEndEdit(index, row, changes) {
        addindex_cutTcBomPartDetailDg = 0;
        const node = $('#cutTcBomMainTree').tree('getSelected');
        const rowstr = {
            "id": row.ID,
            "tcBomMainId": node.id,
            "mainId": row.MAINID,
            "cutNameLayNo": row.CUTNAMELAYNO,
            "layNo": row.LAYNO,
            "amount": row.AMOUNT,
            "packSequence": row.PACKSEQUENCE
        };

        $.ajax({
            url: saveCutTcBomPartDetailUrl,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(rowstr),
            success: function (data) {
                if (data !== "") {
                    Tip.error(data);
                } else {
                    Tip.success("保存裁片明细成功!");
                }
                $('#cutTcBomPartDetailDg').datagrid('reload');
            }
        });
    }

    function addPartDetail() {
        const node = $('#cutTcBomMainTree').tree('getSelected');
        if (node == null) {
            Tip.warn("请选中裁剪图纸bom后在点击增加");
            return;
        }
        const r = EasyUI.grid.getSelections("cutTcBomPartMainDg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const _row = {
            "ID": "",
            "MAINID": r[0].ID,
            "TCBOMMAINID": node.id,//tcBomMainId
            "CUTNAMELAYNO": "",
            "LAYNO": "",
            "AMOUNT": "",
            "PACKSEQUENCE": ""
        };
        if (addindex_cutTcBomPartDetailDg == 0) {
            $("#cutTcBomPartDetailDg").datagrid('insertRow', {
                index: 0,
                row: _row
            });
            $("#cutTcBomPartDetailDg").datagrid('beginEdit', 0);
            editIndex_cutTcBomPartDetailDg = 0;
            addindex_cutTcBomPartDetailDg = 1;
        }
    }

    function doDeletePartDetail() {
        const r = EasyUI.grid.getSelections("cutTcBomPartDetailDg");
        if (r.length === 0) {
            Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
            return;
        }
        const ids = [];
        for (let i = 0; i < r.length; i++) {
            ids.push(r[i].ID);
        }
        Dialog.confirm(function () {
            JQ.ajax(doDeletePartDetailUrl, "post", {
                ids: ids.toString()
            }, function (data) {
                $('#cutTcBomPartDetailDg').datagrid('reload');
            });
        });
    }

    function exportCutTcBomPart() {
        const t = $('#cutTcBomMainTree');
        const node = t.tree('getSelected');
        window.open(exportCutTcBomPartUrl + "?tcBomMainId=" + node.id);
    }
</script>
