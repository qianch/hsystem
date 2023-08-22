<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择套材/非套材BOM</title>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    const tcBomtreeUrl = path + "finishProduct/listTcBom";
    $(function () {
        $('#tcBomTree').tree({
            url: tcBomtreeUrl,
            onDblClick: function (node) {
                if (typeof dblClickTcVersion === "function") {
                    if (node.attributes.state === '2') {
                        dblClickTcVersion(node);
                    } else {
                        Tip.warn("请选择对应的版本信息");
                    }
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供dblClickTcVersion方法，参数为node");
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
        const tcNode = $('#tcBomTree').tree('getSelected');
        $('#tcBomTree').tree('collapse', tcNode.target);
    }

    //树列表展开
    function expand() {
        const tcNode = $('#tcBomTree').tree('getSelected');
        $('#tcBomTree').tree('expand', tcNode.target);
    }

    //tcBom搜索框
    function searchTcInfo() {
        const t = $("#searchTc").searchbox("getText");
        $('#tcBomTree').tree({
            url: tcBomtreeUrl + "?data=" + t.toString()
        });
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',collapsible:false" title="套材BOM">
        <form id="tc_BomForm" autoSearchFunction="false">
            <label>搜索：</label>
            <input id="searchTc" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchTcInfo"
                   editable="true"
                   data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');}}]">
        </form>
        <ul id="tcBomTree" class="easyui-tree"></ul>
    </div>
</div>

