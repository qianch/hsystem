<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.excheck-3.5.min.js"></script>
<script type="text/javascript">
    const ids =${ids};
	let tree = null;
	const setting = {
		check: {
			enable: true,
			chkboxType: {"Y": "ps", "N": "s"}
		},
		data: {
			simpleData: {
				enable: true,
				idKey: "ID",
				pIdKey: "PARENTID"
			},
			key: {
				name: "NAME"
			}
		},
		callback: {
			onNodeCreated: onNodeCreated
		}
	};


	$(document).ready(function () {
        Loading.show();
        $.ajax({
            url: path + "role/permission/list?all=1",
            type: "get",
            dataType: "json",
            success: function (data) {
                Loading.hide();
                tree = $.fn.zTree.init($("#role_permission"), setting, data.rows);
            }
        });

    });

    function onNodeCreated() {
		const tree = $.fn.zTree.getZTreeObj("role_permission");
		let node = null;
		for (let i = 0; i < ids.length; i++) {
            node = tree.getNodeByParam("ID", ids[i], null);
            if (node == null) continue;
            tree.checkNode(node, true, false, false);
        }
        tree.expandAll(true);
    }

</script>
<div>
    <ul class="ztree" id="role_permission" ids="${ids}"></ul>
</div>

