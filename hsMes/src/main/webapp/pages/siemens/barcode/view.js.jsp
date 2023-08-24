<!--
作者:徐波
日期:2017-02-07 14:49:00
页面:产品信息查询JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const treeUrl = path + "siemens/fragmentBarcode/view/tree?code=";
    const searchInfo = function () {
        clear();
        $("#searchInput").searchbox("setText", $("#searchInput").searchbox("getText").toUpperCase());
        const t = $("#searchInput").searchbox("getText");
        const url = treeUrl + t;
        JQ.ajax(url, "get", null, function (data) {
            $('#tree').tree(
                {
                    data: data,
                    method: 'get',
                    animate: true,
                    formatter: function (node) {
                        if (node.text.charAt(0) === 'P') {
                            let root = "<font style=\'font-weight:bold\'>" + node.text.toUpperCase() + "</font>";
                            try {
                                root += " <font color=red>（" + node.children.length + "个小部件）</font>";
                            } catch (e) {
                            }
                            return root;
                        }
                        return node.text.toUpperCase()
                            + " <font style='background: #dedede;border-radius: 5px;color: #6b826b;'>"
                            + node.id + "</font>"
                            + " <font style='background: #dedede;border-radius: 5px;color: #6b826b;'>"
                            + node.fragmentName + "</font>";
                    },
                    onClick: function (node) {
                        clear();
                        query(node.text);
                    }
                });
        });
    };

    function loadPartInfo(data) {
        $("#p").show();
        $("#c").hide();
        $("#p_order").html(data.SALESORDERCODE);
        $("#p_batch").html(data.BATCHCODE);
        $("#p_part").html(data.PARTNAME);
        $("#p_printDate").html(data.PRINTDATE);
    }

    function loadFragmentInfo(data) {
        $("#p").hide();
        $("#c").show();
        $("#c_order").html(data.ORDERCODE);
        $("#c_batch").html(data.BATCHCODE);
        $("#c_part").html(data.PARTNAME);
        $("#c_drawingNo").html(data.FRAGMENTDRAWINGNO);
        $("#c_group").html(data.GROUPNAME);
        $("#c_groupLeader").html(data.GROUPLEADER);
        $("#c_suitUser").html(data.PACKUSERNAME);
        $("#c_suitTime").html(data.PACKEDTIME);
        $("#c_device").html(data.DEVICECODE);
        $("#c_printDate").html(data.PRINTTIME);
        $("#c_fragmentName").html(data.FRAGMENTNAME)
    }

    function clear() {
        $("#p").hide();
        $("#c").hide();
        $("#c_order").html("");
        $("#c_batch").html("");
        $("#c_part").html("");
        $("#c_drawingNo").html("");
        $("#c_group").html("");
        $("#c_groupLeader").html("");
        $("#c_suitUser").html("");
        $("#c_suitTime").html("");
        $("#c_device").html("");
        $("#c_printDate").html("");
        $("#p_order").html("");
        $("#p_batch").html("");
        $("#p_part").html("");
        $("#p_printDate").html("");
    }

    const url = path + "siemens/fragmentBarcode/view";

    function query(code) {
        Loading.show();
        JQ.ajaxPost(url, {code: code}, function (data) {
            Loading.hide();
            if (code.charAt(0) === 'P') {
                loadPartInfo(data);
            } else {
                loadFragmentInfo(data);
            }
        });
    }
</script>