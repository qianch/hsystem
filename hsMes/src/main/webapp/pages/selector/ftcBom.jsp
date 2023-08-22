<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择套材/非套材BOM</title>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    const ftcBomtreeUrl = path + "finishProduct/listFtcBom";
    $(function () {
        $('#ftcBomTree').tree({
            url: ftcBomtreeUrl,
            onDblClick: function (node) {
                if (typeof dblClickFtcVersion === "function") {
                    if (node.attributes.state === '2') {
                        dblClickFtcVersion(node);
                    } else {
                        Tip.warn("请选择对应的版本信息");
                    }
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供dblClickFtcVersion方法，参数为node");
                    }
                }
            },
            onSelect: function (node) {
                //树列表展开/收缩
                if (node.state === "open") {
                    collapse();
                } else {
                    expand();
                }
            }
        });
    });

    //树列表收缩
    function collapse() {
        var ftcNode = $('#ftcBomTree').tree('getSelected');
        $('#ftcBomTree').tree('collapse', ftcNode.target);
    }

    //树列表展开
    function expand() {
        var ftcNode = $('#ftcBomTree').tree('getSelected');
        $('#ftcBomTree').tree('expand', ftcNode.target);
    }

    //ftcBom搜索框
    function searchFtcInfo() {
        var t = $("#searchFtc").searchbox("getText");
        $('#ftcBomTree').tree({
            url: ftcBomtreeUrl + "?data=" + t.toString()
        });
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'east',collapsible:false" title="非套材BOM">
        <form id="fTc_BomForm" autoSearchFunction="false">
            <label>搜索：</label>
            <input id="searchFtc" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchFtcInfo"
                   editable="true"
                   data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');}}]">
        </form>
        <ul id="ftcBomTree" class="easyui-tree"></ul>
    </div>
</div>

