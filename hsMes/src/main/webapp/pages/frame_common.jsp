<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String site = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    long nocache = new Date().getTime();
%>

<title>恒石纤维基业MES系统</title>
<link rel="stylesheet" type="text/css" href="<%=site%>/resources/themes/index.css?_=<%=nocache %>">
<script type="text/javascript" src="<%=site%>/resources/js/index.js?_=<%=nocache %>"></script>
<script>
    $(document).ready(function () {
        loadTasks();
    });
    let _aids = "";

    function loadTasks() {
        $.ajax({
            url: "<%=site%>loop/msg",
            type: "get",
            dataType: 'json',
            success: function (data) {
                let tasks = data.task;
                const notices = data.notice;
                if (tasks != null) {
                    if (tasks.length > 5) {
                        tasks = tasks.slice(0, 4);
                    }
                    let _aids_ = "";
                    for (let i = 0; i < tasks.length; i++) {
                        _aids_ += tasks[i].ID;
                    }
                    if (_aids_ !== _aids) {
                        _aids = _aids_;
                        notify("MES审核通知", "您有新的审核消息");
                    }
                }
                for (let i = 0; i < notices.length; i++) {
                    notify(notices[i].TITLE, notices[i].CONTENT);
                }
                $("#auditDg").datagrid("loadData", tasks);
            }, error: function (e) {
                if (e.responseJSON.error === "expired") {
                    Tip.warn("会话已超时或者已在别处登陆");
                    setTimeout(function () {
                        location.href = "./";
                    }, 1500);
                }
            }
        });
        setTimeout(loadTasks, 61000);
    }

    function auditRowClick(index, row) {
        addTab("我的审核", "icon-tip", "audit/task");
    }

    function formatNode(value, row, index) {
        if (value === 1) {
            return "一级审核";
        }
        if (value === 2) {
            return "二级审核";
        }
    }

    const path = "<%=site%>";
    const userId =${userId};
    const locale = "${locale}";
    //获取项目名称
    const projectName = "恒石纤维基业MES系统";
    $(document).ready(function () {
        $("#projectName").html(projectName);
        $("#locale").val(locale);
    });
    const loginPath = "<%=site%>login";
    const tipLoading = "<spring:message code="Tip.Loading" />";
    const tipSubmiting = "<spring:message code="Tip.Submiting" />";
    const tipInfo = "<spring:message code="Tip.Info" />";
    const tipYes = "<spring:message code="Tip.Yes" />";
    const tipNo = "<spring:message code="Tip.No" />";
    const tipLock = "<spring:message code="Tip.Lock" />";
    const tipUnlock = "<spring:message code="Tip.Unlock" />";
    const tipSuccess = "<spring:message code="Tip.Success" />";
    const tipError = "<spring:message code="Tip.Error" />";
    const tipConfirm = "<spring:message code="Tip.Confirm" />";
    const tipSelectATreeNode = "<spring:message code="Tip.SelectATreeNode" />";
    const tipSelectAtLeastOne = "<spring:message code="Tip.SelectAtLeastOne" />";
    const tipSelectOnlyOne = "<spring:message code="Tip.SelectOnlyOne" />";
    const buttonAddMore = "<spring:message code="Button.AddMore" />";

    function doChange() {
        $.cookie("ui", $("#style").val());
        if ($("#style").val() === "v1") {
            location.href = path;
        } else {
            location.href = path + "?ui=v2";
        }
    }

    function localeChange() {
        location.href = path + "language/localeChange?locale=" + $("#locale").val();
    }
</script>