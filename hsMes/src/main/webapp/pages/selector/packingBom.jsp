<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择包装BOM</title>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    const bcBomtreeUrl = path + "finishProduct/listBcBom";
    $(function () {
        $('#bcBomTree').tree({
            url: bcBomtreeUrl,
            onDblClick: function (node) {
                if (typeof dblClickBcVersion === "function") {
                    if (node.attributes.state === '2') {
                        dblClickBcVersion(node);
                    } else {
                        Tip.warn("请选择对应的版本信息");
                    }
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供dblClickBcVersion方法，参数为node");
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
        const bcNode = $('#bcBomTree').tree('getSelected');
        $('#bcBomTree').tree('collapse', bcNode.target);
    }

    //树列表展开
    function expand() {
        const bcNode = $('#bcBomTree').tree('getSelected');
        $('#bcBomTree').tree('expand', bcNode.target);
    }

    //bcBom搜索框
    function searchBcInfo() {
        const t = $("#searchBc").searchbox("getText");
        $('#bcBomTree').tree({
            url: bcBomtreeUrl + "?data=" + t.toString()
        });
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'east',collapsible:false" title="包材BOM">
        <form id="bc_BomForm" autoSearchFunction="false">
            <label>搜索：</label>
            <input id="searchBc" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchBcInfo"
                   editable="true"
                   data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');}}]">
        </form>
        <ul id="bcBomTree" class="easyui-tree"></ul>
    </div>
</div>

